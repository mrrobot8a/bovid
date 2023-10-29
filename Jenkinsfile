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
        stage('Instalar Maven') {
            steps {
                sh 'apt install maven -y'
            }
        }
        stage('Instalar MySQL y crear base de datos') {
            steps {
                script {
                    sh 'apt install mysql-server -y'
                    sh 'service mysql start'
                    sh 'mysqladmin -u root password "sasa"'
                    sh 'mysql -u root -p"sasa" -e "CREATE DATABASE nombre_de_tu_base_de_datos"'
                    sh 'sed -i "s/bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/mysql.conf.d/mysqld.cnf'
                    sh 'service mysql restart'
                }
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
