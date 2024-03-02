@file:Suppress("unused")

import io.kotest.common.ExperimentalKotest
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.test.TestCaseOrder

object ProjectConfig : AbstractProjectConfig() {
    @ExperimentalKotest
    override val concurrentTests: Int = 1

    @ExperimentalKotest
    override val concurrentSpecs: Int = 1
    override val parallelism: Int = 1

    override val testCaseOrder: TestCaseOrder = TestCaseOrder.Sequential
}
