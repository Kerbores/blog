package com.kerbores.utils.ua;

import java.io.Serializable;

public class UserAgent implements Serializable {
	private static final long serialVersionUID = 7025462762784240212L;
	private OperationSystem operatingSystem = OperationSystem.UNKNOWN;
	private Browser browser = Browser.UNKNOWN;
	private int id;
	private String userAgentString;

	public UserAgent(OperationSystem operatingSystem, Browser browser) {
		this.operatingSystem = operatingSystem;
		this.browser = browser;
		this.id = ((operatingSystem.getId() << 16) + browser.getId());
	}

	public UserAgent(String userAgentString) {
		Browser browser = Browser.parseUserAgentString(userAgentString);
		if (userAgentString.indexOf("Trident/7.0; rv:11.0") > 0) {
			browser = Browser.IE11;
		}
		OperationSystem operatingSystem = OperationSystem.UNKNOWN;
		if (browser != Browser.BOT) {
			operatingSystem = OperationSystem.parseUserAgentString(userAgentString);
		}
		this.operatingSystem = operatingSystem;
		this.browser = browser;
		this.id = ((operatingSystem.getId() << 16) + browser.getId());
		this.userAgentString = userAgentString;
	}

	public static UserAgent parseUserAgentString(String userAgentString) {
		return new UserAgent(userAgentString);
	}

	public Version getBrowserVersion() {
		return this.browser.getVersion(this.userAgentString);
	}

	public OperationSystem getOperatingSystem() {
		return this.operatingSystem;
	}

	public Browser getBrowser() {
		return this.browser;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.operatingSystem.toString() + "-" + this.browser.toString();
	}

	public static UserAgent valueOf(int id) {
		OperationSystem operatingSystem = OperationSystem.valueOf((short) (id >> 16));
		Browser browser = Browser.valueOf((short) (id & 0xFFFF));
		return new UserAgent(operatingSystem, browser);
	}

	public static UserAgent valueOf(String name) {
		if (name == null) {
			throw new NullPointerException("Name is null");
		}
		String[] elements = name.split("-");

		if (elements.length == 2) {
			OperationSystem operatingSystem = OperationSystem.valueOf(elements[0]);
			Browser browser = Browser.valueOf(elements[1]);
			return new UserAgent(operatingSystem, browser);
		}

		throw new IllegalArgumentException("Invalid string for userAgent " + name);
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + (this.browser == null ? 0 : this.browser.hashCode());
		result = 31 * result + this.id;
		result = 31 * result + (this.operatingSystem == null ? 0 : this.operatingSystem.hashCode());

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
		UserAgent other = (UserAgent) obj;
		if (this.browser == null) {
			if (other.browser != null)
				return false;
		} else if (!this.browser.equals(other.browser))
			return false;
		if (this.id != other.id)
			return false;
		if (this.operatingSystem == null) {
			if (other.operatingSystem != null)
				return false;
		} else if (!this.operatingSystem.equals(other.operatingSystem))
			return false;
		return true;
	}
}