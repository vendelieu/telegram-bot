# Testing guide

The `telegram-bot` jvm tests run in one of three modes, selected with `TG_TEST_MODE`:

| Mode | Network | Credentials | What happens |
|---|---|---|---|
| `replay` (default) | no | no | Telegram responses are served from `telegram-bot/src/jvmTest/resources/tg-fixtures` |
| `record` | yes | `.env` | Real Telegram is called, passing tests are stored (redacted) as fixtures |
| `live` | yes | `.env` | Real Telegram is called, nothing is stored |

The required CI build uses `replay`, so it is deterministic and works without secrets (forks, dependency PRs).
The nightly `live` workflow detects drift between the recordings and the real Bot API.

## Everyday commands

```bash
./gradlew :telegram-bot:jvmTest                      # replay, ~15 s, no network
./gradlew :telegram-bot:liveTest                     # against real Telegram (needs .env, see env.example)
./gradlew :telegram-bot:recordFixtures               # re-record everything
./gradlew :telegram-bot:recordFixtures --tests '*ChatSetMethodsTest'   # re-record one spec
./gradlew :ktnip:updateGolden                        # regenerate KSP golden files after an intended change
```

## How replay works

- One fixture file per spec: `tg-fixtures/<fully.qualified.SpecName>.json`, grouped by test name.
- A request is matched by **API method** and consumed in recorded order **per method**, so stateful flows
  (create, edit, delete) replay correctly. The names of the sent parameters must match the recording, a test or
  library change that alters them fails with a "re-record" hint. Parameter *values* are not compared.
- `BotTestContext` picks the mode. In replay it also swaps in stable ids (`FakeIds`) and placeholder media, so tests
  must use `TG_ID`, `CHAT_ID`, `CHANNEL_ID`, `BOT_ID`, `LOREM.*` from the base class instead of reading `.env`.
- Re-recording only keeps tests that passed, a failing run never overwrites a good fixture.
- Fixtures are redacted while recording (tokens, configured ids, names of users, invite links). `FixtureHygieneTest`
  fails the build if a token, a configured credential or an invite link ever lands in a fixture.

## Writing tests

- Extend `BotTestContext` for anything that calls the Bot API and use `sendReq`/`sendReturning`.
- Values the server echoes back must be stable. Use constants (`FIXED_EXPIRY`, fixed scores), not `now` or random
  numbers, otherwise the replayed echo differs from what the test computed.
- Never synchronize with `delay()`. Wait for the signal: `CompletableDeferred`/`Channel` + `withTimeout`, `Job.join()`
  (`parseAndHandle` returns a `Job`), or Kotest `eventually`.
- Prefer virtual time for handler logic: see `UpdateHandlerVirtualTimeTest` (`runTest` + `StandardTestDispatcher`
  injected through `updatesListener { dispatcher = ...; processingDispatcher = ... }`).
- Do not use `GlobalScope`. Shared state touched by handlers must be thread safe (`AtomicInteger`, `AtomicBoolean`).
- A spec that only makes sense against the real network (timeouts, rate limits, long waits) gets
  `@EnabledIf(LiveOnlyCondition::class)` and is skipped in replay.
- New Bot API method: add the action, its test, then run `recordFixtures --tests` for that spec and commit the fixture.
  `SpecCoverageTest` fails when the spec in `buildSrc/.../api.json` lists a method without an implemented action.

## Flaky tests

CI retries a failed test once and marks it as flaky instead of failing the build (`TEST_RETRIES` overrides this).
The weekly `flaky detector` workflow runs the suite repeatedly **without** retries and lists every test that failed
intermittently. A test that shows up there gets fixed, not retried forever.
