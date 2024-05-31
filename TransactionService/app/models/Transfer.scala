package models

case class Transfer (id: Option[Int], fromAccountId: Int, toAccountId: Int, transactionId: Int)

import play.api.libs.json._

object Transfer {
  implicit val transferFormat: OFormat[Transfer] = Json.format[Transfer]
}
