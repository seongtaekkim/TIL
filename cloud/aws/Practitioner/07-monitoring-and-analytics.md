# 07. Monitoring and Analytics

- AWS 환경의 모니터링 방법을 요약
- Amazon CloudWatch
- AWS CloudTrail
- AWS Trusted Advisor



## 1. Amazon CloudWatch

- 다양한 지표를 모니터링 및 관리하고 해당 지표의 데이터를 기반으로 경보 작업을 구성할 수 있는 웹 서비스입니다.



- AWS 인프라와 AWS에서 실행하는 애플리케이션을 실시간으로 모니터링 할 수 있는 서비스
- "지표"를 추적하고 사용하여 모니터링하는 방식으로 작동
- [지표=리소스에 연결된 변수]
  - EC2 인스턴스의 CPU 사용률
- "경보"라는 기능으로 알림 구현 / 사용자가 지표에 대한 임계값을 설정
- 경보를 생성하고 작업을 트리거할 수 있다. -> SNS
- 이러한 모든 지표는 대시보드 기능을 통해 하나의 창에 집계 가능!
- "중앙 위치에서 모든 지표에 액세스 가능"
- 따라서, 애플리케이션, 인프라, 서비스 간에도 가시성 확보 가능.\
- MTTR 단축 TCO 개선
- 앱,리소스 최적화를 위한 인사이트 확보

 

## 2. AWS CloudTrail

계정에 대한 API 호출을 기록합니다. 기록되는 정보에는 API 호출자 ID, API 호출 시간, API 호출자의 소스 IP 주소 등이 포함됩니다. CloudTrail은 누군가 남긴 이동 경로(또는 작업 로그)의 ‘추적’으로 생각할 수 있습니다.



### **AWS CloudTrail 이벤트**

- 포괄적인 API 감사 도구
- EC2 인스턴스를 시작하든, DynamoDB 테이블에 행을 추가하든 사용자의 권한을 변경하든 어떤 요청이든 관계없이 AWS에 대한 모든 요청은 CloudTrail 엔진에 기록 됨.
- 계정에 대한 API 호출을 기록함. (ID / 호출시간 / 소스 IP 주소 등)
- 누군가 남긴 이동 경로(작업로그)의 "추적"
  - API 호출을 사용하여 AWS 리소스를 프로비저닝, 관리 및 구성할 수 있다.
- 이벤트는 일반적으로 API 호출 후 15분 이내에 CloudTrail에서 업데이트됨.

### **CloudTrail Insights**

CloudTrail에서 [**CloudTrail Insights**](https://docs.aws.amazon.com/awscloudtrail/latest/userguide/logging-insights-events-with-cloudtrail.html)를 활성화할 수도 있습니다. 이 옵션 기능을 사용하면 CloudTrail이 AWS 계정에서 비정상적인 API 활동을 자동으로 감지할 수 있습니다. 





## 3. AWS Trusted Advisor

- AWS 환경을 검사하고 AWS 모범 사례에 따라 실시간 권장 사항을 제시하는 웹 서비스이다.

**비용 최적화, 성능, 보안, 내결함성, 서비스 한도**라는 5개 범주에서 결과를 AWS 모범 사례와 비교한다.

- 녹색 체크 표시는 **문제가 감지되지 않은** 항목 수
- 주황색 삼각형은 권장 **조사** 항목 수
- 빨간색 원은 권장 **조치** 수



### Amazon GuardDuty

- Amazon GuardDuty는 AWS 환경 및 리소스에 대한 지능형 위협 탐지 기능을 제공하는 서비스
- 이 서비스는 AWS 환경 내의 네트워크 활동 및 계정 동작을 지속적으로 모니터링하여 위협을 식별





















































