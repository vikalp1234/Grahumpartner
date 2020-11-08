package com.app.graham.partner.listner

import org.json.JSONObject

interface NetworkCallbacks {

    fun successCallback(jsonObject: JSONObject, typedata: String)

}