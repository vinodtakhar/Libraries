# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/vinodtakhar/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keepattributes *Annotation*
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn sun.misc.Unsafe.**
-dontwarn com.google.gson.**

-keep class com.phoneutils.crosspromotion.** { *; }


#adcolony
# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
# For removing warnings due to lack of Multi-Window support
-dontwarn android.app.Activity

-ignorewarnings

-keep class com.jirbo.adcolony.** { *; }