package eu.vendeli.ktgram.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe

class VersionComparisonTest :
    FunSpec({
        test("equal versions compare as equal") {
            compareVersions("1.9.0", "1.9.0") shouldBe 0
        }

        test("lower version compares as less") {
            compareVersions("1.8.1", "1.9.0") shouldBeLessThan 0
            compareVersions("1.9", "1.9.1") shouldBeLessThan 0
        }

        test("higher version compares as greater") {
            compareVersions("2.0.0", "1.9.9") shouldBeGreaterThan 0
            compareVersions("1.10.0", "1.9.0") shouldBeGreaterThan 0
        }

        test("missing segments are treated as zero") {
            compareVersions("1.9", "1.9.0") shouldBe 0
        }

        test("pre-release is lower than plain release") {
            compareVersions("1.9.0-RC", "1.9.0") shouldBeLessThan 0
            compareVersions("1.9.0", "1.9.0-RC") shouldBeGreaterThan 0
        }

        test("pre-release suffixes compare lexically") {
            compareVersions("1.9.0-RC", "1.9.0-RC2") shouldBeLessThan 0
        }

        test("dynamic versions are detected") {
            "1.+".isDynamicVersion().shouldBeTrue()
            "[1.0,2.0)".isDynamicVersion().shouldBeTrue()
            "latest.release".isDynamicVersion().shouldBeTrue()
        }

        test("plain versions are not detected as dynamic") {
            "1.9.0".isDynamicVersion().shouldBeFalse()
            "1.9.0-RC".isDynamicVersion().shouldBeFalse()
        }
    })
