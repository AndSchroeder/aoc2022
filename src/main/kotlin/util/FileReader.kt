package util

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object FileReader {

    fun getListByFile(day: Int, fileType: FileType) =
        Files.readAllLines(Paths.get("src/main/resources/$day/${fileType.fileName}.txt"), StandardCharsets.UTF_8)
}