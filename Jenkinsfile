// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//     }
//     stages {
//         stage('Instalar Java 17') {
//             steps {
//                 sh 'apt update'
//                 sh 'apt install openjdk-17-jre -y'
//                 sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//             }
//         }
//         stage('Instalar Maven') {
//             steps {
//                 sh 'apt install maven -y'
//             }
//         }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }

// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//     }
//     stages {
//         stage('Verificar Java') {
//             steps {
//                 script {
//                     def javaVersion = sh(script: 'java -version', returnStatus: true)
//                     if (javaVersion != 0) {
//                         echo "Java no está instalado. Instalando Java..."
//                         sh 'apt update'
//                         sh 'apt install openjdk-17-jre -y'
//                         sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//                     } else {
//                         echo "Java 17 está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Verificar Maven') {
//             steps {
//                 script {
//                     def mavenVersion = sh(script: 'mvn -v', returnStatus: true)
//                     if (mavenVersion != 0) {
//                         echo "Maven no está instalado. Instalando Maven..."
//                         sh 'apt update'
//                         sh 'apt install maven -y'
//                     } else {
//                         echo "Maven está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Crear Base de Datos') {
//             steps {
//                 script {
//                     sh "apt install mysql-client -y"
//                     sh "mysql -h ${MYSQL_HOST} -P ${MYSQL_PORT} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -e 'CREATE DATABASE ${MYSQL_DB};'"
//                 }
//             }
//         }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStatus: true)
//                     if (JAR_FILE == 0) {
//                         echo "Archivo JAR no encontrado. Verifica la construcción."
//                     }
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }



// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//     }
//     stages {
//         stage('Verificar Java') {
//             steps {
//                 script {
//                     def javaVersion = sh(script: 'java -version', returnStatus: true, returnStdout: true)
//                     if (javaVersion.startsWith('openjdk version "17')) {
//                         echo "Java 17 está instalado."
//                     } else {
//                          sh 'apt update'
//                 sh 'apt install openjdk-17-jre -y'
//                 sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//                     }
//                 }
//             }
//         }
//         stage('Verificar Maven') {
//             steps {
//                 script {
//                     def mavenVersion = sh(script: 'mvn -v', returnStatus: true, returnStdout: true)
//                     if (mavenVersion.startsWith('Apache Maven')) {
//                         echo "Maven está instalado."
//                     } else {
//                        sh 'apt update'
//                        sh 'apt install maven -y'
//                     }
//                 }
//             }
//         }
//         stage('Crear Base de Datos') {
//     steps {
//         script {
//             sh 'mysql -h 10.0.0.5 -P 3306 -u root -psasa -e "CREATE DATABASE db_marcaganaderaTest;"'
//             sh "mysql -h ${MYSQL_HOST} -P ${MYSQL_PORT} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -e 'CREATE DATABASE ${MYSQL_DB};'"
//         }
//     }
// }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }

// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//         SSH_USER = 'ubuntu'  // Reemplaza con tu usuario SSH
//         SSH_SERVER = '172.203.112.32'  // Reemplaza con la dirección de tu servidor SSH
//         SSH_PORT = '22'  // Puerto SSH predeterminado
//     }
//     stages {
//         stage('Verificar Java') {
//             steps {
//                 script {
//                     def javaVersion = sh(script: 'java -version', returnStatus: true, returnStdout: true)
//                     if (javaVersion.startsWith('openjdk version "17')) {
//                         echo "Java 17 está instalado."
//                     } else {
//                         sh 'apt update'
//                         sh 'apt install openjdk-17-jre -y'
//                         sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//                     }
//                 }
//             }
//         }
//         stage('Verificar Maven') {
//             steps {
//                 script {
//                     def mavenVersion = sh(script: 'mvn -v', returnStatus: true, returnStdout: true)
//                     if (mavenVersion.startsWith('Apache Maven')) {
//                         echo "Maven está instalado."
//                     } else {
//                         sh 'apt update'
//                         sh 'apt install maven -y'
//                     }
//                 }
//             }
//         }
//         stage('Crear Base de Datos') {
//             steps {
//                 script {
//                     def sshCommand = "ssh -p ${SSH_PORT} ${SSH_USER}@${SSH_SERVER} "
//                     def createDbCommand = "mysql -h ${MYSQL_HOST} -P ${MYSQL_PORT} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -e 'CREATE DATABASE ${MYSQL_DB};'"
//                     sh "${sshCommand}${createDbCommand}"
//                 }
//             }
//         }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStdout: true).trim()
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }

// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//         SSH_USER = 'ubuntu'  // Reemplaza con tu usuario SSH
//         SSH_SERVER = '172.203.112.32'  // Reemplaza con la dirección de tu servidor SSH
//         SSH_PORT = '22'  // Puerto SSH predeterminado
//     }
//     stages {
//         stage('Verificar Java') {
//             steps {
//                 script {
//                     def javaVersion = sh(script: 'java -version', returnStatus: true)
//                     if (javaVersion != 0) {
//                         echo "Java no está instalado. Instalando Java..."
//                         sh 'apt update'
//                         sh 'apt install openjdk-17-jre -y'
//                         sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//                     } else {
//                         echo "Java 17 está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Verificar Maven') {
//             steps {
//                 script {
//                     def mavenVersion = sh(script: 'mvn -v', returnStatus: true)
//                     if (mavenVersion != 0) {
//                         echo "Maven no está instalado. Instalando Maven..."
//                         sh 'apt update'
//                         sh 'apt install maven -y'
//                     } else {
//                         echo "Maven está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Crear Base de Datos') {
//             steps {
//                 script {
//                     // Utiliza el archivo PEM copiado en el contenedor
//                     sh 'ssh -i /root/ubuntu_key.pem -p ${SSH_PORT} ${SSH_USER}@${SSH_SERVER} "mysql -h localhost -P 3306 -u root -p ${MYSQL_PASSWORD} -e \"CREATE DATABASE ${MYSQL_DB};\""'
//                 }
//             }
//        }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStatus: true)
//                     if (JAR_FILE == 0) {
//                         echo "Archivo JAR no encontrado. Verifica la construcción."
//                     }
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }



// pipeline {
//     agent any
//     environment {
//         JAR_FILE = ''
//         MYSQL_HOST = 'localhost'
//         MYSQL_PORT = '3306'
//         MYSQL_DB = 'db_marcaganaderaTest'
//         MYSQL_USER = 'root'
//         MYSQL_PASSWORD = 'sasa'
//         SSH_USER = 'ubuntu'  // Reemplaza con tu usuario SSH
//         SSH_SERVER = '172.203.112.32'  // Reemplaza con la dirección de tu servidor SSH
//         SSH_PORT = '22'  // Puerto SSH predeterminado
//     }
//     stages {
//         stage('Verificar Java') {
//             steps {
//                 script {
//                     def javaVersion = sh(script: 'java -version', returnStatus: true)
//                     if (javaVersion != 0) {
//                         echo "Java no está instalado. Instalando Java..."
//                         sh 'apt update'
//                         sh 'apt install openjdk-17-jre -y'
//                         sh 'echo "JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> /etc/environment'
//                     } else {
//                         echo "Java 17 está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Verificar Maven') {
//             steps {
//                 script {
//                     def mavenVersion = sh(script: 'mvn -v', returnStatus: true)
//                     if (mavenVersion != 0) {
//                         echo "Maven no está instalado. Instalando Maven..."
//                         sh 'apt update'
//                         sh 'apt install maven -y'
//                     } else {
//                         echo "Maven está instalado."
//                     }
//                 }
//             }
//         }
//         stage('Crear Base de Datos') {
//             steps {
//                 script {
//                     def sshCommand = "ssh  -i /root/ubuntu_key.pem -p ${SSH_PORT} ${SSH_USER}@${SSH_SERVER}"
//                     sh '${sshCommand}'
//                     sh 'hostname'
//                     // Utiliza el archivo PEM copiado en el contenedor
//                     def mysqlCommand = "mysql -h localhost -P 3306 -u root -p${MYSQL_PASSWORD} -e \"CREATE DATABASE ${MYSQL_DB};\""
//                     sh "echo ${MYSQL_PASSWORD} | ${mysqlCommand}"
//                 }
//             }
//         }
//         stage('Clonar Repositorio') {
//             steps {
//                 git url: 'https://github.com/mrrobot8a/bovid.git'  // Reemplaza con la URL de tu repositorio
//             }
//         }
//         stage('Construir y Desplegar') {
//             steps {
//                 sh 'mvn clean install'
//                 script {
//                     JAR_FILE = sh(script: 'find target -type f -name "*.jar" | head -1', returnStatus: true)
//                     if (JAR_FILE == 0) {
//                         echo "Archivo JAR no encontrado. Verifica la construcción."
//                     }
//                 }
//                 sh "java -jar ${JAR_FILE} --spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DB} --spring.datasource.username=${MYSQL_USER} --spring.datasource.password=${MYSQL_PASSWORD}"
//             }
//         }
//     }
// }

