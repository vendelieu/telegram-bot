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

class LoremFlickrPic : RandomPicSource() {
    override fun getPicUrl(height: Int, width: Int): String =
        "https://loremflickr.com/$height/$width"

    override fun getPicUrl(): String = "https://loremflickr.com/10/10"
}

object RandomPicResource :
    ResourcePicker<RandomPicSource>(listOf(UnsplashRandomPic(), LoremPicsumPic(), LoremFlickrPic())) {
    val RANDOM_PIC_URL get() = current.getPicUrl()
}
