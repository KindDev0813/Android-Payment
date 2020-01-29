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

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class StripeHelper(stripeBuilder: StripeHelperBuilder) {

    val context: AppCompatActivity
    val stripePublicKey: String
    val mListener: StripePaymentListener

    val label: String
    val labelColor: Int? = null

    val saveButtonLabel: String
    val saveTextColor: Int


    var saveBackGroundColorRes: Int

    val cancelButtonLabel: String
    val cancelTextColor: Int

    val cancelBackGroundColor: Int


    init {
        this.context = stripeBuilder.context
        this.stripePublicKey = stripeBuilder.stripePublicKey
        this.mListener = stripeBuilder.listener
        this.label = stripeBuilder.label
        this.saveButtonLabel = stripeBuilder.saveButtonLabel
        this.cancelButtonLabel = stripeBuilder.cancelButtonLabel
        this.cancelTextColor = stripeBuilder.cancelTextColor
        this.cancelBackGroundColor = stripeBuilder.cancelBackGroundColor
        this.saveTextColor = stripeBuilder.saveTextColor
        this.saveBackGroundColorRes = stripeBuilder.saveBackGroundColorRes

    }

    var cardDialog: StripeCardDialog? = null
    fun showCardDialog() {
        cardDialog = StripeCardDialog.newInstance(
            stripePublicKey, label, saveButtonLabel, cancelButtonLabel
            , cancelTextColor, saveTextColor, cancelBackGroundColor, saveBackGroundColorRes
        )
        cardDialog?.show(context.supportFragmentManager, "Custom Bottom Sheet")
        cardDialog?.isCancelable = false
        mListener.let { cardDialog?.registerCallback(it) }
    }

    class StripeHelperBuilder(
        val context: AppCompatActivity,
        val stripePublicKey: String,
        val listener: StripePaymentListener
    ) {

        var label: String = context.getString(R.string.addPayment)
        var saveButtonLabel: String = context.getString(R.string.save)
        var cancelButtonLabel: String = context.getString(R.string.cancel)

        var titleColor: Int? = null

        var saveTextColor: Int = ContextCompat.getColor(context, R.color.editText)
        var cancelTextColor: Int = ContextCompat.getColor(context, R.color.editText)

        var cancelBackGroundColor: Int = ContextCompat.getColor(context, R.color.background_material_dark)
        var saveBackGroundColorRes: Int = ContextCompat.getColor(context, R.color.background_material_dark)

        fun setLabel(label: String): StripeHelperBuilder {
            this.label = label
            return this
        }

        fun setSaveButtonLabel(saveButtonLabel: String): StripeHelperBuilder {
            this.saveButtonLabel = saveButtonLabel
            return this
        }

        fun setCancelButtonLabel(cancelButtonLabel: String): StripeHelperBuilder {
            this.cancelButtonLabel = cancelButtonLabel
            return this
        }

        fun changeCancelTextColor(color: Int): StripeHelperBuilder {
            this.cancelTextColor = color
            return this
        }

        fun changeSaveTextColor(color: Int): StripeHelperBuilder {
            this.saveTextColor = color
            return this
        }

        fun changeCancelBackGroundColor(color: Int): StripeHelperBuilder {
            this.cancelBackGroundColor = color
            return this
        }


        fun changeSaveBackGroundColor(color: Int): StripeHelperBuilder {
            this.saveBackGroundColorRes = color
            return this
        }


        private//Do some basic validations to check
        val isValidateStripeData: Boolean
            get() = true


        fun buildNewStripeHelper(): StripeHelper {
            //isValidateEmployeeData
            return StripeHelper(this)
        }
    }


}