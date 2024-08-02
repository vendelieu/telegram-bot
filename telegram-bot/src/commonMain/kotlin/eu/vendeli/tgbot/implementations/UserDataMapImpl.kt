package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.ctx.UserData

class UserDataMapImpl :
    BotContextMapImpl(),
    UserData<String>
