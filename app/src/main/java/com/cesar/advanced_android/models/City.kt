package com.cesar.advanced_android.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val local: String,
    val imagem: String,
    val dias: String,
    val preco: String
): Parcelable