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

package com.onixbyte.captcha.gimpy;

import com.onixbyte.captcha.noise.NoiseProducer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An abstract implementation of {@link GimpyEngine}, which provides a base for creating image distortion effects.
 */
public abstract class AbstractGimpyEngine implements GimpyEngine {

    private final NoiseProducer noiseProducer;

    /**
     * Creates a new {@link AbstractGimpyEngine} with the given noise producer.
     *
     * @param noiseProducer the noise producer to use
     */
    public AbstractGimpyEngine(NoiseProducer noiseProducer) {
        this.noiseProducer = noiseProducer;
    }

    /**
     * Applies a distortion effect to the given image and then adds noise.
     *
     * @param baseImage the image to distort
     * @return the distorted image with noise
     */
    @Override
    public BufferedImage getDistortedImage(BufferedImage baseImage) {
        BufferedImage distortedImage = new BufferedImage(
                baseImage.getWidth(),
                baseImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = (Graphics2D) distortedImage.getGraphics();

        BufferedImage effectImage = applyFilter(baseImage);

        graphics.drawImage(effectImage, 0, 0, null, null);

        graphics.dispose();

        // draw lines over the image and/or text
        noiseProducer.makeNoise(distortedImage, .1f, .1f, .25f, .25f);
        noiseProducer.makeNoise(distortedImage, .1f, .25f, .5f, .9f);

        return distortedImage;
    }

    /**
     * Applies a filter to the given image.
     *
     * @param baseImage the image to apply the filter to
     * @return the filtered image
     */
    protected abstract BufferedImage applyFilter(BufferedImage baseImage);
}