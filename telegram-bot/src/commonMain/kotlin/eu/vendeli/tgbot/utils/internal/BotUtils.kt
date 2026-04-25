package eu.vendeli.tgbot.utils.internal

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.core.ProcessingPipePhase
import eu.vendeli.tgbot.core.interceptors.SessionTrackingInterceptor

fun TelegramBot.setupSessionManager() {
    val cfg = config.sessions
    logger.debug("Initializing session manager")
    cfg.manager = cfg.managerFactory.create(this, cfg)
    update.pipeline.intercept(
        ProcessingPipePhase.Setup,
        SessionTrackingInterceptor(cfg.manager),
    )
}
