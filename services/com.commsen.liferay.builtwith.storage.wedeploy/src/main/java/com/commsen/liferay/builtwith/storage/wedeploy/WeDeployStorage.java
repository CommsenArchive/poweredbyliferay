package com.commsen.liferay.builtwith.storage.wedeploy;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.commsen.liferay.builtwith.api.CheckDTO;
import com.commsen.liferay.builtwith.api.SiteStorage;
import com.commsen.liferay.builtwith.api.SiteStorageException;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;
import com.commsen.wedeploy.client.data.WeDeployDataService;

@Component
public class WeDeployStorage implements SiteStorage {

	private static final String PPROJECT = "poweredbyliferay";
	private static final String DATA_SERVICE = "data";
	private static final String DATA_COLLECTION = "sites";

	@Reference
	private WeDeployDataService weDeployDataService;

	@Activate
	public void init() {
		System.out.println("Attempt to activate WeDeployStorage!");

		String masterToken = System.getenv("WEDEPLOY_PROJECT_MASTER_TOKEN");
		System.out.println("masterToken: " + masterToken);

		System.out.println("WeDeployStorage activated!!!");

	}

	@Deactivate
	public void deactivate() {
		System.out.println("WeDeployStorage deactivated!!!");
	}

	private WeDeployDataCollection getCollection() throws WeDeployClientException {
		return weDeployDataService.connect(PPROJECT, DATA_SERVICE).collection(DATA_COLLECTION);
	}

	@Override
	public void save(CheckDTO checkDTO) throws SiteStorageException {

		WeDeployDataDocument<CheckDTO> checkDoc = new WeDeployDataDocument<CheckDTO>(toId(checkDTO.originalURI), checkDTO);

		try {
			getCollection().save(checkDoc);
		} catch (WeDeployClientException e) {
			throw new SiteStorageException(e);
		}
	}

	@Override
	public CheckDTO get(URI domain) throws SiteStorageException {

		Optional<WeDeployDataDocument<CheckDTO>> checkDTO;
		try {
			checkDTO = getCollection().get(toId(domain.toString()), CheckDTO.class);
		} catch (WeDeployClientException e) {
			throw new SiteStorageException(e);
		}

		if (checkDTO.isPresent()) {
			return checkDTO.get().getObject();
		}

		return null;

		// Response commandResponse = dataContainer.filter("originalURI",
		// domain.toString()).get();
		//
		// System.out.println("Search result: " + commandResponse.body());
		//
		// if (commandResponse.succeeded()) {
		// Gson gson = new Gson();
		// CheckDTO[] results = gson.fromJson(commandResponse.body(), CheckDTO[].class);
		// if (results.length == 0) return null;
		// return results[0];
		// } else {
		// throw new SiteStorageException(commandResponse.statusMessage());
		// }
	}

	
	private String toId(String uri) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(uri.getBytes("UTF-8"));
			BigInteger bigInt = new BigInteger(1,digest);
			return bigInt.toString(16);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
