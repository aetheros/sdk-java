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

package com.aetheros.aos.onem2m.examples;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

import com.aetheros.aos.onem2m.client.AOSClient;
import com.aetheros.aos.onem2m.client.cse.CSE;
import com.aetheros.aos.onem2m.common.resources.AE;
import com.aetheros.aos.onem2m.common.resources.Child;
import com.aetheros.aos.onem2m.common.resources.ExternalConnectivityMonitor;
import com.aetheros.aos.onem2m.common.resources.Node;

public class NodeRetrieve {
    public static void main() {
        // Server.
        final String PROTOCOL = "coap";
        final String DEVICE_GATEWAY = "192.168.225.1";
        final int PORT = 8100;

        // File aei will be persisted to.
        final String AE_FILE_TXT = "ae.example.id";

        // Create an instance of the AOSClient.
        AOSClient aos = new AOSClient();

        // AE args.
        final String aeAppId = "Na1.com.aetheros.iot";
        final String aeAppName = "node-test-app";
        final String[] poa = new String[] { String.format("%s://%s:%d", PROTOCOL, DEVICE_GATEWAY, PORT) };

        // Get and instance of the CSE using the AOSClient.
        CSE mncse =  aos.getCSE(DEVICE_GATEWAY, PORT);

        try {
            // Registration. If the aei has not been saved to disk, attempt
            // to reigster with the device. Otherwise, use the stored aei.
            // The AE contains the aei that you need to make requests
            // the the cse.
            File aeFile = new File(AE_FILE_TXT);

            // Create the ae to send to the cse.
            AE ae = new AE(aeAppId, aeAppName, poa);

            // Check if the aei has already been stored to disk.
            if (aeFile.exists()) {
                // Read the contents - aei - from the file and add it to
                // the ae instance.
                Scanner scanner = new Scanner(aeFile);
                String aei = scanner.next();
                scanner.close();

                ae.setAei(aei);
            } else {
                // Register with the device. Override the existing ae with the
                // one returned from the cse registration call.
                // This instance of ae will contain the aei needed to make future
                // requests to the cse.
                ae = mncse.register(ae);

                // Create the file to store the aei.
                if (aeFile.createNewFile()) {
                    // Write the aei to file.
                    FileWriter fileWriter = new FileWriter(AE_FILE_TXT);
                    fileWriter.write(ae.getAei());
                    fileWriter.close();
                } else {
                    System.out.println("There was an error writing the AE to file.");
                }
            }

            // Use the ae to retrieve the node from the cse.
            Node node = mncse.retrieveNode(ae);

            // Get the children.
            List<Child> children = node.getChildren();

            // Find the rsrp and rsrq.
            for (Child child : children) {
                if (child instanceof ExternalConnectivityMonitor) {
                    ExternalConnectivityMonitor cmext = (ExternalConnectivityMonitor) child;
                    System.out.println(cmext.getRsrp());
                    System.out.println(cmext.getRsrq());
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}