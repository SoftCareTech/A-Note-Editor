package com.softcare.raphnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class StartActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
  private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,  object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,  errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                   // Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT)   .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity( Intent(applicationContext,MainActivity::class.java))
                   // Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT)  .show()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                   // Toast.makeText(applicationContext, "Authentication failed",  Toast.LENGTH_SHORT)   .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
            setTitle("getString(R.string.prompt_info_title)")
            setSubtitle("getString(R.string.prompt_info_subtitle)")
             setDescription("getString(R.string.prompt_info_description)")
            setConfirmationRequired(false)
           // setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            setNegativeButtonText("getString(R.string.prompt_info_use_app_password)") //don't use it together with setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        }.build()
            biometricPrompt.authenticate(promptInfo)
           // startActivity( Intent(applicationContext,MainActivity::class.java))
    }
    /*
      fun onCreate2(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        private lateinit var executor: Executor
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: BiometricPrompt.PromptInfo

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            // Prompt appears when user clicks "Log in".
            // Consider integrating with the keystore to unlock cryptographic operations,
            // if needed by your app.
            val biometricLoginButton =
                findViewById<Button>(R.id.biometric_login)
            biometricLoginButton.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        }
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            // Can't call setNegativeButtonText() and
            // setAllowedAuthenticators(... or DEVICE_CREDENTIAL) at the same time.
            // .setNegativeButtonText("Use account password")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 0)
            }
        }
    }
*/



    // Since we are using the same methods in more than one Activity, better give them their own file.

    object BiometricPromptUtils {
        private const val TAG = "BiometricPromptUtils"
        fun createBiometricPrompt(
            activity: AppCompatActivity,
            processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit
        ): BiometricPrompt {
            val executor = ContextCompat.getMainExecutor(activity)

            val callback = object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errCode, errString)
                    Log.d(TAG, "errCode is $errCode and errString is: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d(TAG, "User biometric rejected. ")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Authentication was successful")
                    processSuccess(result)
                }
            }
            return BiometricPrompt(activity, executor, callback)
        }

        fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo =
            BiometricPrompt.PromptInfo.Builder().apply {
                setTitle(activity.getString(R.string.prompt_info_title))
                setSubtitle(activity.getString(R.string.prompt_info_subtitle))
                setDescription(activity.getString(R.string.prompt_info_description))
                setConfirmationRequired(false)
                setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_password))
            }.build()
    }
    }
