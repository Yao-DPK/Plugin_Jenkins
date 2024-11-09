
#!/bin/bash

# Chemin vers le fichier de consommation énergétique
file_path="/sys/devices/virtual/powercap/intel-rapl/intel-rapl:0/energy_uj"

# Vérifier si le fichier existe
if [ -f "$file_path" ]; then
	# Lire le contenu du fichier et l'afficher dans la console Jenkins
	cat "$file_path"
else
	echo "Le fichier $file_path n'existe pas."
	exit 1
fi
