package posts.erikgronlund.com

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import posts.erikgronlund.com.adapters.PostsListAdapter
import posts.erikgronlund.com.apis.ApiHelper
import posts.erikgronlund.com.apis.Endpoints
import posts.erikgronlund.com.data.PostsAndPhotos
import posts.erikgronlund.com.data.Resource
import posts.erikgronlund.com.data.ServiceBuilder
import posts.erikgronlund.com.repositories.PostsRepository
import posts.erikgronlund.com.viewmodels.PostsViewModel
import posts.erikgronlund.com.viewmodels.PostsViewModelFactory

class PostsActivity : AppCompatActivity() {

    private lateinit var progressBar: View;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: PostsListAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        recyclerView = findViewById(R.id.recycler_view);
        adapter = PostsListAdapter(this)
        recyclerView.adapter = adapter
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE

        val endpoints = ServiceBuilder.buildService(Endpoints::class.java)
        val apiHelper = ApiHelper(endpoints)
        val repository = PostsRepository(apiHelper)
        val factory = PostsViewModelFactory(repository)
        val model: PostsViewModel by viewModels() { factory }

        model.getPostsWithPhotos().observe(this, Observer {
            it?.let {
                val photoStatus = it?.photos?.status
                val postsStatus = it?.posts?.status

                if (photoStatus == Resource.Status.LOADING || postsStatus == Resource.Status.LOADING) {
                    handleLoading();
                } else if (photoStatus == Resource.Status.ERROR || postsStatus == Resource.Status.ERROR) {
                    handleError();
                } else if (photoStatus == Resource.Status.SUCCESS && postsStatus == Resource.Status.SUCCESS) {
                    handleSuccess(it);
                } else {
                    // WTF
                    handleError();
                }
            }
        })
    }

    fun handleLoading() {
        progressBar.visibility = View.VISIBLE
    }

    fun handleSuccess(posts: PostsAndPhotos) {
        progressBar.visibility = View.GONE
        adapter.setPostsAndPhotos(posts)

    }

    fun handleError() {
        progressBar.visibility = View.GONE
    }

}