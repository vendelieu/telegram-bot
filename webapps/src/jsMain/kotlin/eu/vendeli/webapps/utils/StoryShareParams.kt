package eu.vendeli.webapps.utils

external interface StoryShareParams {
    val text: String

    @JsName("widget_link")
    val widgetLink: StoryWidgetLink?
}

external interface StoryWidgetLink {
    val url: String
    val name: String?
}
