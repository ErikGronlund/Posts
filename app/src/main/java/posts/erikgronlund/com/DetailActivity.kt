package posts.erikgronlund.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.CollapsingToolbarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_card.view.*
import posts.erikgronlund.com.data.*
import posts.erikgronlund.com.livedata.RefreshableLiveData
import posts.erikgronlund.com.viewmodels.PostsViewModel

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private var comment1: CardView? = null
    private var comment2: CardView? = null
    private var comment3: CardView? = null
    private var errorView: View? = null
    private lateinit var progressBar: View
    private lateinit var commentsLiveData: RefreshableLiveData<Resource<List<Comment>>>

    companion object {
        const val EXTRA_POST_ID = "id"
        const val EXTRA_POST_TITLE = "title"
        const val EXTRA_POST_BODY = "body"
        const val EXTRA_POST_PHOTO_URL = "photo"

        fun newIntent(context: Context, post: Post, photoUrl: String): Intent {
            val detailIntent = Intent(context, DetailActivity::class.java)

            detailIntent.putExtra(EXTRA_POST_ID, post.id)
            detailIntent.putExtra(EXTRA_POST_TITLE, post.title)
            detailIntent.putExtra(EXTRA_POST_BODY, post.body)
            detailIntent.putExtra(EXTRA_POST_PHOTO_URL, photoUrl)

            return detailIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        setDataFromIntent()
        loadComments()
    }

    private fun initViews() {
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.GONE
    }

    private fun setDataFromIntent() {
        val title = intent.getStringExtra(EXTRA_POST_TITLE)
        val body = intent.getStringExtra(EXTRA_POST_BODY)
        val photoUrl = intent.getStringExtra(EXTRA_POST_PHOTO_URL)

        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title?.capitalize()
        findViewById<TextView>(R.id.details).text = body?.capitalize()
        val backdrop = findViewById<ImageView>(R.id.backdrop);

        Glide.with(this)
            .load(photoUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(backdrop)
    }

    private fun loadComments() {
        val model: PostsViewModel by viewModels()

        val id = intent.getIntExtra(EXTRA_POST_ID, -1)
        commentsLiveData = model.getComments(id.toString())
        commentsLiveData.observe(this, this::handleCommentsChange)
    }

    private fun handleCommentsChange(comments: Resource<List<Comment>>) {
        comments?.let {
            when(it.status) {
                Resource.Status.LOADING -> handleLoading()
                Resource.Status.ERROR -> handleError()
                Resource.Status.SUCCESS -> handleSuccess(it.data)
            }
        }
    }

    private fun handleLoading() {
        progressBar.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    private fun handleSuccess(comments: List<Comment>?) {
        progressBar.visibility = View.GONE
        errorView?.visibility = View.GONE

        if (comments === null)
            return

        val numberOfComments = comments.size

        for (index in 0..2) {
            if (index < numberOfComments - 1) {
                var view = getCommentView(index)
                val comment = comments[index]

                if (view !== null) {
                    showComment(view, comment)
                } else {
                    hideComment(index)
                }
            } else {
                hideComment(index)
            }
        }
    }

    private fun handleError() {
        progressBar.visibility = View.GONE

        if (errorView === null) {
            val stub = findViewById<ViewStub>(R.id.stub_error)
            errorView = stub.inflate()
        }

        errorView?.visibility = View.VISIBLE
        errorView?.findViewById<Button>(R.id.try_again)?.setOnClickListener {
            commentsLiveData.refresh()
        }
    }

    private fun getCommentView(index: Int): CardView? {
        when(index) {
            0 -> {
                if (comment1 === null) {
                  val stub = findViewById<ViewStub>(R.id.stub_comment_1);
                  comment1 = stub.inflate() as CardView;
                }

                return comment1
            }
            1 -> {
               if (comment2 === null) {
                   val stub = findViewById<ViewStub>(R.id.stub_comment_2)
                   comment2 = stub.inflate() as CardView
               }

               return comment2
            }
            2 -> {
                if (comment3 === null) {
                    val stub = findViewById<ViewStub>(R.id.stub_comment_3)
                    comment3 = stub.inflate() as CardView
                }

                return comment3
            }
        }

        return null
    }

    private fun showComment(view: CardView, comment: Comment) {
        view.visibility = View.VISIBLE
        view.title.text = comment.name.capitalize()
        view.description.text = comment.body.capitalize()
    }

    private fun hideComment(index: Int) {
        when (index) {
            0 -> comment1?.visibility = View.GONE
            1 -> comment2?.visibility = View.GONE
            2 -> comment3?.visibility = View.GONE
        }
    }
}