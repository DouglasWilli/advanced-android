package com.cesar.advanced_android.util

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.util.zip.ZipFile

class UnzipWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams)  {

    override fun doWork(): Result {
        val zipFileName = File(applicationContext.filesDir, "images.zip")

        ZipFile(zipFileName).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                if (!entry.name.startsWith("__MACOSX/")) {
                    zip.getInputStream(entry).use { input ->
                        File(applicationContext.filesDir, entry.name).outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }

        return Result.success(Data.Builder().putString("zip_path", "/unzip").build())
    }
}