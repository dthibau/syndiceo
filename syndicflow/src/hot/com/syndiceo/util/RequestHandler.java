package com.syndiceo.util;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestHandler  {

	private Object request;
	private String scheme;
	private String serverName;
	private Integer serverPort;
	private String contextPath;
	
	public RequestHandler(Object request) {
		super();
		this.request = request;
		init();
	}
	
	private void init() {
		try {
			this.scheme = (String)this.request.getClass().getMethod("getScheme").invoke(this.request);
			this.serverName = (String)this.request.getClass().getMethod("getServerName").invoke(this.request);
			this.serverPort = (Integer)this.request.getClass().getMethod("getServerPort").invoke(this.request);
			this.contextPath = (String)this.request.getClass().getMethod("getContextPath").invoke(this.request);
		} catch (Exception e) {
			throw new RuntimeException("Unsupported request type",e);
		}
	}

	public URI getURI() throws URISyntaxException {
		return new URI( getScheme(), null, getServerName(), getServerPort(), getContextPath(), null, null);
	}
	
	public Object getRequest() {
		return request;
	}

	public String getScheme() {
		return scheme;
	}

	public String getServerName() {
		return serverName;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public String getContextPath() {
		return contextPath;
	}
}
