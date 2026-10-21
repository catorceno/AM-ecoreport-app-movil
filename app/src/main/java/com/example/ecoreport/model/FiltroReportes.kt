package com.example.ecoreport.model

data class FiltroReportes(
    val estado: String? = null,
    val categoria: String? = null
) {
    val estaActivo: Boolean
        get() = estado != null || categoria != null
}
