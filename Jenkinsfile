pipeline {
    agent any
    environment {
        JAR_FILE = ''
    }
    stages {
        
        stage('Instalar MySQL y crear base de datos') {
            steps {
                script {
                    // Agregar la clave GPG de MySQL
                    sh 'apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 467B942D3A79BD29'

                    // Actualizar el sistema para reflejar la nueva clave GPG
                    sh 'apt update'

                    // Instalar MySQL
                    sh 'apt install mysql-server -y'

                    // Iniciar el servicio de MySQL
                    sh 'service mysql start'

                    // Establecer la contraseña de root como "sasa" (puedes cambiarla si lo deseas)
                    sh 'mysqladmin -u root password "sasa"'

                    // Crear una base de datos (reemplaza 'nombre_de_tu_base_de_datos' con el nombre deseado)
                    sh 'mysql -u root -p"sasa" -e "CREATE DATABASE db_marcaganaderaTest"'

                    // Asegurar que MySQL escuche en todas las interfaces (opcional, asegúrate de los riesgos de seguridad)
                    sh 'sed -i "s/bind-address.*/bind-address = 0.0.0.0/" /etc/mysql/mysql.conf.d/mysqld.cnf'
                    sh 'service mysql restart'
                }
            }
        }
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
