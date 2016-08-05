package com.marionete.stock.accmanager

import com.marionete.stock.messaging._
import kafka.consumer.ConsumerIterator

/**
  * Created by matheussilveira on 05/08/2016.
  */
class AccountManager {

  def addFunds() = ???

  //Problably will be replicated in other classes, should move it to another place (consumer class)
  def startConsuming(readTopic: String, group: String, broker: String, zookeeper: String,
                     schemaRepo: String): ConsumerIterator[AnyRef, AnyRef] = {
    new KafkaConsumer(readTopic, group, broker, zookeeper, schemaRepo).startConsumer
  }
}

object AccountManager {

  def main(args: Array[String]): Unit = {
    val amanager = new AccountManager()
    val consumerIterator = amanager.startConsuming("funds", "group-1", "localhost:9092", "localhost:2181",
      "http://localhost:8081")
    val fundsRunnable = new AddFundsRunnable(consumerIterator)
    new Thread(fundsRunnable).start()
  }
}
