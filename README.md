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
import com.onixbyte.captcha.text.impl.DefaultTextCreator;
import com.onixbyte.captcha.text.impl.DefaultWordRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class CaptchaExample {

    public static void main(String[] args) throws IOException {
        // Define producers and engines
        TextProducer textProducer = new DefaultTextCreator();
        WordRenderer wordRenderer = new DefaultWordRenderer(
                Color.BLACK,
                Collections.singletonList(new Font("Arial", Font.BOLD, 40))
        );
        GimpyEngine gimpyEngine = new WaterRipple(new DefaultNoiseProducer());
        BackgroundProducer backgroundProducer = new DefaultBackgroundProducer(
                Color.WHITE,
                Color.LIGHT_GRAY
        );
        NoiseProducer noiseProducer = new DefaultNoiseProducer();

        // Create a CAPTCHA producer
        Producer captcha = new DefaultCaptchaProducer(
                wordRenderer,
                gimpyEngine,
                backgroundProducer,
                200,
                50,
                true,
                Color.BLACK,
                1,
                textProducer
        );

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