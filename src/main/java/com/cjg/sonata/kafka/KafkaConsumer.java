package com.cjg.sonata.kafka;

import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.service.SchedService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

	private final SchedService schedService;

	Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(topics="encoding")
	public void listener(Object data) {
		ConsumerRecord consumerRecord = (ConsumerRecord)data;
		String value = (String)consumerRecord.value();
		logger.info("Kafka Consumer value {}", value);
		schedService.encoding(convert(value));
	}

	public BatchDTO convert(String value){

		BatchDTO batchDto = new BatchDTO();
		JsonObject jo = JsonParser.parseString(value).getAsJsonObject();

		batchDto.setOriginalFile(jo.get("originalFile").getAsString());
		batchDto.setMediaId(jo.get("mediaId").getAsLong());
		batchDto.setType(jo.get("type").getAsString());
		batchDto.setReturnUrl(jo.get("returnUrl").getAsString());

		return batchDto;
	}

}
