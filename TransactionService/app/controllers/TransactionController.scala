package controllers

import models.AccountInfo
import play.api.libs.json.JsResult.Exception
import play.api.libs.json.{JsError, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, ControllerComponents}
import services.{KafkaProducerService, TransactionService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TransactionController @Inject()(ws: WSClient, cc: ControllerComponents, transactionService: TransactionService, kafkaProducerService: KafkaProducerService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

    def deposit() = Action.async(parse.json) { implicit request =>
      println("The request is "+ request);
      val accountId = (request.body \ "accountId").as[String]
      val updateUrl = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/updateBalance/$accountId"
      val accountInfoUrl = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/$accountId"
      println("URL is "+ updateUrl)

      val amount = (request.body \ "amount").as[String]
      println("Account is "+ accountId + " amount is "+ amount);

      val accountInfoFuture = ws.url(accountInfoUrl).get().map(res => res.json.as[AccountInfo])

      accountInfoFuture.flatMap { accountInfo =>
        println("The account info is " + accountInfo)

        val jsonPayload =  Json.obj(
          "balance" -> (amount.toDouble + accountInfo.balance).toString()
        )

        println("The json payload is "+ jsonPayload);

        ws.url(updateUrl).put(jsonPayload).map { response =>
          println("The response received is " + response)
          kafkaProducerService.sendMessage("CREDIT ", s"Deposited $amount to account ${accountId}")
          Ok(response.json)
        }
      }.recover {
        case ex: Exception =>
          println("An error occurred: " + ex.getMessage)
          InternalServerError("Something went wrong")
      }
    }

    def withdraw() = Action.async(parse.json) { implicit request =>
      println("The request is "+ request);
      val accountId = (request.body \ "accountId").as[String]
      val updateUrl = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/updateBalance/$accountId"
      val accountInfoUrl = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/$accountId"
      println("URL is "+ updateUrl)

      val amount = (request.body \ "amount").as[String]
      println("Account is "+ accountId + " amount is "+ amount);

      val accountInfoFuture = ws.url(accountInfoUrl).get().map(res => res.json.as[AccountInfo])

      accountInfoFuture.flatMap { accountInfo =>
        println("The account info is " + accountInfo)

        if(accountInfo.balance < amount.toDouble){
          Future.failed(new Exception(JsError("Insufficient Balance")))
        } else{
          val jsonPayload =  Json.obj(
            "balance" -> (accountInfo.balance - amount.toDouble).toString()
          )
          println("The json payload is "+ jsonPayload);

          ws.url(updateUrl).put(jsonPayload).map { response =>
            println("The response received is " + response)
            kafkaProducerService.sendMessage("DEBIT ", s"Withdrawn $amount to account ${accountId}")
            Ok(response.json)
          }
        }.recover {
          case ex: Exception =>
            println("An error occurred: " + ex.getMessage)
            InternalServerError("Something went wrong")
        }
        }
    }

    def transfer()= Action.async(parse.json) { implicit request =>
      val from_account_Id = (request.body \ "fromAccountId").as[String].toInt
      val to_account_Id = (request.body \ "toAccountId").as[String].toInt
      val amount = (request.body \ "amount").as[String].toDouble

      val senderBalanceUpdateURL = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/updateBalance/$from_account_Id"
      val receiverBalanceUpdateURL = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/updateBalance/$to_account_Id"
      val fromAccountInfoURL = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/$from_account_Id"
      val toAccountInfoURL = s"$ACCOUNTS_URL:$ACCOUNTS_PORT/accounts/$to_account_Id"

      val fromAccountInfoFuture = ws.url(fromAccountInfoURL).get().map(res => res.json.as[AccountInfo])

      fromAccountInfoFuture.flatMap { fromAccountInfo =>
        if (fromAccountInfo.balance < amount) {
          Future.failed(new Exception(JsError("Insufficient Balance")))
        } else {
          val updatedSenderBalance = fromAccountInfo.balance - amount
          val senderPayload = Json.obj("balance" -> updatedSenderBalance.toString)

          val updateSenderFuture = ws.url(senderBalanceUpdateURL).put(senderPayload)

          updateSenderFuture.flatMap { senderResponse =>
            ws.url(toAccountInfoURL).get().flatMap { toAccountInfoResponse =>
              val toAccountInfo = toAccountInfoResponse.json.as[AccountInfo]
              val updatedReceiverBalance = toAccountInfo.balance + amount
              val receiverPayload = Json.obj("balance" -> updatedReceiverBalance.toString)

              ws.url(receiverBalanceUpdateURL).put(receiverPayload).map { receiverResponse =>
                Ok(Json.obj("status" -> "success", "message" -> "Money transferred successfully"))
              }
            }
          }
        }
      }.recover {
        case ex: Exception =>
          println("An error occurred: " + ex.getMessage)
          InternalServerError(ex.getMessage)
      }
    }

  val ACCOUNTS_URL = "http://localhost"
  val ACCOUNTS_PORT = "9001"

  def getAllTransactions() = Action.async { implicit request =>
    println("Printing request body" + request.body);
    transactionService.listTransactions().map { transactions =>
      Ok(Json.toJson(transactions))
    }
  }

  def getAllTransfers() = Action.async { implicit request =>
    println("Printing request body" + request.body);
    transactionService.listTransfers().map { transfers =>
      Ok(Json.toJson(transfers))
    }
  }

}
