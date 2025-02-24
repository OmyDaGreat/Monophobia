package xyz.malefic.monophobia.utils

import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TransformOrigin
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.css.functions.brightness
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backdropFilter
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.filter
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.textShadow
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.modifiers.transformOrigin
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.css.s

val InfoContainerStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Fixed)
                .bottom(50.px)
                .right(50.px)
                .color(Colors.White)
                .zIndex(3)
                .textShadow(0.px, 0.px, 10.px, rgba(131, 37, 65, 0.5))
                .padding(15.px, 20.px)
                .background(rgba(131, 37, 65, 0.15))
                .borderRadius(15.px)
                .backdropFilter(blur(8.px))
                .border(1.px, LineStyle.Solid, rgba(131, 37, 65, 0.3))
                .display(DisplayStyle.Flex)
                .flexDirection(FlexDirection.Column)
                .alignItems(AlignItems.FlexEnd)
                .gap(5.px)
                .minWidth(300.px)
        }
    }

val QuoteContainerStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Fixed)
                .top(50.px)
                .left(50.px)
                .color(Colors.White)
                .zIndex(3)
                .textShadow(0.px, 0.px, 10.px, rgba(131, 37, 65, 0.5))
                .padding(12.px, 20.px)
                .background(rgba(131, 37, 65, 0.15))
                .borderRadius(12.px)
                .backdropFilter(blur(8.px))
                .border(1.px, LineStyle.Solid, rgba(131, 37, 65, 0.3))
                .transition(Transition.of("opacity", 1.s))
                .display(DisplayStyle.Flex)
                .flexDirection(FlexDirection.Column)
                .alignItems(AlignItems.FlexStart)
                .gap(5.px)
                .minWidth(300.px)
        }
    }

val WelcomePopupStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Fixed)
                .top(50.percent)
                .left(50.percent)
                .transform {
                    translate((-50).percent, (-50).percent)
                    scale(0.9)
                }.background(rgba(131, 37, 65, 0.25))
                .backdropFilter(blur(12.px))
                .padding(30.px, 50.px)
                .borderRadius(20.px)
                .border(2.px, LineStyle.Solid, rgba(131, 37, 65, 0.5))
                .color(Colors.White)
                .fontSize(32.px)
                .fontWeight(300)
                .letterSpacing(2.px)
                .zIndex(1000)
                .opacity(0)
                .transition(Transition.of("all", 500.ms))
        }
    }

val PopupOverlayStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Fixed)
                .top(0.px)
                .left(0.px)
                .width(100.percent)
                .height(100.percent)
                .background(rgba(0, 0, 0, 0.5))
                .backdropFilter(blur(8.px))
                .zIndex(999)
                .opacity(0)
                .transition(Transition.of("opacity", 500.ms))
        }
    }

val BackgroundImageStyle =
    CssStyle {
        base {
            Modifier
                .fillMaxSize()
                .position(Position.Fixed)
                .zIndex(1)
                .filter(brightness(0.8))
                .transition(Transition.of("all", 500.ms))
        }

        // Add the active state modifier
        cssRule(".active") {
            Modifier
                .filter(brightness(1))
                .transform { scale(1.02) }
        }
    }

val ParticleStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Absolute)
                .background(Config.color)
                .borderRadius(50.percent)
                .pointerEvents(PointerEvents.None)
                .opacity(0)
                .transition(Transition.of("opacity", 300.ms))
                .boxShadow(BoxShadow.of(0.px, 0.px, 10.px, color = Config.color))
        }
    }

val ClickRippleStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Absolute)
                .border(2.px, LineStyle.Solid, Config.color)
                .borderRadius(50.percent)
                .pointerEvents(PointerEvents.None)
                .animation(
                    RippleKeyframes.toAnimation(
                        1.s,
                        iterationCount = AnimationIterationCount.of(1),
                        timingFunction = AnimationTimingFunction.Linear,
                    ),
                )
        }
    }

val SwirlParticleStyle =
    CssStyle {
        base {
            Modifier
                .position(Position.Absolute)
                .background(Config.color)
                .borderRadius(50.percent)
                .pointerEvents(PointerEvents.None)
                .opacity(0.6)
                .transformOrigin(TransformOrigin.Center)
                .animation(
                    SwirlKeyframes.toAnimation(
                        3.s,
                        iterationCount = AnimationIterationCount.Infinite,
                        timingFunction = AnimationTimingFunction.Linear,
                    ),
                )
        }
    }

val RippleKeyframes =
    Keyframes {
        from {
            Modifier
                .width(0.px)
                .height(0.px)
                .opacity(0.5)
        }
        to {
            Modifier
                .width(500.px)
                .height(500.px)
                .opacity(0)
                .margin((-250).px)
        }
    }

val SwirlKeyframes =
    Keyframes {
        from {
            Modifier.transform {
                rotate(0.deg)
                translateX(0.px)
                rotate(0.deg)
            }
        }
        to {
            Modifier.transform {
                rotate(360.deg)
                translateX(100.px)
                rotate((-360).deg)
            }
        }
    }
