package com.skyune.loficorner.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.cleannote.data.NoteDatabase
import com.skyune.loficorner.AppPreferences
import com.skyune.loficorner.data.NoteDatabaseDao
import com.skyune.loficorner.WeatherApplication
import com.skyune.loficorner.exoplayer.MusicServiceConnection
import com.skyune.loficorner.exoplayer.MusicSource
import com.skyune.loficorner.network.WeatherApi
import com.skyune.loficorner.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(sharedPreferences: SharedPreferences): AppPreferences {
        return AppPreferences(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context,
        musicSource: MusicSource
    ) = MusicServiceConnection(musicSource, context)

    @Singleton
    @Provides
    fun provideMusicSource(
    ) = MusicSource()



    @Provides
    @Singleton
    fun provideOpenWeatherApi(): WeatherApi {
        // Define a list of base URLs
        val baseUrls = listOf(
            "test",
            "https://blockchange-audius-discovery-02.bdnodes.net",
            "https://discovery-us-01.audius.openplayer.org",
            "https://blockdaemon-audius-discovery-03.bdnodes.net",
            "https://blockchange-audius-discovery-03.bdnodes.net",
            "https://discoveryprovider3.audius.co",
            "https://audius-discovery-1.cultur3stake.com",
            "https://dn-jpn.audius.metadata.fyi",
            "https://blockdaemon-audius-discovery-04.bdnodes.net",
            "https://dn2.monophonic.digital",
            "https://audius-discovery-7.cultur3stake.com",
        )

        // Try each base URL until you find one that works
        var error: Throwable? = null
        for (baseUrl in baseUrls) {
            try {
                return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WeatherApi::class.java)
            } catch (e: Throwable) {
                // If an error occurs, remember it and try the next base URL
                error = e
            }
        }

        // If all base URLs failed, throw the last error
        throw error ?: RuntimeException("All base URLs failed")
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): WeatherApplication {
        return app as WeatherApplication
    }

    private var context: Context? = null

    fun ContextModule(context: Context?) {
        this.context = context
    }

    @Provides
    fun provideContext(): Context? {
        return context
    }

    @Singleton
    @Provides
    fun provideNotesDao(noteDatabase: NoteDatabase): NoteDatabaseDao
            = noteDatabase.noteDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): NoteDatabase
            = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "notes_db"
    ).fallbackToDestructiveMigration().build()
}