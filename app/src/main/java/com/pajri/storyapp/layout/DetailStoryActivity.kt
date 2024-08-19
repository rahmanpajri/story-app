package com.pajri.storyapp.layout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.pajri.storyapp.R
import com.pajri.storyapp.api.ListStory
import com.pajri.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    companion object {
        const val OBJECT = "object"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        supportActionBar?.title = "Detail Story"

        val story = intent.getParcelableExtra<ListStory>(OBJECT) as ListStory

        with(binding){
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = story.name
            tvDetailCreated.text = story.createdAt
            tvDetailDescription.text = story.description
        }
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }
}