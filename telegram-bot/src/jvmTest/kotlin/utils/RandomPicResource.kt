package utils

abstract class RandomPicSource {
    abstract fun getPicUrl(height: Int, width: Int): String

    abstract fun getPicUrl(): String
}

class LoremPicsumPic : RandomPicSource() {
    override fun getPicUrl(height: Int, width: Int): String =
        "https://picsum.photos/$height/$width"

    override fun getPicUrl(): String = "https://picsum.photos/10"
}

object RandomPicResource :
    ResourcePicker<RandomPicSource>(listOf(LoremPicsumPic())) {
    val RANDOM_PIC_URL get() = current.getPicUrl()
}
