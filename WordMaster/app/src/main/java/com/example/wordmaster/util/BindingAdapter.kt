package com.example.wordmaster.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("showIfNotNull")
fun showIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.VISIBLE else View.GONE
}

