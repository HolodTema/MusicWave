package com.terabyte.musicwave.fragment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.musicwave.R
import com.terabyte.musicwave.activity.MainActivity
import com.terabyte.musicwave.databinding.FragmentAllSongsBinding
import com.terabyte.musicwave.databinding.ItemSongBinding
import com.terabyte.musicwave.model.MusicModel
import com.terabyte.musicwave.viewmodel.MainViewModel
import java.io.FileNotFoundException
import javax.inject.Inject

class AllSongsFragment : Fragment() {
    private lateinit var binding: FragmentAllSongsBinding

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).musicComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllSongsBinding.inflate(inflater, container, false)

        viewModel.liveDataAllMusicModels.observe(requireActivity()) { musicModels ->
            if(musicModels!=null) {
                binding.recyclerAllSongs.isVisible = true
                binding.textNoSongs.isVisible = false
                binding.recyclerAllSongs.adapter = AllSongsAdapter(viewModel, musicModels, layoutInflater)
            }
            else {
                binding.recyclerAllSongs.isVisible = false
                binding.textNoSongs.isVisible = true
            }
        }

        return binding.root
    }

    class AllSongsAdapter(private val viewModel: MainViewModel, private val musicModels: List<MusicModel>, private val inflater: LayoutInflater): RecyclerView.Adapter<AllSongsAdapter.Holder>() {

        override fun getItemCount() = musicModels.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(ItemSongBinding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val musicModel = musicModels[position]
            with(holder.binding) {
                textItemSongName.text = musicModel.title
                textItemSongAuthor.text = musicModel.author

                imageItemSong.setImageURI(musicModel.imageUri)
                if(imageItemSong.drawable==null) {
                    imageItemSong.setImageResource(R.drawable.song_icon_default)
                }

                root.setOnClickListener {
                    viewModel.liveDataChosenMusicModel.value = musicModel

                    val oldChosenPosition = viewModel.chosenPosition
                    viewModel.chosenPosition = holder.adapterPosition
                    if(oldChosenPosition!=null) notifyItemChanged(oldChosenPosition)
                    notifyItemChanged(viewModel.chosenPosition!!)
                }

                if(position%2==0) {
                    if(position==viewModel.chosenPosition) root.setBackgroundResource(R.drawable.background_item_song_right_corners_chosen)
                    else root.setBackgroundResource(R.drawable.background_item_song_right_corners_not_chosen)
                }
                else {
                    if(position==viewModel.chosenPosition) root.setBackgroundResource(R.drawable.background_item_song_left_corners_chosen)
                    else holder.binding.root.setBackgroundResource(R.drawable.background_item_song_left_corners_not_chosen)
                }
            }
        }
        class Holder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root)
    }
}