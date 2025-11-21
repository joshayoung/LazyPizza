package com.joshayoung.lazypizza.core.presentation

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.app.domain.models.AppWriteState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class FirebaseAuthUiClient {
    val firebaseAuth = FirebaseAuth.getInstance()

    val appWriteState: Flow<AppWriteState> =
        callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener {
                    val user = firebaseAuth.currentUser
                    trySend(AppWriteState(user != null, user?.uid))
                }
            firebaseAuth.addAuthStateListener(listener)

            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    // TODO: The fallback to 'guest' here needs to be more robust:
    val currentUser: String
        get() =
            firebaseAuth.currentUser?.uid
                ?: BuildConfig.GUEST_USER

    suspend fun verifyPhoneNumber(
        activity: Activity,
        phoneNumber: String
    ): String =
        suspendCancellableCoroutine {
            val options =
                PhoneAuthOptions
                    .newBuilder()
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(
                        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                it.resume(credential.smsCode ?: "Completed Automatically")
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                it.cancel(e)
                            }

                            override fun onCodeSent(
                                verificationId: String,
                                token: PhoneAuthProvider
                                    .ForceResendingToken
                            ) {
                                it.resume(verificationId)
                            }
                        }
                    ).build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

    suspend fun sendCode(
        verificationId: String?,
        smsCode: String
    ): Boolean =
        suspendCancellableCoroutine {
            if (smsCode == "" || verificationId == null) {
                return@suspendCancellableCoroutine
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)
            firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.resume(true)
                    } else {
                        it.resume(false)
                    }
                }
        }
}