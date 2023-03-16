package uz.jahongir.restapiusingcoroutines.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import uz.jahongir.restapiusingcoroutines.models.MyPostToDoRequest
import uz.jahongir.restapiusingcoroutines.models.MyPostToDoResponse
import uz.jahongir.restapiusingcoroutines.models.MyToDo
import uz.jahongir.restapiusingcoroutines.repository.ToDoRepository
import uz.jahongir.restapiusingcoroutines.retrofit.APIClient
import uz.jahongir.restapiusingcoroutines.retrofit.MyRetrofitService
import uz.jahongir.restapiusingcoroutines.utils.Resource
import uz.jahongir.restapiusingcoroutines.utils.Status

class MyToDoViewModel(val toDoRepository: ToDoRepository):ViewModel(){

    private val liveData = MutableLiveData<Resource<List<MyToDo>>>()

    fun getAllToDo():MutableLiveData<Resource<List<MyToDo>>>{

        viewModelScope.launch {
            liveData.postValue(Resource.loading("loading"))
            try {
                coroutineScope {
                    val list = async {
                        toDoRepository.getAllToDo()
                    }.await()

                    liveData.postValue(Resource.success(list))
                }
            }catch (e:Exception){
                liveData.postValue(Resource.error(e.message))
            }
        }
        return liveData
    }

    private val postLiveData = MutableLiveData<Resource<MyPostToDoResponse>>()

    fun addToDo(myPostToDoRequest: MyPostToDoRequest):MutableLiveData<Resource<MyPostToDoResponse>>{
        viewModelScope.launch {
            postLiveData.postValue(Resource.loading("Loading"))
            try {
                coroutineScope {
                    val response = async {
                        toDoRepository.addToDo(myPostToDoRequest)
                    }.await()
                    postLiveData.postValue(Resource.success(response))
                    getAllToDo()
                }
            }catch (e:Exception){
                postLiveData.postValue(Resource.error(e.message))
            }
        }
        return postLiveData
    }

    private val liveDataUpdate = MutableLiveData<Resource<MyPostToDoResponse>>()
    fun updateToDo(id:Int, myPostToDoRequest: MyPostToDoRequest):MutableLiveData<Resource<MyPostToDoResponse>>{
        viewModelScope.launch {
            liveDataUpdate.postValue(Resource.loading("Updating"))
            try {
                coroutineScope {
                    val response = async {
                        toDoRepository.updateToDo(id, myPostToDoRequest)
                    }.await()
                    liveDataUpdate.postValue(Resource.success(response))
                    getAllToDo()
                }
            }catch (e:Exception){
                liveDataUpdate.postValue(Resource.error(e.message))
            }
        }
        return liveDataUpdate
    }


    fun deleteToDo(id:Int){
        viewModelScope.launch {
            try {
                coroutineScope {
                    val response = launch {
                        toDoRepository.deleteToDo(id)
                    }
                    getAllToDo()
                }
            }catch (e:Exception){
            }
        }
    }
}