package com.app.graham.partner.listner

import com.app.graham.partner.pozo.EditprofileResponceModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    @Multipart
    @POST("update_provider_profile")
    fun registereditprofile(
       @Part("provider_id") provider_id: RequestBody,
            @Part("name") name: RequestBody,
            @Part("owner_name") owner_name: RequestBody,
            @Part("gender") gender: RequestBody,
            @Part("email") email: RequestBody,
            @Part("address") address: RequestBody,
            @Part("city") city: RequestBody,
            @Part("state") state: RequestBody,
            @Part("website") website: RequestBody,
            @Part editprofile: MultipartBody.Part): Call<EditprofileResponceModel>
}