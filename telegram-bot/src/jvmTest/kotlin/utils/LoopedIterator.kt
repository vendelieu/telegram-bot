package utils

import java.util.concurrent.atomic.AtomicInteger

class LoopedIterator<T>(
    private val data: List<T>,
) : Iterator<T> {
    private val currentIdx = AtomicInteger()

    private fun getNextIdx(): Int {
        if (currentIdx.get() == data.lastIndex) {
            currentIdx.set(0)
            return 0
        }

        return currentIdx.incrementAndGet()
    }

    fun current() = data[currentIdx.get()]

    override fun next(): T = data[getNextIdx()]

    override fun hasNext(): Boolean = true
}

inline fun <T> List<T>.iterateLooping() = LoopedIterator(this)
