# Tích hợp React Native

- Yêu cầu iOS >= 13.0
- Yêu cầu Android: minSdkVersion = 21, compileSdkVersion = 34

## Cài đặt cho Android

### Bước 1: Tải SDK và cấu hình project

- Tải phiên bản SDK mới nhất ở link: https://github.com/VNPT-SmartCA/vnpt_smartca_sdk_android
- Copy tất cả file và thư mục trong `SdkSmartCA` vào `android/app/libs` (nếu chưa có thư mục `libs` thì tạo mới)
- Trong `android/build.gradle`, thêm cấu hình như sau

```groovy
buildscript {
    // ...

    repositories {
        // ...
        jcenter()
        flatDir {
            dirs 'libs'
        }
        maven {
            url 'https://jitpack.io'
        }
    }
    //...
}

String storageUrl = System.env.FLUTTER_STORAGE_BASE_URL ?: "https://storage.googleapis.com"

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url '../app/libs/repo'
        }
        maven {
            url "$storageUrl/download.flutter.io"
        }
        maven { url "https://jitpack.io" }
    }
}

// ...
```

- Trong `android/gradle.properties`, enable `newArchEnabled` cho project

```groovy
newArchEnabled=true
```

- Trong `android/app/proguard-rules.pro`, thêm cấu hình

```groovy
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontwarn android.support.**
-dontwarn com.squareup.**
-dontwarn com.google.android.**
-verbose

# -keepattributes *Annotation*
-dontwarn lombok.**
-dontwarn io.realm.**

-dontwarn java.awt.**

# okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Application classes that will be serialized/deserialized over Gson
-keep class vnpt.it3.econtract.data.model.** { *; }
-keep class vnpt.it3.econtract.data.** { *; }

# Java 8
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

# Retrofit 2
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


# pdf lib
-keep class com.shockwave.**

#To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** w(...);
    public static *** i(...);
}

-keep public enum vnpt.it3.econtract.data.**{
    *;
}

-keepclassmembers class **.R$* {
  public static <fields>;
}
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class com.vnptit.innovation.sample.model.** { *; }
-keep class ai.icenter.face3d.native_lib.Face3DConfig { *; }
-keep class ai.icenter.face3d.native_lib.CardConfig { *; }
```

- Trong `android/app/build.gradle`, thêm các cấu hình sau

```groovy
android {
    // ...

    defaultConfig {
        // ...
        multiDexEnabled true
        ndk {
            // abiFilters 'armeabi-v7a', 'arm64-v8a','x86_64'
            debugSymbolLevel 'FULL'
        }
    }

	// ...

    packagingOptions {
        pickFirst 'lib/x86/libc++_shared.so'
        pickFirst 'lib/x86_64/libc++_shared.so'
        pickFirst 'lib/armeabi-v7a/libc++_shared.so'
        pickFirst 'lib/arm64-v8a/libc++_shared.so'
    }

    aaptOptions {
        noCompress "bic"
    }
}

dependencies {
    implementation fileTree(dir: "libs", include: ["*.aar"])
    // The version of react-native is set by the React Native Gradle Plugin
   // ...

    implementation 'com.vnpt.smartca.module.vnpt_smartca_module:flutter_release:1.0'
//
    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'

    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.7.0"
    //khanhht SDK eContract
    implementation "androidx.multidex:multidex:2.0.1"
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    implementation 'androidx.test.espresso:espresso-idling-resource:3.5.1'

    implementation files('libs/vnpt_smartca_sdk_lib-release.aar')
    implementation files('libs/ekyc_sdk-release-v3.2.7.aar')
    implementation files('libs/eContract-v3.1.0.aar')
    implementation 'com.squareup.okhttp3:okhttp:4.7.1'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.6.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
    //noinspection GradleCompatible
    implementation 'com.android.support:exifinterface:28.0.0'
    implementation 'com.google.code.gson:gson:2.10'
    implementation 'com.github.code-mc:loadtoast:1.0.12'

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
    implementation 'io.reactivex.rxjava2:rxjava:2.2.12'
}
```

- Trong `android/app/src/main/AndroidManifest.xml` thêm các cấu hình sau

```xml
// Thêm vào thẻ <application>
tools:replace="android:allowBackup"
tools:targetApi="m"

// Thêm setup 2 activity
<activity  android:name="io.flutter.embedding.android.FlutterFragmentActivity"
	android:launchMode="singleTop"
	android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection"
	android:hardwareAccelerated="true"
	android:windowSoftInputMode="adjustResize"
	android:exported="true"/>

<activity  android:name="io.flutter.embedding.android.FlutterActivity"
	android:launchMode="singleTop"
	android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection"
	android:hardwareAccelerated="true"
	android:windowSoftInputMode="adjustResize"
	android:exported="true"/>
```

- Thêm `network_security_config` để chạy debug, tạo file `network_security_config.xml` trong `android/app/src/main/res/xml` với nội dung như sau

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
	<domain-config  cleartextTrafficPermitted="true">
		<domain  includeSubdomains="true">localhost</domain>
		<domain  includeSubdomains="true">10.0.1.1</domain>
		<domain  includeSubdomains="true">10.0.2.2</domain>
		<domain  includeSubdomains="true">10.0.3.2</domain>
	</domain-config>
</network-security-config>
```

### Bước 2: Tạo `Native Modules` (https://reactnative.dev/docs/native-modules-android)

- Tạo file `SmartCAModule.kt` ngang hàng với `MainApplication.kt` như sau

```kotlin
package com.reactnativeapp
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.vnpt.smartca.ConfigSDK
import com.vnpt.smartca.SmartCAEnvironment
import com.vnpt.smartca.SmartCALanguage
import com.vnpt.smartca.SmartCAResultCode
import com.vnpt.smartca.VNPTSmartCASDK
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.vnpt.smartca.CustomParams
import com.vnpt.smartca.EkycService

class SmartCAModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "SmartCAModule"

    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    @ReactMethod
    fun addListener(type: String?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod
    fun removeListeners(type: Int?) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun createAccount() {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.createAccount { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            val obj: CallbackResult = Json.decodeFromString(
                                CallbackResult.serializer(), result.data.toString()
                            )
                            // SDK trả lại token, credential của khách hàng
                            // Đối tác tạo transaction cho khách hàng để lấy transId, sau đó gọi getWaitingTransaction
                            val token = obj.accessToken
                            val credentialId = obj.credentialId

                            val params = Arguments.createMap().apply {
                                putInt("code", 0)
                                putString("token", token)
                                putString("credentialId", credentialId)
                            }

                            sendEvent(this.reactApplicationContext, "EventReminder", params)

//                            callback.invoke(0, token, credentialId)

//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Xác thực thành công")
//                            builder.setMessage("CredentialId: $credentialId;\nAccessToken: $token")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()
                        }

                        else -> {
                            // Xử lý lỗi
//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Thông báo")
//                            builder.setMessage("status: ${result.status}; statusDesc:  ${result.statusDesc}")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()

                            val params = Arguments.createMap().apply {
                                putInt("code", 1)
                                putString("token",  result.status.toString())
                                putString("credentialId", result.statusDesc)
                            }
//
                            sendEvent(this.reactApplicationContext, "EventReminder", params)
//                            callback.invoke(1, result.status.toString(), result.statusDesc)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun getAuth() {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getAuthentication { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            val obj: CallbackResult = Json.decodeFromString(
                                CallbackResult.serializer(), result.data.toString()
                            )
                            // SDK trả lại token, credential của khách hàng
                            // Đối tác tạo transaction cho khách hàng để lấy transId, sau đó gọi getWaitingTransaction
                            val token = obj.accessToken
                            val credentialId = obj.credentialId
                            val serial = obj.serial

                            val params = Arguments.createMap().apply {
                                putInt("code", 0)
                                putString("token", token)
                                putString("credentialId", credentialId)
                                putString("serial", serial)
                            }

                            sendEvent(this.reactApplicationContext, "EventReminder", params)

//                            callback.invoke(0, token, credentialId)

//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Xác thực thành công")
//                            builder.setMessage("CredentialId: $credentialId;\nAccessToken: $token")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()
                        }

                        else -> {
                            // Xử lý lỗi
//                            val builder = AlertDialog.Builder(context)
//                            builder.setTitle("Thông báo")
//                            builder.setMessage("status: ${result.status}; statusDesc:  ${result.statusDesc}")
//                            builder.setPositiveButton(
//                                "Close"
//                            ) { dialog, _ -> dialog.dismiss() }
//                            builder.show()

                            val params = Arguments.createMap().apply {
                                putInt("code", 1)
                                putString("token",  result.status.toString())
                                putString("credentialId", result.statusDesc)
                            }
//
                            sendEvent(this.reactApplicationContext, "EventReminder", params)
//                            callback.invoke(1, result.status.toString(), result.statusDesc)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun getMainInfo() {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getMainInfo { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            // Xử lý khi confirm thành công
                        }

                        else -> {
                            // Xử lý khi confirm thất bại
                        }
                    }
                }
            } catch (ex: java.lang.Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun getWaitingTransaction(transId: String) {
        currentActivity?.runOnUiThread {
            try {
                if (transId.isNullOrEmpty()) {
//                editTextTrans.setError("Vui lòng điền Id giao dịch");
//                    return
                }

                VNPTSmartCA.getWaitingTransaction(transId) { result ->
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Thông báo")
//                builder.setMessage("status: ${result.status}; statusDesc:  ${result.statusDesc}")
//                builder.setPositiveButton(
//                    "Close"
//                ) { dialog, _ -> dialog.dismiss() }
//                builder.show()

                    val params = Arguments.createMap().apply {
                        putInt("code", 0)
                        putString("token", result.status.toString())
                        putString("credentialId", result.statusDesc)
                    }

                    sendEvent(this.reactApplicationContext, "EventReminder", params)

                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            // Xử lý khi thành công
                        }

                        else -> {
                            // Xử lý khi thất bại
                        }
                    }

                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    private fun signOut() {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.signOut { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            val params = Arguments.createMap().apply {
                                putInt("code", 0)
                                putString("token", "Thông báo")
                                putString("credentialId", "Đăng xuất thành công")
                            }

                            sendEvent(this.reactApplicationContext, "EventReminder", params)
                        }

                        else -> {
                            val params = Arguments.createMap().apply {
                                putInt("code", 1)
                                putString("token",  result.status.toString())
                                putString("credentialId", result.statusDesc)
                            }
//
                            sendEvent(this.reactApplicationContext, "EventReminder", params)
//                            callback.invoke(1, result.status.toString(), result.statusDesc)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex;
            }
        }
    }


    companion object {

        private var VNPTSmartCA = VNPTSmartCASDK()

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context

            var customParams = CustomParams(
                customerId = "", // Số CCCD, giấy tờ của KH
                customerPhone = "", // Số ĐT của KH
                borderRadiusBtn = 99.0, // Border của nút
                colorSecondBtn = "#DEF7EB", // Màu nền nút phụ
                colorPrimaryBtn = "#33CC80", // Màu nền nút chính
                logoCustom = "", // base64 ảnh logo dạng 			"iVBORw0KGgoAAAANSUhEUgAAANgAAA......"
                backgroundLogin = "", // base64 ảnh nền login dạng 			"iVBORw0KGgoAAAANSUhEUgAAANgAAA......",
                packageDefault = "PS0", // Chỉ hiển thị gói cước PS0
            )

            val config = ConfigSDK(
                env = SmartCAEnvironment.DEMO_ENV, // Môi trường kết nối DEMO/PROD
                clientId = "", // clientId tương ứng với môi trường được cấp qua email
                clientSecret = "", // clientSecret tương ứng với môi trường được cấp qua email
                lang = SmartCALanguage.VI,
                isFlutter = false,
                customParams = customParams,
            )


//            config.context = context
//            config.partnerId = "VNPTSmartCAPartner-add1fb94-9629-49`47-b7d8-f2671b04c747"
//            config.environment = SmartCAEnvironment.DEMO_ENV
//            config.lang = SmartCALanguage.VI
//            config.isFlutter = false
            VNPTSmartCA.initSDK(context, config)
            VNPTSmartCA.initEkycService(EkycService(vnptSmartCA = VNPTSmartCA))
            val x = mutableListOf<Int>()
            x.add(Intent.FLAG_ACTIVITY_NEW_TASK)
            VNPTSmartCA.initCustomIntentFlag(x)
//            VNPTSmartCA.ekycService = EkycService(vnptSmartCA = VNPTSmartCA)
        }

        fun onDestroy() {
//        super.onDestroy()
            VNPTSmartCA.destroySDK();
        }
    }

}

@Serializable
data class CallbackResult(
    val credentialId: String,
    val accessToken: String,
    val serial: String,
) : java.io.Serializable
```

- Tạo file `MyAppPackage.kt` ngang hàng với `MainApplication.kt`

```kotlin
import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager

class MyAppPackage : ReactPackage {
    override fun createViewManagers(
        reactContext: ReactApplicationContext
    ): MutableList<ViewManager<View, ReactShadowNode<*>>> = mutableListOf()

    override fun createNativeModules(
        reactContext: ReactApplicationContext
    ): MutableList<NativeModule> = listOf(SmartCAModule(reactContext)).toMutableList()
}
```

- Kết nối `MyAppPackage` vào function `getPackages` trong `MainApplication`

```kotlin
override fun getPackages(): List<ReactPackage> =
	PackageList(this).packages.apply {
	// Packages that cannot be autolinked yet can be added manually here, for example:
	// packages.add(new MyReactNativePackage());
	add(MyAppPackage())
}
```

- Trong `MainActivity` thêm hai function sau

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	SmartCAModule.init(this)
}

override fun onDestroy() {
	super.onDestroy()
	SmartCAModule.onDestroy()
}
```

## Cài đặt cho iOS

### Bước 1: Tải SDK và cấu hình Project

- Tải phiên bản SDK mới nhất từ link: https://github.com/VNPT-SmartCA/ios_vnptsmartca_sdk
- Kéo thả toàn bộ file _.xcframework và _.framework vào trong project. Đi tới Targets Project -> General -> Frameworks, Libraries, and Embedded Content

- Ngoại trừ 2 thư viện FlutterPluginRegistrant.xcframework và permission_handler_apple.xcframework cấu hình Do not Embed, tất cả các thư viện còn lại cấu hình Embed & Sign

- Nếu project chưa cấu hình quyền sử dụng camera(NSCameraUsageDescription) hãy bổ sung cấu hình quyền sử dụng camera trong Info.plist

### Bước 2: Tạo `Native Modules` (https://reactnative.dev/docs/native-modules-ios)

- Sử dụng `xcode` để tạo và mở project ios (\*.xcworkspace)
- Tạo file swift với tên là `SmartCAModule.swift`, lúc này xcode sẽ gợi ý tạo file để `configure an Objective-C Bridging Header`, chọn `Create Bridging Header`

```swift
//
//  SmartCAModule.swift
//

import Foundation
import SmartCASDK
import FlutterPluginRegistrant

@objc(SmartCAModule)
class SmartCAModule: RCTEventEmitter {
  
  private var manager: SmartCAManager?
  private var hasListeners = false
  
  override init() {
      super.init()
      self.manager = SmartCAManager.shared
  }
  
  private var count = 0
  
//  private var hasListeners = false
//
//  var vnptSmartCASDK: VNPTSmartCASDK?
  
  
  override func startObserving() {
    hasListeners = true
  }
  
  override func stopObserving() {
    hasListeners = false
  }
  
  @objc
    func increment() {
      count += 1
      print("count is \(count)")
      sendEvent(withName: "onIncrement", body: ["count": count])
    }
  
//    @objc
//  func initSDK(vc: UIViewController) {
//    if let ab = RCTPresentedViewController() {
//      self.vnptSmartCASDK = VNPTSmartCASDK(
//        viewController: ab,
//        partnerId: "CLIENT_ID",
//        environment: VNPTSmartCASDK.ENVIRONMENT.DEMO,
//        lang: VNPTSmartCASDK.LANG.VI,
//        isFlutterApp: false)
//    }
//
//  }
  
  @objc func createAccount() {
    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.createAccount(callback: { result in
          print(result)
      })
    }
  }
  
  @objc func signOut() {
    
    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.signOut(callback: { result in
                if result.status == SmartCAResultCode.SUCCESS_CODE {
                    // Xử lý khi thành công
  //                Counter.sharedInstance().sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])

                  if self.hasListeners {
                    self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": "Thông báo", "credentialId": "Đăng xuất thành công"])
                      }
                  
                  
                } else {
                    // Xử lý khi thất bại
                }
            });
    }

  }
  
  
  @objc func getAuth() {
    DispatchQueue.main.async {
      // SDK tự động xử lý các trường hợp về token: Hết hạn, chưa kích hoạt...
    self.manager?.vnptSmartCASDK?.getAuthentication(callback: { result in
              if result.status == SmartCAResultCode.SUCCESS_CODE {
                  // Xử lý khi thành công
//                Counter.sharedInstance().sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])

                if self.hasListeners {
                  self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])
                    }
                
                
              } else {
                  // Xử lý khi thất bại
              }
          });
    }
  }
  
  @objc func  getMainInfo(){
    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.getMainInfo(callback: { result in
        
      })
    }
  }
  
  @objc func getWaitingTransaction(_ tranId: String) {
//      self.tranId = "xxxx"; // tạo giao dịch từ backend, lấy tranId từ hệ thống VNPT SmartCA trả về

    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.getWaitingTransaction(tranId: tranId, callback: { result in
            if result.status == SmartCAResultCode.SUCCESS_CODE {
                print("Giao dịch thành công: \(result.status) - \(result.statusDesc) - \(result.data)");
              
              if self.hasListeners {
                self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.status, "credentialId": result.statusDesc])
                  }
              
              
            } else {
              if self.hasListeners {
                self.sendEvent(withName: "EventReminder", body: ["code": 1, "token": result.status, "credentialId": result.statusDesc])
                  }
            }
        });
    }
  }

    // we need to override this method and
    // return an array of event names that we can listen to
    override func supportedEvents() -> [String]! {
      return ["onIncrement", "EventReminder"]
    }
  

  @objc
  override func constantsToExport() -> [AnyHashable : Any]! {
      return ["initialCount": 0]
  }
  
  override static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
  
}

class SmartCAManager: NSObject {
  static let shared = SmartCAManager()
  
  @objc class func getInstance() -> SmartCAManager {
    return SmartCAManager.shared
  }
  
  var vnptSmartCASDK: VNPTSmartCASDK?
  
  @objc
  func initSDK() {
    if let ab = RCTPresentedViewController() {
      
      let customParams = CustomParams(
          customerId: "",
          borderRadiusBtn: 99,
          colorSecondBtn: "#DEF7EB",
          colorPrimaryBtn: "#33CC80",
          featuresLink: "",
          customerPhone: "",
          packageDefault: "",
	  password: "",
          logoCustom: "",
          backgroundLogin: ""
      )
      let config = SDKConfig(clientId: "4185-637127995547330633.apps.signserviceapi.com", clientSecret: "NGNhMzdmOGE-OGM2Mi00MTg0", environment: ENVIRONMENT.DEMO, lang: LANG.VI, customParams: customParams);
      
      self.vnptSmartCASDK = VNPTSmartCASDK(
        viewController: ab,
        config: config)
      
      GeneratedPluginRegistrant.register(with: self.vnptSmartCASDK?.flutterEngine as! FlutterPluginRegistry);
    }
  }
  
  @objc
  func destroySDK() {
    self.vnptSmartCASDK?.destroySDK();
  }
}

```

- Cấu hình file `*-Bridging-Header.h`

```swift
//

//  Use this file to import your target's public headers that you would like to expose to Swift.

//

#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"
#import <React/RCTUtils.h>
```

- Tạo file `SmartCAModule.m`

```swift
// RCTSmartCAModule.m
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"

@interface RCT_EXTERN_MODULE(SmartCAModule, RCTEventEmitter)
RCT_EXTERN_METHOD(increment)
RCT_EXTERN_METHOD(createAccount)
RCT_EXTERN_METHOD(getAuth)
RCT_EXTERN_METHOD(getMainInfo)
RCT_EXTERN_METHOD(getWaitingTransaction: String)
RCT_EXTERN_METHOD(signOut)
@end
```

## Sử dụng trong React Native

- Import native module

```js
import {NativeModules} from 'react-native';
```

- Khởi tạo

```js
const {SmartCAModule} = NativeModules;
```

- Viết hàm lắng nghe sự kiện `EventReminder` (có thể tạo nhiều sự kiện)

```js
useEffect(() => {
	const  eventEmitter  =  new  NativeEventEmitter(SmartCAModule)
	let  eventListener  =  eventEmitter.addListener('EventReminder', event  => {
		let  code  =  event.code
		let  statusCode  =  event.statusCode
		let  statusDesc  =  event.statusDesc
		let  data = event.data

		// todo

	// other event if need
	let  testIOSEvent  =  eventEmitter.addListener('onIncrement', event  => {
		console.log('onIncrement event', event);
	});

	// Removes the listener once unmounted

	return () => {
		eventListener.remove();
		testIOSEvent.remove();
	};
}, []);
```

- Sử dụng các hàm chính

```js
// create account
SmartCAModule.createAccount()

// get auth
SmartCAModule.getAuth()

// get main info
SmartCAModule.getMainInfo()

// get waiting transaction
// Lấy accessToken từ `SmartCAModule.getAuth()`

let transactionId = 'xxx'
SmartCAModule.getWaitingTransaction(transactionId)

// sign out
SmartCAModule.signOut()
```
