package com.marionete.stock.importer

import java.util.concurrent.{CountDownLatch, Executor, ExecutorService, Executors}

import com.marionete.stock.messaging._
import yahoofinance.YahooFinance

/**
  * Created by joseluisvf on 29/07/16.
  */
// gets a list of stocks and periodically pulls from yahoo and returns w/e

class YahooPoller(stocks: Seq[String], timeBetweenPolls: Long) {
  require(stocks.nonEmpty)

  private val threadPool = Executors.newFixedThreadPool(1)
  @volatile private var run = false
  private var countdownLatch: CountDownLatch = new CountDownLatch(1)

  def start(): Unit = {
    run = true
    threadPool.submit {
      while (!run) {
        wait(2000)
      }
      countdownLatch.countDown()
    }
  }

  def stop(): Unit = {
    run = false
    countdownLatch.await()
  }

  def getStocks() = {

  }
}

object Yahoo {
  def getStocks (stocks: Seq[String] ): Seq[Stock] = {
   YahooFinance.get(stocks.toArray)
  }
}
