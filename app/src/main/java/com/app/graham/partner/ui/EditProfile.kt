package com.app.graham.partner.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.android.volley.VolleyError
import com.app.graham.partner.R
import com.app.graham.partner.services.Cons
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.services.callback.CallBack
import com.app.graham.partner.utils.CameraUtil
import com.app.graham.partner.utils.CustomProgessDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.content_edit_profile.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*


class EditProfile : AppCompatActivity(),View.OnClickListener {
    var draw: Drawable? =null
    var profileimage: String = ""
    private val GALLERY = 1
    private val CAMERA = 2
    private var volleyManager: VolleyManager? = null
    var user_id: String = ""

    var gendername:String=""
    var latitude:String =""
    var longitude:String =""
    private val IMAGE_DIRECTORY = "/demonuts"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        volleyManager = VolleyManager()

        val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        latitude = sharedPreference.getString("latitude", "").toString()
        longitude = sharedPreference.getString("longitude", "").toString()
        val  editprofilebtn_save = findViewById<Button>(R.id.editprofilebtn_save)
        editprofilebtn_save.setOnClickListener(this)
        findViewById<FloatingActionButton>(R.id.editprofile_btn).setOnClickListener { view ->

               showPictureDialog()
        }

        radioGroup1.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    gendername =radio.text.toString()
                  })


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
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                beginCrop(data.data)

                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    saveImage1(bitmap)
                    Toast.makeText(this@EditProfile, "Image Show!", Toast.LENGTH_SHORT).show()
                    profile_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,"","")
                    profileimage=path
                   var inputStream:InputStream?=null
                    try {
                    inputStream = getContentResolver().openInputStream(contentURI!!);
                } catch (e:FileNotFoundException) {

                }
                    draw = Drawable.createFromStream(inputStream, contentURI.toString());
                Log.d("checkprofileimage", "inside onaciv draw is " + draw);
                }
                catch (e: IOException)
                {
                    e.printStackTrace()
                    Toast.makeText(this@EditProfile, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(getContentResolver(), thumbnail, "", "")
            beginCrop(path.toUri())

            profileimage=path
            profile_image!!.setImageBitmap(thumbnail)
            var inputStream:InputStream?=null
            try {
                inputStream = getContentResolver().openInputStream(path.toUri());
            } catch (e:FileNotFoundException) {

            }
            draw = Drawable.createFromStream(inputStream, path.toUri().toString());
            Log.d("checkprofileimage", "inside onaciv draw is " + draw);
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
            MediaScannerConnection.scanFile(this@EditProfile, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::---&gt;" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.editprofilebtn_save->
            {
           hit_api()
            }
        }
    }

    private fun hit_api() {
        val params: MutableMap<String, String> = HashMap()
        params["provider_id"] = user_id
        params["salon_name"] = firstName_edittext.text.toString().trim()
        params["owner_name"] = ownername_edittext.text.toString().trim()
        params["owner_id"] = ownerid_edittext.text.toString().trim()
        params["gender"] = gendername
        params["email"] = email_edittext.text.toString().trim()
        params["address"] = editprofileaddress_edittext.text.toString().trim()
        params["city"] = editprofilecity_edittext.text.toString().trim()
        params["state"] = editprofilestate_edittext.text.toString().trim()
        params["lat"] = latitude
        params["long"] = longitude
        params["website"] = editprofilewebsite_edittext.text.toString().trim()
        params["registrationID"] = editprofileregister_edittext.text.toString().trim()

        volleyManager!!.uploadImage(this@EditProfile, params, getString(R.string.please_wait), Cons.BASE_URL.toString() +  "update_provider_profile","editprofile",
                "user", draw, object : CallBack {
            override fun onSuccessResponse(response: JSONObject) {
                try {
                    if (response["Status"] != null && response["Status"] == "Success") {
                        Toast.makeText(this@EditProfile, response["Message"].toString(), Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(this@EditProfile,response["Message"].toString(),Toast.LENGTH_LONG).show()
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


