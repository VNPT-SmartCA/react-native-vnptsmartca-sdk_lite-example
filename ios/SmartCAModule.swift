//
//  Counter.swift
//  ReactNativeApp
//
//  Created by TuanNT on 14/12/2023.
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
    
  
    
//    self.manager?.vnptSmartCASDK?.createAccount(callback: { result in
//              if result.status == SmartCAResultCode.SUCCESS_CODE {
//                  // Xử lý khi thành công
////                Counter.sharedInstance().sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])
//
//                if self.hasListeners {
//                  self.sendEvent(withName: "EventReminder", body: ["code": 0, "token": result.data, "credentialId": ""])
//                    }
//                
//                
//              } else {
//                  // Xử lý khi thất bại
//                if self.hasListeners {
//                  self.sendEvent(withName: "EventReminder", body: ["code": 1, "token": result.status, "credentialId": result.statusDesc])
//                    }
//              }
//          });
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
  
  
  @objc func getAuth(_ customerId: String) {
    DispatchQueue.main.async {
      // SDK tự động xử lý các trường hợp về token: Hết hạn, chưa kích hoạt...
    self.manager?.vnptSmartCASDK?.getAuthentication(customerId: customerId, callback: { result in
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
      let config = SDKConfig(clientId: "", clientSecret: "", environment: ENVIRONMENT.DEMO, lang: LANG.VI, customParams: customParams);
      
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
