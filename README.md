# Projet-SI
Bienvenue sur le repo de notre projet de plugin Jenkins visant à fournir la consommation énergétique de pipelines Jenkins.

## Comment Utiliser le projet

### Lancement PowerAPI
Afin de pouvoir l’outil PowerAPI essentiel au projet, vous pouvez suivre les étapes suivantes:

### Étape 1 : Configuration des prérequis
1. **Accès à l'interface RAPL** : Configurer le fichier `sysfs.conf` pour gérer l'accès à l'interface RAPL.
2. **Cgroup** : Assurez-vous que Cgroup version 1 est installé et modifiez la configuration GRUB pour utiliser Cgroup v1. Exécutez `sudo update-grub` après modification.
3. **Docker et Docker Compose** : Installez Docker et Docker Compose.

### Étape 2 : Créer un cgroup pour le processus
- Pour un processus général :
  ```
  $ cgcreate -g perf_event:monprocessus
  $ cgclassify -g perf_event:monprocessus $(pidof monprocessus)
  ```
- Pour Jenkins :
  ```
  $ cgcreate -g perf_event:jenkins
  $ cgclassify -g perf_event:jenkins $(ps -Af | grep "jenkins" | grep -v grep | awk '{print $2}')
  ```

### Étape 3 : Définir les variables d'environnement
- Définissez les ports, identifiants et autres variables nécessaires dans les fichiers `.env` et dans le fichier `stack_powerapi.yml`.

### Étape 4 : Génération des fichiers de configuration
- Exécutez le script `config_files_powerapi_generator.sh` pour générer les fichiers de configuration nécessaires. Vérifiez et ajustez les fréquences du CPU dans `config_file_sw.json` si nécessaire.

### Étape 5 : Exécuter Docker Compose
- Lancez les services configurés avec :
  ```
  $ docker-compose up -d
  ```

Ou encore exécuter le fichier bash powerapi1.sh pour la première utilisation 

Pour les utilisations futurs, utiliser le fichier bash powerapi2.sh 

Dans le cas ou cela ne fonctionne pas ,  lancez le script clean_docker1.sh et relancez ensuite powerapi1.sh

Ces fichiers se trouvent dans le dossier ressources du plugin

### Comment Installer  le plugin 

Pour installer le plugin: 
1. exécuter mvn clean install dans le dossier energy_checker
2. récupérer le fichier .hpi présent dans le dossier targer
3. Uploader sur Jenkins en allant dans Jenkins > Manage Jenkin > Plugins > Advanced Settings et uploader le fichier .hpi
4. Relancer Jenkins

ou encore 

on lance un serveur qui va héberger le plugin (depending on config) : 

En étant dans le dossier target on fait: 
 
```
python3 -m http.server 8000 --bind 172.20.10.3
ngrok http --domain=settled-leopard-flowing.ngrok-free.app 172.20.10.3:8000
```

URL :
https://settled-leopard-flowing.ngrok-free.app/

# Plugin_Jenkins
# Plugin_Jenkins
# Plugin_Jenkins
