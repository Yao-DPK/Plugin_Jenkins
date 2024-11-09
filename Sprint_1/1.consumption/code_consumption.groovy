pipeline {
    agent any

    environment {
   	 TEMPS_DEBUT = 0
   	 TEMPS_FIN = 0
   	 FICHIER_PATH = '/sys/devices/virtual/powercap/intel-rapl/intel-rapl:0/energy_uj'
    }

    stages {
   	 stage('Pré-Build') {
   		 steps {
       		 script {
           		 TEMPS_DEBUT = System.currentTimeMillis()
           		 def vALEUR_AVANT = sh(script: 'cat /sys/devices/virtual/powercap/intel-rapl/intel-rapl:0/energy_uj', returnStdout: true)
           		 echo "Valeur avant le build : ${vALEUR_AVANT}"
           		 env.VALEUR_AVANT = vALEUR_AVANT
          		 
       		 }
   		 }
   	 }
  	 
   	 stage('Build') {
   		 steps {
       		 echo 'Exécution du build du projet...'
       		 sh 'echo "Build en cours" && sleep 10'
   		 }
   	 }

   	 stage('Post-Build') {
   		 steps {
       		 script {
           		 def vALEUR_APRES = sh(script: 'cat /sys/devices/virtual/powercap/intel-rapl/intel-rapl:0/energy_uj', returnStdout: true)
           		 echo "Valeur après le build : ${vALEUR_APRES}"
           		 env.VALEUR_APRES = vALEUR_APRES
           		 TEMPS_FIN = System.currentTimeMillis()
       		 }
   		 }
   	 }

   	 stage('Résultats') {
   		 steps {
       		 script {
       		 	echo "${env.VALEUR_AVANT}"
           		 long vApres = Long.parseLong(env.VALEUR_APRES.trim())
           		 long vAvant = Long.parseLong(env.VALEUR_AVANT.trim())
           		 long difference = vApres - vAvant
           		 def tempsEcoule = (TEMPS_FIN - TEMPS_DEBUT) / 1000
           		 //def intDiff = Integer.parseInt(difference)
           		 //def tempsEcoule = (TEMPS_FIN - TEMPS_DEBUT) / 1000
           		 def consommation = difference / tempsEcoule / 1000000

           		 echo "Différence de valeur : ${difference}"
           		 echo "Temps écoulé : ${tempsEcoule} secondes"
           		 echo "Consommation : ${consommation} Watts"
       		 }
   		 }
   	 }
    }
    
    post {
   	 always {
   		 echo 'pipeline terminée'
   	 }
    }
}


