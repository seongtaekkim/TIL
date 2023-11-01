# 06. Security



- 공동 책임 모델의 이점을 설명할 수 있습니다.
- Multi-Factor Authentication(MFA)을 설명할 수 있습니다.
- AWS Identity and Access Management(IAM) 보안 수준을 구별할 수 있습니다.
- AWS Organizations의 주요 이점을 설명할 수 있습니다.
- 보안 정책을 기본 수준에서 설명할 수 있습니다.
- AWS를 사용한 규정 준수의 이점을 요약할 수 있습니다.
- 추가 AWS 보안 서비스를 기본 수준에서 설명할 수 있습니다.





## **AWS 공동 책임 모델**

이 과정 전반에서 AWS 클라우드에서 생성할 수 있는 다양한 리소스에 대해 배웠습니다. 이러한 리소스에는 Amazon EC2 인스턴스, Amazon S3 버킷, Amazon RDS 데이터베이스가 포함됩니다. 이러한 리소스를 안전하게 유지할 책임은 누구에게 있습니까? 고객입니까, AWS입니까?

정답은 둘 다입니다. 이유는 AWS 환경을 단일 객체로 취급하지 않기 때문입니다. 오히려 환경을 서로를 기반으로 빌드되는 부분의 모음으로 취급합니다. AWS는 사용자 환경의 일부분을 책임지고 고객은 다른 부분을 책임집니다. 이 개념은 [**공동 책임 모델**](https://aws.amazon.com/compliance/shared-responsibility-model/)로 알려져 있습니다.

공동 책임 모델은 고객 책임(일반적으로 ‘클라우드 내부의 보안’이라고 함)과 AWS 책임(일반적으로 ‘클라우드 자체의 보안’이라고 함)으로 나뉩니다.



![스크린샷 2023-11-01 오후 4.46.38](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-11-01 오후 4.46.38.png)



![스크린샷 2023-11-01 오후 4.51.10](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-11-01 오후 4.51.10.png)





### 고객

고객이 수행하는 보안 단계는 사용하는 서비스, 시스템의 복잡성, 회사별 운영 및 보안 요구 사항과 같은 요소에 따라 달라집니다. 이러한 단계에는 Amazon EC2 인스턴스에서 실행할 운영 체제를 선택, 구성 및 패치 적용하는 단계, 보안 그룹을 구성하는 단계, 사용자 계정을 관리하는 단계가 포함됩니다. 

### AWS

AWS는 클라우드 **자체의** 보안을 책임집니다.

AWS는 클라우드 자체의 보안, 특히 리소스를 호스팅하는 물리적 인프라를 관리합니다. 여기에는 다음이 포함됩니다.

- 데이터 센터의 물리적 보안
- 하드웨어 및 소프트웨어
- 인프라
- 네트워크 인프라
- 가상화 인프라





## 사용자 권한 및 액세스



### **AWS Identity and Access Management(IAM)**

- [**IAM**](https://aws.amazon.com/iam/)를 사용하면 AWS 서비스와 리소스에 대한 액세스를 안전하게 관리할 수 있습니다.  

- IAM 사용자, 그룹 및 역할
- IAM 정책
- Multi-Factor Authentication



### **AWS 계정 루트 사용자**

- AWS 계정을 처음 만들면 [**루트 사용자**](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_root-user.html)라고 하는 자격 증명으로 시작합니다. 



### **IAM 사용자**

**IAM 사용자**는 사용자가 AWS에서 생성하는 자격 증명입니다. 이 사용자는 AWS 서비스 및 리소스와 상호 작용하는 사람 또는 애플리케이션을 나타냅니다. 이 사용자는 이름과 자격 증명으로 구성됩니다.



### **IAM 정책**

**IAM** **정책**은 AWS 서비스 및 리소스에 대한 권한을 허용하거나 거부하는 문서입니다.  

IAM 정책을 사용하면 사용자가 리소스에 액세스할 수 있는 수준을 사용자 지정할 수 있습니다. 예를 들어 사용자가 AWS 계정 내의 모든 Amazon S3 버킷에 액세스하거나 특정 버킷에만 액세스하도록 허용할 수 있습니다.



### **IAM 그룹**

IAM 그룹은 IAM 사용자의 모음입니다. 그룹에 IAM 정책을 할당하면 해당 그룹의 모든 사용자에게 정책에 지정된 권한이 부여됩니다.

다음은 커피숍에서 이 정책 할당이 작동하는 방식의 예입니다. 점주는 계산원마다 권한을 할당하는 대신 ‘계산원’ IAM 그룹을 생성할 수 있습니다. 그런 다음 이 그룹에 IAM 사용자를 추가하고 그룹 수준에서 권한을 연결할 수 있습니다. 





### **IAM 역할**

IAM 역할은 임시로 권한에 액세스하기 위해 수임할 수 있는 자격 증명입니다.  

IAM 사용자, 애플리케이션 또는 서비스가 IAM 역할을 수임하려면 먼저 해당 역할로 전환할 수 있는 권한을 부여받아야 합니다. IAM 역할을 수임한다는 것은 이전 역할에 지정된 모든 권한을 포기하고 새 역할에 지정된 권한을 수임하는 것입니다. 



### **Multi-Factor Authentication**

신원을 확인하기 위해 여러 가지 정보를 제공하도록 요구하는 웹 사이트에 로그인한 적이 있습니까? 암호를 입력한 다음 휴대폰으로 전송된 난수 코드와 같은 두 번째 인증 형식을 제공해야 할 수도 있습니다. 이것이 [**Multi-Factor Authentication**](https://aws.amazon.com/iam/features/mfa/)의 예입니다.





## AWS Organizations

회사에 여러 AWS 계정이 있다고 가정해 보겠습니다. [**AWS Organizations**](https://aws.amazon.com/organizations)를 사용하여 중앙 위치에서 여러 AWS 계정을 통합하고 관리할 수 있습니다.

조직을 생성하면 AWS Organizations가 조직의 모든 계정에 대한 상위 컨테이너 **루트**를 자동으로 생성합니다. 

AWS Organizations에서는 [**서비스 제어 정책(SCP)**](https://docs.aws.amazon.com/organizations/latest/userguide/orgs_manage_policies_scps.html)을 사용하여 조직의 계정에 대한 권한을 중앙에서 제어할 수 있습니다. SCP를 사용하면 각 계정의 사용자 및 역할이 액세스할 수 있는 AWS 서비스, 리소스 및 개별 API 작업을 제한할 수 있습니다.



### SCP

AWS Organizations에서 서비스 제어 정책(SCP)을 조직 루트, 개별 멤버 계정 또는 OU에 적용할 수 있습니다. SCP는 AWS 계정 루트 사용자를 포함하여 계정 내의 모든 IAM 사용자, 그룹 및 역할에 영향을 줍니다.



### **조직 단위**

AWS Organizations에서는 계정을 조직 단위(OU)로 그룹화하여 비슷한 비즈니스 또는 보안 요구 사항이 있는 계정을 손쉽게 관리할 수 있습니다. OU에 정책을 적용하면 OU의 모든 계정이 정책에 지정된 권한을 자동으로 상속합니다.  

개별 계정을 OU로 구성하면 특정 보안 요구 사항이 있는 워크로드 또는 애플리케이션을 보다 간편하게 격리할 수 있습니다. 예를 들어 회사에 특정 규정 요구 사항을 충족하는 AWS 서비스에만 액세스할 수 있는 계정이 있다면, 이러한 계정을 한 OU에 배치할 수 있습니다. 그런 다음 규제 요구 사항을 충족하지 않는 다른 모든 AWS 서비스에 대한 액세스를 차단하는 정책을 해당 OU에 연결할 수 있습니다.



## 규정 준수

### **AWS Artifact**

회사가 속한 업종에 따라 특정 표준을 준수해야 할 수 있습니다. 감사 또는 검사는 회사가 이러한 표준을 충족했는지 확인하는 절차입니다.

 AWS 보안 및 규정 준수 보고서 및 일부 온라인 계약에 대한 온디맨드 액세스를 제공하는 서비스입니다. AWS Artifact는 AWS Artifact Agreements 및 AWS Artifact Reports의 두 가지 기본 섹션으로 구성됩니다.



### AWS Artifact Agreements

- 회사에서 AWS 서비스 전체에서 특정 유형의 정보를 사용하기 위해 AWS와 계약을 체결해야 한다고 가정해 보겠습니다. **AWS Artifact Agreements**를 통해 이를 수행할 수 있습니다. 
- AWS Artifact Agreements에서 개별 계정 및 AWS Organizations 내 모든 계정에 대한 계약을 검토, 수락 및 관리할 수 있습니다. HIPAA(미국 건강 보험 양도 및 책임에 관한 법)와 같은 특정 규정의 적용을 받는 고객의 요구 사항을 해결하기 위한 다양한 유형의 계약이 제공됩니다.

### AWS Artifact Reports

- 다음으로, 회사의 개발 팀원 한 명이 애플리케이션을 빌드하는 도중 특정 규제 표준을 준수하기 위한 책임에 대한 추가 정보가 필요하다고 가정해 보겠습니다. **AWS Artifact Reports**에서 이 정보에 액세스하도록 조언할 수 있습니다.
- AWS Artifact Reports는 외부 감사 기관이 작성한 규정 준수 보고서를 제공합니다. 이러한 감사 기관에서 AWS가 다양한 글로벌, 지역별, 산업별 보안 표준 및 규정을 준수했음을 검증했습니다. AWS Artifact Reports는 릴리스된 최신 보고서가 반영되어 항상 최신 상태로 유지됩니다. 감사 또는 규제 기관에 AWS 보안 제어 항목의 증거로서 AWS 감사 아티팩트를 제공하면 됩니다.





## 서비스 거부 공격



### **서비스 거부 공격**

**서비스 거부(DoS) 공격**은 사용자들이 웹 사이트 또는 애플리케이션을 이용할 수 없게 만들려는 의도적인 시도입니다.

### **분산 서비스 거부(DDoS) 공격**

이제, 장난꾸러기가 친구의 도움을 받게 되었다고 가정해 보겠습니다. 





### **AWS Shield**

AWS Shield는 DDoS 공격으로부터 애플리케이션을 보호하는 서비스입니다. AWS Shield는 두 가지 보호 수준인 Standard 및 Advanced를 제공합니다.

#### AWS Shield Standard

- **AWS Shield Standard**는 모든 AWS 고객을 자동으로 보호하는 무료 서비스입니다. AWS 리소스를 가장 자주 발생하는 일반적인 DDoS 공격으로부터 보호합니다. 
- 네트워크 트래픽이 애플리케이션으로 들어오면 AWS Shield Standard는 다양한 분석 기법을 사용하여 실시간으로 악성 트래픽을 탐지하고 자동으로 완화합니다. 

#### AWS Shield Advanced

- **AWS Shield Advanced**는 상세한 공격 진단 및 정교한 DDoS 공격을 탐지하고 완화할 수 있는 기능을 제공하는 유료 서비스입니다. 
- Amazon CloudFront, Amazon Route 53, Elastic Load Balancing과 같은 다른 서비스와도 통합됩니다. 또한 복잡한 DDoS 공격을 완화하기 위한 사용자 지정 규칙을 작성하여 AWS Shield를 AWS WAF와 통합할 수 있습니다.





## 추가 보안 서비스



### **AWS Key Management Service(AWS KMS)**

[**AWS Key Management Service(AWS KMS)**](https://aws.amazon.com/kms)를 사용하면 **암호화 키**를 사용하여 암호화 작업을 수행할 수 있습니다. 암호화 키는 데이터 잠금(암호화) 및 잠금 해제(암호 해독)에 사용되는 임의의 숫자 문자열입니다. AWS KMS를 사용하여 암호화 키를 생성, 관리 및 사용할 수 있습니다. 또한 광범위한 서비스 및 애플리케이션에서 키 사용을 제어할 수 있습니다.



### **AWS WAF**

[**AWS WAF**](https://aws.amazon.com/waf)는 웹 애플리케이션으로 들어오는 네트워크 요청을 모니터링할 수 있는 웹 애플리케이션 방화벽입니다. 

AWS WAF는 Amazon CloudFront 및 Application Load Balancer와 함께 작동합니다. 이전 모듈에서 배운 네트워크 액세스 제어 목록을 기억해 보십시오. AWS WAF는 비슷한 방식으로 작동하여 트래픽을 차단하거나 허용합니다. 그러나 AWS 리소스를 보호하기 위해 [**웹 ACL(액세스 제어 목록)**](https://docs.aws.amazon.com/waf/latest/developerguide/web-acl.html)을 사용합니다. 



### **Amazon Inspector**

Amazon Inspector는 자동화된 보안 평가를 실행하여 애플리케이션의 보안 및 규정 준수를 개선할 수 있는 서비스입니다. 이 서비스는 Amazon EC2 인스턴스에 대한 오픈 액세스, 취약한 소프트웨어 버전 설치와 같은 보안 모범 사례 위반 및 보안 취약성을 애플리케이션에서 검사합니다. 

Amazon Inspector는 평가를 수행한 후에 보안 탐지 결과 목록을 제공합니다. 이 목록은 심각도 수준에 따라 우선 순위가 결정되고 각 보안 문제에 대한 자세한 설명 및 권장 해결 방법이 포함됩니다. 그러나 AWS는 제공된 권장 사항으로 모든 잠재적 보안 문제가 해결됨을 보장하지 않습니다. 공동 책임 모델에 따라 고객은 AWS 서비스에서 실행되는 애플리케이션, 프로세스 및 도구의 보안에 대한 책임이 있습니다.

- 네트워크 구성 연결성 부분
- Amazon 에이전트
- 보안 평가 서비스



### **Amazon GuardDuty**

[**Amazon GuardDuty**](https://aws.amazon.com/guardduty)는 AWS 인프라 및 리소스에 대한 지능형 위협 탐지 기능을 제공하는 서비스입니다. 이 서비스는 AWS 환경 내의 네트워크 활동 및 계정 동작을 지속적으로 모니터링하여 위협을 식별합니다.

































