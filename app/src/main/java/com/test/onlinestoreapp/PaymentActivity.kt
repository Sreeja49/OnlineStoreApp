package com.test.onlinestoreapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.test.onlinestoreapp.ui.theme.OnlineStoreAppTheme

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val totalAmount = intent.getStringExtra("totalAmount") ?: "TotalAmount"

        setContent {
            OnlineStoreAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                )
                {
                    PaymentScreen(totalAmount+"£")
                }
            }
        }
    }
}

@Composable
fun PaymentScreen(totalAmount:String) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var context= LocalContext.current;
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Payment Details", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Total Amount: $totalAmount", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.credit_cards),
            contentDescription = "Debit Card",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Card Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expiryDate,
            onValueChange = { expiryDate = it },
            label = { Text("Expiry Date (MM/YY)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cvv,
            onValueChange = { cvv = it },
            label = { Text("CVV") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

               if(cardNumber.length != 16){

                   Toast.makeText(context,"Wrong card number",Toast.LENGTH_SHORT).show()

               }
               else if(expiryDate.length == 5 && expiryDate[2] == '/'){

                   Toast.makeText(context,"Wrong expiry",Toast.LENGTH_SHORT).show()

                }
              else  if(cvv.length != 3){

                   Toast.makeText(context,"Wrong cvv",Toast.LENGTH_SHORT).show()

                }

                else{
                   val cartItems = DatabaseHandler(context).getCartItems()

                   save_order_db(context,Utils.GetData(context,"uid"),totalAmount,cartItems)
               }


            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Process Payment")
        }
    }
}



fun save_order_db(context: Context, userId: String, totalAmount: String, orderItems: List<CartModelClass>) {
    val oref = FirebaseDatabase.getInstance().getReference("orders")
    val oid = oref.key
    val orderItemList = mutableListOf<Map<String, Any>>()
    for (item in orderItems) {
        val itemMap = mutableMapOf<String, Any>(
            "itemId" to item.itemId,
            "itemName" to item.itemName,
            "quantity" to item.itemQuantity,
            "total" to item.itemTotal
        )
        orderItemList.add(itemMap)
    }

    val orderMap = mutableMapOf(
        "userId" to userId,
        "totalAmount" to totalAmount,
        "orderItems" to orderItemList
    )

    if (oid != null) {
        oref.child(oid).setValue(orderMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context,"Order saved successfully!",Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context,"Opps Order not successfully saved!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}