package com.app.graham.partner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.trimmedLength
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.InternetConnection
import com.app.graham.partner.utils.ManagePermissions
import com.app.graham.partner.utils.WebConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_mobile_login_screen.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MobileLoginScreen : AppCompatActivity(), View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    var address: String = ""
    var city: String = ""
    var state: String = ""
    var zip: String = ""
    var country: String = ""
    var latitude: String = ""
    var longitude: String = ""
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions
    var sharedPreference: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_login_screen)
        getSupportActionBar()?.hide();

        fusedLocationClient = getFusedLocationProviderClient(this)
        sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        val list = listOf<String>(
                // Manifest.permission.CAMERA,
                // Manifest.permission.READ_EXTERNAL_STORAGE,
                //  Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginbtn -> {
                var contectnumber = logincontact.text.toString().trim()
                if (logincontact.text.toString().trim().isEmpty() && contectnumber.length != 10) {
                    logincontact.error = "PLease enter valid mobile number"
                } else {
                    if (InternetConnection.checkConnection(this@MobileLoginScreen)) {
                        loginhit_api(contectnumber)
                    } else {
                        errormsgdialog()
                    }

                }

            }
        }
    }
    private fun errormsgdialog() {
        val dialog = Dialog(this@MobileLoginScreen)
        dialog.setContentView(R.layout.nointernetlayout)
        dialog.show()
    }
    private fun loginhit_api(mobnumber: String) {
        progessDialog = CustomProgessDialog(this)
        val requestQueue = Volley.newRequestQueue(this)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("mobile", mobnumber)
            jsonObject.put("lat", "28.234443")
            jsonObject.put("long", "77.324343")

            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.user_login, jsonObject, { response ->
                Log.d("resp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response)


            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    applicationContext,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    applicationContext,
                                    "Connection TimeOut! Please check your internet connection.",
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            jobReq.retryPolicy = DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            requestQueue.add(jobReq)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    /*{"Status":"Success","Message":"OTP has been sent successfully.","otp":"438162","user_id":"6"}*/
    private fun Jsonparser(response: JSONObject?) {
        Log.d("", `response`!!.optString("otp"))
        Toast.makeText(this@MobileLoginScreen, `response`!!.getString("otp"), Toast.LENGTH_LONG).show()
        if (`response`!!.getString("Status").contentEquals("Success")) {
            val intent = Intent(this, OtpScreen::class.java)
            intent.putExtra("user_id", response.optString("user_id"))
            intent.putExtra("mobile", logincontact.text.toString())
            startActivity(intent)
        } else {
            logincontact.error = response.getString("Message");
        }
    }

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                if (lastLocation != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    showMessage((lastLocation)!!.latitude.toString() + ": " + (lastLocation)!!.longitude.toString())
                    Log.d("sdxcxcj", (lastLocation)!!.latitude.toString() + ": " + (lastLocation)!!.longitude)
                    longitude = (lastLocation)!!.longitude.toString()
                    latitude = (lastLocation)!!.latitude.toString()
                    val editor = sharedPreference!!.edit()
                    editor.putString("latitude", latitude)
                    editor.putString("longitude", longitude)
                    editor.apply()
                }
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@MobileLoginScreen, string, Toast.LENGTH_LONG).show()
    }

    private fun showSnackbar(
            mainTextStringId: String, actionStringId: String,
            listener: View.OnClickListener
    ) {
        Toast.makeText(this@MobileLoginScreen, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
                this@MobileLoginScreen,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                    View.OnClickListener {
                        startLocationPermissionRequest()
                    })
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions.processPermissionsResult(requestCode, permissions, grantResults)

                if (isPermissionsGranted) {
                    toast("Permissions granted.")
                } else {
                    toast("Permissions denied.")
                }
                return
            }
        }
    }

    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

}


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
