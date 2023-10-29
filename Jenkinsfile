pipeline {
    agent any

    stages {
        stage('Clonar código fuente') {
            steps {
                // Clona el repositorio de Git
                git clone 'https://tu-repositorio-git.com/tu-proyecto.git'
            }
        }

        stage('Construir') {
            steps {
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
