/*
 * Copyright (c) 2024-2025 OnixByte
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.onixbyte.captcha.gimpy.impl;

import com.onixbyte.captcha.gimpy.GimpyEngine;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * {@link FishEyeGimpy} adds a fish-eye distortion effect to an image, with vertical and horizontal lines.
 */
public class FishEyeGimpy implements GimpyEngine {

    private FishEyeGimpy() {
    }

    /**
     * Applies a fish-eye distortion to the given image, drawing horizontal and vertical lines over it.
     *
     * @param baseImage the image to distort
     * @return the distorted image
     */
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        Graphics2D graph = (Graphics2D) baseImage.getGraphics();
        int imageHeight = baseImage.getHeight();
        int imageWidth = baseImage.getWidth();

        // want lines put them in a variable so we might configure these later
        int horizontalLines = imageHeight / 7;
        int verticalLines = imageWidth / 7;

        // calculate space between lines
        int horizontalGaps = imageHeight / (horizontalLines + 1);
        int verticalGaps = imageWidth / (verticalLines + 1);

        // draw the horizontal stripes
        for (int i = horizontalGaps; i < imageHeight; i = i + horizontalGaps) {
            graph.setColor(Color.blue);
            graph.drawLine(0, i, imageWidth, i);
        }

        // draw the vertical stripes
        for (int i = verticalGaps; i < imageWidth; i = i + verticalGaps) {
            graph.setColor(Color.red);
            graph.drawLine(i, 0, i, imageHeight);
        }

        // create a pixel array of the original image.
        // we need this later to do the operations on..
        int[] pix = new int[imageHeight * imageWidth];
        int j = 0;

        for (int j1 = 0; j1 < imageWidth; j1++) {
            for (int k1 = 0; k1 < imageHeight; k1++) {
                pix[j] = baseImage.getRGB(j1, k1);
                j++;
            }
        }

        double distance = randInt(imageWidth / 4, imageWidth / 3);

        // put the distortion in the (dead) middle
        int widthMiddle = baseImage.getWidth() / 2;
        int heightMiddle = baseImage.getHeight() / 2;

        // again iterate over all pixels..
        for (int x = 0; x < baseImage.getWidth(); x++) {
            for (int y = 0; y < baseImage.getHeight(); y++) {

                int relX = x - widthMiddle;
                int relY = y - heightMiddle;

                double d1 = Math.sqrt(relX * relX + relY * relY);
                if (d1 < distance) {

                    int j2 = widthMiddle + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (x - widthMiddle));
                    int k2 = heightMiddle + (int) (((fishEyeFormula(d1 / distance) * distance) / d1) * (double) (y - heightMiddle));
                    baseImage.setRGB(x, y, pix[j2 * imageHeight + k2]);
                }
            }
        }

        return baseImage;
    }

    private int randInt(int i, int j) {
        double d = Math.random();
        return (int) ((double) i + (double) ((j - i) + 1) * d);
    }

    /**
     * A private fish-eye formula implementation.
     */
    private double fishEyeFormula(double s) {
// ... existing code ...
        if (s > 1.0D) {
            return s;
        } else {
            return -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
        }
    }

    /**
     * Creates a new {@link FishEyeGimpyBuilder}.
     *
     * @return a new {@link FishEyeGimpyBuilder}
     */
    public static FishEyeGimpyBuilder builder() {
        return new FishEyeGimpyBuilder();
    }

    /**
     * A builder for creating {@link FishEyeGimpy} instances.
     */
    public static class FishEyeGimpyBuilder {
        private FishEyeGimpyBuilder() {
        }

        /**
         * Builds a new {@link FishEyeGimpy} with the configured properties.
         *
         * @return a new {@link FishEyeGimpy}
         */
        public FishEyeGimpy build() {
            return new FishEyeGimpy();
        }
    }
}
