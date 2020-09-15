package posts.erikgronlund.com.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import posts.erikgronlund.com.data.Comment
import posts.erikgronlund.com.data.PostsAndPhotos
import posts.erikgronlund.com.data.Resource
import posts.erikgronlund.com.livedata.RefreshableLiveData
import posts.erikgronlund.com.repositories.PostsRepository

class PostsViewModel @ViewModelInject constructor(
    private val postsRepository: PostsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val postsAndPhotos: MutableLiveData<PostsAndPhotos> = MutableLiveData<PostsAndPhotos>(PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading()))

    init {
        getPhotos()
        getPosts()
    }

    fun getPostsWithPhotos(): MutableLiveData<PostsAndPhotos> {
        return postsAndPhotos;
    }

    fun refreshPostsWithPhotos() {
        getPosts()
        getPhotos()
    }

    fun getComments(id: String): RefreshableLiveData<Resource<List<Comment>>> {
        return RefreshableLiveData(source = {loadComments(id)})
    }

    private fun loadComments(id: String): LiveData<Resource<List<Comment>>> {
        val liveData = MutableLiveData<Resource<List<Comment>>>(Resource.loading());

        viewModelScope.launch {
            try {
                liveData.postValue(Resource.success(data = postsRepository.getComments(id)))
            } catch (exception: Exception) {
                liveData.postValue(Resource.error(exception))
            }
        }

        return liveData;
    }

    private fun getPosts() {
        val curr = postsAndPhotos.value
        if (curr == null)
            postsAndPhotos.value = PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading())
        curr?.posts = Resource.loading()

        viewModelScope.launch {
            try {
                val curr = postsAndPhotos.value
                curr?.posts = Resource.success(data = postsRepository.getPosts())
                postsAndPhotos.postValue(curr)
            } catch (exception: Exception) {
                val curr = postsAndPhotos.value
                curr?.posts = Resource.error(exception)
                postsAndPhotos.postValue(curr)
            }
        }
    }

    private fun getPhotos() {
        val curr = postsAndPhotos.value
        if (curr == null)
            postsAndPhotos.value = PostsAndPhotos(posts = Resource.loading(), photos = Resource.loading())
        curr?.photos = Resource.loading();

        viewModelScope.launch {
            try {
                val curr = postsAndPhotos.value
                curr?.photos = Resource.success(data = postsRepository.getPhotos())
                postsAndPhotos.postValue(curr)
            } catch (exception: Exception) {
                val curr = postsAndPhotos.value
                curr?.photos = Resource.error(exception)
                postsAndPhotos.postValue(curr)
            }
        }
    }
}
