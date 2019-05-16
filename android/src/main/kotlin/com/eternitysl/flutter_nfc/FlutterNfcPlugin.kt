package com.eternitysl.flutter_nfc

import android.nfc.NfcAdapter
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


const val MIME_TEXT_PLAIN = "text/plain"

class FlutterNfcPlugin(val registrar: Registrar) : MethodCallHandler, EventChannel.StreamHandler {


    private val activity = registrar.activity()


    private var nfcAdapter: NfcAdapter? = null
    private var eventSink: EventChannel.EventSink? = null

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private var isNfcSupported: Boolean = false


    companion object {

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val messenger = registrar.messenger()
            val channel = MethodChannel(messenger, "flutter_nfc")
            val eventChannel = EventChannel(messenger, "com.eternitysl.flutter_nfc_reader")
            val plugin = FlutterNfcPlugin(registrar)
            channel.setMethodCallHandler(plugin)
            eventChannel.setStreamHandler(plugin)
        }
    }

    init {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)?.let { it }
        isNfcSupported = this.nfcAdapter != null
//        enableForegroundDispatch(activity, this.nfcAdapter)
        eventSink?.success("app started")

    }


    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "getGreetings") {
            result.success("Hello from platform")
        } else if (call.method == "startNFC") {
//            this.onResume()
            eventSink?.success("start nfc tried")
            result.success("success")
        } else if (call.method == "stopNFC") {
//            this.onPause()
            eventSink?.success("start nfc stoped")
            result.success("success")
        } else if (call.method == "readNFC") {
            this.eventSink?.success("started to read")

        } else {
            result.notImplemented()

        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        this.eventSink = events

    }

    override fun onCancel(p0: Any?) {
        eventSink = null
//        this.onPause()
    }


}






