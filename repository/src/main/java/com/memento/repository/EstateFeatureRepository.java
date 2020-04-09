package com.memento.repository;

import com.memento.model.EstateFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EstateFeatureRepository extends JpaRepository<EstateFeature, Long> {

    Optional<EstateFeature> findEstateFeatureByFeature(String feature);

    List<EstateFeature> findAllByFeatureIn(Set<String> features);
}
