package services

import daos.{TransactionDao, TransferDao}
import models.{Transaction, Transfer}

import java.time.LocalDateTime
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

  def createTransaction(accountId: Int, amount: Double, transactionType: String): Future[Int] = {
    val timestamp = LocalDateTime.now().toString
    val transaction = Transaction(None, accountId, amount, transactionType, timestamp)
    transactionDao.insert(transaction)
  }

  def createTransfer(fromAccountId: Int, toAccountId: Int, transactionId: Int): Future[Int] = {
    val transfer = Transfer(None, fromAccountId, toAccountId,transactionId)
    transferDao.insert(transfer)
  }

}
