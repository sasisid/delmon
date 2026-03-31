package com.app.delmon.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.app.delmon.R
import com.app.delmon.databinding.FragmentProfileBinding
import com.app.delmon.databinding.FragmentWebAppsBinding


class WebAppsFragment : Fragment() {

    private lateinit var binding: FragmentWebAppsBinding
    var url =""
    var title =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url","")
            title = it.getString("ptitle","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWebAppsBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.pageTitle.text = title
        val mWebView = binding.webview

        val webSettings = mWebView.settings
        webSettings.javaScriptEnabled = true
        mWebView.loadUrl(url)
        mWebView.webViewClient = WebViewClient()
        WebView.setWebContentsDebuggingEnabled(false)

        // Inflate the layout for this fragment
        return binding.root
    }



}