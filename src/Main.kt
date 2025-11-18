import service.*

fun main() {
    print("Digite o numero do mapa: ")
    val numeroMapa = readlnOrNull()?.toIntOrNull() ?: 1
    
    println("\nResolvendo labirinto...\n")
    
    try {
        val solver = LabirintoSolver()
        solver.resolver(numeroMapa)
    } catch (e: Exception) {
        println("Erro: ${e.message}")
    }
}