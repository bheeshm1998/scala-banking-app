package models

import slick.jdbc.MySQLProfile.api._

class TransactionTable(tag: Tag) extends Table[Transaction](tag, "transaction") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def accountId = column[Int]("account_id")
  def amount = column[Double]("amount")
  def transactionType = column[String]("transaction_type")
  def timestamp = column[String]("timestamp")

  def * = (id.?, accountId, amount, transactionType, timestamp) <> ((Transaction.apply _).tupled, Transaction.unapply)
}

