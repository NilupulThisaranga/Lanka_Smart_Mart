package com.lankasmartmart.app.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lankasmartmart.app.R
import com.lankasmartmart.app.data.model.ProductCategory
import com.lankasmartmart.app.ui.theme.WelcomeScreenGreen

@Composable
fun HomeTopBar(
    address: String = "SLTC, Ingiriya Road, Meepe, Padukka",
    onCartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Location Icon (Truck/Delivery)
        Icon(
            imageVector = Icons.Default.LocalShipping,
            contentDescription = "Delivery Location",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Address with Dropdown
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Select Location",
                tint = Color.Black
            )
        }
        
        // Cart Icon
        IconButton(onClick = onCartClick) {
            Icon(
                imageVector = Icons.Outlined.ShoppingBag,
                contentDescription = "Cart",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun HomeBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4F8D4)) // Light green banner bg
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start=16.dp, top=16.dp, bottom=16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(
                        text = "Up to 30% offer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Enjoy our big offer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { /* TODO */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)), // Green button
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Shop Now", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    }
                }
                
                // Image
                Box(
                    modifier = Modifier.weight(0.5f),
                    contentAlignment = Alignment.Center
                ) {
                   AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg/800px-Good_Food_Display_-_NCI_Visuals_Online.jpg")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Offer Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)) // Clip to card
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySection(
    selectedCategory: ProductCategory,
    onCategorySelected: (ProductCategory) -> Unit
) {
    val categories = listOf(
        ProductCategory.FRUITS,
        ProductCategory.MILK_AND_EGG,
        ProductCategory.BEVERAGES,
        ProductCategory.LAUNDRY,
        ProductCategory.VEGETABLES
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

// Map categories to Wikimedia generic images
fun getCategoryImageUrl(category: ProductCategory): String {
    return when (category) {
        ProductCategory.FRUITS -> "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Culinary_fruits_front_view.jpg/320px-Culinary_fruits_front_view.jpg"
        ProductCategory.MILK_AND_EGG -> "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/Milk_glass.jpg/240px-Milk_glass.jpg" // Milk
        ProductCategory.BEVERAGES -> "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Cocktails_with_fruits.jpg/320px-Cocktails_with_fruits.jpg"
        ProductCategory.LAUNDRY -> "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Laundry_Basket.JPG/320px-Laundry_Basket.JPG"
        ProductCategory.VEGETABLES -> "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Market_vegetables.jpg/320px-Market_vegetables.jpg"
        else -> "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/240px-No_image_available.svg.png"
    }
}

@Composable
fun CategoryItem(
    category: ProductCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0)), // Light gray background
            contentAlignment = Alignment.Center
        ) {
             AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getCategoryImageUrl(category))
                    .crossfade(true)
                    .build(),
                contentDescription = category.displayName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().padding(8.dp).clip(CircleShape)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.Black else Color.Gray, // Keep text black if selected, gray otherwise, matching design
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium, // Reduced size slightly to match
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Text(
            text = "See all",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF4CAF50), // Green for See all
            modifier = Modifier.clickable(onClick = onSeeAllClick)
        )
    }
}
