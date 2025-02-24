package xyz.malefic.monophobia.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.dom.HTMLDivElement
import kotlin.js.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private var particleIdCounter: Int = 0

suspend fun fetchQuote(): Quote =
    try {
        window
            .fetch("https://daily.malefic.xyz/quote")
            .then { response ->
                if (response.ok) {
                    response.json()
                } else {
                    throw Exception("Network response was not ok")
                }
            }.then { data ->
                val dynamicData = data.asDynamic()
                console.log("Quote data received:", dynamicData)

                val text = (dynamicData.text as? String)
                val author = (dynamicData.author as? String)

                Quote(
                    text = text ?: "Moment of silence...",
                    author = author?.takeIf { it.isNotEmpty() } ?: "",
                )
            }.await()
    } catch (e: Exception) {
        console.error("Error fetching quote:", e)
        Quote("Moment of silence...", "")
    }

fun updateClock(onUpdate: (String, String) -> Unit) {
    val now = Date()
    val pst =
        Date(
            now.toLocaleString(
                "en-US",
                dateLocaleOptions {
                    timeZone = "America/Los_Angeles"
                },
            ),
        )

    val hours = pst.getHours().toString().padStart(2, '0')
    val minutes = pst.getMinutes().toString().padStart(2, '0')
    val seconds = pst.getSeconds().toString().padStart(2, '0')
    val time = "$hours:$minutes:$seconds"

    val monthNames =
        arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December",
        )

    val year = pst.getFullYear()
    val monthName = monthNames[pst.getMonth()]
    val day = pst.getDate()
    val ordinalSuffix = getOrdinalSuffix(day)
    val date = "$monthName $day$ordinalSuffix, $year"

    onUpdate(time, date)
}

private fun getOrdinalSuffix(day: Int): String {
    val j = day % 10
    val k = day % 100
    return when {
        j == 1 && k != 11 -> "st"
        j == 2 && k != 12 -> "nd"
        j == 3 && k != 13 -> "rd"
        else -> "th"
    }
}

fun createParticle(
    x: Double,
    y: Double,
    particles: SnapshotStateList<Particle>,
) {
    if (particles.size >= Config.PARTICLE_COUNT) particles.removeFirstOrNull()

    val angle = Random.nextDouble() * PI * 2
    val speed = Random.nextDouble() * Config.MAX_SPEED
    val vx = cos(angle) * speed
    val vy = sin(angle) * speed
    val size = Random.nextDouble() * Config.PARTICLE_SIZE + 2

    particles.add(
        Particle(
            id = ++particleIdCounter,
            x = x,
            y = y,
            vx = vx,
            vy = vy,
            life = Config.PARTICLE_LIFE.toDouble(),
            size = size,
            opacity = 0.0,
        ),
    )
}

fun updateParticles(
    particles: SnapshotStateList<Particle>,
    mouseX: Double,
    mouseY: Double,
    delta: Double,
) {
    val iterator = particles.iterator()
    while (iterator.hasNext()) {
        val particle = iterator.next()

        // Scale updates by delta time for smooth motion
        val timeScale = delta.coerceIn(0.0, 2.0)

        // Smooth fade in
        if (particle.opacity < 0.8) {
            particle.opacity += 0.1 * timeScale
        }

        // Update position
        particle.x += particle.vx * timeScale
        particle.y += particle.vy * timeScale
        particle.life -= 16 * timeScale

        // Mouse interaction
        val dx = mouseX - particle.x
        val dy = mouseY - particle.y
        val distance = sqrt(dx * dx + dy * dy)

        if (distance < Config.MOUSE_RADIUS) {
            val force = (Config.MOUSE_RADIUS - distance) / Config.MOUSE_RADIUS * 0.2 * timeScale
            particle.vx += (dx / distance) * force
            particle.vy += (dy / distance) * force
        }

        // Fade out near end of life
        if (particle.life < 300) {
            particle.opacity = (particle.life / 300.0) * 0.8
        }

        // Remove if expired or out of bounds
        if (particle.life <= 0 ||
            particle.x < -50 ||
            particle.x > window.innerWidth + 50 ||
            particle.y < -50 ||
            particle.y > window.innerHeight + 50
        ) {
            iterator.remove()
        }
    }
}

fun createRipple(
    x: Double,
    y: Double,
) {
    val ripple =
        (document.createElement("div") as HTMLDivElement).apply {
            className = "click-ripple"
            style.left = "${x}px"
            style.top = "${y}px"
        }

    document.body?.appendChild(ripple)

    window.setTimeout({
        document.body?.removeChild(ripple)
    }, 1000)
}

fun createSwirlEffect(
    x: Double,
    y: Double,
) {
    repeat(Config.SWIRL_COUNT) { i ->
        val swirl =
            (document.createElement("div") as HTMLDivElement).apply {
                className = "swirl-particle"
                style.left = "${x}px"
                style.top = "${y}px"
                style.width = "4px"
                style.height = "4px"
                style.animationDelay = "${i * (3.0 / Config.SWIRL_COUNT)}s"
            }

        document.getElementById("particle-container")?.appendChild(swirl)

        window.setTimeout({
            document.getElementById("particle-container")?.removeChild(swirl)
        }, 3000)
    }
}

fun clearParticles(particles: SnapshotStateList<Particle>) {
    particles.clear()
}

// Window resize handler
fun setupWindowResizeHandler(particles: SnapshotStateList<Particle>) {
    window.addEventListener("resize", {
        clearParticles(particles)
    })
}
