package com.app.delmon.activity

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.app.delmon.R
import com.app.delmon.app.AppController
import com.app.delmon.databinding.ActivityMainBinding
import com.app.delmon.utils.Constants
import com.mastercard.gateway.android.sdk.GatewayRegion
import com.mastercard.gateway.android.sdk.GatewaySDK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.emvco.threeds.core.ui.UiCustomization


class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false

    companion object {
        var isloggedIn = false ;
        var languageSelected = "en"
    }

    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding.bottomBar
        navController = findNavController(R.id.main_fragment)

        if(Constants.User.apptype==1){
            navController.setGraph(R.navigation.nav_graph)
        }else{
            navController.setGraph(R.navigation.nav_graph2)
        }
//        navController.enableOnBackPressed(true)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("TAG", "onCreate:${destination.id} $destination ")
            if (destination.id == R.id.home_fragment || destination.id == R.id.feedingFragment || destination.id == R.id.receipes_fragment || destination.id == R.id.order_fragment || destination.id == R.id.profile_fragment){
                binding.bottomBar.visibility = View.VISIBLE
            }else{
                binding.bottomBar.visibility = View.GONE
            }
             theme.applyStyle(R.style.Theme_Delmon, false)

//            WindowCompat.setDecorFitsSystemWindows(window, false)
//            if (destination.id == R.id.type_of_account_fragment){
//                theme.applyStyle(R.style.Theme_lightStatusbar, true)
//                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark
//            }else{
//                theme.applyStyle(R.style.Theme_Delmon, false)
//            }

        }


        setupSmoothBottomMenu()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.menu_bottom)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)

        binding.bottomBar.onItemSelected = { position ->
            val destinationId = when (position) {
                0 -> R.id.home_fragment
                1 -> R.id.order_fragment
                2 -> R.id.receipes_fragment
                3 -> R.id.cart_fragment
                4 -> R.id.profile_fragment
                else -> R.id.home_fragment
            }

            if (destinationId == R.id.profile_fragment) {
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(false)
                    .setPopUpTo(navController.graph.startDestinationId, false, true)
                    .build()
                navController.navigate(destinationId, null, navOptions)
            } else {
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(navController.graph.startDestinationId, false, true)
                    .build()
                navController.navigate(destinationId, null, navOptions)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

   /* override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_fragment)
        Log.d("TAG", "onBackPressed: ${fragment!!}")
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }*/

//        super.onBackPressed()
//    }



}