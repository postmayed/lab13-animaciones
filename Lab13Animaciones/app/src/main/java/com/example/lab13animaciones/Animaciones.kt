package com.example.lab13animaciones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* Selector para cambiar entre ejercicios */
@Composable
fun AppAnimaciones() {
    val titulos = listOf("Ej 1", "Ej 2", "Ej 3")
    var pantalla by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            titulos.forEachIndexed { i, t ->
                if (pantalla == i) Button(onClick = { pantalla = i }) { Text(t) }
                else OutlinedButton(onClick = { pantalla = i }) { Text(t) }
            }
        }
        when (pantalla) {
            0 -> Ejercicio1()
            1 -> Ejercicio2()
            else -> Ejercicio3()
        }
    }
}

/* EJERCICIO 1: AnimatedVisibility */
@Composable
fun Ejercicio1() {
    var visible by remember { mutableStateOf(true) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ocultar cuadro" else "Mostrar cuadro")
        }
        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(800)) + expandVertically(tween(800)),
            exit = fadeOut(animationSpec = tween(800)) + shrinkVertically(tween(800))
        ) {
            Box(
                Modifier
                    .size(160.dp)
                    .background(Color(0xFF6200EE), RoundedCornerShape(16.dp))
            )
        }

        Spacer(Modifier.height(16.dp))
        Text("Este texto se reacomoda suavemente", fontSize = 16.sp)
    }
}

/* EJERCICIO 2: animateColorAsState (tween vs spring) */
@Composable
fun Ejercicio2() {
    var esAzul by remember { mutableStateOf(true) }
    var usarSpring by remember { mutableStateOf(false) }

    val color by animateColorAsState(
        targetValue = if (esAzul) Color(0xFF1E88E5) else Color(0xFF43A047),
        animationSpec = if (usarSpring)
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        else
            tween(durationMillis = 1200),
        label = "colorCuadro"
    )

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { esAzul = !esAzul }) { Text("Cambiar color") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { usarSpring = !usarSpring }) {
            Text("Modo actual: " + if (usarSpring) "spring" else "tween")
        }
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier
                .size(180.dp)
                .background(color, RoundedCornerShape(16.dp))
        )
    }
}

/* EJERCICIO 3: animateDpAsState + Modifier.offset */
@Composable
fun Ejercicio3() {
    var activo by remember { mutableStateOf(false) }

    val tamano by animateDpAsState(
        targetValue = if (activo) 110.dp else 60.dp,
        animationSpec = tween(800),
        label = "tamano"
    )
    val x by animateDpAsState(
        targetValue = if (activo) 170.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "x"
    )
    val y by animateDpAsState(
        targetValue = if (activo) 70.dp else 0.dp,
        animationSpec = tween(800),
        label = "y"
    )

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Button(onClick = { activo = !activo }) { Text("Mover y cambiar tamaño") }
        Spacer(Modifier.height(16.dp))

        Text("A) offset ANTES de size y background: se mueve el cuadro completo")
        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            Box(
                Modifier
                    .offset(x, y)
                    .size(tamano)
                    .background(Color(0xFFE53935), RoundedCornerShape(12.dp))
            )
        }

        Text("B) offset DESPUÉS de size y background: el fondo se queda quieto")
        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            Box(
                Modifier
                    .size(tamano)
                    .background(Color(0xFFFB8C00), RoundedCornerShape(12.dp))
                    .offset(x, y)
            )
        }
    }
}