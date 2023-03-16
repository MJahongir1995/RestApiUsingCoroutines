package uz.jahongir.restapiusingcoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import uz.jahongir.restapiusingcoroutines.adapters.MyRvAdapter
import uz.jahongir.restapiusingcoroutines.databinding.ActivityMainBinding
import uz.jahongir.restapiusingcoroutines.databinding.DialogItemBinding
import uz.jahongir.restapiusingcoroutines.models.MyPostToDoRequest
import uz.jahongir.restapiusingcoroutines.models.MyToDo
import uz.jahongir.restapiusingcoroutines.repository.ToDoRepository
import uz.jahongir.restapiusingcoroutines.retrofit.APIClient
import uz.jahongir.restapiusingcoroutines.utils.Status
import uz.jahongir.restapiusingcoroutines.viewmodel.MyToDoViewModel
import uz.jahongir.restapiusingcoroutines.viewmodel.ViewModelFactory
import kotlin.math.log

class MainActivity : AppCompatActivity(), MyRvAdapter.RvClick {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var toDoViewModel: MyToDoViewModel
    private val TAG = "MainActivity"
    private lateinit var toDoRepository: ToDoRepository
    private lateinit var rvAdapter: MyRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        toDoRepository = ToDoRepository(APIClient.getApiService())
        toDoViewModel = ViewModelProvider(
            this,
            ViewModelFactory(toDoRepository)
        ).get(MyToDoViewModel::class.java)
        rvAdapter = MyRvAdapter(rvClick = this)
        binding.rv.adapter = rvAdapter

        loadToDo()

        binding.swipe.setOnRefreshListener {
            loadToDo()
        }

        binding.btnAdd.setOnClickListener {
            addToDo()
        }

    }

    private fun addToDo() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogItem = DialogItemBinding.inflate(layoutInflater)
        dialog.setView(dialogItem.root)

        dialogItem.apply {

            btnSave.setOnClickListener {
                val myPostToDoRequest = MyPostToDoRequest(
                    edtHolat.selectedItem.toString(),
                    edtMatn.text.toString().trim(),
                    edtDeadline.text.toString().trim(),
                    edtTitle.text.toString().trim()
                )
                toDoViewModel.addToDo(myPostToDoRequest).observe(this@MainActivity) {
                    when (it.status) {
                        Status.LOADING -> {
                            addProgress.visibility = View.VISIBLE
                            linearDialog.isEnabled = false
                        }
                        Status.ERROR -> {
                            addProgress.visibility = View.GONE
                            linearDialog.isEnabled = true
                            Toast.makeText(this@MainActivity, "Xatolik", Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            Toast.makeText(
                                this@MainActivity,
                                "${it.data?.sarlavha} ${it.data?.id} ga saqplandi",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.cancel()
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun loadToDo() {
        toDoViewModel.getAllToDo()
            .observe(this) {
                when (it.status) {
                    Status.LOADING -> {
                        Log.d(TAG, "onCreate: Loading")
                        binding.myProgressBar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "onCreate: ${it.message}")
                        binding.myProgressBar.visibility = View.GONE
                        Toast.makeText(this, "Error${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        Log.d(TAG, "onCreate: ${it.data}")
                        rvAdapter.list = it?.data!!
                        rvAdapter.notifyDataSetChanged()

                        binding.myProgressBar.visibility = View.GONE
                    }
                }
            }

    }

    override fun menuClick(imageView: ImageView, myToDo: MyToDo) {
        val popup = PopupMenu(this, imageView)
        popup.inflate(R.menu.todo_menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    editToDo(myToDo)
                }
                R.id.menu_delete -> {
                    deleteToDo(myToDo)
                }
            }
            true
        }
        popup.show()
    }

    private fun editToDo(myToDo: MyToDo) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogItemBinding = DialogItemBinding.inflate(layoutInflater)

        itemDialogItemBinding.apply {
            edtTitle.setText(myToDo.sarlavha)
            edtMatn.setText(myToDo.matn)
            edtDeadline.setText(myToDo.oxirgi_muddat)

            edtHolat.visibility = View.VISIBLE

            when (myToDo.holat) {
                "Yangi" -> edtHolat.setSelection(0)
                "Bajarilmoqda" -> edtHolat.setSelection(1)
                "Yakunlandi" -> edtHolat.setSelection(2)
            }

            btnSave.setOnClickListener {
                val myPostToDoRequest = MyPostToDoRequest(
                    edtHolat.selectedItem.toString(),
                    edtMatn.text.toString().trim(),
                    edtDeadline.text.toString().trim(),
                    edtTitle.text.toString().trim()
                )
                toDoViewModel.updateToDo(myToDo.id, myPostToDoRequest)
                    .observe(this@MainActivity) {

                        when (it.status) {
                            Status.ERROR -> {
                                addProgress.visibility = View.GONE
                                linearDialog.isEnabled = true
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Status.LOADING -> {
                                addProgress.visibility = View.VISIBLE
                                linearDialog.isEnabled = false
                            }
                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    "${it.data?.sarlavha} ${it.data?.id} bilan saqlandi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.cancel()
                            }
                        }
                    }
            }
        }
        dialog.setView(itemDialogItemBinding.root)
        dialog.show()
    }

    private fun deleteToDo(myToDo: MyToDo){
        toDoViewModel.deleteToDo(myToDo.id)

    }
}