package com.memento.service.impl;

import com.memento.model.Floor;
import com.memento.repository.FloorRepository;
import com.memento.service.FloorService;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Primary
@Slf4j
@Transactional
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;

    @Autowired
    public FloorServiceImpl(final FloorRepository floorRepository) {
        this.floorRepository = floorRepository;
    }

    @Override
    public Set<Floor> getAll() {
        return Set.copyOf(floorRepository.findAll());
    }

    @Override
    public Floor save(@NonNull final Floor floor) {
        return floorRepository.save(floor);
    }

    @Override
    public Floor findByNumber(@NonNull final Integer number) {
        return floorRepository.findFloorByNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Липсва етаж с номер: " + number));
    }
}
