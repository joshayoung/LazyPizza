package com.joshayoung.lazypizza.core.data.database.entity

import com.google.firebase.auth.PhoneAuthCredential

interface FirebaseAuthenticatorUiClientCallback {
    fun onVerificationCompleted(credential: PhoneAuthCredential)

    fun onVerificationFailed(message: String)

    fun onCodeSent(verificationId: String)
}
