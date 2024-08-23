package com.cjg.sonata.service;

import com.cjg.sonata.common.HttpRequestUtil;
import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.dto.BatchDTO;
import com.cjg.sonata.repository.BatchRepository;
import com.cjg.sonata.repository.GalleryRepository;
import com.cjg.sonata.service.strategy.AudioStrategy;
import com.cjg.sonata.service.strategy.EncodingStrategy;
import com.cjg.sonata.service.strategy.ImageStrategy;
import com.cjg.sonata.service.strategy.VideoStrategy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedService {
	
	Logger logger = LoggerFactory.getLogger(SchedService.class);

	@Autowired
	BatchRepository batchRepository;

	@Autowired
	ApiService apiService;

	@Autowired
	VideoStrategy videoStrategy;

	@Autowired
	AudioStrategy audioStrategy;

	@Autowired
	ImageStrategy imageStrategy;

	public void encoding(BatchDTO batchDTO) {

		if(batchRepository.findByMediaId(batchDTO.getMediaId()) == null){
			apiService.batchInsert(batchDTO);
		}
		Batch batch = batchRepository.findByMediaId(batchDTO.getMediaId());

		if(batch == null) {
			return;
		}

		EncodingStrategy es = null;

		switch (batch.getType()) {
			case "video" : {
				es = videoStrategy;
				break;
			}
			case "audio" : {
				es = audioStrategy;
				break;

			}
			case "image" : {
				es = imageStrategy;
				break;
			}
			default :{
				logger.error("invalid encoding Type");
				return;
			}
		};

		es.encoding(batch);

	}
}

