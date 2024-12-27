package com.example.fetchproject.di

import android.content.Context
import androidx.room.Room
import com.example.fetchproject.data.network.FetchApiService
import com.example.fetchproject.data.repo.DefaultFetchRepo
import com.example.fetchproject.data.repo.FetchRepo
import com.example.fetchproject.data.source.local.ListItemDatabase
import com.example.fetchproject.data.source.local.LocalDataSource
import com.example.fetchproject.data.source.local.RoomLocalDataSource
import com.example.fetchproject.data.source.remote.RemoteDataSource
import com.example.fetchproject.data.source.remote.RetrofitRemoteDataSource
import com.example.fetchproject.domain.RefreshListUseCase
import com.example.fetchproject.domain.ViewListUseCase
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
object UseCaseModule {
    @Singleton
    @Provides
    fun provideViewListUseCase(
        repo: FetchRepo
    ): ViewListUseCase {
        return ViewListUseCase(repo)
    }

    @Singleton
    @Provides
    fun provideRefreshListUseCase(
        repo: FetchRepo
    ): RefreshListUseCase {
        return RefreshListUseCase(repo)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideFetchRepo(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) : FetchRepo {
        return DefaultFetchRepo(localDataSource = localDataSource, remoteDataSource = remoteDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideLocalDataSource(
        database: ListItemDatabase
    ): LocalDataSource {
        return RoomLocalDataSource(database.listItemDao)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        fetchApiService: FetchApiService
    ): RemoteDataSource {
        return RetrofitRemoteDataSource(fetchApiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FetchApiServiceModule {
    @Singleton
    @Provides
    fun provideFetchApiService() : FetchApiService {
        return Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FetchApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideLocalDatabase(@ApplicationContext context: Context) : ListItemDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ListItemDatabase::class.java,
            "project_db"
        ).build()
    }
}