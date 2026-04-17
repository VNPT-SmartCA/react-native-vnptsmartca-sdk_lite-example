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
    private fun createAccount(orderId: String) {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.createAccount(orderId) { result ->
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
    private fun getAuth(customerId: String) {
        currentActivity?.runOnUiThread {
            try {
                VNPTSmartCA.getAuthentication(customerId) { result ->
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
                borderRadiusBtn = 99.0, // Border của nút
                colorSecondBtn = "#DEF7EB", // Màu nền nút phụ
                colorPrimaryBtn = "#33CC80", // Màu nền nút chính
                logoCustom = "", // base64 ảnh logo dạng 			"iVBORw0KGgoAAAANSUhEUgAAANgAAA......"
                backgroundLogin = "", // base64 ảnh nền login dạng 			"iVBORw0KGgoAAAANSUhEUgAAANgAAA......",
//                packageDefault = "PS0", // Chỉ hiển thị gói cước PS0
//                password = ""
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
            // super.onDestroy()
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
