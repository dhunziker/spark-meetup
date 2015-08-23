package domain

/**
 * Cases classes for http://www.meetup.com/meetup_api/docs/stream/2/rsvps/
 *
 * NOTE: Member and RsvpResponse can't be used using Spark's RDD and saveToCassandra.
 * It seems as if there were some issues in the Spark connector library.
 *
 * @author Dennis
 */
case class Event(event_id: String, event_name: String, event_url: String, time: Option[Long])

case class GroupTopic(topic_name: String, urlkey: String)

case class Group(group_city: String, group_country: String, group_id: Long, group_lat: Double, group_lon: Double, group_name: String, group_state: Option[String], group_topics: List[GroupTopic], group_urlname: String)

case class OtherService(identifier: String)

case class Member(member_id: Long, member_name: String, other_services: Option[Map[String, OtherService]], photo: Option[String])
// Spark connector can't convert OtherService to UDT because it's part of a Map (CollectionConverter)
//case class Member(member_id: Long, member_name: String, other_services: Option[Map[String, String]], photo: Option[String])

case class Venue(lat: Double, lon: Double, venue_id: Long, venue_name: String)

case class RsvpResponse(event: Event, group: Group, guests: Long, member: Member, mtime: Long, visibility: String, response: String, rsvp_id: Long, venue: Option[Venue])
// Spark connector can't convert Option[Venue] to UDT
//case class RsvpResponse(event: Event, group: Group, guests: Long, member: Member, mtime: Long, visibility: String, response: String, rsvp_id: Long, venue: Venue)