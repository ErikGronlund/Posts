package posts.erikgronlund.com.repositories

import posts.erikgronlund.com.apis.ApiHelper

class PostsRepository(private val apiHelper: ApiHelper) {
    suspend fun getPosts() = apiHelper.getPosts()
    suspend fun getPhotos() = apiHelper.getPhotos()
    suspend fun getComments() = apiHelper.getComments()
}