#!/bin/sh
# Script para rodar a aplicação carregando variáveis do .env automaticamente

if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

./mvnw quarkus:dev
