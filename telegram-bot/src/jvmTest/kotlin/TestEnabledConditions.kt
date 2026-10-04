import io.kotest.core.annotation.Condition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass
import utils.TestEnv
import utils.replay.CassetteStore
import utils.replay.TestMode

/** In replay mode a spec is enabled when it has recorded fixtures, otherwise when its credential is configured. */
private fun KClass<out Spec>.isEnabledWith(envKey: String): Boolean =
    if (TestMode.current.isReplay) CassetteStore.exists(qualifiedName.orEmpty()) else TestEnv.has(envKey)

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
    override fun evaluate(kclass: KClass<out Spec>): Boolean = !TestMode.current.isReplay
}
