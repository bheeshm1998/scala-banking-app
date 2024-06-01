package daos

import models.{Account, AccountTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.MySQLProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  val dbConfig = dbConfigProvider.get[MySQLProfile]
  import dbConfig._
  import profile.api._

  val accounts = TableQuery[AccountTable]

  def create(account: Account): Future[Account] = {
    val insertQuery = (accounts returning accounts.map(_.id)
      into ((account, id) => account.copy(id = Some(id)))
      ) += account
    db.run(insertQuery)
  }

  def findById(id: Int): Future[Option[Account]] = {
    db.run(accounts.filter(_.id === id).result.headOption)
  }

  def update(id: Int, newBalance: Double): Future[Int] = {
//    db.run(accounts.filter(_.id === account.id).update(account))
    val query = accounts.filter(_.id === id).map(_.balance).update(newBalance)
    db.run(query)
  }

  def delete(id: Int): Future[Int] = {
    db.run(accounts.filter(_.id === id).delete)
  }

  def findAll(): Future[Seq[Account]] = {
    db.run(accounts.result)
  }

  def findAccountByUserEmail(email: String): Future[Option[Account]] = {
    println("the email to find is " + email);
    val query = accounts.filter(_.username === email).result.headOption
    val queryResult = db.run(query)
    queryResult.onComplete(println)
    queryResult
  }

}
