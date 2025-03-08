package eu.vendeli.aide.dto

import eu.vendeli.aide.dto.ActionStatementType.LAMBDA
import eu.vendeli.aide.dto.ActionStatementType.PLAIN_CALL
import eu.vendeli.aide.dto.ActionStatementType.VARIABLE

/**
 * Represents types of statements where an Action-returning call can be placed.
 *
 * @property PLAIN_CALL Plain call, e.g. `action()`
 * @property VARIABLE Variable declaration, e.g. `val action = action()`
 * @property LAMBDA Lambda expression, e.g. `action.scope { send() }`
 */
internal enum class ActionStatementType {
    PLAIN_CALL,
    VARIABLE,
    LAMBDA,
}
