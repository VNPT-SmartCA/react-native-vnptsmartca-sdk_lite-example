package com.reactnativeapp

import android.os.Bundle
import android.os.PersistableBundle
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate


class MainActivity : ReactActivity() {



  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "ReactNativeApp"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate {
    return DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    SmartCAModule.init(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    SmartCAModule.onDestroy()
  }
}
