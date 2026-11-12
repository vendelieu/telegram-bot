# Testing guide

The `telegram-bot` jvm tests talk to a **backend** that each spec chooses in code:

| Backend | Network | Credentials | What answers |
|---|---|---|---|
| `TestBackend.Mock` (default) | no | no | the in-process `ktgram-mock-server`: stateful, validated against the Bot API spec |
| `TestBackend.Live` | yes | `.env` | real Telegram |

The required CI build uses the mock, so it is deterministic and works without secrets (forks, dependency PRs).
The nightly `live` workflow runs the same suite against real Telegram and detects drift between the mock and the
real Bot API.

## Everyday commands

```bash
./gradlew :telegram-bot:jvmTest        # mock backend, no network
./gradlew :telegram-bot:liveTest       # real Telegram (needs .env, see env.example)
./gradlew :ktnip:updateGolden          # regenerate KSP golden files after an intended change
./gradlew :ktgram-mock-server:jvmTest  # tests of the mock itself
```

## Choosing the backend in code

```kotlin
class DiceTest : BotTestContext()                                      // default: the mock

class PaymentTest : BotTestContext(backend = TestBackend.Live)         // this spec always hits real Telegram

class FloodTest : BotTestContext(                                      // a mock with its own configuration
    backend = TestBackend.Mock { copy(rateLimit = RateLimitConfig()) },
)
```

Specs that do not choose use `TestBackends.default`. `TG_TEST_BACKEND=live|mock` (set by `liveTest`) only sets that
default, specs never read it. A configured mock gets a private server for the duration of the spec, plain mocks share
one server and the bot's state is reset before each spec.

## The mock in a spec

On the mock backend a spec has a `mock` property (the server's `BotHandle`) to inject what users do and to bend the
server:

```kotlin
mock.userSays("/start", fromId = TG_ID)                       // an update arrives
mock.fail("sendMessage", ApiException.tooManyRequests(3))     // the next call is rate limited
mock.stub("getMe", result)                                    // canned answer
mock.calls("sendMessage")                                     // what the bot did
```

It does not exist on `TestBackend.Live`, so a spec that needs it cannot be pointed at real Telegram by accident.

## Writing tests

- Extend `BotTestContext` for anything that calls the Bot API and use `sendReq`/`sendReturning`.
- Use `TG_ID`, `CHAT_ID`, `CHANNEL_ID`, `BOT_ID`, `LOREM.*` from the base class instead of reading `.env`; on the mock
  they are the stable `FakeIds`.
- Never synchronize with `delay()`. Wait for the signal: `CompletableDeferred`/`Channel` + `withTimeout`, `Job.join()`
  (`parseAndHandle` returns a `Job`), `mock.awaitCall(...)`, or Kotest `eventually`.
- Prefer virtual time for handler logic: see `UpdateHandlerVirtualTimeTest` (`runTest` + `StandardTestDispatcher`
  injected through `updatesListener { dispatcher = ...; processingDispatcher = ... }`).
- Do not use `GlobalScope`. Shared state touched by handlers must be thread safe (`AtomicInteger`, `AtomicBoolean`).
- A spec that only makes sense against the real network (timeouts, rate limits, long waits) is enabled with
  `@EnabledIf(LiveOnlyCondition::class)` and declares `backend = TestBackend.Live`.
- Behaviour that depends on the real account (a bot that is not allowed to verify users, rights it lacks) belongs in
  a `TestBackend.Live` spec, or on the mock gets configured through `mock` (stubs, faults, `addBusinessConnection`,
  `setStarBalance`, ...).
- New Bot API method: add the action and its test. `SpecCoverageTest` fails when the spec in `buildSrc/.../api.json`
  lists a method without an implemented action; if the method needs state the mock does not model yet, add it to
  `ktgram-mock-server` (the module's tests cover every method of the spec).

## Flaky tests

CI retries a failed test once and marks it as flaky instead of failing the build (`TEST_RETRIES` overrides this).
The weekly `flaky detector` workflow runs the suite repeatedly **without** retries and lists every test that failed
intermittently. A test that shows up there gets fixed, not retried forever.
