package com.commsen.liferay.builtwith.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.commsen.liferay.builtwith.api.CheckDTO;
import com.commsen.liferay.builtwith.api.SiteChecker;

public class SiteCheckerTest {

	private static SiteChecker siteChecker = new DefaultSiteChecker();

	private static List<URI> invalidUris = new ArrayList<URI>();

	private static List<URI> wrongUris = new ArrayList<URI>();

	@BeforeClass
	public static void setup() {
		invalidUris.add(URI.create("ftp://test"));
		invalidUris.add(URI.create("file:///test"));
		invalidUris.add(URI.create("///test"));
		invalidUris.add(URI.create("//test"));
		invalidUris.add(URI.create("/test"));
		invalidUris.add(URI.create("file"));

		wrongUris.add(URI.create("https://some-site-that-does-not-exists.com"));
		wrongUris.add(URI.create("https://www.liferay.com/osb-community-theme/images/favicon.ico"));
		
		((DefaultSiteChecker)siteChecker).contentChecker = new DefaultContentChecker();
	}

	@Test
	public void testInvaildURL() {
		for (URI uri : invalidUris) {
			try {
				siteChecker.check(uri);
				fail("Passed invalid URI " + uri);
			} catch (IllegalArgumentException e) {
			}
		}
	}

	@Test
	public void testWrongURL() {
		for (URI uri : wrongUris) {
			CheckDTO check = siteChecker.check(uri);
			assertNotNull(check.errorMessage);
			System.out.println(check.errorMessage);
		}
	}

	@Test
	public void testLiferayDotCom() {
		CheckDTO check = siteChecker.check(URI.create("http://liferay.com"));
		System.out.println(check);
		assertEquals("http://liferay.com", check.originalURI);
		assertEquals("https://www.liferay.com/", check.redirectURI);
		assertEquals(200, check.statusCode);
		assertNull(check.errorMessage);
		assertEquals("Liferay Portal Enterprise Edition", check.liferayInfo.versionFromHeader);
	}

	@Test
	public void testRandomSite() {
		CheckDTO check = siteChecker.check(URI.create("http://www.latamcargo.com/"));
		System.out.println(check);
	}
	
}
