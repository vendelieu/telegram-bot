package eu.vendeli.ktnip.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ktnip.dto.CollectorsContext

internal interface Collector {
    fun collect(resolver: Resolver, ctx: CollectorsContext)
}
