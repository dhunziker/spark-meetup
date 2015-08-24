package http

import akka.actor.actorRef2Scala
import akka.actor.DeadLetter
import akka.io.IO
import spray.can.Http
import spray.can.server.UHttp
import spray.can.websocket
import spray.can.websocket.frame.Frame
import spray.http.HttpRequest

/**
 * @author Dennis
 */
abstract class WebSocketClient(connect: Http.Connect, val upgradeRequest: HttpRequest) extends websocket.WebSocketClientWorker {

  implicit val system = context.system

  IO(UHttp) ! connect

  def onMessage(response: String)

  def businessLogic: Receive = {
    case frame: Frame             => onMessage(frame.payload.utf8String)
    case _: Http.ConnectionClosed => context.stop(self)
  }

}