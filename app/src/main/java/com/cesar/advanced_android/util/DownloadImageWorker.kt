package com.cesar.advanced_android.util

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class DownloadImageWorker(context: Context, workerParams: WorkerParameters) :
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

                val imagesZIP = File(applicationContext.filesDir, "images.zip")
                imagesZIP.createNewFile()

                val out = FileOutputStream(imagesZIP)
                copy(stream, out, 1024)
                out.close()

            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
        }

        val outputData = Data.Builder().putString("response", result.toString()).build()

        return Result.success(outputData)
    }

}

@Throws(IOException::class)
fun copy(input: InputStream, output: OutputStream, bufferSize: Int) {
    val buf = ByteArray(bufferSize)
    var n = input.read(buf)
    while (n >= 0) {
        output.write(buf, 0, n)
        n = input.read(buf)
    }
    output.flush()
}
