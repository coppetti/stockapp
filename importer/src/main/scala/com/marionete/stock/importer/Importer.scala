package com.marionete.stock.importer

import com.marionete.stock.domain.Stock
import com.marionete.stock.messaging.{AvroKafkaProducer, KafkaConfig}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.slf4j.StrictLogging




object Importer extends App with StrictLogging {
  val config = ConfigFactory.load()

  val importerConfig = ImporterConfig(config)
  logger.info(
    s"""
       |Configuration:
       |$importerConfig
    """.stripMargin)


  val kafkaConfig = KafkaConfig(config)
  logger.info(
    s"""
       |Kafka Config
       |$kafkaConfig
       |
    """.stripMargin)
  val kafkaProducer = new AvroKafkaProducer[Stock](importerConfig.stocksTopic, kafkaConfig)

  logger.info("Starting the YahooPoller")
  val importer = new YahooPoller(importerConfig.stocks, importerConfig.pollPeriod)(stocks =>
    stocks.foreach { s =>
      logger.debug(s"Sending the stock:$s")
      kafkaProducer.send(s, s.symbol)
    }
  )
  importer.start()

  Thread.sleep(Long.MaxValue)
}

