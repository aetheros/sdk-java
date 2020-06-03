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

package com.aetheros.aos.onem2m.server;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.aetheros.aos.onem2m.common.resources.ACPEXT;
import com.aetheros.aos.onem2m.common.resources.AE;
import com.aetheros.aos.onem2m.common.resources.BAT;
import com.aetheros.aos.onem2m.common.resources.Channel;
import com.aetheros.aos.onem2m.common.resources.Child;
import com.aetheros.aos.onem2m.common.resources.DeviceInformation;
import com.aetheros.aos.onem2m.common.resources.ExternalConnectivityMonitor;
import com.aetheros.aos.onem2m.common.resources.ExternalModuleInformation;
import com.aetheros.aos.onem2m.common.resources.LTE;
import com.aetheros.aos.onem2m.common.resources.NetworkInformation;
import com.aetheros.aos.onem2m.common.resources.Node;
import com.aetheros.aos.onem2m.common.resources.NodeResponsePayload;
import com.aetheros.aos.onem2m.common.resources.PC;
import com.aetheros.aos.onem2m.common.resources.TSTMD;
import com.aetheros.aos.onem2m.common.mediatypes.MediaTypes;
import com.aetheros.aos.onem2m.common.options.OneM2MOptionSet;
import com.aetheros.aos.onem2m.common.options.OneM2MOption;
import com.google.gson.Gson;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * Test CoAP OneM2M Server
 */
public class OneM2MServer extends CoapServer {
	private static final int COAP_PORT = 5683;

	// Support Media Types for Content Format option.
	private Set<Integer> mediaTypes = new HashSet<Integer>();

	public OneM2MServer(NetworkConfig config) throws SocketException {
		super(config);

		// Set supported media types.
		this.mediaTypes.add(MediaTypes.APPLICATION_JSON);

		// Create the AE registration resource.
		Resource spRelativePath = new Resource(".", "AE Registration") {
			@Override
			public void handlePOST(CoapExchange exchange) {
				printRequest(exchange);

				// Validate the request content.
				ValidatedRequest validatedRequest = validateRequest(exchange);

				if(validatedRequest.isValid) {
					// Get the payload.
					// PC pc = new Gson().fromJson(exchange.getRequestText(), PC.class);
					AE ae = new AE();
					ae.setAei(""+new Random().nextInt(1000000000));

					AEOBJ aeObj = new AEOBJ(ae);

					// Build the response.
					OneM2MResponse createdRes = new OneM2MResponse(ResponseCode.CREATED);
					createdRes.setType(Type.CON);
					createdRes.setPayload(new Gson().toJson(aeObj));
					OptionSet options = new OptionSet();
					options.setUriHost(exchange.getSourceAddress().toString());
					options.setUriPort(exchange.getSourcePort());
					options.setUriPath(exchange.getRequestOptions().getUriPath().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
					options.setContentFormat(exchange.getRequestOptions().getContentFormat());
					// options.addOption(new Option(OneM2MOption.FROM, "" + new Random().nextInt(1000000000))); // MN-AE-ID generate random id.
					options.addOption(new Option(OneM2MOption.RESPONSE_STATUS_CODE, "2001"));
					createdRes.setOptions(options);

					// send the response.
					exchange.respond(createdRes);
				} else {
					OneM2MResponse errRes = new OneM2MResponse(validatedRequest.responseCode);
					errRes.setType(Type.NON);
					exchange.respond(errRes);
				}
			}
		};

		Resource retrieveNodePath = new Resource("node", "MN-CSE NODE") {
			@Override
			public void handleGET(CoapExchange exchange) {
				printRequest(exchange);

				// Validate the request content.
				ValidatedRequest validatedRequest = validateRequest(exchange);

				if(validatedRequest.isValid) {
					// Build the response.
					OneM2MResponse res = new OneM2MResponse(ResponseCode.CONTENT);
					res.setType(Type.ACK);

					// Build a node object for the response payload.
					Node node = new Node();
					node.addChild(new CMEXT(new ExternalConnectivityMonitor(
						ThreadLocalRandom.current().nextInt(-180, -10 - 1),
						ThreadLocalRandom.current().nextInt(-50, 50 - 1)
						)
					));
					node.addChild(new DVI(new DeviceInformation("Water Meter", "6.0.0", "A12Q7")));
					node.addChild(new MIEXT(new ExternalModuleInformation(
						2, "8914800000504160OneM2MOption.STATUS_CODE7", "352427090007070", "311270002255837", "20190726T155222",
						"UTC", true
					)));

					// Construct the payload.
					NodeResponsePayload payload = new NodeResponsePayload(node);

					res.setPayload(new Gson().toJson(payload));

					OptionSet options = new OptionSet();
					options.setUriHost(exchange.getSourceAddress().toString());
					options.setUriPort(exchange.getSourcePort());
					options.setUriPath(exchange.getRequestOptions().getUriPath().toString().replaceAll("\\[", "").replaceAll("\\]", ""));
					options.setContentFormat(exchange.getRequestOptions().getContentFormat());
					res.setOptions(options);

					// send the response.
					exchange.respond(res);
				} else {
					OneM2MResponse errRes = new OneM2MResponse(validatedRequest.responseCode);
					errRes.setType(Type.NON);
					exchange.respond(errRes);
				}
			}
		};

		add(spRelativePath);
		add(retrieveNodePath);
	}

	/*
	 * Base resource.
	 */
	class Resource extends CoapResource {
		public Resource(String identifer, String title) {

			// set resource identifier
			super(identifer);

			// set display name
			getAttributes().setTitle(title);
		}
	}

	/**
	 * Starts the server.
	 */
	public static void main(String[] args) {
		System.out.println("================================");
		System.out.println("========= CoAP OneM2M ==========");
		System.out.println("========= Test Server ==========");
		System.out.println("=========   Running   ==========");
		System.out.println("================================\n");
		
		try{
			new OneM2MServer(NetworkConfig.getStandard()).start();;
		} catch(Exception ex) {
			// handle it...
		}
	}

	private boolean isMediaTypeSupported(int mediaType) {
		if(this.mediaTypes.contains(mediaType)) {
			return true;
		} else {
			return false;
		}
	}

	private void printRequest(CoapExchange exchange) {
		System.out.println(String.format("Code: %s", exchange.getRequestCode()));
		System.out.println(String.format("Uri-Host: %s", exchange.getSourceAddress()));
		System.out.println(String.format("Uri-Port: %s", exchange.getSourcePort()));
		System.out.println(String.format("Uri-Path: %s", exchange.getRequestOptions().getUriPath().toString()));
		System.out.println(String.format("Content-Format: %s", exchange.getRequestOptions().getContentFormat()));
		System.out.println(String.format("Accept: %s", exchange.getRequestOptions().getAccept()));

		exchange.getRequestOptions().getOthers().forEach(option -> {
			System.out.println(String.format("%d: %s", option.getNumber(), option.getStringValue()));
		});

		System.out.println(exchange.getRequestText());
	}

	private ValidatedRequest validateRequest(CoapExchange exchange) {
		/*
		 * Content format negotion.
		 * If the hosting CSE does not support the Content-Format specified in the 
		 * accept option of the request, 4.06 "Not Acceptable" shall be sent as a
		 * response.
		 */
		int acceptOption = exchange.getRequestOptions().getAccept();

		switch(acceptOption) {
			case MediaTypes.APPLICATION_JSON:
			return new ValidatedRequest(true);
			default:
			return new ValidatedRequest(false, ResponseCode.NOT_ACCEPTABLE, "Unsupported media type requested by client.");
		}
	}

	class ValidatedRequest {
		public boolean isValid;
		public ResponseCode responseCode;
		public String reason;

		public ValidatedRequest(boolean isValid) {
			this.isValid =  isValid;
		}

		public ValidatedRequest(boolean isValid, ResponseCode responseCode, String reason) {	
			this.isValid =  isValid;
			this.responseCode = responseCode;
			this.reason = reason;
		}
	}

	/**
	 * Wrapper classes for children.
	 */
	private class AEOBJ {
		public AE ae;

		public AEOBJ(AE ae) {
			this.ae = ae;
		}
	}

	private class CMEXT extends Child {
		public Child cmext;

		public CMEXT(Child child) {
			this.cmext= child;
		}
	}

	private class DVI extends Child {
		public Child dvi;

		public DVI(Child child) {
			this.dvi = child;
		}
	}

	private class MIEXT extends Child {
		public Child miext;

		public MIEXT(Child child) {
			this.miext = child;
		}
	}
}
