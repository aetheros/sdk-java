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

import java.util.Objects;

/**
 * Wrapper class for network information object.
 */
public class NetworkInformation extends Child {
    private ACPEXT acpext;
    private TSTMD tstmd;

    /**
     * Constructor.
     * 
     * @param acpext TBD.
     * @param tstmd Test Mode Resource.
     */
    public NetworkInformation(ACPEXT acpext, TSTMD tstmd) {
        this.acpext = acpext;
        this.tstmd = tstmd;
    }
    
    /** 
     * @return ACPEXT
     */
    public ACPEXT getAcpext() {
        return this.acpext;
    }

    
    /** 
     * @return TSTMD The Test Mode Resource.
     */
    public TSTMD getTstmd() {
        return this.tstmd;
    }

    /** 
     * Compares to another instance. 
     * 
     * @param o An object to compare against.
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof NetworkInformation)) {
            return false;
        }
        NetworkInformation networkInformation = (NetworkInformation) o;
        return Objects.equals(acpext, networkInformation.acpext) && Objects.equals(tstmd, networkInformation.tstmd);
    }
    
    /** 
     * @return String The string representation of the instance.
     */
    @Override
    public String toString() {
        return "{" +
            " acpext='" + getAcpext() + "'" +
            ", tstmd='" + getTstmd() + "'" +
            "}";
    }

}
