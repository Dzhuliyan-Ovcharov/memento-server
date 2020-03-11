package com.memento.web.endpoint;

import com.memento.model.AdType;
import com.memento.model.Permission;
import com.memento.service.AdTypeService;
import com.memento.web.endpoint.api.AdTypeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

import static com.memento.web.RequestUrlConstant.AD_TYPES_BASE_URL;

@RestController
@RequestMapping(value = AD_TYPES_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdTypeApiController implements AdTypeApi {

    private final AdTypeService adTypeService;

    @Autowired
    public AdTypeApiController(final AdTypeService adTypeService) {
        this.adTypeService = adTypeService;
    }

    @Override
    @Secured(value = Permission.Value.ADMIN)
    @PostMapping
    public ResponseEntity<AdType> save(@RequestBody @Valid final AdType adType) {
        return ResponseEntity.ok(adTypeService.save(adType));
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<AdType>> getAll() {
        return ResponseEntity.ok(adTypeService.getAll());
    }

    @Override
    @GetMapping(value = "/type/{type}")
    public ResponseEntity<AdType> findByType(@PathVariable(value = "type") final String type) {
        return ResponseEntity.ok(adTypeService.findByType(type));
    }
}
