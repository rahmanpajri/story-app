package com.pajri.storyapp

import android.content.Context
import com.pajri.storyapp.model.LoginSession

internal class SessionPreference(context: Context) {
    companion object{
        private const val PREFS_NAME ="login_session"
        private const val NAME ="name"
        private const val TOKEN ="token"
        private const val USERID ="user_id"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSession(session: LoginSession){
        val edit = preferences.edit()
        edit.putString(USERID, session.userId)
        edit.putString(NAME, session.name)
        edit.putString(TOKEN, session.token)
        edit.apply()
    }

    fun getSession(): LoginSession{
        val model = LoginSession()
        model.userId = preferences.getString(NAME, "")
        model.name = preferences.getString(NAME, "")
        model.token = preferences.getString(TOKEN, "")

        return model
    }

    fun deleteSession(){
        val edit = preferences.edit()
        edit.clear().apply()
    }
}