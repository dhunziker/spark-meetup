CREATE KEYSPACE IF NOT EXISTS meetup
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
  
CREATE TYPE IF NOT EXISTS meetup.event (
  event_id text,
  event_name text,
  event_url text,
  time bigint
);

CREATE TYPE IF NOT EXISTS meetup.group_topic (
  topic_name text,
  urlkey text
);

CREATE TYPE IF NOT EXISTS meetup.group (
  group_city text,
  group_country text,
  group_id bigint,
  group_lat double,
  group_lon double,
  group_name text,
  group_state text,
  group_topics set<frozen<group_topic>>,
  group_urlname text
);

CREATE TYPE IF NOT EXISTS meetup.other_service (
  identifier text
);

CREATE TYPE IF NOT EXISTS meetup.member (
  member_id bigint,
  member_name text,
  --Spark connector can't convert OtherService to UDT, but using text doesn't work for Spray
  --other_services map<text, text>,
  other_services map<text, frozen<other_service>>,
  photo text
);

CREATE TYPE IF NOT EXISTS meetup.venue (
  lat double,
  lon double,
  venue_id bigint,
  venue_name text
);

CREATE TABLE IF NOT EXISTS meetup.rsvp (
  event frozen<event>,
  group frozen<group>,
  guests bigint,
  member frozen<member>,
  mtime bigint,
  visibility text,
  response text,
  rsvp_id bigint,
  venue frozen<venue>,
  PRIMARY KEY (rsvp_id, mtime)
);