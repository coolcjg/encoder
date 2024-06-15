package com.cjg.sonata.service;


import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {

    @InjectMocks
    ApiService apiService;

    @Mock
    BatchRepository batchRepository;

    @Test
    @DisplayName("batchInsert_video")
    public void batchInsert_video(){

        BatchDTO batchDto = new BatchDTO();
        batchDto.setType("video");
        batchDto.setMediaId(1L);
        batchDto.setOriginalFile("D:/NAS/upload/original/2024/05/18/598f0930-27bb-4212-ae1a-68339c7452a1.mp4");
        batchDto.setReturnUrl("https://cjg.com");

        Map<String,Object> result =  apiService.batchInsert(batchDto);

        Assertions.assertThat(result.get("message")).isEqualTo("success");
    }

    @Test
    @DisplayName("batchInsert_audio")
    public void batchInsert_audio(){

        BatchDTO batchDto = new BatchDTO();
        batchDto.setType("audio");
        batchDto.setMediaId(2L);
        batchDto.setOriginalFile("D:/NAS/upload/original/2024/05/18/598f0930-27bb-4212-ae1a-68339c7452a1.mp3");
        batchDto.setReturnUrl("https://cjg.com");

        Map<String,Object> result =  apiService.batchInsert(batchDto);

        Assertions.assertThat(result.get("message")).isEqualTo("success");
    }

    @Test
    @DisplayName("batchInsert_image")
    public void batchInsert_image(){

        BatchDTO batchDto = new BatchDTO();
        batchDto.setType("image");
        batchDto.setMediaId(3L);
        batchDto.setOriginalFile("D:/NAS/upload/original/2024/05/18/598f0930-27bb-4212-ae1a-68339c7452a1.jpg");
        batchDto.setReturnUrl("https://cjg.com");

        Map<String,Object> result =  apiService.batchInsert(batchDto);

        Assertions.assertThat(result.get("message")).isEqualTo("success");
    }
}
