package dev.optimal.tracker.navigation.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

const val NAV_ANIM_DURATION_MS = 5000

sealed interface OptimalTransition {
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)
    val popEnter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
    val popExit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?

    data object Default : OptimalTransition {
        override val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
            slideInHorizontally(tween(NAV_ANIM_DURATION_MS)) { it }
        }
        override val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
            slideOutHorizontally(tween(NAV_ANIM_DURATION_MS)) { -it / 2 }
        }
        override val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
            slideInHorizontally(tween(NAV_ANIM_DURATION_MS)) { -it / 2 }
        }
        override val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
            slideOutHorizontally(tween(NAV_ANIM_DURATION_MS)) { it }
        }
    }

    data class TopLevel(val direction: Int) : OptimalTransition {
        override val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
            slideInHorizontally(
                animationSpec = tween(NAV_ANIM_DURATION_MS),
                initialOffsetX = { if (direction > 0) it else -it }
            )
        }
        override val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(NAV_ANIM_DURATION_MS),
                targetOffsetX = { if (direction > 0) -it else it }
            )
        }
        override val popEnter = null
        override val popExit = null
    }

    // slides up from bottom
    data object Modal : OptimalTransition {
        override val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
            slideInVertically(tween(NAV_ANIM_DURATION_MS)) { it } + fadeIn()
        }
        override val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
            fadeOut(tween(NAV_ANIM_DURATION_MS))
        }
        override val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
            fadeIn(tween(NAV_ANIM_DURATION_MS))
        }
        override val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
            slideOutVertically(tween(NAV_ANIM_DURATION_MS)) { it } + fadeOut()
        }
    }

    // TODO: comment better
    data object SharedElement : OptimalTransition {
        override val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = { fadeIn(tween(NAV_ANIM_DURATION_MS)) }
        override val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = { fadeOut(tween(NAV_ANIM_DURATION_MS)) }
        override val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = { fadeIn(tween(NAV_ANIM_DURATION_MS)) }
        override val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = { fadeOut(tween(NAV_ANIM_DURATION_MS)) }
    }
}
