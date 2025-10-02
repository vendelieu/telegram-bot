@file:Suppress("TooManyFunctions")

package eu.vendeli.ksp.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.implementations.ClassDataImpl
import eu.vendeli.tgbot.implementations.DefaultArgParser
import eu.vendeli.tgbot.implementations.DefaultGuard
import eu.vendeli.tgbot.implementations.UserDataMapImpl
import eu.vendeli.tgbot.interfaces.ctx.ClassData
import eu.vendeli.tgbot.interfaces.ctx.UserData
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.chain.ChainingStrategy
import eu.vendeli.tgbot.types.chain.Link
import eu.vendeli.tgbot.types.chat.Chat
import eu.vendeli.tgbot.types.component.*
import eu.vendeli.tgbot.types.configuration.RateLimits
import eu.vendeli.tgbot.utils.common.fqName
import kotlin.reflect.KClass

internal typealias FileBuilder = FileSpec.Builder

internal const val INVOCATION_LAMBDA_PARAMS = "bot, update, parameters, ctx"

internal val activitiesType = Map::class.asTypeName().parameterizedBy(
    STRING,
    List::class.asTypeName().parameterizedBy(ANY.copy(true)),
)
internal val invocableType = TypeVariableName("Invocable")
internal val linkQName = Link::class.fqName
internal val autowiringFQName = Autowiring::class.fqName

internal val intPrimitiveType = TypeVariableName("int")
internal val longPrimitiveType = TypeVariableName("long")
internal val shortPrimitiveType = TypeVariableName("short")
internal val floatPrimitiveType = TypeVariableName("float")
internal val doublePrimitiveType = TypeVariableName("double")

internal val userClass = User::class.asTypeName()
internal val chatClass = Chat::class.asTypeName()
internal val botClass = TelegramBot::class.asTypeName()
internal val processingCtx = ProcessingCtx::class.asTypeName()
internal val idLongClass = IdLong::class.asTypeName()

internal val updateClass = ProcessedUpdate::class.asTypeName()
internal val messageUpdClass = MessageUpdate::class.asTypeName()

internal val callbackQueryUpdateClass = CallbackQueryUpdate::class.asTypeName()
internal val editedMessageUpdateClass = EditedMessageUpdate::class.asTypeName()
internal val channelPostUpdateClass = ChannelPostUpdate::class.asTypeName()
internal val editedChannelPostUpdateClass = EditedChannelPostUpdate::class.asTypeName()
internal val messageReactionUpdateClass = MessageReactionUpdate::class.asTypeName()
internal val messageReactionCountUpdateClass = MessageReactionCountUpdate::class.asTypeName()
internal val inlineQueryUpdateClass = InlineQueryUpdate::class.asTypeName()
internal val chosenInlineResultUpdateClass = ChosenInlineResultUpdate::class.asTypeName()
internal val shippingQueryUpdateClass = ShippingQueryUpdate::class.asTypeName()
internal val preCheckoutQueryUpdateClass = PreCheckoutQueryUpdate::class.asTypeName()
internal val pollUpdateClass = PollUpdate::class.asTypeName()
internal val pollAnswerUpdateClass = PollAnswerUpdate::class.asTypeName()
internal val myChatMemberUpdateClass = MyChatMemberUpdate::class.asTypeName()
internal val chatMemberUpdateClass = ChatMemberUpdate::class.asTypeName()
internal val chatJoinRequestUpdateClass = ChatJoinRequestUpdate::class.asTypeName()
internal val chatBoostUpdateClass = ChatBoostUpdate::class.asTypeName()
internal val removedChatBoostUpdateClass = RemovedChatBoostUpdate::class.asTypeName()
internal val businessConnectionUpdateClass = BusinessConnectionUpdate::class.asTypeName()
internal val businessMessageUpdateClass = BusinessMessageUpdate::class.asTypeName()
internal val editedBusinessMessageClass = EditedBusinessMessageUpdate::class.asTypeName()
internal val deletedBusinessMessagesClass = DeletedBusinessMessagesUpdate::class.asTypeName()
internal val purchasedPaidMediaUpdateClass = PurchasedPaidMediaUpdate::class.asTypeName()

internal val commonMatcherClass = CommonMatcher::class.asTypeName()
internal val userDataCtx = UserData::class.asTypeName().parameterizedBy(STAR)
internal val userDataCtxDef = UserDataMapImpl::class.asTypeName()
internal val classDataCtx = ClassData::class.asTypeName().parameterizedBy(STAR)
internal val classDataCtxDef = ClassDataImpl::class.asTypeName()

internal val callbackQueryList = listOf(UpdateType.CALLBACK_QUERY)
internal val messageList = listOf(UpdateType.MESSAGE)
internal val notLimitedRateLimits = 0L to 0L

internal val ChainingStrategyDefault: TypeName = ChainingStrategy.Default::class.asTypeName()

@Suppress("UNCHECKED_CAST")
internal inline fun <R> Any?.cast(): R = this as R

@Suppress("UNCHECKED_CAST")
internal inline fun <R> Any?.safeCast(): R? = this as? R

internal fun Pair<Long, Long>.toRateLimits(): RateLimits = RateLimits(first, second)

fun KSType.toKSPClassName() = starProjection().let {
    if (arguments.isEmpty()) it.toClassName() else it.toTypeName()
}

internal inline fun buildMeta(
    qualifier: String,
    function: String,
    rateLimits: RateLimits,
    guardClass: String? = null,
    argParserClass: String? = null,
): Pair<String, Array<Any?>> {
    val parametersList = mutableListOf<Any?>(
        qualifier,
        function,
    )

    return buildString {
        append("InvocationMeta(\n\tqualifier = \"%L\",\n\tfunction = \"%L\"")
        if (rateLimits.period > 0 || rateLimits.rate > 0) {
            append(",\n\trateLimits = %L")
            parametersList.add(rateLimits)
        }

        if (guardClass != null && guardClass != DefaultGuard::class.fqName) {
            append(",\n\tguard = %L::class")
            parametersList.add(guardClass)
        }
        if (argParserClass != null && argParserClass != DefaultArgParser::class.fqName) {
            append(",\n\targParser = %L::class")
            parametersList.add(argParserClass)
        }
        append("\n\t)")
    } to parametersList.toTypedArray()
}

internal fun <T : Annotation> Resolver.getAnnotatedFnSymbols(
    targetPackage: String? = null,
    vararg kClasses: KClass<out T>,
): Sequence<KSFunctionDeclaration> {
    val targetAnnoNames = kClasses.mapNotNull { it.qualifiedName }.toSet()

    // step 1: find meta-annotations (annotations that carry our target ones)
    val metaAnnotations = getAllFiles()
        .flatMap { it.declarations }
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.classKind == ClassKind.ANNOTATION_CLASS }
        .filter { annoDecl ->
            annoDecl.annotations.any { ann ->
                val fqName = ann.annotationType.resolve().declaration.qualifiedName?.asString()
                fqName in targetAnnoNames
            }
        }
        .mapNotNull { it.qualifiedName?.asString() }
        .toSet()

    val allTargetNames = targetAnnoNames + metaAnnotations

    // step 2: gather functions annotated with either direct or meta annotation
    val candidates = allTargetNames
        .asSequence()
        .flatMap { annoName -> getSymbolsWithAnnotation(annoName) }
        .filterIsInstance<KSFunctionDeclaration>()

    return if (targetPackage != null) {
        candidates.filter { f -> f.packageName.asString().startsWith(targetPackage) }
    } else {
        candidates
    }
}


/**
 * Checks if this sequence of annotations contains [target],
 * either directly or via meta-annotations (recursively).
 */
internal fun Sequence<KSAnnotation>.hasAnnotationRecursively(
    target: KClass<out Annotation>
): Boolean {
    val targetFqName = target.qualifiedName ?: return false

    fun KSAnnotation.matchesOrMetaVisited(visited: MutableSet<String>): Boolean {
        val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return false
        if (fqName == targetFqName) return true
        if (!visited.add(fqName)) return false // already checked -> avoid cycles

        val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration
            ?: return false

        // recurse into the annotations on this annotation class
        return annoDecl.annotations.any { it.matchesOrMetaVisited(visited) }
    }

    val visited = mutableSetOf<String>()
    return any { it.matchesOrMetaVisited(visited) }
}

/**
 * Finds the first annotation matching [target], directly or via meta-annotations (recursively).
 *
 * @return the matching KSAnnotation, or null if not found.
 */
internal fun Sequence<KSAnnotation>.findAnnotationRecursively(
    target: KClass<out Annotation>
): KSAnnotation? {
    val targetFqName = target.qualifiedName ?: return null

    fun KSAnnotation.findRecursive(visited: MutableSet<String>): KSAnnotation? {
        val fqName = annotationType.resolve().declaration.qualifiedName?.asString() ?: return null
        if (fqName == targetFqName) return this
        if (!visited.add(fqName)) return null // already visited → avoid cycles

        val annoDecl = annotationType.resolve().declaration as? KSClassDeclaration
            ?: return null

        // Recurse into the annotations present on this annotation class
        for (nested in annoDecl.annotations) {
            val found = nested.findRecursive(visited)
            if (found != null) return found
        }
        return null
    }

    val visited = mutableSetOf<String>()
    for (anno in this) {
        val found = anno.findRecursive(visited)
        if (found != null) return found
    }
    return null
}

/**
 * Filters this sequence of annotations by the given annotation classes,
 * supporting meta-annotations as well.
 */
internal fun <T : Annotation> Sequence<KSAnnotation>.filterByAnnotations(
    resolver: Resolver,
    vararg kClasses: KClass<out T>,
): Sequence<KSAnnotation> {
    val targetAnnoNames = kClasses.mapNotNull { it.qualifiedName }.toSet()

    // Step 1: find meta-annotations
    val metaAnnotations = resolver.getAllFiles()
        .flatMap { it.declarations }
        .filterIsInstance<KSClassDeclaration>()
        .filter { it.classKind == ClassKind.ANNOTATION_CLASS }
        .filter { annoDecl ->
            annoDecl.annotations.any { ann ->
                ann.annotationType.resolve().declaration.qualifiedName?.asString() in targetAnnoNames
            }
        }
        .mapNotNull { it.qualifiedName?.asString() }
        .toSet()

    val allTargetNames = targetAnnoNames + metaAnnotations

    // Step 2: filter actual annotations
    return filter { ann ->
        ann.annotationType.resolve().declaration.qualifiedName?.asString() in allTargetNames
    }
}


internal fun <T : Annotation> Resolver.getAnnotatedClassSymbols(clazz: KClass<T>, targetPackage: String? = null) =
    if (targetPackage == null) getSymbolsWithAnnotation(clazz.qualifiedName!!)
        .filterIsInstance<KSClassDeclaration>()
    else getSymbolsWithAnnotation(clazz.qualifiedName!!)
        .filter {
            it is KSClassDeclaration && it.packageName.asString().startsWith(targetPackage)
        }.cast()

internal fun FileBuilder.addMap(
    name: String,
    type: ParameterizedTypeName,
    symbols: Sequence<KSFunctionDeclaration>,
    tailBlock: CodeBlock? = null,
    iterableAction: CodeBlock.Builder.(KSFunctionDeclaration) -> Unit,
) = addProperty(
    PropertySpec
        .builder(
            name,
            type,
            KModifier.PRIVATE,
        ).apply {
            initializer(
                CodeBlock
                    .builder()
                    .apply {
                        add("mapOf(\n")
                        if (symbols.iterator().hasNext()) symbols.forEach {
                            iterableAction(it)
                        }
                        tailBlock?.also { add(it) }
                        add(")\n")
                    }.build(),
            )
        }.build(),
)
