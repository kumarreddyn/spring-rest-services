package rest.services.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseUtil {

	@Autowired
	ObjectMapper objectMapper;
	
	public  ResponseEntity<?> generateResponse(Map<String, Object> dataMap, String responseCode){
		
		RestResponse restResponse = new RestResponse(responseCode, dataMap);
		restResponse.setCode(responseCode);
		restResponse.setData(dataMap);
		
		String jsonResponse = "{}";
		
		try {
			jsonResponse = objectMapper.writeValueAsString(restResponse);
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);				
	}
	
	
}
