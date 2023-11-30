import io.kotest.core.annotation.EnabledCondition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass

class ChatTestingOnlyCondition : EnabledCondition {
    override fun enabled(kclass: KClass<out Spec>): Boolean =
        System.getenv("CHAT_ID") != null
}
