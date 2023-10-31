pipeline {
    agent any
    environment {
        JAR_FILE = ''
    }
    stages {
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/mrrobot8a/bovid.git'
            }
        }
        stage('Construir y Desplegar') {
            steps {
                sh 'mvn clean package'
                script {
                    JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
                }
                sh "java -jar ${JAR_FILE}"
            }
        }
    }
}
