package services

import org.apache.maven.plugin.logging.Log
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class FileService(private val logger: Log) {

    private val tempDir = System.getProperty("java.io.tmpdir")
    private val tempPath = if (tempDir.endsWith(File.pathSeparator)) {
        tempDir + "nimbus" + File.pathSeparator
    } else {
        tempDir + File.pathSeparator + "nimbus" + File.pathSeparator
    }

    fun getFileText(path: String): String {
        return File(path).inputStream().bufferedReader().use{it.readText()}
    }

    fun replaceInFile(wordsToReplace: Map<String, String?>, file: File): File {
        val charset = StandardCharsets.UTF_8

        var content = String(file.readBytes(), charset)
        for ((from, to) in wordsToReplace) {
            if (to != null) content = content.replace(from, to)
        }

        val newFile = File(tempPath + file.name)
        newFile.parentFile.mkdirs()
        newFile.writeBytes(content.toByteArray(charset))
        return newFile
    }

    fun saveFile(contents: String, path: String) {
        val file = File(path)
        file.parentFile.mkdirs()

        file.outputStream().bufferedWriter().use{it.write(contents)}
    }

    companion object {
        @JvmStatic
        fun addDirectorySeparatorIfNecessary(path: String): String {
            if (path.endsWith(File.separatorChar)) return path
            return "$path${File.separator}"
        }
    }
}