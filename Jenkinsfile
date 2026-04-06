pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
        }
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package'
                }
            }
        }
    }
}