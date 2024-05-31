package controllers

import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import services.AccountService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AccountController @Inject()(cc: ControllerComponents, accountService: AccountService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def createAccount() = Action.async(parse.json) { implicit request =>
    println("Inside the controller method ")
    val accountType = (request.body \ "accountType").as[String]
    val username = (request.body \ "username").as[String]

    accountService.createAccount(username, accountType).map { account =>
      //      Created(s"User created with ID: ${user.id.get}")
      Created(Json.toJson(account))
    }
  }

  def getAccount(id: Int) = Action.async { implicit request =>
    accountService.getAccount(id).map {
      //      case Some(user) => Ok(s"User found: ${user.name}, ${user.email}, ${user.phoneNumber}, ${user.role}")
      case Some(account) => Ok(Json.toJson(account))
      case None => NotFound("Account not found")
    }
  }

  def updateBalance(id: Int) = Action.async(parse.json) { implicit request =>
    println("request is "+ request + " request body " + request.body);
    val balance= (request.body \ "balance").as[String]
    println("Balance string " + balance)
    val balanceInDouble = balance.toDouble
    println("Balance in double " + balanceInDouble);
    println("Request for updating balance "+ balanceInDouble);
    accountService.updateBalance(id, balanceInDouble).map { result =>
      if (result > 0) Ok(Json.toJson(result))
      else NotFound("User not found")
    }
  }

  def deleteAccount(id: Int) = Action.async { implicit request =>
    accountService.deleteAccount(id).map { result =>
      if (result > 0) Ok(s"Account deleted with ID: $id")
      else NotFound("Account not found")
    }
  }

  def listAccounts() = Action.async { implicit request =>
    accountService.listAccounts().map { accounts =>
      Ok(Json.toJson(accounts))
    }
  }
}

//  def getUserTransaactions(id: Int) = Action.async { implicit request =>
//    userService.getUserTransactions(id).map {
//      case Some(user) => Ok(s"Trnsactions:  found: ${user.name}, ${user.email}, ${user.phoneNumber}, ${user.role}")
//      case None => NotFound("No transactions found for user")
//    }
//  }
