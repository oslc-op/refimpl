package co.oslc.refimpl.client

import org.eclipse.lyo.oslc4j.core.model.Link
import java.net.URI
import kotlin.random.Random

fun randomId(maxId: Int): Int {
    val r = Random(System.nanoTime())
    return r.nextInt(1, maxId)
}

fun randomIdExcept(maxId: Int, exceptId: Int): Int {
    val r = Random(System.nanoTime())
    var linkedId = r.nextInt(1, maxId)
    while (linkedId == exceptId) {
        linkedId = r.nextInt(1, maxId)
    }
    return linkedId
}

fun link(to: String): Link = Link(URI.create(to))
fun link(to: URI): Link = Link(to)

fun singleLinkTo(to: String) = setOf(link(to))
fun singleLinkTo(to: URI) = setOf(link(to))

fun randomLink(links: List<Link>): Set<Link> {
    if (links.isEmpty()) {
        return emptySet()
    }
    return setOf(links[randomId(links.size+1) - 1])
}
