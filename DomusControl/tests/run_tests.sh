#!/usr/bin/env bash
#
# Compila o projeto e os testes, e corre a suite.
# Uso: ./tests/run_tests.sh   (a partir da raiz do projeto)

set -euo pipefail

cd "$(dirname "$0")/.."

echo "A compilar o projeto..."
mkdir -p build
find src -name "*.java" > /tmp/domus_sources.txt
javac -d build @/tmp/domus_sources.txt

echo "A compilar os testes..."
javac -cp build -d build tests/TestRunner.java

echo "A correr os testes..."
echo
java -cp build TestRunner
