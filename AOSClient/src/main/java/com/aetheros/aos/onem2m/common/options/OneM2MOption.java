/****************************************************************************
 *            AETHEROS, INC. CONFIDENTIAL
 *
 * The source code contained or described herein and all documents related
 * to the source code ("Material") are owned by Aetheros, Inc. or its
 * suppliers or licensors. Title to the Material remains with Aetheros or its
 * suppliers and licensors. The Material contains trade secrets and proprietary
 * and confidential information of Aetheros or its suppliers and licensors. The
 * Material is protected by worldwide copyright and trade secret laws and treaty
 * provisions. No part of the Material may be used, copied, reproduced, modified,
 * published, uploaded, posted, transmitted, distributed, or disclosed in any way
 * without the prior express written permission of Aetheros, Inc.
 *
 * No license under any patent, copyright, trade secret or other intellectual
 * property right is granted to or conferred upon you by disclosure or delivery
 * of the Material, either expressly, by implication, inducement, estoppel or
 * otherwise. Any license under such intellectual property rights must be
 * express and approved by Aetheros, Inc. in writing.
 *
 *      Copyright (c) 2019-2020 Aetheros, Inc.  All Rights Reserved.
 *
 *****************************************************************************/

package com.aetheros.aos.onem2m.common.options;

import org.eclipse.californium.core.coap.Option;

/**
 * OneM2M specific options.
 */
public class OneM2MOption extends Option {
    // OneM2M
    public static final int FROM = 256;
    public static final int REQUEST_IDENTIFIER = 257;
    public static final int ORIGINATING_TIMESTAMP = 259;
    public static final int REQUEST_EXPIRATION_TIMESTAMP = 260;
    public static final int RESULT_EXPIRATION_TIMESTAMP = 261;
    public static final int OPERATION_EXECUTION_TIME = 262;
    public static final int RESPONSE_TYPE = 263;
    public static final int EVENT_CATEGORY = 264;
    public static final int RESPONSE_STATUS_CODE = 265;
    public static final int GROUP_REQUEST_IDENTIFIER = 266;
    public static final int RESOURCE_TYPE = 267;
    // ./OneM2M

    public static final int UNKNOWN			= -1;

	// RFC 7252
	public static final int RESERVED_0		= 0;
	public static final int IF_MATCH		= 1;
	public static final int URI_HOST		= 3;
	public static final int ETAG			= 4;
	public static final int IF_NONE_MATCH	= 5;
	public static final int URI_PORT		= 7;
	public static final int LOCATION_PATH	= 8;
	public static final int URI_PATH		= 11;
	public static final int CONTENT_FORMAT	= 12;
	public static final int MAX_AGE			= 14;
	public static final int URI_QUERY		= 15;
	public static final int ACCEPT			= 17;
	public static final int LOCATION_QUERY	= 20;
	public static final int PROXY_URI		= 35;
	public static final int PROXY_SCHEME	= 39;
	public static final int SIZE1			= 60;
	public static final int RESERVED_1		= 128;
	public static final int RESERVED_2		= 132;
	public static final int RESERVED_3		= 136;
	public static final int RESERVED_4		= 140;

	// draft-ietf-core-observe-14
	public static final int OBSERVE			= 6;

	// draft-ietf-core-block-14
	public static final int BLOCK2			= 23;
	public static final int BLOCK1			= 27;
	public static final int SIZE2			= 28;

	//TODO temporary assignment
	public static final int OSCORE			= 9;

	public OneM2MOption(int number, String str) {
		super(number, str);
	}

	public OneM2MOption(int number, int val) {
		super(number, val);
	}

	/**
	 * Option names.
	 */
	public static class Names {
		public static final String Reserved 		= "Reserved";

		public static final String If_Match 		= "If-Match";
		public static final String Uri_Host 		= "Uri-Host";
		public static final String ETag 			= "ETag";
		public static final String If_None_Match 	= "If-None-Match";
		public static final String Uri_Port 		= "Uri-Port";
		public static final String Location_Path 	= "Location-Path";
		public static final String Uri_Path 		= "Uri-Path";
		public static final String Content_Format	= "Content-Format";
		public static final String Max_Age 			= "Max-Age";
		public static final String Uri_Query 		= "Uri-Query";
		public static final String Accept 			= "Accept";
		public static final String Location_Query 	= "Location-Query";
		public static final String Proxy_Uri 		= "Proxy-Uri";
		public static final String Proxy_Scheme		= "Proxy-Scheme";
		public static final String Size1			= "Size1";

		public static final String Observe			= "Observe";

		public static final String Block2			= "Block2";
		public static final String Block1			= "Block1";
		public static final String Size2			= "Size2";

        public static final String Object_Security  = "Object-Security";
        
        public static final String From                                 = "oneM2M-FR";
        public static final String Request_Identifier                   = "oneM2M-RQI";
        public static final String Originating_Timestamp                = "oneM2M-OT";
        public static final String Request_Expiration_Timestamp         = "oneM2M-RQET";
        public static final String Result_Expiration_Timestamp          = "oneM2M-RSET";
        public static final String Operation_Execution_Time             = "oneM2M-OET";
        public static final String Notification_Uri_of_Response_Type    = "oneM2M-RTURI";
        public static final String Event_Category                       = "oneM2M-EC";
        public static final String Response_Status_Code                 = "oneM2M-RSC";
        public static final String Group_Request_Identifier             = "oneM2M-GID";
        public static final String Resource_Type                        = "oneM2M-TY";
	}

	/**
	 * Option default values.
	 */
	public static class Defaults {
		
		/** The default Max-Age. */
		public static final long MAX_AGE = 60L;
	}

	/**
	 * The format types of CoAP options.
	 */
	public static enum optionFormats {
		INTEGER, STRING, OPAQUE, UNKNOWN, EMPTY
	}

	/**
	 * Returns the option format based on the option number.
	 * 
	 * @param optionNumber
	 *            The option number
	 * @return The option format corresponding to the option number
	 */
	public static optionFormats getFormatByNr(int optionNumber) {
		switch (optionNumber) {
		case CONTENT_FORMAT:
		case MAX_AGE:
		case URI_PORT:
		case OBSERVE:
		case BLOCK2:
		case BLOCK1:
		case SIZE2:
		case SIZE1:
		case ACCEPT:
        case FROM:
        case REQUEST_IDENTIFIER:
        case ORIGINATING_TIMESTAMP:
        case REQUEST_EXPIRATION_TIMESTAMP:
        case RESULT_EXPIRATION_TIMESTAMP:
        case OPERATION_EXECUTION_TIME:
        case RESPONSE_TYPE:
        case EVENT_CATEGORY:
        case RESPONSE_STATUS_CODE:
        case GROUP_REQUEST_IDENTIFIER:
        case RESOURCE_TYPE:
			return optionFormats.INTEGER;
		case IF_NONE_MATCH:
			return optionFormats.EMPTY;
		case URI_HOST:
		case URI_PATH:
		case URI_QUERY:
		case LOCATION_PATH:
		case LOCATION_QUERY:
		case PROXY_URI:
		case PROXY_SCHEME:
			return optionFormats.STRING;
		case ETAG:
		case IF_MATCH:
		case OSCORE:
			return optionFormats.OPAQUE;
		default:
			return optionFormats.UNKNOWN;
		}
	}

	/**
	 * Checks whether an option is critical.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is critical
	 */
	public static boolean isCritical(int optionNumber) {
		return (optionNumber & 1) != 0;
	}

	/**
	 * Checks whether an option is elective.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is elective
	 */
	public static boolean isElective(int optionNumber) {
		return (optionNumber & 1) == 0;
	}

	/**
	 * Checks whether an option is unsafe.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is unsafe
	 */
	public static boolean isUnsafe(int optionNumber) {
		// When bit 6 is 1, an option is Unsafe
		return (optionNumber & 2) > 0;
	}

	/**
	 * Checks whether an option is safe.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is safe
	 */
	public static boolean isSafe(int optionNumber) {
		return !isUnsafe(optionNumber);
	}

	/**
	 * Checks whether an option is not a cache-key.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is not a cache-key
	 */
	public static boolean isNoCacheKey(int optionNumber) {
		/*
		 * When an option is not Unsafe, it is not a Cache-Key (NoCacheKey) if
		 * and only if bits 3-5 are all set to 1; all other bit combinations
		 * mean that it indeed is a Cache-Key
		 */
		return (optionNumber & 0x1E) == 0x1C;
	}

	/**
	 * Checks whether an option is a cache-key.
	 * 
	 * @param optionNumber
	 *            The option number to check
	 * @return {@code true} if the option is a cache-key
	 */
	public static boolean isCacheKey(int optionNumber) {
		return !isNoCacheKey(optionNumber);
	}

	/**
	 * Checks if is single value.
	 * 
	 * @param optionNumber
	 *            the option number
	 * @return {@code true} if is single value
	 */
	public static boolean isSingleValue(int optionNumber) {
		switch (optionNumber) {
		case CONTENT_FORMAT:
		case MAX_AGE:
		case PROXY_URI:
		case PROXY_SCHEME:
		case URI_HOST:
		case URI_PORT:
		case IF_NONE_MATCH:
		case OBSERVE:
		case ACCEPT:
		case OSCORE:
        case FROM:
        case REQUEST_IDENTIFIER:
        case ORIGINATING_TIMESTAMP:
        case REQUEST_EXPIRATION_TIMESTAMP:
        case RESULT_EXPIRATION_TIMESTAMP:
        case OPERATION_EXECUTION_TIME:
        case RESPONSE_TYPE:
        case EVENT_CATEGORY:
        case RESPONSE_STATUS_CODE:
        case GROUP_REQUEST_IDENTIFIER:
        case RESOURCE_TYPE:
		default:
			return true;
		case ETAG:
		case IF_MATCH:
		case URI_PATH:
		case URI_QUERY:
		case LOCATION_PATH:
		case LOCATION_QUERY:
			return false;
		}
	}

	/**
	 * Checks if is uri option.
	 * 
	 * @param optionNumber
	 *            the option number
	 * @return {@code true} if is uri option
	 */
	public static boolean isUriOption(int optionNumber) {
		boolean result = optionNumber == URI_HOST || optionNumber == URI_PATH || optionNumber == URI_PORT || optionNumber == URI_QUERY;
		return result;
	}

	/**
	 * Returns a string representation of the option number.
	 * 
	 * @param optionNumber
	 *            the option number to describe
	 * @return a string describing the option number
	 */
	public static String toString(int optionNumber) {
		switch (optionNumber) {
		case RESERVED_0:
		case RESERVED_1:
		case RESERVED_2:
		case RESERVED_3:
		case RESERVED_4:
			return Names.Reserved;
		case IF_MATCH:
			return Names.If_Match;
		case URI_HOST:
			return Names.Uri_Host;
		case ETAG:
			return Names.ETag;
		case IF_NONE_MATCH:
			return Names.If_None_Match;
		case URI_PORT:
			return Names.Uri_Port;
		case LOCATION_PATH:
			return Names.Location_Path;
		case URI_PATH:
			return Names.Uri_Path;
		case CONTENT_FORMAT:
			return Names.Content_Format;
		case MAX_AGE:
			return Names.Max_Age;
		case URI_QUERY:
			return Names.Uri_Query;
		case ACCEPT:
			return Names.Accept;
		case LOCATION_QUERY:
			return Names.Location_Query;
		case PROXY_URI:
			return Names.Proxy_Uri;
		case PROXY_SCHEME:
			return Names.Proxy_Scheme;
		case OBSERVE:
			return Names.Observe;
		case BLOCK2:
			return Names.Block2;
		case BLOCK1:
			return Names.Block1;
		case SIZE2:
			return Names.Size2;
		case SIZE1:
			return Names.Size1;
		case OSCORE:
			return Names.Object_Security;
		default:
			return String.format("Unknown (%d)", optionNumber);
		}
	}
	
	
	/** 
	 * @param name
	 * @return int
	 */
	public static int toNumber(String name) {
		if (Names.If_Match.equals(name))			return IF_MATCH;
		else if (Names.Uri_Host.equals(name))		return URI_HOST;
		else if (Names.ETag.equals(name))			return ETAG;
		else if (Names.If_None_Match.equals(name))	return IF_NONE_MATCH;
		else if (Names.Uri_Port.equals(name))		return URI_PORT;
		else if (Names.Location_Path.equals(name))	return LOCATION_PATH;
		else if (Names.Uri_Path.equals(name))		return URI_PATH;
		else if (Names.Content_Format.equals(name))	return CONTENT_FORMAT;
		else if (Names.Max_Age.equals(name))		return MAX_AGE;
		else if (Names.Uri_Query.equals(name))		return URI_QUERY;
		else if (Names.Accept.equals(name))			return ACCEPT;
		else if (Names.Location_Query.equals(name))	return LOCATION_QUERY;
		else if (Names.Proxy_Uri.equals(name))		return PROXY_URI;
		else if (Names.Proxy_Scheme.equals(name))	return PROXY_SCHEME;
		else if (Names.Observe.equals(name))		return OBSERVE;
		else if (Names.Block2.equals(name))			return BLOCK2;
		else if (Names.Block1.equals(name))			return BLOCK1;
		else if (Names.Size2.equals(name))			return SIZE2;
		else if (Names.Size1.equals(name))			return SIZE1;
		else if (Names.Object_Security.equals(name)) return OSCORE;
		else return UNKNOWN;
	}
}