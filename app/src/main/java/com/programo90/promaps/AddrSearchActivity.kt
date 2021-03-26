package com.programo90.promaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.programo90.promaps.databinding.ActivityAddrSearchBinding

class AddrSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddrSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddrSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchaddrBackBtn.setOnClickListener {
            finish()
        }

        NGeocoding("고척돔구장").execute()
    }
}