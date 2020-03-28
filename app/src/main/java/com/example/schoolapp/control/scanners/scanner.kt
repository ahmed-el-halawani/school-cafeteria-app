package com.example.schoolapp.control.scanners

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.schoolapp.R
import kotlinx.android.synthetic.main.activity_scanner.*
import me.dm7.barcodescanner.zbar.BarcodeFormat
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class scanner : AppCompatActivity(), ZBarScannerView.ResultHandler {

    private lateinit var mScannerView: ZBarScannerView
    private var chick = 0
    private lateinit var result:String
    private var testNum = 0
    private var flash = false
    lateinit var ScannerFormatList:ArrayList<BarcodeFormat>
    lateinit var state:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        mScannerView = ZBarScannerView(this)
        this.setContentView(R.layout.activity_scanner)

        val b = intent.extras!!
        state = b.getString("state")!!
        when (state){
            "qrCode"->{
                ScannerFormatList =arrayListOf(
                    BarcodeFormat.QRCODE
                )
            }

            "barCode" ->{
                ScannerFormatList = arrayListOf(
                    BarcodeFormat.EAN13,
                    BarcodeFormat.EAN8,
                    BarcodeFormat.UPCA,
                    BarcodeFormat.UPCE
                )
            }
        }

    }

    override fun onStart() {
        super.onStart()
        cameraFrame.addView(mScannerView)
        btn_flash.setOnClickListener {
            flash = !flash
            mScannerView.flash = flash
        }


    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
        mScannerView.flash = flash
        mScannerView.setFormats(ScannerFormatList)
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        testNum++
        when(state) {
            "qrCode" -> {
                mScannerView.stopCamera()
                val backIntent = Intent()
                backIntent.putExtra("result", rawResult!!.contents)
                setResult(Activity.RESULT_OK, backIntent)
                finish()
            }

            "barCode" -> {
                when (chick) {
                    0 -> {
                        val handler = Handler()
                        handler.postDelayed({
                            result = rawResult!!.contents
                            mScannerView.resumeCameraPreview(this)
                            chick++
                        }, 150)
                    }
                    1 -> {
                        if (result == rawResult!!.contents) {
                            mScannerView.stopCamera()
                            val backIntent = Intent()
                            backIntent.putExtra("result", rawResult.contents)
                            backIntent.putExtra("testNum", testNum.toString())
                            setResult(Activity.RESULT_OK, backIntent)
                            finish()
                        } else {
                            chick = 1
                            result = rawResult.contents
                            mScannerView.resumeCameraPreview(this)
                        }
                    }
                }
            }
        }
    }

}
