package com.pajri.storyapp.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginSession (
    @field:SerializedName("userId")
    @Expose
    var userId: String? = null,
    @field:SerializedName("name")
    @Expose
    var name: String? = null,
    @field:SerializedName("token")
    @Expose
    var token: String? = null
): Parcelable