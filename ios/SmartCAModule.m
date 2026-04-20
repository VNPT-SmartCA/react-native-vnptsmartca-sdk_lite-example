#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(SmartCAModule, RCTEventEmitter)
RCT_EXTERN_METHOD(getAuth:(NSString *)customerId)
RCT_EXTERN_METHOD(getWaitingTransaction:(NSString *)tranId)
@end
