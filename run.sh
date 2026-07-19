#!/usr/bin/env bash
#
# Compila o projeto para build/ e arranca a aplicação.
# Uso: ./run.sh

set -euo pipefail

cd "$(dirname "$0")"

echo "A compilar..."
mkdir -p build
find src -name "*.java" > /tmp/domus_sources.txt
javac -d build @/tmp/domus_sources.txt

echo "A arrancar o DomusControl..."
java -cp build app.Main
