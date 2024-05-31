package modules

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.google.inject.AbstractModule
import services.KafkaConsumerService

class KafkaModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystem]).toInstance(ActorSystem("myActorSystem"))
    bind(classOf[Materializer]).toInstance(ActorMaterializer()(ActorSystem("myActorSystem")))
    bind(classOf[KafkaConsumerService]).asEagerSingleton()
  }
}
