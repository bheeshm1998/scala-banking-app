package models

case class Account (id: Option[Int], accountType: String, userId: String, balance: Double, createdAt: Option[String] )

import play.api.libs.json._

object Account {
  implicit val accountFormat: OFormat[Account] = Json.format[Account]
}
