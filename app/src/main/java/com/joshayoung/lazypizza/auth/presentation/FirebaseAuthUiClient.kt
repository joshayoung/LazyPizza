package com.joshayoung.lazypizza.auth.presentation

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

class FirebaseAuthUiClient {
    val firebaseAuth = FirebaseAuth.getInstance()

    fun verifyPhoneNumber(
        activity: Activity,
        phoneNumber: String
    ): Flow<String> =
        callbackFlow {
            val options =
                PhoneAuthOptions
                    .newBuilder()
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(
                        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                trySend(credential.smsCode ?: "Completed Automatically")
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                close(e)
                            }

                            override fun onCodeSent(
                                verificationId: String,
                                token: PhoneAuthProvider
                                    .ForceResendingToken
                            ) {
                                trySend(verificationId)
                            }
                        }
                    ).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            awaitClose {}
        }

    fun sendCode(
        verificationId: String?,
        smsCode: String
    ): Flow<Boolean> =
        callbackFlow {
            if (verificationId == null) {
                return@callbackFlow
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
            firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(true).isSuccess
                    } else {
                        trySend(false).isSuccess
                    }
                }
            awaitClose {}
        }
}