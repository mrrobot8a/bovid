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
        stage('Instalar MySQL 8.0.33 y Crear Base de Datos') {
            steps {
                script {
                    sh 'wget https://dev.mysql.com/get/mysql-apt-config_0.8.15-1_all.deb'
                    sh 'sudo dpkg -i mysql-apt-config_0.8.15-1_all.deb'
                    sh 'echo "mysql-server mysql-server/root_password password root" | sudo debconf-set-selections'
                    sh 'echo "mysql-server mysql-server/root_password_again password root" | sudo debconf-set-selections'
                    sh 'sudo apt update'
                    sh 'sudo apt install mysql-server=8.0.33-1ubuntu18.04 -y'
                    sh 'sudo service mysql start'
                    sh 'mysql -u root -proot -e "CREATE DATABASE db_marcaganaderaTest;"'
                }
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
