package com.vkasurinen.vknews.data.local.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourceEntity(
    val id: String?,
    val name: String?
) : Parcelable