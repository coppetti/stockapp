package com.marionete.stock.messaging

/**
  * Created by matheussilveira on 02/08/2016.
  */


import java.util.{Properties}
import kafka.utils.VerifiableProperties
import kafka.consumer.{ConsumerConfig, Consumer}
import io.confluent.kafka.serializers.{KafkaAvroDecoder}

object KafkaConsumer {
  var readTopic: String = "stocks"
  val group = "group-1"

  val broker = "localhost:9092"
  val zookeeper = "localhost:2181"
  val schemaRepo = "http://localhost:8081"

  val consumerProps = new Properties()
  consumerProps.put("zookeeper.connect", zookeeper)
  consumerProps.put("group.id", group)
  consumerProps.put("auto.offset.reset", "smallest")
  consumerProps.put("schema.registry.url", schemaRepo)

  val consumerVerifiableProps = new VerifiableProperties(consumerProps)
  val keyDecoder = new KafkaAvroDecoder(consumerVerifiableProps)
  val valueDecoder = new KafkaAvroDecoder(consumerVerifiableProps)

  lazy val consumerIterator = Consumer.create(new ConsumerConfig(consumerProps)).createMessageStreams(Map(readTopic -> 1), keyDecoder, valueDecoder).get(readTopic).get(0).iterator()

  def main(args: Array[String]): Unit = {
    var count = 0
    while (true) {
      println(consumerIterator.next().message().toString(),count)
      count +=1
    }
  }
}