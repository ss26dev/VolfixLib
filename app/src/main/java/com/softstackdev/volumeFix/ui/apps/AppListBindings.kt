package com.softstackdev.volumeFix.ui.apps

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softstackdev.volumeFix.data.apps.AppData

/**
 * [BindingAdapter]s for the [AppData]s list.
 */
@BindingAdapter("state")
fun recyclerViewState(listView: RecyclerView, state: UIState<List<UIAppData>>?) {
    when (state) {
        is UIState.Data -> {
            listView.visibility = View.VISIBLE
            (listView.adapter as AppsAdapter).submitList(state.data)
        }
        else -> {
            listView.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("state")
fun progressViewState(progressBar: ProgressBar, state: UIState<List<UIAppData>>?) {
    when (state) {
        is UIState.Loading -> {
            progressBar.visibility = View.VISIBLE
        }
        else -> {
            progressBar.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("state")
fun progressViewState(errorTextView: TextView, state: UIState<List<UIAppData>>?) {
    when (state) {
        is UIState.Failure -> {
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = state.error
        }
        else -> {
            errorTextView.visibility = View.INVISIBLE
        }
    }
}

@BindingAdapter("tintExcluded")
fun tintExcluded(imageView: ImageView, uiAppData: UIAppData) {
    imageView.apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(if (uiAppData.excluded) 0f else 1f)
        })
    }
}