# Preserve line numbers for crash traces (e.g., Firebase Crashlytics)
-keepattributes SourceFile,LineNumberTable

# Optional: Hide original source file names in stack traces
-renamesourcefileattribute SourceFile

# General Android & Java
-dontwarn sun.misc.**
-dontwarn java.lang.invoke.*

# Keep application classes
-keep class your.package.name.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Moshi (if you're using it instead of Gson)
-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.* class * { *; }

# Room (Jetpack persistence)
-keep class androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
-keepattributes Signature
-keepattributes *Annotation*

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Firebase Analytics
-keep class com.google.android.gms.measurement.** { *; }
-dontwarn com.google.android.gms.measurement.**

# Firebase Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

# Glide (if used)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Dagger / Hilt
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.**
-dontwarn javax.inject.**


# Serializable support
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep classes with native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Optional: if you don't want to ship JCIP annotations
-dontwarn net.jcip.annotations.**
-dontwarn net.minidev.asm.**

# Keep WebView JavaScript interface (if any)
# -keepclassmembers class your.package.YourJsInterface {
#    pu
