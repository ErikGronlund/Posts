package posts.erikgronlund.com.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.post_item.view.*
import posts.erikgronlund.com.DetailActivity
import posts.erikgronlund.com.R
import posts.erikgronlund.com.data.Photo
import posts.erikgronlund.com.data.Post
import posts.erikgronlund.com.data.PostsAndPhotos
import kotlin.random.Random

class PostsListAdapter(): RecyclerView.Adapter<PostsListAdapter.PostViewHolder>() {
    private var postsList: PostsAndPhotos? = null

    fun setPostsAndPhotos(posts: PostsAndPhotos?) {
        postsList = posts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return postsList?.posts?.data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList?.posts?.data!![position]
        val index = Random.nextInt(postsList?.photos?.data!!.size - 1);
        val photo = postsList?.photos?.data!![index]

        holder.bind(post, photo, position)
    }

    class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {

        fun bind(post: Post, photo: Photo, index: Int) {
            itemView.title.text = post.title.capitalize()
            itemView.body.text = post.body.capitalize()

            // Glide couldn't load photo thumbnailUrl from provided api in assignment so using picsum api instead.
            val thumbnailUrl = "https://picsum.photos/seed/${index + 1}/200"
            val detailPhotoUrl = "https://picsum.photos/seed/${index + 1}/1200/700"

            Glide.with(itemView)
                .load(thumbnailUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.image)

            itemView.body.setOnClickListener { v ->
                val context = v.context
                val intent = DetailActivity.newIntent(context, post, detailPhotoUrl);
                context.startActivity(intent)
            }
        }
    }
}
