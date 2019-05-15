#import "FlutterNfcPlugin.h"
#import <flutter_nfc/flutter_nfc-Swift.h>

@implementation FlutterNfcPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterNfcPlugin registerWithRegistrar:registrar];
}
@end
