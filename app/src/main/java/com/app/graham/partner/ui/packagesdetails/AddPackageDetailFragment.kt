package com.app.graham.partner.ui.packagesdetails

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.net.toUri
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_package_detail.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class AddPackageDetailFragment : Fragment(),View.OnClickListener {
    private val GALLERY = 1
    private val CAMERA = 2
    var draw: Drawable? =null
    var profileimage: String = ""
    private var volleyManager: VolleyManager? = null
    var user_id: String = ""
    private var progessDialog: CustomProgessDialog? = null
    private var DatajsonArray: JSONArray? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vv = inflater.inflate(R.layout.fragment_add_package_detail, container, false)


        val sharedPreference: SharedPreferences =requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()


        val addpackage_image = vv.findViewById<ImageView>(R.id.addpackage_image)
        addpackage_image.setOnClickListener(this)
        hit_serviceapiapi()
        return vv;
    }

    private fun hit_serviceapiapi() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.list_provider_gallery, jsonObject, Response.Listener { response ->
                Log.d("galleryresp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response,"servicedata")
            },
                    Response.ErrorListener { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) {
                            Toast.makeText(
                                    activity,
                                    "Cannot connect to Internet...Please check your connection!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ServerError) {
                            Toast.makeText(
                                    activity,
                                    "Server Error ! Please try again later ",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(
                                    activity,
                                    "AuthFailure...Please enter valid Username and Password.",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is ParseError) {
                            Toast.makeText(
                                    activity,
                                    "Parsing error! Please try again after some time!!",
                                    Toast.LENGTH_LONG
                            ).show()
                        } else if (error is TimeoutError) {
                            Toast.makeText(
                                    activity,
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

    private fun Jsonparser(response: JSONObject?,servicedata:String) {
        if(response!!.getString("Status").contentEquals("Success")){
           if(servicedata.contentEquals("servicedata")){
               DatajsonArray = response.optJSONArray("Data")
             /*  gallerylist = ArrayList<GallerylistPozo>()
               if (DatajsonArray != null)  {
                   for (i in 0 until DatajsonArray!!.length()) {
                       val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                       gallerylist!!.add(GallerylistPozo(
                               responsedata_obj.optString("image_id"),
                               responsedata_obj.optString("image")))
                   }
*/

             }else{
                Toast.makeText(requireActivity(),`response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addpackage_image->{
                showPictureDialog()
            }
            R.id.addpackage_btn->{
                if(packagedetail_packagename.text.toString().isEmpty()){
                    packagedetail_packagename.error="Please enter valid Package name"
                }/*else if(packageDetails_services.text.toString().isEmpty()){
                    packageDetails_services.error="Please enter valid Services"
                }*/else if(packagedetail_amount.text.toString().isEmpty()){
                    packagedetail_amount.error="Please enter valid Amount"
                }else{

                }
            }
        }
    }


    private fun showPictureDialog() {
        val pictureDialog = android.app.AlertDialog.Builder(requireActivity())
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

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                beginCrop(data.data)

                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, contentURI)
                    saveImage1(bitmap)
                    Toast.makeText(requireActivity(), "Image Show!", Toast.LENGTH_SHORT).show()
                    addpackage_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), bitmap, "", "")
                    profileimage=path
                    var inputStream: InputStream?=null
                    try {
                        inputStream = requireActivity().getContentResolver().openInputStream(contentURI!!);
                    } catch (e: FileNotFoundException) {

                    }
                    draw = Drawable.createFromStream(inputStream, contentURI.toString());
                    Log.d("checkprofileimage", "inside onaciv draw is " + draw);
                }
                catch (e: IOException)
                {
                    e.printStackTrace()
                    Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA)
        {

            val thumbnail = data!!.extras!!.get("data") as Bitmap
            // saveImage1(thumbnail)

            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), thumbnail, "", "")
            beginCrop(path.toUri())

            profileimage=path
            addpackage_image!!.setImageBitmap(thumbnail)

            var inputStream: InputStream?=null
            try {
                inputStream = requireActivity().getContentResolver().openInputStream(path.toUri());
            } catch (e: FileNotFoundException) {

            }
            draw = Drawable.createFromStream(inputStream, path.toUri().toString());
            Log.d("checkprofileimage", "inside onaciv draw is " + draw);
        }
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
            MediaScannerConnection.scanFile(requireActivity(), arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::---&gt;" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    private fun beginCrop(contentURI: Uri?): Any {
        return CropImage.activity(contentURI)
                .setCropMenuCropButtonTitle("Done")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(requireActivity())
    }
    companion object {
        private val IMAGE_DIRECTORY = "/nalhdaf"
    }

}