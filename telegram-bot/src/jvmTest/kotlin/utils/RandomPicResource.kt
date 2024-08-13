package utils

abstract class RandomPicSource {
    abstract fun getPicUrl(height: Int, width: Int): String

    abstract fun getPicUrl(): String
}

class UnsplashRandomPic : RandomPicSource() {
    override fun getPicUrl(height: Int, width: Int): String =
        "https://source.unsplash.com/random/${height}x$width?sig=incrementingIdentifier"

    override fun getPicUrl(): String = "https://source.unsplash.com/random/10x10?sig=incrementingIdentifier"
}

class LoremPicsumPic : RandomPicSource() {
    override fun getPicUrl(height: Int, width: Int): String =
        "https://picsum.photos/$height/$width"

    override fun getPicUrl(): String = "https://picsum.photos/10"
}

object RandomPicResource :
    ResourcePicker<RandomPicSource>(listOf(UnsplashRandomPic(), LoremPicsumPic())) {
    val RANDOM_PIC_URL get() = current.getPicUrl()
}
