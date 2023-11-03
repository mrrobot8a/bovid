pipeline {
    agent any
    environment {
        JAR_FILE = ''
        MYSQL_HOST = 'localhost'
        MYSQL_PORT = '3306'
        MYSQL_DB = 'db_marcaganaderaTest'
        MYSQL_USER = 'root'
        MYSQL_PASSWORD = 'sasa'
        PROJECT_DIRECTORY = '/var/lib/jenkins/workspace/deploy-App-backend-springboot-bovid'
        PROJECT_DIRECTOR  = '/home/ubuntu/projectbovid/target/'
        
    }
    stages {
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
               sh "sudo find ${PROJECT_DIRECTORY} -mindepth 1 -delete"
              
            } 
                echo "El directorio ${PROJECT_DIRECTORY}  existe, se creará."
                sh " sudo git clone https://github.com/mrrobot8a/bovid.git ${PROJECT_DIRECTORY}"
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
                        sh 'sudo apt update'
                        sh 'sudo apt install maven -y'
                    } else {
                        sh 'echo " $M2_HOME"'
                        echo "Maven está instalado."
                    }
                }
            }
        }
        stage('Construir') {
            steps {
                script {
                   sh 'whoami'
                   sh 'pwd'
                   sh 'mvn clean install'
                }   
            }
                 
               
        }     
            
        stage('Desplegar') {
            steps {
                
                script {
                    JAR_FILE = sh(script: 'find /home/ubuntu/projectbovid/target -type f -name "*.jar" | head -1', returnStatus: true)
                    echo JAR_FILE
                    if (JAR_FILE == 0) {
                        echo "Archivo JAR no encontrado. Verifica la construcción."
                    }
                     sh "java -jar ${PROJECT_DIRECTOR}/${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
                }
            }
        }
            
                    
    }
        
       
}

