package com.memento.repository;

import com.memento.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    Optional<Floor> findFloorByNumber(Integer number);
}
