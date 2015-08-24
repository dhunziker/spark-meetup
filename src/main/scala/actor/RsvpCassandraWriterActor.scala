package actor

import org.apache.spark.SparkConf

import com.datastax.spark.connector.cql.CassandraConnector

import akka.actor.Actor
import akka.actor.ActorLogging

/**
 * @author Dennis
 */
class RsvpCassandraWriterActor(val conf: SparkConf) extends Actor with ActorLogging {

  def receive: Receive = {
    case msg: String => saveToCassandra(msg);
  }

  private def saveToCassandra(msg: String) {
    try {
      CassandraConnector(conf).withSessionDo { session =>
        val rsvp = msg.replaceAll("'", "")//.replaceAll("\"", "\\\"")
        session.execute("INSERT INTO meetup.rsvp JSON '" + rsvp + "'")
      }
    } catch {
      case e: Exception => log.error(e, "Failed to persists rsvp message to Cassandra")
    }
  }

}