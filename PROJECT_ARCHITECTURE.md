# Tong hop thong tin va kien truc du an

## 1. Tong quan

- Ten app: `ReactNativeApp`
- Loai du an: ung dung React Native dong vai tro demo/example cho viec tich hop `VNPT SmartCA SDK` bang native Android/iOS.
- Muc tieu chinh: cung cap mot lop giao dien JS rat mong de goi cac ham native cua SmartCA, nhan event callback tu native va hien thi ket qua tren man hinh.
- Ban chat kien truc: day khong phai app business day du, ma la project mau de tich hop SDK ky so/xac thuc SmartCA.

## 2. Cong nghe va phu thuoc chinh

### JavaScript / React Native

- React `18.2.0`
- React Native `0.73.0`
- TypeScript `5.0.4`
- Hermes duoc bat tren Android: `android/gradle.properties`
- New Architecture duoc bat tren Android: `newArchEnabled=true`
- Thu vien UI bo sung: `react-native-dialog`

### Android

- Kotlin + React Native native module
- `minSdkVersion = 21`
- `compileSdkVersion = 34`
- `targetSdkVersion = 33`
- Java/Kotlin toolchain theo huong dan README: Java 17
- Tich hop nhieu file `.aar` trong `android/app/libs`

### iOS

- iOS native bridge bang Swift + Objective-C bridge export
- Podfile theo cau truc RN mac dinh
- README yeu cau Xcode `15.2`
- `guide.md` yeu cau iOS `>= 13.0`
- Tich hop san cac framework/xcframework trong `ios/SDK_iOS`

## 3. Cau truc thu muc chinh

```text
.
|-- App.tsx
|-- index.js
|-- package.json
|-- README.md
|-- guide.md
|-- PROJECT_ARCHITECTURE.md
|-- __tests__/
|-- android/
|   |-- build.gradle
|   |-- gradle.properties
|   |-- settings.gradle
|   `-- app/
|       |-- build.gradle
|       |-- libs/
|       `-- src/main/
|           |-- AndroidManifest.xml
|           `-- java/com/reactnativeapp/
|               |-- MainActivity.kt
|               |-- MainApplication.kt
|               |-- MyAppPackage.kt
|               `-- SmartCAModule.kt
`-- ios/
    |-- Podfile
    |-- SmartCAModule.swift
    |-- RCTCalendarModule.h
    |-- ReactNativeApp-Bridging-Header.h
    |-- ReactNativeApp/
    `-- SDK_iOS/
```

## 4. Kien truc tong the

Kien truc du an gom 3 lop:

1. Lop JS/UI React Native
2. Lop bridge native Android/iOS
3. Lop SDK SmartCA va SDK phu tro native duoc dong goi san

So do logic:

```text
App.tsx
  -> NativeModules.SmartCAModule
    -> Android: SmartCAModule.kt
    -> iOS: SmartCAModule.swift
      -> VNPTSmartCASDK / SmartCA native SDK
        -> callback ket qua
          -> native event "EventReminder"
            -> NativeEventEmitter trong App.tsx
              -> Alert + hien thi text tren UI
```

Nhan xet quan trong:

- UI React Native chi la man demo test SDK, khong co kien truc feature/module phuc tap.
- Toan bo nghiep vu thuc te nam trong native module va SDK dong goi san.
- Project hien tai khong co navigation, state management library, network layer JS, service layer JS, hay local data layer o phia React Native.

## 5. Entry points va khoi dong ung dung

### JS entry

- `index.js`: dang ky component goc qua `AppRegistry.registerComponent`.
- `App.tsx`: man hinh demo chinh va cung la UI duy nhat.

### Android startup

- `MainApplication.kt`
  - dang ky custom package `MyAppPackage()` de expose native module cho RN.
  - bat New Architecture neu build config cho phep.
- `MainActivity.kt`
  - `onCreate()` goi `SmartCAModule.init(this)` de khoi tao SDK.
  - `onDestroy()` goi `SmartCAModule.onDestroy()` de giai phong SDK.

### iOS startup

- `ios/ReactNativeApp/AppDelegate.m`
  - sau khi app khoi dong, goi `[[SmartCAManager getInstance] initSDK]`.
- `ios/SmartCAModule.swift`
  - `SmartCAManager` la singleton quan ly instance `VNPTSmartCASDK`.

## 6. Lop giao dien React Native

File trung tam: `App.tsx`

Chuc nang hien tai:

- Tao cac nut thao tac:
  - `Create Account`
  - `Get Auth`
  - `Get Main`
  - `Get Waiting Transaction`
  - `SignOut`
- Co `TextInput` de nhap `Transaction ID`
- Lang nghe `NativeEventEmitter(SmartCAModule)` voi event:
  - `EventReminder`
  - `onIncrement` (chi dung de test iOS bridge)
- Khi nhan event, app:
  - doc `code`, `token`, `credentialId`
  - tao chuoi message
  - cap nhat state `text`, `code`, `visible`
  - hien `Alert`

Nhan xet:

- `App.tsx` van giu nhieu ma scaffold mac dinh cua React Native template (`Section`, import `NewAppScreen`, comment cu), nhung phan nay khong con dong vai tro thuc te.
- `react-native-dialog` da duoc import, state `visible` cung duoc cap nhat, nhung phan dialog hien tai dang bi comment out. Nghia la app dang dung `Alert` la chinh.

## 7. Android native module

### 7.1. Dang ky module

- `MyAppPackage.kt` tao module `SmartCAModule` va expose cho React Native.

### 7.2. Cac API expose sang JS

Trong `SmartCAModule.kt`, module expose cac ham:

- `createAccount()`
- `getAuth()`
- `getMainInfo()`
- `getWaitingTransaction(transId: String)`
- `signOut()`

Ngoai ra co:

- `addListener()`
- `removeListeners()`

Hai ham nay la bat buoc de tuong thich voi `NativeEventEmitter` cua React Native.

### 7.3. Co che callback/event

- Module dung `DeviceEventManagerModule.RCTDeviceEventEmitter` de ban event `EventReminder` len JS.
- Payload event thuong co dang:

```json
{
  "code": 0 | 1,
  "token": "...",
  "credentialId": "...",
  "serial": "..." // chi co o getAuth thanh cong
}
```

Quy uoc:

- `code = 0`: thanh cong
- `code = 1`: loi

### 7.4. Khoi tao SDK

`SmartCAModule.init(context)` thuc hien:

- Tao `CustomParams` de tuy bien UI/du lieu dau vao cho SDK
- Tao `ConfigSDK` voi:
  - `env = DEMO_ENV`
  - `clientId = ""`
  - `clientSecret = ""`
  - `lang = VI`
  - `isFlutter = false`
- Goi `VNPTSmartCA.initSDK(context, config)`
- Khoi tao them `EkycService`
- Cau hinh custom intent flags cho SDK

Luu y:

- `clientId`, `clientSecret`, `customerId`, `customerPhone` dang de rong. Day la project mau, chua san sang cho production neu khong bo sung cau hinh thuc te.
- Environment dang hard-code la `DEMO_ENV`.

## 8. iOS native module

### 8.1. Cach bridge sang React Native

Bo bridge iOS gom 3 thanh phan:

- `SmartCAModule.swift`: code Swift chinh
- `ios/  RCTCalendarModule.h #import _React/SmartCAModule.m`: file Objective-C bridge export `RCT_EXTERN_MODULE`
- `ReactNativeApp-Bridging-Header.h`: expose header React cho Swift

Ten file bridge export hien tai rat bat thuong va de gay nham lan. Ve ban chat, day dang dong vai tro file `SmartCAModule.m` de export cac method Swift sang RN.

### 8.2. Cac API expose sang JS

- `increment()`
- `createAccount()`
- `getAuth()`
- `getMainInfo()`
- `getWaitingTransaction(_ tranId: String)`
- `signOut()`

### 8.3. Event

`supportedEvents()` tra ve:

- `onIncrement`
- `EventReminder`

Module chi gui event khi `hasListeners = true`.

### 8.4. Quan ly SDK

`SmartCAManager` la singleton chiu trach nhiem:

- `initSDK()`
- `destroySDK()`
- giu instance `vnptSmartCASDK`

Khoi tao SDK trong iOS:

- Tao `CustomParams`
- Tao `SDKConfig(clientId: "", clientSecret: "", environment: .DEMO, lang: .VI, customParams: ...)`
- Tao `VNPTSmartCASDK(viewController: ab, config: config)`
- Dang ky plugin Flutter vao `flutterEngine` cua SDK qua `GeneratedPluginRegistrant.register`

Nhan xet:

- iOS cho thay ro SmartCA SDK co noi bo su dung Flutter engine, du du an host la React Native.
- Viec dang ky `FlutterPluginRegistrant` la chi dau ro rang rang SmartCA SDK tren iOS phu thuoc vao lop Flutter embedded.

## 9. Phu thuoc native va SDK dong goi san

### Android

Trong `android/app/libs` dang co cac binary quan trong:

- `vnpt_smartca_sdk_lib-release-v1.0.aar`
- `ekyc_sdk-release-v3.2.7.aar`
- `eContract-v3.6.12.aar`
- `load-toast.aar`
- repo maven local trong `android/app/libs/repo`

Ngoai ra `build.gradle` con khai bao:

- `com.vnpt.smartca.module.vnpt_smartca_module:flutter_release:1.0`
- Retrofit / OkHttp / Gson / RxJava2
- AndroidX appcompat, material, constraintlayout, multidex

### iOS

Trong `ios/SDK_iOS` co san nhieu framework/xcframework:

- `SmartCASDK.framework`
- nhom `eContract-eKYC/*`
- nhom `Flutter/*`
- `SmartCASDK` va cac framework phu tro eKYC/eContract

Dieu nay xac nhan du an dang embed san SDK da build truoc, khong lay tu package manager chuan cho phan nghiep vu SmartCA.

## 10. Luong nghiep vu chinh

### 10.1. Tao tai khoan

1. User bam `Create Account`
2. JS goi `SmartCAModule.createAccount()`
3. Native module goi SDK `createAccount`
4. SDK tra callback
5. Native module gui event `EventReminder`
6. JS nhan event va hien thong bao

Ket qua thanh cong tren Android duoc parse tu `result.data` JSON de lay:

- `accessToken`
- `credentialId`

### 10.2. Lay xac thuc

1. User bam `Get Auth`
2. JS goi `getAuth()`
3. Native SDK tu xu ly cac trang thai token
4. Native module gui event ve JS

Android parse them `serial` tu payload thanh cong.

### 10.3. Lay thong tin chinh

1. User bam `Get Main`
2. JS goi `getMainInfo()`
3. Native SDK xu ly
4. Hien tai app demo chua dua ket qua nay len UI ro rang

Day la mot diem cho thay man demo chua hoan thien dong nhat giua cac API.

### 10.4. Ky giao dich cho giao dich cho xu ly

1. User nhap `Transaction ID`
2. User bam `Get Waiting Transaction`
3. JS goi `getWaitingTransaction(transId)`
4. Native SDK xu ly giao dich dang cho
5. Native gui event ket qua ve JS

Theo comment trong Android:

- phia doi tac/back-end can tao transaction truoc de lay `transId`
- sau do app moi goi `getWaitingTransaction`

### 10.5. Dang xuat

1. User bam `SignOut`
2. JS goi `signOut()`
3. Native SDK dang xuat
4. Native gui event thong bao thanh cong/that bai

## 11. Cau hinh build va runtime dang chu y

### Android

- `usesCleartextTraffic=true`
- co `network_security_config.xml` cho localhost/debug
- khai bao camera, biometric, storage permissions
- them 2 Flutter activities:
  - `io.flutter.embedding.android.FlutterFragmentActivity`
  - `io.flutter.embedding.android.FlutterActivity`
- `multiDexEnabled true`
- `packagingOptions` dung `pickFirst` cho `libc++_shared.so`
- `aaptOptions.noCompress "bic"`

### iOS

- Podfile gan nhu mac dinh cua RN 0.73
- auto pod installation duoc bat trong `react-native.config.js`
- co Swift bridge va Objective-C bridge de expose native module

## 12. Kien truc ma nguon hien tai

### Diem manh

- Tich hop native bridge da hoat dong cho ca Android va iOS.
- SDK va binary native duoc dong goi san trong repo, giup demo/deploy nhanh.
- Luong giao tiep RN <-> native ro rang, don gian, de debug.

### Gioi han

- UI chi co 1 man hinh monolithic `App.tsx`.
- Khong co tach lop `screens`, `components`, `services`, `hooks`, `types`.
- Khong co navigation.
- Khong co state management ngoai `useState` cuc bo.
- Khong co error handling nhat quan giua Android/iOS.
- Mot so API co trien khai khong dong deu giua 2 nen tang:
  - `createAccount()` tren iOS hien tai chi `print(result)`, chua gui `EventReminder` nhu Android.
  - `getMainInfo()` ca Android/iOS chua show ket qua len JS mot cach ro rang.
- Con nhieu file/template/comment mac dinh va file ten bat thuong trong `ios/`.

## 13. Test va chat luong ma nguon

- Co 1 test mac dinh: `__tests__/App.test.tsx`
- Test chi verify render `App`, chua cover cac bridge native hoac business flow SmartCA.
- ESLint/Prettier/Jest/Babel/Metro deu gan nhu de mac dinh.

## 14. File cau hinh khong nam trong luong chinh

- `app-config.json`
  - noi dung giong cau hinh cho mini app/web container
  - khong thay duoc su dung trong React Native app hien tai
  - kha nang la file du thua hoac phuc vu mot he thong khac

## 15. Ket luan kien truc

Du an nay la mot example React Native de tich hop `VNPT SmartCA SDK` theo huong:

- React Native chi lam lop giao dien va trigger API
- Android/iOS native module dong vai tro adapter
- SmartCA SDK native/Flutter embedded dong vai tro nghiep vu chinh

Neu xem du an theo goc do kien truc he thong, co the tom gon nhu sau:

1. `App.tsx` la demo console UI.
2. `SmartCAModule` la integration boundary giua JS va native.
3. `VNPTSmartCASDK` va cac `.aar` / `.framework` moi la noi chua logic nghiep vu thuc te.

## 16. De xuat neu muon phat trien tiep

1. Tach `App.tsx` thanh `screens`, `components`, `native`, `types`.
2. Dinh nghia TypeScript types cho event payload tu native.
3. Chuan hoa contract event giua Android va iOS de cung payload, cung hanh vi.
4. Day ket qua `getMainInfo()` ve JS mot cach day du.
5. Bo sung env/config management cho `clientId`, `clientSecret`, `environment`.
6. Don dep file bridge iOS ten bat thuong va comment/template khong con dung.
7. Them test cho native contract va UI handling.
