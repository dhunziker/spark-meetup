package domain

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import domain.RsvpResponseJsonProtocol.rsvpResponseFormat
import spray.json.pimpAny
import spray.json.pimpString

/**
 * @author Dennis
 */
class RsvpResponseJsonSpec extends FlatSpec with Matchers {

  val jsonString = """{"event":{"event_id":"224258725","event_name":"HackTheTower & Adopt Openjdk - September 2015 hack day","event_url":"http://www.meetup.com/Londonjavacommunity/events/224258725/","time":1442048400000},"guests":0,"rsvp_id":1566928812,"response":"yes","mtime":1440350914925,"member":{"member_id":102766142,"member_name":"Dennis Hunziker","other_services":{"twitter":{"identifier":"@dennishunziker"},"facebook":{"identifier":"https://www.facebook.com/dennis.hunziker"}},"photo":"http://photos4.meetupstatic.com/photos/member/2/0/d/c/thumb_160388412.jpeg"},"visibility":"public","group":{"group_id":841735,"group_topics":[{"topic_name":"Computer programming","urlkey":"computer-programming"},{"topic_name":"Agile Project Management","urlkey":"agile-project-management"},{"topic_name":"Open Source","urlkey":"opensource"},{"topic_name":"Technology","urlkey":"technology"},{"topic_name":"Software Development","urlkey":"softwaredev"},{"topic_name":"Java","urlkey":"java"},{"topic_name":"Web Development","urlkey":"web-development"}],"group_urlname":"Londonjavacommunity","group_lat":51.52,"group_city":"London","group_name":"LJC - London Java Community","group_lon":-0.1,"group_country":"gb"},"venue":{"lat":51.516247,"lon":-0.080935,"venue_id":21376342,"venue_name":"Salesforce London Office - 26th / 27th Floors, Salesforce Tower"}}"""

  val event = Event("224258725", "HackTheTower & Adopt Openjdk - September 2015 hack day", "http://www.meetup.com/Londonjavacommunity/events/224258725/", Option(1442048400000L))

  val groupTopics = List(GroupTopic("Computer programming", "computer-programming"),
    GroupTopic("Agile Project Management", "agile-project-management"),
    GroupTopic("Open Source", "opensource"),
    GroupTopic("Technology", "technology"),
    GroupTopic("Software Development", "softwaredev"),
    GroupTopic("Java", "java"),
    GroupTopic("Web Development", "web-development"))

  val group = Group("London", "gb", 841735, 51.52, -0.1, "LJC - London Java Community", None, groupTopics, "Londonjavacommunity")

  val otherServices = Map(
    "twitter" -> OtherService("@dennishunziker"),
    "facebook" -> OtherService("https://www.facebook.com/dennis.hunziker"))
  val member = Member(102766142, "Dennis Hunziker", Option(otherServices), Option("http://photos4.meetupstatic.com/photos/member/2/0/d/c/thumb_160388412.jpeg"))

  val venue = Venue(51.516247, -0.080935, 21376342, "Salesforce London Office - 26th / 27th Floors, Salesforce Tower")

  val rsvpResponse = RsvpResponse(event, group, 0, member, 1440350914925L, "public", "yes", 1566928812, Option(venue))

  "spray" should "deserialize JSON to case classes" in {
    jsonString.parseJson.convertTo[RsvpResponse] should be(this.rsvpResponse)
  }

  "spray" should "serialize case classes to JSON" in {
    rsvpResponse.toJson.toString should be(jsonString)
  }

}