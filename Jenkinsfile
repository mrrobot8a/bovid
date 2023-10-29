pipeline {
    agent any
    stages {
        stage('Instalar Java 17') {
            steps {
                sh 'apt update'
                sh 'apt install openjdk-17-jre -y'
                sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
                sh 'source /etc/environment'
                sh 'echo pwd'
            }
        }
        stage('Instalar Maven') {
            steps {
                sh 'apt install maven -y'
            }
        }
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/mrrobot8a/bovid.git'
            }
        }
        stage('Construir y Desplegar') {
            steps {
                sh 'mvn clean install'
                sh 'java -jar target/nombre-del-archivo.jar'  // Reemplaza "nombre-del-archivo.jar" con el nombre correcto de tu archivo JAR
            }
        }
    }
}
