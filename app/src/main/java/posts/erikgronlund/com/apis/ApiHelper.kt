package posts.erikgronlund.com.apis

class ApiHelper(private val endpoints: Endpoints) {
    suspend fun getPosts() = endpoints.getPosts()

    suspend fun getPhotos() = endpoints.getPhotos();

    suspend fun getComments(id: String) = endpoints.getComments(id);
}