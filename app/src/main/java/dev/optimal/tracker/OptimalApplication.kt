package dev.optimal.tracker

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class OptimalApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>

    override fun onCreate() {
        super.onCreate()
        // TODO initialize Logging
    }

    /**
     * Coil uses this to get the shared image loader.
     */
    override fun newImageLoader(): ImageLoader = imageLoader.get()
}