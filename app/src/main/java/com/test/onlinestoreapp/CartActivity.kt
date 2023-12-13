package com.test.onlinestoreapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                CartScreen(cartItems)
            }
        }
    }
}

@Composable
fun CartScreen(cartItems: List<CartModelClass>) {
    val totalAmount = calculateTotalAmount(cartItems)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        CartItemsList(cartItems)

        Spacer(modifier = Modifier.weight(1f))

        CheckoutButton(totalAmount)
    }
}

@Composable
fun CartItemsList(cartItems: List<CartModelClass>) {
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


@Composable
fun CheckoutButton(totalAmount: String) {
    var  context= LocalContext.current;
    Button(
        onClick = {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra("totalAmount",totalAmount)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Checkout ($totalAmount)")
    }
}

@Composable
fun calculateTotalAmount(cartItems: List<CartModelClass>): String {
    var total = 0.0
    for (cartItem in cartItems) {
        total += cartItem.itemTotal.toDouble()
    }
    return String.format("%.2f", total)
}

data class CartModelClass(
    val itemId: String="",
    val itemName: String="",
    val itemQuantity: String="",
    val itemTotal: String="",
    val itemImage: String=""
)



