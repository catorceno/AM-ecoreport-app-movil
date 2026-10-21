package com.example.ecoreport.ui_comp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.model.EstadoReporte
import com.example.ecoreport.model.FiltroReportes

// Opciones del select: valor guardado en BD (null = todos) y texto visible
private val opcionesEstado: List<Pair<String?, String>> = listOf(
    null to "Todos",
    EstadoReporte.PENDIENTE.name to "Pendiente",
    EstadoReporte.EN_PROCESO.name to "En proceso",
    EstadoReporte.RESUELTO.name to "Resuelto"
)

@Composable
fun PantallaReportes(
    reportes: List<ReporteEntity>,
    filtroAplicado: FiltroReportes,
    buscar: (estado: String?, categoria: String?) -> Unit,
    seleccionarReporte: (ReporteEntity) -> Unit,
    volver: () -> Unit
) {

    // Valores que el usuario está editando (no filtran hasta presionar BUSCAR)
    var estadoElegido by rememberSaveable {
        mutableStateOf(filtroAplicado.estado)
    }

    var categoriaTexto by rememberSaveable {
        mutableStateOf(filtroAplicado.categoria ?: "")
    }

    var menuEstadoAbierto by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Estado (select)
        Box {

            OutlinedButton(
                onClick = {
                    menuEstadoAbierto = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                val etiquetaEstado = opcionesEstado
                    .firstOrNull { it.first == estadoElegido }
                    ?.second ?: "Todos"

                Text(
                    text = "Estado: $etiquetaEstado  ▼"
                )
            }

            DropdownMenu(
                expanded = menuEstadoAbierto,
                onDismissRequest = {
                    menuEstadoAbierto = false
                }
            ) {

                opcionesEstado.forEach { (valor, etiqueta) ->

                    DropdownMenuItem(
                        text = {
                            Text(text = etiqueta)
                        },
                        onClick = {
                            estadoElegido = valor
                            menuEstadoAbierto = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // Categoría (input)
        OutlinedTextField(
            value = categoriaTexto,
            onValueChange = {
                categoriaTexto = it
            },
            label = {
                Text("Categoría")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // Buscar (button)
        Button(
            onClick = {
                buscar(
                    estadoElegido,
                    categoriaTexto
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "BUSCAR"
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (reportes.isEmpty()) {

            Text(
                text = if (filtroAplicado.estaActivo) {
                    "No hay reportes que coincidan con la búsqueda."
                } else {
                    "No existen reportes registrados."
                }
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = reportes,
                    key = { reporte ->
                        reporte.id
                    }
                ) { reporte ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },

                        shape = RoundedCornerShape(16.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Categoría: ${reporte.categoria}"
                            )

                            Text(
                                text = "Ubicación: ${reporte.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${reporte.estado}"
                            )

                            Text(
                                text = "Fecha: ${reporte.fecha}"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "VOLVER"
            )
        }
    }
}