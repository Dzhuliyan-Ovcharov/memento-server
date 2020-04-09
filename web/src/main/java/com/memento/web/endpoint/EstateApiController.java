package com.memento.web.endpoint;

import com.memento.model.Estate;
import com.memento.model.Permission;
import com.memento.model.User;
import com.memento.service.EstateService;
import com.memento.web.converter.EstateRequestToEstatePropertyMap;
import com.memento.web.converter.EstateToEstateResponsePropertyMap;
import com.memento.web.dto.EstateRequest;
import com.memento.web.dto.EstateResponse;
import com.memento.web.endpoint.api.EstateApi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.memento.web.RequestUrlConstant.ESTATES_BASE_URL;

@RestController
@RequestMapping(value = ESTATES_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class EstateApiController implements EstateApi {

    private final EstateService estateService;
    private final ModelMapper modelMapper;

    @Autowired
    public EstateApiController(final EstateService estateService, final ModelMapper modelMapper) {
        this.estateService = estateService;
        this.modelMapper = modelMapper;
        modelMapper.addMappings(new EstateRequestToEstatePropertyMap());
        modelMapper.addMappings(new EstateToEstateResponsePropertyMap());
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<EstateResponse> findById(@PathVariable final Long id) {
        final Estate estate = estateService.findById(id);
        final EstateResponse estateResponse = modelMapper.map(estate, EstateResponse.class);
        return ResponseEntity.ok(estateResponse);
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<EstateResponse>> getAll() {
        return ResponseEntity.ok(estateService.getAll()
                .stream()
                .map(estate -> modelMapper.map(estate, EstateResponse.class))
                .collect(Collectors.toSet()));
    }

    @Override
    @GetMapping(value = "/{email}")
    public ResponseEntity<Set<EstateResponse>> getEstatesByUserEmail(@PathVariable final String email) {
        return ResponseEntity.ok(estateService.getEstatesByUserEmail(email)
                .stream()
                .map(estate -> modelMapper.map(estate, EstateResponse.class))
                .collect(Collectors.toSet()));
    }

    @Override
    @Secured({Permission.Value.ADMIN, Permission.Value.AGENCY})
    @PostMapping
    public ResponseEntity<EstateResponse> save(@RequestBody @Valid final EstateRequest estateRequest) {
        final Estate estate = modelMapper.map(estateRequest, Estate.class);
        final Estate savedEstate = estateService.save(estate);
        final EstateResponse estateResponse = modelMapper.map(savedEstate, EstateResponse.class);
        return ResponseEntity.ok(estateResponse);
    }

    @Override
    @Secured({Permission.Value.ADMIN, Permission.Value.AGENCY})
    @PutMapping(value = "/{id}")
    public ResponseEntity<EstateResponse> update(@PathVariable final Long id, @RequestBody @Valid final EstateRequest estateRequest) {
        final Estate estate = modelMapper.map(estateRequest, Estate.class);
        final Estate updatedEstate = estateService.update(id, estate);
        final EstateResponse estateResponse = modelMapper.map(updatedEstate, EstateResponse.class);
        return ResponseEntity.ok(estateResponse);
    }
}
