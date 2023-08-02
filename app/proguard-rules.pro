## Common
-keepattributes Signature, InnerClasses
-keepattributes *Annotation*
-keepattributes Exceptions
-dontwarn javax.annotation.**

## Remove logs
-assumenosideeffects class android.util.Log* {
    public static *** isLoggable(java.lang.String, int);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** d(...);
}
-assumenosideeffects class timber.log.Timber* {
    public static *** tag(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** d(...);
}

## Androidx
-keepclassmembers class androidx.fragment.app.FragmentManagerState** {
    *** Companion;
}

## ViewBinding
-keep, allowobfuscation class * implements androidx.viewbinding.ViewBinding {
    public protected <methods>;
}
# Rules for res/navogation/$name/argType/<navigation>
#     <fragment/activity> <argument app:argType="package.YourClassR8"> </fragment/activity>
# </navigation>
# See https://developer.android.com/guide/navigation/navigation-pass-data#proguard_considerations

## Dagger
-dontwarn com.google.errorprone.annotations.**

# Gson
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Kotlin serializer
-dontnote kotlinx.serialization.SerializationKt
-keepclassmembers class com.opasichnyi.beautify.data.api.entity** {
    *** Companion;
}
-keepclasseswithmembers class com.opasichnyi.beautify.data.api.entity** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.opasichnyi.beautify.data.api.entity.**$$serializer { *; }

# Kotlin serializer enums
-keepclassmembers class com.opasichnyi.beautify.data.api.type** {
    *** Companion;
}
-keepclasseswithmembers class com.opasichnyi.beautify.data.api.type** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.opasichnyi.beautify.data.api.type.**$$serializer { *; }
-keepclassmembers enum com.opasichnyi.beautify.data.api.type** { *; }

# Crashlytics
-keepattributes SourceFile,LineNumberTable

## Architecture
-keep, allowobfuscation class com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity { *; }
-keep, allowobfuscation class * extends com.opasichnyi.beautify.view.impl.entity.RootDataUIEntity {
    public protected <methods>;
}
