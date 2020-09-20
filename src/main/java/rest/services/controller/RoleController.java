package rest.services.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import rest.services.entity.Role;
import rest.services.service.RoleService;

@RestController
@RequestMapping(path = "/role")
public class RoleController {
	
	@Autowired
	RoleService roleService;
	
	@RequestMapping(path = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@RequestBody Role role) {
		return roleService.save(role);
	}
	
	@RequestMapping(path = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody Role role) {
		return roleService.update(role);
	}
	
	@RequestMapping(path = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@RequestBody String requestData) {
		return roleService.delete(requestData);
	}
	
	@RequestMapping(path = "/get-all-roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllRoles() {
		return roleService.getAllActiveRoles();
	}

}
