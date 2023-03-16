package uz.jahongir.restapiusingcoroutines.retrofit

import retrofit2.Call
import retrofit2.http.*
import uz.jahongir.restapiusingcoroutines.models.MyPostToDoRequest
import uz.jahongir.restapiusingcoroutines.models.MyPostToDoResponse
import uz.jahongir.restapiusingcoroutines.models.MyToDo

interface MyRetrofitService {

    @GET("plan")
    suspend fun getAllToDo(): List<MyToDo>

    @POST("plan/")
    suspend fun addToDo(@Body myPostToDoRequest: MyPostToDoRequest):MyPostToDoResponse

    @PATCH("plan/{id}/")
    suspend fun updateToDo(@Path("id")id:Int, @Body myPostToDoRequest: MyPostToDoRequest):MyPostToDoResponse

    @DELETE("plan/{id}/")
    suspend fun deleteToDo(@Path("id") id:Int): Int
}