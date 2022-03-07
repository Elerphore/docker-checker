import java.io.File
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.system.exitProcess

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.URLEncoder

processBuilder()

fun processBuilder() {
    val processBuilder = ProcessBuilder()
    processBuilder.command("bash", "-c", "docker container ps -a")

    try {
                                                    
        val process: Process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val line = reader.readText();
        
        val exitVal = process.waitFor()
        if (exitVal == 0) {
            generateTextFile(line)
            exitProcess(0)
        } else {
            exitProcess(exitVal)
        }
        
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}

fun generateTextFile(text: String) {
    val values = mapOf("content" to text)
    val client = HttpClient.newBuilder().build();
    val url = ""
    val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(formData(values))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString());
}

fun String.utf8(): String = URLEncoder.encode(this, "UTF-8")

fun formData(data: Map<String, String>): HttpRequest.BodyPublisher? {

    val res = data.map {(k, v) -> "${(k.utf8())}=${v.utf8()}"}
        .joinToString("&")

    return HttpRequest.BodyPublishers.ofString(res)
}
