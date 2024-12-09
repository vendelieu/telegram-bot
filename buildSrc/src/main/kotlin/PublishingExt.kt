import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property

open class PublishingExtension(
    factory: ObjectFactory,
) {
    val name = factory.property<String>()
    val description = factory.property<String>()
}
