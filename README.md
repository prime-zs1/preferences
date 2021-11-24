# preferences

[![version](https://badge.fury.io/gh/conventional-changelog%2Fstandard-version.svg)](https://badge.fury.io/gh/conventional-changelog%2Fstandard-version)
# Amplitude

## How to add

Add below lines in your root build.gradle at the end of repositories

``` groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency to your app build.gradle file

``` groovy
dependencies {
     implementation 'com.github.zikrt:preferences:latest_version'
}
```

And then sync your gradle.

