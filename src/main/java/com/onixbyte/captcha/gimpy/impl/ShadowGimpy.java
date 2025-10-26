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

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.TransformFilter;
import com.onixbyte.captcha.gimpy.AbstractGimpyEngine;
import com.onixbyte.captcha.noise.NoiseProducer;
import com.onixbyte.captcha.noise.impl.DefaultNoiseProducer;

import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * An implementation of {@link com.onixbyte.captcha.gimpy.GimpyEngine} that adds a shadow and ripple effect to the image.
 */
public class ShadowGimpy extends AbstractGimpyEngine {

    private final RippleFilter rippleFilter;
    private final ShadowFilter shadowFilter;

    /**
     * Creates a new {@link ShadowGimpy} with the given noise producer.
     *
     * @param noiseProducer the noise producer to use
     */
    private ShadowGimpy(NoiseProducer noiseProducer) {
        super(noiseProducer);

        Random rand = new SecureRandom();

        this.rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(7.6f);
        rippleFilter.setYAmplitude(rand.nextFloat() + 1.0f);
        rippleFilter.setXWavelength(rand.nextInt(7) + 8);
        rippleFilter.setYWavelength(rand.nextInt(3) + 2);
        rippleFilter.setEdgeAction(TransformFilter.BILINEAR);

        this.shadowFilter = new ShadowFilter();
        shadowFilter.setRadius(10);
        shadowFilter.setDistance(5);
        shadowFilter.setOpacity(1);
    }

    /**
     * Applies a shadow and ripple effect to the given image.
     *
     * @param baseImage the image to apply the filter to
     * @return the filtered image
     */
    protected BufferedImage applyFilter(BufferedImage baseImage) {
        BufferedImage effectImage = rippleFilter.filter(baseImage, null);
        return shadowFilter.filter(effectImage, null);
    }

    /**
     * Creates a new {@link ShadowGimpyBuilder}.
     *
     * @return a new {@link ShadowGimpyBuilder}
     */
    public static ShadowGimpyBuilder builder() {
        return new ShadowGimpyBuilder();
    }

    /**
     * A builder for creating {@link ShadowGimpy} instances.
     */
    public static class ShadowGimpyBuilder {
        private NoiseProducer noiseProducer;

        private ShadowGimpyBuilder() {
            this.noiseProducer = DefaultNoiseProducer.builder().build();
        }

        /**
         * Sets the noise producer to use.
         *
         * @param noiseProducer the noise producer
         * @return this builder
         */
        public ShadowGimpyBuilder noiseProducer(NoiseProducer noiseProducer) {
            if (Objects.isNull(noiseProducer)) {
                throw new IllegalArgumentException("Noise producer should not be null.");
            }

            this.noiseProducer = noiseProducer;
            return this;
        }

        /**
         * Builds a new {@link ShadowGimpy} with the configured properties.
         *
         * @return a new {@link ShadowGimpy}
         */
        public ShadowGimpy build() {
            return new ShadowGimpy(noiseProducer);
        }
    }
}
