package com.example.demo.console

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

private val mapper = jacksonObjectMapper()

fun main() {
    println("Consola Vecindario API")
    val baseUrl = prompt("Base URL (ej. https://localhost:8080)", "http://localhost:8080")
    if (!baseUrl.startsWith("https://")) {
        println("Aviso: estas usando HTTP. El requerimiento pide HTTPS.")
    }

    val client = ApiClient(baseUrl)

    while (true) {
        if (!client.isAuthenticated()) {
            println("\nDebes iniciar sesion para continuar.")
            login(client)
            continue
        }

        println("\nMenu")
        println("1. Crear comunidad")
        println("2. Crear usuario")
        println("3. Login (obtener JWT)")
        println("4. Publicar aviso")
        println("5. Listar avisos")
        println("6. Comentar aviso")
        println("7. Marcar aviso como atendido")
        println("8. Eliminar aviso")
        println("9. Logout")
        println("10. Salir")

        when (prompt("Opcion")) {
            "1" -> createCommunity(client)
            "2" -> createUser(client)
            "3" -> login(client)
            "4" -> createNotice(client)
            "5" -> listNotices(client)
            "6" -> createComment(client)
            "7" -> markAttended(client)
            "8" -> deleteNotice(client)
            "9" -> {
                client.clearToken()
                println("Sesion cerrada.")
            }
            "10" -> return
            else -> println("Opcion invalida")
        }
    }
}

private fun createCommunity(client: ApiClient) {
    val name = prompt("Nombre comunidad")
    val address = prompt("Direccion comunidad")

    val body = mapOf("name" to name, "address" to address)
    val response = client.post("/api/communities", body)
    println(response)
}

private fun createUser(client: ApiClient) {
    val communityId = promptLong("Community ID")
    val name = prompt("Nombre")
    val email = prompt("Email")
    val password = prompt("Password")
    val address = prompt("Direccion")

    val body = mapOf(
        "communityId" to communityId,
        "name" to name,
        "email" to email,
        "password" to password,
        "address" to address,
    )
    val response = client.post("/api/users", body)
    println(response)
}

private fun login(client: ApiClient) {
    val email = prompt("Email")
    val password = prompt("Password")

    val body = mapOf("email" to email, "password" to password)
    val response = client.post("/api/auth/login", body)
    println(response)

    val token = extractToken(response)
    if (!token.isNullOrBlank()) {
        client.setToken(token)
        println("Token guardado para siguientes requests.")
    }
}

private fun createNotice(client: ApiClient) {
    val communityId = promptLong("Community ID")
    val authorUserId = promptLong("Author User ID")
    val category = prompt("Categoria (HELP, ALERT, PURCHASES, MEETING, LOST_PET, OTHER)")
    val title = prompt("Titulo")
    val description = prompt("Descripcion")

    val body = mapOf(
        "communityId" to communityId,
        "authorUserId" to authorUserId,
        "category" to category,
        "title" to title,
        "description" to description,
    )
    val response = client.post("/api/notices", body)
    println(response)
}

private fun listNotices(client: ApiClient) {
    val communityId = promptOptionalLong("Community ID (opcional)")
    val category = promptOptional("Categoria (opcional)")

    val query = buildString {
        if (communityId != null) append("communityId=$communityId")
        if (category != null) {
            if (isNotEmpty()) append("&")
            append("category=$category")
        }
    }
    val path = if (query.isBlank()) "/api/notices" else "/api/notices?$query"
    val response = client.get(path)
    println(response)
}

private fun createComment(client: ApiClient) {
    val noticeId = promptLong("Notice ID")
    val authorUserId = promptLong("Author User ID")
    val message = prompt("Comentario")

    val body = mapOf(
        "noticeId" to noticeId,
        "authorUserId" to authorUserId,
        "message" to message,
    )
    val response = client.post("/api/comments", body)
    println(response)
}

private fun markAttended(client: ApiClient) {
    val noticeId = promptLong("Notice ID")
    val response = client.put("/api/notices/$noticeId/attended", emptyMap<String, Any>())
    println(response)
}

private fun deleteNotice(client: ApiClient) {
    val noticeId = promptLong("Notice ID")
    val response = client.delete("/api/notices/$noticeId")
    println(response)
}

private fun extractToken(response: String): String? {
    val idx = response.indexOf("\n")
    if (idx == -1) return null
    val body = response.substring(idx + 1)
    return try {
        val parsed: Map<String, Any> = mapper.readValue(body)
        parsed["token"]?.toString()
    } catch (_: Exception) {
        null
    }
}

private fun prompt(label: String, default: String? = null): String {
    val suffix = if (default == null) ": " else " [$default]: "
    print(label + suffix)
    val input = readLine()?.trim().orEmpty()
    return if (input.isBlank() && default != null) default else input
}

private fun promptOptional(label: String): String? {
    val value = prompt(label)
    return if (value.isBlank()) null else value
}

private fun promptLong(label: String): Long {
    while (true) {
        val value = prompt(label)
        val parsed = value.toLongOrNull()
        if (parsed != null) return parsed
        println("Valor invalido, intenta de nuevo")
    }
}

private fun promptOptionalLong(label: String): Long? {
    val value = prompt(label)
    return if (value.isBlank()) null else value.toLongOrNull()
}

private class ApiClient(baseUrl: String) {
    private val base = baseUrl.removeSuffix("/")
    private val client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build()
    private var token: String? = null

    fun setToken(value: String) {
        token = value
    }
    
    fun clearToken() {
        token = null
    }

    fun isAuthenticated(): Boolean = !token.isNullOrBlank()

    fun get(path: String): String = request("GET", path, null)

    fun post(path: String, body: Any): String = request("POST", path, body)

    fun put(path: String, body: Any): String = request("PUT", path, body)

    fun delete(path: String): String = request("DELETE", path, null)

    private fun request(method: String, path: String, body: Any?): String {
        val uri = URI.create(base + path)
        val builder = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(Duration.ofSeconds(20))
            .header("Content-Type", "application/json")

        if (!token.isNullOrBlank()) {
            builder.header("Authorization", "Bearer $token")
        }

        if (body != null) {
            val json = mapper.writeValueAsString(body)
            builder.method(method, HttpRequest.BodyPublishers.ofString(json))
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody())
        }

        val request = builder.build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return formatResponse(response.statusCode(), response.body())
    }

    private fun formatResponse(status: Int, body: String): String {
        val pretty = try {
            val json: Any = mapper.readValue(body)
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json)
        } catch (_: Exception) {
            body
        }
        return "Status: $status\n$pretty"
    }
}
