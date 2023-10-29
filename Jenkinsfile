pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/mrrobot8a/bovid.git'
        TARGET_FOLDER = '/home/ubuntu/java'
    }

    stages {
        stage('Clonar código fuente') {
            steps {
                sh "git clone $GIT_REPO_URL $TARGET_FOLDER"
            }
        }

        stage('Construir') {
            steps {
                // Agrega pasos de construcción específicos para tu proyecto, por ejemplo, si es un proyecto Maven:
                sh "cd $TARGET_FOLDER" // Cambia al directorio del proyecto
                sh './mvnw clean package' // Utiliza Maven para construir el proyecto
            }
        }
        
        stage('Desplegar en el servidor') {
            steps {
                // Agrega pasos para copiar y desplegar tu aplicación en el servidor
                sh "scp $TARGET_FOLDER/tu-aplicacion.jar ubuntu@172.203.81.195:/ruta/de/despliegue/"
                sh 'ssh ubuntu@172.203.81.195 "java -jar /ruta/de/despliegue/tu-aplicacion.jar"'
            }
        }
    }
}
