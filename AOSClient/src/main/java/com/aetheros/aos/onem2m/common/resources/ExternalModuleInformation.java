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
 * Module Info External Resource (miext).
 */
public class ExternalModuleInformation extends Child {
        private int cnstat;
        private String iccid;
        private String imei;
        private String imsi;
        private String modtm; 
        private String modtz;
        private boolean psmen;


    public ExternalModuleInformation(int cnstat, String iccid, String imei, String imsi, String modtm, String modtz, boolean psmen) {
        this.cnstat = cnstat;
        this.iccid = iccid;
        this.imei = imei;
        this.imsi = imsi;
        this.modtm = modtm;
        this.modtz = modtz;
        this.psmen = psmen;
    }

    public int getCnstat() {
        return this.cnstat;
    }

    public void setCnstat(int cnstat) {
        this.cnstat = cnstat;
    }

    public String getIccid() {
        return this.iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getModtm() {
        return this.modtm;
    }

    public void setModtm(String modtm) {
        this.modtm = modtm;
    }

    public String getModtz() {
        return this.modtz;
    }

    public void setModtz(String modtz) {
        this.modtz = modtz;
    }

    public boolean isPsmen() {
        return this.psmen;
    }

    public boolean getPsmen() {
        return this.psmen;
    }

    public void setPsmen(boolean psmen) {
        this.psmen = psmen;
    }

}