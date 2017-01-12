package com.commsen.liferay.builtwith.storage.wedeploy;

import java.net.URI;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.commsen.liferay.builtwith.api.CheckDTO;
import com.commsen.liferay.builtwith.api.SiteStorage;
import com.commsen.liferay.builtwith.api.SiteStorageException;
import com.google.gson.Gson;
import com.wedeploy.api.ApiClient;
import com.wedeploy.api.WeDeploy;
import com.wedeploy.api.sdk.Response;

@Component
public class WeDeployStorage implements SiteStorage {
	
	private static final String DATA_COLLECTION = "sites";
	private static final String DATA_URL = "http://data";

	private WeDeploy dataContainer;
	
	@Activate
	public void init () {
		ApiClient.init();
		dataContainer = WeDeploy.url(DATA_URL).path(DATA_COLLECTION);
		System.out.println("WeDeployStorage activated!!!");
	}
	
	@Deactivate
	public void deactivate () {
		System.out.println("WeDeployStorage deactivated!!!");
	}

	@Override
	public void save(CheckDTO checkDTO) throws SiteStorageException {
		StorableCheckDTO storableCheckDTO = StorableCheckDTO.fromCheck(checkDTO);
		Response commandResponse = dataContainer.post(storableCheckDTO);

		System.out.println(commandResponse);
		
	}

	@Override
	public CheckDTO get (URI domain) throws SiteStorageException {

		Response commandResponse = dataContainer.filter("originalURI", domain.toString()).get();

		System.out.println("Search result: " + commandResponse.body());
		
		if (commandResponse.succeeded()) {
			Gson gson = new Gson();
			CheckDTO[] results = gson.fromJson(commandResponse.body(), CheckDTO[].class);
			if (results.length == 0) return null;
			return results[0];
		}
		return null;
	}

}
