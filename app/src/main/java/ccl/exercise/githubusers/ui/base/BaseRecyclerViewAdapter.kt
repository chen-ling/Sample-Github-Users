package ccl.exercise.githubusers.ui.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder<*>>() {

    protected val items = mutableListOf<T>()

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun appendItems(items: List<T>) {
        val startPosition = this.items.size
        this.items.addAll(items)
        notifyItemInserted(startPosition)
    }

    fun getItem(position: Int): T {
        return items[position]
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }
}