def label = "hellonode-${UUID.randomUUID().toString()}"

podTemplate(
        label: label,
        containers: [
                //container image는 docker search 명령 이용
                containerTemplate(name: "docker", image: "docker:latest", ttyEnabled: true, command: "cat"),
                containerTemplate(name: "kubectl", image: "lachlanevenson/k8s-kubectl", command: "cat", ttyEnabled: true)
        ],
        //volume mount
        volumes: [
                hostPathVolume(hostPath: "/var/run/docker.sock", mountPath: "/var/run/docker.sock")
        ]
)
        {
            node(label) {
                stage("Get Source") {
                    //git url:"https://github.com/happykube/hellonode.git", branch: "main", credentialsId: "git_credential"
                    git branch: 'jenkins', url: 'https://github.com/seongtaekkim/TIL.git'
                }

                //-- 환경변수 파일 읽어서 변수값 셋팅
                def props = readProperties  file:"cloud/jenkins/src/demo/pipeline.properties"
                def tag = props["version"]
                def dockerRegistry = props["dockerRegistry"]
                def credential_registry=props["credential_registry"]
                def image = props["image"]
                def deployment = props["deployment"]
                def service = props["service"]
                def ingress = props["ingress"]
                def selector_key = props["selector_key"]
                def selector_val = props["selector_val"]
                def namespace = props["namespace"]

                try {
                    stage("Build Microservice image") {
                        container("docker") {
                            docker.withRegistry("${dockerRegistry}", "${credential_registry}") {
                                sh "docker build -f ./cloud/jenkins/src/demo/Dockerfile -t ${image}:${tag} ."
                                sh "docker push ${image}:${tag}"
                                sh "docker tag ${image}:${tag} ${image}:latest"
                                sh "docker push ${image}:latest"
                            }
                        }
                    }
                    stage( "Clean Up Existing Deployments" ) {
                        container("kubectl") {
                            sh "kubectl delete deployments -n ${namespace} --selector=${selector_key}=${selector_val}"
                        }
                    }

                    stage( "Deploy to Cluster" ) {
                        container("kubectl") {
                            sh "kubectl apply -n ${namespace} -f ${deployment}"
                            sh "sleep 5"
                            sh "kubectl apply -n ${namespace} -f ${service}"
//                            sh "kubectl apply -n ${namespace} -f ${ingress}"
                        }
                    }

                } catch(e) {
                    currentBuild.result = "FAILED"
                }
            }
        }
