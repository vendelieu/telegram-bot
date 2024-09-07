package eu.vendeli.webapps.ui

external interface ThemeParams {
    @JsName("bg_color")
    val backgroundColor: String?

    @JsName("secondary_bg_color")
    val secondaryBackgroundColor: String?

    @JsName("text_color")
    val textColor: String?

    @JsName("hint_color")
    val hintColor: String?

    @JsName("link_color")
    val linkColor: String?

    @JsName("button_color")
    val buttonColor: String?

    @JsName("button_text_color")
    val buttonTextColor: String?

    @JsName("header_bg_color")
    val headerBgColor: String?

    @JsName("bottom_bar_bg_color")
    val bottomBarBgColor: String?

    @JsName("accent_text_color")
    val accentTextColor: String?

    @JsName("section_bg_color")
    val sectionBgColor: String?

    @JsName("section_header_text_color")
    val sectionHeaderTextColor: String?

    @JsName("subtitle_text_color")
    val subtitleTextColor: String?

    @JsName("section_separator_color")
    val sectionSeparatorColor: String?

    @JsName("destructive_text_color")
    val destructiveTextColor: String?
}
