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
 * Application Entity.
 */
public class AE {
    private String[] aa;

    private String aei;
    private String api; // application id
    private String apn; // application name
    private String[] poa; // point of attachment
    private String[] at;

    private String ct;
    private String lt;
    private String pi;
    private String ri;
    private String rn;
    private String ty;

    public String[] getAa() {
        return this.aa;
    }

    public String getAei() {
        return this.aei;
    }

    public String[] getAt() {
        return this.at;
    }

    public String getCt() {
        return this.ct;
    }

    public String getLt() {
        return this.lt;
    }

    public String getPi() {
        return this.pi;
    }

    public String getRi() {
        return this.ri;
    }

    public String getRn() {
        return this.rn;
    }

    public String getTy() {
        return this.ty;
    }

    /**
     * Default constructor.
     */
    public AE() {}

    /**
     * Constructor I.
     * @param api Application ID.
     * @param apn Application name.
     * @param poa  Point of attachment.
     */
    public AE(String api, String apn, String[] poa) {
        this.api = api;
        this.apn = apn;
        this.poa =  poa;
    }

    
    /** 
     * @return String The application ID.
     */
    public String getApi() {
        return this.api;
    }

    
    /** 
     * @param api The application ID.
     */
    public void setApi(String api) {
        this.api = api;
    }

    
    /** 
     * @return String The application name.
     */
    public String getApn() {
        return this.apn;
    }

    
    /** 
     * @param apn  The application name.
     */
    public void setApn(String apn) {
        this.apn = apn;
    }

    
    /** 
     * @return String[] Points of access.
     */
    public String[] getPoa() {
        return this.poa;
    }

    
    /** 
     * @param poa Points of access.
     */
    public void setPoa(String[] poa) {
        this.poa = poa;
    }

	public void setRn(String rn) {
        this.rn = rn;
    }

	public void setAei(String aei) {
        this.aei = aei;
    }

    @Override
    public String toString() {
        return "{" +
            " aa='" + getAa() + "'" +
            ", aei='" + getAei() + "'" +
            ", api='" + getApi() + "'" +
            ", apn='" + getApn() + "'" +
            ", poa='" + getPoa() + "'" +
            ", at='" + getAt() + "'" +
            ", ct='" + getCt() + "'" +
            ", lt='" + getLt() + "'" +
            ", pi='" + getPi() + "'" +
            ", ri='" + getRi() + "'" +
            ", rn='" + getRn() + "'" +
            ", ty='" + getTy() + "'" +
            "}";
    }
}