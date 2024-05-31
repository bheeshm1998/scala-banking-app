package models

import slick.jdbc.MySQLProfile.api._

class AccountTable (tag: Tag) extends Table[Account](tag, "account") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def accountType = column[String]("account_type")
  def username = column[String]("username")
  def balance = column[Double]("balance")
  def createdAt = column[String]("created_at")

  def * = (id.?, accountType, username, balance, createdAt.?) <> ((Account.apply _).tupled, Account.unapply)
}