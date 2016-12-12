package com.charter.solo.sync.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.charter.solo.sync.service.SoloSyncUIDbConnector;
import com.charter.solo.sync.types.SoloSyncUIResponse;

@Component
public class ProducerRestClient {

	private static final Logger log = LoggerFactory.getLogger(ProducerRestClient.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	SoloSyncUIDbConnector dbImpl;

	@Value("${event.manager.url}")
	private String url;

	private String xmlString = null;

	private HttpStatus statusCode = null;

	private SoloSyncUIResponse response = null;

	public SoloSyncUIResponse executeProducer(String biller, String id) {

		String fileName = null;

		if (biller != null && biller.equalsIgnoreCase("csg")) {
			fileName = "csg.xml";
			response = executeCsgProducer(fileName, id);
		} else if (biller != null && biller.equalsIgnoreCase("icoms")) {
			response = executeIcomsProducer(fileName, id);
			fileName = "icoms.xml";
		}

		return response;

	}

	private SoloSyncUIResponse executeCsgProducer(String fileName, String accountNumber) {
			
		log.debug("In ExecuteCsgProducer method() with [fileName = "+fileName+" , accountNumber = "+accountNumber+" ]");
		
		if (fileName != null && accountNumber != null) {

			InputStream in = null;

			try {

				in = this.getClass().getResourceAsStream("/stubs/" + fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(in);
				doc.getDocumentElement().normalize();
				doc.getElementsByTagName("AccountId").item(1).setTextContent(accountNumber);

				DOMSource domSource = new DOMSource(doc);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				xmlString = writer.toString();
				log.debug(xmlString);
			} catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		HttpEntity<String> request = new HttpEntity<String>(xmlString, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
		statusCode = result.getStatusCode();
		log.debug("Triggered Event Manager and returned with Response staus code : "+statusCode.toString());
		
		SoloSyncUIResponse resp = null;

		if (statusCode != null && statusCode == HttpStatus.OK) {

			Long msgId = Long.parseLong(result.getBody());
			resp = dbImpl.findMessage(msgId);
			
			if(resp != null){
				log.debug("Found a record in database with message Id :"+msgId);
			}
			
			else{
				
				log.debug("Did not find a record in database with message Id :"+msgId);

			}

		}
		return resp;
	}

	private SoloSyncUIResponse executeIcomsProducer(String fileName, String locationId) {
		
		log.debug("In ExecuteIcomsProducer method() with [fileName = "+fileName+" , locationId = "+locationId+" ]");


		if (fileName != null && locationId != null) {

			InputStream in = null;

			try {

				in = this.getClass().getResourceAsStream("/stubs/" + fileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(in);
				doc.getDocumentElement().normalize();
				doc.getElementsByTagName("LocationIdentifier").item(0).setTextContent(locationId);

				DOMSource domSource = new DOMSource(doc);
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer = tf.newTransformer();
				transformer.transform(domSource, result);
				xmlString = writer.toString();
				log.debug(xmlString);
			} catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		HttpEntity<String> request = new HttpEntity<String>(xmlString, headers);
		ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
		statusCode = result.getStatusCode();
		log.debug("Triggered Event Manager and returned with Response staus code : "+statusCode.toString());
		SoloSyncUIResponse resp = null;

		if (statusCode != null && statusCode == HttpStatus.OK) {

			Long msgId = Long.parseLong(result.getBody());
			resp = dbImpl.findMessage(msgId);
			
			if(resp != null){
				log.debug("Found a record in database with message Id :"+msgId);
			}
			
			else{
				
				log.debug("Did not find a record in database with message Id :"+msgId);

			}

		}
		
		return resp;

	}

}
