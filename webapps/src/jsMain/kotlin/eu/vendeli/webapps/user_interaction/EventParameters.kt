package eu.vendeli.webapps.user_interaction

import eu.vendeli.webapps.sensors.LocationData

external interface FullscreenFailedInfo {
    val error: String
}

val FullscreenFailedInfo.Error: FullscreenError
    get() = FullscreenError.valueOf(error)

external interface HomeScreenCheckedInfo {
    val status: String
}

val HomeScreenCheckedInfo.Status: HomeScreenStatus
    get() = HomeScreenStatus.valueOf(status)

external interface AccelerometerFailedInfo {
    val error: String
}

val AccelerometerFailedInfo.Error: AccelerometerError
    get() = AccelerometerError.valueOf(error)

external interface DeviceOrientationFailedInfo {
    val error: String
}

val DeviceOrientationFailedInfo.Error: DeviceOrientationError
    get() = DeviceOrientationError.valueOf(error)

external interface GyroscopeFailedInfo {
    val error: String
}

val GyroscopeFailedInfo.Error: GyroscopeError
    get() = GyroscopeError.valueOf(error)

external interface LocationRequestedInfo {
    val locationData: LocationData
}

external interface ShareMessageFailedInfo {
    val error: String
}

val ShareMessageFailedInfo.Error: ShareMessageError
    get() = ShareMessageError.valueOf(error)

external interface EmojiStatusFailedInfo {
    val error: String
}

val EmojiStatusFailedInfo.Error: EmojiStatusError
    get() = EmojiStatusError.valueOf(error)

external interface EmojiStatusAccessRequestedInfo {
    val status: String
}

val EmojiStatusAccessRequestedInfo.Status: EmojiStatusAccess
    get() = EmojiStatusAccess.valueOf(status)

external interface FileDownloadRequestedInfo {
    val status: String
}

val FileDownloadRequestedInfo.Status: FileDownloadStatus
    get() = FileDownloadStatus.valueOf(status)
