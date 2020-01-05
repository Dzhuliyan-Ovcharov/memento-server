package com.memento.repository;

import com.memento.model.EstateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstateTypeRepository extends JpaRepository<EstateType, Long> {

    Optional<EstateType> findEstateTypeByType(String type);
}
