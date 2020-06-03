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

import java.util.ArrayList;

/**
 * Device Information Resource.
 */
public class DeviceInformation extends Child {
    private String ct;
    private String dlb;
    private String dty;
    private String fwv;
    private String hwv;
    private ArrayList<String> lbl;
    private String lt;
    private String man;
    private int mgd;
    private String mod;
    private String pi;
    private String ri;
    private String rn;
    private String swv;
    private int ty;

    public DeviceInformation(String dty, String fwv, String mod) {
        this.dty = dty;
        this.fwv = fwv; 
        this.mod = mod;
    }

    /**
     * Returns value of ct
     * 
     * @return
     */
    public String getCt() {
        return ct;
    }

    /**
     * Sets new value of ct
     * 
     * @param
     */
    public void setCt(String ct) {
        this.ct = ct;
    }

    /**
     * Returns value of dlb
     * 
     * @return
     */
    public String getDlb() {
        return dlb;
    }

    /**
     * Sets new value of dlb
     * 
     * @param
     */
    public void setDlb(String dlb) {
        this.dlb = dlb;
    }

    /**
     * Returns value of dty
     * 
     * @return
     */
    public String getDty() {
        return dty;
    }

    /**
     * Sets new value of dty
     * 
     * @param
     */
    public void setDty(String dty) {
        this.dty = dty;
    }

    /**
     * Returns value of fwv
     * 
     * @return
     */
    public String getFwv() {
        return fwv;
    }

    /**
     * Sets new value of fwv
     * 
     * @param
     */
    public void setFwv(String fwv) {
        this.fwv = fwv;
    }

    /**
     * Returns value of hwv
     * 
     * @return
     */
    public String getHwv() {
        return hwv;
    }

    /**
     * Sets new value of hwv
     * 
     * @param
     */
    public void setHwv(String hwv) {
        this.hwv = hwv;
    }

    /**
     * Add a meter id to the lbl array.
     * @param lbl
     */
    public void addLbl(String lbl) {
        this.lbl.add(lbl);
    }

    /**
     * 
     * @param lbl
     */
    public ArrayList<String> getLbl() {
        return this.lbl;
    }


    /**
     * Returns value of lt
     * 
     * @return
     */
    public String getLt() {
        return lt;
    }

    /**
     * Sets new value of lt
     * 
     * @param
     */
    public void setLt(String lt) {
        this.lt = lt;
    }

    /**
     * Returns value of man
     * 
     * @return
     */
    public String getMan() {
        return man;
    }

    /**
     * Sets new value of man
     * 
     * @param
     */
    public void setMan(String man) {
        this.man = man;
    }

    /**
     * Returns value of mgd
     * 
     * @return
     */
    public int getMgd() {
        return mgd;
    }

    /**
     * Sets new value of mgd
     * 
     * @param
     */
    public void setMgd(int mgd) {
        this.mgd = mgd;
    }

    /**
     * Returns value of mod
     * 
     * @return
     */
    public String getMod() {
        return mod;
    }

    /**
     * Sets new value of mod
     * 
     * @param
     */
    public void setMod(String mod) {
        this.mod = mod;
    }

    /**
     * Returns value of pi
     * 
     * @return
     */
    public String getPi() {
        return pi;
    }

    /**
     * Sets new value of pi
     * 
     * @param
     */
    public void setPi(String pi) {
        this.pi = pi;
    }

    /**
     * Returns value of ri
     * 
     * @return
     */
    public String getRi() {
        return ri;
    }

    /**
     * Sets new value of ri
     * 
     * @param
     */
    public void setRi(String ri) {
        this.ri = ri;
    }

    /**
     * Returns value of rn
     * 
     * @return
     */
    public String getRn() {
        return rn;
    }

    /**
     * Sets new value of rn
     * 
     * @param
     */
    public void setRn(String rn) {
        this.rn = rn;
    }

    /**
     * Returns value of swv
     * 
     * @return
     */
    public String getSwv() {
        return swv;
    }

    /**
     * Sets new value of swv
     * 
     * @param
     */
    public void setSwv(String swv) {
        this.swv = swv;
    }

    /**
     * Returns value of ty
     * 
     * @return
     */
    public int getTy() {
        return ty;
    }

    /**
     * Sets new value of ty
     * 
     * @param
     */
    public void setTy(int ty) {
        this.ty = ty;
    }
}