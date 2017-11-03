/*package com.concretepage.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;


	@Component
	public class Voice  
	implements ApplicationListener<ApplicationReadyEvent> {

	  /**
	   * This event is executed as late as conceivably possible to indicate that 
	   * the application is ready to service requests.
	   *//*
	  @Override
	  public void onApplicationEvent(final ApplicationReadyEvent event) {
	 
		  Configuration configuration = new Configuration();

	        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
	        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
	        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		StreamSpeechRecognizer recognizer = null;
		try {
			recognizer = new StreamSpeechRecognizer(configuration);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream stream = null;
		try {
			stream = new FileInputStream(new File("a2002011001-e02.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	        recognizer.startRecognition(stream);
		SpeechResult result;
	        while ((result = recognizer.getResult()) != null) {
		    System.out.format("Hypothesis: %s\n", result.getHypothesis());
		}
		recognizer.stopRecognition();
	 
	    return;
	  }
	 
	} // class*/
