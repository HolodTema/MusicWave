package com.terabyte.musicwave.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

//I explained why there's inject when I described @Binds
class ViewModelFactoryImpl @Inject constructor(
    private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]

        //here we need to check if viewModel has already been found or no
        //if no, we need to find it
        if (creator==null) {
            for ((key, value) in creators) {
                if(modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        //if viewModel hasn't been found, it means that programmer put incorrect class here
        if(creator==null) {
            throw IllegalArgumentException("Unknown model class: $modelClass")
        }

        //and finally we return the value from Provider
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        }
        catch(e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Module
abstract class ViewModelBuilderModule {

    //here we use exactly binds with abstract keyword because ViewModelProvider.Factory is an interface.
    //That's why we can avoid creating implementation using constructor using @Provides
    //in order to avoid it we use @Binds, but that's why everything became abstract!
    //but @Binds also can't let us just leave hidden-constructor implementations creating - we still
    //need to mark our impl constructor with @Inject
    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelFactoryImpl
    ): ViewModelProvider.Factory
}

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)