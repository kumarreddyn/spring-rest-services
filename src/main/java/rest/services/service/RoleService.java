package rest.services.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import rest.services.common.ResponseUtil;
import rest.services.constants.RestServiceConstants;
import rest.services.entity.Role;
import rest.services.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ResponseUtil responseUtil;
	
	public ResponseEntity<?> save(Role role) {		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		role.setIsActive(true);
		role.setCreatedDate(new Date());
		
		role = roleRepository.save(role);
		dataMap.put("message", "Role saved");
		dataMap.put("role", role);
		
		return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLE_SAVED);
	}

	public ResponseEntity<?> getAllActiveRoles() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("roles", roleRepository.findByIsActiveOrderByNameAsc(true));
		dataMap.put("message", "Roles found.");
		return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLES_FOUND);
	}

	public ResponseEntity<?> update(Role role) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
				
		Optional<Role> optionalRole = roleRepository.findById(role.getRoleId());
		if(optionalRole.isPresent()) {
			Role existingRole = optionalRole.get();			
			existingRole.setName(role.getName());
			existingRole.setModifiedDate(new Date());
			existingRole = roleRepository.save(existingRole);		
			dataMap.put("message", "Role updated.");
			dataMap.put("role", existingRole);
			
			return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLE_UPDATED);
		}else {
			return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLE_NOT_EXIST);
		}
		
		
	}

	public ResponseEntity<?> delete(String requestData) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(requestData);
			Long roleId = jsonNode.get("roleId").asLong();		
			Optional<Role> optionalRole = roleRepository.findById(roleId);
			optionalRole.get().setIsActive(false);		
			roleRepository.save(optionalRole.get());		
			dataMap.put("message", "Role deleted successfully.");		
		
			return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLE_DELETED);
		} catch (Exception e) {
			return responseUtil.generateResponse(dataMap, RestServiceConstants.ROLE_NOT_DELETED);
		}
	}

}
