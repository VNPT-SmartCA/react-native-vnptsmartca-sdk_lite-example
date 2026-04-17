#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import <ReactNativeApp-Swift.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  self.moduleName = @"ReactNativeApp";
  // You can add your custom initial props in the dictionary below.
  // They will be passed down to the ViewController used by React Native.
  self.initialProps = @{};
  
  BOOL value = [super application:application didFinishLaunchingWithOptions:launchOptions];
  
//  UIViewController *vc = [UIApplication sharedApplication].delegate.window.rootViewController;
  
  
  
  [[SmartCAManager getInstance] initSDK];
  
//  self.returnSwiftClassInstance.initSDK;

  return value;
}

//- (Counter *)returnSwiftClassInstance
//{
//  return [[Counter alloc] init];
//}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
  return [self getBundleURL];
}

- (NSURL *)getBundleURL
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

@end
