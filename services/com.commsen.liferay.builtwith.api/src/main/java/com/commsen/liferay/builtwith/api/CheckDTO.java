package com.commsen.liferay.builtwith.api;

import org.osgi.dto.DTO;

public class CheckDTO extends DTO {

	public String originalURI;

	public String title;

	public DateDTO date = new DateDTO();

	public String redirectURI;

	public int statusCode;
	
	public String statusMessage;

	public String errorMessage;

	public boolean builtWithLiferay;

	public LiferayDTO liferayInfo = new LiferayDTO();

}
