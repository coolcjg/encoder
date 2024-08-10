package com.cjg.sonata.repository;

import com.cjg.sonata.domain.Batch;
import com.cjg.sonata.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    Gallery save(Gallery gallery);
}
