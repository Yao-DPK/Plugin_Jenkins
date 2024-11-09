#!/bin/bash

if [ $# -ne 2 ]; then
  echo "Usage: $0 <ip> <port>"
  exit 1
fi

IP=$1
PORT=$2

cd energy_checker/target || { echo "Le répertoire spécifié n'existe pas."; exit 1; }

python3 -m http.server "$PORT" --bind "$IP" &

HTTP_SERVER_PID=$!

sleep 5

ngrok http --domain=settled-leopard-flowing.ngrok-free.app "$IP:$PORT" &

sleep 5

wait $HTTP_SERVER_PID
