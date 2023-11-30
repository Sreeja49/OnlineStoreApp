package com.test.onlinestoreapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

class ProductDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productName = intent.getStringExtra("productName") ?: "Product"
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productDescription = intent.getStringExtra("productDescription") ?: "No description available."
        val productImage = intent.getStringExtra("productImage") ?: ""
        val cartItemCount = 1

        setContent {
            OnlineStoreAppTheme {
                ProductDetail(productName, productPrice, productDescription, productImage, cartItemCount)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductDetail(name: String, price: Double, description: String, imageUrl: String, cartItemCount: Int) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color(android.graphics.Color.parseColor("#50758D")),

                title = { Text("Product Detail")  },
                actions = {
                    CartImageWithCounter(cartItemCount = cartItemCount, onCartClick = {

                        Toast.makeText(context, "Cart clicked!", Toast.LENGTH_SHORT).show()
                    })
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (imageUrl.isNotEmpty()) {
                NetworkImageUrl(url = imageUrl, contentDescription = "$name image")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, style = MaterialTheme.typography.h4, textAlign = TextAlign.Center)
            Text("Price: $price", style = MaterialTheme.typography.subtitle1, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.body1, textAlign = TextAlign.Center)
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Add to Cart")
            }
        }
    }
}

@Composable
fun CartImageWithCounter(cartItemCount: Int, onCartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onCartClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
        if (cartItemCount > 0) {
            Text(
                text = cartItemCount.toString(),
                modifier = Modifier.padding(start = 4.dp),
                color = Color.Red
            )
        }
    }
}

@Composable
fun NetworkImageUrl(url: String, contentDescription: String) {
    Image(
        painter = rememberImagePainter(data = url),
        contentDescription = contentDescription,
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .padding(4.dp),
        contentScale = ContentScale.Fit
    )
}


