import service.*
import model.core.*

fun main() {
    val numeroMapa = solicitarNumeroMapa()
    val config = MapaConfig(
        numeroMapa = numeroMapa,
        displayMode = DisplayFlag.LIVE,
        snapshotStep = 200,
        delay = 50L
    )
    val renderer = ConsoleMapaRenderer()
    val solver = LabirintoSolver(
        loader = MapaLoader(),
        finder = BfsPathFinder(renderer),
        renderer = renderer
    )
    solver.resolver(config)
}

private fun solicitarNumeroMapa(): Int {
    print("Informe o numero do mapa (ex: 1): ")
    val entrada = readlnOrNull()?.trim()
    return entrada?.toIntOrNull()?.takeIf { it > 0 } ?: 1
}