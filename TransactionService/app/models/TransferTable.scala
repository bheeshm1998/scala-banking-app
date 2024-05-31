package models

import slick.jdbc.MySQLProfile.api._

class TransferTable(tag: Tag) extends Table[Transfer](tag, "transfer") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def fromAccountId = column[Int]("from_account_id")
  def toAccountId = column[Int]("to_account_id")
  def transactionId = column[Int]("transaction_id")

  def * = (id.?, fromAccountId, toAccountId, transactionId) <> ((Transfer.apply _).tupled, Transfer.unapply)
}
