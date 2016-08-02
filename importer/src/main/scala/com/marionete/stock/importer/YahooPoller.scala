package com.marionete.stock.importer

import java.util
import java.util.concurrent.{CountDownLatch, Executor, ExecutorService, Executors}

import ExecutorExtension._
import com.marionete.stock.domain.Stock
import com.sun.glass.ui.MenuItem.Callback
import com.typesafe.scalalogging.slf4j.StrictLogging
import yahoofinance.YahooFinance

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
  * Created by joseluisvf on 29/07/16.
  */
// gets a list of stocks and periodically pulls from yahoo and returns w/e

class YahooPoller(stocks: Seq[String],
                  timeBetweenPolls: Long)(callback: Seq[Stock] => Unit) extends StrictLogging {
  require(stocks.nonEmpty)
  require(callback != null)
  private val threadPool = Executors.newFixedThreadPool(1)
  @volatile private var run = false
  private val countdownLatch: CountDownLatch = new CountDownLatch(1)

  def start(): Unit = {
    run = true
    threadPool.submit {
      while (run) {
        Try {
          Yahoo.getStocks(stocks)
        } match {
          case Success(stockz) => callback(stockz)
          case Failure(t) => logger.warn("No stocks retrieved", t)
        }
        Thread.sleep(timeBetweenPolls)
      }
      ()
      countdownLatch.countDown()
    }
  }

  def stop(): Unit = {
    run = false
    countdownLatch.await()
  }

}

object Yahoo {
  def getStocks(stocks: Seq[String]): Seq[Stock] = {
    YahooFinance
      .get(stocks.toArray)
      .asScala
      .withFilter { case (k, _) => k != null }
      .map { case (_, stock) =>
        val ask = Option(stock.getQuote(false).getAsk).getOrElse(java.math.BigDecimal.ZERO)
        val bid = Option(stock.getQuote(false).getBid).getOrElse(java.math.BigDecimal.ZERO)

        Stock(stock.getName,
          ask,
          bid)
      }.toList
  }
}
