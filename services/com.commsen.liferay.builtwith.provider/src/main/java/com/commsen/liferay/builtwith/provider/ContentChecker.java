package com.commsen.liferay.builtwith.provider;

import org.jsoup.nodes.Document;

import com.commsen.liferay.builtwith.api.LiferayDTO;

public interface ContentChecker {

	public void check (Document document, LiferayDTO liferayInfo);
}
