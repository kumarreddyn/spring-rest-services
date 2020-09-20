package rest.services.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest.services.entity.User;
import rest.services.helper.LoginModel;
import rest.services.service.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(path = { "/openapi/register" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@RequestBody User user) {
		return userService.register(user);
	}
	
	@PostMapping(path = {"/save" })
	public ResponseEntity<?> save(@RequestParam User user, @RequestParam List<Long> roleIds) {		
		return userService.save(user, roleIds);
	}
	
	@PostMapping(path = "/update")
	public ResponseEntity<?> update(@RequestParam("user") User user, @RequestParam List<Long> roleIds) {
		return userService.update(user, roleIds);
	}
	
	@RequestMapping(path = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@RequestBody String requestData) {
		return userService.delete(requestData);
	}
	
	
	@RequestMapping(path = "/get-all-users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUsers() {
		return userService.getAllActiveUsers();
	}

	@RequestMapping(path = "/find/{userId}")
	public ResponseEntity<?> findUser(@PathVariable final Long userId) {
		return userService.findUser(userId);
	}
	
	@RequestMapping(path = "/openapi/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<?> login(@RequestBody LoginModel loginModel) {				
		return userService.login(loginModel);
	}
	
}
