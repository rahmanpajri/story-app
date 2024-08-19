package com.pajri.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.databinding.ListStoryAdapterBinding
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pajri.storyapp.layout.DetailStoryActivity

class StoryAdapter:PagingDataAdapter<ListStory, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val bindingLayer = ListStoryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(bindingLayer)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ListViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            holder.bind(data)
        }
    }

    inner class ListViewHolder(private val binding: ListStoryAdapterBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: ListStory){
            com.bumptech.glide.Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = data.name
            binding.tvItemCreatedAt.text = data.createdAt
            binding.tvItemDescription.text = data.description

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.OBJECT, data)
                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>(){
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}