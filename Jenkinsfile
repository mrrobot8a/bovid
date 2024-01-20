pipeline {
    agent any

    
    tools {
        // Asegúrate de que el nombre 'Maven-3.9.3' coincida con el configurado en Global Tool Configuration
        maven 'Maven-3.9.3-bovid'
    }
    
    
    environment {
        JAR_FILE = ''
        MYSQL_HOST = 'localhost'
        MYSQL_PORT = '3306'
        MYSQL_DB = 'db_marcaganaderaTest'
        MYSQL_USER = 'alcaldia'
        MYSQL_PASSWORD = 'Admin-1230'
        PROJECT_DIRECTORY = '/var/lib/jenkins/workspace/deploy-App-backend-springboot-bovid'
        PROJECT_DIRECTOR  = '/home/ubuntu/projectbovid/target/'
        
    }
   

    stages {

        stage('Detener JAR en ejecución') {
            steps {
                script {
                    def isRunning = sh(script: "ps aux | grep '[j]ava -jar .*${PROJECT_DIRECTORY}/.*\\.jar' | awk '{print \$2}'", returnStdout: true).trim()
                    if (isRunning) {
                        echo "Deteniendo el JAR en ejecución con PID: ${isRunning}"
                        sh "kill ${isRunning}"
                    } else {
                        echo "No hay instancias del JAR ejecutándose."
                    }
                }
            }
        }
        stage('Preparar carpetas') {
            steps {
               sh 'chown -R jenkins:jenkins /var/lib/jenkins/'
            }
            
        }
        stage('Verificar Java') {
            steps {
                script {
                    def javaVersion = sh(script: 'java -version', returnStatus: true)
                   
                     
                        if (javaVersion != 0) {
                         echo "Java no está instalado. Instalando Java..."
                         sh 'apt update'
                         sh 'apt install openjdk-17-jre -y'
                         sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
                     } else {
                         echo "Java 17 está instalado."
                     }
                }
            }
        }
        stage('Clonar Repositorio') {
            steps {
                script {
                    sh 'whoami'
           

                    // Verifica si el directorio ya existe
            def projectDir = sh(script: "if [ -d ${PROJECT_DIRECTORY}/src ]; then echo 'true'; else echo 'false'; fi", returnStdout: true).trim()
            
            if (projectDir == 'true') {
                echo "El directorio ${PROJECT_DIRECTORY} existe, se eliminará."
              //  sh "sudo rm -rf ${PROJECT_DIRECTORY}/.* ${PROJECT_DIRECTORY}/*"
               sh "find ${PROJECT_DIRECTORY} -mindepth 1 -delete"
              
            } 
                echo "El directorio ${PROJECT_DIRECTORY}  existe, se creará."
                sh "git clone https://github.com/mrrobot8a/bovid.git ${PROJECT_DIRECTORY}"
               // sh " sudo mkdir ${PROJECT_DIRECTORY}"
            
             }
            }
        }
        stage('Verificar Maven') {
            steps {
                script {
                    def mavenVersion = sh(script: 'mvn -v', returnStatus: true)
                    if (mavenVersion != 0) {
                        echo "Maven no está instalado. Instalando Maven..."
                        sh 'apt update'
                        sh 'apt install maven -y'
                    } else {
                        sh 'echo " $M2_HOME"'
                        echo "Maven está instalado."
                    }
                }
            }
        }
        stage('Crear Base de Datos') {
    steps {
        script {
            // Comando para verificar si la base de datos ya existe
            def databaseExists = sh(script: "mysql -h localhost -P 3306 -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -e \"SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '${MYSQL_DB}';\" | grep -q '${MYSQL_DB}'", returnStatus: true)

            if (databaseExists == 0) {
                echo "La base de datos ${MYSQL_DB} ya existe. No es necesario crearla."
            } else {
                echo "La base de datos ${MYSQL_DB} no existe. Creándola..."
                sh "mysql -h localhost -P 3306 -u root -p${MYSQL_PASSWORD} -e \"CREATE DATABASE ${MYSQL_DB};\""
                echo "Base de datos ${MYSQL_DB} creada con éxito."
            }
        }
    }
}
        stage('Construir') {
            steps {
                script {
                   sh 'whoami'
                   sh 'pwd'
                //   sh 'mvn clean install'
                   // Define the path to your Maven 3.9.3 installation
                    // def mavenHome = '/var/lib/jenkins/sdkmaven/apache-maven-3.9.3'

                    // Use the specified Maven version
                    sh "mvn clean package"
                }   
            }
                 
               
        }     
            
        stage('Desplegar') {
            steps {
                
                script {
                    
                 try {
                // Use the find command to locate the .jar file in the target directory
                JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true ).trim()
                echo "JAR file found: ${JAR_FILE}"
               


                if (JAR_FILE == null) {
                    error "Archivo JAR no encontrado en la carpeta 'target'. Verifica la construcción."
                }

                echo "Jar file found: ${JAR_FILE}"
                sh "java -jar ${PROJECT_DIRECTORY}/${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
                } catch (Exception e) {
                echo "Ocurrió un error en la etapa de despliegue: ${e}"
                currentBuild.result = 'FAILURE' // Marcar el pipeline como fallido
            }
            
                }
            }
        }
        
        stage('notificar') {
            
             steps {
                  
                   echo "JAR file found:"
             }
            
        }
            
                    
    }
        
       
}

