package service

class LabirintoSolver(
    private val loader: CarregarMapa,
    private val finder: EncontrarCaminho,
    private val renderer: MapaRender
) {
    fun resolver(config: MapaConfig) {
        val grid = loader.carregarMapa(config.numeroMapa)
        val linhas = grid.size
        val colunas = grid[0].size
        val pequeno = linhas <= 35 && colunas <= 35
        val tunedConfig = config.copy(
            displayMode = if (pequeno) DisplayFlag.LIVE else DisplayFlag.SNAPSHOT,
            snapshotStep = if (pequeno) config.snapshotStep else maxOf(200, config.snapshotStep),
            delay = if (pequeno) config.delay else 0L
        )
        val caminho = finder.findPath(grid, tunedConfig)
        renderer.renderFinal(grid, caminho.path)
        println("Distancia minima: ${caminho.distancia}")
        println("Total de posicoes no caminho: ${caminho.path.size}")
    }
}