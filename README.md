Lưu ý trước khi chạy cập nhật **đúng tham số clientId, clientSecret** đã được cấp qua email

Xem hướng dẫn tại đây: 

[**Android**](https://demorms.vnptit.vn/help/docs/sdks/sdk_lite/steps/android/#b%C6%B0%E1%BB%9Bc-2-kh%E1%BB%9Fi-t%E1%BA%A1o-sdk-t%E1%BA%A1i-n%C6%A1i-mu%E1%BB%91n-b%E1%BA%AFt-%C4%91%E1%BA%A7u-k%E1%BA%BFt-n%E1%BB%91i)

[**iOS**](https://demorms.vnptit.vn/help/docs/sdks/sdk_lite/steps/ios#b%C6%B0%E1%BB%9Bc-2-kh%E1%BB%9Fi-t%E1%BA%A1o-sdk-t%E1%BA%A1i-n%C6%A1i-b%E1%BA%AFt-%C4%91%E1%BA%A7u-k%E1%BA%BFt-n%E1%BB%91i)


React-Native example project use native (android/iOS) SmartCA SDK

- React-Native: 0.73.0
- Android Studio Hedgehog 2023.1.1, Java 17
- Xcode 26.2

# Hướng dẫn chạy example

> **Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

### For iOS

```bash
# using npm
npm run ios

# OR using Yarn
yarn ios
```

If everything is set up _correctly_, you should see your new app running in your _Android Emulator_ or _iOS Simulator_ shortly provided you have set up your emulator/simulator correctly.

This is one way to run your app — you can also run it directly from within Android Studio and Xcode respectively.

# Hướng dẫn tích hợp SmartCA SDK [Tại đây](./guide.md)
