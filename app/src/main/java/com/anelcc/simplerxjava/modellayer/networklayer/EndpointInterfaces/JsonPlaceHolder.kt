package com.anelcc.simplerxjava.modellayer.networklayer.EndpointInterfaces

import com.anelcc.simplerxjava.modellayer.entities.Message
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPlaceHolder {
    //region Regular Web Methods

    @GET("/posts")
    fun getMessages(): Call<List<Message>>

    @GET("/posts/{articleId}")
    fun getMessage(@Path("articleId") articleId: String): Call<Message>

    @POST("/posts")
    fun postMessage(@Body message: Message): Call<Message>

    //endregion

}