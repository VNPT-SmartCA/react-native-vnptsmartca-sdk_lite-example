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

  override func startObserving() {
    hasListeners = true
  }

  override func stopObserving() {
    hasListeners = false
  }

  @objc func getAuth(_ customerId: String) {
    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.getAuthentication(customerId: customerId, callback: { result in
        guard self.hasListeners else {
          return
        }

        if result.status == SmartCAResultCode.SUCCESS_CODE {
          self.sendEvent(
            withName: "EventReminder",
            body: [
              "code": 0,
              "token": String(describing: result.data),
              "credentialId": ""
            ]
          )
        } else {
          self.sendEvent(
            withName: "EventReminder",
            body: [
              "code": 1,
              "token": String(result.status),
              "credentialId": result.statusDesc
            ]
          )
        }
      })
    }
  }

  @objc func getWaitingTransaction(_ tranId: String) {
    DispatchQueue.main.async {
      self.manager?.vnptSmartCASDK?.getWaitingTransaction(tranId: tranId, callback: { result in
        guard self.hasListeners else {
          return
        }

        self.sendEvent(
          withName: "EventReminder",
          body: [
            "code": result.status == SmartCAResultCode.SUCCESS_CODE ? 0 : 1,
            "statusCode": String(result.status),
            "statusDesc": result.statusDesc,
            "data": String(describing: result.data)
          ]
        )
      })
    }
  }

  override func supportedEvents() -> [String]! {
    return ["EventReminder"]
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
    guard let viewController = RCTPresentedViewController() else {
      return
    }

    let customParams = CustomParams(
      borderRadiusBtn: 0,
      colorSecondBtn: "",
      colorPrimaryBtn: "",
      featuresLink: "",
      logoCustom: "",
      backgroundLogin: ""
    )
    let config = SDKConfig(
      clientId: "",
      clientSecret: "",
      environment: ENVIRONMENT.DEMO,
      lang: LANG.VI,
      isFlutterApp: false,
      customParams: customParams
    )

    self.vnptSmartCASDK = VNPTSmartCASDK(
      viewController: viewController,
      config: config
    )

    if let flutterEngine = self.vnptSmartCASDK?.flutterEngine {
      GeneratedPluginRegistrant.register(with: flutterEngine)
    }
  }

  @objc
  func destroySDK() {
    self.vnptSmartCASDK?.destroySDK()
  }
}
