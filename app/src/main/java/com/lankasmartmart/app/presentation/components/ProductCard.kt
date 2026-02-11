package com.lankasmartmart.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.lankasmartmart.app.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lankasmartmart.app.data.model.Product
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        // Image Box with Add Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF5F5F5)) // Light gray background like in design
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .listener(
                        onStart = { android.util.Log.d("ProductCard", "Loading image for: ${product.name}") },
                        onSuccess = { _, _ -> android.util.Log.d("ProductCard", "Image loaded for: ${product.name}") },
                        onError = { _, result -> android.util.Log.e("ProductCard", "Error loading image for ${product.name}: ${result.throwable.message}") }
                    )
                    .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Fit,
                error = painterResource(id = android.R.drawable.ic_menu_report_image),
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                modifier = Modifier
                    .matchParentSize()
                    .padding(16.dp)
            )
            
            // Add Button (+)
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .clickable { /* TODO: Add to cart */ },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add to Cart",
                    tint = Color.Black,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Title
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        // Rating
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700), // Gold
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${product.rating}", // e.g. 4.8
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
             Text(
                text = " (287)", // Mock count for now
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Price
        Text(
            text = "RS.${NumberFormat.getNumberInstance(Locale.US).format(product.price)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        // DEBUG: Show URL
        Text(
            text = product.imageUrl,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Red,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
