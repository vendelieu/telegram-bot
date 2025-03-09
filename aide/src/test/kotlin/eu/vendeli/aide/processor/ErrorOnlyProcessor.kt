package eu.vendeli.aide.processor

class ErrorOnlyProcessor : AbstractTestProcessor() {
    override fun StringBuilder.analyse(data: AnalysisData) = Unit
}
