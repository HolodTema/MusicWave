package com.terabyte.musicwave.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.terabyte.musicwave.R
import com.terabyte.musicwave.activity.MainActivity

class PlaylistsFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).musicComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_songs, container, false)
    }
}