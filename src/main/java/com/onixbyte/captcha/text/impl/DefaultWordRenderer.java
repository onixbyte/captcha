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

import com.onixbyte.captcha.text.enums.FontStyle;
import com.onixbyte.captcha.text.WordRenderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * The default implementation of {@link WordRenderer}.
 */
public class DefaultWordRenderer implements WordRenderer {

    private final int fontSize;
    private final Font[] fonts;
    private final Color fontColour;
    private final int charSpace;

    /**
     * Creates a new {@link DefaultWordRenderer}.
     *
     * @param fontSize   the font size
     * @param fonts      the fonts to use
     * @param fontColour the font colour
     * @param charSpace  the space between characters
     */
    private DefaultWordRenderer(int fontSize, Font[] fonts, Color fontColour, int charSpace) {
        this.fontSize = fontSize;
        this.fonts = fonts;
        this.fontColour = fontColour;
        this.charSpace = charSpace;
    }

    /**
     * Renders a word to an image.
     *
     * @param word   the word to be rendered
     * @param width  the width of the image to be created
     * @param height the height of the image to be created
     * @return the BufferedImage created from the word
     */
    @Override
    public BufferedImage renderWord(String word, int width, int height) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();
        g2D.setColor(fontColour);

        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);

        FontRenderContext frc = g2D.getFontRenderContext();
        Random random = new SecureRandom();

        int startPosY = (height - fontSize) / 5 + fontSize;

        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int widthNeeded = 0;
        for (int i = 0; i < wordChars.length; i++) {
            chosenFonts[i] = fonts[random.nextInt(fonts.length)];

            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            charWidths[i] = (int) gv.getVisualBounds().getWidth();
            if (i > 0) {
                widthNeeded = widthNeeded + 2;
            }
            widthNeeded = widthNeeded + charWidths[i];
        }

        int startPosX = (width - widthNeeded) / 2;
        for (int i = 0; i < wordChars.length; i++) {
            g2D.setFont(chosenFonts[i]);
            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + (int) charWidths[i] + charSpace;
        }

        return image;
    }

    /**
     * Creates a new {@link DefaultWordRendererBuilder}.
     *
     * @return a new {@link DefaultWordRendererBuilder}
     */
    public static DefaultWordRendererBuilder builder() {
        return new DefaultWordRendererBuilder();
    }

    /**
     * A builder for creating {@link DefaultWordRenderer} instances.
     */
    public static class DefaultWordRendererBuilder {
        private int fontSize;
        private String[] fonts;
        private Color fontColour;
        private int charSpace;
        private FontStyle fontStyle;

        private DefaultWordRendererBuilder() {
            this.fontSize = 40;
            this.fonts = new String[]{"Arial", "Courier"};
            this.fontColour = Color.BLACK;
            this.charSpace = 2;
            this.fontStyle = FontStyle.BOLD;
        }

        /**
         * Sets the font size to use.
         *
         * @param fontSize the font size
         * @return this builder
         */
        public DefaultWordRendererBuilder fontSize(int fontSize) {
            if (fontSize <= 0) {
                throw new IllegalArgumentException("Font size should be greater than 0.");
            }

            this.fontSize = fontSize;
            return this;
        }

        /**
         * Sets the fonts to use.
         *
         * @param fonts the fonts
         * @return this builder
         */
        public DefaultWordRendererBuilder fonts(String... fonts) {
            if (Objects.isNull(fonts) || fonts.length == 0) {
                throw new IllegalArgumentException("Fonts should not be empty.");
            }

            for (String font : fonts) {
                if (Objects.isNull(font) || !font.trim().isEmpty()) {
                    throw new IllegalArgumentException("Font should not be null.");
                }
            }

            this.fonts = fonts;
            return this;
        }

        public DefaultWordRendererBuilder fontStyle(FontStyle fontStyle) {
            this.fontStyle = fontStyle;
            return this;
        }

        /**
         * Sets the font colour to use.
         *
         * @param fontColour the font colour
         * @return this builder
         */
        public DefaultWordRendererBuilder fontColour(Color fontColour) {
            if (Objects.isNull(fontColour)) {
                throw new IllegalArgumentException("Font colour should not be null.");
            }

            this.fontColour = fontColour;
            return this;
        }

        /**
         * Sets the space between characters.
         *
         * @param charSpace the char space
         * @return this builder
         */
        public DefaultWordRendererBuilder charSpace(int charSpace) {
            if (charSpace <= 0) {
                throw new IllegalArgumentException("Char space should be greater than 0.");
            }

            this.charSpace = charSpace;
            return this;
        }

        /**
         * Builds a new {@link DefaultWordRenderer} with the configured properties.
         *
         * @return a new {@link DefaultWordRenderer}
         */
        public DefaultWordRenderer build() {
            Font[] _fonts = new Font[fonts.length];

            int _fontStyle = Font.BOLD;
            switch (fontStyle) {
                case PLAIN:
                    _fontStyle = Font.PLAIN;
                    break;
                case ITALIC:
                    _fontStyle = Font.ITALIC;
                    break;
                case BOLD_ITALIC:
                    _fontStyle = Font.BOLD | Font.ITALIC;
                    break;
                default:
                    // nothing happens since font style default to Font.BOLD
            }

            for (int index = 0; index < fonts.length; ++index) {
                _fonts[index] = new Font(fonts[index], _fontStyle, fontSize);
            }

            return new DefaultWordRenderer(fontSize, _fonts, fontColour, charSpace);
        }
    }
}
