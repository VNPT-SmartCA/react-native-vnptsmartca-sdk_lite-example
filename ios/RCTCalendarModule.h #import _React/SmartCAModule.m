//
//  RCTBridgeModule.h> @interface RCTSmartCAModule : NSObject <RCTBridgeModule> RCTSmartCAModule.m
//  ReactNativeApp
//
//  Created by TuanNT on 14/12/2023.
//

// RCTSmartCAModule.m
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"

@interface RCT_EXTERN_MODULE(SmartCAModule, RCTEventEmitter)
//@interface RCT_EXTERN_REMAP_MODULE(SmartCAModule, SmartCAModule, RCTEventEmitter)
RCT_EXTERN_METHOD(increment)
RCT_EXTERN_METHOD(createAccount)
RCT_EXTERN_METHOD(getAuth:(NSString *)customerId)
RCT_EXTERN_METHOD(getMainInfo)
//RCT_EXTERN_METHOD(getWaitingTransaction: String: String)
RCT_EXTERN_METHOD(getWaitingTransaction: String)
RCT_EXTERN_METHOD(signOut)
@end
