/**
 * File:    SampleDriverParams.java
 * Project: es_dms_sample_driver
 *
 * Copyright (C) 2011-2018 Genesys Telecommunications Laboratories Inc.
 *
 * January 05, 2011
 */
package com.youtube.channel;
import com.genesyslab.mcr.smserver.common.SrvUtil;
import com.genesyslab.mcr.smserver.gframework.CfgOptions;
import com.genesyslab.mcr.smserver.gframework.LmsMessages;

import java.util.ArrayList;
import java.util.Properties;

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
public class YoutubeDriverParams
{
    /**
     * The next data fields are used in the driver's data fetching monitor (refer to SampleMonitor class).
     */
    // Interaction media type
    protected String inboundMedia;
    // Interaction type - in this sample this value is a hardcoded one, may be specified in options, if needed
    protected String itxType = "Inbound";
    // Interaction subtype - in this sample this value is a hardcoded one, may be specified in options, if needed
    protected String itxSubType = "InboundNew";

    // Data fetching monitor's control parameters
    protected int samplingPeriod;
    protected int messagesCreateMax;
    protected int itxSubmitTimeout;
    protected int itxResubmitDelay;
    protected int itxResubmitAttempts;

    protected String sdrAPIToken;
    protected String sdrAPIURL;
    protected String sdrAPIKey;
    /**
     * The next data field is not used anywhere in the driver, the only purpose of the field is to demonstrate how to
     * get configuration option's values.
     */
    protected Properties queriesList = null;

    // Names of configuration options
    private final static String XOPTION_X_INBOUND_MEDIA = "x-inbound-media";


    private final static String MOPTION_SAMPLING_PERIOD = "sampling-period";
    private final static String MOPTION_MESSAGES_CREATE_MAX = "messages-create-max";


    private final static String MOPTION_ITX_SUBMIT_TIMEOUT = "itx-submit-timeout";
    private final static String MOPTION_ITX_RESUBMIT_DELAY = "itx-resubmit-delay";
    private final static String MOPTION_ITX_RESUBMIT_ATTEMPTS = "itx-resubmit_attempts";

    // local
    private YoutubeDriver sampleDriver;

    /**
     * Constructor
     */
    public YoutubeDriverParams(YoutubeDriver sampleDriver)
    {
	this.sampleDriver = sampleDriver;
    }

    /**
     * Fetch configuration of the driver.
     * 
     * @throws Exception
     *             if failed to get configuration options
     */
    protected void getConfiguration()
	    throws Exception
    {
	final String logRpfx = sampleDriver.logPfx("getConfiguration");

	// get channel option XOPTION_x_inbound_media
	inboundMedia = CfgOptions.getOption(sampleDriver.channelPU.getChannelName(), XOPTION_X_INBOUND_MEDIA, null,
					    "sample", null, logRpfx, true);
	 	// get data fetching monitor parameters
        sdrAPIToken = CfgOptions.getOption(sampleDriver.channelPU.getChannelName(), "sdr-api-token", null, "SDR token not initialzed", null, logRpfx, true);

        sdrAPIURL = CfgOptions.getOption(sampleDriver.channelPU.getChannelName(), "sdr-api-url", null, "SDR URL not initialzed", null, logRpfx, true);

        sdrAPIKey = CfgOptions.getOption(sampleDriver.channelPU.getChannelName(), "sdr-api-key", null, "SDR key not initialzed", null, logRpfx, true);

        queriesList = getMonitorParams(sampleDriver.channelPU.getChannelName());

	sampleDriver.getGfrPU().trace(LmsMessages.generic_std2, logRpfx, "monitor configuration processing finished");
    }

    /**
     * Get data fetching monitor parameters.
     * 
     * @param channelName
     *            media channel name
     * @return data fetching queries as an object Properties with records: key=<query name>, value=<query body>
     */
    private Properties getMonitorParams(String channelName)
    {
	final String logRpfx = sampleDriver.logPfx("getMonitorParams");

	Properties twtQueriesProps = new Properties();

	try {
	    String monitorSection = channelName + "-monitor";

	    // get data fetching monitor's option MOPTION_sampling_period
	    samplingPeriod = CfgOptions.getIntOption(monitorSection, MOPTION_SAMPLING_PERIOD, new int[] { 60, -3600 },
						     600, null, logRpfx, false);

	    // get data fetching monitor's option MOPTION_messages_create_max
	    messagesCreateMax = CfgOptions.getIntOption(monitorSection, MOPTION_MESSAGES_CREATE_MAX,
							new int[] { 0, -1000000 }, 0, null, logRpfx, false);
		sampleDriver.getGfrPU().trace("messagesCreateMax "+messagesCreateMax);

	    // get data fetching monitor's option MOPTION_itx_request_timeout
	    itxSubmitTimeout = CfgOptions.getIntOption(monitorSection, MOPTION_ITX_SUBMIT_TIMEOUT, new int[] { 1, -60 },
						       30, null, logRpfx, false);

	    // get data fetching monitor's option MOPTION_itx_repeat_timeout
	    itxResubmitDelay = CfgOptions.getIntOption(monitorSection, MOPTION_ITX_RESUBMIT_DELAY,
						       new int[] { 1, -120 }, 30, null, logRpfx, false);

	    // get data fetching monitor's option MOPTION_itx_attempts_max
	    itxResubmitAttempts = CfgOptions.getIntOption(monitorSection, MOPTION_ITX_RESUBMIT_ATTEMPTS,
							  new int[] { 0, -9 }, 3, null, logRpfx, false);

	    // ......................................................................................
	    // get data fetching monitor's options with prefix "qry-" (data frtching queries)
	    ArrayList<String> qryNames = CfgOptions.getOptionsNames(monitorSection, "qry-");

	    if (null == qryNames)
		return twtQueriesProps;

	    for (String qryName : qryNames) {
		String qryValue = CfgOptions.getOption(monitorSection, qryName, null, null, null, logRpfx, true);

		twtQueriesProps.setProperty(qryName, qryValue);
	    }

	    return twtQueriesProps;
	}
	catch (Exception ex) {
	    sampleDriver.getGfrPU().trace(LmsMessages.exception2, logRpfx, SrvUtil.getExcLogRecord(ex));
	    return null;
	}
    }
}
