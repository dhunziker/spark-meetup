import scala.sys.process._
import akka.japi.Util.immutableSeq
import com.typesafe.config.ConfigFactory

/**
 * @author Dennis
 */
object AppConfig {

  private val config = ConfigFactory.load()

  private val root = config.getConfig("meetup-queries")

  object MeetupConfig {
    private val meetupConfig = root.getConfig("meetup")

    val MeetupHost = meetupConfig.getString("host")

    val MeetupPort = meetupConfig.getInt("port")

    val MeetupRsvpUrl = meetupConfig.getString("rsvp.url")
  }

  object SparkConfig {
    private val sparkConfig = root.getConfig("spark")

    val SparkMaster = sparkConfig.getString("master")

    val SparkStreamingBatchInterval = sparkConfig.getInt("streaming.batch.interval")

    private val cassandraConfig = sparkConfig.getConfig("cassandra")

    val CassandraConnectionHost = cassandraConfig.getString("connection.host")
  }

  /** Attempts to detect System property, falls back to config. */
  // Detect Spark Master and Cassandra entry point using dsetool
  //  val SparkMaster: String = try { "dsetool sparkmaster".!!.trim }
  //      catch { case x:Exception => sys.props.get("spark.master").getOrElse(config.getString("spark.master")) }
  //
  //  val StreamingBatchInterval = config.getInt("spark.streaming.batch.interval")
  //
  //  val SparkExecutorMemory = config.getBytes("spark.executor.memory")
  //
  //  val SparkCoresMax = sys.props.get("spark.cores.max").getOrElse(config.getInt("spark.cores.max"))
  //
  //  val DeployJars: Seq[String] = immutableSeq(
  //    config.getStringList("spark.jars")).filter(new java.io.File(_).exists)
  //
  //  /** Attempts to detect System property, falls back to config,
  //    * to produce a comma-separated string of hosts. */
  //  val CassandraSeedNodes: String = sys.props.get("spark.cassandra.connection.host") getOrElse
  //        immutableSeq(config.getStringList("spark.cassandra.connection.host")).mkString(",")
  //
  //  val CassandraKeyspace: String = config.getString("spark.cassandra.keyspace")
  //
  //  val CassandraTable: String = config.getString("spark.cassandra.table")
}