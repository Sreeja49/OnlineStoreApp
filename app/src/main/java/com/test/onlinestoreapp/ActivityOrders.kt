package com.test.onlinestoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

data class Order(
    val orderId: String = "",
    val totalAmount: String = ""
)

class ActivityOrders : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineStoreAppTheme {
                orders_list()
            }
        }
    }
}

@Composable
fun orders_list() {
    val context = LocalContext.current
    var orders by remember { mutableStateOf(listOf<Order>()) }

    LaunchedEffect(key1 = Unit) {
        val db = FirebaseDatabase.getInstance()
        val order_ref = db.getReference("orders")

        order_ref.get().addOnSuccessListener { dataSnapshot ->
            val fetchedOrders = mutableListOf<Order>()
            dataSnapshot.children.forEach { snapshot ->
                val order = snapshot.getValue(Order::class.java)
                order?.let { fetchedOrders.add(it.copy(orderId = snapshot.key ?: "")) }
            }
            orders = fetchedOrders
        }.addOnFailureListener {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn {
        items(orders) { order ->
            card_row(order) {
                val intent = Intent(context, ActivityOrdersDetail::class.java)
                intent.putExtra("oid", order.orderId)
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun card_row(order: Order, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.orderId}", style = MaterialTheme.typography.h6)
            Text("Total: ${order.totalAmount}", style = MaterialTheme.typography.subtitle1)
            Text("Tap to see order details", style = MaterialTheme.typography.caption)
        }
    }
}
