package day

import util.FileReader


object Day07 : Day {
    override val examplePartOneSolution: String = "95437"
    override val examplePartTwoSolution: String = "24933642"

    override fun examplePartOne() = FileReader.getExampleList("07").interpret().solveOne()
        .apply { check(this == examplePartOneSolution) }

    override fun examplePartTwo() = FileReader.getExampleList("07").interpret().solveTwo()
        .apply { check(this == examplePartTwoSolution) }

    override fun solvePartOne() = FileReader.getInputList("07").interpret().solveOne()
    override fun solvePartTwo() = FileReader.getInputList("07").interpret().solveTwo()

    private var root: ElfDirectory = ElfDirectory("/", parent = null)
    private var current: ElfDirectory = root

    private fun ElfDirectory.solveOne(): String {
        val list = mutableListOf<Pair<String, Long>>()
        getDirectorySize(list, this)
        val solution = list.filter { it.second <= 100_000 }.sumOf{ it.second }.toString()
        root = ElfDirectory("/", parent = null)
        current = root
        return solution
    }

    private fun ElfDirectory.solveTwo(): String {
        val list = mutableListOf<Pair<String, Long>>()
        getDirectorySize(list, this)
        val freeSpace = 70_000_000 - root.size()
        val neededSpace = 30_000_000 - freeSpace
        val solution = list.filter { it.second >= neededSpace }.minBy { it.second }.second.toString()
        root = ElfDirectory("/", parent = null)
        current = root
        return solution
    }

    private fun List<String>.interpret() = forEach { interpret(it)}.let { root }

    private fun interpret(input: String) {
        with(input) {
            when {
                startsWith("$ cd") -> changeDirectory(replace("$ cd ",""))
                startsWith("$ ls") -> Unit
                startsWith("dir") -> current.createDirectory(replace("dir ",""))
                else -> current.createFile(input)
            }
        }
    }

    private fun getDirectorySize(list: MutableList<Pair<String, Long>>, directory: ElfDirectory) {
        list.add(directory.name to directory.size())
        directory.directories.forEach { getDirectorySize(list, it) }

    }

    private fun changeDirectory(name: String) {
        if (name == "/") return
        current = if (name == "..") {
            current.parent!!
        } else {
            current.findDirectory(name)!!
        }
    }
    data class ElfDirectory(
        val name: String,
        val files: MutableList<ElfFile> = mutableListOf(),
        val directories: MutableList<ElfDirectory> = mutableListOf(),
        val parent: ElfDirectory?
    ) {
        fun size(): Long {
            return files.sumOf(ElfFile::size) + directories.sumOf { it.size()}
        }

        fun findDirectory(name: String) = directories.find { it.name == name }
        private fun findFile(name: String) = files.find { it.name == name }

        fun createFile(input: String) {
            val (size, name) = input.split(" ")
            if (findFile(name) == null) {
                files.add(ElfFile(name = name, size = size.toLong()))
            }
        }

        fun createDirectory(name: String) {
            if (findDirectory(name) == null) {
                directories.add(ElfDirectory(name = name, parent = this))
            }
        }
    }

    data class ElfFile(
        val name: String,
        val size: Long,
    )
}


