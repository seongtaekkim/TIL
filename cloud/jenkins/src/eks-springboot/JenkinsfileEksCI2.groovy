
pipeline {
    tools {
        maven 'Maven3.9.6'
    }
    agent  {
        label 'dind-agent'
    }
    environment {
        registry = "738612635754.dkr.ecr.ap-northeast-2.amazonaws.com/stecr"
    }

    stages {
        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/jenkins']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: 'https://github.com/seongtaekkim/TIL']]])
            }
        }
        stage ('Build') {
            steps {
                sh 'mvn -f cloud/jenkins/src/eks-springboot clean package -Dmaven.test.skip=true'
            }
        }
        // Building Docker images
        stage('Building image') {
            steps{
                script {
                    dockerImage = docker.build registry
                    dockerImage.tag("$BUILD_NUMBER")
                }
            }
        }

        // Uploading Docker images into AWS ECR
        stage('Pushing to ECR') {
            steps{
                script {
                    sh 'aws ecr get-login-password --region ap-northeast-2 | docker login --username aws --password-stdin 738612635754.dkr.ecr.ap-northeast-2.amazonaws.com'
                    sh 'docker push a738612635754.dkr.ecr.ap-northeast-2.amazonaws.com/stecr:$BUILD_NUMBER'
                }
            }
        }
//        stage ('Helm Deploy') {
//            steps {
//                script {
//                    sh "helm upgrade first --install mychart --namespace helm-deployment --set image.tag=$BUILD_NUMBER"
//                }
//            }
//        }
    }
}
