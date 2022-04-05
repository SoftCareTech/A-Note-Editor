package com.softcare.raphnote

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_BIOMETRIC_ENROLL
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executor

class StartActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
  private lateinit var promptInfo: BiometricPrompt.PromptInfo
  val allowedAuthenticators:Int =BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)

                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                                    }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

            })

        promptInfo =
            BiometricPrompt.PromptInfo.Builder()
                .apply {
                    setTitle(getString(R.string.prompt_info_title))
                        setSubtitle(getString(R.string.prompt_info_subtitle))
                        setDescription(getString(R.string.prompt_info_description))
                        setConfirmationRequired(false)
                    setAllowedAuthenticators(allowedAuthenticators )
                    if(allowedAuthenticators and DEVICE_CREDENTIAL==0)
                    setNegativeButtonText(getString(R.string.prompt_info_use_app_password)) //don't use it together with setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)

                }
            .build()
        val s: SharedPreferences = this.getSharedPreferences(
            "RaphNote",
            MODE_PRIVATE
        )
        if(s.getBoolean("first",true)){
            startActivity(Intent(applicationContext, AppGuide::class.java))
            finish()
        }
       if( s.getInt("lockAfter",0)!=3) {
           when (BiometricManager.from(this).canAuthenticate(allowedAuthenticators)) {
               BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
               BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> notEnrol()
               else -> {
                   disableLock()
                   Toast.makeText(
                       applicationContext,
                       "The phone have no security",
                       Toast.LENGTH_SHORT
                   ).show()
               }
           }
       }
  else{
           startActivity(Intent(applicationContext, MainActivity::class.java))
           Toast.makeText(
               applicationContext,
               "App Security disabled. You can enable it in the App Settings",
               Toast.LENGTH_SHORT).show()
           finish()

       }

    }

    private fun notEnrol() {
        val title = getString(R.string.bio_en_title)
        val message = getString(R.string.bio_en_msg)
        val button1String = getString(R.string.yes)
        val button2String = getString(R.string.no)
        val ad: AlertDialog.Builder = AlertDialog.Builder(this)
        ad.setTitle(title)
        ad.setMessage(message)
        ad.setPositiveButton(
            button1String,
            { dialog, arg1 -> launchSettings() })
        ad.setNegativeButton(
            button2String
        ) { dialog, arg1 ->  disableLock() }
        ad.show()

    }
private  fun  disableLock(){
    val s: SharedPreferences = this.getSharedPreferences(
        "RaphNote",
        MODE_PRIVATE
    )
    val editor = s.edit()
    editor.putInt("lockAfter", 3)  // 3 is never
    editor.apply()
    editor.commit()
    startActivity(Intent(applicationContext, MainActivity::class.java))
    finish()
}

    private  fun launchSettings(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val enrollIntent =
            Intent(ACTION_BIOMETRIC_ENROLL).apply {
                putExtra( Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }
            var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
            }

            resultLauncher.launch(enrollIntent)
        } else {
            val title = getString(R.string.settings_error)
            val message = getString(R.string.settings_error_msg)
            val ad: AlertDialog.Builder = AlertDialog.Builder(this)
            ad.setTitle(title)
            ad.setMessage(message)
            ad.show()
        }

    }
  fun open(v: View){
    when (BiometricManager.from(this).canAuthenticate(allowedAuthenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt.authenticate(promptInfo)
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> notEnrol()
        else -> {
            disableLock()
            Toast.makeText(
                applicationContext,
                getString(R.string.no_security_msg),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

}
