pipeline {
  agent any
  environment {
    dockerHubRegistry = 'jeeseob/groom-devOps-BE-reservation'
    dockerHubRegistryCredential = 'docker-credential'
  }
  stages {

    stage('Checkout Application Git Branch') {
        steps {
           script {
                    SLACK_CHANNEL = "jenkins"
                    SLACK_SUCCESS_COLOR = "#2C953C";
                    SLACK_FAIL_COLOR = "#FF3232";
                    // Git Commit 계정
                    GIT_COMMIT_AUTHOR = sh(script: "git --no-pager show -s --format=%an ${env.GIT_COMMIT}", returnStdout: true).trim();
                    // Git Commit 메시지
                    GIT_COMMIT_MESSAGE = sh(script: "git --no-pager show -s --format=%B ${env.GIT_COMMIT}", returnStdout: true).trim();
                }
            git credentialsId: 'github-credential',
                url: 'https://github.com/GROOM-PJT/Reservation.git',
                branch: 'main'
        }
        post {
                failure {
                  echo 'Repository clone failure !'
                }
                success {
                  echo 'Repository clone success !'
                  slackSend (
                        channel: SLACK_CHANNEL,
                        color: SLACK_SUCCESS_COLOR,
                        message: "==================================================================\n배포 파이프라인이 시작되었습니다.\n${GIT_COMMIT_AUTHOR} - ${GIT_COMMIT_MESSAGE}\n==================================================================\n"
                    )
                }
        }
    }

   stage('Gradle Jar Build') {
    agent any
        steps {
            echo 'Bulid Gradle'
            dir ('.'){
                sh """
                ./gradlew clean build --exclude-task test
                """
            }
        }
        post {
            failure {
                error 'This pipeline stops gradle Jar Build'
            }
            success {
                  echo 'Gradle Build Success!'
                  slackSend (
                        channel: SLACK_CHANNEL,
                        color: SLACK_SUCCESS_COLOR,
                        message: "==================================================================\nGradle Build Success!\n==================================================================\n"
                    )
                }
        }
    }

    stage('Docker Image Build') {
        steps {
            // sh "cp ./build/libs/reservation-server-0.0.1-SNAPSHOT.jar ./"
            sh "docker build . -t ${dockerHubRegistry}:${currentBuild.number}"
            sh "docker build . -t ${dockerHubRegistry}:latest"
        }
        post {
                failure {
                  echo 'Docker image build failure !'
                }
                success {
                  echo 'Docker image build success !'
                }
        }
    }

    stage('Docker Image Push') {
        steps {
            withDockerRegistry([ credentialsId: dockerHubRegistryCredential, url: "" ]) {
                                sh "docker push ${dockerHubRegistry}:${currentBuild.number}"
                                sh "docker push ${dockerHubRegistry}:latest"

                                sleep 20 /* Wait uploading */ 
                            }
        }
        post {
                failure {
                  echo 'Docker Image Push failure !'
                  sh "docker rmi ${dockerHubRegistry}:${currentBuild.number}"
                  sh "docker rmi ${dockerHubRegistry}:latest"
                }
                success {
                  echo 'Docker image push success !'
                  sh "docker rmi ${dockerHubRegistry}:${currentBuild.number}"
                  sh "docker rmi ${dockerHubRegistry}:latest"
                }
        }
    }

    stage('K8S Manifest Update') {
        steps {
            git credentialsId: 'github-credential',
                url: 'https://github.com/GROOM-PJT/gitOps.git',
                branch: 'main'

            sh "sed -i 'jeeseob/groom-devOps-BE-reservation:.*\$/groom-devOps-BE-reservation:${currentBuild.number}/g' deployment.yaml"
            sh "git add deployment.yaml"
            sh "git commit -m '[UPDATE] groom-devOps-BE-reservation ${currentBuild.number} image versioning'"
            sshagent(credentials: ['github-credential']) {
                sh "git remote set-url origin git@github.com:GROOM-PJT/gitOps.git"
                sh "git push -u origin main"
             }
        }
        post {
                failure {
                  echo 'K8S Manifest Update failure !'
                }
                success {
                  echo 'K8S Manifest Update success !'
                }
        }
    }
  }
}
