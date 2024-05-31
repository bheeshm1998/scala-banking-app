package models

case class Transaction(id: Option[Int], accountId: Int, amount: Double, transactionType: String, timestamp: String);

import play.api.libs.json._

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] = Json.format[Transaction]
}
