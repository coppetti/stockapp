package com.marionete.stock.messaging

/**
  * Created by matheussilveira on 02/08/2016.
  */


import java.util.Properties

import kafka.utils.VerifiableProperties
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerIterator}
import io.confluent.kafka.serializers.KafkaAvroDecoder
import scala.util.parsing.json._


class BuyStockRunnable(consumerIterator: ConsumerIterator[AnyRef, AnyRef]) extends Runnable {

  override def run(): Unit = {


    while (true) {
      val json: Option[Any] = JSON.parseFull(consumerIterator.next().message().toString)
      json match {
        case Some(e: Map[String, _]) => println(e)
        case _ => //Nothing Happens
      }
    }
  }


  def buyStock(stock: String): (String, String) = ???

}


class AddFundsRunnable(consumerIterator: ConsumerIterator[AnyRef, AnyRef]) extends Runnable {

  override def run(): Unit = {


    while (true) {
      val json: Option[Any] = JSON.parseFull(consumerIterator.next().message().toString)
      json match {
        case Some(e: Map[String, _]) => println(e)
        case _ => //Nothing Happens
      }
    }
  }


  def addFunds(stock: String): (String, String) = ???

}


class StockConsumerRunnable(consumerIterator: ConsumerIterator[AnyRef, AnyRef]) extends Runnable {

  val lastQuotes: scala.collection.mutable.Map[String, Tuple2[String, String]] = scala.collection.mutable.Map()

  override def run(): Unit = {


    while (true) {
      val json: Option[Any] = JSON.parseFull(consumerIterator.next().message().toString)
      json match {
        case Some(e: Map[String, _]) => {
          if (!lastQuotes.contains(e("symbol").toString)) {
            lastQuotes.put(e("symbol").toString, (e("buyingPrice").toString,
              e("sellingPrice").toString))
          }
          else {
            lastQuotes(e("symbol").toString) = (e("buyingPrice").toString, e("sellingPrice").toString)
          }
        }
      }
    }
  }


  def getStock(stock: String): (String, String) = lastQuotes(stock)

}


class KafkaConsumer(readTopic: String, group: String, broker: String, zookeeper: String,
                    schemaRepo: String) {

  val consumerProps = new Properties()
  consumerProps.put("zookeeper.connect", zookeeper)
  consumerProps.put("group.id", group)
  consumerProps.put("auto.offset.reset", "smallest")
  consumerProps.put("schema.registry.url", schemaRepo)

  val consumerVerifiableProps = new VerifiableProperties(consumerProps)
  val keyDecoder = new KafkaAvroDecoder(consumerVerifiableProps)
  val valueDecoder = new KafkaAvroDecoder(consumerVerifiableProps)

  def startConsumer: ConsumerIterator[AnyRef, AnyRef] = {
    Consumer.create(new ConsumerConfig(consumerProps)).
      createMessageStreams(Map(readTopic -> 1), keyDecoder, valueDecoder).get(readTopic).get.last.iterator()
  }

}

object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    val consumer = new KafkaConsumer("stocks", "group-1", "localhost:9092", "localhost:2181",
      "http://localhost:8081").startConsumer
    val stockRunnable = new StockConsumerRunnable(consumer)
    new Thread(stockRunnable).start()
    Thread.sleep(10000)
    println(stockRunnable.getStock("Yahoo! Inc.").toString())
  }
}


