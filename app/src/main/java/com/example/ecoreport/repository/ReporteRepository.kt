package com.example.ecoreport.repository

import com.example.ecoreport.data.ReporteDao
import com.example.ecoreport.data.ReporteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ReporteRepository (
    private val reporteDao: ReporteDao
) {
    fun obtenerTodos():
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerTodos()
    }
    fun obtenerPorCategoria(
        categoria: String?
    ):
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerPorCategoria(categoria)
    }

    fun obtenerPorEstado(
        estado: String?
    ):
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerPorEstado(estado)
    }

    // Combina ambos filtros: un reporte se muestra solo si cumple Estado Y Categoría.
    // Un filtro en null equivale a "sin filtrar" por ese campo.
    fun filtrar(
        estado: String?,
        categoria: String?
    ):
            Flow<List<ReporteEntity>> {
        return combine(
            obtenerPorEstado(estado),
            obtenerPorCategoria(categoria)
        ) { porEstado, porCategoria ->
            val idsCategoria = porCategoria.map { it.id }.toSet()
            porEstado.filter { it.id in idsCategoria }
        }
    }

    suspend fun insertar(reporte: ReporteEntity){
        reporteDao.insertar(reporte)
    }

    suspend fun actualizar(reporte: ReporteEntity) {
        reporteDao.actualizar(reporte)
    }

    suspend fun eliminar(reporte: ReporteEntity) {
        reporteDao.eliminar(reporte)
    }
}
