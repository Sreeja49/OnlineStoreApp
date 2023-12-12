package com.test.onlinestoreapp

import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase

class ActivityOrdersDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val oid = intent.getStringExtra("oid") ?: return

        setContent {
            OnlineStoreAppTheme {
                order_detail(oid)
            }
        }
    }
}

@Composable
fun order_detail(oid: String) {
    val context = LocalContext.current
    var orderItems by remember { mutableStateOf<List<OrderDetailModelClass>>(emptyList()) }

    LaunchedEffect(key1 = oid) {
        val db = FirebaseDatabase.getInstance()
        val order_ref = db.getReference("orders").child(oid).child("orderItems")

        order_ref.get().addOnSuccessListener { dataSnapshot ->
            val items = mutableListOf<OrderDetailModelClass>()
            dataSnapshot.children.forEach { snapshot ->
                snapshot.getValue(OrderDetailModelClass::class.java)?.let { items.add(it) }
            }
            orderItems = items
        }.addOnFailureListener {
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn {
        items(orderItems) { item ->
            item_card(item)
        }
    }
}

@Composable
fun item_card(item: OrderDetailModelClass) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Item Name: ${item.itemName}", style = MaterialTheme.typography.h6)
            Text("Quantity: ${item.quantity}", style = MaterialTheme.typography.subtitle1)
            Text("Total: ${item.total}", style = MaterialTheme.typography.subtitle1)
        }
    }
}
data class OrderDetailModelClass(
    val itemId: String="",
    val itemName: String="",
    val quantity: String="",
    val total: String=""
)
