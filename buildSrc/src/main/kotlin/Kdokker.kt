import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

@Serializable
data class Api(
    val methods: Map<String, ApiEntity>,
    val types: Map<String, ApiEntity>,
)

@Serializable
data class ApiEntity(
    var name: String,
    var href: String,
    var description: List<String>? = null,
    var returns: List<String> = emptyList(),
    var fields: List<Field> = emptyList(),
)

@Serializable
data class Field(
    var name: String,
    var types: List<String>,
    var required: Boolean,
    var description: String,
)

private val mapper = Json { ignoreUnknownKeys = true }

abstract class Kdokker : DefaultTask() {
    private val jsonRes = mapper.decodeFromString<Api>(javaClass.getResource("api.json")!!.readText())
    private val classRegex = Regex(
        "@Serializable(?:\\(.*\\))?\\s(?:data|sealed)?\\s?class\\s+(\\w+)\\s*(?:\\(|\\{)",
        RegexOption.DOT_MATCHES_ALL,
    )
    private val funRegex = Regex(
        "(@\\w+(\\([^\\)]*\\))?\\s*)*\\b(inline\\s+)?fun\\s+(\\w+)",
    )
    private val kdocRegex = Regex("\\n/\\*\\*.*\\*/", RegexOption.DOT_MATCHES_ALL)
    private val newline = "\n"
    private val apiFiles = project.layout.projectDirectory
        .dir("src/commonMain/kotlin/eu/vendeli/tgbot/api")
        .asFileTree.files
    private val typeFiles = project.layout.projectDirectory
        .dir("src/commonMain/kotlin/eu/vendeli/tgbot/types")
        .asFileTree.files
        .filter {
            !it.path.contains("component") && !it.path.contains("options") && !it.path.contains("configuration")
        }

    private fun File.isKotlinFile() = isFile && extension == "kt"

    private fun String.beginWithUpperCase(): String = when (this.length) {
        0 -> ""
        1 -> uppercase()
        else -> this[0].uppercase() + this.substring(1)
    }

    private fun String.snakeToCamelCase() = split('_')
        .mapIndexed { i, it ->
            if (i == 0) return@mapIndexed it
            it.beginWithUpperCase()
        }.joinToString("")

    @TaskAction
    fun updateKDoc() {
        apiFiles.parallelStream().forEach { file ->
            if (!file.isKotlinFile()) return@forEach
            // Read file content
            val fileContent = file.readText()
            var modifiedContent = fileContent.replace(kdocRegex, "") // remove old kdocs
            val method = funRegex.find(modifiedContent) ?: return@forEach

            val methodName = method.groups[4]!!.value
            val methodMeta = jsonRes.methods[methodName]
                ?: jsonRes.methods["send" + methodName.beginWithUpperCase()]
                ?: return@forEach
            var kdoc = "/**$newline"
            kdoc += methodMeta.description?.joinToString("$newline * ", " * ") ?: ""
            kdoc += "$newline *$newline * [Api reference](${methodMeta.href})"
            kdoc += "$newline *"

            kdoc += methodMeta.fields.takeIf { it.isNotEmpty() }?.joinToString("$newline * ", prefix = " ") {
                "@param " + it.name.snakeToCamelCase() + " " + it.description
            } ?: ""

            kdoc += newline + methodMeta.returns.joinToString("|", " * @returns ") { "[$it]" }
            kdoc += "$newline */$newline"

            modifiedContent = modifiedContent.replaceFirst(method.value, kdoc + method.value)

            file.writeText(modifiedContent)
        }

        typeFiles.parallelStream().forEach { file ->
            if (!file.isKotlinFile()) return@forEach
            val fileContent = file.readText()
            var modifiedContent = fileContent.replace(kdocRegex, "") // remove old kdocs
            val clazz = classRegex.find(modifiedContent) ?: return@forEach
            val className = clazz.groups.last()!!.value
            val classMeta = jsonRes.types[className]
            if (classMeta == null) {
                logger.warn("Class $className details not found")
                return@forEach
            }

            var kdoc = "/**$newline"
            kdoc += classMeta.description?.joinToString("$newline * ", " * ") ?: ""
            kdoc += "$newline *$newline * [Api reference](${classMeta.href})"
            kdoc += "$newline *"
            kdoc += classMeta.fields.takeIf { it.isNotEmpty() }?.joinToString("$newline * ", prefix = " ") {
                "@property " + it.name.snakeToCamelCase() + " " + it.description
            } ?: ""
            kdoc += "$newline */$newline"
            modifiedContent = modifiedContent.replace(clazz.value, kdoc + clazz.value)

            file.writeText(modifiedContent)
        }
    }
}
