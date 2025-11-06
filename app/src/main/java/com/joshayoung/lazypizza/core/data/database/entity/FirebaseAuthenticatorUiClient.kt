package com.joshayoung.lazypizza.core.data.database.entity

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Stable
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@Stable
class FirebaseAuthenticatorUiClient(
    private val callback: FirebaseAuthenticatorUiClientCallback
) {
    private val auth = FirebaseAuth.getInstance()
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    fun startPhoneVerification(
        phoneNumber: String,
        activity: Activity
    ) {
        val options =
            PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            // This callback will be invoked in two situations:
                            // 1 - Instant verification. In some cases the phone number can be instantly
                            //     verified without needing to send or enter a verification code.
                            // 2 - Auto-retrieval. On some devices Google Play services can automatically
                            //     detect the incoming verification SMS and perform verification without
                            //     user action.
                            Log.d(TAG, "onVerificationCompleted:$credential")
                            auth
                                .signInWithCredential(credential)
                                .addOnCompleteListener(activity) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        callback.onVerificationCompleted(credential)

                                        val user = task.result?.user
                                    } else {
                                        // Sign in failed, display a message and update the UI
                                        Log.w(
                                            TAG,
                                            "signInWithCredential:failure",
                                            task.exception
                                        )
                                        if (task.exception is
                                                FirebaseAuthInvalidCredentialsException
                                        ) {
                                            // The verification code entered was invalid
                                            throw Exception("test")
                                        }
                                        // Update UI
                                    }
                                }
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            callback.onVerificationFailed(e.message ?: "Verification Failed")
                            // This callback is invoked in an invalid request for verification is made,
                            // for instance if the the phone number format is not valid.
                            Log.w(TAG, "onVerificationFailed", e)

                            if (e is FirebaseAuthInvalidCredentialsException) {
                                Log.w(TAG, "invalid", e)
                                // Invalid request
                            } else if (e is FirebaseTooManyRequestsException) {
                                Log.w(TAG, "exceptoin", e)
                                // The SMS quota for the project has been exceeded
                            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                                Log.w(TAG, "missing", e)
                                // reCAPTCHA verification attempted with null Activity
                            }

                            // Show a message and update the UI
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            callback.onCodeSent(verificationId)
                            // The SMS verification code has been sent to the provided phone number, we
                            // now need to ask the user to enter the code and then construct a credential
                            // by combining the code with a verification ID.
                            Log.d(TAG, "onCodeSent:$verificationId")

                            resendToken = token
                        }
                    }
                ).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode(
        code: String,
        verificationId: String
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        auth
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                task.result?.user
            }
        callback.onVerificationCompleted(credential)
    }
}
