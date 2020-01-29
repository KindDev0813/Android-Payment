package com.intuz.stripepaymentmodule


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.intuz.stripehelper.StripeHelper
import com.intuz.stripehelper.StripePaymentListener
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), StripePaymentListener {
    override fun onStripPaymentInFail(errorMessage: String) {
        Log.e("Error", errorMessage)
    }

    override fun onStripPaymentInSuccess(token: Token) {
        Log.e("TOEKN", token.id)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //InitPayment
        payment.setOnClickListener {
            val stripe = StripeHelper.StripeHelperBuilder(this@MainActivity, StripeConstant.PUBLISHABLE_KEY, this)
                .setLabel("Card detail")
                .setSaveButtonLabel("Done") // Default label Save
                .changeSaveBackGroundColor(ContextCompat.getColor(
                    this@MainActivity,R.color.colorPrimary )) // Default save button bg color background_material_dark
                .changeSaveTextColor(ContextCompat.getColor(
                    this@MainActivity,R.color.buttonText ))    // Default save text color #AAAAAA
                .setCancelButtonLabel("Dismiss") // Default label cancel
                .changeCancelBackGroundColor(ContextCompat.getColor(
                    this@MainActivity,R.color.colorPrimary )) // Default Cancel button bg color background_material_dark
                .changeCancelTextColor(ContextCompat.getColor(
                    this@MainActivity,R.color.buttonText ))    // Default Cancel text color #AAAAAA
                .buildNewStripeHelper()
            stripe.showCardDialog()
        }
    }

}
