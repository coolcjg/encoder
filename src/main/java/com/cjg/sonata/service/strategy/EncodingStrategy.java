package com.cjg.sonata.service.strategy;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.domain.Gallery;
import com.cjg.sonata.repository.BatchRepository;
import com.cjg.sonata.repository.GalleryRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class EncodingStrategy {

    @Setter
    @Value("${ffmpegPath}")
    public String ffmpegPath;

    @Setter
    @Value("${ffprobePath}")
    public String ffprobePath;

    @Setter
    @Value("${imageEncoderPath}")
    public String imageEncoderPath;

    public abstract void encoding(Batch batch);

    @Autowired
    GalleryRepository galleryRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    HttpRequestUtil httpRequestUtil;

    public void encodingFail(Batch batch) {
        batch.setStatus(EncodingStatus.FAIL.getName());
        batchRepository.save(batch);

        Map<String, Object> param = new HashMap();
        param.put("mediaId", batch.getMediaId());
        param.put("status", EncodingStatus.FAIL.getName());
        httpRequestUtil.encodingRequest(batch.getReturnUrl(), param);
    }


    public void encodingSuccess(Batch batch) {

        Map<String,Object> param = new HashMap();
        param.put("mediaId", batch.getMediaId());
        param.put("status", EncodingStatus.SUCCESS.getName());
        param.put("encodingFileName", batch.getEncodingFileName());
        param.put("encodingFilePath", batch.getEncodingFilePath());

        File encodingFile = new File(batch.getEncodingFilePath() + batch.getEncodingFileName());
        param.put("encodingFileSize", encodingFile.length() + "");

        param.put("thumbnailPath", batch.getThumbnailPath());

        httpRequestUtil.encodingRequest(batch.getReturnUrl(), param);

    }

    public Gallery insertGallery(Gallery gallery){
        return galleryRepository.save(gallery);
    }

    public Gallery setGalleryParam(Batch batch){

        //기본 파라미터 설정
        Gallery gallery = new Gallery();
        gallery.setMediaId(batch.getMediaId());
        gallery.setType(batch.getType());

        int index  = batch.getEncodingFilePath().lastIndexOf("/upload/") + 7;

        gallery.setThumbnailFilePath(batch.getEncodingFilePath().substring(index));
        gallery.setThumbnailFileName(batch.getMediaId() + ".jpg");

        gallery.setEncodingFilePath(batch.getEncodingFilePath().substring(index));
        gallery.setEncodingFileName(batch.getEncodingFileName());

        gallery.setStatus("N");

        return gallery;

    }
}
