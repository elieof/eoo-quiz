package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.config.KafkaProperties
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import reactor.kafka.sender.SenderRecord
import reactor.kafka.sender.SenderResult

import java.time.Instant
import java.util.concurrent.ExecutionException


@RestController
@RequestMapping("/api/eooquiz-kafka")
class EooquizKafkaResource(
    private val kafkaProperties: KafkaProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private lateinit var sender: KafkaSender<String, String>

    init {
        sender = KafkaSender.create(SenderOptions.create(kafkaProperties.getProducerProps()))
    }

    @PostMapping("/publish/{topic}")
    @Throws(*[ExecutionException::class, InterruptedException::class])
    fun publish(@PathVariable topic: String, @RequestParam message: String, @RequestParam(required = false) key: String?): Mono<PublishResult> {
        log.debug("REST request to send to Kafka topic $topic with key $key the message : $message")
        return Mono.just(SenderRecord.create(topic, null, null, key, message, null))
            .as(sender::send)
            .next()
            .map(SenderResult::recordMetadata)
            .map { metadata ->
                PublishResult(metadata.topic(), metadata.partition(), metadata.offset(), Instant.ofEpochMilli(metadata.timestamp()))
            }
    }

    @GetMapping("/consume")
    fun consume(@RequestParam("topic") topics: List<String>, @RequestParam consumerParams: Map<String, String>): SseEmitter {
        log.debug("REST request to consume records from Kafka topics $topics")
        val consumerProps = kafkaProperties.getConsumerProps()
        consumerProps.putAll(consumerParams)
        consumerProps.remove("topic")

        val receiverOptions = ReceiverOptions.<String, String>create(consumerProps).subscription(topics)
        return KafkaReceiver.create(receiverOptions)
                .receive()
                .map(ConsumerRecord::value)
    }

    class PublishResult(
        val topic: String,
        val partition: Int,
        val offset: Long,
        val timestamp: Instant
    )
}
