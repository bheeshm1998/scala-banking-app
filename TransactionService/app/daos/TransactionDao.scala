package daos

import models.{TransactionTable, Transaction}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.MySQLProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[MySQLProfile]
  import dbConfig._
  import profile.api._

  val transactions = TableQuery[TransactionTable]

  def findAll(): Future[Seq[Transaction]] = {
    db.run(transactions.result)
  }



}
