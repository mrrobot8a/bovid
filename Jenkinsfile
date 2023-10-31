pipeline {
    agent any
    environment {
        JAR_FILE = ''
        MYSQL_HOST = 'localhost'
        MYSQL_PORT = '3306'
        MYSQL_DB = 'db_marcaganaderaTest'
        MYSQL_USER = 'root'
        MYSQL_PASSWORD = 'sasa'
    }
    stages {
        stage('Instalar Java 17') {
            steps {
                sh 'apt update'
                sh 'apt install openjdk-17-jre -y'
                sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
            }
        }
        stage('Instalar Maven') {
            steps {
                sh 'apt install maven -y'
            }
        }
        stage('Instalar MySQL') {
            steps {
                sh 'wget https://dev.mysql.com/get/mysql-apt-config_0.8.17-1_all.deb'
                sh 'sudo dpkg -i mysql-apt-config_0.8.17-1_all.deb'
                sh 'sudo apt update'
                sh 'sudo apt install mysql-server'
                sh 'sudo systemctl status mysql'
            }
        }
        stage('Configurar MySQL') {
            steps {
                sh 'mysql -u root -e "CREATE DATABASE db_marcaganaderaTest;"'
            }
        }
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
            }
        }
        stage('Construir y Desplegar') {
            steps {
                sh 'mvn clean install'
                script {
                    JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
                }
                sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
            }
        }
    }
}
