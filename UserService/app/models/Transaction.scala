package models

case class Transaction(id: Int, amount: Int, transactionType: String, from: String, to: String)
