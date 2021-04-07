package com.cesar.advanced_android.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.cesar.advanced_android.databinding.ActivityMainBinding
import com.cesar.advanced_android.models.City
import com.cesar.advanced_android.models.Packages
import com.cesar.advanced_android.ui.adapter.CityAdapter
import com.cesar.advanced_android.util.DownloadImageWorker
import com.cesar.advanced_android.util.DownloadWorker
import com.cesar.advanced_android.util.UnzipWorker
import com.cesar.advanced_android.R
import com.cesar.advanced_android.ui.MapActivity
import com.cesar.advanced_android.ui.details.DetailsActivity

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cityAdapter by lazy {
        CityAdapter { city ->
            Intent(this, DetailsActivity::class.java).apply {
                putExtra("city", city)
                startActivity(this)
            }
        }
    }

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val oneTimeDownload = OneTimeWorkRequest
        .Builder(DownloadWorker::class.java)
        .setConstraints(constraints)
        .setInputData(
            Data.Builder().putString(
                "URL",
                "https://raw.githubusercontent.com/haldny/imagens/main/pacotes.json"
            ).build()
        )
        .build()

    private val oneTimeDownloadImages = OneTimeWorkRequest
        .Builder(DownloadImageWorker::class.java)
        .setConstraints(constraints)
        .setInputData(
            Data.Builder()
                .putString("URL", "https://github.com/haldny/imagens/raw/main/cidades.zip").build()
        )
        .build()

    private val unzipWorker = OneTimeWorkRequest.Builder(UnzipWorker::class.java)
        .setConstraints(constraints)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        initUi()
        setUpWorkManagers()
    }

    private fun initUi() {
        binding.rvCities.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cityAdapter
        }
    }

    private fun listCities(cities: ArrayList<City>) {
        cityAdapter.submitList(cities)
        binding.progressBar.visibility = View.GONE
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = "MY_NOTIFICATION_CHANNEL"
            val name = "WorkManagerApplication"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val descriptionText = "Hello Android"

            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setUpWorkManagers() {
        WorkManager.getInstance(this).apply {

            getWorkInfoByIdLiveData(oneTimeDownloadImages.id)
                .observe(this@MainActivity, {
                    when (it.state.isFinished) {
                        true -> {
                            val builder = NotificationCompat.Builder(
                                this@MainActivity,
                                "MY_NOTIFICATION_CHANNEL"
                            )
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Status do app")
                                .setContentText("Dados baixados")
                            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                                4,
                                builder.build()
                            )
                        }
                        else -> {
                            val builder = NotificationCompat.Builder(
                                this@MainActivity,
                                "MY_NOTIFICATION_CHANNEL"
                            )
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Status do app")
                                .setContentText("Baixando dados")
                            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                                3,
                                builder.build()
                            )
                        }
                    }
                })

            getWorkInfoByIdLiveData(oneTimeDownload.id)
                .observe(this@MainActivity, {
                    when (it.state.isFinished) {
                        true -> {
                            val gson = Gson()
                            val data = it.outputData.getString("response")
                            val pacotes =
                                gson.fromJson<Packages>(data, object : TypeToken<Packages>() {}.type)
                            listCities(pacotes.pacotes)

                            val builder = NotificationCompat.Builder(
                                this@MainActivity,
                                "MY_NOTIFICATION_CHANNEL"
                            )
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Status do app")
                                .setContentText("Imagens baixadas")
                            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                                2,
                                builder.build()
                            )
                        }
                        else -> {
                            val builder = NotificationCompat.Builder(
                                this@MainActivity,
                                "MY_NOTIFICATION_CHANNEL"
                            )
                                .setSmallIcon(R.drawable.ic_notification)
                                .setContentTitle("Status do app")
                                .setContentText("Baixando imagens")
                            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
                                1,
                                builder.build()
                            )
                        }
                    }
                })

            beginWith(oneTimeDownloadImages)
                .then(unzipWorker)
                .then(oneTimeDownload)
                .enqueue()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.btnMaps -> {
            Intent(this, MapActivity::class.java).apply {
                startActivity(this)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}