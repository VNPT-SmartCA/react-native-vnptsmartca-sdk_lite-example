package com.reactnativeapp

import android.annotation.SuppressLint
import android.content.Intent
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.vnpt.smartca.ConfigSDK
import com.vnpt.smartca.CustomParams
import com.vnpt.smartca.SmartCAEnvironment
import com.vnpt.smartca.SmartCALanguage
import com.vnpt.smartca.SmartCAResultCode
import com.vnpt.smartca.VNPTSmartCASDK
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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
    fun getAuth(customerId: String) {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getAuthentication(customerId) { result ->
                    when (result.status) {
                        SmartCAResultCode.SUCCESS_CODE -> {
                            val obj: CallbackResult = Json.decodeFromString(
                                CallbackResult.serializer(), result.data.toString()
                            )
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
                        }

                        else -> {
                            val params = Arguments.createMap().apply {
                                putInt("code", 1)
                                putString("token",  result.status.toString())
                                putString("credentialId", result.statusDesc)
                            }

                            sendEvent(this.reactApplicationContext, "EventReminder", params)
                        }
                    }
                }
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    fun getWaitingTransaction(transId: String) {
        currentActivity?.runOnUiThread {
            try {
                if (transId.isNullOrEmpty()) {
                    return@runOnUiThread
                }

                VNPTSmartCA.getWaitingTransaction(transId) { result ->
                    val params = Arguments.createMap().apply {
                        putInt("code", if (result.status == SmartCAResultCode.SUCCESS_CODE) 0 else 1)
                        putString("statusCode", result.status.toString())
                        putString("statusDesc", result.statusDesc)
                        putString("data", result.data?.toString())
                    }

                    sendEvent(this.reactApplicationContext, "EventReminder", params)
                }
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    companion object {

        private var VNPTSmartCA = VNPTSmartCASDK()

        @SuppressLint("StaticFieldLeak")
        fun init(context: android.content.Context) {

            val customParams = CustomParams(
                borderRadiusBtn = 999.0,
                colorSecondBtn = "",
                colorPrimaryBtn = "",
                featuresLink = "",
                logoCustom = "",
                backgroundLogin = ""
            )

            val config = ConfigSDK(
                clientId = "",
                clientSecret = "",
                env = SmartCAEnvironment.DEMO_ENV,
                customParams = customParams,
                lang = SmartCALanguage.VI,
                isFlutter = false,
            )

            VNPTSmartCA.initSDK(context, config)
            VNPTSmartCA.initCustomIntentFlag(mutableListOf(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        fun onDestroy() {
            VNPTSmartCA.destroySDK()
        }
    }
}

@Serializable
data class CallbackResult(
    val credentialId: String,
    val accessToken: String,
    val serial: String,
) : java.io.Serializable
