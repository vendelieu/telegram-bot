package utils

class LoopedRange(
    start: Int,
    count: Int,
) : Iterable<Int> {
    private val range = start..<start + count * 1
    private var currentIndex = start
    val currentIdx: Int get() = currentIndex
    val iterator = iterator()

    override fun iterator(): Iterator<Int> = object : Iterator<Int> {
        override fun hasNext(): Boolean = true

        override fun next(): Int {
            currentIndex++
            if (currentIndex > range.last()) {
                currentIndex = range.first
            }
            return currentIndex
        }
    }
}
