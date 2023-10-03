package com.nurakbar.myfundamentals1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.nurakbar.myfundamentals1.adapter.MainAdapter
import com.nurakbar.myfundamentals1.api.ApiConfig
import com.nurakbar.myfundamentals1.R
import com.nurakbar.myfundamentals1.response.SearchResponse
import com.nurakbar.myfundamentals1.databinding.ActivityMainBinding
import com.nurakbar.myfundamentals1.ui.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    private lateinit var lottieAnimationView: LottieAnimationView
    private val apiService = ApiConfig.getApiService()
    private val delayDuration = 3000L

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainAdapter = MainAdapter(mutableListOf())
        activityMainBinding.recyclerViewMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }

        this.viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        lottieAnimationView = activityMainBinding.lottieBackgroundAnimationMain

        activityMainBinding.progressBarMain.visibility = View.VISIBLE
        lottieAnimationView.visibility = View.VISIBLE

        fetchGithubUsers("Akbar")

        Handler(Looper.getMainLooper()).postDelayed({
            activityMainBinding.progressBarMain.visibility = View.GONE
            lottieAnimationView.visibility = View.GONE
        }, delayDuration)

        setupSearchView()
        setupRecyclerView()
    }

    private fun setupSearchView() {
        with(activityMainBinding) {
            searchViewMain.setupWithSearchBar(searchBarMain)
            searchViewMain.editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchViewMain.text.toString()
                    performSearch(query)
                    searchViewMain.hide()  // Hide the SearchView after searching
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupRecyclerView() {
        mainAdapter.setOnItemClickCallback { user ->
            // Handle item click: Open detail activity
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            intent.putExtra(DetailActivity.EXTRA_AVATAR_URL, user.avatarUrl)
            startActivity(intent)
        }
    }

    private fun performSearch(query: String) {
        // Check if the query is not empty
        if (query.isNotEmpty()) {
            // Show the loading indicator
            activityMainBinding.progressBarMain.visibility = View.VISIBLE
            fetchGithubUsers(query)
        }
    }

    private fun fetchGithubUsers(query: String) {
        apiService.getSearchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                // Hide the loading indicator
                activityMainBinding.progressBarMain.visibility = View.GONE
                lottieAnimationView.visibility = View.GONE

                if (response.isSuccessful) {
                    val users = response.body()?.items
                    if (users.isNullOrEmpty()) {
                        // Show a message if there are no search results
                        Toast.makeText(this@MainActivity, "No results found", Toast.LENGTH_SHORT).show()
                    } else {
                        // Update the adapter if there are search results
                        mainAdapter.updateUsers(users)
                        activityMainBinding.recyclerViewMain.adapter = mainAdapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                // Hide the loading indicator
                activityMainBinding.progressBarMain.visibility = View.GONE
                lottieAnimationView.visibility = View.GONE

                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}