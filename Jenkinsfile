pipeline {
    agent any

    stages {

        stage('Verificar e instalar Git') {

            steps {
                script {
                    def gitInstalled = sh(script: 'git --version', returnStatus: true)
                    if (gitInstalled != 0) {
                        echo 'Git no está instalado. Se procederá a la instalación.'
                        // Instala Git en el servidor (ajusta el comando según tu sistema)
                        sh 'sudo apt-get update && sudo apt-get install git -y'
                    } else {
                        echo 'Git ya está instalado en el servidor.'
                    }
                }
            }
        }

        stage('Clonar código fuente') {
            steps {
                // Clona el repositorio de Git
                git  'https://github.com/mrrobot8a/bovid.git'
            }
        }
        
        stage('Instalar Maven') {
            steps {
                script {
                    def mavenInstalled = sh(script: 'mvn --version', returnStatus: true)
                    if (mavenInstalled != 0) {
                        echo 'Maven no está instalado. Se procederá a la instalación.'
                        // Instala Maven en el servidor (ajusta el comando según tu sistema)
                        sh 'apt-get update && sudo apt-get install maven -y'
                    } else {
                        echo 'Maven ya está instalado en el servidor.'
                    }
                }
            }
        }

        stage('Construir') {
            steps {
                sh 'chmod +x mvnw' // Da permisos de ejecución a Maven
                sh './mvnw clean package' // Utiliza Maven para construir el proyecto
            }
        }

        stage('Desplegar en el servidor') {

            steps {

                sh 'scp bovid-0.0.1-SNAPSHOT.jar ubuntu@172.203.81.195:/home/ubuntu/java' // Copia el archivo JAR al servidor

                sshagent(['llave-ssh-azure']) {

                    sh 'ssh ubuntu@172.203.81.195 "java -jar /home/ubuntu/java/bovid-0.0.1-SNAPSHOT.jar"' // Inicia la aplicación en el servidor
                }
            }
        }
    }
}
