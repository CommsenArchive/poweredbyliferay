package com.commsen.liferay.builtwith.api;

import org.osgi.dto.DTO;

public class PortletDTO extends DTO implements Comparable<PortletDTO> {

	public String moduleName;

	public String portletName;

	public int count;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + ((portletName == null) ? 0 : portletName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PortletDTO other = (PortletDTO) obj;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		if (portletName == null) {
			if (other.portletName != null)
				return false;
		} else if (!portletName.equals(other.portletName))
			return false;
		return true;
	}

	@Override
	public int compareTo(PortletDTO o) {
		if (this == o) {
			return 0;
		}
		if (this.moduleName == null) {
			if (o.moduleName == null)
				return 0;
			return -1;
		}
		if (o.moduleName == null) return 1;
		if (!moduleName.equals(o.moduleName)) {
			return this.moduleName.compareTo(o.moduleName);
		}
		if (portletName == null) {
			if (o.portletName == null)
				return 0;
			return -1;
		}
		return this.portletName.compareTo(o.portletName);
	}

}
