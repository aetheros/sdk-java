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

import com.aetheros.aos.onem2m.client.exceptions.InvalidOneM2MOptionException;
import com.aetheros.aos.onem2m.client.exceptions.UnsupportedContentFormatException;
import com.aetheros.aos.onem2m.common.mediatypes.MediaTypes;

import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionNumberRegistry;
import org.eclipse.californium.core.coap.OptionSet;

/**
 * Container for OneM2M options.
 */
public class OneM2MOptionSet {
	/**
	 * The format types of CoAP options.
	 */
	public static enum optionFormats {
		INTEGER, STRING, OPAQUE, UNKNOWN, EMPTY
	}

	/*
	 * The OptionSet class is final, so we are going to
	 * wrap it in this OneM2MOptionSet class to extend the
	 * options beyond the basic CoAP options.
	 */
	private OptionSet optionSet;

	public OneM2MOptionSet() {
		optionSet = new OptionSet();
	}

	/**
	 * Sets the Content-Format.
	 * 
	 * @param format Format code as an integer.
	 * @return
	 */
	public OneM2MOptionSet setContentFormat(int format) {
		optionSet.setContentFormat(format);
		return this;
	}
	
	public OneM2MOptionSet setUriQuery(String queryString) {
		optionSet.setUriQuery(queryString);
		return this;
	}

	/**
	 * Sets OneM2M or CoAP options.  CoAP should be instances of Option and OneM2M options
	 * should be instances of OneM2MOption.  If you create a OneM2MOption with a CoAP option
	 * number it will throw a InvalidOneM2MOptionException.
	 * 
	 * @param option The option to add.
	 * @exception InvalidOneM2MOptionException
	 * @exception UnsupportedContentFormatException
	 * @return
	 */
	public OneM2MOptionSet addOption(Option option) throws InvalidOneM2MOptionException, UnsupportedContentFormatException {
		if(option instanceof OneM2MOption) {
			// Only allow options that are part of the one m2m specification to be represented by the onem2moption class.
			if(!isValidOneM2MOption(option.getNumber())) {
				throw new InvalidOneM2MOptionException(String.format("%s is not a valid OneM2M option.", option.getNumber()));
			}

			// Determine the options format.
			switch(getOptionFormat(option.getNumber())) {
				case STRING:
					// Extended OneM2M option.
					optionSet.addOption(new Option(option.getNumber(), option.getStringValue()));
					break;
				case INTEGER:
					// Extended OneM2M option.
					optionSet.addOption(new Option(option.getNumber(), option.getIntegerValue()));
					break;
				default:
					// do nothing.
			}
		} else {
			/*
			 * Only allow supported content formats to be set in the CoAP accepted option.
			 */
			if(option.getNumber() == OptionNumberRegistry.ACCEPT) {
				if(!MediaTypes.isSupportMediaType(option.getIntegerValue())) {
					throw new UnsupportedContentFormatException(String.format("%d if not a support Content-Format", option.getIntegerValue()));
				}
			}

			// Base CoAP options
			optionSet.addOption(option);
		}

		return this;
	}

	/**
	 * Return the CoAP option set.
	 * @return The option set.
	 */
	public OptionSet getOptionSet() {
		return optionSet;
	}

	/**
	 * @return The string representation of the option set.
	 */
	public String toString() {
		return this.optionSet.toString();
	}


	/**
	 * Determines the expected option format.
	 * 
	 * @param optionNumber The option number.
	 * @return An integer indicating the option format (0 == string, 1 == uint, -1 == unsupported).
	 */
	private optionFormats getOptionFormat(int optionNumber) {
		switch(optionNumber) {
			case OneM2MOption.FROM:
			case OneM2MOption.REQUEST_IDENTIFIER:
			case OneM2MOption.REQUEST_EXPIRATION_TIMESTAMP:
			case OneM2MOption.ORIGINATING_TIMESTAMP:
			case OneM2MOption.RESULT_EXPIRATION_TIMESTAMP:
			case OneM2MOption.OPERATION_EXECUTION_TIME:
			case OneM2MOption.RESPONSE_TYPE:
			case OneM2MOption.GROUP_REQUEST_IDENTIFIER:
				return optionFormats.STRING;
			case OneM2MOption.EVENT_CATEGORY:
			case OneM2MOption.RESPONSE_STATUS_CODE:
			case OneM2MOption.RESOURCE_TYPE:
				return optionFormats.INTEGER;
			default:
				return optionFormats.UNKNOWN;
		}
	}

	/**
	 * Tests if the option is supported by OneM2M.
	 * 
	 * @param optionNumber The option number.
	 */
	private boolean isValidOneM2MOption(int optionNumber) {
		switch(optionNumber) {
			case OneM2MOption.FROM:
			case OneM2MOption.REQUEST_IDENTIFIER:
			case OneM2MOption.REQUEST_EXPIRATION_TIMESTAMP:
			case OneM2MOption.ORIGINATING_TIMESTAMP:
			case OneM2MOption.RESULT_EXPIRATION_TIMESTAMP:
			case OneM2MOption.OPERATION_EXECUTION_TIME:
			case OneM2MOption.RESPONSE_TYPE:
			case OneM2MOption.GROUP_REQUEST_IDENTIFIER:
			case OneM2MOption.EVENT_CATEGORY:
			case OneM2MOption.RESPONSE_STATUS_CODE:
			case OneM2MOption.RESOURCE_TYPE:
				return true;
			default:
				return false;
		}
	}

}