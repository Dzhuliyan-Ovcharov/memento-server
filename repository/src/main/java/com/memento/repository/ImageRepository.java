package com.memento.repository;

import com.memento.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByName(String path);

    List<Image> findAllByEstateId(Long estateId);
}
