
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

@Serializable
data class Api(
    val methods: Map<String, ApiEntity>,
    val types: Map<String, ApiEntity>,
)

@Serializable
data class ApiEntity(
    var name: String,
    var href: String,
    var description: List<String>,
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
    private val kdocRegex = Regex("\\n/\\*\\*.*\\*/", RegexOption.DOT_MATCHES_ALL)
    private val NEWLINE = "\n"
    private val apiFiles = project.layout.projectDirectory
        .dir("src/commonMain/kotlin/eu/vendeli/tgbot/api").asFileTree.files
    private val typeFiles = project.layout.projectDirectory
        .dir("src/commonMain/kotlin/eu/vendeli/tgbot/types").asFileTree.files.filter {
            !it.path.contains("internal")
        }

    private fun String.beginWithUpperCase(): String = when (this.length) {
        0 -> ""
        1 -> uppercase()
        else -> this[0].uppercase() + this.substring(1)
    }

    private fun String.snakeToCamelCase() = split('_').mapIndexed { i, it ->
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

            val methodName = method.groups[3]!!.value
            val methodMeta = jsonRes.methods[methodName]
                ?: jsonRes.methods["send" + methodName.beginWithUpperCase()]
                ?: return@forEach
            var kdoc = "/**$NEWLINE"
            kdoc += methodMeta.description.joinToString("$NEWLINE * ", " * ")
            kdoc += "$NEWLINE * Api reference: ${methodMeta.href}"
            kdoc += "$NEWLINE * "

            kdoc += methodMeta.fields.joinToString("$NEWLINE * ") {
                "@param " + it.name.snakeToCamelCase() + " " + it.description
            }

            kdoc += NEWLINE + methodMeta.returns.joinToString("|", " * @returns ") { "[$it]" }
            kdoc += "$NEWLINE */$NEWLINE"

            modifiedContent = modifiedContent.replace(method.value, kdoc + method.value)

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

            var kdoc = "/**$NEWLINE"
            kdoc += classMeta.description.joinToString("$NEWLINE * ", " * ")
            kdoc += NEWLINE + " * Api reference: ${classMeta.href}"
            kdoc += "$NEWLINE * "
            kdoc += classMeta.fields.joinToString("$NEWLINE * ") {
                "@property " + it.name.snakeToCamelCase() + " " + it.description
            }
            kdoc += "$NEWLINE */$NEWLINE"
            modifiedContent = modifiedContent.replace(clazz.value, kdoc + clazz.value)

            file.writeText(modifiedContent)
        }
    }
}
