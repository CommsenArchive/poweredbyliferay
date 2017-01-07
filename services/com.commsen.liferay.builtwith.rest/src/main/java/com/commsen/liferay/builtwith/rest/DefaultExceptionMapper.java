package com.commsen.liferay.builtwith.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;

@Provider
@Component (immediate = true, service = Object.class)
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		
		exception.printStackTrace();
		
		Response response = Response.serverError().type(MediaType.APPLICATION_JSON).
				entity("{\"error\"=\"" + exception + "\"}").build();
		return response;
	}

}
