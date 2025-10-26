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
import com.jhlabs.image.TransformFilter;
import com.jhlabs.image.WaterFilter;
import com.onixbyte.captcha.gimpy.AbstractGimpyEngine;
import com.onixbyte.captcha.noise.NoiseProducer;
import com.onixbyte.captcha.noise.impl.DefaultNoiseProducer;

import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * An implementation of {@link com.onixbyte.captcha.gimpy.GimpyEngine} that adds a water ripple effect to the image.
 */
public class WaterRipple extends AbstractGimpyEngine {

    private final WaterFilter waterFilter;
    private final RippleFilter rippleFilter;

    /**
     * Creates a new {@link WaterRipple} with the given noise producer.
     *
     * @param noiseProducer the noise producer to use
     */
    private WaterRipple(NoiseProducer noiseProducer) {
        super(noiseProducer);

        this.waterFilter = new WaterFilter();
        waterFilter.setAmplitude(1.5f);
        waterFilter.setPhase(10);
        waterFilter.setWavelength(2);

        this.rippleFilter = new RippleFilter();
        rippleFilter.setWaveType(RippleFilter.SINE);
        rippleFilter.setXAmplitude(2.6f);
        rippleFilter.setYAmplitude(1.7f);
        rippleFilter.setXWavelength(15);
        rippleFilter.setYWavelength(5);
        rippleFilter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);

    }

    /**
     * Applies a water ripple effect to the given image.
     *
     * @param baseImage the image to apply the filter to
     * @return the filtered image
     */
    protected BufferedImage applyFilter(BufferedImage baseImage) {
        BufferedImage effectImage = waterFilter.filter(baseImage, null);
        return rippleFilter.filter(effectImage, null);
    }

    /**
     * Creates a new {@link WaterRippleBuilder}.
     *
     * @return a new {@link WaterRippleBuilder}
     */
    public static WaterRippleBuilder builder() {
        return new WaterRippleBuilder();
    }

    /**
     * A builder for creating {@link WaterRipple} instances.
     */
    public static class WaterRippleBuilder {
        private NoiseProducer noiseProducer;

        private WaterRippleBuilder() {
            this.noiseProducer = DefaultNoiseProducer.builder().build();
        }

        /**
         * Sets the noise producer to use.
         *
         * @param noiseProducer the noise producer
         * @return this builder
         */
        public WaterRippleBuilder noiseProducer(NoiseProducer noiseProducer) {
            if (Objects.isNull(noiseProducer)) {
                throw new IllegalArgumentException("Noise producer should not be null.");
            }

            this.noiseProducer = noiseProducer;
            return this;
        }

        /**
         * Builds a new {@link WaterRipple} with the configured properties.
         *
         * @return a new {@link WaterRipple}
         */
        public WaterRipple build() {
            return new WaterRipple(noiseProducer);
        }
    }
}
