package service

class MapaLoader(
    private val resourceDir: String = "src/resources"
) : CarregarMapa {
    override fun carregarMapa(numeroMapa: Int): Array<CharArray> {
        val path = Paths.get(resourceDir, "caso$numeroMapa.txt")
        if (!Files.exists(path)) {
            throw IllegalArgumentException("Mapa $numeroMapa nao encontrado em $resourceDir")
        }
        val lines = Files.readAllLines(path)
        require(lines.isNotEmpty()) { "Mapa vazio" }
        val largura = lines.first().length
        require(lines.all { it.length == largura }) { "Todas as linhas do mapa devem ter o mesmo tamanho" }
        return Array(lines.size) { index -> lines[index].toCharArray() }
    }
}