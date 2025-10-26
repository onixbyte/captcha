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

package com.onixbyte.captcha.noise.impl;

import com.onixbyte.captcha.noise.NoiseProducer;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

/**
 * The default implementation of {@link NoiseProducer}.
 */
public class DefaultNoiseProducer implements NoiseProducer {

    private final Color noiseColour;

    /**
     * Creates a new {@link DefaultNoiseProducer} with the given noise colour.
     *
     * @param noiseColour the colour of the noise
     */
    private DefaultNoiseProducer(Color noiseColour) {
        this.noiseColour = noiseColour;
    }

    /**
     * Adds noise to an image. It uses four factor values to determine the noise
     * curve.
     *
     * @param image       the image to add the noise to
     * @param factorOne   the factor for the first control point of the noise curve
     * @param factorTwo   the factor for the second control point of the noise curve
     * @param factorThree the factor for the third control point of the noise curve
     * @param factorFour  the factor for the fourth control point of the noise curve
     */
    @Override
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
        // image size
        int width = image.getWidth();
        int height = image.getHeight();

        // the points where the line changes the stroke and direction
        Point2D[] points;
        Random rand = new SecureRandom();

        // the curve from where the points are taken
        CubicCurve2D cubicCurve = new CubicCurve2D.Float(
                width * factorOne, height * rand.nextFloat(),
                width * factorTwo, height * rand.nextFloat(),
                width * factorThree, height * rand.nextFloat(),
                width * factorFour, height * rand.nextFloat()
        );

        // creates an iterator to define the boundary of the flattened curve
        PathIterator pi = cubicCurve.getPathIterator(null, 2);
        Point2D[] tmp = new Point2D[200];
        int i = 0;

        // while pi is iterating the curve, adds points to tmp array
        while (!pi.isDone()) {
            float[] coords = new float[6];
            switch (pi.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                case PathIterator.SEG_LINETO:
                    tmp[i] = new Point2D.Float(coords[0], coords[1]);
            }
            i++;
            pi.next();
        }

        points = new Point2D[i];
        System.arraycopy(tmp, 0, points, 0, i);

        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        graph.setColor(noiseColour);

        // for the maximum 3 point change the stroke and direction
        for (i = 0; i < points.length - 1; i++) {
            if (i < 3) {
                graph.setStroke(new BasicStroke(0.9f * (4 - i)));
            }
            graph.drawLine(
                    (int) points[i].getX(), (int) points[i].getY(),
                    (int) points[i + 1].getX(), (int) points[i + 1].getY()
            );
        }

        graph.dispose();
    }

    /**
     * Creates a new {@link DefaultNoiseProducerBuilder}.
     *
     * @return a new {@link DefaultNoiseProducerBuilder}
     */
    public static DefaultNoiseProducerBuilder builder() {
        return new DefaultNoiseProducerBuilder();
    }

    /**
     * A builder for creating {@link DefaultNoiseProducer} instances.
     */
    public static class DefaultNoiseProducerBuilder {
        private Color noiseColour;

        private DefaultNoiseProducerBuilder() {
            this.noiseColour = Color.BLACK;
        }

        /**
         * Sets the colour of the noise.
         *
         * @param noiseColour the noise colour
         * @return this builder
         */
        public DefaultNoiseProducerBuilder noiseColour(Color noiseColour) {
            this.noiseColour = noiseColour;
            return this;
        }

        /**
         * Builds a new {@link DefaultNoiseProducer} with the configured properties.
         *
         * @return a new {@link DefaultNoiseProducer}
         */
        public DefaultNoiseProducer build() {
            return new DefaultNoiseProducer(noiseColour);
        }
    }
}
