package rest.services.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import rest.services.common.ResponseUtil;
import rest.services.constants.RestServiceConstants;
import rest.services.constants.SecurityConstants;
import rest.services.entity.Role;
import rest.services.entity.User;
import rest.services.helper.LoginModel;
import rest.services.repository.RoleRepository;
import rest.services.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ResponseUtil responseUtil;
	
	public ResponseEntity<?> register(User user) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			Optional<Role> roleOptional = roleRepository.findByNameAndIsActive(RestServiceConstants.ROLE_USER, true);
			if(roleOptional.isPresent()) {
				Set<Role> defaultRoles = new HashSet<>();
				defaultRoles.add(roleOptional.get()); 
				user.setRoles(defaultRoles);
			}
			
			user.setIsActive(true);
			user.setCreatedDate(new Date());		
			User savedUser = userRepository.save(user);
			dataMap.put("message", "User Details of "+savedUser.getName()+" registered ");
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_REGISTERED);	
		}catch (Exception e) {
			e.printStackTrace();
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_REGISTERED);
		}
		
	}
	
	public ResponseEntity<?> findUser(Long userId){
		Map<String, Object> dataMap = new HashMap<String, Object>(); 
		Optional<User> user = userRepository.findById(userId);
		dataMap.put("user", user.toString());
		return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_FOUND);
	}

	public ResponseEntity<?> save(User user, List<Long> roleIds) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();

		try {			
			
			user.setIsActive(true);
			user.setCreatedDate(new Date());					
			User savedUser = userRepository.save(user);
			
			for(Long roleId: roleIds) {
				Optional<Role> optionalRole = roleRepository.findById(roleId);
				if(optionalRole.isPresent()) {
					savedUser.getRoles().add(optionalRole.get());
					optionalRole.get().getUsers().add(savedUser);
				}
			}
			savedUser = userRepository.save(user);
			
			dataMap.put("message", "User Details of "+savedUser.getName()+" Saved ");
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_SAVED);
		}catch (Exception e) {
			e.printStackTrace();
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_SAVED);
		}
		
	}

	public ResponseEntity<?> update(User userData, List<Long> roleIds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		try {
			
			Optional<User> optionalUser = userRepository.findById(userData.getUserId());			
			
			if(optionalUser.isPresent()) {
				User existingUser = optionalUser.get();
				existingUser.setName(userData.getName());
				existingUser.setEmailAddress(userData.getEmailAddress());
				existingUser.setMobileNumber(userData.getMobileNumber());
				existingUser.setModifiedDate(new Date());
				
				existingUser.getRoles().clear();
				for(Long roleId: roleIds) {
					Optional<Role> optionalRole = roleRepository.findById(roleId);
					if(optionalRole.isPresent()) {
						existingUser.getRoles().add(optionalRole.get());
						optionalRole.get().getUsers().add(existingUser);
					}
				}
				
				existingUser = userRepository.save(existingUser);
				dataMap.put("message", "Role updated successfully.");
				dataMap.put("user", existingUser);
				
				return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_UPDATED);
			}else {
				return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_EXIST);
			}
		}catch (Exception e) {			
			e.printStackTrace();
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_UPDATED);
		}		
	}

	public ResponseEntity<?> delete(String requestData) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(requestData);
			Long userId = jsonNode.get("userId").asLong();		
			Optional<User> optionalUser = userRepository.findById(userId);
			optionalUser.get().setIsActive(false);		
			userRepository.save(optionalUser.get());		
			dataMap.put("message", "User deleted successfully.");		
		
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_DELETED);
		} catch (Exception e) {
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_DELETED);
		}
	}


	public ResponseEntity<?> getAllActiveUsers() {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("users", userRepository.findByIsActiveOrderByNameAsc(true));
		dataMap.put("message", "Users found");
		return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_FOUND);
	}

	
	public ResponseEntity<?> login(LoginModel loginModel) {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		User user = userRepository.findByEmailAddressAndPasswordOrMobileNumberAndPassword(loginModel.getUsername(),
				loginModel.getPassword(), loginModel.getUsername(), loginModel.getPassword());

		if (null != user) {
			Map<String, Object> customClaims = new HashMap<>();
			
			List<String> userRoles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
			customClaims.put("userRoles", userRoles);
			customClaims.put("user", user);
			customClaims.put("loggedInUserId", user.getUserId());
			
			String authToken = generateAuthenticationToken(user, customClaims);
    		dataMap.put("message", "valid login");
    		customClaims.put("userRoles", userRoles);
    		dataMap.put("user", user);    		
    		dataMap.put("AUTH_TOKEN", authToken);
    		
    		return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_LOGGED_IN);
		} else {
			dataMap.put("message", "Invalid login");
			dataMap.put("isLoggedIn", "false");
			return responseUtil.generateResponse(dataMap, RestServiceConstants.USER_NOT_LOGGED_IN);
		}
	}
	
	private String generateAuthenticationToken(User user, Map<String, Object> customClaims) {
		
		Long now = System.currentTimeMillis();
		String token = Jwts.builder()
			.setSubject(user.getEmailAddress())
			.addClaims(customClaims)
			.setIssuedAt(new Date(now))
			.setExpiration(new Date(now + (24 * 60 * 60 * 1000)))  //1 day
			.signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET_KEY.getBytes())
			.compact();
		
		return token;
	}
}
