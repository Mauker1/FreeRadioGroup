# FreeRadioGroup

FreeRadioGroup is an Android library that allows grouping radio buttons without constraining them into a Linear Layout fashion, you can place the buttons anywhere you like.

[ ![Download](https://api.bintray.com/packages/mauker/maven/FreeRadioGroup/images/download.svg) ](https://bintray.com/mauker/maven/FreeRadioGroup/_latestVersion)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/Mauker1/FreeRadioGroup/blob/main/LICENSE)
![APK size](https://img.shields.io/badge/Size-42KB-e91e63.svg)

<a href='https://ko-fi.com/A623L7G' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=f' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

<img src='https://i.stack.imgur.com/WZE9c.gif' width='216' height='480' />


# Download

To add `FreeRadioGroup` to your project add the following gradle dependency:

```kotlin
implementation 'br.com.mauker:freeradiogroup:1.0.1'
```

Also make sure to have `jcenter()` in your gradle repositories.

# Usage

To use `FreeRadioGroup` add the following code in the same XML layout as your Radio Buttons:

```xml
<br.com.mauker.freeradiogroup.FreeRadioGroup
    android:id="@+id/radioGroup"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:referenced_ids="radioButton,radioButton2,radioButton3"
    app:checkedRadioButton="@id/radioButton2"
    tools:ignore="MissingConstraints" />
```

## Breaking down the properties

- The first and most important attribute is `app:referenced_ids`. Use it to add radio buttons to this group by adding their IDs;
- The `app:checkedRadioButton` attribute will define which radio button from this group should be checked by default;
- As of `V_1.0.0` if you're adding `FreeRadioGroup` inside a constraint layout, it's necessary to add the `tools:ignore="MissingConstraints"` to safely ignore the missing constraints error message.

*Note:* Since `FreeRadioGroup` has no width or height, you can use either `wrap_content` or `0dp`.

# Using the group in your Kotlin (or Java) code

To use `FreeRadioGroup` inside your code, simply use `findViewById()` to get its reference.

```kotlin
val group: FreeRadioGroup = findViewById(R.id.radioGroup)
```

## Checking and clearing programmatically

You can either check an individual radio button from a group or clear the selection using the following methods:

- `group.check(radioButtonId: Int)` to check a the radio button from this group;
- `group.clearCheck()` to clear the current selection.

## Getting the current selection

You can get the selected radio button by using the `group.getCheckedRadioButtonId()` method. It'll return the radio button ID or `View.NO_ID` if there's no selection.

## Setting up the checked listener

It's also possible to listen for changes in selection by using the `OnCheckedChangeListener` interface.

```kotlin
group.setOnCheckedChangeListener(object: OnCheckedChangeListener {
    override fun onCheckedChanged(group: FreeRadioGroup, checkedId: Int) {
        // Do something with the selection
    }
})
```
