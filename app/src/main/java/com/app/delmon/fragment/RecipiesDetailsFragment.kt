package com.app.delmon.fragment

import android.graphics.Color
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentRecipiesDetailsBinding
import com.squareup.picasso.Picasso


class RecipiesDetailsFragment : Fragment() {


    private lateinit var binding: FragmentRecipiesDetailsBinding
    var recipiesPosition:Int =0
    var from:String = ""
    var videoUrl:String = ""
    private lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipiesPosition = it.getInt("position")
            from = it.getString("from","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRecipiesDetailsBinding.inflate(inflater, container, false)
        var data = RecipesFragment.recipiesData
        var  detailsData = ProductDetailsFragment.recipiesData
//        val typeface = Typeface.createFromAsset(requireContext().assets, "font/poppins_regular.ttf")
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.apply {
            if (from=="details") {
                pageTitle.text = detailsData[recipiesPosition].name
                Picasso.get().load(detailsData!![recipiesPosition]!!.thumbnailImage)
                    .placeholder(R.drawable.placeholder_image).error(
                    R.drawable.placeholder_image
                ).into(recipiesImg)
                ingredientContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(detailsData[recipiesPosition].ingredients, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(detailsData[recipiesPosition].ingredients)
                }
                stepsContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(detailsData[recipiesPosition].steps, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(detailsData[recipiesPosition].steps)
                }
                videoUrl = detailsData[recipiesPosition].videos!!
                Log.d("TAG", "videoView: ${detailsData[recipiesPosition].videos}")
            }else{
                pageTitle.text = RecipesFragment.recipiesData[recipiesPosition].name
                Picasso.get().load(data!![recipiesPosition]!!.thumbnailImage)
                    .placeholder(R.drawable.placeholder_image).error(
                        R.drawable.placeholder_image
                    ).into(recipiesImg)
                ingredientContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.e("delmonUser", "Ingredient: ${data[recipiesPosition].ingredients}")

                    if(data[recipiesPosition].ingredients != null) {
                        Html.fromHtml(data[recipiesPosition].ingredients, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        ""
                    }
//                    Html.fromHtml(data[recipiesPosition].ingredients, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    if(data[recipiesPosition].ingredients != null) {
                        Html.fromHtml(data[recipiesPosition].ingredients)
                    } else {
                        ""
                    }
//                    Html.fromHtml("" + data[recipiesPosition].ingredients)
                }
                stepsContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    if(data[recipiesPosition].steps != null) {
                        Html.fromHtml("" + data[recipiesPosition].steps, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        ""
                    }
//                    Html.fromHtml("" + data[recipiesPosition].steps, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    if(data[recipiesPosition].steps != null) {
                        Html.fromHtml("" + data[recipiesPosition].steps)
                    } else {
                        ""
                    }
//                    Html.fromHtml("" + data[recipiesPosition].steps)
                }

                videoUrl = if(data[recipiesPosition].videos != null) {
                    data[recipiesPosition].videos!!
                } else {
                    ""
                }


            }
//            ingredientContent.typeface = typeface
//            stepsContent.typeface = typeface
            /*val uri: Uri = Uri.parse(data[recipiesPosition].videos)
*/
            // on below line we are setting
            // video uri for our video view.
//            videoView.setVideoURI(uri)

          /*  // on below line we are creating variable
            // for media controller and initializing it.
            val mediaController = MediaController(requireContext())

            // on below line we are setting anchor
            // view for our media controller.
            mediaController.setAnchorView(videoView)

            // on below line we are setting media player
            // for our media controller.
            mediaController.setMediaPlayer(videoView)

            // on below line we are setting media
            // controller for our video view.
            videoView.setMediaController(mediaController)*/

            // on below line we are
            // simply starting our video view.
            recipiesImg.setOnClickListener {
                videoView.visibility= View.VISIBLE
//                UiUtils.showToast(requireContext(),"sadd ",true)
                playVideo()

            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    fun playVideo(){
        val uri: Uri = Uri.parse(videoUrl)

        // on below line we are setting
        // video uri for our video view.
        binding.videoView.setVideoURI(uri)

        // on below line we are creating variable
        // for media controller and initializing it.
        val mediaController = MediaController(requireContext())

        // on below line we are setting anchor
        // view for our media controller.
        mediaController.setAnchorView(binding.videoView)

        // on below line we are setting media player
        // for our media controller.
        mediaController.setMediaPlayer(binding.videoView)

        // on below line we are setting media
        // controller for our video view.
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setOnPreparedListener(OnPreparedListener {
            binding.videoView.setBackgroundColor(Color.TRANSPARENT)
            binding.recipiesImg.visibility= View.GONE
        })

        // on below line we are
        // simply starting our video view.
        binding.videoView.start()

    }

}