package domain

import spray.json.DefaultJsonProtocol

/**
 * JSON protocol definition used by Spray to convert JSON into case classes.
 * The custom RsvpResponseFormat was an attempt to circumvent the limitations in the Spark connector,
 * however, custom deserialization of a Map containing OtherService would have gotten too complex.
 *
 * @author Dennis
 */
object RsvpResponseJsonProtocol extends DefaultJsonProtocol {

  implicit val eventFormat = jsonFormat4(Event)

  implicit val groupTopicFormat = jsonFormat2(GroupTopic)

  implicit val groupFormat = jsonFormat9(Group)

  /* Spark connector can't convert OtherService to UDT, but Spray, by default can't deserialize key/value object to String */
  implicit val otherServiceFormat = jsonFormat1(OtherService)

  implicit val memberFormat = jsonFormat4(Member)

  implicit val venueFormat = jsonFormat4(Venue)

  implicit val rsvpResponseFormat = jsonFormat9(RsvpResponse)

  /* Spark connector can't convert Option[Venue] to UDT but Spray, by default, can't deserialize JSON if venue is missing but not declared as Option */
  /*implicit object RsvpResponseFormat extends JsonFormat[RsvpResponse] {
    def read(json: JsValue): RsvpResponse =
      json.asJsObject.getFields("event", "group", "guests", "member", "mtime", "visibility", "response", "rsvp_id", "venue") match {
        case Vector(event, group, JsNumber(guests), member, JsNumber(mtime), JsString(visibility), JsString(response), JsNumber(rsvp_id)) => toRsvpResponse(event, group, guests, member, mtime, visibility, response, rsvp_id, null)
        case Vector(event, group, JsNumber(guests), member, JsNumber(mtime), JsString(visibility), JsString(response), JsNumber(rsvp_id), venue) => toRsvpResponse(event, group, guests, member, mtime, visibility, response, rsvp_id, venue)
        case _ => deserializationError("RsvpResponse expected")
      }
    def write(obj: RsvpResponse): spray.json.JsValue = obj.toJson
  }

  private def toRsvpResponse(event: JsValue, group: JsValue, guests: BigDecimal, member: JsValue, mtime: BigDecimal, visibility: String, response: String, rsvp_id: BigDecimal, venue: JsValue) = {
    new RsvpResponse(eventFormat.read(event),
        groupFormat.read(group),
        guests.toLong,
        memberFormat.read(member),
        mtime.toLong, visibility,
        response, rsvp_id.toLong,
        if (venue != null) venueFormat.read(venue) else Venue(0, 0, 0, null))
  }*/

}