package modules

import com.google.inject.AbstractModule
import services.{KafkaProducerService, TransactionService}

import java.time.Clock

class KafkaModule extends AbstractModule {
  override def configure() = {
    bind(classOf[KafkaProducerService]).asEagerSingleton()
    bind(classOf[TransactionService]).asEagerSingleton()
  }
}
