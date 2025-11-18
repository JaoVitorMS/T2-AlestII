package service

import model.core.*
import java.nio.file.*
import kotlin.system.measureTimeMillis

class LabirintoSolver {
    fun resolver(numeroMapa: Int) {
        val grid = carregarMapa(numeroMapa)
        val totalCelulas = grid.size.toLong() * grid[0].size
        
        var tamanhoCaminho = 0
        val tempo = measureTimeMillis {
            val caminho = findPath(grid)
            tamanhoCaminho = caminho.size - 1
            
            if (totalCelulas <= 1_000_000) {
                renderizar(grid, caminho)
            }
        }
        
        println("Menor distancia: $tamanhoCaminho")
        println("Tempo de execucao: ${tempo / 1000.0}s")
    }
    
    private fun carregarMapa(numeroMapa: Int): Array<CharArray> {
        val path = Paths.get("src/resources", "caso$numeroMapa.txt")
        if (!Files.exists(path)) {
            error("Mapa 'caso$numeroMapa.txt' nao encontrado")
        }
        
        val lines = Files.readAllLines(path)
        return Array(lines.size) { i -> lines[i].toCharArray() }
    }
    
    private fun findPath(grid: Array<CharArray>): List<Pontos> {
        val start = encontrar(grid, 'A') ?: error("Ponto A nao encontrado")
        val end = encontrar(grid, 'B') ?: error("Ponto B nao encontrado")
        
        val totalCelulas = grid.size.toLong() * grid[0].size
        val usarHashMap = totalCelulas > 50_000_000
        
        if (usarHashMap) {
            return findPathHashMap(grid, start, end)
        }
        
        val visited = Array(grid.size) { BooleanArray(grid[0].size) }
        val parent = Array(grid.size) { arrayOfNulls<Pontos>(grid[0].size) }
        val queue = ArrayDeque<Pontos>()
        
        queue.add(start)
        visited[start.x][start.y] = true
        
        while (queue.isNotEmpty()) {
            val atual = queue.removeFirst()
            
            if (atual == end) break
            
            for ((dx, dy) in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
                val nx = atual.x + dx
                val ny = atual.y + dy
                
                if (nx !in grid.indices || ny !in grid[0].indices) continue
                if (visited[nx][ny]) continue
                if (grid[nx][ny] == '#') continue
                
                val proximo = Pontos(nx, ny)
                queue.add(proximo)
                visited[nx][ny] = true
                parent[nx][ny] = atual
            }
        }
        
        return reconstruirCaminho(end, parent)
    }
    
    private fun findPathHashMap(grid: Array<CharArray>, start: Pontos, end: Pontos): List<Pontos> {
        val visited = HashSet<Pontos>()
        val parent = HashMap<Pontos, Pontos>()
        val queue = ArrayDeque<Pontos>()
        
        queue.add(start)
        visited.add(start)
        
        while (queue.isNotEmpty()) {
            val atual = queue.removeFirst()
            
            if (atual == end) {
                return reconstruirCaminhoHashMap(end, parent)
            }
            
            for ((dx, dy) in listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)) {
                val nx = atual.x + dx
                val ny = atual.y + dy
                
                if (nx !in grid.indices || ny !in grid[0].indices) continue
                
                val proximo = Pontos(nx, ny)
                if (proximo in visited) continue
                if (grid[nx][ny] == '#') continue
                
                queue.add(proximo)
                visited.add(proximo)
                parent[proximo] = atual
            }
        }
        
        return reconstruirCaminhoHashMap(end, parent)
    }
    
    private fun reconstruirCaminhoHashMap(destino: Pontos, parent: HashMap<Pontos, Pontos>): List<Pontos> {
        val caminho = mutableListOf<Pontos>()
        var atual: Pontos? = destino
        while (atual != null) {
            caminho.add(atual)
            atual = parent[atual]
        }
        return caminho.reversed()
    }
    
    private fun encontrar(grid: Array<CharArray>, char: Char): Pontos? {
        for (i in grid.indices)
            for (j in grid[i].indices)
                if (grid[i][j] == char) return Pontos(i, j)
        return null
    }
    
    private fun reconstruirCaminho(destino: Pontos, parent: Array<Array<Pontos?>>): List<Pontos> {
        val caminho = mutableListOf<Pontos>()
        var atual: Pontos? = destino
        while (atual != null) {
            caminho.add(atual)
            atual = parent[atual.x][atual.y]
        }
        return caminho.reversed()
    }
    
    private fun renderizar(grid: Array<CharArray>, caminho: List<Pontos>) {
        val pathSet = caminho.toSet()
        val RED = "\u001b[31m"
        val RESET = "\u001b[0m"
        
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                val ponto = Pontos(i, j)
                if (ponto in pathSet && grid[i][j] != 'A' && grid[i][j] != 'B') {
                    print("${RED}*${RESET}")
                } else {
                    print(grid[i][j])
                }
            }
            println()
        }
    }
}