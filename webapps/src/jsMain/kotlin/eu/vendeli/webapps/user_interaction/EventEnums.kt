package eu.vendeli.webapps.user_interaction

@Suppress("EnumEntryName")
enum class FullscreenError {
    unsupported,
    already_fullscreen,
}

@Suppress("EnumEntryName")
enum class AccelerometerError {
    unsupported,
}

@Suppress("EnumEntryName")
enum class DeviceOrientationError {
    unsupported,
}

@Suppress("EnumEntryName")
enum class HomeScreenStatus {
    unsupported,
    unknown,
    added,
    missed,
}

@Suppress("EnumEntryName")
enum class GyroscopeError {
    unsupported,
}

@Suppress("EnumEntryName")
enum class ShareMessageError {
    unsupported,
    message_expired,
    message_send_failed,
    user_declined,
    unknown_error,
}

@Suppress("EnumEntryName")
enum class EmojiStatusError {
    unsupported,
    suggested_emoji_invalid,
    duration_invalid,
    user_declined,
    server_error,
    unknown_error,
}

@Suppress("EnumEntryName")
enum class EmojiStatusAccess {
    allowed,
    cancelled,
}

@Suppress("EnumEntryName")
enum class FileDownloadStatus {
    downloading,
    cancelled,
}
