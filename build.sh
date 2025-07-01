#!/bin/bash

# Checking if the build version is given
if [ -z "$1" ]; then
  echo "Usage: ./build.sh <version>"
  exit 1
fi

VERSION=$1
JAR_NAME="BrickBreaker_${VERSION}.jar"
SRC_DIR="src"
RESOURCES_DIR="resources"
MAIN_CLASS="Main"
OUT_DIR="out"
RELEASE_DIR="release"

echo "==> Clearing OUT directory..."
rm -rf "$OUT_DIR"
mkdir -p "$OUT_DIR"

echo "==> Compiling source code..."
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$OUT_DIR" @sources.txt
rm sources.txt

echo "==> Copying resources from '$RESOURCES_DIR'..."
if [ -d "$RESOURCES_DIR" ]; then
  cp -r "$RESOURCES_DIR"/. "$OUT_DIR"/
else
  echo "⚠️  No folder: $RESOURCES_DIR – skip copying resources."
fi

echo "==> Creating manifest file..."
echo "Main-Class: $MAIN_CLASS" > manifest.txt

echo "==> Clearing old build in $RELEASE_DIR..."
mkdir -p "$RELEASE_DIR"
rm -f "$RELEASE_DIR"/BrickBreaker_*.jar

echo "==> Creating JAR archive..."
jar cfm "$RELEASE_DIR/$JAR_NAME" manifest.txt -C "$OUT_DIR" .

echo "✅ Ready! File: $RELEASE_DIR/$JAR_NAME"

# Clearing temporary manifest file
rm manifest.txt
