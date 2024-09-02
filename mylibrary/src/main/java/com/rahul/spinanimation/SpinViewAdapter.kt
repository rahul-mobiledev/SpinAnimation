package com.rahul.spinanimation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.rahul.mylibrary.databinding.SpinItemBinding

class SpinViewAdapter(
    @StyleRes private val textStyle: Int,
    private val itemHeight: Float
) : RecyclerView.Adapter<SpinViewAdapter.SpinItemViewHolder>() {

    private val data: MutableList<String> = mutableListOf()

    inner class SpinItemViewHolder(
        private val binding: SpinItemBinding,
        private val textStyle: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                item.updateLayoutParams<ViewGroup.LayoutParams> {
                    height = itemHeight.toInt()
                }
                item.text = data[position % data.size]
                item.setTextAppearance(textStyle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinItemViewHolder {
        return SpinItemViewHolder(
            SpinItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            textStyle
        )
    }


    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: SpinItemViewHolder, position: Int) {
        holder.bind(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<String>) {
        data.apply {
            clear()
            addAll(0, list)
        }
        notifyDataSetChanged()
    }
}
