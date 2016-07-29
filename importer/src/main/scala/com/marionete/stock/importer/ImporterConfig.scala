package com.marionete.stock.importer

import com.typesafe.config.Config

import scala.util.Try

/**
  * Created by matheussilveira on 29/07/2016.
  */
case class ImporterConfig(stocksTopic: String,
                          stocks: Seq[String],
                          pollPeriod: Long)

object ImporterConfig{
  def apply(config:Config): ImporterConfig = {
    val topic = config.getString("importer.topic")
    val pollPeriod = config.getInt("importer.poll.period")
    val stocks = config.getString("importer.yahoo.stocks").split(',').map(_.trim)
    require(stocks.nonEmpty)
    new ImporterConfig(topic, stocks, pollPeriod)
  }
}
