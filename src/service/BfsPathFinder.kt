package service

class BfsPathFinder(
    private val renderer: MapaRender
) : EncontrarCaminho {

    override fun findPath(grid: Array<CharArray>, config: MapaConfig): Caminho {
        val start = localizar(grid, 'A') ?: throw IllegalStateException("Ponto A nao encontrado")
        val target = localizar(grid, 'B') ?: throw IllegalStateException("Ponto B nao encontrado")
        val rows = grid.size
        val cols = grid[0].size
        val visited = Array(rows) { BooleanArray(cols) }
        val parent = Array(rows) { arrayOfNulls<Pontos>(cols) }
        val queue: ArrayDeque<Pontos> = ArrayDeque()
        queue.add(start)
        visited[start.x][start.y] = true
        var found = false
        val smallMap = rows <= 35 && cols <= 35
        var steps = 0
        val snapshotStep = config.snapshotStep.coerceAtLeast(1)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == target) {
                found = true
                break
            }
            for (dir in DIRECTIONS) {
                val nextX = current.x + dir.x
                val nextY = current.y + dir.y
                if (!inBounds(nextX, nextY, rows, cols)) continue
                if (visited[nextX][nextY]) continue
                if (!isWalkable(grid[nextX][nextY])) continue
                val next = Pontos(nextX, nextY)
                queue.add(next)
                visited[nextX][nextY] = true
                parent[nextX][nextY] = current
                steps++
                if (shouldRender(steps, config.displayMode, smallMap, snapshotStep)) {
                    renderer.renderProgresso(grid, visited, start, next)
                    if (config.delay > 0L) Thread.sleep(config.delay)
                }
            }
        }
        if (!found) throw IllegalStateException("Nao existe caminho entre A e B")
        val caminho = rebuildPath(target, parent)
        return Caminho(caminho.size - 1, caminho)
    }

    private fun localizar(grid: Array<CharArray>, target: Char): Pontos? {
        for (i in grid.indices) for (j in grid[i].indices) if (grid[i][j] == target) return Pontos(i, j)
        return null
    }

    private fun rebuildPath(destination: Pontos, parent: Array<Array<Pontos?>>): List<Pontos> {
        val caminho = mutableListOf<Pontos>()
        var atual: Pontos? = destination
        while (atual != null) {
            caminho.add(atual)
            atual = parent[atual.x][atual.y]
        }
        caminho.reverse()
        return caminho
    }

    private fun isWalkable(cell: Char) = cell == '.' || cell == 'B' || cell == 'A'

    private fun inBounds(x: Int, y: Int, rows: Int, cols: Int) = x in 0 until rows && y in 0 until cols

    private fun shouldRender(step: Int, mode: DisplayFlag, small: Boolean, snapshot: Int): Boolean {
        return when (mode) {
            DisplayFlag.OFF -> false
            DisplayFlag.LIVE -> small
            DisplayFlag.SNAPSHOT -> !small && step % snapshot == 0
        }
    }

    companion object {
        private val DIRECTIONS = listOf(
            Pontos(-1, 0),
            Pontos(1, 0),
            Pontos(0, -1),
            Pontos(0, 1)
        )
    }
}