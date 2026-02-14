package utils

import io.github.cdimascio.dotenv.dotenv

/**
 * Centralized environment loader for tests. Loads variables from `.env` (project root)
 * with fallback to system environment. Host env vars take precedence over `.env`.
 *
 * Use [get] for string values. Use [getLong] for optional Long (e.g. TELEGRAM_ID).
 */
object TestEnv {
    private val dotenv by lazy {
        dotenv {
            ignoreIfMissing = true
            ignoreIfMalformed = true
        }
    }

    fun get(key: String): String? = dotenv[key]

    fun getLong(key: String): Long? = get(key)?.toLongOrNull()

    fun has(key: String): Boolean = get(key) != null
}
