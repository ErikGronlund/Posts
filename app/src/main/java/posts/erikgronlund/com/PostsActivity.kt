package posts.erikgronlund.com

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.hilt.android.AndroidEntryPoint
import posts.erikgronlund.com.adapters.PostsListAdapter
import posts.erikgronlund.com.data.PostsAndPhotos
import posts.erikgronlund.com.data.Resource
import posts.erikgronlund.com.viewmodels.PostsViewModel

@AndroidEntryPoint
class PostsActivity : AppCompatActivity() {

    private lateinit var progressBar: View;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: PostsListAdapter;
    private var errorView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        recyclerView = findViewById(R.id.recycler_view);
        adapter = PostsListAdapter()
        recyclerView.adapter = adapter
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE

        val model: PostsViewModel by viewModels()

        model.getPostsWithPhotos().observe(this, Observer {
            it?.let {
                val photoStatus = it.photos.status
                val postsStatus = it.posts.status

                if (photoStatus == Resource.Status.ERROR || postsStatus == Resource.Status.ERROR) {
                    handleError();
                } else if (photoStatus == Resource.Status.LOADING || postsStatus == Resource.Status.LOADING) {
                    handleLoading();
                } else if (photoStatus == Resource.Status.SUCCESS && postsStatus == Resource.Status.SUCCESS) {
                    handleSuccess(it);
                } else {
                    // WTF
                    handleError();
                }
            }
        })
    }

    private fun handleLoading() {
        progressBar.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    private fun handleSuccess(posts: PostsAndPhotos) {
        progressBar.visibility = View.GONE
        errorView?.visibility = View.GONE
        adapter.setPostsAndPhotos(posts)
    }

    private fun handleError() {
        progressBar.visibility = View.GONE
        adapter.setPostsAndPhotos(null)

        if (errorView === null) {
            val stub = findViewById<ViewStub>(R.id.stub_error)
            errorView = stub.inflate()
        }

        errorView?.visibility = View.VISIBLE
        errorView?.findViewById<Button>(R.id.try_again)?.setOnClickListener {
            val model: PostsViewModel by viewModels()
            model.refreshPostsWithPhotos()
        }
    }

}