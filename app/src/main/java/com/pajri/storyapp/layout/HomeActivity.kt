package com.pajri.storyapp.layout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pajri.storyapp.R
import com.pajri.storyapp.SessionPreference
import com.pajri.storyapp.adapter.LoadingStateAdapter
import com.pajri.storyapp.adapter.StoryAdapter
import com.pajri.storyapp.databinding.ActivityHomeBinding
import com.pajri.storyapp.model.LoginSession
import com.pajri.storyapp.viewmodel.HomeViewModel
import com.pajri.storyapp.viewmodel.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mSessionPreference: SessionPreference
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val mHomeViewModel: HomeViewModel by viewModels{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "List Story"

        val layoutManager = LinearLayoutManager(this)
        binding.listStories.layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listStories.addItemDecoration(itemDecoration)

        val loginSession = intent.getParcelableExtra<LoginSession>(EXTRA_RESULT) as LoginSession
        Log.d("HomeActivity", "token : ${loginSession.token}")

        getStories(loginSession)

        binding.newStory.setOnClickListener{
            val move = Intent(this@HomeActivity, NewStoryActivity::class.java)
            move.putExtra(NewStoryActivity.LOGIN_SESSION, loginSession)
            startActivity(move)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        itemResponse(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun itemResponse(selectedItem: Int) {
        when (selectedItem) {
                R.id.action_logout -> {
                    logOut()
                }
                R.id.change_language ->{
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                }
                R.id.maps ->{
                    startActivity(Intent(this@HomeActivity, MapsActivity::class.java))
                }
        }
    }

    private fun logOut() {
        mSessionPreference = SessionPreference(this)
        mSessionPreference.deleteSession()
        Log.d(".HomeActivity", "lihat : ${mSessionPreference.getSession()}")
        Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
        val moveToLogin = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(moveToLogin)
        finish()
    }

    private fun getStories(loginSession: LoginSession) {
        showLoading(true)
        val adapter = StoryAdapter()
        binding.listStories.adapter =adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        mHomeViewModel.getStories(loginSession).observe(this,{
            adapter.submitData(lifecycle, it)
        })
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        }else{
            binding.loadingBar.visibility = View.GONE
        }
    }

    companion object{
        const val EXTRA_RESULT="extra_person"
    }
}