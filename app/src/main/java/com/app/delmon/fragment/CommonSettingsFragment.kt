package com.app.delmon.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.delmon.R
import com.app.delmon.databinding.FragmentCommonSettingsBinding
import com.app.delmon.databinding.FragmentSettingsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val pageTitle = "param1"
private const val pageContent = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommonSettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommonSettingsFragment : Fragment() {
    private var pageTitle: String? = null
    private var pageContent: String? = null
    private lateinit var binding: FragmentCommonSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageTitle = it.getString("ptitle")
            pageContent = it.getString("pcontent")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommonSettingsBinding.inflate(inflater, container, false)

        binding.apply {
            title.text = pageTitle.toString()
            content.text = pageContent.toString()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommonSettingsFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommonSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(pageTitle, param1)
                    putString(pageContent, param2)
                }
            }
    }
}