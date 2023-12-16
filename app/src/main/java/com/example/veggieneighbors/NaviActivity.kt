package com.example.veggieneighbors

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.veggieneighbors.databinding.ActivityNaviBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

private const val TAG_MYFRIDGE = "myfridge_fragment"
private const val TAG_NEARME = "nearme_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_CART = "cart_fragment"
private const val TAG_MYPAGE = "mypage_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bnv_main:BottomNavigationView = binding.navigationView

        bnv_main.run { setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.myfridgeTabBtn -> {
                    val myFridgeFragment = MyfridgeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, myFridgeFragment).commit()
                }
                R.id.nearmeTabBtn -> {
                    val nearmeFragment = NearmeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, nearmeFragment).commit()
                }
                R.id.homeTabBtn -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                }
                R.id.cartTabBtn -> {
                    val cartFragment = CartFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, cartFragment).commit()
                }
                R.id.mypageTabBtn -> {
                    val mypageFragment = MypageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_container, mypageFragment).commit()
                }

            }
            true
        }
            selectedItemId = R.id.homeTabBtn
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "FCM Topic subscription failed.")
                }
            }
        class NaviActivity : AppCompatActivity() {

            private lateinit var binding : ActivityNaviBinding

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNaviBinding.inflate(layoutInflater)
                setContentView(binding.root)

                var bnv_main:BottomNavigationView = binding.navigationView

                bnv_main.run { setOnNavigationItemSelectedListener {
                    when(it.itemId) {
                        R.id.myfridgeTabBtn -> {
                            val myFridgeFragment = MyfridgeFragment()
                            supportFragmentManager.beginTransaction().replace(R.id.fl_container, myFridgeFragment).commit()
                        }
                        R.id.nearmeTabBtn -> {
                            val nearmeFragment = NearmeFragment()
                            supportFragmentManager.beginTransaction().replace(R.id.fl_container, nearmeFragment).commit()
                        }
                        R.id.homeTabBtn -> {
                            val homeFragment = HomeFragment()
                            supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                        }
                        R.id.cartTabBtn -> {
                            val cartFragment = CartFragment()
                            supportFragmentManager.beginTransaction().replace(R.id.fl_container, cartFragment).commit()
                        }
                        R.id.mypageTabBtn -> {
                            val mypageFragment = MypageFragment()
                            supportFragmentManager.beginTransaction().replace(R.id.fl_container, mypageFragment).commit()
                        }

                    }
                    true
                }
                    selectedItemId = R.id.homeTabBtn
                }

                FirebaseMessaging.getInstance().subscribeToTopic("all")
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w("FCM", "FCM Topic subscription failed.")
                        }
                    }


                /*
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                        return@addOnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and show the token
                    Log.d("FCM", "FCM Token: $token")
                    Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_LONG).show()
                }*/
            }
        }

        /*
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and show the token
            Log.d("FCM", "FCM Token: $token")
            Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_LONG).show()
        }*/
    }
    }
