package com.localtoolhub.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.localtoolhub.app.databinding.ItemDrawerToolBinding

class DrawerAdapter(
    private val items: List<ToolItem>,
    private val onClick: (ToolItem) -> Unit
) : RecyclerView.Adapter<DrawerAdapter.ToolViewHolder>() {

    private var selectedId: String? = items.firstOrNull()?.id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val binding = ItemDrawerToolBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(items[position], items[position].id == selectedId)
    }

    override fun getItemCount(): Int = items.size

    fun select(itemId: String) {
        val previousId = selectedId
        if (previousId == itemId) return

        selectedId = itemId
        previousId?.let { oldId ->
            val oldIndex = items.indexOfFirst { it.id == oldId }
            if (oldIndex >= 0) notifyItemChanged(oldIndex)
        }

        val newIndex = items.indexOfFirst { it.id == itemId }
        if (newIndex >= 0) notifyItemChanged(newIndex)
    }

    inner class ToolViewHolder(
        private val binding: ItemDrawerToolBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolItem, isSelected: Boolean) {
            binding.iconView.setImageResource(item.iconResId)
            binding.titleView.text = item.title
            binding.root.isSelected = isSelected
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
