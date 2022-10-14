pipeline {
  agent any
  environment {
    dockerHubRegistry = 'jeeseob/groom-devOps-BE-reservation'
    dockerHubRegistryCredential = 'docker-credential'
  }
  stages {

    stage('Checkout Application Git Branch') {
        steps {
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
                }
        }
    }

   stage('Gradle Jar Build') {
    agent any
        steps {
            echo 'Bulid Gradle'
            dir ('.'){
                sh """
                ./gradlew build -x test
                """
            }
        }
        post {
            failure {
                error 'This pipeline stops gradle Jar Build'
            }
        }
    }

    stage('Docker Image Build') {
        steps {
            sh "cp build/libs/reservation-server-0.0.1-SNAPSHOT.jar ./"
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
