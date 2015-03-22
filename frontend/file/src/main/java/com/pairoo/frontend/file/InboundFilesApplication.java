package com.pairoo.frontend.file;

import java.io.File;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

/**
 * Integration application for inbound files.
 */
public class InboundFilesApplication {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"springBeans.xml", InboundFilesApplication.class);
		File inDir = (File) new DirectFieldAccessor(context.getBean(FileReadingMessageSource.class)).getPropertyValue("directory");
		File outDir = (File) new DirectFieldAccessor(context.getBean(FileWritingMessageHandler.class)).getPropertyValue("destinationDirectory");
		System.out.println("Input directory is: " + inDir.getAbsolutePath());
		System.out.println("Output directory is: " + outDir.getAbsolutePath());
		System.out.println("===================================================");
	}
}
