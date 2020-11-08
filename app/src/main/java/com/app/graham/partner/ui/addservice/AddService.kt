package com.app.graham.partner.ui.addservice

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
import android.widget.*
import androidx.core.net.toUri
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.graham.partner.R
import com.app.graham.partner.adapter.CategoriesCustomAdapter
import com.app.graham.partner.adapter.SubCategoriesCustomAdapter
import com.app.graham.partner.pozo.CategoriesListPozo
import com.app.graham.partner.pozo.SubCategoriesListPozo
import com.app.graham.partner.services.Cons
import com.app.graham.partner.services.VolleyManager
import com.app.graham.partner.services.callback.CallBack
import com.app.graham.partner.ui.home.HomeFragment
import com.app.graham.partner.utils.CustomProgessDialog
import com.app.graham.partner.utils.WebConfig
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_service.*
import kotlinx.android.synthetic.main.fragment_add_service.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

class AddService : Fragment(),View.OnClickListener {
    private var progessDialog: CustomProgessDialog? = null
    var cal = Calendar.getInstance()
    var addservicenextbtn : Button? = null
    var docs_categoriesspinner: Spinner? = null
    var docs_subcategoriesspinner: Spinner? = null
    var categorieId:String =""
    var subcategorieId:String =""
    var user_id:String =""
    private var categorieslist: ArrayList<CategoriesListPozo>? = null
    private var subcategorieslist: ArrayList<SubCategoriesListPozo>? = null
    var gendername:String=""
    var shopid:String =""
    var addservice_image:ImageView? =null
    private var DatajsonArray: JSONArray? = null
    private var SubDatajsonArray: JSONArray? = null
    var draw: Drawable? =null
    var addservicepath: String = ""
    private var volleyManager: VolleyManager? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/demonuts"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root= inflater.inflate(R.layout.fragment_add_service, container, false)
        volleyManager = VolleyManager()
        val sharedPreference1: SharedPreferences = requireActivity().getSharedPreferences("Shopdata", Context.MODE_PRIVATE)
        shopid = sharedPreference1.getString("shopid", "").toString()//Shopdata

        docs_categoriesspinner = root.findViewById<View>(R.id.docs_categories) as Spinner
        docs_subcategoriesspinner = root.findViewById<View>(R.id.docs_subcategories) as Spinner
        addservicenextbtn = root.findViewById<View>(R.id.addservicenextbtn) as Button
        addservice_image = root.findViewById<View>(R.id.addservice_image) as ImageView
        getcategories()
        addservice_image!!.setOnClickListener(this)
        root.genderrediogroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = root.findViewById(checkedId)
                    gendername =radio.text.toString()

                })


        val sharedPreference: SharedPreferences = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        user_id = sharedPreference.getString("user_id", "").toString()

        docs_subcategoriesspinner!!.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(!subcategorieslist!![position].tittle.contentEquals("Select SubCategory"))
                    subcategorieId = subcategorieslist!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        docs_categoriesspinner!!.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(!categorieslist!![position].trendname.contentEquals("Select Category"))
                    categorieId = categorieslist!![position].id
                if(categorieId.contentEquals("0") ||categorieId.contentEquals("")){

                }else{
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        addservicenextbtn!!.setOnClickListener {
            if(servicefee_addservice.text.toString().equals("")){
                servicefee_addservice.error ="Please enter valid amount"
            }else if(service_gettime.text.toString().equals("")){
                service_gettime.error ="PLease enter Time"
            }else{
                hit_api(servicefee_addservice.text.toString(),service_gettime.text.toString())
            }


        }

        return root;
    }

    private fun getcategories() {
        progessDialog = CustomProgessDialog(activity)
        val requestQueue = Volley.newRequestQueue(activity)
        try {
            val jsonObject = JSONObject()
            jsonObject.put("provider_id", user_id)
            Log.d("request", jsonObject.toString())
            val jobReq = JsonObjectRequest(Request.Method.POST, WebConfig.GET_CATEGORY, jsonObject, { response ->
                Log.d("subcategoriesresp", response.toString())
                progessDialog!!.hide_progress()
                Jsonparser(response,"Category")
            },
                    { error ->
                        progessDialog!!.hide_progress()
                        if (error is NetworkError) { Toast.makeText(activity, "Cannot connect to Internet...Please check your connection!", Toast.LENGTH_LONG).show()
                        } else if (error is ServerError) { Toast.makeText(activity, "Server Error ! Please try again later ", Toast.LENGTH_LONG).show()
                        } else if (error is AuthFailureError) {
                            Toast.makeText(activity, "AuthFailure...Please enter valid Username and Password.", Toast.LENGTH_LONG).show()
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

    private fun Jsonparser(response: JSONObject?, categotydata:String) {
        if(response!!.getString("Status").contentEquals("Success")){
            if(categotydata.contentEquals("Category")) {
                DatajsonArray = response.optJSONArray("Data")
                categorieslist = ArrayList<CategoriesListPozo>()
                if (DatajsonArray != null) {
                    categorieslist!!.add(CategoriesListPozo("0", "Select Category"))
                    for (i in 0 until DatajsonArray!!.length()) {
                        val responsedata_obj = DatajsonArray!!.optJSONObject(i)
                        categorieslist!!.add(CategoriesListPozo(
                                responsedata_obj.optString("id"),
                                responsedata_obj.optString("title")))
                    }
                    docs_categoriesspinner = this.docs_categories
                    val customDropDownAdapter = CategoriesCustomAdapter(requireActivity(), categorieslist!!)
                    docs_categoriesspinner!!.adapter = customDropDownAdapter
                }
            }else if (categotydata.contentEquals("subCategory")) {
                SubDatajsonArray = response.optJSONArray("Data")
                subcategorieslist = ArrayList<SubCategoriesListPozo>()
                if (SubDatajsonArray != null) {
                    subcategorieslist!!.add(SubCategoriesListPozo("0", "Select SubCategory"))
                    for (i in 0 until SubDatajsonArray!!.length()) {
                        val responsedata_obj = SubDatajsonArray!!.optJSONObject(i)
                        subcategorieslist!!.add(SubCategoriesListPozo(
                                responsedata_obj.optString("id"),
                                responsedata_obj.optString("title")))
                    }
                    docs_subcategoriesspinner = this.docs_subcategories
                    val customDropDownAdapter = SubCategoriesCustomAdapter(requireActivity(), subcategorieslist!!)
                    docs_subcategoriesspinner!!.adapter = customDropDownAdapter
                }
            }else if(categotydata.contentEquals("addservice")){
                var fragment: Fragment? = null
                fragment = HomeFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host_fragment, fragment)
                transaction.commit()

            } else {
                Toast.makeText(requireActivity(), `response`!!.getString("Message"), Toast.LENGTH_LONG).show()

            }
        }else {
            Toast.makeText(requireActivity(), `response`!!.getString("Message"), Toast.LENGTH_LONG).show()
        }
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.addservicenextbtn->
            {

            }
            R.id.addservice_image -> {
                showPictureDialog()
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

        if (requestCode == GALLERY) {
            if (data != null) {
                beginCrop(data.data)

                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, contentURI)
                    saveImage1(bitmap)
                    Toast.makeText(requireActivity(), "Image Show!", Toast.LENGTH_SHORT).show()
                    addservice_image!!.setImageBitmap(bitmap)
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), bitmap, "addsevice", "")
                    addservicepath = path
                    var inputStream: InputStream? = null
                    try {
                        inputStream =requireActivity().getContentResolver().openInputStream(contentURI!!);
                    } catch (e: FileNotFoundException) {
                    }
                    draw = Drawable.createFromStream(inputStream, contentURI.toString());
                    Log.d("checkaddshopimage", "inside onaciv draw is " + draw);
                    } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            val bytes = ByteArrayOutputStream()
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(), thumbnail, "addservice", "")
            beginCrop(path.toUri())

            addservicepath = path
            addservice_image!!.setImageBitmap(thumbnail)
            var inputStream: InputStream? = null
            try {
                inputStream = requireActivity().getContentResolver().openInputStream(path.toUri());
            } catch (e: FileNotFoundException) {

            }
            draw = Drawable.createFromStream(inputStream, path.toUri().toString());
            Log.d("addshoppath", "inside onaciv draw is " + draw);
        }
    }

    private fun beginCrop(contentURI: Uri?): Any {
        return CropImage.activity(contentURI)
                .setCropMenuCropButtonTitle("Done")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(requireActivity())
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

    private fun hit_api(servicefees:String ,timedata:String) {
        val params: MutableMap<String, String> = HashMap()
        params["provider_id"] = user_id
        params["service_id"] = categorieId
        params["gender"] = gendername
        params["shop_id"] = shopid
        params["amount"] = servicefees
        params["time"] = timedata
        params["description"] = "aasdszxs"

        volleyManager!!.uploadImage(requireActivity(), params, getString(R.string.please_wait), Cons.BASE_URL.toString() + "provider_add_services", "image",
                "user", draw, object : CallBack {
            override fun onSuccessResponse(response: JSONObject) {
                try {
                    if (response["Status"] != null && response["Status"] == "Success") {
                        Toast.makeText(requireActivity(), response["Message"].toString(), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireActivity(), response["Message"].toString(), Toast.LENGTH_LONG).show()
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

