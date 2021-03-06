package com.marionete.stock.exchange

import java.net.URL

import com.marionete.stock.domain.BuyIntent
import com.marionete.stock.messaging.{AddFundsRunnable, AvroKafkaProducer, KafkaConfig, KafkaConsumer}
import kafka.consumer.ConsumerIterator

/**
  * Created by matheussilveira on 05/08/2016.
  */
class BuyStocks {
  def buyStocks() = ???

  //Problably will be replicated in other classes, should move it to another place (consumer class)
  def startConsuming(readTopic: String, group: String, broker: String, zookeeper: String,
                     schemaRepo: String): ConsumerIterator[AnyRef, AnyRef] = {
    new KafkaConsumer(readTopic, group, broker, zookeeper, schemaRepo).startConsumer
  }
}

object BuyStocks {
  def main(args: Array[String]): Unit = {
    val buyer = new BuyStocks()
    val consumerIterator = buyer.startConsuming("buying", "group-1", "localhost:9092", "localhost:2181",
      "http://localhost:8081")
    val fundsRunnable = new AddFundsRunnable(consumerIterator)
    new Thread(fundsRunnable).start()
  }
}
