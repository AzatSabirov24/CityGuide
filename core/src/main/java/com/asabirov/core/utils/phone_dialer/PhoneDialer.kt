package com.asabirov.core.utils.phone_dialer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf

class PhoneDialer(private val context: Context) {

    operator fun invoke(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse(
            "tel:${phoneNumber}"
        )
        ContextCompat.startActivity(context, intent, bundleOf())
    }
}