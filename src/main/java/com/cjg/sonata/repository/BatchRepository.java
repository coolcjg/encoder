package com.cjg.sonata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjg.sonata.domain.Batch;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
	
	Batch save(Batch batch);
	
	List<Batch> findAllByStatusOrderByRegDateAsc(String status);

}
