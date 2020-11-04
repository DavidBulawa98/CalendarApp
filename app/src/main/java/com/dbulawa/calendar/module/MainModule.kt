package com.dbulawa.calendar.module

import android.app.Activity
import com.dbulawa.calendar.authorization.GoogleAuthService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


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


}