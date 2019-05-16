import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_nfc/flutter_nfc.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _greetings = 'Bad bad';
  String _nfcData = "no data";
  StreamSubscription _dataSubscription = null;


  @override
  void initState() {
    super.initState();
    initPlatformState();
    initGreetings();
    readNFC();
    // readNFC();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterNfc.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }


  Future<void> initGreetings() async {
    String greeting;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      greeting = await FlutterNfc.getGreetings;
    } on PlatformException {
      greeting = 'Failed to get greetings.'; 
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _greetings = greeting;
    });
  }

  Future<void> startNFC() async{
    try{
      await FlutterNfc.startNFC;
    }catch(e){
      print(e);
    }
  }

  Future<void> stopNFC() async{
    try{
      await FlutterNfc.stopNFC;
    }catch(e){
      print(e);
    }
  }

  Future<void> readNFC() async {
    try{
    _dataSubscription =  FlutterNfc.read.listen((response) {
      setState(() {
        this._nfcData = response.toString();
      });
    });
    }catch(e){
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              Text(_greetings),
              Text(_nfcData,style: TextStyle(fontSize: 25.0),),
              IconButton(
                icon: Icon(Icons.nfc),
                onPressed: this.startNFC
              ),
              IconButton(
                icon: Icon(Icons.stop),
                onPressed: this.stopNFC,
              )
            ],
          ),
        ),
      ),
    );
  }
}
