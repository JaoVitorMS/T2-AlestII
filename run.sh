#!/bin/bash

# Script para compilar e executar o solucionador de labirintos

echo "Compilando o projeto..."
kotlinc -d build -cp src src/Main.kt src/model/core/*.kt src/service/*.kt

if [ $? -eq 0 ]; then
    echo -e "\n✓ Compilação bem-sucedida!\n"
    echo "============================================"
    # Aumentar memória para mapas grandes (6 e 7)
    kotlin -J-Xmx8g -cp build MainKt
else
    echo "✗ Erro na compilação!"
    exit 1
fi
