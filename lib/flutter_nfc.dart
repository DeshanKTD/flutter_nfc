import 'dart:async';

import 'package:flutter/services.dart';

class FlutterNfc {
  static const MethodChannel _channel =
      const MethodChannel('flutter_nfc');

  static const stream =
      const EventChannel('com.eternitysl.flutter_nfc_reader');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static Future<String> get getGreetings async {
    final String greeting = await _channel.invokeMethod('getGreetings');
    return greeting;
  }

  static Future<String> get startNFC async {
    final String result = await _channel.invokeMethod('startNFC');
    print("started nfc");
    return result;
  }

  static Future<String> get stopNFC async {
    final String result = await _channel.invokeMethod('stopNFC');
    print(stopNFC);
    return result;
  }

  static Stream<dynamic> get read {
    return stream.receiveBroadcastStream();
  }
}
