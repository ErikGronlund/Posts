package posts.erikgronlund.com.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import posts.erikgronlund.com.data.Photo
import posts.erikgronlund.com.data.Post
import posts.erikgronlund.com.data.PostsAndPhotos
import posts.erikgronlund.com.data.Resource
import posts.erikgronlund.com.repositories.PostsRepository
import kotlin.coroutines.coroutineContext

class PostsViewModel(private val postsRepository: PostsRepository) : ViewModel() {
    private val posts: MutableLiveData<Resource<List<Post>>> = MutableLiveData<Resource<List<Post>>>()

    private val photos: MutableLiveData<Resource<List<Photo>>> = MutableLiveData<Resource<List<Photo>>>()
    private val postsAndPhotos: MutableLiveData<PostsAndPhotos> = MutableLiveData<PostsAndPhotos>()
    // val postAndPhotos: MediatorLiveData<PostsAndPhotos> = MediatorLiveData<PostsAndPhotos>()

    init {
       /* postAndPhotos.addSource(posts) { result: Resource<List<Post>>? ->
            result?.let {
                postAndPhotos.value?.posts = it
            }
        }

        postAndPhotos.addSource(photos) { result: Resource<List<Photo>>? ->
            result?.let {
                postAndPhotos.value?.photos = it
            }
        };*/

        // println("### Init is called!!")

        // getPostsWithPhotos()
        getPhotos2()
        getPosts2()

        postsAndPhotos.value = PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading())
    }

    fun getPostsWithPhotos(): MutableLiveData<PostsAndPhotos> {
        // println("#### getPostsWithPhotos")
        // getPosts2()
        // getPhotos2()

        return postsAndPhotos;
    }

    fun refreshPostsWithPhotos() {
        getPosts2()
        getPhotos2()
    }

    private fun getPosts() = liveData<Resource<List<Post>>>{
        emit(Resource.loading())
        try {
            emit(Resource.success(data = postsRepository.getPosts()))
        } catch (exception: Exception) {
            emit(Resource.error(exception))
        }
    }

    private fun getPosts2() {
        // println("#### Loading posts")
        val curr = postsAndPhotos.value
        if (curr == null)
            postsAndPhotos.value = PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading())
        curr?.posts = Resource.loading()
        try {
            viewModelScope.launch {
                val curr = postsAndPhotos.value
                println("### Fetching posts from repository")
                curr?.posts = Resource.success(data = postsRepository.getPosts())
                println("### DONE Fetching posts from repository")
                postsAndPhotos.value = curr
                //println("### New data: ")
                //println(posts?.value?.data)
            }
        } catch (exception: Exception) {
            val curr = postsAndPhotos.value 
            curr?.posts = Resource.error(exception)
            postsAndPhotos.value = curr
        }
    }

    private fun getPhotos() = liveData<Resource<List<Photo>>>{
        emit(Resource.loading())
        try {
            emit(Resource.success(data = postsRepository.getPhotos()))
        } catch (exception: Exception) {
            emit(Resource.error(exception))
        }
    }

    private fun getPhotos2() {
        val curr = postsAndPhotos.value
        if (curr == null)
            postsAndPhotos.value = PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading())
        curr?.photos = Resource.loading();
        //photos.postValue(Resource.loading())
        // println("#### Loading photos")
        try {
            val curr = postsAndPhotos.value
            curr?.photos = Resource.loading();
            viewModelScope.launch {
                println("### Fetching photos from repository")
                curr?.photos = Resource.success(data = postsRepository.getPhotos())
                println("### DONE Fetching photos from repository")
                postsAndPhotos.postValue(curr) // photos.postValue(Resource.success(data = postsRepository.getPhotos()))
            }
        } catch (exception: Exception) {
            val curr = postsAndPhotos.value
            curr?.photos = Resource.error(exception)
            postsAndPhotos.value = curr
        }
    }

}
