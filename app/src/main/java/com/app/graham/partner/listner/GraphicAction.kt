package com.app.graham.partner.listner

import android.app.Activity

interface GraphicAction {
    fun action_taken(context: Activity, type:String, select:Int)
}