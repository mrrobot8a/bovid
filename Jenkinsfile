pipeline {
    agent any
    environment {
        JAR_FILE = ''
        MYSQL_HOST = 'localhost'
        MYSQL_PORT = '3306'
        MYSQL_DB = 'mydb'
        MYSQL_USER = 'root'
        MYSQL_PASSWORD = 'sasa'
    }
    stages {
        stage('Verificar Java') {
            steps {
                script {
                    def javaVersion = sh(script: 'java -version', returnStatus: true, returnStdout: true).trim()
                    if (javaVersion.startsWith('openjdk version "17')) {
                        echo "Java 17 está instalado."
                    } else {
                        error "Java 17 no está instalado. Por favor, instálalo antes de continuar."
                    }
                }
            }
        }
        stage('Verificar Maven') {
            steps {
                script {
                    def mavenVersion = sh(script: 'mvn -v', returnStatus: true, returnStdout: true).trim()
                    if (mavenVersion.startsWith('Apache Maven')) {
                        echo "Maven está instalado."
                    } else {
                        error "Maven no está instalado. Por favor, instálalo antes de continuar."
                    }
                }
            }
        }
        stage('Clonar Repositorio') {
            steps {
                git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
            }
        }
        stage('Construir y Desplegar') {
            steps {
                sh 'mvn clean package'
                script {
                    JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
                }
                sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
            }
        }
    }
}

