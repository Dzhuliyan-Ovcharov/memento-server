package com.memento.repository;

import com.memento.model.Estate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    List<Estate> findAllByUserEmail(String email);
}
