package posts.erikgronlund.com.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import posts.erikgronlund.com.R
import posts.erikgronlund.com.data.Photo
import posts.erikgronlund.com.data.Post
import posts.erikgronlund.com.data.PostsAndPhotos

class PostsListAdapter(private val postsList: PostsAndPhotos): RecyclerView.Adapter<PostsListAdapter.PostViewHolder>() {

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
        val post = postsList.posts.data!![position]
        val photo = postsList.photos.data!![position]

        holder.bind(post, photo)
    }

    class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {

        private var image: ImageView? = null
        private var title: TextView? = null
        private var body: TextView? = null

        init {
            image = itemView.findViewById<ImageView>(R.id.image)
            title = itemView.findViewById<TextView>(R.id.title)
            body = itemView.findViewById<TextView>(R.id.body)
        }

        fun bind(post: Post, photo: Photo) {
            title?.text = post.title
            body?.text = post.body
        }

    }
}
