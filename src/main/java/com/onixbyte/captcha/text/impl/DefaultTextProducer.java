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

package com.onixbyte.captcha.text.impl;

import com.onixbyte.captcha.text.TextProducer;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * The default implementation of {@link TextProducer}.
 */
public class DefaultTextProducer implements TextProducer {

    private final int length;

    private final char[] chars;

    private DefaultTextProducer(int length, char[] chars) {
        this.length = length;
        this.chars = chars;
    }

    /**
     * Returns a string of random characters.
     *
     * @return a string of random characters
     */
    public String getText() {
        Random rand = new SecureRandom();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(chars[rand.nextInt(chars.length)]);
        }

        return text.toString();
    }

    /**
     * Creates a new {@link DefaultTextProducerBuilder}.
     *
     * @return a new {@link DefaultTextProducerBuilder}
     */
    public static DefaultTextProducerBuilder builder() {
        return new DefaultTextProducerBuilder();
    }

    /**
     * A builder for creating {@link DefaultTextProducer} instances.
     */
    public static class DefaultTextProducerBuilder {
        private int length;
        private char[] chars;

        private DefaultTextProducerBuilder() {
            this.length = 6;
            this.chars = new char[]{
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                    'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
                    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                    'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            };
        }

        /**
         * Sets the length of the text to generate.
         *
         * @param length the length
         * @return this builder
         */
        public DefaultTextProducerBuilder length(int length) {
            if (length <= 0) {
                throw new IllegalArgumentException("Length should be greater than 0.");
            }
            this.length = length;
            return this;
        }

        /**
         * Sets the characters to use for generating the text.
         *
         * @param chars the characters
         * @return this builder
         */
        public DefaultTextProducerBuilder chars(char[] chars) {
            if (Objects.isNull(chars) || chars.length == 0) {
                throw new IllegalArgumentException("Chars cannot be empty.");
            }
            this.chars = chars;
            return this;
        }

        /**
         * Builds a new {@link DefaultTextProducer} with the configured properties.
         *
         * @return a new {@link DefaultTextProducer}
         */
        public DefaultTextProducer build() {
            return new DefaultTextProducer(length, chars);
        }
    }
}
