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
    private val funRegex = Regex("(@\\w+\\s*\\(\\\".*\\\"\\))?\\s*(inline\\s+)?fun\\s+(\\w+)\\s*\\((.*)\\)")
    private val kdocRegex = Regex("\\n/\\*\\*.*\\*/", RegexOption.DOT_MATCHES_ALL)
    private val NEWLINE = "\n"
    private val apiFiles = project.layout.projectDirectory
        .dir("src/commonMain/kotlin/eu/vendeli/tgbot/api").asFileTree.files

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
            if (file.isFile && file.extension == "kt") {
                // Read file content
                val fileContent = file.readText()
                var modifiedContent = fileContent.replace(kdocRegex, "") // remove old kdocs
                val method = funRegex.find(modifiedContent) ?: return@forEach

                val methodName = method.groups[3]!!.value
                val methodMeta = jsonRes.methods[methodName]
                    ?: jsonRes.methods["send" + methodName.beginWithUpperCase()]
                    ?: return@forEach
                var kdoc = "/**\n"
                kdoc += methodMeta.description.joinToString("\n * ", " * ")
                kdoc += "$NEWLINE * "

                kdoc += methodMeta.fields.joinToString("\n * ") {
                    "@param " + it.name.snakeToCamelCase() + " " +
                        " " + if (it.required) "Required" else "" + it.description
                }

                kdoc += NEWLINE + methodMeta.returns.joinToString("|", " * @returns ") { "[$it]" }
                kdoc += NEWLINE + " * Api reference: ${methodMeta.href}\n*/\n"

                modifiedContent = modifiedContent.replace(method.value, kdoc + method.value)

                file.writeText(modifiedContent)
            }
        }
    }
}
