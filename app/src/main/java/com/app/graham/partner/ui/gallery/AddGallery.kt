package com.app.graham.partner.ui.gallery

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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.android.volley.VolleyError
import com.app.graham.partner.R
import com.app.graham.partner.services.Cons
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.services.callback.CallBack
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_gallery.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class AddGallery : Fragment(),View.OnClickListener {
    private val GALLERY = 1
    private val CAMERA = 2
    var draw: Drawable? =null
    var profileimage: String = ""
    private var volleyManager: VolleyManager? = null
    var user_id: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val vv= inflater.inflate(R.layout.fragment_add_gallery, container, false)
        volleyManager = VolleyManager()
        val addgallery_image =vv.findViewById(R.id.addgallery_image) as ImageView
        addgallery_image.setOnClickListener(this)
        val addgallery_uploadbtn =vv.findViewById(R.id.addgallery_uploadbtn) as Button
        addgallery_uploadbtn.setOnClickListener(this)

        val sharedPreference: SharedPreferences =requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()

        return vv
    }

    override fun onClick(v: View?) {
      when(v!!.id){
          R.id.addgallery_image ->{
              showPictureDialog()
          }R.id.addgallery_uploadbtn ->{
          hit_api()
      }
      }
    }
    private fun hit_api() {

        val params: MutableMap<String, String> = HashMap()
        params["provider_id"] = user_id


        volleyManager!!.uploadImage(requireActivity(), params, getString(R.string.please_wait), Cons.BASE_URL.toString() +  "save_provider_gallery","addgallery",
                "user", draw, object : CallBack {
            override fun onSuccessResponse(response: JSONObject) {
                try {
                    if (response["Status"] != null && response["Status"] == "Success") {
                        var fragment: Fragment? = null
                        fragment = GalleryFragment()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.nav_host_fragment, fragment)
                        transaction.commit()
                        Toast.makeText(requireActivity(), response["Message"].toString(), Toast.LENGTH_LONG).show()
                    }else {
                        Toast.makeText(requireActivity(),response["Message"].toString(),Toast.LENGTH_LONG).show()
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
                    addgallery_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    if(bitmap!=null) {
                        val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), bitmap, "addgallery", "")
                        profileimage = path
                        var inputStream: InputStream? = null
                        try {
                            inputStream = requireActivity().getContentResolver().openInputStream(contentURI!!);
                        } catch (e: FileNotFoundException) {

                        }
                        draw = Drawable.createFromStream(inputStream, contentURI.toString());
                        Log.d("checkprofileimage", "inside onaciv draw is " + draw);
                    }
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
             saveImage1(thumbnail)

            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            if(thumbnail!=null) {
                val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), thumbnail, "addgallery", "")
                profileimage=path
                var inputStream: InputStream?=null
                try {
                    inputStream = requireActivity().getContentResolver().openInputStream(path.toUri());
                } catch (e: FileNotFoundException) {

                }
                draw = Drawable.createFromStream(inputStream, path.toUri().toString());
                Log.d("checkprofileimage", "inside onaciv draw is " + draw);

            }//  beginCrop(path.toUri())

            addgallery_image!!.setImageBitmap(thumbnail)

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