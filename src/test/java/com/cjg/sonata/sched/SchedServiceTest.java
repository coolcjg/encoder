package com.cjg.sonata.sched;


import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.domain.Gallery;
import com.cjg.sonata.service.SchedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SchedServiceTest {

    @InjectMocks
    SchedService schedService;

    @Test
    public void setGalleryParamTest_videoInput(){
        Batch batch = new Batch();
        batch.setBatchId(1L);
        batch.setMediaId(1L);
        batch.setType("video");
        batch.setEncodingFilePath("/test/upload/encoding/2024/05/01");
        batch.setEncodingFileName(1L + ".mp4");

        Gallery gallery = schedService.setGalleryParam(batch);

        Assertions.assertEquals(1L, gallery.getMediaId());
        Assertions.assertEquals("video", gallery.getType());
        Assertions.assertEquals("/test/upload/encoding/2024/05/01", gallery.getEncodingFilePath());
        Assertions.assertEquals("1.jpg", gallery.getMediaId() + ".jpg");
        Assertions.assertEquals("/test/upload/encoding/2024/05/01", gallery.getEncodingFilePath());
        Assertions.assertEquals("1.mp4", gallery.getEncodingFileName());
    }

    @Test
    public void setGalleryParamTest_imageInput(){
        Batch batch = new Batch();
        batch.setBatchId(1L);
        batch.setMediaId(1L);
        batch.setType("image");
        batch.setEncodingFilePath("/test/upload/encoding/2024/05/01");
        batch.setEncodingFileName(1L + ".jpg");

        Gallery gallery = schedService.setGalleryParam(batch);

        Assertions.assertEquals(1L, gallery.getMediaId());
        Assertions.assertEquals("image", gallery.getType());
        Assertions.assertEquals("/test/upload/encoding/2024/05/01", gallery.getEncodingFilePath());
        Assertions.assertEquals("1.jpg", gallery.getMediaId() + ".jpg");
        Assertions.assertEquals("/test/upload/encoding/2024/05/01", gallery.getEncodingFilePath());
        Assertions.assertEquals("1.jpg", gallery.getEncodingFileName());
    }


}
