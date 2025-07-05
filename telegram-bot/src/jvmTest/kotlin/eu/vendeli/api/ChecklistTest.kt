package eu.vendeli.api

import BotTestContext
import eu.vendeli.tgbot.api.checklist.editMessageChecklist
import eu.vendeli.tgbot.api.checklist.sendChecklist
import eu.vendeli.tgbot.types.checklist.InputChecklist
import eu.vendeli.tgbot.types.checklist.InputChecklistTask
import io.kotest.matchers.shouldBe

class ChecklistTest : BotTestContext() {
    @Test
    suspend fun `sendChecklist method test`() {
        val checklist = InputChecklist(
            title = "Test Checklist",
            tasks = listOf(
                InputChecklistTask(1, "First task"),
                InputChecklistTask(2, "Second task"),
            ),
            othersCanAddTasks = true,
            othersCanMarkTasksAsDone = true,
        )

        sendChecklist(checklist)
            .sendBusinessReturning(CHAT_ID, "test", bot)
            .shouldFailure()
            .description shouldBe "Bad Request: business connection not found"
    }

    @Test
    suspend fun `editMessageChecklist method test`() {
        val checklist = InputChecklist(
            title = "Test Checklist",
            tasks = listOf(
                InputChecklistTask(1, "First task"),
                InputChecklistTask(2, "Second task"),
            ),
            othersCanAddTasks = true,
            othersCanMarkTasksAsDone = true,
        )

        editMessageChecklist(1, checklist)
            .sendBusinessReturning(CHAT_ID, "test", bot)
            .shouldFailure()
            .description shouldBe "Bad Request: business connection not found"
    }
}
