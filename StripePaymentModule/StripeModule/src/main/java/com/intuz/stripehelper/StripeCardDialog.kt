package com.intuz.stripehelper

//  The MIT License (MIT)

//  Copyright (c) 2020 Intuz Pvt Ltd.

//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:

//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.stripe_card_dialog.*


class StripeCardDialog : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(
            stripePublicKey: String, title: String, saveLabel: String, cancelLabel: String,
            cancelTextColor: Int, saveTextColor: Int, bgColorCancel: Int, bgColorSaveRes: Int
        ): StripeCardDialog {
            val cardDialog = StripeCardDialog()
            val bundle = Bundle()
            bundle.putString("STRIPPUBLICKEY", stripePublicKey)
            bundle.putString("TITLE", title)
            bundle.putString("SAVE_LABEL", saveLabel)
            bundle.putString("CANCEL_LABEL", cancelLabel)

            bundle.putInt("TEXT_COLOR_CANCEL", cancelTextColor)
            bundle.putInt("TEXT_COLOR_SAVE", saveTextColor)

            bundle.putInt("BG_COLOR_CANCEL", bgColorCancel)

            bundle.putInt("BG_COLOR_SAVE_RES", bgColorSaveRes)

            cardDialog.arguments = bundle
            return cardDialog
        }
    }

    private var stripePublicKey: String = ""

    private var title: String? = ""

    private var saveLabel: String? = ""
    private var cancelLabel: String? = ""

    var mListener: StripePaymentListener? = null

    private var btnCancel: AppCompatButton? = null
    private var btnSave: AppCompatButton? = null
    private var txtHeader: AppCompatTextView? = null


    private var cancelTextColor: Int? = null
    private var saveTextColor: Int? = null
    private var bgColorCancel: Int? = null
    private var bgColorSaveRes: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.stripe_card_dialog, container, false)

        if (arguments != null) {
            stripePublicKey = arguments!!.getString("STRIPPUBLICKEY")
            title = arguments?.getString("TITLE","Add payment details")

            saveLabel = arguments?.getString("SAVE_LABEL", "SAVE")
            cancelLabel = arguments?.getString("CANCEL_LABEL", "CANCEL")

            cancelTextColor = arguments?.getInt("TEXT_COLOR_CANCEL")
            saveTextColor = arguments?.getInt("TEXT_COLOR_SAVE")

            bgColorCancel = arguments?.getInt("BG_COLOR_CANCEL")
            bgColorSaveRes = arguments?.getInt("BG_COLOR_SAVE_RES")
        }
        KeyboardUtil(activity, view)
        btnSave = view.findViewById(R.id.save_payment) as AppCompatButton
        btnCancel = view.findViewById(R.id.cancel_payment) as AppCompatButton
        txtHeader = view.findViewById(R.id.payment_form_title) as AppCompatTextView

        // setLabel
        //  payment_form_title.text = title
        btnSave?.text = saveLabel
        btnCancel?.text = cancelLabel
        txtHeader?.text = title


        //Set Color if pass
        bgColorSaveRes?.let { btnSave?.setBackgroundColor(it) }
        bgColorCancel?.let { btnCancel?.setBackgroundColor(it) }

        saveTextColor?.let { btnSave?.setTextColor(it) }
        cancelTextColor?.let { btnCancel?.setTextColor(it) }


        btnSave?.setOnClickListener {
            // DO SOMETHING
            KeyboardUtil.hideKeyboard(activity)
            var card = card_multiline_widget?.card
            if (card != null) {
                progress_circular.visibility = View.VISIBLE
                createToken(card)
            }
        }

        btnCancel?.setOnClickListener {
            // DO SOMETHING
            KeyboardUtil.hideKeyboard(activity)
            this@StripeCardDialog.dialog?.hide()
        }

        return view
    }

    private fun createToken(card: Card) {
        val strip = Stripe(this.activity!!, stripePublicKey)
        strip.createToken(card, object : TokenCallback {
            override fun onSuccess(token: Token) {
                progress_circular?.visibility = View.GONE
                mListener?.onStripPaymentInSuccess(token)
                this@StripeCardDialog.dialog?.hide()
            }

            override fun onError(error: Exception) {
                progress_circular?.visibility = View.GONE
                mListener?.onStripPaymentInFail(error.localizedMessage)
                this@StripeCardDialog.dialog?.hide()
            }
        })


    }

    fun registerCallback(@NonNull mListener: StripePaymentListener) {
        this.mListener = mListener
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }
}