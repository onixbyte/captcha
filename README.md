# Captcha

A Java library for generating CAPTCHA images. This project is a spiritual successor to Google's Kaptcha, modernised for contemporary development practices.

## Features

*   Easy to use and integrate into your existing projects.
*   Customisable CAPTCHA generation with various distortion effects.
*   Support for different text, noise, and background producers.
*   Clean, modern, and well-documented codebase.

## Installation

### Gradle

Add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.onixbyte:captcha:1.0.0")
}
```

### Maven

Add the following to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.onixbyte</groupId>
    <artifactId>captcha</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

Here is a simple example of how to generate a CAPTCHA image:

```java
import com.onixbyte.captcha.Producer;
import com.onixbyte.captcha.background.BackgroundProducer;
import com.onixbyte.captcha.background.impl.DefaultBackgroundProducer;
import com.onixbyte.captcha.gimpy.GimpyEngine;
import com.onixbyte.captcha.gimpy.impl.WaterRipple;
import com.onixbyte.captcha.impl.DefaultCaptchaProducer;
import com.onixbyte.captcha.noise.NoiseProducer;
import com.onixbyte.captcha.noise.impl.DefaultNoiseProducer;
import com.onixbyte.captcha.text.TextProducer;
import com.onixbyte.captcha.text.WordRenderer;
import com.onixbyte.captcha.text.enums.FontStyle;
import com.onixbyte.captcha.text.impl.DefaultTextProducer;
import com.onixbyte.captcha.text.impl.DefaultWordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CaptchaExample {

    public static void main(String[] args) throws IOException {
        // Define producers and engines
        TextProducer textProducer = DefaultTextProducer.builder()
                // .length(6) // default length is 6
                // .chars("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) // default available characters are 'a' to 'z', 'A' to 'Z' and '0' - '9'
                .build();
        WordRenderer wordRenderer = DefaultWordRenderer.builder()
                // .fontColour(Color.BLACK) // default to Color.BLACK
                // .fonts("Arial") // default to Arial and Courier
                // .fontSize(40) // default to 40
                // .charSpace(2) // default to 2
                // .fontStyle(FontStyle.ITALIC) // Default to FontStyle.BOLD
                .build();
        NoiseProducer noiseProducer = DefaultNoiseProducer.builder()
                // .noiseColour(Color.BLACK) // default to Color.BLACK
                .build();
        GimpyEngine gimpyEngine = WaterRipple.builder()
                // .noiseProducer(DefaultNoiseProducer.builder().build()) // default to DefaultNoiseProducer with default configurations
                .build();
        BackgroundProducer backgroundProducer = DefaultBackgroundProducer.builder()
                // .colourFrom(Color.WHITE) // default to Color.LIGHT_GRAY
                // .colourTo(Color.LIGHT_GRAY) // default to Color.WHITE
                .build();

        // Create a CAPTCHA producer
        Producer captcha = DefaultCaptchaProducer.builder()
                .textProducer(textProducer) // default to DefaultTextProducer with default configurations
                .wordRenderer(wordRenderer) // default to DefaultWordRenderer with default configurations
                .gimpyEngine(gimpyEngine) // default to WaterRipple with default configurations
                .backgroundProducer(backgroundProducer) // default to DefaultBackgroundProducer with default configurations
                .build();

        // Generate text and create the image
        String captchaText = captcha.createText();
        BufferedImage captchaImage = captcha.createImage(captchaText);

        // Save the image to a file
        ImageIO.write(captchaImage, "png", new File("captcha.png"));

        System.out.println("CAPTCHA text: " + captchaText);
        System.out.println("CAPTCHA image saved to captcha.png");
    }
}
```

## Licence

This project is licenced under the MIT license. See the `LICENSE` file for more details.