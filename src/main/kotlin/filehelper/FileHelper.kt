package filehelper

object FileHelper {
    fun readFile(fileName: String): List<String> {
        return this::class.java.classLoader.getResource(fileName).readText().lines()
    }
}