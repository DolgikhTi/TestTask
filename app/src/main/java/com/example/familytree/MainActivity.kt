package com.example.familytree

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var currentNode: Node
    private lateinit var tree: Node
    private lateinit var gson: Gson
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TreeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Gson для работы с JSON
        gson = Gson()
        // Загрузка сохраненного состояния дерева или создание нового
        tree = loadTreeState() ?: Node("Root")

        // Начальный узел - корень дерева
        currentNode = tree

        // Инициализация RecyclerView для отображения дерева
        recyclerView = findViewById(R.id.treeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TreeAdapter(tree) { selectedNode -> onNodeSelected(selectedNode) }
        recyclerView.adapter = adapter

        // Получение ссылок на кнопки из макета
        val addChildButton: Button = findViewById(R.id.addChildButton)
        val deleteNodeButton: Button = findViewById(R.id.deleteNodeButton)

        addChildButton.setOnClickListener {
            val nodeNameEditText: EditText = findViewById(R.id.nodeNameEditText)
            val nodeName = nodeNameEditText.text.toString()
            addChild(nodeName)
        }

        deleteNodeButton.setOnClickListener {
            deleteNode()
        }
    }

    // Обработка выбора узла в RecyclerView
    private fun onNodeSelected(selectedNode: Node) {
        currentNode = selectedNode
        updateUI()
    }

    // Обновление интерфейса пользователя после изменений в дереве
    private fun updateUI() {
        adapter.updateData(tree)
    }

    // Добавление дочернего узла
    private fun addChild(nodeName: String) {
        val newNode = Node(nodeName)
        currentNode.children.add(newNode)
        newNode.parent = currentNode
        updateUI()
        saveTreeState()
    }

    // Удаление текущего узла
    private fun deleteNode() {
        currentNode.parent?.children?.remove(currentNode)
        currentNode.parent = null
        updateUI()
        saveTreeState()
    }

    private fun saveTreeState() {
        val treeJson = gson.toJson(tree)
        // Сохранение состояния дерева в SharedPreferences или другом хранилище
    }

    private fun loadTreeState(): Node? {
        val savedTreeJson = "" // Получение сохраненного состояния из SharedPreferences или другого хранилища
        return if (savedTreeJson.isNotEmpty()) {
            gson.fromJson(savedTreeJson, object : TypeToken<Node>() {}.type)
        } else {
            null
        }
    }

    // Вызывается при выходе из приложения, сохранение состояния дерева
    override fun onPause() {
        super.onPause()
        saveTreeState()
    }
}