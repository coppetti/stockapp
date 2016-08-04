package com.marionete.stock.user


import com.marionete.stock.messaging._
import kafka.consumer.ConsumerIterator


/**
  * Created by matheussilveira on 04/08/2016.
  */

class User(name: String, id: String) {

  def startConsuming(readTopic: String, group: String, broker: String, zookeeper: String,
                     schemaRepo: String): ConsumerIterator[AnyRef, AnyRef] = {
    new KafkaConsumer(readTopic, group, broker, zookeeper, schemaRepo).startConsumer
  }

  def publishBuyIntent: Unit = {

  }
}


object User {

  def main(args: Array[String]): Unit = {
    val user = new User("Matheus", "19861001")
    val consumerIterator = user.startConsuming("stocks", "group-1", "localhost:9092", "localhost:2181",
      "http://localhost:8081")
    val stockRunnable = new StockConsumerRunnable(consumerIterator)
    new Thread(stockRunnable).start()
    Thread.sleep(10000)
    println(stockRunnable.getStock("Yahoo! Inc.").toString())
  }
}
