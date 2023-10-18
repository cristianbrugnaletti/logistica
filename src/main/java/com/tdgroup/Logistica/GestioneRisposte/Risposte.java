package com.tdgroup.Logistica.GestioneRisposte;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatusCode;

public class Risposte {


	public static Map<String, Object> ErrorResponse(int statusCode, String errorMessage, String path) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", statusCode);
		errorResponse.put("error", errorMessage);
		errorResponse.put("path", path);
		return errorResponse;
	}

	public static Map<String, Object> SuccessResponse(String message, String path) {
		Map<String, Object> successResponse = new HashMap<>();
		successResponse.put("timestamp", LocalDateTime.now());
		successResponse.put("status", 200);
		successResponse.put("message", message);
		successResponse.put("path", path);
		return successResponse;
	}
	
	public static Map<String, Object> ErrorResponseHttp(HttpStatusCode httpStatusCode, String errorMessage, String path) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timestamp", LocalDateTime.now());
		errorResponse.put("status", httpStatusCode);
		errorResponse.put("error", errorMessage);
		errorResponse.put("path", path);
		return errorResponse;
	}
}
