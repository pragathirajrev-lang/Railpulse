#!/usr/bin/env sh
DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -f "$DIR/tools/maven/apache-maven-3.9.16/bin/mvn" ]; then
    exec "$DIR/tools/maven/apache-maven-3.9.16/bin/mvn" "$@"
else
    exec mvn "$@"
fi

