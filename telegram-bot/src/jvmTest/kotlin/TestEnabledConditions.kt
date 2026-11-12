import io.kotest.core.annotation.Condition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass
import utils.TestEnv
import utils.TestBackends

/** The mock can run every spec, against real Telegram a spec needs its credential. */
private fun KClass<out Spec>.isEnabledWith(envKey: String): Boolean =
    TestBackends.default.isMock || TestEnv.has(envKey)

class ChatTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = kclass.isEnabledWith("CHAT_ID")
}

class ChannelTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = kclass.isEnabledWith("CHANNEL_ID")
}

class PaymentProviderTestingOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = kclass.isEnabledWith("PAYMENT_PROVIDER_TOKEN")
}

/** For specs that only make sense against the real network (timeouts, rate limits, long waits). */
class LiveOnlyCondition : Condition {
    override fun evaluate(kclass: KClass<out Spec>): Boolean = TestBackends.default.isLive
}
