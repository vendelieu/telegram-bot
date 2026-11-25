package eu.vendeli.quarkus.extension.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Build steps of the KtGram extension.
 * <p>
 * Written in Java on purpose: Quarkus' extension annotation processor only runs on Java sources,
 * and it is what registers the {@code @BuildStep} methods.
 */
class KtGramProcessor {

    private static final String FEATURE = "ktgram";
    private static final String KTGRAM_METADATA =
            "META-INF/native-image/eu.vendeli.tgbot.generated/ktgram/reachability-metadata.json";
    private static final Pattern METADATA_TYPE = Pattern.compile("\"type\"\\s*:\\s*\"([^\"]+)\"");

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem registerBeans() {
        return AdditionalBeanBuildItem.builder()
                .addBeanClasses(
                        "eu.vendeli.quarkus.extension.KtGramBots",
                        "eu.vendeli.quarkus.extension.QuarkusClassManager")
                .build();
    }

    /**
     * Keeps every bean class KtGram resolves through its {@code ClassManager} (handlers, guards,
     * argument parsers, filters, ...): they are only looked up programmatically, so ArC would remove them.
     * <p>
     * KtGram's handler annotations have {@code SOURCE} retention, so those classes can't be found in
     * the Jandex index. Instead, read the native-image metadata that KtGram's KSP processor (ktnip)
     * generates next to the context loader: it lists exactly the classes the generated code resolves.
     */
    @BuildStep
    UnremovableBeanBuildItem keepHandlerBeans() throws IOException {
        Set<String> resolvedClasses = new HashSet<>();
        Enumeration<URL> metadata = Thread.currentThread().getContextClassLoader().getResources(KTGRAM_METADATA);
        while (metadata.hasMoreElements()) {
            try (InputStream in = metadata.nextElement().openStream()) {
                Matcher matcher = METADATA_TYPE.matcher(new String(in.readAllBytes(), StandardCharsets.UTF_8));
                while (matcher.find()) {
                    resolvedClasses.add(matcher.group(1));
                }
            }
        }
        return UnremovableBeanBuildItem.beanClassNames(resolvedClasses);
    }
}
