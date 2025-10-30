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

package com.onixbyte.captcha.impl;

import com.onixbyte.captcha.background.BackgroundProducer;
import com.onixbyte.captcha.background.impl.DefaultBackgroundProducer;
import com.onixbyte.captcha.gimpy.GimpyEngine;
import com.onixbyte.captcha.Producer;
import com.onixbyte.captcha.gimpy.impl.WaterRipple;
import com.onixbyte.captcha.text.TextProducer;
import com.onixbyte.captcha.text.WordRenderer;
import com.onixbyte.captcha.text.impl.DefaultTextProducer;
import com.onixbyte.captcha.text.impl.DefaultWordRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Default {@link Producer} implementation which draws a captcha image using {@link WordRenderer},
 * {@link GimpyEngine}, {@link BackgroundProducer}. Text creation uses {@link TextProducer}.
 */
public class DefaultCaptchaProducer implements Producer {

    /**
     * The word renderer.
     */
    private final WordRenderer wordRenderer;

    /**
     * The gimpy engine.
     */
    private final GimpyEngine gimpyEngine;

    /**
     * The background producer.
     */
    private final BackgroundProducer backgroundProducer;

    /**
     * The width of the captcha image.
     */
    private final int width;

    /**
     * The height of the captcha image.
     */
    private final int height;

    /**
     * Whether a border is drawn around the image.
     */
    private final boolean borderDrawn;

    /**
     * The colour of the border.
     */
    private final Color borderColour;

    /**
     * The thickness of the border.
     */
    private final int borderThickness;

    /**
     * The text producer.
     */
    private final TextProducer textProducer;

    /**
     * Creates a new {@link DefaultCaptchaProducer}.
     *
     * @param wordRenderer       the word renderer to use
     * @param gimpyEngine        the gimpy engine to use
     * @param backgroundProducer the background producer to use
     * @param width              the width of the captcha image
     * @param height             the height of the captcha image
     * @param borderDrawn        whether a border should be drawn around the image
     * @param borderColour       the colour of the border
     * @param borderThickness    the thickness of the border
     * @param textProducer       the text producer to use
     */
    private DefaultCaptchaProducer(
            WordRenderer wordRenderer,
            GimpyEngine gimpyEngine,
            BackgroundProducer backgroundProducer,
            int width,
            int height,
            boolean borderDrawn,
            Color borderColour,
            int borderThickness,
            TextProducer textProducer
    ) {
        this.wordRenderer = wordRenderer;
        this.gimpyEngine = gimpyEngine;
        this.backgroundProducer = backgroundProducer;
        this.width = width;
        this.height = height;
        this.borderDrawn = borderDrawn;
        this.borderColour = borderColour;
        this.borderThickness = borderThickness;
        this.textProducer = textProducer;
    }

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return image with the text
     */
    public BufferedImage createImage(String text) {

        BufferedImage bi = wordRenderer.renderWord(text, width, height);
        bi = gimpyEngine.getDistortedImage(bi);
        bi = backgroundProducer.addBackground(bi);
        Graphics2D graphics = bi.createGraphics();
        if (borderDrawn) {
            drawBox(graphics);
        }
        return bi;
    }

    private void drawBox(Graphics2D graphics) {
        graphics.setColor(borderColour);

        if (borderThickness != 1) {
            BasicStroke stroke = new BasicStroke((float) borderThickness);
            graphics.setStroke(stroke);
        }

        Line2D line1 = new Line2D.Double(0, 0, 0, ((double) width));
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0, 0, width, 0);
        graphics.draw(line2);
        line2 = new Line2D.Double(0, height - 1, width, height - 1);
        graphics.draw(line2);
        line2 = new Line2D.Double(width - 1, height - 1, width - 1, 0);
        graphics.draw(line2);
    }

    /**
     * @return the text to be drawn
     */
    public String createText() {
        return textProducer.getText();
    }

    /**
     * Creates a new {@link DefaultCaptchaProducerBuilder}.
     *
     * @return a new {@link DefaultCaptchaProducerBuilder}
     */
    public static DefaultCaptchaProducerBuilder builder() {
        return new DefaultCaptchaProducerBuilder();
    }

    /**
     * A builder for creating {@link DefaultCaptchaProducer} instances.
     */
    public static class DefaultCaptchaProducerBuilder {
        private WordRenderer wordRenderer;
        private GimpyEngine gimpyEngine;
        private BackgroundProducer backgroundProducer;
        private int width;
        private int height;
        private boolean borderDrawn;
        private Color borderColour;
        private int borderThickness;
        private TextProducer textProducer;

        private DefaultCaptchaProducerBuilder() {
            this.wordRenderer = DefaultWordRenderer.builder().build();
            this.gimpyEngine = (GimpyEngine) WaterRipple.builder().build();
            this.backgroundProducer = DefaultBackgroundProducer.builder().build();
            this.width = 200;
            this.height = 50;
            this.borderDrawn = true;
            this.borderColour = Color.BLACK;
            this.borderThickness = 1;
            this.textProducer = DefaultTextProducer.builder().build();
        }

        /**
         * Sets the word renderer to use.
         *
         * @param wordRenderer the word renderer
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder wordRenderer(WordRenderer wordRenderer) {
            this.wordRenderer = wordRenderer;
            return this;
        }

        /**
         * Sets the gimpy engine to use.
         *
         * @param gimpyEngine the gimpy engine
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder gimpyEngine(GimpyEngine gimpyEngine) {
            this.gimpyEngine = gimpyEngine;
            return this;
        }

        /**
         * Sets the background producer to use.
         *
         * @param backgroundProducer the background producer
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder backgroundProducer(BackgroundProducer backgroundProducer) {
            this.backgroundProducer = backgroundProducer;
            return this;
        }

        /**
         * Sets the width of the captcha image.
         *
         * @param width the width
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder width(int width) {
            this.width = width;
            return this;
        }

        /**
         * Sets the height of the captcha image.
         *
         * @param height the height
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder height(int height) {
            this.height = height;
            return this;
        }

        /**
         * Sets whether a border should be drawn around the image.
         *
         * @param borderDrawn whether a border should be drawn
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder borderDrawn(boolean borderDrawn) {
            this.borderDrawn = borderDrawn;
            return this;
        }

        /**
         * Sets the colour of the border.
         *
         * @param borderColour the border colour
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder borderColour(Color borderColour) {
            this.borderColour = borderColour;
            return this;
        }

        /**
         * Sets the thickness of the border.
         *
         * @param borderThickness the border thickness
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder borderThickness(int borderThickness) {
            this.borderThickness = borderThickness;
            return this;
        }

        /**
         * Sets the text producer to use.
         *
         * @param textProducer the text producer
         * @return this builder
         */
        public DefaultCaptchaProducerBuilder textProducer(TextProducer textProducer) {
            this.textProducer = textProducer;
            return this;
        }

        /**
         * Builds a new {@link DefaultCaptchaProducer} with the configured properties.
         *
         * @return a new {@link DefaultCaptchaProducer}
         */
        public DefaultCaptchaProducer build() {
            return new DefaultCaptchaProducer(wordRenderer, gimpyEngine, backgroundProducer, width, height, borderDrawn, borderColour, borderThickness, textProducer);
        }
    }
}
