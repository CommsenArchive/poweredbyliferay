package com.commsen.liferay.builtwith.provider;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.osgi.service.component.annotations.Component;

import com.commsen.liferay.builtwith.api.LiferayDTO;
import com.commsen.liferay.builtwith.api.PortletDTO;

@Component
public class DefaultContentChecker implements ContentChecker {

	public void check(Document document, LiferayDTO liferayInfo) {
		testForBuildVersion(liferayInfo, document);
		testForPortlets(liferayInfo, document);
		testForLanguages(liferayInfo, document);
	}

	private void testForBuildVersion(LiferayDTO liferayInfo, Document document) {

		Pattern pattern = Pattern.compile("\\&b=(\\d\\d\\d\\d)\\&");
		Elements elements = document.getElementsByTag("link");
		for (Element element : elements) {
			String link = element.attr("href");
			Matcher matcher = pattern.matcher(link);
			if (matcher.find()) {
				liferayInfo.detectedBuildNumbers.add(matcher.group(1));
			}
		}
		elements = document.getElementsByTag("script");
		for (Element element : elements) {
			String script = element.attr("src");
			Matcher matcher = pattern.matcher(script);
			if (matcher.find()) {
				liferayInfo.detectedBuildNumbers.add(matcher.group(1));
			}
		}
	}

	private void testForPortlets(LiferayDTO liferayInfo, Document document) {

		Elements portlets = document.getElementsByClass("portlet-boundary");
		Map<String, PortletDTO> cache = new WeakHashMap<String, PortletDTO>();
		for (Element element : portlets) {
			PortletDTO portlet = portletFromId(element.id());
			String key = portlet.moduleName + "|" + portlet.portletName;
			portlet.count = 1;
			if (cache.containsKey(key)) {
				portlet.count += cache.get(key).count;
			}
			cache.put(key, portlet);
		}
		liferayInfo.portlets.addAll(cache.values());
	}

	private PortletDTO portletFromId(String s) {
		PortletDTO portlet = new PortletDTO();
		String tmp = s.replaceAll("_INSTANCE_.*", "");
		String[] split = tmp.split("_WAR_");
		portlet.portletName = split[0].replace("p_p_id_", "").replaceAll("_*$", "").replaceAll("^_*", "");
		if (split.length > 1)
			portlet.moduleName = split[1].replaceAll("_*$", "").replaceAll("^_*", "");
		return portlet;
	}

	private void testForLanguages(LiferayDTO liferayInfo, Document document) {

		liferayInfo.primaryLanguage = document.getElementsByTag("html").get(0).attr("lang");

		Elements langTags = document.getElementsByAttribute("hreflang");
		for (Element element : langTags) {
			String lang = element.attr("hreflang");
			if (lang.equals("x-default")) continue;
			liferayInfo.supportedLanguages.add(lang);
		}
	}

}
