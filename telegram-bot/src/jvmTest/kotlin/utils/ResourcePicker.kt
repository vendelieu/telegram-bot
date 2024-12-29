package utils

abstract class ResourcePicker<T>(
    resource: List<T>,
) {
    private var pool: LoopedIterator<T> = resource.iterateLooping()

    fun swapAndGet(): T {
        pool.next()
        return pool.current()
    }

    val current: T
        get() = pool.current()
}
