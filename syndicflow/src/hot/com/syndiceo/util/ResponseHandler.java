package com.syndiceo.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseHandler {
	
	private Object response;

	public ResponseHandler(Object response) {
		super();
		this.response = response;
	}

	public void setContentType(String contentType) {
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse)response).setContentType(contentType);
		} else {
			try {
				response.getClass().getMethod("setContentType", String.class).invoke(response, contentType);
			} catch (Exception e) {
				throw new RuntimeException("Unsupporter response type",e);
			}
		}
	}

	public void setContentLength(Integer contentLength) {
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse)response).setContentLength(contentLength);
		} else {
			try {
				response.getClass().getMethod("setBufferSize", Integer.class).invoke(response, contentLength);
			} catch (Exception e) {
				throw new RuntimeException("Unsupporter response type",e);
			}
		}
	}

	public void setHeader(String param, String value) {
		if (response instanceof HttpServletResponse) {
			((HttpServletResponse)response).setHeader(param,value);
		}
	}
	
	public OutputStream getOutputStream() throws IOException {
		if (response instanceof HttpServletResponse) {
			return ((HttpServletResponse)response).getOutputStream();
		} else {
			try {
				return (OutputStream)response.getClass().getMethod("getPortletOutputStream").invoke(response);
			} catch (Exception e) {
				throw new RuntimeException("Unsupporter response type",e);
			}
		}
	}
	public PrintWriter getWriter() throws IOException {
		if (response instanceof HttpServletResponse) {
			return ((HttpServletResponse)response).getWriter();
		} else {
			try {
				return (PrintWriter)response.getClass().getMethod("getPortletWriter").invoke(response);
			} catch (Exception e) {
				throw new RuntimeException("Unsupporter response type",e);
			}
		}
	}
}
