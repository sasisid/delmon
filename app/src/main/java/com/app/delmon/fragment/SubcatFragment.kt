package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.SubCategoryResponse
import com.app.delmon.R
import com.app.delmon.adapter.FeedingSubcatAdapter
import com.app.delmon.databinding.FragmentSubcatBinding
import com.app.delmon.interfaces.IOnBackPressed
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel


class SubcatFragment : Fragment() {
    private lateinit var binding: FragmentSubcatBinding
    private lateinit var homeViewModel: HomeViewModel
        var subcatId =0
        var from = "onclick"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            subcatId = it.getInt("catId",0)
            from = it.getString("from").toString()
        }

    }
    override fun onStart() {
        super.onStart()
        // I'm using null here because drawing nothing is faster than drawing transparent pixels.

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubcatBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        // Inflate the layout for this fragment
        if (from == "onclick") {
            DialogUtils.showLoader(requireContext())
            fetchSubCatData(subcatId)
        }else{
            Log.d("TAG", "onCreateView: back ")
            requireFragmentManager().popBackStack()
        }
        return binding.root
    }

    fun fetchSubCatData(catId:Int){
        homeViewModel.getSubCategoryData(requireContext(),catId)
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding.root, msg)
                            }
                        } else {
                            it.data?.let { data ->
                                val args = Bundle()
                                args.putInt("catId",catId)
                                if (data.isNullOrEmpty()){
                                    findNavController().navigate(R.id.action_subcatFragment_to_category_fragment,args)
                                }else{
                                    fetchSubCat(data)
                                }
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    fun fetchSubCat(data: List<SubCategoryResponse.Data?>) {
        binding!!.subcatRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.VERTICAL,false)
        binding!!.subcatRecyc.scheduleLayoutAnimation();
        binding!!.subcatRecyc.adapter = FeedingSubcatAdapter(requireContext(),data,object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                args.putInt("catId", data[position]!!.id!!)
                args.putString("from", "onclick")
//                args.putInt("catIdPosition", catIdPosition)
                findNavController().navigate(R.id.action_subcatFragment_self,args)

            }
        })

    }

//    override fun onBackPressed(): Boolean {
     /*   return if (myCondition) {
            //action not popBackStack
            true
        } else {
            false
        }*/
//    }

}