package com.example.familytree

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Адаптер для отображения дерева в RecyclerView
class TreeAdapter(private var rootNode: Node, private val onNodeClickListener: (Node) -> Unit) :
    RecyclerView.Adapter<TreeAdapter.NodeViewHolder>() {

    init {
        updateData(rootNode)
    }

    // ViewHolder для элемента дерева
    class NodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nodeNameTextView: TextView = itemView.findViewById(R.id.nodeNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tree_node_item, parent, false)
        return NodeViewHolder(view)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val currentNode = getNodeAtPosition(position)
        holder.nodeNameTextView.text = currentNode.name

        // Обработка клика по узлу
        holder.itemView.setOnClickListener {
            onNodeClickListener(currentNode)
        }
    }

    // Возвращает количество элементов в дереве
    override fun getItemCount(): Int {
        return calculateTreeSize(rootNode)
    }

    // Получение узла по позиции в RecyclerView
    private fun getNodeAtPosition(position: Int): Node {
        return getNodeAtPosition(rootNode, position)
    }

    // Получение узла по позиции в дереве
    private fun getNodeAtPosition(node: Node, position: Int): Node {
        val flattenedList = flattenTree(rootNode)
        return flattenedList[position]
    }

    // Обновление данных в адаптере
    fun updateData(rootNode: Node) {
        this.rootNode = rootNode
        notifyDataSetChanged()
    }

    // Вычисление общего количества узлов в дереве
    private fun calculateTreeSize(node: Node): Int {
        return flattenTree(node).size
    }

    // Построение плоского списка узлов из дерева
    private fun flattenTree(node: Node): List<Node> {
        val nodeList = mutableListOf<Node>()
        nodeList.add(node)
        node.children.forEach { child ->
            nodeList.addAll(flattenTree(child))
        }
        return nodeList
    }
}