package com.aetheros.aos.onem2m.server;

import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;

/**
 * OneM2M wrapper around coap response.
 */
public class OneM2MResponse extends Response {

    protected OneM2MResponse(ResponseCode code) {
        super(code);
    }
}