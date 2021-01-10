package com.dbulawa.calendar.di

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbulawa.calendar.R
import com.dbulawa.calendar.authorization.AuthInterceptor
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.db.DatabaseManager
import com.dbulawa.calendar.db.dao.EventDao
import com.dbulawa.calendar.wsclient.GoogleEventService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Named


@Module
@InstallIn(ActivityComponent::class)
class MainModule {

    @Provides
    fun googleSignInClient(activity : Activity) : GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(GoogleAuthService.SCOPE_CALENDAR_EVENTS)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(activity, gso)
    }


    @Provides
    @Named("DaysOfTheWeek")
    fun daysOfTheWeek(activity: Activity) : Array<String>{
        return activity.resources.getStringArray(R.array.DaysOfTheWeekRes)
    }

    @Provides
    @Named("MonthsOfTheYear")
    fun monthsOfTheYear(activity: Activity) : Array<String>{
        return activity.resources.getStringArray(R.array.MonthsOfTheYearRes)
    }

    @Provides
    fun databaseManager(@ApplicationContext context: Context): DatabaseManager {
        return Room.databaseBuilder(context, DatabaseManager::class.java, "CalendarDb")
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun eventDao(db : DatabaseManager) : EventDao {
        return db.eventDao()
    }

    @Provides
    fun getRetrofit(client: OkHttpClient, moshi: Moshi) : Retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Provides
    fun getHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(authInterceptor).addInterceptor(interceptor).build()
    }

    @Provides
    fun googleEventService(retrofit: Retrofit) : GoogleEventService {
        return retrofit.create(GoogleEventService::class.java)
    }

    @Provides
    fun moshi() : Moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
}
