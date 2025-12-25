package eu.vendeli.ksp.collectors

import com.google.devtools.ksp.processing.Resolver
import eu.vendeli.ksp.dto.CollectorsContext

internal interface Collector {
    fun collect(resolver: Resolver, ctx: CollectorsContext)
}
