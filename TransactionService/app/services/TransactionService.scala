package services

import daos.{TransactionDao, TransferDao}
import models.{Transaction, Transfer}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionService @Inject()( transactionDao: TransactionDao, transferDao: TransferDao, kafkaProducerService: KafkaProducerService)(implicit ec: ExecutionContext) {

  def listTransactions(): Future[Seq[Transaction]] = {
    transactionDao.findAll();
  }

  def listTransfers(): Future[Seq[Transfer]] = {
    transferDao.findAll();
  }

}
