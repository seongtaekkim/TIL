def NAMESPACE = "ns-project"

pipeline {
    agent any
    stages {
        stage('Maven Build') {
            steps {
                withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'Maven3.9.6', mavenSettingsConfig: '', traceability: true) {
                    sh 'mvn -f cloud/jenkins/src/local-k8s-springboot clean package -Dmaven.test.skip=true'
                }
            }
        }
        stage('Docker Build And Push') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com/', 'seongtaekkim') {
                        image = docker.build("seongtaekkim/local-k8s-springboot:v1", "./cloud/jenkins/src/local-k8s-springboot");
                        image.push();
                    }
                }
            }
        }
//        stage('Run kubectl') {
//            steps {
//                container('kubectl') {
//                    withCredentials([usernamePassword(
//                            credentialsId: 'seongtaekkim',
//                            usernameVariable: 'USERNAME',
//                            passwordVariable: 'PASSWORD')]) {
//                        /* namespace 존재여부 확인. 미존재시 namespace 생성 */
//                        sh "kubectl get ns ${NAMESPACE}|| kubectl create ns ${NAMESPACE}"
//
//                        /* secret 존재여부 확인. 미존재시 secret 생성 */
//                        sh """x
//                            kubectl get secret my-secret -n ${NAMESPACE} || \
//                            kubectl create secret docker-registry my-secret \
//                            --docker-server=https://registry.hub.docker.com/ \
//                            --docker-username=${USERNAME} \
//                            --docker-password=${PASSWORD} \
//                            -n ${NAMESPACE}
//                        """
//                        /* k8s-deployment.yaml 의 env값을 수정해준다(DATE로). 배포시 수정을 해주지 않으면 변경된 내용이 정상 배포되지 않는다. */
//                        /*sh "echo ${VERSION}"
//                        sh "sed -i.bak 's#VERSION_STRING#${VERSION}#' ./k8s/k8s-deployment.yaml"*/
//                        sh "echo ${DATE}"
//                        /*sh "sed -i.bak 's#DATE_STRING#${DATE}#' ./k8s/k8s-deployment.yaml"*/
//
//                        /* yaml파일로 배포를 수행한다 */
//                        sh "kubectl apply -f ./k8s/k8s-deployment.yaml -n ${NAMESPACE}"
//                        sh "kubectl apply -f ./k8s/k8s-service.yaml -n ${NAMESPACE}"
//                    }
//                }
//            }
//        }
    }
}
