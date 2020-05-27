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

package com.aetheros.aos.onem2m.common.resources;

/**
 * Structure class for payload container serialization.
 */
public class PC {
    AE ae;
    Node nod;

    /**
     * Default constructor.
     */
    public PC() {}

    /**
     * Constructor II
     * 
     * @param ae An instance of AE.
     */
    public PC(AE ae) {
        this.ae = ae;
    }

    /**
     * Constructor II
     * 
     * @param nod An instance of node.
     */
    public PC(Node node) {
        this.nod= node;
    }

    /**
     * Constructor III
     * 
     * @param ae An instance of AE.
     * @param nod An instance of node.
     */
    public PC(AE ae, Node nod) {
        this.ae = ae;
        this.nod = nod;
    }
    
    /** 
     * @return Node The node resource.
     */
    public Node getNod() {
        return this.nod;
    }
    
    /** 
     * @return AE The application entity.
     */
    public AE getAe() {
        return this.ae;
    }

    
    /** 
     * @param ae The application entity.
     */
    public void setAe(AE ae) {
        this.ae = ae;
    }

    /** 
     * @return String The string representation of the instance.
     */
    @Override
    public String toString() {
        if(this.ae != null) {
            return "{" +
                " ae='" + getAe() + "'" +
                "}";
        } else if(this.nod != null) {
            return "{" +
                " ae='" + getNod() + "'" +
                "}";
        } else {
            return "{}";
        }
    }
}