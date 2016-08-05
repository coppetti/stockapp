package com.marionete.stock.user


import java.net.URL

import com.marionete.stock.domain.{BuyIntent, Founds}
import com.marionete.stock.messaging._
import kafka.consumer.ConsumerIterator


/**
  * Created by matheussilveira on 04/08/2016.
  */

class User(name: String, id: String) {

  //Problably will be replicated in other classes, should move it to another place (consumer class)
  def startConsuming(readTopic: String, group: String, broker: String, zookeeper: String,
                     schemaRepo: String): ConsumerIterator[AnyRef, AnyRef] = {
    new KafkaConsumer(readTopic, group, broker, zookeeper, schemaRepo).startConsumer
  }
  //This method should not be here, will be moved soon :D
  def extractDouble(str: String): Double = {
    str.split(" ").map(s => if (s.replace(".", "").forall(_.isDigit)) s).last.toString.toDouble
  }

  def publishBuyIntent(stock: String, buyingPrice: String, qty: Int): Unit = {
    val brokers = Seq("localhost:9092")
    val schemaUrl = new URL("http://localhost:8081")
    val buyProducer = new AvroKafkaProducer[BuyIntent]("buying", new KafkaConfig(brokers, schemaUrl))
    buyProducer.send(new BuyIntent(this.name, stock, extractDouble(buyingPrice), qty), this.id)
  }

  def addFounds(user: String, qty: Double): Unit = {
    val brokers = Seq("localhost:9092")
    val schemaUrl = new URL("http://localhost:8081")
    val foundAdder = new AvroKafkaProducer[Founds]("founds", new KafkaConfig(brokers, schemaUrl))
    foundAdder.send(new Founds(this.name, qty), this.id)
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
