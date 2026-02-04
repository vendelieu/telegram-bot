import io.kotest.core.annotation.Condition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass

class ChatTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean {
        return System.getenv("CHAT_ID") != null
    }
}

class ChannelTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean {
        return System.getenv("CHANNEL_ID") != null
    }
}

class PaymentProviderTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean {
        return System.getenv("PAYMENT_PROVIDER_TOKEN") != null
    }
}
