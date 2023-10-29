pipeline {
    agent any
    stages {
        stage('Instalar Java 17') {
            steps {
                sh 'sudo apt update'
                sh 'sudo apt install openjdk-17-jre -y'
            }
        }
        stage('Instalar Maven') {
            steps {
                sh 'sudo apt install maven -y'
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
