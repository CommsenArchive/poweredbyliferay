package com.commsen.liferay.builtwith.provider;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.commsen.liferay.builtwith.api.CheckDTO;
import com.commsen.liferay.builtwith.api.LiferayDTO;
import com.commsen.liferay.builtwith.api.SiteChecker;

@Component
public class DefaultSiteChecker implements SiteChecker {

	private String dateFormat = "yyyy-MM-dd hh:mm:ss";
	private SimpleDateFormat format = new SimpleDateFormat(dateFormat);

	@Reference(
			policy=ReferencePolicy.DYNAMIC, 
			policyOption=ReferencePolicyOption.GREEDY, 
			cardinality=ReferenceCardinality.OPTIONAL
			)
	volatile ContentChecker contentChecker;
	
	
	@Activate
	public void activate () {
		System.out.println("DefaultSiteChecker activated!!!");
	}
	
	@Deactivate
	public void deactivate () {
		System.out.println("DefaultSiteChecker deactivated!!!");
	}

	public static void main(String[] args) {
		URI u = URI.create("http://test");
		System.out.println(u.getScheme());
		u = URI.create("HTTP://test");
		System.out.println(u);
	}

	private boolean isValidUri(URI uri) {
		if (uri == null)
			return false;
		if (uri.getScheme() == null)
			return false;
		if (uri.getScheme().toLowerCase().matches("https?"))
			return true;
		return false;
	}

	public CheckDTO check(URI uri) {
		
		if (uri == null) {
			throw new IllegalArgumentException("Argument 'uri' can not be null!");
		}
		
		if (!isValidUri(uri)) {
			throw new IllegalArgumentException("Invalid uri schema: " + uri);
		}
		
		CheckDTO check = new CheckDTO();
		check.originalURI = uri.toString();
		check.date.dateFormat = dateFormat;
		check.date.date = format.format(new Date());	
		
		Connection connection = Jsoup.connect(uri.toString())
				.followRedirects(true)
				.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			    .referrer("http://www.google.com");
		Response response = null;
		try {
			response = connection.execute();
		} catch (IOException e) {
			check.errorMessage = e.getClass() +" : " + e.getMessage();
			return check;
		}

		check.redirectURI = response.url().toString();
		check.statusCode = response.statusCode();
		check.statusMessage = response.statusMessage();
		
		LiferayDTO liferayInfo = new LiferayDTO();
		check.liferayInfo = liferayInfo;
		liferayInfo.versionFromHeader = response.header("Liferay-Portal");
			
		Document document;
		try {
			document = response.parse();
		} catch (IOException e) {
			check.errorMessage = e.getClass() + " : " + e.getMessage();
			return check;
		}
		
		check.title = document.title();
				
		if (contentChecker != null) {
			contentChecker.check(document, liferayInfo);
		}
		
		if (
				(liferayInfo.versionFromHeader != null &&
				liferayInfo.versionFromHeader.trim().length() > 0) 
				||
				(liferayInfo.detectedBuildNumbers != null &&
				!liferayInfo.detectedBuildNumbers.isEmpty())
			) {
			check.builtWithLiferay = true;
		} else {
			check.builtWithLiferay = false;
		}

		return check;
	}

}
