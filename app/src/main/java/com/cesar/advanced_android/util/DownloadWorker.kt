package com.cesar.advanced_android.util

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private var urlConnection: HttpURLConnection? = null

    override fun doWork(): Result {

        val url = URL(inputData.getString("URL"))

        val result = StringBuilder()

        try {
            urlConnection = url.openConnection() as HttpURLConnection?
            urlConnection?.doInput = true
            urlConnection?.connectTimeout = 20 * 100
            urlConnection?.readTimeout = 20 * 100

            if (urlConnection?.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(urlConnection?.inputStream)
                val reader = BufferedReader(InputStreamReader(stream))

                var line : String?

                do {
                    line = reader.readLine()
                    if (line == null) break
                    result.append(line)
                } while (true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
        }

        val outputData = Data.Builder().putString("response", result.toString()).build()
        Thread.sleep(5000)
        return Result.success(outputData)
    }

}