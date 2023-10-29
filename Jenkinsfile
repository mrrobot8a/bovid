pipeline {
    agent any
    environment {
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
        stage('Importar clave pública de MySQL') {
            steps {
                sh 'wget https://dev.mysql.com/get/mysql-apt-config_0.8.15-1_all.deb'
                sh 'dpkg -i mysql-apt-config_0.8.15-1_all.deb'
                sh 'apt-key adv --keyserver keys.gnupg.net --recv-keys 467B942D3A79BD29'
                sh 'apt update'
            }
        }
        stage('Instalar MySQL 8.0.33 y Crear Base de Datos') {
            steps {
                script {
                    sh 'echo "mysql-server mysql-server/root_password password root" | debconf-set-selections'
                    sh 'echo "mysql-server mysql-server/root_password_again password root" | debconf-set-selections'
                    sh 'apt install mysql-server=8.0.33-1ubuntu18.04 -y'
                    sh 'service mysql start'

                    withCredentials([usernamePassword(credentialsId: 'mysql-credentials', passwordVariable: 'MYSQL_PASSWORD', usernameVariable: 'MYSQL_USERNAME')]) {
                        sh "mysql -u ${MYSQL_USERNAME} -p${MYSQL_PASSWORD} -e 'CREATE DATABASE db_marcaganaderaTest;'"
                    }
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
                    JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
                }
                sh "java -jar ${JAR_FILE}"
            }
        }
    }
}
