package com.lankasmartmart.app.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

@Composable
fun SplashScreen(navController: NavController) {
    val textScale = remember { androidx.compose.animation.core.Animatable(0f) }
    val cartAlpha = remember { androidx.compose.animation.core.Animatable(0f) }
    val cartOffsetX = remember { androidx.compose.animation.core.Animatable(-300f) } // Start off-screen left

    LaunchedEffect(key1 = true) {
        // Step 1: Animate Text (Scale up)
        textScale.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.spring(
                dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                stiffness = androidx.compose.animation.core.Spring.StiffnessLow
            )
        )
        
        // Step 2: Animate Cart (Slide in from left)
        launch {
            cartAlpha.animateTo(
                targetValue = 1f,
                animationSpec = androidx.compose.animation.core.tween(500)
            )
        }
        cartOffsetX.animateTo(
            targetValue = 0f,
            animationSpec = androidx.compose.animation.core.spring(
                dampingRatio = androidx.compose.animation.core.Spring.DampingRatioLowBouncy,
                stiffness = androidx.compose.animation.core.Spring.StiffnessLow
            )
        )

        // Step 3: Wait and Navigate
        delay(1500)
        navController.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(com.lankasmartmart.app.ui.theme.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cart Image (Uploaded PNG)
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.lankasmartmart.app.R.drawable.cart_image),
                contentDescription = "Cart",
                modifier = Modifier
                    .size(120.dp) // Adjusted size for image
                    .alpha(cartAlpha.value)
                    .offset(x = cartOffsetX.value.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Animated Text
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFFFDD835))) { // Lanka - Yellow/Gold
                        append("Lanka")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFFD32F2F))) { // Smart - Red/Maroon
                        append("Smart")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF388E3C))) { // Mart - Green
                        append("Mart")
                    }
                },
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(textScale.value)
            )
        }
    }
}
