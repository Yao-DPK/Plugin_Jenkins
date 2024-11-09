pipeline {
    agent any

    stages {
        stage('Capture PID') {
            steps {
                script {
                    def my_pid = sh(script: 'my_command & echo $!', returnStdout: true).trim()
                    writeFile file: 'pids.txt', text: my_pid
                }
            }
        }

        stage('Loop over PIDs') {
            steps {
                script {
                    def pids = readFile('pids.txt').trim().split('\n')

                    for (pid in pids) {
                        echo "PID: $pid"
                    }
                }
            }
        }
    }
}