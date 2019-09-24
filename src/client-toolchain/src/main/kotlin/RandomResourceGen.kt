import org.eclipse.lyo.oslc4j.core.model.AbstractResource
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider

class RandomResourceGen<T : AbstractResource>(private val generator: (ServiceProvider, Int) -> T) {
    fun generate(sp: ServiceProvider, n: Int): List<T> = (1..n).map { i -> generator(sp, i) }
}
