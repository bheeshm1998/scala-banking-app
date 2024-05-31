//package controllers
//
//import play.api.libs.json.Json
//import play.api.mvc.{AbstractController, AnyContent, BaseController, ControllerComponents, Request}
//import services.KafkaConsumerService
//
//import javax.inject.{Inject, Singleton}
//import scala.concurrent.ExecutionContext
//
//@Singleton
//class KafkaController @Inject()(val cc: ControllerComponents, kafkaConsumerService: KafkaConsumerService) extends BaseController {
//
//  def kafkaConsumer() = Action { implicit request =>
//    kafkaConsumerService.consumeMessages("transactions").onComplete(msg => println(msg))
//    Ok("Kafka success")
//  }
//
//  def index() = Action { implicit request: Request[AnyContent] =>
//    Ok(views.html.index())
//  }
//}
//
//
//
//
//
//
//
//  //  override protected def controllerComponents: ControllerComponents = ???
//}
