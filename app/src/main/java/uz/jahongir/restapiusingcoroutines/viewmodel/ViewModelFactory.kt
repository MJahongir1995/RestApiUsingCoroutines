package uz.jahongir.restapiusingcoroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.jahongir.restapiusingcoroutines.repository.ToDoRepository

class ViewModelFactory(val toDoRepository: ToDoRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MyToDoViewModel::class.java)){
            return MyToDoViewModel(toDoRepository) as T
        }

        throw java.lang.IllegalArgumentException("Error")
    }
}