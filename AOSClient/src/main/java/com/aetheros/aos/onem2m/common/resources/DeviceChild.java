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
 * Structure class for device object.
 */
public class DeviceChild extends Child {
    private DeviceInformation dvi;
    private ExternalModuleInformation miext;

    
    /** 
     * Constructor I.
     * 
     * @param dvi Device Information.
     */
    public void addDeviceInformation(DeviceInformation dvi) {
        this.dvi = dvi;
    }

    
    /** 
     * Constructor II.
     * 
     * @param miext External Module Infomartion.
     */
    public void addExternalModuleInformation(ExternalModuleInformation miext) {
        this.miext = miext;
    }

    /** 
     * Constructor III.
     * 
     * @param dvi Device Information.
     * @param miext External Module Infomartion.
     */
    public DeviceChild(DeviceInformation dvi, ExternalModuleInformation miext) {
        this.dvi = dvi;
        this.miext = miext;
    }

    
    /** 
     * @return DeviceInformation
     */
    public DeviceInformation getDvi() {
        return this.dvi;
    }

    
    /** 
     * @return ExternalModuleInformation
     */
    public ExternalModuleInformation getMiext() {
        return this.miext;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " dvi='" + getDvi() + "'" +
            ", miext='" + getMiext() + "'" +
            "}";
    }
    
}