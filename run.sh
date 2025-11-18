#!/bin/bash

# Script para compilar e executar o solucionador de labirintos

echo "Compilando o projeto..."
kotlinc -d build -cp src src/Main.kt src/model/core/*.kt src/service/*.kt

if [ $? -eq 0 ]; then
    echo -e "\n✓ Compilação bem-sucedida!\n"
    echo "============================================"
    kotlin -cp build MainKt
else
    echo "✗ Erro na compilação!"
    exit 1
fi
