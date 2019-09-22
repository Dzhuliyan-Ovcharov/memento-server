package com.memento.repository;

import com.memento.model.EstateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateTypeRepository extends JpaRepository<EstateType, Long> {

}
