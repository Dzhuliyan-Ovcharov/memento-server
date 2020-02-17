package com.memento.web.endpoint;

import com.memento.model.Role;
import com.memento.service.RoleService;
import com.memento.web.endpoint.api.RoleApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.memento.web.RequestUrlConstant.ROLE_BASE_URL;

@RestController
@RequestMapping(value = ROLE_BASE_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class RoleApiController implements RoleApi {

    private final RoleService roleService;

    @Autowired
    public RoleApiController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    @GetMapping
    public ResponseEntity<Set<Role>> getAll() {
        return ResponseEntity.ok(roleService.getAll());
    }
}
