package com.memento.repository;

import com.memento.model.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {

    List<Neighborhood> findAllByCity_Name(String cityName);
}
