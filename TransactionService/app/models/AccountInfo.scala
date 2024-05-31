package models

import play.api.libs.json._

case class AccountInfo(id: Int, accountType: String, userId: String, balance: Double)

// Define implicit Reads and Writes for the case class
object AccountInfo {
  implicit val accountInfoReads: Reads[AccountInfo] = Json.reads[AccountInfo]
  implicit val accountInfoWrites: Writes[AccountInfo] = Json.writes[AccountInfo]
}

