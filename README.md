# Tomo

Tomo is a collection of fast image processing effects for Android.
Its main goal is to generate dynamic content for aesthetically pleasing apps.

## Showcase

In this demo app we showcase a cool adaptive background being generated using
the content of the screen:

![](/docs/tomo_showcase.gif)

## Using it

Add the snippet below in your root `build.gradle` at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then, add the dependency to your module:

```
dependencies {
    compile 'com.github.AllanHasegawa:Tomo:x.y.z'
}
```

Latest release: [![Release](https://jitpack.io/v/AllanHasegawa/Tomo.svg)](https://jitpack.io/#AllanHasegawa/Tomo)

Initialize the library in your `Application` class:

```
class MyApp : Application {
    override fun onCreate() {
        Tomo.initialize(this)
        ...
    }
}
```

Now you're ready to either apply the effects over `Bitmap`s or `ImageView`s:

```
val myBitmap: Bitmap = ...
val bitmapProcessed = Tomo.applyAdaptiveBackgroundGenerator(myBitmap, darkTheme = true)

val myImageView: ImageView = ...
Tomo.applyAdaptiveBackgroundGenerator(myImageView, darkTheme = true)
```

## Built-in effects

### Adaptive Background Generator

<table>
  <tr>
    <th rowspan="2">Source Image</th>
    <th colspan="2">Adaptive Background Generator</th>
  </tr>
  <tr>
    <th>Dark Theme</th>
    <th>Light Theme</th>
  </tr>
  <tr>
    <td><img width="100" src="/docs/tsunami_original.jpg?raw=true"></td>
    <td><img width="100" src="/docs/tsunami_adp_bg_dark.png?raw=true"></td>
    <td><img width="100" src="/docs/tsunami_adp_bg_light.png?raw=true"></td>
  </tr>
  <tr>
    <td><img width="100" src="/docs/dali_original.jpg?raw=true"></td>
    <td><img width="100" src="/docs/dali_adp_bg_dark.png?raw=true"></td>
    <td><img width="100" src="/docs/dali_adp_bg_light.png?raw=true"></td>
  </tr>
</table>

## Custom effects

Tomo comes equipped with a list of image transformations that can be
arranged in any order to build cool custom effects.

To transform a `Bitmap`, call `Tomo::applyCustomTransformation()`:

```kotlin
val newBitmap = Tomo.applyCustomTransformation(oldBitmap) {
    // Scale to 1/10 of its size
    resize(
        newWidth = initialSize.width / 10,
        newHeight = initialSize.height / 10
    )

    // Blur it
    blur(radius = 25f)

    // Clamp the value (from HSV)
    valueClamp(
        lowValue = 0.05f,
        highValue = 0.3f,
        saturationMultiplier = 1.3f,
        saturationLowValue = 0f,
        saturationHighValue = 1f
    )

    // Apply a noise overlay
    grayNoise()
}
```

### `resize`

`resize`, as the name implies, lets you resize the bitmap.

### `blur`

`blur` applies a gaussian blur. It's maximum radius is `25f`.

### `valueClamp`

`valueClamp` clamps the value and the saturation of an image.
It can also scale the saturation.

### `grayNoise`

`grayNoise` applies a gray noise over the image.

### `rgbNoise`

`rgbNoise` assigns a random, close, RGB color to each pixel.

# License

```
Copyright 2019 Allan Yoshio Hasegawa

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```