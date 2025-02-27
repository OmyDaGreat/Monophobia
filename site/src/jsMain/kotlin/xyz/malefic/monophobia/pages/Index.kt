@file:Suppress("ktlint:standard:no-wildcard-imports")

package xyz.malefic.monophobia.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.BackgroundPosition
import com.varabyte.kobweb.compose.css.BackgroundSize
import com.varabyte.kobweb.compose.css.CSSPosition
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.backdropFilter
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.css.overflow
import com.varabyte.kobweb.compose.css.transition
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import kotlin.js.Date
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val PRIMARY_COLOR = Color.rgb(131, 37, 65)
val ACCENT_COLOR = Color.rgb(241, 147, 175)
val TEXT_COLOR = Color.rgb(255, 245, 250)
val DARK_COLOR = Color.rgb(91, 17, 35)

@Page
@Composable
fun HomePage() {
    org.jetbrains.compose.web.dom.Style {
        "body" style {
            margin(0.px)
            padding(0.px)
            overflow(Overflow.Hidden)
            fontFamily("Poppins, sans-serif")
        }
    }

    var time by remember { mutableStateOf(getCurrentTime()) }
    var mouseX by remember { mutableStateOf(0.0) }
    var mouseY by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        val intervalId =
            window.setInterval({
                time = getCurrentTime()
            }, 1000)

        val mouseMoveListener: (dynamic) -> Unit = { event ->
            mouseX = event.clientX as Double
            mouseY = event.clientY as Double
        }

        document.addEventListener("mousemove", mouseMoveListener)

//        onDispose {
//            window.clearInterval(intervalId)
//            document.removeEventListener("mousemove", mouseMoveListener)
//        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .backgroundImage(url("https://gallery.malefic.xyz/photos/Ace%20Of%20Hearts/MonophobiaBackground.png"))
                .backgroundSize(BackgroundSize.Cover)
                .backgroundPosition(BackgroundPosition.of(CSSPosition.Center))
                .fontFamily("Poppins, sans-serif")
                .color(TEXT_COLOR),
    ) {
        // Interactive overlay that creates a subtle color cast
        Div(
            attrs = {
                style {
                    position(Position.Absolute)
                    top(0.px)
                    left(0.px)
                    right(0.px)
                    bottom(0.px)
                    backgroundColor(rgba(PRIMARY_COLOR.red, PRIMARY_COLOR.green, PRIMARY_COLOR.blue, 0.2))
                    backdropFilter(blur(2.px))
                }
            },
        )

        // Particle system
        ParticleSystem(mouseX, mouseY)

        // Clock widget
        Box(
            modifier =
                Modifier
                    .position(Position.Absolute)
                    .top(30.px)
                    .right(30.px)
                    .backgroundColor(rgba(DARK_COLOR.red, DARK_COLOR.green, DARK_COLOR.blue, 0.85))
                    .padding(20.px)
                    .borderRadius(15.px)
                    .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 15.px, color = rgba(0, 0, 0, 0.3)),
        ) {
            SpanText(
                text = time,
                modifier =
                    Modifier
                        .fontSize(2.5.em)
                        .fontWeight(FontWeight.Bold),
            )
        }

        // Interactive card
        InteractiveCard()

        // Quote display
        QuoteDisplay()
    }
}

@Composable
fun ParticleSystem(
    mouseX: Double,
    mouseY: Double,
) {
    // Create particles
    val particles =
        remember {
            List(30) {
                Particle(
                    x = Random.nextDouble() * window.innerWidth,
                    y = Random.nextDouble() * window.innerHeight,
                    size = Random.nextDouble(3.0, 7.0),
                    speed = Random.nextDouble(0.2, 1.0),
                )
            }
        }

    var positions by remember { mutableStateOf(particles.map { Pair(it.x, it.y) }) }

    LaunchedEffect(Unit) {
        val intervalId =
            window.setInterval({
                positions =
                    particles.mapIndexed { index, particle ->
                        // Update particle position
                        val dx = (mouseX - particle.x) * 0.01 * particle.speed
                        val dy = (mouseY - particle.y) * 0.01 * particle.speed

                        particle.x += dx
                        particle.y += dy

                        // Add some random movement
                        particle.x += sin(Date().getTime() * 0.001 + index) * 0.5
                        particle.y += cos(Date().getTime() * 0.001 + index) * 0.5

                        // Keep within boundaries
                        if (particle.x < 0) particle.x = window.innerWidth.toDouble()
                        if (particle.x > window.innerWidth) particle.x = 0.0
                        if (particle.y < 0) particle.y = window.innerHeight.toDouble()
                        if (particle.y > window.innerHeight) particle.y = 0.0

                        Pair(particle.x, particle.y)
                    }
            }, 16)

//        onDispose {
//            window.clearInterval(intervalId)
//        }
    }

    // Render particles
    positions.forEachIndexed { index, (x, y) ->
        val particle = particles[index]
        Div(
            attrs = {
                style {
                    position(Position.Absolute)
                    left(x.px)
                    top(y.px)
                    width(particle.size.px)
                    height(particle.size.px)
                    borderRadius(50.percent)
                    backgroundColor(rgba(ACCENT_COLOR.red, ACCENT_COLOR.green, ACCENT_COLOR.blue, 0.7))
                    boxShadow(
                        offsetX = 0.px,
                        offsetY = 0.px,
                        blurRadius = 10.px,
                        color = rgba(ACCENT_COLOR.red, ACCENT_COLOR.green, ACCENT_COLOR.blue, 0.5),
                    )
                    transition(Transition.of("transform", 1.s))
                    property("transform", "scale(${1 + sin(Date().getTime() * 0.001 + index) * 0.3})")
                }
            },
        )
    }
}

@Composable
fun InteractiveCard() {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .position(Position.Absolute)
                .bottom(30.px)
                .left(30.px)
                .width(300.px)
                .backgroundColor(rgba(DARK_COLOR.red, DARK_COLOR.green, DARK_COLOR.blue, if (isHovered) 0.95 else 0.85))
                .padding(20.px)
                .borderRadius(15.px)
                .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 15.px, color = rgba(0, 0, 0, 0.3))
                .transition(Transition.all(0.3.s))
                .transform { if (isHovered) translateY((-5).px) else translateY(0.px) }
                .cursor(Cursor.Pointer)
                .onMouseEnter { isHovered = true }
                .onMouseLeave { isHovered = false },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SpanText(
                text = "Welcome Malefic",
                modifier =
                    Modifier
                        .fontSize(1.5.em)
                        .fontWeight(FontWeight.Bold)
                        .margin(bottom = 10.px),
            )

            SpanText(
                text = "Move your mouse to interact with particles",
                modifier =
                    Modifier
                        .fontSize(0.9.em)
                        .lineHeight(1.5),
            )

            if (isHovered) {
                SpanText(
                    text = "Created with Kobweb & Kotlin",
                    modifier =
                        Modifier
                            .margin(top = 15.px)
                            .fontSize(0.8.em)
                            .color(ACCENT_COLOR),
                )
            }
        }
    }
}

@Composable
fun QuoteDisplay() {
    var quote by remember { mutableStateOf(Quote("Loading...", "Unknown")) }

    LaunchedEffect(Unit) {
        window.setInterval({
            MainScope().launch { quote = fetchQuote() }
        }, 60000 * 30) // Change quote every 10 seconds
    }

    Box(
        modifier =
            Modifier
                .position(Position.Absolute)
                .top(50.percent)
                .left(50.percent)
                .transform { translate((-50).percent, (-50).percent) }
                .width(60.percent)
                .maxWidth(700.px)
                .backgroundColor(rgba(DARK_COLOR.red, DARK_COLOR.green, DARK_COLOR.blue, 0.8))
                .padding(30.px)
                .borderRadius(10.px)
                .boxShadow(offsetX = 0.px, offsetY = 4.px, blurRadius = 15.px, color = rgba(0, 0, 0, 0.3)),
    ) {
        SpanText(
            text = "${quote.text} - ${quote.author}",
            modifier =
                Modifier
                    .fontSize(1.8.em)
                    .fontStyle(FontStyle.Italic)
                    .textAlign(TextAlign.Center)
                    .fillMaxWidth(),
        )
    }
}

suspend fun fetchQuote(): Quote {
    try {
        console.log("Fetching JSON...")
        val response = window.fetch("https://daily.malefic.xyz/quote").await()

        if (!response.ok) {
            console.error("Failed to fetch: ${response.statusText}")
            return Quote("A moment of silence...", "Unknown")
        }

        console.log("Received response, parsing JSON...")
        val json: dynamic = response.json().await() // Convert response to JSON

        // Accessing fields dynamically
        val text: String = json.text as String
        val author: String = json.author as String

        console.log("Received key1: $text, key2: $author")
        return Quote(text, author)
    } catch (e: Throwable) {
        console.error("Error fetching JSON: $e")
        return Quote("A moment of silence...", "Unknown")
    }
}

data class Quote(
    val text: String,
    val author: String,
)

fun getCurrentTime(): String {
    val date = Date()
    val hours = date.getHours().toString().padStart(2, '0')
    val minutes = date.getMinutes().toString().padStart(2, '0')
    val seconds = date.getSeconds().toString().padStart(2, '0')
    return "$hours:$minutes:$seconds"
}

class Particle(
    var x: Double,
    var y: Double,
    val size: Double,
    val speed: Double,
)
