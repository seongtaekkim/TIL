def label = "hellonode-${UUID.randomUUID().toString()}"

podTemplate(
        label: label,
        containers: [
                containerTemplate(name: "kubectl", image: "lachlanevenson/k8s-kubectl", command: "cat", ttyEnabled: true)
        ],
)
        {
            node(label) {
                stage("Get Source") {
                    git branch: 'jenkins', url: 'https://github.com/seongtaekkim/TIL.git'
                }

                //-- 환경변수 파일 읽어서 변수값 셋팅
                def props = readProperties  file:"cloud/jenkins/src/eks-springboot/pipeline.properties"
                def deployment = props["deployment"]
                def service = props["service"]
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
                        }
                    }

                } catch(e) {
                    currentBuild.result = "FAILED"
                }
            }
        }
