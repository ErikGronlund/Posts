package posts.erikgronlund.com.apis

import posts.erikgronlund.com.data.Comment
import posts.erikgronlund.com.data.Photo
import posts.erikgronlund.com.data.Post
import retrofit2.http.GET

interface Endpoints {
    @GET("/posts")
    suspend fun getPosts(): List<Post>

    @GET("/photos")
    suspend fun getPhotos(): List<Photo>

    @GET("/comments")
    suspend fun getComments(): List<Comment>

}