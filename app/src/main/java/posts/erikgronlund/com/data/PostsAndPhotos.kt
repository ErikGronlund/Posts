package posts.erikgronlund.com.data

data class PostsAndPhotos(var posts: Resource<List<Post>>, var photos: Resource<List<Photo>>)