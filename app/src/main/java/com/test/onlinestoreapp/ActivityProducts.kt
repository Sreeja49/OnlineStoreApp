package com.test.onlinestoreapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

@Keep
data class ProductModelClass(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val image: String = ""
)

class ActivityProducts : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProductList()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductList() {
    val context = LocalContext.current
    val products = remember { mutableStateOf(listOf<ProductModelClass>()) }
    val expanded = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = "loadData") {
        Utils.showProgressDialog(context)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("products")

        myRef.get().addOnSuccessListener { dataSnapshot ->
            val items = dataSnapshot.children.map { snapshot ->
                snapshot.getValue(ProductModelClass::class.java)!!
            }
            products.value = items
            Utils.hideProgressDialog()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products List") },
                actions = {
                    IconButton(onClick = { expanded.value = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded.value = false
                            val intent = Intent(context, ActivityOrders::class.java)
                            context.startActivity(intent)
                        }) {
                            Text("My Orders")
                        }
                        DropdownMenuItem(onClick = {
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            if(context is Activity){
                                context.finish()
                            }
                            Utils.SetData(context,"uid","")

                            expanded.value = false
                        }) {
                            Text("Logout")
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn {
            items(products.value.chunked(1)) { rowItems ->
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    for (product in rowItems) {
                        ProductCard(product) {

                            val intent = Intent(context, ProductDetailActivity::class.java)
                            intent.putExtra("productId", product.id)
                            intent.putExtra("productName", product.name)
                            intent.putExtra("productPrice", product.price)
                            intent.putExtra("productDescription", product.description)
                            intent.putExtra("productImage", product.image)
                            context.startActivity(intent)


                        }
                    }
                    if (rowItems.size < 2) {

                        Spacer(modifier = Modifier.weight(1f).padding(horizontal = 8.dp))

                    }
                }
            }
        }
    }


}

@Composable
fun ProductCard(product: ProductModelClass, onItemClick: (ProductModelClass) -> Unit) {
    Card(modifier = Modifier.padding(4.dp).clickable { onItemClick(product) }, elevation = 10.dp)
    {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally)
        {
            NetworkImage(url = product.image, contentDescription = "Product Image")
            Text(product.name, style = MaterialTheme.typography.h6)
            Text("Price: ${product.price}")

        }
    }
}

@Composable
fun NetworkImage(url: String, contentDescription: String) {

    Image(painter = rememberImagePainter(data = url), contentDescription = contentDescription, modifier = Modifier.padding(4.dp).height(150.dp).fillMaxWidth(), contentScale = ContentScale.Fit)
}

@Preview(showBackground = true)
@Composable
fun PreviewProductList() {
    OnlineStoreAppTheme {
        ProductList()
    }
}
