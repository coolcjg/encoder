package com.cjg.sonata.service.strategy;

import com.cjg.sonata.common.EncodingStatus;
import com.cjg.sonata.domain.Batch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Component
public class ImageStrategy extends EncodingStrategy {

    Logger logger = LoggerFactory.getLogger(ImageStrategy.class);

    public void encoding(Batch batch) {

        try {

            logger.info("BATCH MEDIA : " + batch.getMediaId());
            logger.info("BATCH MEDIA PATH : " + batch.getOriginalFilePath() + batch.getOriginalFileName());

            batch.setStatus(EncodingStatus.ENCODING.getName());
            batchRepository.save(batch);

            String input = batch.getOriginalFilePath() + batch.getOriginalFileName();
            String output = batch.getEncodingFilePath() + batch.getEncodingFileName();

            File outputDir = new File(batch.getEncodingFilePath());

            if(!outputDir.exists()) {
                outputDir.mkdirs();
            }

            List<String> list = new ArrayList();

            list.add(imageEncoderPath);
            list.add(input);
            list.add(output);

            logger.info("IMAGE ENCODING PARAM LIST : " + list);

            ProcessBuilder pb = new ProcessBuilder(list);
            pb.redirectErrorStream(true);

            //타겟 하나 잡기
            Process process = pb.start();

            InputStreamReader in = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(in);

            String line = "";
            while((line = br.readLine()) != null) {
            }

            process.waitFor();

            int exitValue = process.exitValue();

            if(exitValue == 0) {
                batch.setThumbnailPath(output);
                batch.setStatus(EncodingStatus.SUCCESS.getName());
                batchRepository.save(batch);

                String returnUrl = batch.getReturnUrl();
                if(returnUrl != null && !returnUrl.equals("")) {
                    encodingSuccess(batch);
                    insertGallery(setGalleryParam(batch));
                }

            }else {
                encodingFail(batch);
            }

        }catch(IOException | InterruptedException e) {
            logger.info("ERROR : " + e);
            encodingFail(batch);
        };

    }
}
