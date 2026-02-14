import io.kotest.core.annotation.Condition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass
import utils.TestEnv

class ChatTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = TestEnv.has("CHAT_ID")
}

class ChannelTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = TestEnv.has("CHANNEL_ID")
}

class PaymentProviderTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = TestEnv.has("PAYMENT_PROVIDER_TOKEN")
}
