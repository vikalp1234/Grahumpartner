package com.app.graham.partner.shop

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Address
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.android.volley.VolleyError
import com.app.graham.partner.R
import com.app.graham.partner.services.Cons
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.services.callback.CallBack
import com.app.graham.partner.toast
import com.app.graham.partner.utils.CameraUtil.checkPermissions
import com.app.graham.partner.utils.ManagePermissions
import com.bumptech.glide.Glide.init
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_shop.*
import kotlinx.android.synthetic.main.content_edit_profile.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AddShopActivity : AppCompatActivity(), View.OnClickListener {
    var addshop_shopname: EditText? = null
    var addshop_address: EditText? = null
    var addshop_city: EditText? = null
    var addshop_state: EditText? = null
    var addshop_email: EditText? = null
    var addshop_mobile: EditText? = null
    var addshop_open: TextView? = null
    var addshop_close: TextView? = null

    private val GALLERY = 1
    private val CAMERA = 2
    var addshopimage: String = ""
    var draw: Drawable? = null
    private var volleyManager: VolleyManager? = null
    var addshopradioshoptype: String = ""
    var user_id: String = ""
    var latitude: String = ""
    var longitude: String = ""

    private val IMAGE_DIRECTORY = "/demonuts"
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shop)
        getSupportActionBar()?.hide();
        volleyManager = VolleyManager()
        init();
        val addshop_btn = findViewById<Button>(R.id.addshop_btn)
        addshop_btn.setOnClickListener(this)
        val addshop_image = findViewById<ImageView>(R.id.addshop_image)
        addshop_image.setOnClickListener(this)
        val list = listOf<String>(
                 Manifest.permission.CAMERA,
                 Manifest.permission.READ_EXTERNAL_STORAGE
             //   Manifest.permission.ACCESS_FINE_LOCATION
        )
        managePermissions = ManagePermissions(this, list, PermissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()


        addshopradioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    addshopradioshoptype = radio.text.toString()
                    /* Toast.makeText(applicationContext," On checked change : ${radio.text}",
                             Toast.LENGTH_SHORT).show()
 */
                })


    }

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
           // getLastLocation()
        }
    }
    private fun showSnackbar(
            mainTextStringId: String, actionStringId: String,
            listener: View.OnClickListener
    ) {
        Toast.makeText(this@AddShopActivity, mainTextStringId, Toast.LENGTH_LONG).show()
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
                this@AddShopActivity,
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
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode ->{
                val isPermissionsGranted = managePermissions.processPermissionsResult(requestCode,permissions,grantResults)

                if(isPermissionsGranted){
                    toast("Permissions granted.")
                }else{
                    toast("Permissions denied.")
                }
                return
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        val IMAGE_DIRECTORY = "/nalhdaf"
    }

    private fun init() {

        val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        latitude = sharedPreference.getString("latitude", "").toString()
        longitude = sharedPreference.getString("longitude", "").toString()
        addshop_shopname = findViewById<EditText>(R.id.addshop_shopname)
        addshop_address = findViewById<EditText>(R.id.addshop_address)
        addshop_city = findViewById<EditText>(R.id.addshop_city)
        addshop_state = findViewById<EditText>(R.id.addshop_state)
        addshop_email = findViewById<EditText>(R.id.addshop_email)
        addshop_mobile = findViewById<EditText>(R.id.addshop_mobile)
        addshop_open = findViewById<TextView>(R.id.addshop_open)
        addshop_close = findViewById<TextView>(R.id.addshop_close)
        addshop_open!!.setOnClickListener(this)
        addshop_close!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.addshop_btn -> {
                if (addshop_shopname!!.text.toString().trim().isEmpty()) {
                    addshop_shopname!!.error = "Please enter valid Shop name"
                    addshop_shopname!!.requestFocus()
                } else if (addshop_address!!.text.toString().trim().isEmpty()) {
                    addshop_address!!.error = "Please enter valid Shop Address"
                    addshop_address!!.requestFocus()
                } else if (addshop_city!!.text.toString().trim().isEmpty()) {
                    addshop_city!!.error = "Please enter valid Shop City name"
                    addshop_city!!.requestFocus()
                } else if (addshop_state!!.text.toString().trim().isEmpty()) {
                    addshop_state!!.error = "Please enter valid State name"
                    addshop_state!!.requestFocus()
                } else if (addshop_pincode!!.text.toString().trim().isEmpty()) {
                    addshop_pincode!!.error = "Please enter valid State name"
                    addshop_pincode!!.requestFocus()
                } else if (addshop_open!!.text.toString().trim().isEmpty()) {
                    addshop_open!!.error = "Please enter valid open time"
                    addshop_open!!.requestFocus()
                } else if (addshop_pincode!!.text.toString().trim().isEmpty()) {
                    addshop_pincode!!.error = "Please enter valid State name"
                    addshop_pincode!!.requestFocus()
                } else if (addshop_mobile!!.text.toString().trim().isEmpty()) {
                    addshop_mobile!!.error = "Please enter valid Mobile number"
                    addshop_mobile!!.requestFocus()
                } else {
                    val addressString = addshop_address!!.text.toString().trim() + addshop_city!!.text.toString().trim() + addshop_state!!.text.toString().trim()
                    hit_api()
                }
            }
            R.id.addshop_image -> {
                showPictureDialog()
            }
            R.id.addshop_open -> {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    addshop_open!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

            }
            R.id.addshop_close -> {
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    addshop_close!!.text = SimpleDateFormat("HH:mm").format(cal.time)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()


            }


        }
    }


    private fun showPictureDialog() {
        val pictureDialog = android.app.AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun chooseImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                beginCrop(data.data)

                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    saveImage1(bitmap)
                    Toast.makeText(this@AddShopActivity, "Image Show!", Toast.LENGTH_SHORT).show()
                    addshop_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "addshop", "")
                    addshopimage = path
                    var inputStream: InputStream? = null
                    try {
                        inputStream = getContentResolver().openInputStream(contentURI!!);
                    } catch (e: FileNotFoundException) {

                    }
                    draw = Drawable.createFromStream(inputStream, contentURI.toString());
                    Log.d("checkaddshopimage", "inside onaciv draw is " + draw);
                    // upload_button.setVisibility(View.VISIBLE);
                    // upload = 1;
                    //setProfileImage(draw);


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@AddShopActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(getContentResolver(), thumbnail, "addshop", "")
            beginCrop(path.toUri())

            addshopimage = path
            addshop_image!!.setImageBitmap(thumbnail)
            var inputStream: InputStream? = null
            try {
                inputStream = getContentResolver().openInputStream(path.toUri());
            } catch (e: FileNotFoundException) {

            }
            draw = Drawable.createFromStream(inputStream, path.toUri().toString());
            Log.d("addshopimage", "inside onaciv draw is " + draw);
        }
    }

    private fun beginCrop(contentURI: Uri?): Any {
        return CropImage.activity(contentURI)
                .setCropMenuCropButtonTitle("Done")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this)
    }

    private fun saveImage1(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this@AddShopActivity, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::---&gt;" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }




    private fun hit_api() {
        val params: MutableMap<String, String> = HashMap()
        params["provider_id"] = user_id
        params["shop_name"] = addshop_shopname!!.text.toString().trim()
        params["address"] = addshop_address!!.text.toString().trim()
        params["city"] = addshop_city!!.text.toString().trim()
        params["state"] = addshop_state!!.text.toString().trim()
        params["pincode"] = addshop_pincode.text.toString().trim()
        params["lat"] = latitude
        params["long"] = longitude
        params["email"] = addshop_email!!.text.toString().trim()
        params["mobile"] = addshop_mobile!!.text.toString().trim()
        params["shop_type"] = addshopradioshoptype

        volleyManager!!.uploadImage(this@AddShopActivity, params, getString(R.string.please_wait), Cons.BASE_URL.toString() + "add_new_shop", "addshop_image",
                "user", draw, object : CallBack {
            override fun onSuccessResponse(response: JSONObject) {
                try {
                    if (response["Status"] != null && response["Status"] == "Success") {
                        val intent: Intent = Intent(this@AddShopActivity, ShopListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@AddShopActivity, response["Message"].toString(), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onSuccessJsonArrayResponse(response: JSONArray?) {}
            override fun onVolleyError(error: VolleyError?) {}
            override fun onTokenExpire() {}
        })

    }
}



