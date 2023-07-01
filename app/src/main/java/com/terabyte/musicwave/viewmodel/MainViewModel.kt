package com.terabyte.musicwave.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.terabyte.musicwave.di.MusicScope
import com.terabyte.musicwave.model.MusicModel

class MainViewModel: ViewModel() {
    val liveDataAllMusicModels = MutableLiveData<List<MusicModel>?>()

    val liveDataChosenMusicModel = MutableLiveData<MusicModel>()

    var chosenPosition: Int? = null


    fun getAllMusicModelsIfNecessary(context: Context) {
        if(!liveDataAllMusicModels.isInitialized) {
            val uriAllSongs = Media.EXTERNAL_CONTENT_URI
            val selection = Media.IS_MUSIC + "!=0"

            val musicModelFields = arrayOf(Media._ID, Media.TITLE, Media.ARTIST,
                Media.ALBUM, Media.ALBUM_ID, Media.DATA, Media.DURATION)

            val cursor = context.contentResolver.query(uriAllSongs, musicModelFields, selection, null, null)
            val result = if(cursor!=null) {
                val songs = mutableListOf<MusicModel>()
                with(cursor) {
                    while(cursor.moveToNext()) {
                        val id = getLong(getColumnIndexOrThrow(Media._ID))
                        val title = getString(getColumnIndexOrThrow(Media.TITLE))
                        val author = getString(getColumnIndexOrThrow(Media.ARTIST))
                        val album = getString(getColumnIndexOrThrow(Media.ALBUM))
                        val albumId = getLong(getColumnIndexOrThrow(Media.ALBUM_ID))
                        val uri = getString(getColumnIndexOrThrow(Media.DATA))
                        val duration = getInt(getColumnIndexOrThrow(Media.DURATION))

                        val imageContentUri = Uri.parse("content://media/external/audio/albumart")
                        val imageUri = ContentUris.withAppendedId(imageContentUri, albumId)

                        songs.add(MusicModel(id, title, author, album, albumId, uri, duration, imageUri))
                    }
                }
                songs
            }
            else null
            liveDataAllMusicModels.value = result
        }
    }

}