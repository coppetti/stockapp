package com.marionete.stock.domain

import kafka.consumer.ConsumerIterator


/**
  * Created by matheussilveira on 04/08/2016.
  */
case class StockQuote(msgIterator: ConsumerIterator[AnyRef, AnyRef])