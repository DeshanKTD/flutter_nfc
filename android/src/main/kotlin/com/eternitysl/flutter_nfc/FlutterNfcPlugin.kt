package com.eternitysl.flutter_nfc

import android.content.Intent
import android.content.IntentFilter
import android.app.PendingIntent

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.PluginRegistry.NewIntentListener
import android.support.v7.app.AppCompatActivity


import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter

const val MIME_TEXT_PLAIN = "text/plain"

class FlutterNfcPlugin(val registrar: Registrar): MethodCallHandler,NewIntentListener {

  private val activity = registrar?.activity()

  private var tvIncomingMessage:String?=null
  private var nfcAdapter:NfcAdapter?=null

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private val isNfcSupported: Boolean = this.nfcAdapter != null

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_nfc")
      val plugin = FlutterNfcPlugin(registrar)
      channel.setMethodCallHandler(plugin)
    }
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } 
    else if(call.method == "getGreetings"){
      result.success("Hello from platform")
    }
    else if(call.method == "startNFC"){
        this.onResume()
        result.success("success")
    }
    else if(call.method == "stopNFC"){
        this.onPause()
        result.success("success")
    }
    else {
      result.notImplemented()

    }
  }


   override fun onNewIntent(intent: Intent) :Boolean{
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        receiveMessageFromDevice(intent)
        return true
    }

     private fun receiveMessageFromDevice(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this[0] as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)

                print(inMessage)
                // tvIncomingMessage?.text = inMessage
            }
        }
    }

    

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()
        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType(MIME_TEXT_PLAIN)
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }


    private fun disableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {
        adapter?.disableForegroundDispatch(activity)
    }

    private fun onResume() {

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(activity, this.nfcAdapter)
        // receiveMessageFromDevice()
    }

    private fun onPause() {
        disableForegroundDispatch(activity, this.nfcAdapter)
    }

}
