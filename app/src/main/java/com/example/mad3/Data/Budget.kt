package com.example.mad3.Data

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.example.mad3.R
import com.example.mad3.UI.activity_transaction_history
import java.util.Date

data class Budget(
    val title: String,
    val amount: Double,
    val category: String,
    val date: Date,
    val type: TransactionType
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString()!!,
        amount = parcel.readDouble(),
        category = parcel.readString()!!,
        date = Date(parcel.readLong()),
        type = TransactionType.valueOf(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(amount)
        parcel.writeString(category)
        parcel.writeLong(date.time)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Budget> {
        override fun createFromParcel(parcel: Parcel): Budget {
            return Budget(parcel)
        }

        override fun newArray(size: Int): Array<Budget?> {
            return arrayOfNulls(size)
        }

        // Convert from a Map back to Budget
        fun fromMap(map: Map<String, Any>): Budget {
            return Budget(
                title = map["title"] as String,
                amount = (map["amount"] as Number).toDouble(),
                category = map["category"] as String,
                date = Date(map["date"] as Long),
                type = TransactionType.valueOf(map["type"] as String)
            )
        }
    }

    // Optional: Helper function to convert to Map
    fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "amount" to amount,
            "category" to category,
            "date" to date.time,
            "type" to type.name
        )
    }
}



enum class TransactionType {
    INCOME,
    EXPENSE
}