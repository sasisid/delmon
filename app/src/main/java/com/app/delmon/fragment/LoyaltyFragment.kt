package com.app.delmon.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.WalletResponse
import com.app.delmon.adapter.PointsAdapter
import com.app.delmon.databinding.FragmentLoyaltyBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.Constants
import com.app.delmon.utils.DialogUtils
import com.app.delmon.viewmodel.HomeViewModel

class LoyaltyFragment : Fragment() {


    private var _binding: FragmentLoyaltyBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoyaltyBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.apply {
            points.text = Constants.User.loyalty+ " BD"

        }

        DialogUtils.showLoader(requireContext())
        pointdata()
        return binding.root

    }


    fun pointdata(){
        homeViewModel.gePointList(requireContext()).observe(viewLifecycleOwner) {
            if (it.error!!) {

            } else {
                Log.d("fghj", "" + it.data!!.size)

                // UiUtils.showSnack(binding.root, it.message!!)
                pointsRecyc(it.data!!)


            }

            DialogUtils.dismissLoader()
        }

    }


    private fun pointsRecyc(data: List<WalletResponse.Data?>) {
        binding.pointsRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding.pointsRecyc.scheduleLayoutAnimation();
        binding.pointsRecyc.adapter = PointsAdapter(requireContext(), data, object :
            OnClickListener {
            override fun onClickItem(position: Int) {

            }
        },"loyalty")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}