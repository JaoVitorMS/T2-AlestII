package service

class ConsoleMapaRenderer : MapaRender {
    override fun renderFinal(grid: Array<CharArray>, path: List<Pontos>) {
        val frame = Array(grid.size) { row -> grid[row].copyOf() }
        for (ponto in path) {
            val atual = frame[ponto.x][ponto.y]
            if (atual != 'A' && atual != 'B' && atual != '#') {
                frame[ponto.x][ponto.y] = '*'
            }
        }
        println("=== Caminho Final ===")
        frame.forEach { println(it.concatToString()) }
    }

    override fun renderProgresso(
        grid: Array<CharArray>,
        visited: Array<BooleanArray>,
        startPonto: Pontos,
        pontoCerto: Pontos
    ) {
        limparTela()
        val builder = StringBuilder()
        builder.appendLine("Progresso da busca")
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val char = when {
                    startPonto.x == i && startPonto.y == j -> 'A'
                    grid[i][j] == 'B' && pontoCerto.x == i && pontoCerto.y == j -> 'B'
                    pontoCerto.x == i && pontoCerto.y == j -> 'o'
                    grid[i][j] == 'B' -> 'B'
                    grid[i][j] == '#' -> '#'
                    visited[i][j] -> '·'
                    else -> '.'
                }
                builder.append(char)
            }
            builder.appendLine()
        }
        print(builder.toString())
    }

    private fun limparTela() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }
}