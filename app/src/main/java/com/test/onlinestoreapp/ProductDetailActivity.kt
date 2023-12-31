package com.test.onlinestoreapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        val productId = intent.getStringExtra("productId") ?: "ProductId"
        val productName = intent.getStringExtra("productName") ?: "Product"
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productDescription = intent.getStringExtra("productDescription") ?: "No description available."
        val productImage = intent.getStringExtra("productImage") ?: ""
        var databaseHandler=DatabaseHandler(this)
        val cartItemCount=  databaseHandler.getCartItems().size
        setContent {
            OnlineStoreAppTheme {
                ProductDetail(productId,productName, productPrice, productDescription, productImage, cartItemCount)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductDetail(id:String, name: String, price: Double, description: String, imageUrl: String, cartItemCount: Int) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(

                backgroundColor = Color(android.graphics.Color.parseColor("#50758D")),
                title = { Text("Product Detail")  },
                actions = {
                    CartImageWithCounter(cartItemCount = cartItemCount, onCartClick = {

                        val intent = Intent(context, CartActivity::class.java)
                        context.startActivity(intent)



                    })
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()) ,
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

                          var databaseHandler=DatabaseHandler(context);

                          val previous_qty=databaseHandler.getQty(id)

                          if(previous_qty.equals("")){

                        val total_qty=0+1

                        val product_total=total_qty.toDouble()*price

                        Toast.makeText(context,"Added to cart",Toast.LENGTH_LONG).show()

                          databaseHandler.saveIntoCart(id,name,total_qty.toString(),product_total.toString(),imageUrl)


                    }
                    else{

                              val total_qty=previous_qty.toInt()+1

                              val product_total=total_qty.toDouble()*price
                              databaseHandler.updateQty(id,total_qty.toString(),product_total.toString())

                              Toast.makeText(context,"Updated Cart",Toast.LENGTH_LONG).show()

                    }
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


