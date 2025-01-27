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
        DISCORD_WEBHOOK = credentials('be-dev-deploy-discord-webhook-url')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                container('gradle') {
                    sh "./gradlew test"
                }
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
                                ./gradlew clean jib \
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

        stage('Update Helm Values') {
            steps {
                container('gradle') {
                    withCredentials([usernamePassword(credentialsId: 'github-credential', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh """
                            rm -rf deploy-repo
                            git clone https://\${GIT_USERNAME}:\${GIT_PASSWORD}@github.com/dygma0/literature-backend.git deploy-repo
                            cd deploy-repo
                            sed -i 's/tag: ".*"/tag: "${VERSION}"/g' deploy/values.yaml
                            git config --global user.email "webdev0594@gmail.com" 
                            git config --global user.name "dygma0"
                            git add deploy/values.yaml
                            git commit -m "🚀 BEHOLD! The grand deployment of version ${VERSION} has arrived! 🎉"
                            git push origin develop
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            discordSend description: """⚓ 항해 성공! 새로운 버전이 무사히 배포되었습니다! 🚢

🏴‍☠️ 배포 버전: ${VERSION}
🗺️ 항해 일지: ${BUILD_URL}
🎯 API 문서: https://gow-jvm-api-dev.cd80.run/swagger-ui/index.html
⏱️ 항해 시간: ${currentBuild.durationString}

순풍에 돛을 달고 새로운 버전이 안전하게 도착했습니다! 🌊""",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "🏴‍☠️ Literature Backend 배포 항해 #${BUILD_NUMBER}",
                    webhookURL: DISCORD_WEBHOOK
        }
        failure {
            discordSend description: """💥 난파 발생! 배포가 실패했습니다! ⚠️

🏴‍☠️ 시도한 버전: ${VERSION}
🗺️ 사고 위치: ${BUILD_URL}
⏱️ 표류 시간: ${currentBuild.durationString}
📜 사고 경위: ${currentBuild.description ?: '원인 불명의 사고입니다!'}

긴급 수리가 필요합니다! 선원들의 신속한 확인 바랍니다! 🔧""",
                    link: env.BUILD_URL,
                    result: currentBuild.currentResult,
                    title: "⚠️ Literature Backend 배포 사고 #${BUILD_NUMBER}",
                    webhookURL: DISCORD_WEBHOOK
        }
    }
}
