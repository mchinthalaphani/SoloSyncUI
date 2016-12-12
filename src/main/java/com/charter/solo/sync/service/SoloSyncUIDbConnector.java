package com.charter.solo.sync.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.charter.solo.sync.types.SoloSyncUIResponse;
import com.charter.solosync.db.connector.SolosyncMessageConnector;
import com.charter.solosync.db.model.SolosyncMessage;

public class SoloSyncUIDbConnector {
	
	@Autowired
	private SolosyncMessageConnector solosyncmessageConnector;
	
	public SoloSyncUIResponse findMessage(Long msgId){
		
		SolosyncMessage message = solosyncmessageConnector.findOne(msgId);
		return soloSyncMessageMapper(message);
		
	}
	
	private SoloSyncUIResponse soloSyncMessageMapper(SolosyncMessage message){
		
		SoloSyncUIResponse response = new SoloSyncUIResponse();
		response.setMessageId(message.getSolosyncMessageId());
		response.setTransactionId(message.getSolosyncTransId());
		response.setMessageStatus(message.getMessageStatus());
		response.setCreatedTimestamp(message.getCreateTimestamp());
		
		return response;
	}

}
