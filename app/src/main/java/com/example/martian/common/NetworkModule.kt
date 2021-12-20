package com.example.martian

import com.example.martian.common.NasaApi
import com.example.martian.photos.PhotosListUseCase
import com.example.martian.photos.PhotosListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNasaApi(): NasaApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/mars-photos/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(NasaApi::class.java)
    }

}

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun providesUseCase(
        nasaApi: NasaApi
    ): PhotosListUseCase {
        return PhotosListUseCaseImpl(nasaApi)
    }
}