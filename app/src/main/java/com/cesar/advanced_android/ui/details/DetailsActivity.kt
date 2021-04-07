package com.cesar.advanced_android.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.cesar.advanced_android.databinding.ActivityDetailsBinding
import com.cesar.advanced_android.models.City
import java.io.File

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getParcelableExtra<City>("city")

        supportActionBar?.title = city?.local

        binding.apply {
            city?.apply {
                imgCity.setImageURI(File(this@DetailsActivity.filesDir, this.imagem).toUri())
                tvItemDias.text = city.dias
                tvItemPreco.text = city.preco
            }
        }
    }
}