package eu.vendeli.tgbot.interfaces

abstract class KeyboardBuilder<T : Button> {
    protected abstract val buttons: MutableList<T?>

    /**
     * The function that collects and returns [Keyboard]
     *
     * @return
     */
    internal fun build(): List<List<T>> {
        val builtButtons = mutableListOf<List<T>>()

        val buttonsIterator = buttons.iterator()
        var currentLine: MutableList<T> = mutableListOf()

        while (buttonsIterator.hasNext()) {
            val item = buttonsIterator.next()
            if (item == null) {
                builtButtons.add(currentLine)
                currentLine = mutableListOf()
                continue
            }
            currentLine.add(item)
        }
        if (currentLine.isNotEmpty()) builtButtons.add(currentLine)

        return builtButtons
    }
}
