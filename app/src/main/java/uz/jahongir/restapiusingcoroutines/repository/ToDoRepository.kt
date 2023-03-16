package uz.jahongir.restapiusingcoroutines.repository

import uz.jahongir.restapiusingcoroutines.models.MyPostToDoRequest
import uz.jahongir.restapiusingcoroutines.retrofit.MyRetrofitService

class ToDoRepository(private val myRetrofitService: MyRetrofitService) {

    suspend fun getAllToDo() = myRetrofitService.getAllToDo()
    suspend fun addToDo(myPostToDoRequest: MyPostToDoRequest) = myRetrofitService.addToDo(myPostToDoRequest)
    suspend fun updateToDo(id:Int, myPostToDoRequest: MyPostToDoRequest) = myRetrofitService.updateToDo(id,myPostToDoRequest)
    suspend fun deleteToDo(id:Int) = myRetrofitService.deleteToDo(id)
}