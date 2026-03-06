package dev.optimal.tracker.core.ui.di

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
    ): ImageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache(null) // no point caching local files on disk
        .networkObserverEnabled(false) // no network, don't observe connectivity
        .components {
            add(SvgDecoder.Factory())
        }
        .crossfade(true)
        //TODO: debug logger
        .build()
}
