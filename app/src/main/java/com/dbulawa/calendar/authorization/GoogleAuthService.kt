package com.dbulawa.calendar.authorization

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.dbulawa.calendar.ui.main.MainFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.*
import javax.inject.Inject


class GoogleAuthService @Inject constructor(
    val signInClient : GoogleSignInClient,
    private val activity: Activity
){

    companion object{
        //Constants
        val RC_AUTHORIZE_CALENDAR = 512
        val RC_SIGN_IN = 12
        val SCOPE_CALENDAR_EVENTS_STRING = "https://www.googleapis.com/auth/calendar.events"
        val SCOPE_CALENDAR_EVENTS = Scope(SCOPE_CALENDAR_EVENTS_STRING)
    }


     fun handleSignInResult() {
        if (!GoogleSignIn.hasPermissions(
                GoogleSignIn.getLastSignedInAccount(activity),
                SCOPE_CALENDAR_EVENTS
            )
        ) {
            GoogleSignIn.requestPermissions(
                activity,
                RC_AUTHORIZE_CALENDAR,
                GoogleSignIn.getLastSignedInAccount(activity),
                SCOPE_CALENDAR_EVENTS
            )
        } else {
           getToken()
        }
    }

    fun getToken(){
        val signedInAccount = GoogleSignIn.getLastSignedInAccount(activity)

        if (signedInAccount != null && !signedInAccount.isExpired) {
            val credential = GoogleAccountCredential.usingOAuth2(
                activity.applicationContext,
                Collections.singleton(
                    SCOPE_CALENDAR_EVENTS_STRING
                )
            )
            credential.selectedAccount = signedInAccount.account
            AsyncTask.execute {
                Log.v("OAuthToken", credential.token)
            }
        }else{
            activity.startActivityForResult(signInClient.signInIntent, RC_SIGN_IN)
        }
    }
}