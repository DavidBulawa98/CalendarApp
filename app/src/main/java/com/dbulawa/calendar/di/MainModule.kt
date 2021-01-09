package com.dbulawa.calendar.di

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dbulawa.calendar.R
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.dbulawa.calendar.db.DatabaseManager
import com.dbulawa.calendar.db.dao.EventDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
}
