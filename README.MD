# Spannator

[![Generic badge](https://img.shields.io/badge/jitPack-1.0.0-ff00.svg)](https://github.com/Magora-Android/Spannator/)

Just a small spannable helper.


# Adding to project
To use Spannee you need to add as a dependency:

`implementation 'com.github.magora-android:spannator:1.0.0'`

And it's available on Jitpack, so you need to add

    allprojects {
        repositories {
            // ...
            maven { url "https://jitpack.io" }
        }
        // ...
    }


# How To
Spannator has several predefined parameters:

- "backgroundColor"
- "textColor"
- "url"
- "textSizePx"
- "textSizeSp"
- "typeface" ("bold", "normal", "monospace")

**Just create a string resource**
like this:

    <string name="text_spannee_bg_and_color"><annotation backgroundColor="#D81B60">Background</annotation> span, <annotation textColor="#4500B2">text color</annotation> span and <annotation backgroundColor="#D81B60"><annotation textColor="#FFFFFF">all together</annotation></annotation></string>

or like this:

    <string name="text_spannee_url">This is <annotation url="https://google.com">Clickable url</annotation> span</string>

then create a Spannee instance

`val spannator = Spannee(resources)`

and load the string

`val textView; TextView = ...`
`textView.text = spannator(R.string.text_spannee_bg_and_color)`

**Also there's a "clickable" span attribute**

    <string name="text_spannee_clickable">This is <annotation clickable="CustomTag">Custom clickable text</annotation> span</string>

`val textView; TextView = ...`
`textView.text = spannator(R.string.text_spannee_clickable) { tag ->
    Toast.makeText(this, "Clicked at tag", Toast.LENGTH_SHORT).show()
}`

... and use the power of Spannator
