import java.io.File

internal fun File.isKotlinFile() = isFile && extension == "kt"

internal val funRegex = Regex(
    "(@\\w+\\s*\\(\\\".*\\\"\\))?\\s*(inline\\s+)?fun\\s+(\\w+)\\s*\\((.*)\\)", RegexOption.DOT_MATCHES_ALL,
)
