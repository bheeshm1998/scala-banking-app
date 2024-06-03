package daos

import models.{Transaction, TransactionTable, Transfer, TransferTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.MySQLProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransferDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[MySQLProfile]
  import dbConfig._
  import profile.api._

  val transfers = TableQuery[TransferTable]

  def findAll(): Future[Seq[Transfer]] = {
    db.run(transfers.result)
  }

  def insert(transfer: Transfer): Future[Int] = db.run((transfers returning transfers.map(_.id)) += transfer)

}
