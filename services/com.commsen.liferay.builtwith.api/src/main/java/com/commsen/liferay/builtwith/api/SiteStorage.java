package com.commsen.liferay.builtwith.api;

import java.net.URI;

public interface SiteStorage {

	public void save (CheckDTO checkDTO) throws SiteStorageException;

	public CheckDTO get (URI domain) throws SiteStorageException;

}
