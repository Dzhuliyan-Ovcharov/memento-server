package com.memento.repository;

import com.memento.model.AdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdTypeRepository extends JpaRepository<AdType, Long> {

    Optional<AdType> findAdTypeByType(String type);
}
