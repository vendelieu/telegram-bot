import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ScGenerator : DefaultTask() {
    private val apiFiles = project.layout.projectDirectory
        .dir("telegram-bot/src/commonMain/kotlin/eu/vendeli/tgbot/api").asFileTree.files
    private val funDeclaration = Regex("fun\\s+\\w+\\s*\\([^)]*\\)")

    @TaskAction
    fun generateExtensions() {
        var imports =
            "@file:Suppress(\"KotlinRedundantDiagnosticSuppress\")\n\npackage eu.vendeli.ktgram.extutils\n\n" +
                "import eu.vendeli.tgbot.TelegramBot\n"
        var extensions = ""

        apiFiles.parallelStream().forEach { file ->
            if (!file.isKotlinFile()) return@forEach

            val fileContent = file.readText()
            val methods = funRegex.findAll(fileContent)
            val import = file.path.substringAfter("commonMain/kotlin/")
                .replace('/', '.')
                .split('.').dropLast(2)

            methods.forEach m@{
                val function = (funDeclaration.find(it.value)?.value ?: return@m)
                    .replace("fun ", "")
                val name = function.substringBefore("(")
                val parameters = function
                    .substringAfter("(")
                    .substringBefore(")")
                    .split(",")
                    .joinToString { p ->
                        if (p.isBlank()) return@joinToString ""
                        p.substringBefore(":")
                    }

                val funRef = "$name($parameters)"

                imports += "import " + import.joinToString(".") + ".$name\n"

                extensions += "@Suppress(\"NOTHING_TO_INLINE\")\n" +
                    "inline fun TelegramBot.${function.replace("(", "Sc(")} = $funRef\n"
            }
            val outputFile = project.layout.projectDirectory
                .dir("ktgram-utils/src/commonMain/kotlin/eu/vendeli/ktgram/extutils")
                .file("TelegramBotSc.kt").asFile

            outputFile.writeText(imports + extensions)
        }
    }
}

