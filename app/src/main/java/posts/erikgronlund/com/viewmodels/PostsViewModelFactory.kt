package posts.erikgronlund.com.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import posts.erikgronlund.com.repositories.PostsRepository

class PostsViewModelFactory(val arg: PostsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PostsRepository::class.java).newInstance(arg)
    }

}