package com.equipo0.myplantscatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerbolatioTheme {
                HerboApp()
            }
        }
    }
}

@Composable
fun HerboApp() {
    var pantalla by remember { mutableStateOf<HerboDestino>(HerboDestino.Inicio) }

    when (pantalla) {
        HerboDestino.Inicio        -> PantallaInicio(onVerDetalle = { pantalla = HerboDestino.DetallePlanta }, onNavegar = { pantalla = it }, selectedDestino = pantalla)
        HerboDestino.Catalogo      -> CatalogoPlantasScreen(onPlantaClick = { pantalla = HerboDestino.DetallePlanta }, onNavegar = { pantalla = it }, selectedDestino = pantalla)
        HerboDestino.Busqueda      -> BusquedaSintomasScreen(onBack = { pantalla = HerboDestino.Inicio }, onNavegar = { pantalla = it }, selectedDestino = pantalla)
        HerboDestino.DetallePlanta -> DetallePlantaScreen(onBack = { pantalla = HerboDestino.Inicio })
        HerboDestino.Favoritos     -> MisFavoritosScreen(onBack = { pantalla = HerboDestino.Inicio }, onNavegar = { pantalla = it }, selectedDestino = pantalla)
        HerboDestino.Aprender      -> SeccionEducativaScreen(onNavegar = { pantalla = it }, selectedDestino = pantalla)
        HerboDestino.Configuracion -> ConfiguracionScreen(onBack = { pantalla = HerboDestino.Inicio })
    }
}