package com.joshayoung.lazypizza.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.joshayoung.lazypizza.core.ui.theme.LazyPizzaTheme
import com.joshayoung.lazypizza.core.ui.theme.surfaceHighest
import com.joshayoung.lazypizza.core.ui.theme.textPrimary

@Composable
fun SmallRoundedPizzaScaffold(
    topBar: @Composable () -> Unit,
    topPadding: Dp = 60.dp,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.surfaceHighest)
                .padding(top = topPadding)
                .dropShadow(
                    shape =
                        RoundedCornerShape(20.dp),
                    shadow =
                        Shadow(
                            radius = 16.dp,
                            spread = 0.dp,
                            color =
                                MaterialTheme.colorScheme.textPrimary.copy(alpha = 0.04f),
                            offset = DpOffset(x = 0.dp, -(4.dp))
                        )
                ).clip(RoundedCornerShape(20.dp)),
        topBar = topBar
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Preview
@Composable
private fun SmallRoundedPizzaScaffoldPreview() {
    LazyPizzaTheme {
        SmallRoundedPizzaScaffold(
            topBar = {
                RoundedTopBar(backToCart = {})
            },
            topPadding = 60.dp
        ) {
            Text("Content")
        }
    }
}
