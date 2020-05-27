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
import java.util.List;

/**
 * OneM2M Node Resource.
 */
public class Node {
    private ChildrenContainer ch;
    private String ct;
    private String lt;
    private String ni;
    private String pi;
    private String ri;
    private String rn;
    private int ty;

    /**
     * Default constructor.
     */
    public Node() {
        this.ch = new ChildrenContainer();
    }

    /**
     * Adds a child to the Node's childrens container (ch).
     * 
     * @param child An instance of a class derived from Child.
     */
    public void addChild(Child child) {
        this.ch.addChild(child);
    }

    /**
     * A child container.
     */
    public static class ChildrenContainer extends ArrayList<Child> {
        /**
         * Adds a child to the Node's children container.
         * @param child An instance of a class derived from Child.
         */
        public void addChild(Child child) {
            this.add(child);
        }
    }

    /**
     * Get all of the node's children.
     * 
     * @return A list of the node's children.
     */
    public List<Child> getChildren() {
        return this.ch;
    }
}