package com.example.martian.photos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.martian.photos.model.Photo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class PhotoListViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Boolean>
    private lateinit var photosObserver: Observer<List<Photo>>
    private lateinit var useCase: PhotosListUseCase
    private lateinit var viewModel: PhotoListViewModel
    private lateinit var photos: List<Photo>

    @Before
    fun setup() {
        photos = mock()
        useCase = mock()
        viewModel = PhotoListViewModel(useCase)
        loadingObserver = mock()
        errorObserver = mock()
        photosObserver = mock()
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getError().observeForever(errorObserver)
        viewModel.getPhotos().observeForever(photosObserver)
    }


    @Test
    fun should_callUsecase() {
        useCase = mock()
        viewModel = PhotoListViewModel(useCase)
        viewModel.getPhotosByRoverName("")
        verify(useCase).getPhotosByRoverName(any(), any())
    }

    @Test
    fun should_ShowLoadingWhenFetchingPhotos() {
        viewModel.getPhotosByRoverName("")
        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun should_HideErrorWhenFetchingPhotos() {
        viewModel.getPhotosByRoverName("")
        verify(errorObserver).onChanged(eq(false))
    }


    @Test
    fun should_HideError_whenPhotosAreLoaded() {
        setupWithSuccess(photos)
        viewModel.getPhotosByRoverName("")
        verify(errorObserver, times(2)).onChanged(eq(false))
    }


    @Test
    fun should_HideLoading_whenPhotosAreLoaded() {
        setupWithSuccess(photos)
        viewModel.getPhotosByRoverName("")
        verify(loadingObserver).onChanged(false)
    }

    @Test
    fun should_ShowPhotos_whenSuccess() {
        setupWithSuccess(photos)
        viewModel.getPhotosByRoverName("")
        verify(photosObserver).onChanged(eq(photos))
    }

    @Test
    fun should_ShowErrorWhenFailure() {
        setupWithFailure()
        viewModel.getPhotosByRoverName("")
        verify(errorObserver).onChanged(true)

    }

    @Test
    fun should_HideLoadingWhenFailure() {
        setupWithFailure()
        viewModel.getPhotosByRoverName("")
        verify(loadingObserver).onChanged(false)

    }

    private fun setupWithFailure() {
        doAnswer {
            val listener: PhotosListUseCase.Listener<List<Photo>, String> = it.getArgument(1)
            listener.onFailure("Something went wrong")
        }.whenever(useCase).getPhotosByRoverName(any(), any())
    }


    private fun setupWithSuccess(photos: List<Photo>) {
        doAnswer {
            val listener: PhotosListUseCase.Listener<List<Photo>, String> = it.getArgument(1)
            listener.onSuccess(photos)
        }.whenever(useCase).getPhotosByRoverName(any(), any())
    }


}