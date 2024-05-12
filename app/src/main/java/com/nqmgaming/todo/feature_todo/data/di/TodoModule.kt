package com.nqmgaming.todo.feature_todo.data.di

import android.content.Context
import androidx.room.Room
import com.nqmgaming.todo.feature_todo.data.local.TodoDao
import com.nqmgaming.todo.feature_todo.data.local.TodoDatabase
import com.nqmgaming.todo.feature_todo.data.remote.TodoApi
import com.nqmgaming.todo.feature_todo.data.repo.TodoListRepoImpl
import com.nqmgaming.todo.feature_todo.domain.repo.TodoListRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Provides
    fun providesRetrofitApi(retrofit: Retrofit): TodoApi {
        return retrofit.create(TodoApi::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://todoapp-437a3-default-rtdb.firebaseio.com/")
            .build()
    }

    @Provides
    fun providesRoomDao(database: TodoDatabase): TodoDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesTodoListRepo(dao: TodoDao, api: TodoApi, @IoDispatcher dispatcher: CoroutineDispatcher): TodoListRepo {
        return TodoListRepoImpl(dao, api, dispatcher)
    }
}