package com.mobbile.paul.mttms.ui.auth


import androidx.lifecycle.ViewModelProvider
import com.mobbile.paul.mttms.BaseActivity
import com.mobbile.paul.mttms.R
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.mobbile.paul.mttms.util.Utils.Companion.isInternetAvailable
import androidx.lifecycle.Observer
import com.mobbile.paul.mttms.models.AuthBiData
import com.mobbile.paul.mttms.ui.modules.Modules
import com.mobbile.paul.mttms.util.Util.appDate
import com.mobbile.paul.mttms.util.Utils.Companion.USER_INFOS

class Userauth : BaseActivity() {

    @Inject
    internal lateinit var modelFactory: ViewModelProvider.Factory

    lateinit var vmodel: AuthViewModel

    private var preferences: SharedPreferences? = null

    var todayDates: String? = null


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vmodel = ViewModelProviders.of(this, modelFactory)[AuthViewModel::class.java]
        preferences = getSharedPreferences(USER_INFOS, Context.MODE_PRIVATE)
        todayDates = appDate()
        showProgressBar(false)

        btn_login.setOnClickListener {
            if (!isInternetAvailable(this)) {
                Toast.makeText(
                    applicationContext,
                    "No Internet Connection, Thanks!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showProgressBar(true)
                dataProcess()
            }
        }
        vmodel.authObservable().observe(this, authObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataProcess() {
        val permit = checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        val username: String = et_email.text.toString()
        val password: String = et_password.text.toString()
        val tel = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val validateDates = preferences!!.getString("today_date_preferences", "")

        if (permit == PackageManager.PERMISSION_GRANTED) {
            vmodel.userAuth("muhammad.i@mt3.com", "1417", "356750108496043", validateDates!!)
            /*vmodel.userAuth(
                username,
                password,
                tel.getImei(0),
                validateDates!!
            )*/
        } else {
            makeRequest()
        }
    }

    private val authObserver = Observer<AuthBiData> {
        showProgressBar(false)
        when (it.status) {
            200 -> {
                saveUserInfoInSharePref(it)
            }
            else -> {
                Toast.makeText(applicationContext, it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            DEVICE_STATE_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        "imei permission deny, please allow",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        showProgressBar(false)
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            DEVICE_STATE_PERMISSION
        )
    }

    private fun saveUserInfoInSharePref(auths: AuthBiData) {
        if(auths.setpref==1) {
            Log.d(TAG, "${auths.employeeid}")
            preferences!!.edit().apply()
            val editor = preferences!!.edit()
            editor.clear()
            editor.putString("today_date_preferences", todayDates)
            editor.putInt("employee_id_user_preferences", auths.employeeid)
            editor.putInt("depot_id_user_preferences", auths.depots_id)
            editor.putInt("region_id_user_preferences", auths.region_id)
            editor.apply()
            callModuleIntent()
        }else{
            callModuleIntent()
        }
    }

    private fun callModuleIntent() {
        val intent = Intent(this, Modules::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = "Userauth"
        const val DEVICE_STATE_PERMISSION = 101
    }
}
