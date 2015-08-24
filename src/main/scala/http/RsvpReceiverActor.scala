package http

import java.util.concurrent.TimeUnit

import scala.concurrent.Await

import akka.actor.actorRef2Scala
import akka.util.Timeout
import domain.RsvpResponse
import domain.RsvpResponseJsonProtocol._
import spray.can.Http
import spray.http.HttpRequest
import spray.json.pimpString

/**
 * @author Dennis
 */
class RsvpReceiverActor(connect: Http.Connect, upgradeRequest: HttpRequest) extends WebSocketClient(connect, upgradeRequest) {

  implicit val timeout = Timeout(30, TimeUnit.SECONDS)

  val selection1 = system.actorSelection("/user/RsvpCassandraWriterActor")

  val cassandraWriter = Await.result(selection1.resolveOne, timeout.duration)
  
  val selection2 = system.actorSelection("/user/Supervisor0/SparkStreamWriterActor")

  val streamWriter = Await.result(selection2.resolveOne, timeout.duration)

  def onMessage(response: String) {
    val rsvp = response.parseJson.convertTo[RsvpResponse]
    cassandraWriter ! response
    streamWriter ! rsvp
  }

}