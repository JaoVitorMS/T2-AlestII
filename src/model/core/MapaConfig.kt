package model.core

data class MapaConfig(
    val numeroMapa: Int, 
    val displayMode: DisplayFlag,
    val snapshotStep: Int,
    val delay: Long
)