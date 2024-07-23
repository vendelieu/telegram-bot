package eu.vendeli.tgbot.implementations

import eu.vendeli.tgbot.interfaces.UserData

class UserDataMapImpl :
    BotContextMapImpl(),
    UserData<String>
