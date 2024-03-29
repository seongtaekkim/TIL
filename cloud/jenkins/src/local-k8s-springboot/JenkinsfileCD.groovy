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
                def props = readProperties  file:"cloud/jenkins/src/local-k8s-springboot/pipeline.properties"
                def tag = props["version"]
                def dockerRegistry = props["dockerRegistry"]
                def credential_registry=props["credential_registry"]
                def image = props["image"]
                def deployment = props["deployment"]
                def service = props["service"]
                def ingress = props["ingress"]
                def label_key = props["label_key"]
                def label_value = props["label_value"]
                def namespace = props["namespace"]

                try {
                    stage( "Clean Up Existing Deployments" ) {
                        container("kubectl") {
                            sh "kubectl delete deployments -n ${namespace} -l ${label_key}=${label_value}"
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
