/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mandelag.smartcctv.services;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Session;
import org.opencv.imgcodecs.Imgcodecs;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;

/**
 *
 * @author Keenan
 */
public class ObjectDetector {

    private static void bgr2rgb(byte[] data) {
        for (int i = 0; i < data.length; i += 3) {
            byte tmp = data[i];
            data[i] = data[i + 2];
            data[i + 2] = tmp;
        }
    }

    private static Tensor<UInt8> makeImageTensor(String filename) throws IOException {
        BufferedImage img = ImageIO.read(new File(filename));
        if (img.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            throw new IOException(
                    String.format(
                            "Expected 3-byte BGR encoding in BufferedImage, found %d (file: %s). This code could be made more robust",
                            img.getType(), filename));
        }
        byte[] data = ((DataBufferByte) img.getData().getDataBuffer()).getData();
        // ImageIO.read seems to produce BGR-encoded images, but the model expects RGB.
        bgr2rgb(data);
        final long BATCH_SIZE = 1;
        final long CHANNELS = 3;
        long[] shape = new long[]{BATCH_SIZE, img.getHeight(), img.getWidth(), CHANNELS};
        return Tensor.create(UInt8.class, shape, ByteBuffer.wrap(data));
    }

    public static void main(String[] args) {
        Graph objectDetectionGraph = new Graph();
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("src\\main\\models\\ssd_mobilenet_v1_coco_2017_11_17\\frozen_inference_graph.pb"))) {
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
                    for (int i = 0; i < length; i++) {
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
