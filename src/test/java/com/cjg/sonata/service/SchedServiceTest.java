package com.cjg.sonata.service;


import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.common.TestPropertyUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SchedServiceTest {

    @Mock
    BatchRepository batchRepository;

    @Mock
    HttpRequestUtil httpRequestUtil;

    @InjectMocks
    SchedService schedService;

    @Mock
    ApiService apiService;

    @Test
    @DisplayName("encoding_video")
    public void encoding_video(){

        BatchDTO batchDTO = new BatchDTO();
        batchDTO.setMediaId(1L);

        Batch batch = new Batch();
        batch.setMediaId(batchDTO.getMediaId());
        batch.setType("video");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("video.mp4");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("video_enc.mp4");

        given(batchRepository.findByMediaId(1L)).willReturn(batch);

        schedService.setFfmpegPath(TestPropertyUtil.ffmpegPath);
        schedService.setFfprobePath(TestPropertyUtil.ffprobePath);
        schedService.encoding(batchDTO);

    }

    @Test
    @DisplayName("encoding_audio")
    public void encoding_audio() {

        BatchDTO batchDTO = new BatchDTO();
        batchDTO.setMediaId(2L);

        Batch batch = new Batch();
        batch.setMediaId(2L);
        batch.setType("audio");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("audio.mp3");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("audio_enc.mp3");

        given(batchRepository.findByMediaId(2L)).willReturn(batch);

        schedService.setFfmpegPath(TestPropertyUtil.ffmpegPath);
        schedService.setFfprobePath(TestPropertyUtil.ffprobePath);
        schedService.encoding(batchDTO);

    }

    @Test
    @DisplayName("encoding_image")
    public void encoding_image() {

        BatchDTO batchDTO = new BatchDTO();
        batchDTO.setMediaId(3L);

        Batch batch = new Batch();
        batch.setMediaId(3L);
        batch.setType("image");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("image.jpg");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("image_enc.jpg");

        given(batchRepository.findByMediaId(3L)).willReturn(batch);

        schedService.setImageEncoderPath(TestPropertyUtil.imageEncoderPath);
        schedService.encoding(batchDTO);

    }

    @Test
    @DisplayName("encodingFail")
    public void encodingFail(){
        Batch batch = new Batch();
        batch.setMediaId(1L);
        schedService.encodingFail(batch);
    }


}
