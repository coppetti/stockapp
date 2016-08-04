package com.marionete.stock.messaging

/**
  * Created by matheussilveira on 02/08/2016.
  */


import java.util.Properties

import kafka.utils.VerifiableProperties
import kafka.consumer.{Consumer, ConsumerConfig, ConsumerIterator}
import io.confluent.kafka.serializers.KafkaAvroDecoder
import com.marionete.stock.domain._



class KafkaConsumer(readTopic:String,group:String,broker:String,zookeeper:String,
                    schemaRepo:String){

  val consumerProps = new Properties()
  consumerProps.put("zookeeper.connect", zookeeper)
  consumerProps.put("group.id", group)
  consumerProps.put("auto.offset.reset", "smallest")
  consumerProps.put("schema.registry.url", schemaRepo)

  val consumerVerifiableProps = new VerifiableProperties(consumerProps)
  val keyDecoder = new KafkaAvroDecoder(consumerVerifiableProps)
  val valueDecoder = new KafkaAvroDecoder(consumerVerifiableProps)

  def startConsumer: ConsumerIterator[AnyRef, AnyRef] ={
    Consumer.create(new ConsumerConfig(consumerProps)).
      createMessageStreams(Map(readTopic -> 1), keyDecoder, valueDecoder).get(readTopic).get(0).iterator()
  }


}


object KafkaConsumer {
  def main(args: Array[String]): Unit = {
    val consumer = new KafkaConsumer("stocks","group-1","localhost:9092","localhost:2181",
                                      "http://localhost:8081").startConsumer
    while(true){
      consumer.next().message().toString match{
        case msg if (msg.contains("Apple")) => println(msg.toString)
        case _ => //println(consumer.next().message().toString)
      }
    }
  }
}