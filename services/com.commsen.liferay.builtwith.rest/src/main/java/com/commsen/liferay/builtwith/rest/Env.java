package com.commsen.liferay.builtwith.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;

import com.commsen.em.annotations.RequiresJaxrsServer;

@Path("/env")
@Component (immediate = true, service = Object.class)
@RequiresJaxrsServer
public class Env {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String env() throws Exception {
		StringBuilder result = new StringBuilder();
		result.append("</table>Env vaiables: <table>");
		System.getenv().entrySet().stream().forEach(e -> result.append("<tr><td>").append(e.getKey()).append("</td><td>").append(e.getValue()).append("</td></tr>"));
		result.append("</table>System propeties: <table>");
		System.getProperties().entrySet().stream().forEach(e -> result.append("<tr><td>").append(e.getKey()).append("</td><td>").append(e.getValue()).append("</td></tr>"));
		result.append("</table>");
		
		return result.toString();
	}

}
