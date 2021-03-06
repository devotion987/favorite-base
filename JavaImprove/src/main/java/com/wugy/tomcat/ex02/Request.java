package com.wugy.tomcat.ex02;

import java.io.IOException;
import java.io.InputStream;

import com.wugy.tomcat.utils.ConsTomcat;

public class Request extends AbstractHttpServlet {

	private InputStream input;
	private String uri;

	public Request(InputStream input) {
		this.input = input;
	}

	public String getUri() {
		return uri;
	}

	private String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1);
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2);
		}
		return null;
	}

	public void parse() {
		// Read a set of characters from the socket
		StringBuffer request = new StringBuffer(ConsTomcat.size);
		int i;
		byte[] buffer = new byte[ConsTomcat.size];
		try {
			i = input.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for (int j = 0; j < i; j++) {
			request.append((char) buffer[j]);
		}
		System.out.print(request.toString());
		uri = parseUri(request.toString());
	}

}