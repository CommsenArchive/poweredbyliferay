package com.commsen.liferay.builtwith.storage.wedeploy;

import com.commsen.liferay.builtwith.api.CheckDTO;

public class StorableCheckDTO extends CheckDTO {
	
	public String id;
	
	public static final StorableCheckDTO fromCheck(CheckDTO check) {
		StorableCheckDTO result = new StorableCheckDTO();
		result.builtWithLiferay = check.builtWithLiferay;
		result.date = check.date;
		result.errorMessage = check.errorMessage;
		result.liferayInfo = check.liferayInfo;
		result.originalURI = check.originalURI;
		result.redirectURI = check.redirectURI;
		result.statusCode = check.statusCode;
		result.statusMessage = check.statusMessage;
		result.title = check.title;
		result.id = result.originalURI;
		return result;
	}

}
