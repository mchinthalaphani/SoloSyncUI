package com.charter.solo.sync.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.charter.solo.sync.rest.client.ProducerRestClient;
import com.charter.solo.sync.types.SoloSyncUIResponse;

@RestController
public class SoloSyncUIController {
	
	private static final Logger logger = LoggerFactory.getLogger(SoloSyncUIController.class);
	
	@Autowired
	private ProducerRestClient producerRestClient;
	
	@RequestMapping(value = URIConstants.TRIGGER_PRODUCER , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody SoloSyncUIResponse triggerProdcuer(@PathVariable("biller") String biller, @PathVariable("accountNumber") String id){
		logger.debug("In Solo Sync UI Controller, Triggering "+biller+ "with identifier "+id);
		return producerRestClient.executeProducer(biller, id);
		
	}

	
	

}
