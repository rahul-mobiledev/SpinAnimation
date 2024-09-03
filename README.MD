[![](https://jitpack.io/v/rahul-mobiledev/SpinAnimation.svg)](https://jitpack.io/#rahul-mobiledev/SpinAnimation)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/SanojPunchihewa/InAppUpdater/blob/master/LICENSE)

<h1 align="center">Spin Animation</h1>
<p align="center">Android Library to easily implement Game of chance</p>

## :pencil2: Usage

### Step 1: Add it in your root build.gradle

```Gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

### Step 2: Add the dependency

```Gradle
dependencies {
    implementation 'com.github.rahul-mobiledev:SpinAnimation:{version}'
}
```

### Step 3: Initialize the UpdateManager

Initialize the UpdateManager in your `onCreate` method of the Activity

```kotlin
// Initialize the App Update Center with the Activity and the API key
binding.spinView.apply {
    // Set String Data List
    setDataItems(
        listOf(
            "100x",
            "0.5x",
            "1x",
            "2x",
            "3x",
            "4x",
            "5x"
        )
    )

    // Start Infinite Scroll
    startInfiniteScroll()



    CoroutineScope(Dispatchers.Main).launch {
        delay(3000)
        // Stop and Set Result to display
        setResult("5x")
    }
}
```

**UI Attributes**

* `scrollSpeedMultiplier` : Speed Multiplier for Scrolling among list (0.5x,1x,1.5x,2x,10x...)
* `decelerationDelay` : Deceleration Delay Multiplier to set result by delay (1x,2x,3x...)
* `edgeItemScale` : Item scale from 0..1 for Edge Items while Scrolling
* `textStyle` : to set Text Appearance
* `itemHeight` : Height of each item (should be divided by 3 of total view height not to get
  bouncing effect)
* `scrollSound` : Raw resource of sound file to play when each item scroll from center
* `vibrationDuration` : Duration in millisecond to vibrate device

## :exclamation: Troubleshoot

- SpinAnimation works only with devices running **Android 5.0 (API level 21) or higher**

## :open_hands: Contributions

Any contributions are welcome!

## :page_facing_up: License

```
MIT License

Copyright (c) 2019 Sanoj Punchihewa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
