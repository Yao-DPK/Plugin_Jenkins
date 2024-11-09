#!/bin/bash


echo "Nom de l'hôte : $(hostname)"


echo "Système d'exploitation : $(uname -o)"


echo "Version du noyau : $(uname -r)"


echo "Architecture : $(uname -m)"


echo "Mémoire disponible :"
free -h | grep "Mem:" | awk '{print $2 " de RAM disponible"}'


echo "Espace disque disponible :"
df -h | grep "^/dev" | awk '{print $6 ": " $4 " disponibles"}'
