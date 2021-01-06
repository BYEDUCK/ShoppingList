package com.byeduck.shoppinglist.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsAPI {

    @GET("place/findplacefromtext/json")
    fun findShop(
        @Query("key") apiKey: String,
        @Query("fields") fields: String,
        @Query("locationbias") locationBias: String,
        @Query("input") queryInput: String = "shop",
        @Query("inputtype") inputType: String = "textquery"
    ): Call<String>

}