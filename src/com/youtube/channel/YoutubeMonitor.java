/**
 * File:    SampleMonitor.java
 * Project: es_dms_sample_driver
 *
 * Copyright (C) 2011-2018 Genesys Telecommunications Laboratories Inc.
 *
 * January 05, 2011
 */
package com.youtube.channel;

import com.genesyslab.mcr.smserver.channel.ChannelConnectors.InboundRoute;
import com.genesyslab.mcr.smserver.channel.ChannelDriverConstants;
import com.genesyslab.mcr.smserver.common.SrvUtil;
import com.genesyslab.mcr.smserver.gframework.FieldNames;
import com.genesyslab.mcr.smserver.gframework.LmsMessages;
import com.genesyslab.platform.commons.collections.KeyValueCollection;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.openmedia.protocol.interactionserver.requests.interactionmanagement.RequestSubmit;
import com.youtube.dm.auth.Integration;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
public class YoutubeMonitor extends Thread
{
    private YoutubeDriver sampleDriver;
    private YoutubeDriverParams driverParams = null;
    private long lastMsgsFetchTime = 0;

    /**
    // @param SampleMonitor
     */
	//sdrCloudInterface  youtubeClient ;
	Integration youtubeClient;

	public YoutubeMonitor(YoutubeDriver sampleDriver)
    {
	super("YoutubeMonitor");
	setDaemon(true);

	this.sampleDriver = sampleDriver;
	this.driverParams = sampleDriver.driverParams;
	this.youtubeClient = sampleDriver.youtubeClient;
	//this.youtubeClient=new sdrCloudInterface(sampleDriver.driverParams.sdrAPIToken,sampleDriver.driverParams.sdrAPIToken,sampleDriver.driverParams.sdrAPIURL);
	final String logRpfx = sampleDriver.logPfx("YoutubeMonitor.<init>");
	sampleDriver.getGfrPU().trace(LmsMessages.called, logRpfx, "");
	sampleDriver.getGfrPU().trace(25888, logRpfx, sampleDriver.driverParams.sdrAPIURL);

	sampleDriver.getGfrPU().trace("Monitor Initialized ");

	}

	public String getBase64ByUrl(String imageUrl) throws IOException {
		byte[] byteChunk=null;
		URL ur= null;
		try {
			ur = new URL(imageUrl);
		} catch (MalformedURLException e) {
			sampleDriver.getGfrPU().trace(25881, "YoutubeMonitor.<init>", "image url "+imageUrl);


		}

		//sol6 extract right big image
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			is = ur.openStream ();
			byteChunk = new byte[1024]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		}
		catch (IOException e) {
			sampleDriver.getGfrPU().trace(25882, "YoutubeMonitor.<init>", "convert image url to byte "+e.getMessage());

		}
		finally {
			if (is != null) { is.close(); }
		}
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    public void run()
    {
	final String logRpfx = sampleDriver.logPfx("YoutubeMonitor.run");
	//sampleDriver.getGfrPU().trace("Monitor run ");

		int samplingperiod=5;
	lastMsgsFetchTime = System.currentTimeMillis() - samplingperiod;
	int messagesCreated = 0;
//	sampleDriver.getGfrPU().trace("Monitor run 2 ");

	// -------------------------------------
	if (0 == driverParams.messagesCreateMax)
	{
	//	sampleDriver.getGfrPU().trace("Monitor if ");

		return;}

	// -------------------------------------

	while (true) {
	    // Check the driver's connection state -------------------------------------------------
		//sampleDriver.getGfrPU().trace("Monitor while ");

	    if (!sampleDriver.mediaConnected) {
	    	
		try {
			sampleDriver.getGfrPU().trace("Driver not connected put to sleep ");

		    sleep(1000);
		}
		catch (InterruptedException ex) {
		//	sampleDriver.getGfrPU().trace("Monitor Catch 1  ");
		    sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
		}
		continue;
	    }

	    // Control messages' generation periodicity --------------------------------------------
	    if (System.currentTimeMillis() - lastMsgsFetchTime < samplingperiod ) {
		try {
		sampleDriver.getGfrPU().trace("sample period sleep ");

		    sleep(1000);
		}
		catch (InterruptedException ex) {
		//	sampleDriver.getGfrPU().trace("Monitor Catch 2");
		    sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
		}
		continue;
	    }

	    // Form and submit a message------------------------------------------------------------
	    if (messagesCreated <driverParams.messagesCreateMax ) {
		try {
			sampleDriver.getGfrPU().trace("Youtube Monitor");

		//	String r = restobj.getInstagramFeedrequest(driverParams.sdraccounturl);
		////	String r = restobj.GetInstagramFeed();
	     //	String r=	insta.getNewInstagramComments();
			//sdrCloudInterface sdr = new sdrCloudInterface();
			String r = this.youtubeClient.getYotubeComment();
			if(!(r==null || r.isEmpty()))
			{
				sampleDriver.getGfrPU().trace("Youtube Message Received ");
			JSONObject obj = new JSONObject(r);
			JSONArray YoutubeFeed = obj.getJSONArray("updates");
			for (int i = 0; i < YoutubeFeed.length(); i++) {
				++messagesCreated;
				JSONObject currentobj = YoutubeFeed.getJSONObject(i);
				String commentid = currentobj.getString("id");
				String commentDate = currentobj.getString("time");
				String commentAuthor = currentobj.getString("username");
				String commentText = currentobj.getString("text");
				String decodeText=URLDecoder.decode(commentText, StandardCharsets.UTF_8.toString());
				commentText=decodeText;
				String commentThumbnail = currentobj.getString("userThumbnail");
				String videoID = currentobj.getString("videoID");
				String videoThumbnail = currentobj.getString("videoThumbnail");
				String videoTitle = currentobj.getString("videoTitle");
				String encodeTitle=URLDecoder.decode(videoTitle, StandardCharsets.UTF_8.toString());
				videoTitle=encodeTitle;
				String videoDescription = currentobj.getString("videoDescription");
				String videoOwner = currentobj.getString("videoOwner");
				String videoDate = currentobj.getString("videoDate");
				String msgId = "SDRYou-" + 1000000 + messagesCreated;
				sampleDriver.getGfrPU().trace(" Youtube Comment :"+commentid);
				KeyValueCollection messageData = new KeyValueCollection();
				messageData.addString(YoutubeDriver.KEY_PFX + ChannelDriverConstants.UMSKEY_MsgType,
						YoutubeDriver.MSGTYPE_SAMPLE);
				messageData.addString(YoutubeDriver.KEY_PFX + ChannelDriverConstants.UMSKEY_MsgId, msgId);

				// SM Server's required fields =====================================================
				messageData.addString(FieldNames.UMS_Channel, sampleDriver.channelPU.getChannelName());

				messageData.addString(ChannelDriverConstants.UMSKEY_UserName, commentAuthor);

				messageData.addString(ChannelDriverConstants.UMSKEY_FirstName, commentAuthor);
				messageData.addString(ChannelDriverConstants.UMSKEY_MsgPlainText, commentText);
				messageData.addString(ChannelDriverConstants.UMSKEY_FromAddr, commentAuthor);
				messageData.addString(ChannelDriverConstants.UMSKEY_ToAddr, videoOwner);

				messageData.addString(ChannelDriverConstants.UMSKEY_MsgPlainText, commentText);

				messageData.addString(ChannelDriverConstants.UMSKEY_MsgOriginTime, commentDate);
				//================================================
				messageData.addString(ChannelDriverConstants.UMSKEY_MediaType, "youtube");
				messageData.addUTFString("YoutubeUserName", commentAuthor);
				messageData.addString("commentID", commentid);
				messageData.addUTFString("commentDate", commentDate);
				messageData.addUTFString("commentAuthor", commentAuthor);
				messageData.addUTFString("commentText", commentText);

				messageData.addUTFString("videoAuthor", videoOwner);


				messageData.addUTFString("videoDescription", videoDescription);
				messageData.addUTFString("videoTitle", videoTitle);

				byte[] commentThumbnailArr = Base64.getDecoder().decode(getBase64ByUrl(commentThumbnail));
				messageData.addBinary("commentAuthorImg",commentThumbnailArr );
				byte[] videoThumbnailArr = Base64.getDecoder().decode(getBase64ByUrl(videoThumbnail));
				messageData.addBinary("videoImg", videoThumbnailArr);
				messageData.addUTFString("videoID", videoID);

				messageData.addBinary("videoAuthorImg",commentThumbnailArr );
				messageData.addUTFString("videoDate", videoDate);
				// =================================================================================

				sampleDriver.getGfrPU().trace(LmsMessages.generic_trc2, logRpfx,
						"data from Youtube Message...\n" + messageData.toStringLine());

				// Submit message ============================================

				submitAndWait(messageData);

				// ===========================================================



			}
		}
			}
		catch (Exception ex) {
		    sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
		}

		lastMsgsFetchTime = System.currentTimeMillis();
	    }
	}
    }

  /*  *//**
     * The method submits request to Interaction Server and waits for a response from it. If there is no connection to
     * Interaction Server exists, the method tries to repeat the request up to driverParams.itxAttemptsMax times with
     * interval driverParams.itxRepeatTimeout seconds.
     * 
     //* @param messageData
     *            content of a message to send
     //* @param messageMediaType
     *            interaction media type
     * @return true - successfully sent request, false - failed to send
    // * @throws Exception
     *             if failed to deliver request to Interaction Server
     */
    private boolean submitAndWait(KeyValueCollection messageData)
	    throws Exception
    {
	final String logRpfx = sampleDriver.logPfx("submitAndWait Youtube");

	// Form RequestSubmit object (refer to Genesys Platform SDK) ...............................

	// Set interaction attributes
	RequestSubmit requestSubmit = RequestSubmit.create(driverParams.inboundMedia, driverParams.itxType);
	requestSubmit.setInteractionSubtype(driverParams.itxSubType);
	requestSubmit.setReferenceId(sampleDriver.channelPU.getNextItxReferenceId());
	requestSubmit.setUserData(messageData);
	// Get and set tenant ID and interaction queue name
	InboundRoute inbRoute = sampleDriver.channelPU.getChannelConnector().getInboundRoute();
	requestSubmit.setTenantId(inbRoute.getTenantId());
	requestSubmit.setQueue(inbRoute.getPagingQName());

	// Print the request to a log file
	sampleDriver.getGfrPU().trace(LmsMessages.sent_data, logRpfx, "itx request...\n" + requestSubmit);

	// Assign a unique reference ID to the request
	int refID = requestSubmit.getReferenceId();

	// Send the request to Interaction Server ..................................................
	int attemptNum = 0; // the request's send attempt number

	while (true) {
	    try {
		++attemptNum;

		Message responseMsg = sampleDriver.channelPU
			.sendItxRequestAndWait(requestSubmit, 1000 * driverParams.itxSubmitTimeout);
		if (null == responseMsg) // failed to get a response from Interaction Server during specified timeout
		    throw new Exception("timeout on sendItxRequestAndWait");

		// Print a response received from Interaction Server
		sampleDriver.getGfrPU().trace(LmsMessages.received_data, logRpfx, "itx response...\n" + responseMsg);
		return true;
	    }
	    catch (IllegalStateException ex) {
		if (attemptNum >= driverParams.itxResubmitAttempts) {
		    sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));

		    String logRec = String
			    .format("failed to submit request, stop attempts because exceded max %d allowed, refID=%d",
				    attemptNum, driverParams.itxResubmitAttempts, refID);
		    sampleDriver.getGfrPU().trace(LmsMessages.error2, logRpfx, logRec);

		    return false;
		}

		sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, ex.getMessage());
	    }
	    catch (Exception ex) { // ProtocolException
		sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
		return false;
	    }

	    // ......................................................................................
	    String logRec = String.format("failed to submit request,"
		    + " will repeat request attempt %d from %d allowed in %d seconds, refID=%d", attemptNum + 1,
					  driverParams.itxResubmitAttempts, driverParams.itxResubmitDelay, refID);
	    sampleDriver.getGfrPU().trace(LmsMessages.error2, logRpfx, logRec);
	    sleep(1000 * driverParams.itxResubmitDelay);
	}
    }



}
