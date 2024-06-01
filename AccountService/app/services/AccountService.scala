package services

import daos.AccountDAO
import models.Account

import java.sql.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountService @Inject()(accountDAO: AccountDAO)(implicit ec: ExecutionContext) {

  def createAccount(userId: String, accountType: String): Future[Account] = {
    val account = Account(None, accountType, userId, 0, None);
    accountDAO.create(account)
  }

  def updateBalance(id: Int, balance: Double): Future[Int] = {
//    val updatedAccount = Account(Some(id), name, email, password, phoneNumber, role)
    accountDAO.update(id, balance);
  }

  def deleteAccount(id: Int): Future[Int] = {
    accountDAO.delete(id)
  }

  def getAccount(id: Int): Future[Option[Account]] = {
    accountDAO.findById(id)
  }

  def getAccountIdFromUserId(email: String): Future[Option[Account]] ={
    accountDAO.findAccountByUserEmail(email);
  }

  def listAccounts(): Future[Seq[Account]] = {
    accountDAO.findAll()
  }

  //  def getAccountTransactions(id: Int): Future[Seq[Transaction]] = {
  //
  //  }
}

