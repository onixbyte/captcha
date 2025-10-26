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
import com.onixbyte.captcha.text.impl.DefaultTextCreator;

import java.awt.image.BufferedImage;

/**
 * A {@link NoiseProducer} that does not add any noise to the image.
 */
public class NoNoiseProducer implements NoiseProducer {

    private NoNoiseProducer() {
    }

    /**
     * This implementation does nothing.
     *
     * @param image       the image to add the noise to
     * @param factorOne   ignored
     * @param factorTwo   ignored
     * @param factorThree ignored
     * @param factorFour  ignored
     */
    @Override
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {
        // Do nothing.
    }

    /**
     * Creates a new {@link NoNoiseProducerBuilder}.
     *
     * @return a new {@link NoNoiseProducerBuilder}
     */
    public static NoNoiseProducerBuilder builder() {
        return new NoNoiseProducerBuilder();
    }

    /**
     * A builder for creating {@link NoNoiseProducer} instances.
     */
    public static class NoNoiseProducerBuilder {
        private NoNoiseProducerBuilder() {}

        /**
         * Builds a new {@link NoNoiseProducer} with the configured properties.
         *
         * @return a new {@link NoNoiseProducer}
         */
        public NoiseProducer build() {
            return new NoNoiseProducer();
        }
    }
}
