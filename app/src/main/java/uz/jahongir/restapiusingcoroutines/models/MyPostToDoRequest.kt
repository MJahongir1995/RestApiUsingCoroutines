package uz.jahongir.restapiusingcoroutines.models

data class MyPostToDoRequest(
    val holat: String,
    val matn: String,
    val oxirgi_muddat: String,
    val sarlavha: String
)