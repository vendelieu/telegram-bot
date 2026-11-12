# ktgram-mock-server

A local stand-in for `https://api.telegram.org`, built from the same scrapped spec the library is generated from
(`buildSrc/src/main/resources/api.json`). The library's own jvm tests run against it.

Use it to run a bot (KtGram or any other library) end to end without a token, network or flood limits, and to
provoke the situations that are hard to reproduce against the real API: blocked users, `429`, webhook failures.

## Get it

Published to Maven Central with every KtGram release (JVM only), usually as a test dependency:

```kotlin
dependencies {
    testImplementation("eu.vendeli:ktgram-mock-server:<version>")
}
```

It does not depend on the KtGram library, any Bot API client works with it. It brings no logging backend, add your own
(for example logback) to see the server's logs.

## Run

```bash
./gradlew :ktgram-mock-server:runMockServer \
  --args="--port 8081"
```

Point the bot at it:

```kotlin
val bot = TelegramBot("123456789:AAMockServerToken_0123456789abcdefg") { apiHost = "http://127.0.0.1:8081" }
```

Options: `--host`, `--port`, `--token <t>` (repeatable),
`--reject-unknown-chats`, `--rate-limit`, `--require-https-webhook`, `--no-validate`.

## Embed in tests

```kotlin
MockTelegramServer(MockServerConfig(port = 0)).start().use { server ->
    val handle = server.bot()                       // control surface of the default token
    val bot = TelegramBot(MockServerConfig.DEFAULT_TOKEN) { apiHost = server.baseUrl }

    handle.userSays("/start", fromId = 42, firstName = "Ann")
    val call = handle.awaitCall("sendMessage")       // what the bot did
    handle.lastMessage(42)!!.str("text")
}
```

`BotHandle` also has `pressButton`, `inlineQuery`, `enqueue(rawUpdate)`, `addChat`, `addUser`, `setMemberStatus`,
`blockedBy`, `stub`, `stubError`, `fail`, `rateLimit`, `calls`, `messages`, `fileBytes`, `reset`.
Every call can also be made in-process with `server.call(token, method, params)`.

## How a call is answered

1. token check (`401`), unknown method (`404`; method names are case-insensitive)
2. injected **faults** (`BotHandle.fail`, `/__mock/.../faults`), then **stubs**
3. **validation** against the spec: required fields, types, nested objects (`400 Bad Request: ...`)
4. flood control when enabled (`429` with `retry_after`)
5. **stateful handlers**, and finally a **schema-generated** result for everything else, so all methods of the spec are callable.

## What is stateful

- **Messages**: send for all media kinds, albums, location/venue/contact/dice/poll/invoice/game/checklist, edit
  text/caption/markup/media/live location/checklist, stopPoll, delete, forward, copy, replies, reactions.
- **Formatting**: `HTML`, `Markdown`, `MarkdownV2` (nested entities, quotes, expandable quotes) with Telegram's parse
  errors, UTF-16 offsets and auto-detected commands/urls/mentions/hashtags/cashtags/emails.
- **Chats**: getChat (title, description, permissions, sticker set, invite link), members, ban/unban/restrict/promote,
  pins, settings, invite links (create/edit/revoke/export), join requests, forum topics and the General topic.
- **Stickers**: create/get/add/reorder/replace/delete sets, emoji lists, titles, thumbnails, custom emoji lookup,
  uploads, and the name/limit rules of the Bot API.
- **Bot profile**: commands, name and descriptions per language, menu buttons, default administrator rights.
- **Business**: connections gate every `business_connection_id` and its rights, account name/username/bio/photo/gift
  settings, incoming business messages, read and delete, stories, Stars balance and transfers.
- **Money**: gift catalogue and owned gifts, Stars balance, transactions and refunds, shipping and pre-checkout
  queries, successful payments, invoice links, game scores and leaderboards.
- **People and chats**: admin titles, member tags, emoji status, boosts, profile photos/audios, sender-chat bans,
  verification, passport errors, personal chat messages.
- **Live interactions**: drafts, ephemeral messages (stored apart from regular ones, editable and deletable), widget
  queries and widget data, suggested posts (pending, approved, declined), Mini App / guest / inline / join-request
  queries that must be answered exactly once, premium gift subscriptions and Stars subscriptions.
- **Business gifts**: list with filters and paging, convert to Stars, upgrade, transfer, delete.
- **Other**: managed bot tokens and access settings, prepared inline messages and buttons.
- **Files**: uploads, `getFile`, `/file/bot<token>/...` downloads, optional fetching of url files.
- **Updates**: `getUpdates` (offset confirmation, long polling, `409` on a second poller or an active webhook,
  `allowed_updates`) and webhooks (secret header, retries, `last_error_message`).

`BotHandle` creates the situations a bot reacts to: `userSays`, `pressButton`, `inlineQuery`, `requestToJoin`,
`shippingQuery`, `preCheckoutQuery`, `pay`, `widgetQuery`, `webAppQuery`, `suggestPost`, `addBoost`, `addBusinessConnection`,
`addBusinessGift`, `addGift`, `setStarBalance`, `setProfilePhotos`. Raw updates passed to `enqueue` register their
query ids too, so the matching `answer*` call is accepted once.

Business bot rights are enforced from the "Requires the `can_x` business bot right" sentences of the scrapped
descriptions (`addBusinessConnection(rights = ...)` grants a subset, no argument grants everything), and every
send/edit through a connection needs `can_reply`. The spec words three rights `can_change_*` while
`BusinessBotRights` calls them `can_edit_*`; the mock treats them as the same right.

Every method of the spec that leaves something a bot could read back is stateful now. The only generated catalogue is
`getChatShareThemeBackdrops` (two backdrops, replaceable with `BotHandle.setShareThemeBackdrops`).

## Configuration notes

- `MockServerConfig.textLength` counts the 4096/1024 limits in code points (default) or UTF-16 units.
- `MockServerConfig.fetchUrlFiles` downloads http(s) urls given as files so `getFile` and downloads return real bytes;
  by default urls only get a synthetic `file_id`.

## Known gaps

- `parse_mode` handling is a compact reimplementation of Telegram's parser; exotic nestings may still differ.

## Control API over HTTP

For tests in other languages. Everything is under `/__mock`:

| Request | Effect |
|---|---|
| `GET /health` | spec version |
| `POST /bots/{token}/messages` `{text, from_id?, chat_id?, chat_type?}` | a user sends a message |
| `POST /bots/{token}/callbacks` `{chat_id, message_id, data}` | a user presses an inline button |
| `POST /bots/{token}/inline` `{query}` | an inline query |
| `POST /bots/{token}/updates` | queue a raw Update |
| `GET /bots/{token}/calls[?method=]` | what the bot called |
| `GET /bots/{token}/chats/{id}/messages` | messages of a chat |
| `POST /bots/{token}/stubs` `{method, result \| error, match?, times?}` | canned answers |
| `POST /bots/{token}/faults` `{method?, status, description, retry_after?, times?, delay_ms?}` | injected failures |
| `POST`/`DELETE /bots/{token}/block` `{user_id}` | the user blocks / unblocks the bot |
| `DELETE /bots/{token}` | reset the bot |

## Keeping it honest

The library's own jvm test suite runs against this server by default, `AllMethodsTest` calls each of the spec's methods and
checks the result against the declared return type, and `LibraryEndToEndTest` runs a real KtGram bot against the
server. The nightly `live` workflow runs the same suite against real Telegram, which is what detects drift between this
mock and the real Bot API.
