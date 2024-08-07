package com.cjg.sonata.kafka;

import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.service.SchedService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerTest {


    @InjectMocks
    KafkaConsumer kafkaConsumer;

    @Mock
    SchedService schedService;

    @Test
    @DisplayName("convert")
    public void listener(){
        ConsumerRecord consumerRecord = new ConsumerRecord("encoding", 0, 10, null,"{\"originalFile\":\"D:/NAS/upload/original/2024/08/07/9516fb98-ee92-4ab2-800a-d9a0212a9b93.jpg\",\"mediaId\":\"303\",\"type\":\"image\",\"returnUrl\":\"http://localhost:4000/api/encodingResult\"}");
        kafkaConsumer.listener(consumerRecord);
    }

    @Test
    @DisplayName("convert")
    public void convert(){

        String value = "{\"originalFile\":\"D:/NAS/upload/original/2024/08/07/e84e0370-dddf-4cb4-8e50-fbf150e25715.jpg\",\"mediaId\":\"302\",\"type\":\"image\",\"returnUrl\":\"http://localhost:4000/api/encodingResult\"}";
        BatchDTO result = kafkaConsumer.convert(value);

        Assertions.assertThat(result.getOriginalFile()).isEqualTo("D:/NAS/upload/original/2024/08/07/e84e0370-dddf-4cb4-8e50-fbf150e25715.jpg");
        Assertions.assertThat(result.getMediaId()).isEqualTo(302);
        Assertions.assertThat(result.getType()).isEqualTo("image");
        Assertions.assertThat(result.getReturnUrl()).isEqualTo("http://localhost:4000/api/encodingResult");


    }


}
