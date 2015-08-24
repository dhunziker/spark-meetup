CREATE TABLE IF NOT EXISTS meetup.rsvps_by_city (
  city text,
  counter_value counter,
  PRIMARY KEY (city)
);