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

package com.aetheros.aos.onem2m.client.cse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.aetheros.aos.onem2m.client.exceptions.DuplicateRegistrationException;
import com.aetheros.aos.onem2m.client.exceptions.InvalidAcceptTypeException;
import com.aetheros.aos.onem2m.client.exceptions.InvalidOneM2MOptionException;
import com.aetheros.aos.onem2m.client.exceptions.OneM2MException;
import com.aetheros.aos.onem2m.client.exceptions.UnsupportedContentFormatException;
import com.aetheros.aos.onem2m.client.request.OneM2MRequest;
import com.aetheros.aos.onem2m.common.resources.ACPEXT;
import com.aetheros.aos.onem2m.common.resources.AE;
import com.aetheros.aos.onem2m.common.resources.AEID;
import com.aetheros.aos.onem2m.common.resources.BAT;
import com.aetheros.aos.onem2m.common.resources.DeviceInformation;
import com.aetheros.aos.onem2m.common.resources.ExternalConnectivityMonitor;
import com.aetheros.aos.onem2m.common.resources.ExternalModuleInformation;
import com.aetheros.aos.onem2m.common.resources.Node;
import com.aetheros.aos.onem2m.common.mediatypes.MediaTypes;
import com.aetheros.aos.onem2m.common.options.OneM2MOption;
import com.aetheros.aos.onem2m.common.options.OneM2MOptionSet;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory; // Keep for later use.

import java.util.UUID;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionNumberRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * CSE (Common Service Entity).
 */
public class CSE {
    private String host;
    private int port;
    private boolean noPortSpecified = false;
    private CoapClient client;
    private AEID aeId;
    private Gson gson = buidlGson();
    private int accept = MediaTypes.APPLICATION_JSON;

    /**
     * Constructor I.
     * 
     * @param host Hostname or IP of the CSE server.
     */
    public CSE(String host) {
        this.host = host;
        this.noPortSpecified = true;
    }

    /**
     * Constructor II.
     * 
     * @param host Hostname or IP of the CSE server.
     * @param port Port of the CSE service is running on.
     */
    public CSE(String host, int port) {
        this.host = host;
        this.port = port;
        this.aeId = null;
    }

    /**
     * Registers an AE (Application Entity) with the CSE and stores the 
     * stores the returned MN-AE-ID.
     * 
     * @param ae The AE being registered. 
     * @return The registed AE.
     * @throws DuplicateRegistrationException
     */
    public AE register(AE ae) throws OneM2MException {
        // CSE resource.
        final String RESOURCE = ".";

        /*
         * Throw an exception if an AE is already registered with this cse instance.
         */
        if (this.aeId != null) {
            throw new DuplicateRegistrationException();
        }

        /*
         * Create a CoAP client to talk to the server.
         */
        try {
            if(this.noPortSpecified) {
                this.client = new CoapClient(new URI(String.format("coap://%s/%s", this.host, RESOURCE)));
            } else {
                this.client = new CoapClient("coap", this.host, this.port, RESOURCE);
            }

            // Build the request.
            OneM2MRequest req = new OneM2MRequest(Code.POST, Type.CON);
            OneM2MOptionSet options = new OneM2MOptionSet();

            try {
                // Build the request options.
                options.setContentFormat(MediaTypes.APPLICATION_JSON);
                options.addOption(new OneM2MOption(OneM2MOption.FROM, ""));  // Should be empty
                options.addOption(new OneM2MOption(OneM2MOption.REQUEST_IDENTIFIER, UUID.randomUUID().toString())); // <-- Generate cheap hash for request id?
                options.addOption(new OneM2MOption(OneM2MOption.RESOURCE_TYPE, 2)); // OneM2M option.
                options.addOption(new Option(OptionNumberRegistry.ACCEPT, this.accept)); // CoAP option.

                // Set the request options.
                req.setOptions(options);

                // Serialize PC object for the payload.
                String payload = this.gson.toJson(new Payload(ae));

                // Set the payload.
                req.setPayload(payload);

                // Send the request.
                CoapResponse res = this.client.advanced(req); /****** Blocking call ********/

                // throws invalid accept type exception
                validateResponse(res);


                // Deserialize the response object.
                JSONObject obj = new JSONObject(res.getResponseText());

                // Convert the json object to a pojo.
                AE responseAE = this.gson.fromJson(obj.getJSONObject("ae").toString(), AE.class);


                this.aeId = new AEID(responseAE.getAei()); // Store the aeid.

                return responseAE;

            } catch (InvalidOneM2MOptionException ex) {
                throw ex;
            } catch (UnsupportedContentFormatException ex) {
                throw ex;
            } catch(OneM2MException ex) {
                throw ex;
            }
        } catch (URISyntaxException ex) {
            // handle uri issue.
            System.out.println("An exception was thrown");
        } catch (ConnectorException | IOException e) {
            System.err.println("Got an error: " + e);
        } finally {
            client.shutdown();
        }

        // Return an empty AE.
        // @todo handle CoAP server errors.
        return new AE();
    }

    /**
     * Sets the media type the client will accept in response.
     * @param accept
     */
    public void setAcceptType(int accept) {
        this.accept = accept;
    }

    /**
     * Retrieves a node resource from the CSE using the MN-AE-ID obtained
     * in the register method.  Calls to this method must be preceded by a
     * call to register.
     * 
     * @return An instance of the node.
     * @throws Exception TBD.
     */
    public Node retrieveNode(AE ae) throws Exception {
        // @todo implement specific exception.
        final String RESOURCE = "node?rcn=4"; // ? why does setUriQuery not work in options?

        try {
            if(this.noPortSpecified) {
                this.client = new CoapClient(new URI(String.format("coap://%s/%s", this.host, RESOURCE)));
            } else {
                this.client = new CoapClient("coap", this.host, this.port, RESOURCE);
            }

            // Build the request.
            OneM2MRequest req = new OneM2MRequest(Code.GET, Type.CON);
            OneM2MOptionSet options = new OneM2MOptionSet();
            
            // Build the request options.
            try {
                // options.setUriQuery("rcn=4"); // needed for children.  This does not work?
                options.addOption(new OneM2MOption(OneM2MOption.FROM, ae.getAei())); // <-- Send the AI-ID
                options.addOption(new OneM2MOption(OneM2MOption.REQUEST_IDENTIFIER, UUID.randomUUID().toString())); // <-- Generate cheap hash for request id?
                options.addOption(new Option(OneM2MOption.ACCEPT, MediaTypeRegistry.APPLICATION_JSON));
            } catch (InvalidOneM2MOptionException ex) {
                System.out.println(ex.getMessage());
            } catch (UnsupportedContentFormatException ex) {
                System.out.println(ex.getMessage());
            }

            // Set the request options.
            req.setOptions(options);

            // Send the request.
            CoapResponse res = this.client.advanced(req);  /****** Blocking call ********/

            // Convert the response payload to a object.
            // NodeResponsePayload response = this.gson.fromJson(res.getResponseText(), NodeResponsePayload.class);
            JSONObject responseObj = new JSONObject(res.getResponseText());
            Node nod = this.gson.fromJson(responseObj.getJSONObject("nod").toString(), Node.class);

            // The node children deserialize to Child class instances when using gson.  
            // For now, the JSON raw json response child array contains objects with 
            // the object type as a key and the actual object as the value.
            // Loop over and raw child objects and create the correct pojo for each.
            // @todo Write custom deserializer for Child class.
            JSONArray ch = responseObj.getJSONObject("nod").getJSONArray("ch");

            // Process children.
            for(int i=0;i<ch.length();i++) {
                JSONObject jsonChild = new JSONObject(ch.get(i).toString());

                // Replace the base child class with the correct subclass.
                if(jsonChild.has("dvi")) {
                    nod.getChildren().set(i, this.gson.fromJson(jsonChild.getJSONObject("dvi").toString(), DeviceInformation.class));
                } else if(jsonChild.has("bat")) {
                    nod.getChildren().set(i, this.gson.fromJson(jsonChild.getJSONObject("bat").toString(), BAT.class));
                } else if(jsonChild.has("miext")) {
                    nod.getChildren().set(i, this.gson.fromJson(jsonChild.getJSONObject("miext").toString(), ExternalModuleInformation.class));
                } else if(jsonChild.has("cmext")) {
                    nod.getChildren().set(i, this.gson.fromJson(jsonChild.getJSONObject("cmext").toString(), ExternalConnectivityMonitor.class));
                } else if(jsonChild.has("acpext")) {
                    nod.getChildren().set(i, this.gson.fromJson(jsonChild.getJSONObject("acpext").toString(), ACPEXT.class));
                }
            }

            // Return the node from the object
            return nod;
        } catch(JsonParseException ex) {
            System.out.println(ex.getMessage());
        } catch(URISyntaxException ex) {
            // handle uri issue.
            System.out.println("An exception was thrown");
        } catch (ConnectorException | IOException e) {
            System.err.println("Got an error: " + e);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            client.shutdown();
        }

        //  Return an empty AE.
        // @todo handle CoAP server errors.
        return new Node();
    }

    /**
     * Returns the MN-AE-ID generated by the CSE during AE registration.
     * 
     * @return An instance of MN-AE-ID.
     */
    public AEID getAEID() {
        return this.aeId;
    }

    private void validateResponse(CoapResponse response) throws OneM2MException {
        switch(response.getCode()) {
            case NOT_ACCEPTABLE:
                throw new InvalidAcceptTypeException(this.accept);
        }
    }

    /**
     * Builds a GSON instance capable of deserializing polymorphic classes (children of the
     * Child class).  Needed for later use with child class and its subclasses.
     * 
     * @return An instance of Gson.
     */
    private Gson buidlGson() {
        // final RuntimeTypeAdapterFactory<Child> typeFactory = RuntimeTypeAdapterFactory
        // .of(Child.class, "type")
        // .registerSubtype(ACPEXT.class, ACPEXT.class.getName())
        // .registerSubtype(DeviceInformation.class, DeviceInformation.class.getName())
        // .registerSubtype(ExternalConnectivityMonitor.class, ExternalConnectivityMonitor.class.getName())
        // .registerSubtype(NetworkInformation.class, NetworkInformation.class.getName())
        // .registerSubtype(ExternalModuleInformation.class, ExternalModuleInformation.class.getName())
        // .registerSubtype(BAT.class, BAT.class.getName());
        
        // return new GsonBuilder().registerTypeAdapterFactory(typeFactory).create();
        return new Gson();
    }
}

/**
 * Payload wrapper.
 */
class Payload {
    AE ae;

    public Payload(AE ae) {
        this.ae = ae;
    }
}
