package com.onixbyte.captcha.text.enums;

/**
 * Defines the supported font styles for rendering CAPTCHA text.
 * <p>
 * These values typically correspond to {@link java.awt.Font} style flags when creating a
 * {@code Font} instance:
 * <ul>
 *   <li>{@link #PLAIN} &rarr; {@link java.awt.Font#PLAIN}</li>
 *   <li>{@link #BOLD} &rarr; {@link java.awt.Font#BOLD}</li>
 *   <li>{@link #ITALIC} &rarr; {@link java.awt.Font#ITALIC}</li>
 *   <li>{@link #BOLD_ITALIC} &rarr; {@link java.awt.Font#BOLD} | {@link java.awt.Font#ITALIC}</li>
 * </ul>
 * This enum provides an abstraction over AWT constants so the public API can express text styling
 * intent without exposing {@code java.awt} directly.
 *
 * @see java.awt.Font
 */
public enum FontStyle {

    /**
     * Plain style (no bold or italic).
     */
    PLAIN,
    /**
     * Bold style.
     */
    BOLD,
    /**
     * Italic style.
     */
    ITALIC,
    /**
     * Combined bold and italic style.
     */
    BOLD_ITALIC,
}
