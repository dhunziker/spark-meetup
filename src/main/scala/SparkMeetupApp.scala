import scala.reflect.runtime.universe
import com.datastax.spark.connector._

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkEnv
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext

import com.datastax.spark.connector.streaming.toDStreamFunctions

import AppConfig.SparkConfig.CassandraConnectionHost
import AppConfig.SparkConfig.SparkMaster
import AppConfig.SparkConfig.SparkStreamingBatchInterval
import actor.DeadLetterActor
import actor.RsvpCassandraWriterActor
import akka.actor.DeadLetter
import akka.actor.Props
import actor.SparkStreamWriterActor
import domain.RsvpResponse
import http.RsvpReceiverActor
import spray.can.Http
import spray.http.HttpHeaders
import spray.http.HttpMethods
import spray.http.HttpRequest
import spray.http.Uri.apply

/**
 * @author Dennis
 */
object SparkMeetupApp extends App {

  println("Starting application...")

  val conf = new SparkConf(true)
    .setMaster(SparkMaster)
    .setAppName(getClass.getSimpleName)
    .set("spark.cores.max", 4.toString)
    .set("spark.cassandra.connection.host", CassandraConnectionHost)

  val sc = new SparkContext(conf)

  val ssc = new StreamingContext(sc, Seconds(SparkStreamingBatchInterval))

  val stream = ssc.actorStream[RsvpResponse](Props(new SparkStreamWriterActor), "SparkStreamWriterActor")

  //stream.count().print()
  
  stream.map(rsvp => (rsvp.group.group_city, rsvp.guests + 1)).reduceByKey(_ + _).saveToCassandra("meetup", "rsvps_by_city")

  ssc.start()

  implicit val system = SparkEnv.get.actorSystem

  // Creating all actors
  val deadLetterActor = system.actorOf(Props(new DeadLetterActor()), "DeadLetterActor")

  val rsvpCassandraWriterActor = system.actorOf(Props(new RsvpCassandraWriterActor(conf)), "RsvpCassandraWriterActor")

  println("Sleep for 5 seconds before starting to read from WebSocket")
  Thread.sleep(5000)

  // Create actor to retrieve data via WebSocket
  val ssl = false
  val agent = "spray-websocket-client" + (if (ssl) "-ssl" else "-basic")
  val host = "stream.meetup.com"
  val port = 80
  val headers = List(
    HttpHeaders.Host(host, port),
    HttpHeaders.Connection("Upgrade"),
    HttpHeaders.RawHeader("Upgrade", "websocket"),
    HttpHeaders.RawHeader("Sec-WebSocket-Version", "13"),
    HttpHeaders.RawHeader("Sec-WebSocket-Key", "x3JJHMbDL1EzLkh9GBhXDw=="),
    HttpHeaders.RawHeader("Sec-WebSocket-Extensions", "permessage-deflate"))

  system.actorOf(Props(new RsvpReceiverActor(
    Http.Connect(host, port, ssl),
    HttpRequest(HttpMethods.GET, "/2/rsvps", headers))), "RsvpWebSocketReceiver")

  ssc.awaitTermination()

  println("Application stopped")
}