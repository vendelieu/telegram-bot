# Contributing

We love your input! We want to make contributing to this project as easy and transparent as possible, whether it's:

- Reporting a bug
- Discussing the current state of the code
- Submitting a fix
- Proposing new features
- Becoming a maintainer

## We Use [Github Flow](https://guides.github.com/introduction/flow/index.html), So All Code Changes Happen Through Pull Requests

Pull requests are the best way to propose changes to the codebase (we
use [Github Flow](https://guides.github.com/introduction/flow/index.html)). We actively welcome your pull requests:

1. Fork the repo and create your branch from `master`.
2. If you've added code that should be tested, add tests.
3. Ensure the test suite passes.
4. Make sure your code lints (run Gradle formatting/formatKotlin command).
5. Specify `dev` as target branch.
6. Issue that pull request!

Helpful gradle tasks are:

* `./gradlew apiDump` - generates api dump
* `./gradlew prepareRelease` - formats code, does api validation, updates kdocs, runs api violations check.

**Pre-push hook (optional):** Run `./scripts/install-hooks.sh` to install a git hook that runs `prepareRelease` before each push. This ensures code is formatted and validated before it reaches the remote.

### Environment

Tests load environment variables from a `.env` file (project root) or system environment.
Host environment variables take precedence over `.env`.

**Setup:**

1. Copy `env.example` to `.env` in the project root.
2. Fill in your values. `.env` is gitignored.

**Required for most tests:**

| Variable | Description |
|----------|-------------|
| `TELEGRAM_ID` | Your Telegram user ID (e.g. from [@my_id_bot](https://t.me/my_id_bot)) |
| `BOT_TOKEN` | Bot token from [@BotFather](https://t.me/BotFather). Send a message to the bot first. |

**Optional (enables additional tests):**

| Variable | Description |
|----------|-------------|
| `BOT_TOKEN_2` | Second bot for rate-limit testing |
| `CHAT_ID` | Chat ID for chat-related tests |
| `CHANNEL_ID` | Channel ID for channel tests |
| `PAYMENT_PROVIDER_TOKEN` | Payment provider token for payment tests |

## Any contributions you make will be under the Apache 2.0 Software License

In short, when you submit code changes, your submissions are understood to be under the
same [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) that covers the project. \
Feel free to contact the
maintainers if that's a concern.

## Report bugs using Github's [issues](https://github.com/vendelieu/telegram-bot/issues)

We use GitHub issues to track public bugs. Report a bug by opening a new issue; it's that easy!

## Use a Consistent Coding Style

Try to stick to the [conventions of Kotlin](https://kotlinlang.org/docs/coding-conventions.html).
