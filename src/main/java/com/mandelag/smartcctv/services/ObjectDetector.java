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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tensorflow.Graph;

/**
 *
 * @author Keenan
 */
public class ObjectDetector {
    public static void main(String[] args) {
        Graph objectDetectionGraph = new Graph();
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream("src\\java\\res\\ssd_mobilenet_v1_coco_2017_11_17\\frozen_inference_graph.pb"))){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos);
            byte[] reader = new byte[1024];
            int lengthRead = 0;
            while( (lengthRead = bis.read(reader) ) != -1) {
                bos.write(Arrays.copyOfRange(reader, 0, lengthRead));
            }
            try(FileOutputStream fos = new FileOutputStream("test.pb")) {
                fos.write(baos.toByteArray());
            }
            //objectDetectionGraph.importGraphDef(graphDef);
        } catch (IOException ex) {
            Logger.getLogger(ObjectDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
