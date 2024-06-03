package services
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties
import com.typesafe.config.{Config, ConfigFactory}

import javax.inject.{Inject, Singleton}

@Singleton
class KafkaProducerService @Inject()(config: Config) {
//  val config = ConfigFactory.load()
  val topic = "transactions"

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.bootstrap.servers"))
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, config.getString("kafka.producer.key.serializer"))
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, config.getString("kafka.producer.value.serializer"))
  props.put(ProducerConfig.ACKS_CONFIG, config.getString("kafka.producer.acks"))
  props.put(ProducerConfig.RETRIES_CONFIG, config.getString("kafka.producer.retries"))
  props.put(ProducerConfig.BATCH_SIZE_CONFIG, config.getString("kafka.producer.batch.size"))
  props.put(ProducerConfig.LINGER_MS_CONFIG, config.getString("kafka.producer.linger.ms"))
  props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, config.getString("kafka.producer.buffer.memory"))

  val producer = new KafkaProducer[String, String](props)

  def sendMessage(key: String, value: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, value)
    producer.send(record)
  }
}
