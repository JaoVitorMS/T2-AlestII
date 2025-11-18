
package model.contract

interface CarregarMapa{
    fun carregarMapa(numeroMapa: Int): Array<CharArray>
}

interface EncontrarCaminho{
    fun findPath(
        grid: Array<CharArray>,
        config: MapaConfig
    ): Caminho
}

interface MapaRender{
    fun renderFinal(grid: Array<CharArray>, path: List<Pontos>)
    fun renderProgresso(
        grid: Array<CharArray>, 
        visited: Array<BooleanArray>,
        startPonto: Pontos,
        pontoCerto: Pontos
    )
}