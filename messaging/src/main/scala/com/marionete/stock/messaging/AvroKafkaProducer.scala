package com.marionete.stock.messaging

import java.util.Properties
import java.util.concurrent.Future

import com.sksamuel.avro4s.{FromRecord, RecordFormat, ToRecord}
import com.typesafe.scalalogging.slf4j.StrictLogging
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer


/**
  * Created by joseluisvf on 29/07/16.
  */
class AvroKafkaProducer[T <: Product](topic: String, config: KafkaConfig) extends StrictLogging {
  require(topic != null && topic.nonEmpty, "Invalid topic name")

  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  props.put("schema.registry.url", config.schemaRegistryUrl.toString)

  private val kafkaProducer = new KafkaProducer[String, GenericRecord](props)

  def send(t: T, key: String)(implicit toRecord:ToRecord[T], fromRecord: FromRecord[T]): Future[RecordMetadata] = {
    val record = RecordFormat[T].to(t)
    val producerRecord = new ProducerRecord(topic, key, record)
    kafkaProducer.send(producerRecord)
  }
}
