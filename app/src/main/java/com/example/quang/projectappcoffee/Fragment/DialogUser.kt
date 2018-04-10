package com.example.quang.projectappcoffee.Fragment


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quang.projectappcoffee.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_dialog.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class DialogUser : DialogFragment() {

    companion object {

        var mVerificationInProgress = false
        const val TAG = "DialogUser"
        const val STATE_INITIALIZED = 1
        const val STATE_CODE_SENT = 2
        const val STATE_VERIFY_FAILED = 3
        const val STATE_VERIFY_SUCCESS = 4
        const val STATE_SIGNIN_FAILED = 5
        const val STATE_SIGNIN_SUCCESS = 6
    }

    lateinit var firebaseDB: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    private lateinit var callBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var mVerificationId: String
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.user_dialog, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
        firebaseDB = FirebaseDatabase.getInstance().getReference("Member")
        img_Close.setOnClickListener {
            this.dismiss()
        }
        btn_Login.setOnClickListener {
            if (!validatePhone()) {
                return@setOnClickListener
            }
            else {
                progressbar.visibility = View.VISIBLE
                startPhoneNumberVerification(edt_PhoneNumber.text.toString())
            }
        }

        mAuth = FirebaseAuth.getInstance()

        callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:" + credential)

                mVerificationInProgress = false
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)

                mVerificationInProgress = false


                if (e is FirebaseAuthInvalidCredentialsException) {

                    edt_VerifyCode.error = "Invalid phone number."

                } else if (e is FirebaseTooManyRequestsException) {
                    Snackbar.make(activity.findViewById<View>(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show()

                }

                updateUI(STATE_VERIFY_FAILED)

            }

            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {

                Log.d(TAG, "onCodeSent:" + verificationId!!)


                mVerificationId = verificationId
                mResendToken = token!!


                updateUI(STATE_CODE_SENT)

            }
        }

        btn_Verify.setOnClickListener {
            if (TextUtils.isEmpty(edt_VerifyCode.text.toString())) {
                edt_VerifyCode.error = " Cannot be empty."
                return@setOnClickListener
            } else {
                verifyPhoneNumberWithCode(mVerificationId, edt_VerifyCode.text.toString())

            }
        }

        btn_Resend.setOnClickListener {
            resendVerificationCode(edt_PhoneNumber.text.toString(), mResendToken)

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { p0 ->
                    if (p0.isSuccessful) {
                        Log.d(TAG, "signInWithCredential:success")

                        val user = p0.result.user
                        updateUI(STATE_SIGNIN_SUCCESS, user)

                    } else {
                        Log.w(TAG, "signInWithCredential:failure", p0.exception)
                        if (p0.exception is FirebaseAuthInvalidCredentialsException) {
                            edt_VerifyCode.error = "Invalid code."
                        }

                        updateUI(STATE_SIGNIN_FAILED)

                    }
                }
    }


    private fun updateUI(uiState: Int) {
        updateUI(uiState, mAuth.currentUser, null)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user)
        } else {
            updateUI(STATE_INITIALIZED)
        }
    }

    private fun updateUI(uiState: Int, user: FirebaseUser) {
        updateUI(uiState, user, null)
    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential) {
        updateUI(uiState, null, cred)
    }

    private fun updateUI(uiState: Int, user: FirebaseUser?, cred: PhoneAuthCredential?) {
        when (uiState) {
            STATE_INITIALIZED -> {
                enableViews(btn_Login, edt_PhoneNumber)
                disableViews(btn_Verify, btn_Resend, edt_VerifyCode)
            }
            STATE_CODE_SENT -> {
                enableViews(btn_Verify, btn_Resend, edt_PhoneNumber, edt_VerifyCode)
                disableViews(btn_Login)
            }
            STATE_VERIFY_FAILED -> {
                enableViews(btn_Login, btn_Verify, btn_Resend, edt_PhoneNumber, edt_VerifyCode)
                progressbar.visibility = View.INVISIBLE
            }
            STATE_VERIFY_SUCCESS -> {
                disableViews(btn_Login, btn_Verify, btn_Resend, edt_PhoneNumber, edt_VerifyCode)
                progressbar.visibility = View.VISIBLE
                if (cred != null) {
                    if (cred.smsCode != null) {
                        edt_VerifyCode.setText(cred.smsCode)

                    } else {
                        Log.d(TAG, "STATE_VERIFY_SUCCESS: " + "no sms")
                        edt_VerifyCode.setText(R.string.instant_validation)
                        edt_VerifyCode.setTextColor(Color.parseColor("#4bacb8"))
                    }
                }

            }
            STATE_SIGNIN_FAILED -> {
                progressbar.visibility = View.INVISIBLE
            }
            STATE_SIGNIN_SUCCESS -> {

            }
        }

        if (user == null) {
            //signed out
        } else {
            this.dismiss()
        }
    }



    private fun startPhoneNumberVerification(phone: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                activity,
                callBack
        )
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        signInWithPhoneAuthCredential(credential)
    }

    private fun resendVerificationCode(phoneNumber: String,
                                       token: PhoneAuthProvider.ForceResendingToken) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                activity,
                callBack,
                token)
    }

    private fun validatePhone(): Boolean {
        if (!Pattern.matches("^[+]?[0-9]{10,13}\$",edt_PhoneNumber.text.toString())){
            Toast.makeText(activity,"Phone numbers are numbers or accents",Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(edt_PhoneNumber.text.toString())) {
            edt_PhoneNumber.error = "Please enter phone number!!"
            return false
        }
        return true
    }

    private fun enableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = true
        }
    }

    private fun disableViews(vararg views: View) {
        for (v in views) {
            v.isEnabled = false
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
        if (mVerificationInProgress && validatePhone()) {
            startPhoneNumberVerification(edt_PhoneNumber.text.toString())
        }
    }


}

