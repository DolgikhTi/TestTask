package com.example.familytree

import java.security.MessageDigest

//Узел дерева
data class Node(
    val name: String,
    val children: MutableList<Node> = mutableListOf(),
    var parent: Node? = null
) {
    fun generateNodeName(): String {
        // Генерация названия из последних 20 байт хэша узла
        val hash = MessageDigest.getInstance("SHA-256").digest(name.toByteArray())
        val truncatedHash = hash.takeLast(20).toByteArray()
        return truncatedHash.joinToString("") { "%02x".format(it) }
    }
}
