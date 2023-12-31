package com.cjg.sonata.repository;

import org.springframework.data.repository.CrudRepository;

import com.cjg.sonata.domain.Media;

public interface MediaRepository extends CrudRepository<Media, Long> {
	
	public Media save(Media media);
	
	public Media findByMediaId(Long mediaId);

}
