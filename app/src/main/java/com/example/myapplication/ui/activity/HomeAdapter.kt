package com.example.myapplication.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Story
import com.example.myapplication.databinding.RvStoryBinding
import com.example.myapplication.utility.Constanta
import com.example.myapplication.utility.Helper


class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var data = mutableListOf<Story>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = data[position]
        holder.bind(story)
    }

    fun initData(story: List<Story>) {
        data.clear()
        data = story.toMutableList()
    }

    inner class ViewHolder(private val binding: RvStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            with(binding) {
                tvItemName.text = story.name
                storyUploadTime.text =
                    "🕓 ${itemView.context.getString(R.string.txt_uploaded)} ${
                        Helper.getTimelineUpload(
                            itemView.context,
                            story.createdAt
                        )
                    }"
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(Constanta.StoryDetail.UserName.name, story.name)
                    intent.putExtra(Constanta.StoryDetail.ImageURL.name, story.photoUrl)
                    intent.putExtra(
                        Constanta.StoryDetail.ContentDescription.name,
                        story.description
                    )
                    intent.putExtra(
                        Constanta.StoryDetail.UploadTime.name,
                        /*
                        dynamic set uploaded time locally
                            en : uploaded + on + 30 April 2022 00.00
                            id : diupload + pada + 30 April 2022 00.00
                        */
                        "${itemView.context.getString(R.string.txt_uploaded)} ${
                            itemView.context.getString(
                                R.string.txt_time_on
                            )
                        } ${Helper.getUploadStoryTime(story.createdAt)}"

                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            /* transition between recyclerview & acitvity detail */
                            androidx.core.util.Pair(ivItemPhoto, "story_image"),
                            androidx.core.util.Pair(tvItemName, "user_name"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

}