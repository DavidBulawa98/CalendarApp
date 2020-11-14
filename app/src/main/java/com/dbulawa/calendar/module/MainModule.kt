package com.dbulawa.calendar.module

import android.app.Activity
import com.dbulawa.calendar.R
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Named


@Module
@InstallIn(FragmentComponent::class)
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
}