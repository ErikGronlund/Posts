package posts.erikgronlund.com.repositories

import posts.erikgronlund.com.apis.ApiHelper
import posts.erikgronlund.com.apis.Endpoints
import posts.erikgronlund.com.data.ServiceBuilder
import javax.inject.Inject

class PostsRepository @Inject constructor() {
    private val endpoints = ServiceBuilder.buildService(Endpoints::class.java)
    private val apiHelper = ApiHelper(endpoints)

    suspend fun getPosts() = apiHelper.getPosts()
    suspend fun getPhotos() = apiHelper.getPhotos()
    suspend fun getComments(id: String) = apiHelper.getComments(id)
}