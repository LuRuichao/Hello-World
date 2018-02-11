package com.unisk.kafkatest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.util.StringUtils;


//@Configuration
//@EnableKafka
public class KafkaConfiguration {

	@Value("${kafka.broker.address}")
	private String brokerAddress;

	@Value("${kafka.default.topic}")
	private String defaultTopic;

	@Value("${kafka.jks.location}")
	private String jksLocation;

	public KafkaConfiguration() {
		URL authLocation = KafkaConfiguration.class.getClassLoader().getResource("kafka_client_jaas.conf");
		if (System.getProperty("java.security.auth.login.config") == null) {
			System.setProperty("java.security.auth.login.config", authLocation.toExternalForm());
		}
	}

	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
		if (StringUtils.isEmpty(jksLocation)) {
			props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, KafkaConfiguration.class.getClassLoader().getResource("kafka.client.truststore.jks").getPath());
		} else {
			props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, jksLocation);
		}
		props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "KafkaOnsClient");
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
		props.put(SaslConfigs.SASL_MECHANISM, "ONS");
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 30 * 1000);//
		return props;
	}

	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<String, String>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<String, String>(producerFactory());
		kafkaTemplate.setDefaultTopic(defaultTopic);
		return kafkaTemplate;
	}
}
