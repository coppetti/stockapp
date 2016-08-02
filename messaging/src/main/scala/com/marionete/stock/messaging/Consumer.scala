package com.marionete.stock.messaging

/**
  * Created by matheussilveira on 02/08/2016.
  */
import java.util.Properties

import kafka.consumer.{ConsumerConfig, Consumer}


class KafkaConsumer {
  val topic = "test"
  val consumer = Consumer.create(consumerConfig())

  def consumerConfig (): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", "localhost:9092")
    props.put("group.id", "stocks")
    props.put("zookeeper.session.timeout.ms", "10000")
    props.put("zookeeper.sync.time.ms", "200")
    props.put("auto.commit.interval.ms", "200")
    props.put("auto.offset.reset", "smallest")
    new ConsumerConfig(props)
  }

  def read(): Unit = {
    val streamsMap = consumer.createMessageStreams(Map(topic->1))
    val streams = streamsMap(topic)
    streams.map {
      m => {
        try {
          val next = m.iterator().next()
          println(new String(next.key()) + " " + new String(next.message()))
        }
        finally {
          println("done !")
        }
      }
    }
  }
}

object KafkaConsumer extends App {
  val consumer = new KafkaConsumer
  consumer.read
}