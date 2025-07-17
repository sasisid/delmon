package com.app.delmon.fragment

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.delmon.LuckyWheel.LuckyItem
import com.app.delmon.LuckyWheel.LuckyWheelView
import com.app.delmon.Model.SpinnerListData
import com.app.delmon.R
import com.app.delmon.databinding.FragmentLuckyWheelBinding
import com.app.delmon.interfaces.SingleTapListener
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel
import java.util.*


class LuckyWheelFragment : Fragment() {
    private lateinit var binding: FragmentLuckyWheelBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var  luckyWheelView: LuckyWheelView

    var data: List<SpinnerListData.Data> = ArrayList()
    private val coin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLuckyWheelBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val imageView: ImageView =binding.back
        imageView.setOnClickListener(View.OnClickListener {
           requireFragmentManager().popBackStack()
        })
        luckyWheelView = binding.luckyWheel
        binding.play.isEnabled = true
        binding.play.alpha = 1f

        DialogUtils.showLoader(requireContext())
        getData()


   /*     val luckyItem7 = LuckyItem()
        luckyItem7.text = "120"
        luckyItem7.color = Color.parseColor("#752BEF")
        (data as ArrayList<LuckyItem>).add(luckyItem7)
      val luckyItem1 = LuckyItem()
        luckyItem1.text = "10%"
        luckyItem1.icon = R.drawable.rawfeed
        luckyItem1.color = Color.parseColor("#d6a320")
        val luckyItem8 = LuckyItem()
        luckyItem8.text = "140"
        luckyItem8.color = Color.parseColor("#8E84FF")
        (data as ArrayList<LuckyItem>).add(luckyItem8)*/


        binding.play.setOnClickListener{
            val index = getRandomIndex()
            luckyWheelView.startLuckyWheelWithTargetIndex(index)

            binding.play.isEnabled = false
            binding.play.alpha = .5f
        }

        luckyWheelView.setLuckyRoundItemSelectedListener(object : LuckyWheelView.LuckyRoundItemSelectedListener {
            override fun LuckyRoundItemSelected(index: Int) {
                DialogUtils.showLoader(requireContext())
                updateWinningList(data[index-1].id.toString())
            }
        })



        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getData() {

        homeViewModel.getSpinnerData(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
                        } else {
                            data = it.data as List<SpinnerListData.Data>
                            luckyWheelView.setData(it.data as List<SpinnerListData.Data>?)
                            luckyWheelView.setRound(getRandomRound())
                        }
                    }
                }
                DialogUtils.dismissLoader()
            }
    }

    private fun updateWinningList(spinId:String){

        homeViewModel.updateWinData(requireContext(),spinId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.success?.let { error ->
                        if (!error) {
                        } else {
//                            UiUtils.showToast(requireContext(),"You Won ",true)
                            DialogUtils.showPopUp(requireContext(), object :
                                SingleTapListener {
                                override fun singleTap() {
//                            findNavController().navigate(R.id.action_login_fragment_self)
//                            DialogUtils
                                }

                            },"Exciting news! You've received a surprise gift, and our team is just as eager to reveal it as you are to receive it. We can't spill the beans just yet, but rest assured, it's something special and tailored just for you.\n\nStay tuned! In a little while, we'll be sharing all the details about this wonderful surprise. Until then, the anticipation builds, and we hope it brings a smile to your face.")
                        }
                    }
                }
                DialogUtils.dismissLoader()
            }
    }




    private fun getRandomIndex(): Int {
        val ind = intArrayOf(1, 2, 3, 4, 5, 6)
        val rand: Int = Random().nextInt(ind.size)
        return ind[rand]
    }

    private fun getRandomRound(): Int {
        val rand = Random()
        return rand.nextInt(10) + 15
    }


}