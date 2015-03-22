package com.pairoo.frontend.file;

import java.io.File;
import java.util.Map;

import org.springframework.integration.annotation.Headers;
import org.springframework.integration.annotation.Payload;

public class InboundFileProcessor {
	public File onNewFileArrival(@Headers Map<String, Object> headers,
			@Payload File file) {

		System.out.printf("A new file has arrived deposited into "
				+ "the input folder at the absolute path %s \n", file
				.getAbsolutePath());

		System.out.println("The headers are:");
		for (String k : headers.keySet()) {
			System.out.println(String.format("%s=%s", k, headers.get(k)));
		}

		return file;
	}
}
