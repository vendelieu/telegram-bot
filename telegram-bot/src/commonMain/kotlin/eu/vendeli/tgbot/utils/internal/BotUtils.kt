package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.ProcessingPipePhase
import eu.vendeli.tgbot.core.interceptors.SessionTrackingInterceptor

fun TelegramBot.setupSessionManager() {
    config.sessions?.let { cfg ->
        logger.debug("Initializing session manager")
        cfg.manager = cfg.managerFactory.create(this, cfg)
        if (cfg.trackIncoming) {
            logger.debug("Tracking incoming messages")
            update.pipeline.intercept(
                ProcessingPipePhase.Setup,
                SessionTrackingInterceptor(cfg.manager),
            )
        }
    }
}
