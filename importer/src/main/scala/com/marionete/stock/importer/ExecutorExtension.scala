package com.marionete.stock.importer

import java.util.concurrent.Executor

import scala.concurrent.{Future, Promise}

/**
  * Created by joseluisvf on 29/07/16.
  */

// it has to be an object
object ExecutorExtension {



  implicit class RunnableWrapper(val executor: Executor) extends AnyVal {
    def submit[T](thunk: => T): Future[T] = {
      val promise = Promise[T]()
      executor.execute(new Runnable {
        override def run(): Unit = {
          try {
            val t = thunk
            promise.success(t)
          } catch {
            case t: Throwable => promise.failure(t)
          }
        }
      })
      promise.future
    }
  }
}
