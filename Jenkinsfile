pipeline {
    agent any
    environment {
        // Definimos una variable de entorno para almacenar el nombre del archivo JAR
        JAR_FILE = ''
    }
    stages {
        stage('Instalar Java 17') {
            steps {
                sh 'apt update'
                sh 'apt install openjdk-17-jre -y'
                sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
            }
        }
        stage('Instalar MySQL 8.0.33') {
            steps {
                sh 'apt update'
                sh 'apt install mysql-server=8.0.33-1ubuntu18.04 -y'
            }
        }
        stage('Iniciar MySQL y Crear la Base de Datos') {
            steps {
                sh 'service mysql start'
                sh 'mysql -e "CREATE DATABASE db_marcaganaderaTest;"'
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
                script {
                    // Usamos el comando find para buscar el archivo JAR generado y almacenar su nombre
                    JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
                }
                sh "java -jar ${JAR_FILE}"
            }
        }
    }
}
