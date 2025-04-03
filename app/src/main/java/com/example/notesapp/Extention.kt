package com.example.notesapp

import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.setSingleClick(
    clickSpendTime: Long = 500L,
    execution: () -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(p0: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < clickSpendTime) {
                return
            }
            lastClickTime = SystemClock.elapsedRealtime()
            execution.invoke()
        }
    })
}

fun loadImageFromAsset(context: Context, directory: String, intoView: ImageView) {
    Glide.with(context).load(
        "file:///android_asset/$directory"
    ).into(intoView)
}
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}