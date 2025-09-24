package co.koko.heartbeatmessages.ui.components

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun rememberKeyboardState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(view)
            val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            keyboardState.value = isKeyboardVisible
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return keyboardState
}

@Composable
fun rememberKeyboardHeight(): State<Int> {
    val keyboardHeight = remember { mutableStateOf(0) }
    val view = LocalView.current

    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(view)
            val imeHeight = insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
            keyboardHeight.value = imeHeight
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    return keyboardHeight
}
