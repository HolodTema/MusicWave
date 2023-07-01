package com.terabyte.musicwave.model

import android.net.Uri

data class MusicModel(val id: Long,
val title: String,
val author: String,
val album: String,
val albumId: Long,
val uri: String,
val duration: Int,
val imageUri: Uri
)
