pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:8.11.1-jdk21-jammy
    command:
    - cat
    tty: true
    resources:
      requests:
        memory: "2Gi"
        cpu: "1"
      limits:
        memory: "4Gi"
        cpu: "2"
"""
        }
    }

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
                container('gradle') {
                    script {
                        withCredentials([usernamePassword(
                            credentialsId: 'dockerhub-credential',
                            usernameVariable: 'DOCKER_HUB_USERNAME',
                            passwordVariable: 'DOCKER_HUB_PASSWORD'
                        )]) {
                            sh """
                                ./gradlew jib \
                                    -Dorg.gradle.jvmargs="-Xmx2048m -XX:MaxMetaspaceSize=512m" \
                                    -Djib.from.image=eclipse-temurin:21-jdk-jammy \
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
}
