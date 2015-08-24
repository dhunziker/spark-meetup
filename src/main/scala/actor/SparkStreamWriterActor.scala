package actor

import org.apache.spark.streaming.receiver.ActorHelper
import akka.actor.Actor
import domain.RsvpResponse

/**
 * @author Dennis
 */
class SparkStreamWriterActor extends Actor with ActorHelper {

  def receive: Receive = {
    case rsvp: RsvpResponse => store(rsvp)
    case _ => println("random message")
  }

}