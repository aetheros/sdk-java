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

public class BAT extends Child {
    private InnerClass bat;

    public BAT() {}

    public String getBtl() {
        return bat.btl;
    }

    public String getBts() {
        return bat.bts;
    }

    public String getCt() {
        return bat.ct;
    }

    public String getLt() {
        return bat.lt;
    }

    public String getMgd() {
        return bat.mgd;
    }

    public String getPi() {
        return bat.pi;
    }

    public String getRi() {
        return bat.ri;
    }

    public String getRn() {
        return bat.rn;
    }

    public String getTy() {
        return bat.ty;
    }



    class InnerClass {
        private String btl;
        private String bts;
        private String ct;
        private String lt;
        private String mgd;
        private String pi;
        private String ri;
        private String rn;
        private String ty;

        public String getBtl() {
            return this.btl;
        }

        public void setBtl(String btl) {
            this.btl = btl;
        }

        public String getBts() {
            return this.bts;
        }

        public void setBts(String bts) {
            this.bts = bts;
        }

        public String getCt() {
            return this.ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public String getLt() {
            return this.lt;
        }

        public void setLt(String lt) {
            this.lt = lt;
        }

        public String getMgd() {
            return this.mgd;
        }

        public void setMgd(String mgd) {
            this.mgd = mgd;
        }

        public String getPi() {
            return this.pi;
        }

        public void setPi(String pi) {
            this.pi = pi;
        }

        public String getRi() {
            return this.ri;
        }

        public void setRi(String ri) {
            this.ri = ri;
        }

        public String getRn() {
            return this.rn;
        }

        public void setRn(String rn) {
            this.rn = rn;
        }

        public String getTy() {
            return this.ty;
        }

        public void setTy(String ty) {
            this.ty = ty;
        }
    }
}