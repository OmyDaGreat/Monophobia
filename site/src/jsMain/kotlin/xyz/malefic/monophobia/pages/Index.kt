@file:Suppress("ktlint:standard:no-wildcard-imports")

package xyz.malefic.monophobia.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.toModifier
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Text
import xyz.malefic.monophobia.utils.BackgroundImageStyle
import xyz.malefic.monophobia.utils.Config
import xyz.malefic.monophobia.utils.InfoContainerStyle
import xyz.malefic.monophobia.utils.Particle
import xyz.malefic.monophobia.utils.ParticleStyle
import xyz.malefic.monophobia.utils.PopupOverlayStyle
import xyz.malefic.monophobia.utils.Quote
import xyz.malefic.monophobia.utils.QuoteContainerStyle
import xyz.malefic.monophobia.utils.WelcomePopupStyle
import xyz.malefic.monophobia.utils.createParticle
import xyz.malefic.monophobia.utils.createRipple
import xyz.malefic.monophobia.utils.createSwirlEffect
import xyz.malefic.monophobia.utils.fetchQuote
import xyz.malefic.monophobia.utils.setupWindowResizeHandler
import xyz.malefic.monophobia.utils.updateParticles
import kotlin.js.Date
import kotlin.random.Random

@Page
@Composable
fun HomePage() {
    var mouseX by remember { mutableStateOf(0.0) }
    var mouseY by remember { mutableStateOf(0.0) }
    var showWelcomePopup by remember { mutableStateOf(true) }
    var currentQuote by remember { mutableStateOf(Quote("Moment of silence...", "")) }
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }
    val particles = remember { mutableStateListOf<Particle>() }
    var lastParticleCreation by remember { mutableStateOf(0.0) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Initial quote fetch
        currentQuote = fetchQuote()

        // Welcome popup
        launch {
            delay(3000)
            showWelcomePopup = false
        }

        // Single update loop for everything
        scope.launch {
            var lastTime = Date().getTime()

            while (true) {
                val currentTimestamp = Date().getTime()
                val delta = (currentTimestamp - lastTime) / 16.0
                lastTime = currentTimestamp

                // Update PST time
                val now = Date()
                // Adjust for PST (-8 hours or -7 for PDT)
                val pstHours = (now.getUTCHours() - 8 + 24) % 24

                // Time in HH:MM:SS format
                currentTime =
                    buildString {
                        append(pstHours.toString().padStart(2, '0'))
                        append(":")
                        append(now.getUTCMinutes().toString().padStart(2, '0'))
                        append(":")
                        append(now.getUTCSeconds().toString().padStart(2, '0'))
                    }

                // Date with ordinal (e.g., February 23rd, 2025)
                val months =
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
                val day = now.getUTCDate()
                val ordinal =
                    when {
                        day % 10 == 1 && day != 11 -> "st"
                        day % 10 == 2 && day != 12 -> "nd"
                        day % 10 == 3 && day != 13 -> "rd"
                        else -> "th"
                    }

                currentDate = "${months[now.getUTCMonth()]} ${day}$ordinal, ${now.getUTCFullYear()}"

                // Update particles with delta time
                if (!showWelcomePopup) {
                    updateParticles(particles, mouseX, mouseY, delta)
                }

                // Random swirl effects when not active
                if (!showWelcomePopup &&
                    !document.getElementById("background")?.classList?.contains("active")!!
                ) {
                    val x = Random.nextDouble() * window.innerWidth
                    val y = Random.nextDouble() * window.innerHeight
                    createSwirlEffect(x, y)
                }

                delay(16)
            }
        }

        // Update quote every 30 minutes
        scope.launch {
            while (true) {
                currentQuote = fetchQuote()
                delay(30 * 60 * 1000)
            }
        }

        // Setup window resize handler
        setupWindowResizeHandler(particles)
    }

    Box(Modifier.fillMaxSize().background(Color.black)) {
        // Background Image
        Box(
            BackgroundImageStyle
                .toModifier()
                .id("background")
                .fillMaxSize()
                .position(Position.Fixed)
                .zIndex(1)
                .backgroundImage(url("https://gallery.malefic.xyz/photos/aceofhearts/MonophobiaBackground.png"))
                .backgroundSize(BackgroundSize.Cover)
                .backgroundPosition(BackgroundPosition.of(CSSPosition.Center)),
        )

        // Particle Container
        Box(
            Modifier
                .id("particle-container")
                .fillMaxSize()
                .position(Position.Fixed)
                .zIndex(2),
        ) {
            particles.forEach { particle ->
                key(particle.id) {
                    Box(
                        ParticleStyle
                            .toModifier()
                            .position(Position.Absolute)
                            .left(particle.x.px)
                            .top(particle.y.px)
                            .width(particle.size.px)
                            .height(particle.size.px)
                            .opacity(particle.opacity)
                            .boxShadow(BoxShadow.of(0.px, 0.px, 10.px, color = Config.color))
                            .background(Config.color),
                    )
                }
            }
        }

        // Info Container (Clock and Date)
        Box(
            InfoContainerStyle.toModifier(),
        ) {
            Box(
                Modifier
                    .fontSize(22.px)
                    .fontWeight(300)
                    .letterSpacing(1.px),
            ) {
                Text(currentTime)
            }
            Box(
                Modifier
                    .fontSize(18.px)
                    .opacity(0.9)
                    .fontWeight(300)
                    .letterSpacing(0.5.px),
            ) {
                Text(currentDate)
            }
        }

        // Quote Container
        Box(
            QuoteContainerStyle.toModifier(),
        ) {
            Box(
                Modifier
                    .fontSize(22.px)
                    .fontWeight(300)
                    .letterSpacing(1.px),
            ) {
                Text(currentQuote.text)
            }
            if (currentQuote.author.isNotEmpty()) {
                Box(
                    Modifier
                        .fontSize(18.px)
                        .opacity(0.9)
                        .fontWeight(300)
                        .letterSpacing(0.5.px),
                ) {
                    Text(currentQuote.author)
                }
            }
        }

        // Welcome Popup
        if (showWelcomePopup) {
            Box(
                PopupOverlayStyle.toModifier(),
            )
            Box(
                WelcomePopupStyle
                    .toModifier()
                    .opacity(1)
                    .transform {
                        translate((-50).percent, (-50).percent)
                        scale(1)
                    },
            ) {
                Text("Welcome Malefic")
            }
        }
    }

    // Mouse Event Handler Layer
    if (!showWelcomePopup) {
        Box(
            Modifier
                .fillMaxSize()
                .position(Position.Fixed)
                .zIndex(1000)
                .onMouseMove { event ->
                    mouseX = event.clientX.toDouble()
                    mouseY = event.clientY.toDouble()
                    document.getElementById("background")?.classList?.add("active")

                    // Throttle particle creation
                    val now = Date().getTime()
                    if (now - lastParticleCreation > 16) {
                        createParticle(mouseX, mouseY, particles)
                        lastParticleCreation = now
                    }
                }.onMouseOut {
                    document.getElementById("background")?.classList?.remove("active")
                }.onClick { event ->
                    createRipple(event.clientX.toDouble(), event.clientY.toDouble())
                    createSwirlEffect(event.clientX.toDouble(), event.clientY.toDouble())
                },
        )
    }
}
