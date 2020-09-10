package posts.erikgronlund.com.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import posts.erikgronlund.com.R
import posts.erikgronlund.com.data.Photo
import posts.erikgronlund.com.data.Post
import posts.erikgronlund.com.data.PostsAndPhotos
import kotlin.random.Random

class PostsListAdapter(): RecyclerView.Adapter<PostsListAdapter.PostViewHolder>() {
    private var postsList: PostsAndPhotos? = null

    fun setPostsAndPhotos(posts: PostsAndPhotos) {
        postsList = posts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
      val postsData = postsList?.posts?.data
      if (postsData != null) {
          return postsData.size
      } else {
          return 0
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PostViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postsList?.posts?.data!![position]
        val index = Random.nextInt(postsList?.photos?.data!!.size - 1);
        val photo = postsList?.photos?.data!![index]

        holder.bind(post, photo)
    }

    class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {

        private val image: ImageView
        private val title: TextView
        private val body: TextView

        init {
            image = itemView.findViewById<ImageView>(R.id.image)
            title = itemView.findViewById<TextView>(R.id.title)
            body = itemView.findViewById<TextView>(R.id.body)
        }

        fun bind(post: Post, photo: Photo) {
            title.text = post.title
            body.text = post.body
            // Couldn't load photo thumbnailUrl so Justin had to do.
            Glide.with(itemView)
                .load("https://assets.entrepreneur.com/content/3x2/2000/learn-beiber-twitter.jpg")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image)
        }
    }
}
