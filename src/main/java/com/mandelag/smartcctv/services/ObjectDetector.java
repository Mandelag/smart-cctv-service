/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.smartcctv.services;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Session;

/**
 *
 * @author Keenan
 */
public class ObjectDetector {

    public static void main(String[] args) {
        Graph objectDetectionGraph = new Graph();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("src\\main\\java\\res\\ssd_mobilenet_v1_coco_2017_11_17\\frozen_inference_graph.pb"))) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);
            byte[] reader = new byte[1024];
            int lengthRead = 0;
            while ((lengthRead = bis.read(reader)) != -1) {
                bos.write(Arrays.copyOfRange(reader, 0, lengthRead));
            }
            bos.flush();
            //try (FileOutputStream fos = new FileOutputStream("test.pb")) {
            //    fos.write(baos.toByteArray());
            //}
            objectDetectionGraph.importGraphDef(baos.toByteArray());
            Iterator<Operation> opsIterator = objectDetectionGraph.operations();
            while (opsIterator.hasNext()) {
                Operation ops = opsIterator.next();
                try {
                    int length = ops.numOutputs();
                    for (int i=0; i<length; i++) {
                        System.out.println(ops.output(i));
                    }
                } catch (IllegalArgumentException e) {

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ObjectDetector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
