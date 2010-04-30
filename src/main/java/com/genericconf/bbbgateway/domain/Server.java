/**
 * Copyright 2010 Generic Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.genericconf.bbbgateway.domain;

import org.apache.commons.lang.StringUtils;

public class Server extends Entity {

	private static final long serialVersionUID = 1L;

	private String domain;
	private String context;
	private String securitySalt;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSecuritySalt() {
		return securitySalt;
	}

	public void setSecuritySalt(String securitySalt) {
		this.securitySalt = securitySalt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((securitySalt == null) ? 0 : securitySalt.hashCode());
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
		Server other = (Server) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (securitySalt == null) {
			if (other.securitySalt != null)
				return false;
		} else if (!securitySalt.equals(other.securitySalt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Server [context=" + context + ", domain=" + domain + ", securitySalt=" + StringUtils.repeat("*", securitySalt.length()) + "]";
	}

}