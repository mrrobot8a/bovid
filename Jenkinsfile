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
        stage('Instalar mysql') {
            steps {
                sh 'apt install mysql-server -y'
                sh 'mysql_secure_installation'
                sh 'mysql -u root -e "CREATE DATABASE db_marcaganaderaTest;"'
                sh 'mysql -u root -e "CREATE USER \'root\'@\'localhost\' IDENTIFIED BY \'sasa\';"'
                sh 'mysql -u root -e "GRANT ALL PRIVILEGES ON root.* TO \'bovid\'@\'localhost\';"'
                sh 'mysql -u root -e "FLUSH PRIVILEGES;"'
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
