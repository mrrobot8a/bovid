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
                // Aquí puedes agregar pasos para compilar tu código si es necesario
            }
        }
        
        stage('Desplegar en el servidor') {
            steps {
                // Aquí puedes agregar pasos para desplegar tu aplicación en el servidor
            }
        }
    }
}
