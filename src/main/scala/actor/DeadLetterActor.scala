package actor

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.DeadLetter

/**
 * @author Dennis
 */
class DeadLetterActor extends Actor with ActorLogging {

  // Let this actor register itself for DeadLetter events
  context.system.eventStream.subscribe(self, classOf[DeadLetter])

  def receive: Receive = {
    case DeadLetter(msg, from, to) => log.error(s"Dead letter from $from to $to: $msg")
  }

}