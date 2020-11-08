package com.app.graham.partner.listner

import android.content.Intent

interface Cameralistner {
    fun camerarequest(intent: Intent, id:Int, pathdata: String, typedata:String)

}