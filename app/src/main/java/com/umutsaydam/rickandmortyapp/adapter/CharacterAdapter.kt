package com.umutsaydam.rickandmortyapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umutsaydam.rickandmortyapp.databinding.RvItemBinding
import com.umutsaydam.rickandmortyapp.models.Result
import com.umutsaydam.rickandmortyapp.utils.ItemClickListener


class CharacterAdapter :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {
    lateinit var binding: RvItemBinding
    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    inner class CharacterViewHolder(itemView: RvItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        fun bind(character: Result) {
            with(binding) {
                Glide.with(root.context).load(character.image).into(ivCharacterImage)
                tvCharacterTitle.text = character.name
                tvCharacterType.text = character.type
                tvCharacterStatus.text = character.status
                Log.e("Adapter", "$character.name eklendi. ${character.id}")
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun getChangePayload(oldItem: Result, newItem: Result): Any? {
            return super.getChangePayload(oldItem, newItem);
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CharacterAdapter.CharacterViewHolder {
        binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterAdapter.CharacterViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.setIsRecyclable(false)
        holder.bind(character)
        binding.cvCharacterLayout.setOnClickListener {
            itemClickListener.onItemClickedListener(character)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}