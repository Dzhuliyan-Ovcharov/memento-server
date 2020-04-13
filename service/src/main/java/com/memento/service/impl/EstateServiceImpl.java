package com.memento.service.impl;

import com.memento.model.*;
import com.memento.repository.EstateRepository;
import com.memento.service.*;
import com.memento.shared.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@Transactional
public class EstateServiceImpl implements EstateService {

    private final FloorService floorService;
    private final EstateTypeService estateTypeService;
    private final AdTypeService adTypeService;
    private final EstateFeatureService estateFeatureService;
    private final UserService userService;
    private final EstateRepository estateRepository;

    @Autowired
    public EstateServiceImpl(final FloorService floorService,
                             final EstateTypeService estateTypeService,
                             final AdTypeService adTypeService,
                             final EstateFeatureService estateFeatureService,
                             final UserService userService,
                             final EstateRepository estateRepository) {
        this.floorService = floorService;
        this.estateTypeService = estateTypeService;
        this.adTypeService = adTypeService;
        this.estateFeatureService = estateFeatureService;
        this.userService = userService;
        this.estateRepository = estateRepository;
    }

    @Override
    public Estate findById(final Long id) {
        return estateRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Set<Estate> getAll() {
        return Set.copyOf(estateRepository.findAll());
    }

    @Override
    public Estate save(@NonNull final Estate estate) {
        final Estate resolvedEstate = resolveEstateData(estate);
        final User user = userService.findByEmail(estate.getUser().getEmail());

        final Estate estateToSave = Estate.builder()
                .price(estate.getPrice())
                .quadrature(estate.getQuadrature())
                .description(estate.getDescription())
                .floor(resolvedEstate.getFloor())
                .estateType(resolvedEstate.getEstateType())
                .adType(resolvedEstate.getAdType())
                .estateFeatures(resolvedEstate.getEstateFeatures())
                .user(user)
                .images(Collections.emptySet())
                .build();

        return estateRepository.save(estateToSave);
    }

    @Override
    public Estate update(@NonNull final Long id, @NonNull final Estate estate) {
        final Estate oldEstate = findById(id);
        final Estate resolvedEstate = resolveEstateData(estate);

        final Estate newEstate = Estate.builder()
                .id(oldEstate.getId())
                .price(estate.getPrice())
                .quadrature(estate.getQuadrature())
                .description(estate.getDescription())
                .floor(resolvedEstate.getFloor())
                .estateType(resolvedEstate.getEstateType())
                .adType(resolvedEstate.getAdType())
                .estateFeatures(resolvedEstate.getEstateFeatures())
                .user(oldEstate.getUser())
                .images(oldEstate.getImages())
                .build();

        return estateRepository.save(newEstate);
    }

    @Override
    public Set<Estate> getEstatesByUserEmail(@NonNull final String email) {
        return Set.copyOf(estateRepository.findAllByUserEmail(email));
    }

    @Override
    public Set<Estate> getLatest(@NonNull final Long count) {
        Page<Estate> all = estateRepository.findAll(PageRequest.of(0, count.intValue(), Sort.Direction.DESC, "id"));
        return Set.copyOf(all.getContent());
    }

    private Estate resolveEstateData(final Estate estate) {
        final Floor floor = floorService.findByNumber(estate.getFloor().getNumber());
        final EstateType estateType = estateTypeService.findByType(estate.getEstateType().getType());
        final AdType adType = adTypeService.findByType(estate.getAdType().getType());
        final Set<EstateFeature> estateFeatures = estateFeatureService.findByFeatures(estate.getEstateFeatures()
                .stream()
                .map(EstateFeature::getFeature)
                .collect(Collectors.toSet()));

        return Estate.builder()
                .id(estate.getId())
                .price(estate.getPrice())
                .quadrature(estate.getQuadrature())
                .description(estate.getDescription())
                .floor(floor)
                .estateType(estateType)
                .adType(adType)
                .estateFeatures(estateFeatures)
                .build();
    }
}
