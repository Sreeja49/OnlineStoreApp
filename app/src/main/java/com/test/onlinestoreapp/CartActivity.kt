package com.test.onlinestoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseHandler = DatabaseHandler(this)
        val cartItems = databaseHandler.getCartItems()

        setContent {
            OnlineStoreAppTheme {
                CartItemsList(cartItems)
            }
        }
    }
}
@Composable
fun CartItemsList(cartItems: ArrayList<CartModelClass>) {
    LazyColumn {
        items(cartItems) { cartItem ->
            CartItemCard(cartItem)
        }
    }
}
@Composable
fun CartItemCard(cartItem: CartModelClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = cartItem.itemImage),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = cartItem.itemName, style = MaterialTheme.typography.h6)
                Text(text = "Quantity: ${cartItem.itemQuantity}")
                Text(text = "Total: ${cartItem.itemTotal}")
            }
        }
    }
}

data class CartModelClass(
    val itemId: String,
    val itemName: String,
    val itemQuantity: String,
    val itemTotal: String,
    val itemImage: String
)



