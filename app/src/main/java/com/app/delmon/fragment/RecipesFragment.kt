package com.app.delmon.fragment

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.delmon.Model.CategoryResponse
import com.app.delmon.Model.RecipesResponse
import com.app.delmon.R
import com.app.delmon.adapter.CategoryAdapter
import com.app.delmon.adapter.RecipesAdapter
import com.app.delmon.databinding.FragmentRecipesBinding
import com.app.delmon.interfaces.OnClickListener
import com.app.delmon.utils.DialogUtils
import com.app.delmon.utils.UiUtils
import com.app.delmon.viewmodel.HomeViewModel

class RecipesFragment : Fragment() {

    private lateinit var binding: FragmentRecipesBinding
    private lateinit var homeViewModel: HomeViewModel
    lateinit var adapter:RecipesAdapter
    companion object{
        var recipiesData: ArrayList<RecipesResponse.Data> = ArrayList()
        var filteredrecipiesData: ArrayList<RecipesResponse.Data> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.back.setOnClickListener {
            requireFragmentManager().popBackStack()
        }
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No implementation needed here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.e("delmonUser", "filteredrecipiesData: $filteredrecipiesData")
                Log.e("delmonUser", "recipiesData: $recipiesData")
//                Log.e("delmonUser", "it.name: ${recipiesData[0].name}")
               filteredrecipiesData = recipiesData.filter {
                   it.name?.contains(s.toString(),ignoreCase = true) ?: false
               } as ArrayList<RecipesResponse.Data>
                recipesListRecyc(filteredrecipiesData)
                binding.recipesListRecyc.adapter!!.notifyDataSetChanged()

            }
        })
        getcatData()
        receipesListData(0)
        // Inflate the layout for this fragment
        return binding.root
    }
    fun getcatData(){
        DialogUtils.showLoader(requireContext())
        homeViewModel.getCategoryData(requireContext())
            .observe(viewLifecycleOwner
            ) {
                Log.d(ContentValues.TAG, "categorydata: " + it)
                it?.let {
                    it.error?.let { error ->
                        if (error) {
                            it.message?.let { msg ->
                                UiUtils.showSnack(binding!!.root, msg)
                            }
                        } else {
                            it.data?.let { data ->
                                recipesCatRecyc(data)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }

    fun receipesListData(catId:Int){
        DialogUtils.showLoader(requireContext())
        homeViewModel.getRecipes(requireContext(),catId)
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
                            it.data?.let {
                                recipiesData = it as ArrayList<RecipesResponse.Data>
                                filteredrecipiesData = recipiesData
                                recipesListRecyc(filteredrecipiesData)
                            }

                        }
                    }

                }
                DialogUtils.dismissLoader()
            }
    }


    private fun recipesCatRecyc(data: List<CategoryResponse.Data?>) {
        binding.recipesRecyc.layoutManager = GridLayoutManager(requireContext(),1,
            GridLayoutManager.HORIZONTAL,false)
        binding.recipesRecyc.scheduleLayoutAnimation();
        binding.recipesRecyc.adapter = CategoryAdapter(requireContext(),data, object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                receipesListData(data[position]!!.id!!)

            }
        }, 0)
    }

    private fun recipesListRecyc(data: List<RecipesResponse.Data?>) {
        binding.recipesListRecyc.layoutManager = GridLayoutManager(requireContext(),2,
            GridLayoutManager.VERTICAL,false)
        binding.recipesListRecyc.scheduleLayoutAnimation();
        adapter = RecipesAdapter(requireContext(),data,"first",object :
            OnClickListener {
            override fun onClickItem(position: Int) {
                val args = Bundle()
                args.putInt("position",position)
                findNavController().navigate(R.id.action_receipes_fragment_to_recipiesDetailsFragment,args)

            }
        })
        binding.recipesListRecyc.adapter = adapter
        binding.recipesListRecyc.adapter!!.notifyDataSetChanged()

    }

}