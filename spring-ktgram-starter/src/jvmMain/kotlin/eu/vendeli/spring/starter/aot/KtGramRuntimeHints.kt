package eu.vendeli.spring.starter.aot

import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider

class KtGramRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val scanner = object : ClassPathScanningCandidateComponentProvider(false) {
            override fun isCandidateComponent(bd: AnnotatedBeanDefinition) = true
        }.apply {
            addIncludeFilter { _, _ -> true }
            classLoader?.let { resourceLoader = org.springframework.core.io.DefaultResourceLoader(it) }
        }

        scanner.findCandidateComponents("eu.vendeli.tgbot.types").forEach { bd ->
            hints.reflection().registerType(
                TypeReference.of(bd.beanClassName!!),
                MemberCategory.ACCESS_DECLARED_FIELDS,
                MemberCategory.INVOKE_DECLARED_METHODS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
            )
        }
    }
}
