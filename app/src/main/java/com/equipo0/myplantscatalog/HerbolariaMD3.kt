/**
 * Herbolaria de Tabasco — Material Design 3 + Jetpack Compose
 *
 * Pantallas incluidas:
 *  1. PantallaInicio
 *  2. CatalogoPlantasScreen
 *  3. BusquedaSintomasScreen
 *  4. DetallePlantaScreen
 *  5. MisFavoritosScreen
 *  6. SeccionEducativaScreen
 *  7. ConfiguracionScreen
 *
 * Dependencias requeridas en build.gradle.kts (app):
 *   implementation("androidx.compose.material3:material3:<last_version>")
 *   implementation("io.coil-kt:coil-compose:<last_version>")
 *   implementation("androidx.navigation:navigation-compose:<last_version>")
 */
package com.equipo0.myplantscatalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// ─────────────────────────────────────────────────────────────────────────────
// TEMA  (Material Design 3 — color seed verde herbolario)
// ─────────────────────────────────────────────────────────────────────────────

private val HerbolarioGreen = Color(0xFF2F7F33)
private val HerbolarioGreenContainer = Color(0xFFB7F0B8)
private val HerbolarioOnGreen = Color(0xFFFFFFFF)
private val HerbolarioOnGreenContainer = Color(0xFF002107)
private val BackgroundLight = Color(0xFFFDFCF0)
private val SurfaceLight = Color(0xFFFFFFFF)
private val BackgroundDark = Color(0xFF141E15)
private val SurfaceDark = Color(0xFF1E2A1F)

private val LightColorScheme = lightColorScheme(
    primary = HerbolarioGreen,
    onPrimary = HerbolarioOnGreen,
    primaryContainer = HerbolarioGreenContainer,
    onPrimaryContainer = HerbolarioOnGreenContainer,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = Color(0xFFE8F5E9),
    onBackground = Color(0xFF1A1C18),
    onSurface = Color(0xFF1A1C18),
    onSurfaceVariant = Color(0xFF43483F),
    outline = Color(0xFF73796E),
    secondary = Color(0xFF526350),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD5E8D0),
    onSecondaryContainer = Color(0xFF101F10),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9CD49E),
    onPrimary = Color(0xFF003909),
    primaryContainer = Color(0xFF1A5F1F),
    onPrimaryContainer = HerbolarioGreenContainer,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = Color(0xFF2A3A2B),
    onBackground = Color(0xFFE2E3DC),
    onSurface = Color(0xFFE2E3DC),
    onSurfaceVariant = Color(0xFFC3C8BC),
    outline = Color(0xFF8D9287),
    secondary = Color(0xFFB9CCB5),
    onSecondary = Color(0xFF263424),
    secondaryContainer = Color(0xFF3C4B3A),
    onSecondaryContainer = Color(0xFFD5E8D0),
)

@Composable
fun HerbolatioTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography(), // usa la tipografía M3 por defecto
        content = content
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// NAVEGACIÓN
// ─────────────────────────────────────────────────────────────────────────────

enum class HerboDestino { Inicio, Catalogo, Busqueda, Favoritos, Aprender, DetallePlanta, Configuracion }

data class NavItem(val label: String, val icon: ImageVector, val destino: HerboDestino)

val bottomNavItems = listOf(
    NavItem("Inicio",    Icons.Outlined.Home,        HerboDestino.Inicio),
    NavItem("Catálogo",  Icons.Outlined.MenuBook,    HerboDestino.Catalogo),
    NavItem("Buscar",    Icons.Outlined.Search,      HerboDestino.Busqueda),
    NavItem("Favoritos", Icons.Outlined.FavoriteBorder, HerboDestino.Favoritos),
    NavItem("Aprender",  Icons.Outlined.School,      HerboDestino.Aprender),
)

// ─────────────────────────────────────────────────────────────────────────────
// 1. PANTALLA DE INICIO
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    onVerDetalle: () -> Unit = {},
    onNavegar: (HerboDestino) -> Unit = {},
    selectedDestino: HerboDestino = HerboDestino.Inicio
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "¡Hola, bienvenido!",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Herbolaria de Tabasco",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(onClick = { onNavegar(HerboDestino.Configuracion) }) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = { HerboBottomBar(selected = selectedDestino, onNavegar = onNavegar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }

            // Sección: Recomendado
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recomendado para ti",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = {}) {
                        Text("Ver todo", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                PlantaDestacadaCard(
                    nombre = "Maguey Morado",
                    descripcion = "Tradicionalmente usado en Tabasco para aliviar inflamaciones y desinfectar heridas leves.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBM7UafMXxTAvcfHcwAjus_BXb_uxBQ5Q2ZshC1uuIyCmOlj-rC3TR4pJf-GhwRjXxRmXx7GQlbaEbFi3WzjhEiPyxLYNjmu8P_aou8QmzQDcBp6PEHRs2WFnyfkM4qENViNxgw8CH5etn10blmVKp6UqJyk9QUQYL2rLXCvfJZy7C6vorXTppTIKiRczE317sjXVG_9i8c0DL-zpplbg_suiljFkRZAvpki8_tFBhUzQiioV1Q6yj4lpi0io7-Zbe6ocNdeYMEm8k",
                    onVerMas = onVerDetalle
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            // Sección: ¿Cómo te sientes?
            item {
                Text(
                    "¿Cómo te sientes hoy?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            val sintomas = listOf(
                Triple(Icons.Outlined.MedicalServices, Color(0xFF1565C0), "Tos y Resfriado" to "Té de saúco, bugambilia..."),
                Triple(Icons.Outlined.Restaurant,      Color(0xFFE65100), "Dolor de estómago" to "Hierbabuena, manzanilla..."),
                Triple(Icons.Outlined.Bedtime,         Color(0xFF6A1B9A), "No puedo dormir" to "Toronjil, valeriana..."),
            )
            items(sintomas) { (icon, tint, textos) ->
                SintomaCard(
                    icon = icon,
                    iconTint = tint,
                    titulo = textos.first,
                    subtitulo = textos.second,
                    onClick = { onNavegar(HerboDestino.Busqueda) }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PlantaDestacadaCard(
    nombre: String,
    descripcion: String,
    imageUrl: String,
    onVerMas: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                FilledTonalIconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Column(Modifier.padding(16.dp)) {
                Text(nombre, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(descripcion, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onVerMas, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Visibility, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Leer más sobre esta planta", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun SintomaCard(
    icon: ImageVector,
    iconTint: Color,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
                }
            }
            Column {
                Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. CATÁLOGO DE PLANTAS
// ─────────────────────────────────────────────────────────────────────────────

data class Planta(
    val nombre: String,
    val familia: String,
    val etiqueta: String,
    val imageUrl: String
)

val plantasDemo = listOf(
    Planta("Maguey Morado", "Asparagaceae", "Antiinflamatorio", "https://lh3.googleusercontent.com/aida-public/AB6AXuBM7UafMXxTAvcfHcwAjus_BXb_uxBQ5Q2ZshC1uuIyCmOlj-rC3TR4pJf-GhwRjXxRmXx7GQlbaEbFi3WzjhEiPyxLYNjmu8P_aou8QmzQDcBp6PEHRs2WFnyfkM4qENViNxgw8CH5etn10blmVKp6UqJyk9QUQYL2rLXCvfJZy7C6vorXTppTIKiRczE317sjXVG_9i8c0DL-zpplbg_suiljFkRZAvpki8_tFBhUzQiioV1Q6yj4lpi0io7-Zbe6ocNdeYMEm8k"),
    Planta("Hierbabuena",   "Lamiaceae",    "Digestivo",        "https://images.unsplash.com/photo-1628556270448-4d4e4148e1b1?w=400"),
    Planta("Manzanilla",    "Asteraceae",   "Relajante",        "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400"),
    Planta("Valeriana",     "Caprifoliaceae","Sedante",          "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400"),
)

val categoriasDemo = listOf("Todas", "Digestivas", "Respiratorias", "Nervio", "Piel")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoPlantasScreen(
    onPlantaClick: () -> Unit = {},
    onNavegar: (HerboDestino) -> Unit = {},
    selectedDestino: HerboDestino = HerboDestino.Catalogo
) {
    var query by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todas") }

    Scaffold(
        topBar = {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Spa, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(22.dp))
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("Herbolario Digital", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    }
                    FilledTonalIconButton(onClick = { onNavegar(HerboDestino.Configuracion) }) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Perfil")
                    }
                }
                Spacer(Modifier.height(12.dp))
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {},
                    active = false,
                    onActiveChange = {},
                    placeholder = { Text("Buscar planta medicinal...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = {}) { Icon(Icons.Outlined.Tune, contentDescription = "Filtros") }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {}
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categoriasDemo) { cat ->
                        FilterChip(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        bottomBar = { HerboBottomBar(selected = selectedDestino, onNavegar = onNavegar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }
            items(plantasDemo) { planta ->
                PlantaCatalogoCard(planta = planta, onClick = onPlantaClick)
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun PlantaCatalogoCard(planta: Planta, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = planta.imageUrl,
                contentDescription = planta.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
            )
            Column(Modifier.weight(1f).align(Alignment.CenterVertically)) {
                Text(planta.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(planta.familia, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(planta.etiqueta, style = MaterialTheme.typography.labelSmall) }
                )
            }
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. BÚSQUEDA POR SÍNTOMAS
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaSintomasScreen(
    onBack: () -> Unit = {},
    onNavegar: (HerboDestino) -> Unit = {},
    selectedDestino: HerboDestino = HerboDestino.Busqueda
) {
    var query by remember { mutableStateOf("") }
    val sugerencias = listOf("Tos", "Insomnio", "Fiebre", "Inflamación", "Estrés")
    val sintomas = listOf(
        Triple(Icons.Outlined.MedicalServices, Color(0xFF1565C0), "Tos y Resfriado"),
        Triple(Icons.Outlined.Restaurant,      Color(0xFFE65100), "Dolor de estómago"),
        Triple(Icons.Outlined.Bedtime,         Color(0xFF6A1B9A), "No puedo dormir"),
        Triple(Icons.Outlined.SelfImprovement, Color(0xFF00695C), "Estrés y ansiedad"),
        Triple(Icons.Outlined.Healing,         Color(0xFFC62828), "Dolor de cabeza"),
        Triple(Icons.Outlined.Thermostat,      Color(0xFFAD1457), "Fiebre"),
    )

    Scaffold(
        topBar = {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                    Text(
                        "Explorar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.width(48.dp))
                }
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Remedios, plantas, síntomas...") },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                Text(
                    "Sugerencias",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sugerencias) { s ->
                        SuggestionChip(onClick = { query = s }, label = { Text(s) })
                    }
                }
            }
        },
        bottomBar = { HerboBottomBar(selected = selectedDestino, onNavegar = onNavegar) }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "¿Qué síntoma tienes?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(sintomas) { (icon, tint, titulo) ->
                SintomaCard(icon = icon, iconTint = tint, titulo = titulo, subtitulo = "Ver plantas recomendadas", onClick = {})
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. DETALLE DE LA PLANTA
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DetallePlantaScreen(onBack: () -> Unit = {}) {
    val scrollState = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero image
            Box(Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuApjZesi_V6wpPJ5i1Bunitt1BQ5taDmweccmpvw6A5UrdSHjNWtjVgC_SuJA8RLzMuNbwtvlCf_WsuyWTJxopy2MEnJQMe-J89iuM86CqbNsFQtUrlHC3wx_h-AWa1UGM5yqoFfdL9n6AjG4yv7V1TRE3GWffxJW0siL0jmhMl_v9fTEcPUhinMHun6rh12v14WVbmW43iIRBcW72e0jSPHOA6ejEh29wnr0I39fOgRgylj5XsGhFeIiT3gkbnQqImoiZ0e-H0LN8",
                    contentDescription = "Maguey Morado",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient overlay top
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)))
                )
                // Gradient overlay bottom
                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.White)))
                )
            }

            // Content
            Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(Modifier.weight(1f)) {
                        Text("Maguey Morado", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
                        Text("Tradescantia spathacea", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontStyle = FontStyle.Italic)
                    }
                    FilledTonalIconButton(onClick = {}) {
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(Modifier.height(24.dp))

                DetalleSeccion(
                    icono = Icons.Outlined.MedicalServices,
                    titulo = "Usos Medicinales",
                    contenido = "Tradicionalmente utilizado por sus propiedades antiinflamatorias y antisépticas. Excelente para tratar afecciones respiratorias como la tos y el asma, además de ayudar en la cicatrización de heridas superficiales."
                )

                Spacer(Modifier.height(16.dp))

                DetalleSeccion(
                    icono = Icons.Outlined.LocalFireDepartment,
                    titulo = "Preparación",
                    contenido = "Hervir 3 hojas frescas en 1 litro de agua durante 10 minutos. Colar y consumir tibio, 2-3 veces al día. Para uso externo, aplicar directamente el jugo de la hoja sobre la zona afectada."
                )

                Spacer(Modifier.height(16.dp))

                DetalleSeccion(
                    icono = Icons.Outlined.Warning,
                    titulo = "Precauciones",
                    contenido = "Evitar durante el embarazo. No exceder la dosis recomendada. Consultar con un profesional de salud antes de usar en niños menores de 5 años."
                )

                Spacer(Modifier.height(24.dp))

                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.Share, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Compartir información")
                }
                Spacer(Modifier.height(32.dp))
            }
        }

        // Floating back & share buttons over image
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledTonalIconButton(
                onClick = onBack,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White)
            }
            FilledTonalIconButton(
                onClick = {},
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.3f)
                )
            ) {
                Icon(Icons.Outlined.Share, contentDescription = "Compartir", tint = Color.White)
            }
        }
    }
}

@Composable
private fun DetalleSeccion(icono: ImageVector, titulo: String, contenido: String) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                contenido,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. MIS FAVORITOS
// ─────────────────────────────────────────────────────────────────────────────

data class PlantaGuardada(val nombre: String, val categoria: String, val estado: String, val imageUrl: String)

val favoritosDemo = listOf(
    PlantaGuardada("Monstera Deliciosa", "Interior • Luz indirecta", "Saludable",   "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=200"),
    PlantaGuardada("Bugambilia",         "Exterior • Pleno sol",     "Medicinal",   "https://images.unsplash.com/photo-1599598425947-5202edd56bdc?w=200"),
    PlantaGuardada("Aloe Vera",          "Interior/Exterior",        "Cicatrizante","https://images.unsplash.com/photo-1551893665-f843f600794e?w=200"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisFavoritosScreen(
    onBack: () -> Unit = {},
    onNavegar: (HerboDestino) -> Unit = {},
    selectedDestino: HerboDestino = HerboDestino.Favoritos
) {
    val plantas = remember { mutableStateListOf(*favoritosDemo.toTypedArray()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mis Plantas Guardadas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Text("${plantas.size} plantas en tu colección", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, contentDescription = "Más opciones") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = { HerboBottomBar(selected = selectedDestino, onNavegar = onNavegar) }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }
            items(plantas, key = { it.nombre }) { planta ->
                FavoritaCard(
                    planta = planta,
                    onEliminar = { plantas.remove(planta) }
                )
            }
            if (plantas.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                            Spacer(Modifier.height(12.dp))
                            Text("No tienes plantas guardadas", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun FavoritaCard(planta: PlantaGuardada, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = planta.imageUrl,
                contentDescription = planta.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(88.dp).clip(RoundedCornerShape(12.dp))
            )
            Column(Modifier.weight(1f)) {
                Text(planta.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(planta.categoria, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(onClick = {}, label = { Text(planta.estado, style = MaterialTheme.typography.labelSmall) })
                    TextButton(onClick = onEliminar, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                        Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Eliminar", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. SECCIÓN EDUCATIVA
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeccionEducativaScreen(
    onNavegar: (HerboDestino) -> Unit = {},
    selectedDestino: HerboDestino = HerboDestino.Aprender
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aprender", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Menu, contentDescription = "Menú") }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Outlined.Search, contentDescription = "Buscar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = { HerboBottomBar(selected = selectedDestino, onNavegar = onNavegar) }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Hero header
            item {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                    Text(
                        "Cultura y Tradición",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Sabiduría ancestral\nen tus manos",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = MaterialTheme.typography.displaySmall.lineHeight
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Aprende sobre el poder curativo de las plantas medicinales de Tabasco, transmitidas de generación en generación.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Artículo destacado
            item {
                ArticuloCard(
                    titulo = "El arte de las infusiones",
                    descripcion = "Descubre cómo preparar infusiones medicinales efectivas con plantas locales de Tabasco.",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBVq5mKpxRlJKzHz8TrfnM8AL7gOpQQlJdOHY3TzU-kam-Sk5PoHysTD4ZJshu9MwKY46JIJL0Ao0M6bo06-R0q0Ex_0HkKq-0wgQqCj0yDIEGdhnJd46kAECpJ8jLSp3I4V63OSHIMjoUJanHDLSncBiJqDbJD5G9FsgGMysYo5kQmIZNNVmtAjkGuKSq2S6NZqEPtZYn4onSZxPTCL95n2NqUtRP28A02V4aj8cOEP_3RdDVOb5AuJLAXDrk48lNbcN9IVHV_EzA",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                ArticuloCard(
                    titulo = "Historia de la medicina tradicional",
                    descripcion = "Un recorrido por los conocimientos medicinales de los pueblos originarios de Tabasco.",
                    imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=600",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ArticuloCard(titulo: String, descripcion: String, imageUrl: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
            )
            Column(Modifier.padding(20.dp)) {
                Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(descripcion, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.MenuBook, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Leer artículo completo")
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 7. CONFIGURACIÓN
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(onBack: () -> Unit = {}) {
    var modoOffline by remember { mutableStateOf(false) }
    var notificaciones by remember { mutableStateOf(true) }
    var modoOscuro by remember { mutableStateOf(false) }
    var textoGrande by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Perfil
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCuVeUQAq7MQ4hfi-TtAAbBgMSlSPYY6S6H4zdmcwjcFJjpgk7GGN0Mx0pMphYDys2VYHS4SEfDnAeb-A3P5888CdSife-df9MUlbJ1dkbevsSLCk1HRbVUKrPvIILAkyb5pDhjBAEBZyNkSnUAHNMgOUNVshT5GtsBF-HeTcph0z08T-45CY4A_c8x5w8OPSh7pPM-o2BPTNUJuz9L1w6BLnrVajqXfaeBwItcaxrh5xMydSBOpPyFiVRHkhx0jOmGfMKmsP6qa8Q",
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp).clip(CircleShape)
                        )
                        Column {
                            Text("Juan Pérez", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Premium Member", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            item {
                Text(
                    "Preferencias del Sistema",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }

            item {
                ConfigSwitch(
                    icono = Icons.Outlined.CloudOff,
                    titulo = "Modo Offline",
                    subtitulo = "Usar sin conexión a internet",
                    checked = modoOffline,
                    onCheckedChange = { modoOffline = it }
                )
            }
            item {
                ConfigSwitch(
                    icono = Icons.Outlined.Notifications,
                    titulo = "Notificaciones",
                    subtitulo = "Recibir recordatorios de plantas",
                    checked = notificaciones,
                    onCheckedChange = { notificaciones = it }
                )
            }
            item {
                ConfigSwitch(
                    icono = Icons.Outlined.DarkMode,
                    titulo = "Modo Oscuro",
                    subtitulo = "Tema oscuro para la aplicación",
                    checked = modoOscuro,
                    onCheckedChange = { modoOscuro = it }
                )
            }
            item {
                ConfigSwitch(
                    icono = Icons.Outlined.TextFields,
                    titulo = "Texto Grande",
                    subtitulo = "Mayor tamaño de letra para leer mejor",
                    checked = textoGrande,
                    onCheckedChange = { textoGrande = it }
                )
            }

            item {
                Text(
                    "Acerca de",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp, start = 4.dp)
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Versión de la app", fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("1.0.0") },
                    leadingContent = {
                        Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(22.dp))
                            }
                        }
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                )
            }

            item {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Icon(Icons.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar sesión")
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ConfigSwitch(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(titulo, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitulo, style = MaterialTheme.typography.bodySmall) },
        leadingContent = {
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(22.dp))
                }
            }
        },
        trailingContent = { Switch(checked = checked, onCheckedChange = onCheckedChange) },
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// BOTTOM NAVIGATION BAR COMPARTIDO
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HerboBottomBar(
    selected: HerboDestino,
    onNavegar: (HerboDestino) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected == item.destino) item.icon.toFilled() else item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                selected = selected == item.destino,
                onClick = { onNavegar(item.destino) }
            )
        }
    }
}

// Helper para icon filled (en un proyecto real usarías el set de iconos filled directamente)
private fun ImageVector.toFilled(): ImageVector = this

// ─────────────────────────────────────────────────────────────────────────────
// ACTIVITY / ENTRY POINT
// ─────────────────────────────────────────────────────────────────────────────

/*
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
*/
