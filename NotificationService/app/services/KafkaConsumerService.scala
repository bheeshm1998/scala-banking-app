//package services
//
//
//import akka.actor.ActorSystem
//import akka.kafka.{ConsumerSettings, Subscriptions}
//import akka.kafka.scaladsl.Consumer
//import akka.stream.Materializer
//import akka.stream.scaladsl.{Keep, Sink}
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.common.serialization.StringDeserializer
//
//import scala.concurrent.Future
//import javax.inject.{Inject, Singleton}
//
//case class KafkMessage(value: String)
//
//@Singleton
//class KafkaConsumerService @Inject() (implicit system: ActorSystem, mat: Materializer)  {
////  try {
////    val config = ConfigFactory.load()
////    val topic = "transactions"
////
////    val props = new Properties()
////    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrap.servers"))
////    props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getString("kafka.consumer.group.id"))
////    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getString("kafka.consumer.enable.auto.commit"))
////    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, config.getString("kafka.consumer.auto.commit.interval.ms"))
////    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, config.getString("kafka.consumer.session.timeout.ms"))
////    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getString("kafka.consumer.key.deserializer"))
////    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getString("kafka.consumer.value.deserializer"))
////
////    val consumer = new KafkaConsumer[String, String](props)
////    consumer.subscribe(Collections.singletonList(topic))
////
////    println("Starting Kafka Consumer...")
////
////    while (true) {
////      val records: ConsumerRecords[String, String] = consumer.poll(1000)
////      for (record <- records.asScala) {
////        println(s"Received message: ${record.value()} from partition: ${record.partition()} at offset: ${record.offset()}")
////      }
////    }
////  } catch {
////    case e: Exception =>
////      println("Error in Kafka Consumer:")
////      e.printStackTrace()
////  }
//
//  println("INSIDE THE KAFAK CONSUMER SERVICE")
//
//
//  private val kafkaConsumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
//    .withBootstrapServers("localhost:9092")
//    .withGroupId("notificationGroup")
//    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
//
//
//    Consumer
//    .plainSource(kafkaConsumerSettings, Subscriptions.topics("transactions"))
//    .mapAsync(1) { msg =>
//      println("msg received in the consumer is  "+ msg)
//      val jsonVal = msg.value();
//      Future.successful(jsonVal)
//    }
//    .runWith(Sink.ignore)
//
////  def consumeMessages(topic: String): Future[Seq[KafkMessage]] = {
////    Consumer
////      .plainSource(kafkaConsumerSettings, Subscriptions.topics(topic))
////      .map(record => KafkMessage(record.value()))
////      .toMat(Sink.seq)(Keep.right)
////      .run()
////  }
//}


package services

import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, Sink}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.{ExecutionContext, Future}
import javax.inject.{Inject, Singleton}
import com.typesafe.config.ConfigFactory

@Singleton
class KafkaConsumerService @Inject() (implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext)  {

  println("INSIDE THE KAFKA CONSUMER SERVICE")

  private val config = ConfigFactory.load()
  private val kafkaConsumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers(config.getString("kafka.bootstrap.servers"))
    .withGroupId(config.getString("kafka.consumer.group.id"))
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // Start from the earliest offset
    .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.getString("kafka.consumer.enable.auto.commit"))
    .withProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, config.getString("kafka.consumer.auto.commit.interval.ms"))
    .withProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, config.getString("kafka.consumer.session.timeout.ms"))
    .withProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, config.getString("kafka.consumer.key.deserializer"))
    .withProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, config.getString("kafka.consumer.value.deserializer"))

  println("Kafka consumer settings initialized")

  Consumer
    .plainSource(kafkaConsumerSettings, Subscriptions.topics("transactions"))
    .mapAsync(1) { msg =>
      println(msg.key() + msg.value())
      val logFile = "transactions.log"

      // Example of appending messages
      val logMsg: String = s"${msg.key()} ${msg.value()}"
      TransactionLogging.appendToFile(logFile,  logMsg);
      val jsonVal = msg.value()
      Future.successful(jsonVal)
    }
    .runWith(Sink.ignore)
    .onComplete {
      case scala.util.Success(_) => println("Kafka consumer started successfully")
      case scala.util.Failure(e) => println("Failed to start Kafka consumer: " + e.getMessage)
    }
}
