# 7. Monitoring and Analytics



- AWS 환경의 모니터링 방법을 요약할 수 있습니다.
- Amazon CloudWatch의 이점을 설명할 수 있습니다.
- AWS CloudTrail의 이점을 설명할 수 있습니다.
- AWS Trusted Advisor의 이점을 설명할 수 있습니다.



## Amazon CloudWatch

- 다양한 지표를 모니터링 및 관리하고 해당 지표의 데이터를 기반으로 경보 작업을 구성할 수 있는 웹 서비스입니다.



중앙위치에서 모든 지표에 엑세스

애플리케이션, 인프라 및 서비스에 대한 가시성 확보

MTTR 단축 TCO 개선

앱,리소스 최적화를 위한 인사이트 확보





## AWS CloudTrail



계정에 대한 API 호출을 기록합니다. 기록되는 정보에는 API 호출자 ID, API 호출 시간, API 호출자의 소스 IP 주소 등이 포함됩니다. CloudTrail은 누군가 남긴 이동 경로(또는 작업 로그)의 ‘추적’으로 생각할 수 있습니다.



### **AWS CloudTrail 이벤트**

커피숍 점주가 AWS Management Console의 AWS Identity and Access Management(IAM) 섹션을 탐색한다고 가정해 보겠습니다. 점주는 Mary라는 이름의 새 IAM 사용자가 생성된 것을 발견하지만 이 사용자를 만든 사람, 시기 또는 방법은 알 수 없습니다.



### **CloudTrail Insights**

CloudTrail에서 [**CloudTrail Insights**](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-insights-events-with-cloudtrail.html)를 활성화할 수도 있습니다. 이 옵션 기능을 사용하면 CloudTrail이 AWS 계정에서 비정상적인 API 활동을 자동으로 감지할 수 있습니다. 



## AWS Trusted Advisor



AWS 환경을 검사하고 AWS 모범 사례에 따라 실시간 권장 사항을 제시하는 웹 서비스입니다.

**비용 최적화, 성능, 보안, 내결함성, 서비스 한도**라는 5개 범주에서 결과를 AWS 모범 사례와 비교합니다. 

- 녹색 체크 표시는 **문제가 감지되지 않은** 항목 수를 나타냅니다.
- 주황색 삼각형은 권장 **조사** 항목 수를 나타냅니다.
- 빨간색 원은 권장 **조치** 수를 나타냅니다.



### Amazon GuardDuty

- Amazon GuardDuty는 AWS 환경 및 리소스에 대한 지능형 위협 탐지 기능을 제공하는 서비스입니다.
- 이 서비스는 AWS 환경 내의 네트워크 활동 및 계정 동작을 지속적으로 모니터링하여 위협을 식별합니다.





















































