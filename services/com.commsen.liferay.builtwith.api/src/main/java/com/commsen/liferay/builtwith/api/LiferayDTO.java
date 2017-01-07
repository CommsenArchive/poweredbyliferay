package com.commsen.liferay.builtwith.api;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.osgi.dto.DTO;

public class LiferayDTO extends DTO {

	public String versionFromHeader;

	public Set<String> detectedBuildNumbers = new HashSet<String>();

	public String primaryLanguage;

	public Set<String> supportedLanguages = new HashSet<String>();

	public SortedSet<PortletDTO> portlets = new TreeSet<PortletDTO>();

}