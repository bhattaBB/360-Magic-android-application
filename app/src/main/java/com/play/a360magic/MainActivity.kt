package com.play.a360magic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.play.a360magic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }




    private fun setListeners(){
        binding.playButton.setOnClickListener {
            if(binding.urlText.text.toString().isNullOrBlank()){
                Toast.makeText(this@MainActivity,"Please enter a valid url to play.",Toast.LENGTH_LONG).show()
            }else{
                 val intent = Intent(this@MainActivity,PlayerViewActivity::class.java)
                intent.putExtra("Url",binding.urlText.text.toString())
                startActivity(intent)
//                binding.urlText.setText("")
            }


        }




    }
}