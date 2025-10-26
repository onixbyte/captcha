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

package com.onixbyte.captcha.background.impl;

import com.onixbyte.captcha.background.BackgroundProducer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The default implementation of {@link BackgroundProducer}, which creates a gradient background.
 */
public class DefaultBackgroundProducer implements BackgroundProducer {

    /**
     * The starting colour of the gradient.
     */
    private final Color colourFrom;

    /**
     * The ending colour of the gradient.
     */
    private final Color colourTo;

    /**
     * Creates a new {@link DefaultBackgroundProducer} with the given colours.
     *
     * @param colourFrom the starting colour of the gradient
     * @param colourTo   the ending colour of the gradient
     */
    private DefaultBackgroundProducer(Color colourFrom, Color colourTo) {
        this.colourFrom = colourFrom;
        this.colourTo = colourTo;
    }

    /**
     * Adds a gradient background to the given image.
     *
     * @param baseImage the image to add the background to
     * @return the image with the gradient background added
     */
    public BufferedImage addBackground(BufferedImage baseImage) {
        int width = baseImage.getWidth();
        int height = baseImage.getHeight();

        // create an opaque image
        BufferedImage imageWithBackground = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graph = (Graphics2D) imageWithBackground.getGraphics();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        hints.add(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY));
        hints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));

        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));

        graph.setRenderingHints(hints);

        GradientPaint paint = new GradientPaint(0, 0, colourFrom, width, height, colourTo);
        graph.setPaint(paint);
        graph.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        graph.drawImage(baseImage, 0, 0, null);

        return imageWithBackground;
    }

    /**
     * Creates a new {@link DefaultBackgroundProducerBuilder}.
     *
     * @return a new {@link DefaultBackgroundProducerBuilder}
     */
    public static DefaultBackgroundProducerBuilder builder() {
        return new DefaultBackgroundProducerBuilder();
    }

    /**
     * A builder for creating {@link DefaultBackgroundProducer} instances.
     */
    public static class DefaultBackgroundProducerBuilder {
        private Color colourFrom;
        private Color colourTo;

        private DefaultBackgroundProducerBuilder() {
            this.colourFrom = Color.LIGHT_GRAY;
            this.colourTo = Color.WHITE;
        }

        /**
         * Sets the starting colour of the gradient.
         *
         * @param colourFrom the starting colour
         * @return this builder
         */
        public DefaultBackgroundProducerBuilder colourFrom(Color colourFrom) {
            this.colourFrom = colourFrom;
            return this;
        }

        /**
         * Sets the ending colour of the gradient.
         *
         * @param colourTo the ending colour
         * @return this builder
         */
        public DefaultBackgroundProducerBuilder colourTo(Color colourTo) {
            this.colourTo = colourTo;
            return this;
        }

        /**
         * Builds a new {@link DefaultBackgroundProducer} with the configured properties.
         *
         * @return a new {@link DefaultBackgroundProducer}
         */
        public DefaultBackgroundProducer build() {
            return new DefaultBackgroundProducer(colourFrom, colourTo);
        }
    }
}
