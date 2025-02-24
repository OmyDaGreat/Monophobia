package xyz.malefic.monophobia.utils

import com.varabyte.kobweb.compose.ui.graphics.Color

data class Particle(
    val id: Int,
    var x: Double,
    var y: Double,
    var vx: Double,
    var vy: Double,
    var life: Double,
    var size: Double,
    var opacity: Double = 0.0,
)

data class Quote(
    val text: String,
    val author: String,
)

object Config {
    const val PARTICLE_COUNT = 20
    const val PARTICLE_SIZE = 4.0
    const val PARTICLE_LIFE = 1500
    const val MAX_SPEED = 4.0
    const val MOUSE_RADIUS = 150.0
    const val SWIRL_COUNT = 8
    val color = Color.rgb(131, 37, 65)
}
