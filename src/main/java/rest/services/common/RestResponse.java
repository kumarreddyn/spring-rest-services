package rest.services.common;

import java.util.Map;

public class RestResponse {

	private String code;	
	private Map<String, Object> data;

	public RestResponse(String code, Map<String, Object> data) {
		this.code = code;		
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UserServiceAPIResponse [code=" + code + ", data=" + data + "]";
	}

}
