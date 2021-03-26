package com.programo90.promaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.programo90.promaps.databinding.ActivityDirectionBinding

class DirectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDirectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectionBinding.inflate(layoutInflater)

        setContentView(binding.root)


        if(intent.getStringExtra("set") == "origin") {
            binding.directionOriginBox.setText(intent.getStringExtra("origin"))

        } else if(intent.getStringExtra("set") == "destination"){
            binding.directionDestinationBox.setText(intent.getStringExtra("destination"))
        }

        binding.directionCloseBtn.setOnClickListener {
            this.finish()
        }
    }
}