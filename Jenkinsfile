pipeline {
    agent any
    
    environment {
        VERSION = sh(script: "echo \$(date +%s) | md5sum | cut -d' ' -f1", returnStdout: true).trim()
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build & Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'dockerhub-credential',
                        usernameVariable: 'DOCKER_HUB_USERNAME',
                        passwordVariable: 'DOCKER_HUB_PASSWORD'
                    )]) {
                        sh """
                            ./gradlew jib \
                                -Djib.to.tags=${VERSION} \
                                -Djib.to.auth.username=${DOCKER_HUB_USERNAME} \
                                -Djib.to.auth.password=${DOCKER_HUB_PASSWORD}
                        """
                    }
                }
            }
        }
    }
}
