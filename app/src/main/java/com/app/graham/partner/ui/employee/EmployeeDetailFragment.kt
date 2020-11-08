package com.app.graham.partner.ui.employee

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.listner.IOnBackPressed
import com.app.graham.partner.services.Cons
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.services.callback.CallBack
import com.app.graham.partner.toast
import com.app.graham.partner.ui.gallery.GalleryFragment
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.ManagePermissions
import com.app.graham.partner.utils.WebConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.content_edit_profile.*
import kotlinx.android.synthetic.main.content_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_add_gallery.*
import kotlinx.android.synthetic.main.fragment_employee_detail.*
import kotlinx.android.synthetic.main.fragment_employee_detail.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class EmployeeDetailFragment : Fragment(),IOnBackPressed,View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    var user_id :String=""
    var shopid:String =""
    private val GALLERY = 1
    private val CAMERA = 2
    var draw: Drawable? =null
    var addempimage: String = ""
    private val IMAGE_DIRECTORY = "/demonuts"
    private var volleyManager: VolleyManager? = null
    var gendername:String=""
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         val vv = inflater.inflate(R.layout.fragment_employee_detail, container, false)
        volleyManager = VolleyManager()
        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()//Shopdata
        vv.findViewById<FloatingActionButton>(R.id.addempfab).setOnClickListener { view ->
            if(counternumber_empadd.text.toString().isEmpty()){
                counternumber_empadd.error ="Please enter valid Counter Id"
            }else if(empid_empadd.text.toString().isEmpty()){
                empid_empadd.error ="Please enter valid Employee id"
            }else if(firstname_empadd.text.toString().isEmpty()){
                firstname_empadd.error ="Please enter valid First name"
            }else if(mobilenumber_empadd.text.toString().isEmpty() && mobilenumber_empadd.text.length !=10){
                mobilenumber_empadd.error ="Please enter valid mobile number"
            }else if(specialist_empadd.text.toString().isEmpty()){
                specialist_empadd.error ="Please enter valid employee Specialist  "
            }else if(dob_empadd.text.toString().toString().isEmpty()){
                specialist_empadd.error ="Please enter valid employee Date of Birth "
            }else{
                add_newEmployee(counternumber_empadd.text.toString().trim(),empid_empadd.text.toString().trim(),firstname_empadd.text.toString().trim(),
                        email_empadd.text.toString().trim(),
                        mobilenumber_empadd.text.toString().trim(),
                        specialist_empadd.text.toString().trim(),dob_empadd.text.toString().toString())
            }

        }

        vv.findViewById<FloatingActionButton>(R.id.employeeimagefab).setOnClickListener { view ->
            showPictureDialog()
        }
        vv.employeeradioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton =vv.findViewById(checkedId)
                    gendername =radio.text.toString()
                    /* Toast.makeText(applicationContext," On checked change : ${radio.text}",
                             Toast.LENGTH_SHORT).show()
 */                })

        val list = listOf<String>(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
                //   Manifest.permission.ACCESS_FINE_LOCATION
        )
        managePermissions = ManagePermissions(requireActivity(), list, PermissionsRequestCode)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()



        return vv;
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
        Toast.makeText(requireActivity(), mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
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
                    requireActivity().toast("Permissions granted.")
                }else{
                    requireActivity().toast("Permissions denied.")
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


    private fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File (
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }
        try
        {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".png"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(requireActivity(), arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException){
            e1.printStackTrace()
        }
        return ""
    }


    private fun add_newEmployee(counternumber: String, empid: String, firstname: String, email_empadd: String, mobilenumber: String, specialist: String,dob:String) {
        val params: MutableMap<String, String> = HashMap()
        params["provider_id"] = user_id
        params["shop_id"] = shopid
        params["name"] = firstname
        params["gender"] = gendername
        params["dob"] = dob
        params["counter_no"] = counternumber
        params["employee_id"] = empid
        params["specialist"] = specialist
        params["mobile"] = mobilenumber
        params["email"] = email_empadd
        volleyManager!!.uploadImage(requireActivity(), params, getString(R.string.please_wait), Cons.BASE_URL.toString() + "add_new_employee","employee_image",
                "user", draw, object : CallBack {
            override fun onSuccessResponse(response: JSONObject) {
                Log.d("vcmkv",response.toString())
                try {
                    if (response["Status"] != null && response["Status"] == "Success") {
                        var fragment: Fragment? = null
                        fragment = EmployeeFragment()
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
                    addemployee_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    if(bitmap!=null) {
                        val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), bitmap, "addemployee", "")
                        addempimage = path
                        var inputStream: InputStream? = null
                        try {
                            inputStream = requireActivity().getContentResolver().openInputStream(contentURI!!);
                        } catch (e: FileNotFoundException) {

                        }
                        saveImage1(bitmap)

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
                val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), thumbnail, "addemployee", "")
                addempimage=path
                var inputStream: InputStream?=null
                try {
                    inputStream = requireActivity().getContentResolver().openInputStream(path.toUri());
                } catch (e: FileNotFoundException) {

                }

                draw = Drawable.createFromStream(inputStream, path.toUri().toString());
                Log.d("checkprofileimage", "inside onaciv draw is " + draw);

            }//  beginCrop(path.toUri())

            addemployee_image!!.setImageBitmap(thumbnail)

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

        return "";
    }


    private fun beginCrop(contentURI: Uri?): Any {
        return CropImage.activity(contentURI)
                .setCropMenuCropButtonTitle("Done")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(requireActivity())
    }


    override fun onBackPressed(): Boolean {
        var fragment: Fragment? = null
        fragment = EmployeeFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
     //   getFragmentManager()?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        return true;/*if (myCondition) {
            //action not popBackStack
            true
        } else {
            false
        }*/
    }

    override fun onClick(v: View?) {
    when(v?.id){
        R.id.addempfab->
        {

        }
    }
    }
}
