/**
 * File:    SampleDriver.java
 * Project: es_dms_sample_driver
 *
 * Copyright (C) 2011-2018 Genesys Telecommunications Laboratories Inc.
 *
 * January 05, 2011
 */
package com.youtube.channel;

import com.genesyslab.mcr.smserver.channel.*;
import com.genesyslab.mcr.smserver.common.SrvUtil;
import com.genesyslab.mcr.smserver.gframework.GfrPU;
import com.genesyslab.mcr.smserver.gframework.LmsMessages;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.youtube.dm.auth.Integration;
/**
 * <p>
 * Genesys Digital Messaging Server - Sample Channel Driver.
 * <p>
 * Sample driver for Digital Messaging Server.
 * <p>
 * Copyright (C) 2011-2018 Genesys Telecommunications Laboratories Inc.
 * <p>
 * All rights reserved.
 * 
 * @version 9.0
 */
public class YoutubeDriver
	implements ChannelDriver
{
    static final private String NAME = "Genesys Sample Driver";
    static final private String VERSION = "9.0.001.03";
    static final private String COPYRIGHT_NOTE = "Copyright 2011-2018 Genesys Telecommunications Laboratories Inc.";
	Integration youtubeClient;

	/**
     * Object ChannelDriverPU passed to the driver in initialize method
     */
    protected ChannelDriverPU channelPU = null;

    /**
     * Driver parameters populated by SampleDriverParams.getConfiguration
     */
    protected YoutubeDriverParams driverParams = null;

    /**
     * Connection state with media source
     */
    protected boolean mediaConnected = false;

    /**
     * Miscellaneous constants
     */
    protected final static String REQSERVICE_SAMPLE = "Sample";
    protected final static String REQMETHOD_SENDMESSAGE = "SendMessage";

    protected final static String MSGTYPE_DIRECTMESSAGE = "DirectMessage";
    protected final static String MSGTYPE_SAMPLE = "SampleMsg";

    // Keys used in interactions
    protected final static String KEY_PFX = "_sample";
    protected final static String KEY_QUERYNAME = "QueryName";

	//sdrCloudInterface restIntegration;
    /**
     * Object SampleMonitor(), which imitates inbound data flow and submits interactions to Interaction Server.
     */
    private YoutubeMonitor sampleMonitor = null;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.genesyslab.mcr.smsserver.channel.ChannelDriver#initialize(com.genesyslab.mcr.smsserver.channel.ChannelPU)
     */
    public void initialize(ChannelDriverPU channelDriverPU)
	    throws Exception
    {
	this.channelPU = (ChannelPU) channelDriverPU;
	final String logRpfx = logPfx("initialize");
	this.driverParams = new YoutubeDriverParams(this);
	//this.restIntegration = new sdrCloudInterface(this.driverParams.sdrAPIToken,this.driverParams.sdrAPIKey,this.driverParams.sdrAPIURL);
	try {
	    driverParams.getConfiguration();

		getGfrPU().trace(LmsMessages.generic_std2, logRpfx, "params" + driverParams.sdrAPIToken + " "+driverParams.sdrAPIKey+" "+driverParams.sdrAPIURL);
		this.youtubeClient = new Integration(this.driverParams.sdrAPIToken, this.driverParams.sdrAPIKey, this.driverParams.sdrAPIURL);
      }
	catch (Exception ex) {
	    getGfrPU().trace(LmsMessages.error2, logRpfx, "Failed to get configuration parameters, " + ex.getMessage());
	    throw new Exception("Channel driver initiaization failed");
	}

	// getTenantId() is introduced since DMS 9.0.001.02 ----------------------------------------
	Integer channnel_tenantId = channelPU.getChannelConnector().getInboundRoute().getTenantId();
	getGfrPU().trace(LmsMessages.generic_std2, logRpfx, "channnel tenantId = " + channnel_tenantId);
	// -----------------------------------------------------------------------------------------

	// create monitor, which imitates input data source and submit inbound interactions
	sampleMonitor = new YoutubeMonitor(this);
	// start monitor
	sampleMonitor.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#shutdown()
     */
    public void shutdown()
	    throws Exception
    {
	final String logRpfx = logPfx("shutdown");

	getGfrPU().trace(LmsMessages.generic_std2, logRpfx, "request to shutdown channel driver");

	disconnect();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#connect()
     */
    public void connect()
	    throws Exception
    {
	final String logRpfx = logPfx("connect");
	getGfrPU().trace(LmsMessages.drv_trc1, logRpfx,
			 "request to driver to change connection state to 'connect', currently connected= "
				 + mediaConnected);

	if (mediaConnected) {
	    channelPU.connectionState(true, "driver was connected already");
	    return;
	}

	try {
	    // connecting process is implemented here
	    channelPU.connectionState(true, "connected now");
	    mediaConnected = true;
	}
	catch (Exception ex) {
	    mediaConnected = false;
	    channelPU.connectionState(false, "connecting attempt failed");
	    throw new Exception("connecting attempt failed", ex);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#disconnect()
     */
    public void disconnect()
	    throws Exception
    {
	final String logRpfx = logPfx("disconnect");
	getGfrPU().trace(LmsMessages.drv_trc1, logRpfx,
			 "request to driver to change connection state to 'disconnect', currently connected= "
				 + mediaConnected);

	if (!mediaConnected) {
	    channelPU.connectionState(false, "driver was disconnected already");
	    return;
	}

	try {
	    // disconnecting process is implemented here
	    channelPU.connectionState(false, "disconnected now");
	    mediaConnected = false;
	}
	catch (Exception ex) {
	    mediaConnected = true;
	    channelPU.connectionState(true, "disconnecting attempt failed");
	    throw new Exception("disconnecting attempt failed", ex);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#isConnected()
     */
    public boolean isConnected()
	    throws Exception
    {
	return mediaConnected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#exeModeChanged(java.lang.String)
     */
    public void exeModeChanged(String exeMode)
    {
	final String logRpfx = logPfx("exeModeChanged");

	String logRec = String.format("notification to driver about execution mode changing, now '%s'", exeMode);

	getGfrPU().trace(LmsMessages.twt_trc, logRpfx, logRec);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#chatSessionNotification(java.lang.String,
     * com.genesyslab.platform.commons.collections.KeyValueCollection)
     */
    // public void chatSessionNotification(String notification, KeyValueCollection messageData)
    // {
    // final String logRpfx = logPfx("chatSessionNotification");
    //
    // String logRec = String.format("notification %s", notification);
    // getGfrPU().trace(LmsMessages.twt_trc, logRpfx, logRec);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#configurationChanged(com.genesyslab.platform.commons.
     * collections .KeyValueCollection, com.genesyslab.platform.commons.collections.KeyValueCollection,
     * com.genesyslab.platform.commons.collections.KeyValueCollection)
     */
    public void configurationChanged(KeyValueCollection kvcSectionsChanged, KeyValueCollection kvcSectionsCreated,
	    KeyValueCollection kvcSectionsDeleted)
    {
	final String logRpfx = logPfx("configurationChanged");

	String logRec = String.format("configuration changes in channel");
	getGfrPU().trace(LmsMessages.generic_std2, logRpfx, logRec);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#getName()
     */
    public String getName()
    {
	return NAME + ". " + COPYRIGHT_NOTE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#getVersion()
     */
    public String getVersion()
    {
	return VERSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#getMediaAccount()
     */
    public String getMediaAccount()
    {
	return String.format("%s,Version=%s", NAME, VERSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#getService(java.lang.String, java.lang.String,
     * com.genesyslab.platform.commons.collections.KeyValueCollection)
     */
    public KeyValueCollection getService(String serviceName, String methodName, KeyValueCollection requestParams)
	    throws Exception
    {
	final String logRpfx = logPfx("getService");

	if (!mediaConnected) {
	    String errMsg = String.format("connection with instagram was not established, service= '%s', method= '%s'",
					  serviceName, methodName);
	    getGfrPU().trace(LmsMessages.error2, logRpfx, errMsg);
	    return null;
	}

	getGfrPU().trace(LmsMessages.received_data, logRpfx,
			 String.format("request service= '%s', method= '%s', data= %s", serviceName, methodName,
				       requestParams.toStringLine()));

	return null;
    }

    /**
     * Helper method, which returns GfrPU object
     * 
     * @return
     */
    protected GfrPU getGfrPU()
    {
	return (null == channelPU) ? null : channelPU.getGfrPU();
    }

    /**
     * Helper method, which constructs signature to use it in creating log messages.
     * 
     * @param secondName
     * @return
     */
    protected String logPfx(String secondName)
    {
	return "(" + channelPU.getChannelName() + ").(driver).(" + secondName + "):";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#umsRequest(java.lang.String, java.lang.String,
     * java.lang.String, com.genesyslab.platform.commons.collections.KeyValueCollection)
     */
    public String umsRequest(String serverRequestId, String serviceName, String methodName,
	    KeyValueCollection requestData)
	    throws Exception
    {
	final String logRpfx = logPfx("umsRequest");

	// ..........................................................................................
	if (!mediaConnected) {
	    getGfrPU().trace(LmsMessages.error2, logRpfx, String
		    .format("connection with media source was not established, umsRequest id=%s", serverRequestId));
	    channelPU.umsFaultResponse(serverRequestId, 1101, "connection with media source was not established", null);
	    return serverRequestId;
	}

	// ..........................................................................................
	getGfrPU().trace(LmsMessages.received_data, logRpfx,
			 String.format("umsRequest id=%s, serviceName=%s, methodName=%s, data=%s", serverRequestId,
				       serviceName, methodName, requestData.toStringLine()));

	// ..........................................................................................
	if (!REQSERVICE_SAMPLE.equalsIgnoreCase(serviceName)) {
	    String errMsg = String.format("unknown serviceName received %s", serviceName);
	    getGfrPU().trace(LmsMessages.error2, logRpfx, errMsg);
	    channelPU.umsFaultResponse(serverRequestId, 1102, errMsg, null);
	    return serverRequestId;
	}

	// ..........................................................................................
	if (REQMETHOD_SENDMESSAGE.equalsIgnoreCase(methodName))
	    return sendMessageRequest(serverRequestId, requestData);
	else {
	    String errMsg = String.format("unknown methodName received %s", methodName);
	    getGfrPU().trace(LmsMessages.error2, logRpfx, errMsg);
	    channelPU.umsFaultResponse(serverRequestId, 1102, errMsg, null);
	    return serverRequestId;
	}
    }

    /**
     * Executes ESP request 'send message' received.
     * 
     * @param serverRequestId
     * @param requestData
     * @return
     * @throws Exception
     */
    private String sendMessageRequest(String serverRequestId, KeyValueCollection requestData)
	    throws Exception
    {
    	restClient restresp = new restClient();

	final String logRpfx = logPfx("sendMessageRequest");

	// ..........................................................................................
	String msgType = requestData.getString(KEY_PFX + ChannelDriverConstants.UMSKEY_MsgType);

	// ..........................................................................................
	if (!MSGTYPE_DIRECTMESSAGE.equalsIgnoreCase(msgType)) {
	    String errMsg = String.format("incorrect message type in request %s", serverRequestId);
	    getGfrPU().trace(LmsMessages.error2, logRpfx, errMsg);
	    channelPU.umsFaultResponse(serverRequestId, 1103, errMsg, null);
	    return serverRequestId;
	}
	// ..........................................................................................
	KeyValueCollection resultData = new KeyValueCollection();

	try {

		getGfrPU().trace(LmsMessages.error2, logRpfx, String.format("sendMessageRequest Result Data exist"));

		String method = requestData.getString("MethodType");

		getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube Method Type... " + method);

		if(method.equals("reply")) {
			String ID = requestData.getString("ID");

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube ID ... " + ID);
			String MSG = requestData.getString("MsgText");

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube MSG... " + MSG);
			String response = this.youtubeClient.replyOnSpecificYotubeComment(ID, MSG);
			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube JAR response... " + response);

		}else if(method.equals("comment")) {
			String ID = requestData.getString("ID");

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube ID ... " + ID);
			String MSG = requestData.getString("MsgText");

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube MSG... " + MSG);


			String response = this.youtubeClient.sendYotubeMessage(ID, MSG);
		    getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube JAR response... " + response);

		}if(method.equals("delete")) {
			String ID = requestData.getString("ID");

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube ID ... " + ID);

			String response =	this.youtubeClient.deleteYotubeComment(ID);

			getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube JAR response... " + response);
		}

	    // The next line imitates sending a message to media
	    getGfrPU().trace(LmsMessages.sent_data, logRpfx, " Youtube direct message... " + requestData.toStringLine());

	    // Send positive response with a report about processing
	    // The next line imitates adding of the message's id for the message sent
	    resultData.addString(KEY_PFX + ChannelDriverConstants.UMSKEY_MsgId, "ID-0001");
	    channelPU.umsResponse(serverRequestId, resultData);
	    return serverRequestId;
	}
	catch (Exception ex) {
	    getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
	    // Send negative response with a report about processing
	    channelPU.umsFaultResponse(serverRequestId, 1105, ex.getMessage(), null);
	    return serverRequestId;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smsserver.channel.ChannelDriver#submitMessageResult(java.lang.String, boolean)
     */
    public void submitMessageResult(String requestId, boolean success)
    {
	final String logRpfx = logPfx("submitMessageResult");

	getGfrPU().trace(LmsMessages.called, logRpfx,
			 String.format("with result:: requestId=%s success=%b", requestId, success));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#submitMessageResult(java.lang.String, boolean,
     * com.genesyslab.platform.commons.collections.KeyValueCollection)
     */
    public void submitMessageResult(String requestId, boolean success, KeyValueCollection resultData)
	    throws Exception
    {
	final String logRpfx = logPfx("submitMessageResult");

	String logRec = String.format("with result:: requestId=%s, success=%b, resultData=...\n%s", requestId, success,
				      resultData);
	getGfrPU().trace(LmsMessages.called, logRpfx, logRec);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.genesyslab.mcr.smserver.channel.ChannelDriver#getAttachments(java.lang.String,
     * com.genesyslab.mcr.smserver.channel.AttachmentsProcessor)
     */
    @Override
    public int getAttachments(String interactionId, AttachmentsProcessor attachmentsProcessor)
	    throws Exception
    {
	return 0;
    }
}
