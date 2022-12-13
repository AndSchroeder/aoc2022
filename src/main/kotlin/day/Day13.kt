package day

import util.FileReader

object Day13 : Day("13", "13", "140") {
    override fun examplePartOne() = FileReader.getExampleList(day).solveOne()
    override fun examplePartTwo() = FileReader.getExampleList(day).solveTwo()
    override fun solvePartOne() = FileReader.getInputList(day).solveOne()
    override fun solvePartTwo() = FileReader.getInputList(day).solveTwo()

    private fun List<String>.solveOne() = this.chunked(3).mapIndexed { index, chunk ->
        index + 1 to (chunk[0].parse() <= chunk[1].parse())
    }.filter { it.second }.sumOf { it.first }.toString()

    private fun List<String>.solveTwo(): String {
        val (dividerOne, dividerTwo) = "[[2]]" to "[[6]]"
        val list = (this + listOf(dividerOne, dividerTwo)).filterNot { it.isBlank() }.sortedBy { it.parse() }
        return ((list.indexOf(dividerOne) + 1) * (list.indexOf(dividerTwo) + 1)).toString()
    }

    private fun String.parse() = ElfPackage.ElfList().also { parseStep(it, this.shorten()) }

    private fun parseStep(elfList: ElfPackage.ElfList?, input: String) {
        if (elfList == null) return
        when {
            input.startsWith("[") -> ElfPackage.ElfList(parent = elfList).also { newList ->
                elfList.list.add(newList)
                parseStep(newList, input.shorten())
            }
            input.startsWith("]") -> parseStep(elfList.parent, input.shorten())
            input.startsWith(",") -> parseStep(elfList, input.shorten())
            input.isBlank() -> return
            else -> input.split(",").first().replace("]", "").toIntOrNull().also { number ->
                number?.let { elfList.list.add(ElfPackage.ElfNumber(number)) }
                parseStep(elfList, input.shorten())
            }
        }
    }

    sealed class ElfPackage : Comparable<ElfPackage> {
        class ElfNumber(val number: Int) : ElfPackage() {
            override fun toString()=  number.toString()
        }

        class ElfList(val list: MutableList<ElfPackage> = mutableListOf(), val parent: ElfList? = null) :
            ElfPackage() {
            override fun toString()= "[${list.joinToString(",")}]"
        }

        override fun compareTo(other: ElfPackage): Int = when {
            this is ElfNumber && other is ElfNumber -> this.number - other.number
            this is ElfList && other is ElfList -> this.compare(other)
            this is ElfNumber && other is ElfList -> ElfList(mutableListOf(this)).compareTo(other)
            this is ElfList && other is ElfNumber -> this.compareTo(ElfList(mutableListOf(other)))
            else -> 1
        }

        private fun ElfList.compare(other: ElfList): Int {
            val (thisIterator, otherIterator) = this.list.iterator() to other.list.iterator()
            while (thisIterator.hasNext() && otherIterator.hasNext()) {
                val result = thisIterator.next() compareTo otherIterator.next()
                if (result != 0) return result
            }
            return thisIterator.hasNext() compareTo otherIterator.hasNext()
        }
    }

    private fun String.shorten(start: Int = 1) = substring(start until length)
}
