package com.app.graham.partner.listner

import android.app.Activity

interface Graphiclisteners {
    fun takePhotoFromCamera(context: Activity, type:String)
    fun choosePhotoFromGallary(context: Activity, type:String)
}