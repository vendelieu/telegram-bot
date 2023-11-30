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

### Environment

Environment parameters that are required for testing:

* Token - a token for the bot that is obtained from [BotFather](https://t.me/BotFather). (don't forget to write some
  message to the bot for correct
  interaction)
* Telegram Id - can be found out through the bots if you still don't know it. (for
  example [this one](https://t.me/my_id_bot))

#### The final parameters look like this:

`TELEGRAM_ID` - your telegram id. :)

`BOT_TOKEN` - token for testing bot.

`BOT_TOKEN_2` - second bot for testing (for limits purpose).

## Any contributions you make will be under the Apache 2.0 Software License

In short, when you submit code changes, your submissions are understood to be under the
same [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) that covers the project. \
Feel free to contact the
maintainers if that's a concern.

## Report bugs using Github's [issues](https://github.com/vendelieu/telegram-bot/issues)

We use GitHub issues to track public bugs. Report a bug by opening a new issue; it's that easy!

## Use a Consistent Coding Style

Try to stick to the [conventions of Kotlin](https://kotlinlang.org/docs/coding-conventions.html).
