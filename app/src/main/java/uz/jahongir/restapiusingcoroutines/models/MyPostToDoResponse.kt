package uz.jahongir.restapiusingcoroutines.models

data class MyPostToDoResponse(
    val holat: String,
    val id: Int,
    val matn: String,
    val oxirgi_muddat: String,
    val sarlavha: String
)