package com.marionete.stock.messaging

import java.net.URL

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by joseluisvf on 29/07/16.
  */
case class KafkaConfig(brokers:Seq[String], schemaRegistryUrl:URL)

object KafkaConfig{
  def apply(config:Config):KafkaConfig={
    val brokers = config.getString("kafka.brokers").split(',').map(_.trim)
    val schemaUrl = new URL(config.getString("kafka.schema.url"))
    KafkaConfig(brokers, schemaUrl)
  }

  ConfigFactory.load()
}