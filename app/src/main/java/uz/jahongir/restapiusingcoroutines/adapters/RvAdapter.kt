package uz.jahongir.restapiusingcoroutines.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.jahongir.restapiusingcoroutines.databinding.ItemRvBinding
import uz.jahongir.restapiusingcoroutines.models.MyToDo

class MyRvAdapter(var list: List<MyToDo> = emptyList(), val rvClick: RvClick) : RecyclerView.Adapter<MyRvAdapter.VH>() {
    inner class VH(private var itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(myToDo: MyToDo, position: Int) {
            itemRvBinding.name.text = myToDo.sarlavha
            itemRvBinding.deadline.text = myToDo.oxirgi_muddat
            itemRvBinding.status.text = myToDo.holat
            itemRvBinding.matn.text = myToDo.matn

            itemRvBinding.more.setOnClickListener {
                rvClick.menuClick(itemRvBinding.more, myToDo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }
    override fun getItemCount(): Int = list.size

    interface RvClick{
        fun menuClick(imageView:ImageView, myToDo: MyToDo)
    }
}