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

package com.aetheros.aos.onem2m.client.request;

import com.aetheros.aos.onem2m.common.options.OneM2MOptionSet;

import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Message;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;

/**
 * OneM2MRequest enforce OneM2M specific constraints and logic.
 */
public class OneM2MRequest extends Request {
    private int version = 1;
    private int contentType;

    private OneM2MOptionSet options;

    public OneM2MRequest(Code code) {
        super(code);
    }

    /**
     * @param code Request code.
     * @param type Request type.
     */
    public OneM2MRequest(Code code, Type type) {
        super(code, type);
    }
    
    /** 
     * Enforce valid content types here.
     * 
     * @param contentType Content type.
     */
    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    
    /** 
     * Enforce valid options and set default options here.
     * 
     * @param options 
     * @return Message 
     */
    public Message setOptions(OneM2MOptionSet options) {
        super.setOptions(options.getOptionSet());
        this.options = options;
		return this;
    }
}
