package utils

abstract class ResourcePicker<T>(resource: List<T>) {
    private val range = LoopedRange(0, resource.size)
    private var pool: List<T> = listOf()

    init {
        this.pool = resource
    }

    fun swapAndGet(): T {
        range.iterator.next()
        return pool[range.currentIdx]
    }

    val current: T
        get() = pool[range.currentIdx]
}
