package com.test.onlinestoreapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.annotation.SuppressLint

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db_online_store"
        private const val CART_TABLE = "tbl_cart"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ITEM_ID = "item_id"
        private const val COLUMN_ITEM_NAME = "item_name"
        private const val COLUMN_ITEM_QUANTITY = "item_qty"
        private const val COLUMN_ITEM_TOTAL = "item_total"
        private const val COLUMN_ITEM_IMAGE = "item_image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createCartTable = "CREATE TABLE IF NOT EXISTS $CART_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ITEM_ID TEXT, " +
                "$COLUMN_ITEM_NAME TEXT, " +
                "$COLUMN_ITEM_QUANTITY TEXT, " +
                "$COLUMN_ITEM_TOTAL TEXT, " +
                "$COLUMN_ITEM_IMAGE TEXT)"
        db.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $CART_TABLE")
        onCreate(db)
    }

    fun saveIntoCart(item_id: String, item_name: String, item_qty: String, item_total: String, item_image: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, item_id)
        values.put(COLUMN_ITEM_NAME, item_name)
        values.put(COLUMN_ITEM_QUANTITY, item_qty)
        values.put(COLUMN_ITEM_TOTAL, item_total)
        values.put(COLUMN_ITEM_IMAGE, item_image)
        val result = db.insert(CART_TABLE, null, values)
        db.close()
        return result > 0
    }

    @SuppressLint("Range")
    fun getQty(id: String): String {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $CART_TABLE WHERE $COLUMN_ITEM_ID='$id'"
        val cursor = db.rawQuery(selectQuery, null)
        var qty = ""
        if (cursor.moveToFirst()) {
            qty = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY))
        }
        cursor.close()
        return qty
    }

    @SuppressLint("Range")
    fun getCartItems(): ArrayList<CartModelClass> {
        val carts = ArrayList<CartModelClass>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $CART_TABLE"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val cart = CartModelClass(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_TOTAL)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_IMAGE))
                )
                carts.add(cart)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return carts
    }

    fun updateQty(item_id: String, qty: String, total: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ITEM_QUANTITY, qty)
        contentValues.put(COLUMN_ITEM_TOTAL, total)
        val success = db.update(CART_TABLE, contentValues, "$COLUMN_ITEM_ID = ?", arrayOf(item_id))
        db.close()
        return success > 0
    }

    fun deleteCart() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $CART_TABLE")
        db.close()
    }

    @SuppressLint("Range")
    fun getCartTotal(): String {
        val db = this.readableDatabase
        val selectQuery = "SELECT SUM($COLUMN_ITEM_TOTAL) as total FROM $CART_TABLE"
        val cursor = db.rawQuery(selectQuery, null)
        var total = "Error"
        if (cursor.moveToFirst()) {
            total = cursor.getString(cursor.getColumnIndex("total"))
        }
        cursor.close()
        db.close()
        return total
    }


}

data class Cart(
    val item_id: String,
    val item_name: String,
    val item_qty: String,
    val item_price: String,
    val item_image: String
)
