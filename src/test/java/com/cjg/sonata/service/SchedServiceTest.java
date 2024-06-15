package com.cjg.sonata.service;


import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.common.TestPropertyUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.repository.BatchRepository;
import com.cjg.sonata.repository.GalleryRepository;
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
    GalleryRepository galleryRepository;

    @Test
    @DisplayName("encoding_video")
    public void encoding_video() {

        List<Batch> batchList = new ArrayList();

        Batch batch = new Batch();
        batch.setMediaId(1L);
        batch.setType("video");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("video.mp4");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("video_enc.mp4");

        batchList.add(batch);

        given(batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName())).willReturn(batchList);

        schedService.setFfmpegEncoderPath(TestPropertyUtil.ffmpegEncoderPath);
        schedService.encoding();

    }

    @Test
    @DisplayName("encoding_audio")
    public void encoding_audio() {

        List<Batch> batchList = new ArrayList();

        Batch batch = new Batch();
        batch.setMediaId(2L);
        batch.setType("audio");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("audio.mp3");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("audio_enc.mp3");

        batchList.add(batch);

        given(batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName())).willReturn(batchList);

        schedService.setFfmpegEncoderPath(TestPropertyUtil.ffmpegEncoderPath);
        schedService.encoding();

    }

    @Test
    @DisplayName("encoding_image")
    public void encoding_image() {

        List<Batch> batchList = new ArrayList();

        Batch batch = new Batch();
        batch.setMediaId(3L);
        batch.setType("image");
        batch.setReturnUrl("https://cjg.com");
        batch.setOriginalFilePath("D:/NAS/uploadTest/");
        batch.setOriginalFileName("image.jpg");

        batch.setEncodingFilePath("D:/NAS/uploadTest/");
        batch.setEncodingFileName("image_enc.jpg");

        batchList.add(batch);

        given(batchRepository.findAllByStatusOrderByRegDateAsc(EncodingStatus.WAITING.getName())).willReturn(batchList);

        schedService.setImageEncoderPath(TestPropertyUtil.imageEncoderPath);
        schedService.encoding();

    }

    @Test
    @DisplayName("encodingFail")
    public void encodingFail(){
        Batch batch = new Batch();
        batch.setMediaId(1L);
        schedService.encodingFail(batch);
    }


}
