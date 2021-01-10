package com.dbulawa.calendar.authorization

import android.app.Activity
import android.app.AuthenticationRequiredException
import android.net.wifi.hotspot2.pps.Credential
import android.os.AsyncTask
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import java.util.*
import javax.inject.Inject


class GoogleAuthService @Inject constructor(
    private val signInClient : GoogleSignInClient,
    private val activity: Activity
){

    companion object{
        //Constants
        val RC_AUTHORIZE_CALENDAR = 512
        val RC_SIGN_IN = 12
        val SCOPE_CALENDAR_EVENTS_STRING = "https://www.googleapis.com/auth/calendar.events"
        val SCOPE_CALENDAR_EVENTS = Scope(SCOPE_CALENDAR_EVENTS_STRING)
    }


     fun handleSignInResult() : GoogleAccountCredential?{
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
            return getToken()
        }
        throw IllegalAccessException("Not authenticated")
     }

    private fun getToken() : GoogleAccountCredential?{
        val signedInAccount = GoogleSignIn.getLastSignedInAccount(activity)

        val credential = GoogleAccountCredential.usingOAuth2(
            activity.applicationContext,
            Collections.singleton(
                SCOPE_CALENDAR_EVENTS_STRING
            )
        )
        credential.selectedAccount = signedInAccount!!.account

        return credential
    }
}