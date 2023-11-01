# 08. fee and support



- AWS 요금 및 지원 모델을 설명할 수 있습니다.
- AWS 프리 티어를 설명할 수 있습니다.
- AWS Organizations 및 통합 결제의 주요 이점을 설명할 수 있습니다.
- AWS 예산의 이점을 설명할 수 있습니다.
- AWS Cost Explorer의 이점을 설명할 수 있습니다.
- AWS 요금 계산기의 주요 이점을 설명할 수 있습니다.
- 다양한 AWS Support 플랜을 구별할 수 있습니다.
- AWS Marketplace의 이점을 설명할 수 있습니다.





## **AWS 프리 티어**

지정된 기간 동안 비용을 신경 쓸 필요 없이 특정 서비스를 사용할 수 있습니다. 

- 상시 무료
- 12개월 무료
- 평가판





## AWS 요금 개념









## 결제 대시보드

[**AWS 결제 및 비용 관리 대시보드**](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/billing-what-is.html)를 사용하여 AWS 청구서를 결제하고, 사용량을 모니터링하고, 비용을 분석 및 제어할 수 있습니다.

- 이번 달 누계 금액을 지난 달과 비교하고 현재 사용량을 기준으로 내달 사용량을 예측합니다.
- 서비스별 월 누계 지출을 확인합니다.
- 서비스별 프리 티어 사용량을 확인합니다.
- Cost Explorer에 액세스하여 예산을 생성합니다.
- Savings Plans를 구매하고 관리합니다.
- [AWS 비용 및 사용 보고서](https://docs.aws.amazon.com/cur/latest/userguide/what-is-cur.html)를 게시합니다.





## **통합 결제**

이전 모듈에서는 중앙 위치에서 여러 AWS 계정을 관리할 수 있는 서비스인 AWS Organizations에 대해 알아보았습니다. 또한 AWS Organizations는 **[통합 결제](https://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/consolidated-billing.html)** 옵션을 제공합니다. 

AWS Organizations의 통합 결제 기능을 사용하면 조직의 모든 AWS 계정에 대한 단일 청구서를 받을 수 있습니다. 결제를 통합하면 조직에 있는 모든 연결 계정의 결합된 비용을 손쉽게 추적할 수 있습니다. 기본적으로 조직에 허용되는 최대 계정 수는 4개이지만, 필요한 경우 AWS Support에 문의하여 할당량을 늘릴 수 있습니다.

월별 청구서에서 각 계정에서 발생한 항목별 요금을 검토할 수 있습니다. 그러므로 단일 월별 청구서의 편의성은 유지하면서도 조직의 계정에 대한 투명성을 높일 수 있습니다.

통합 결제의 또 다른 이점은 조직의 계정 전체에서 대량 할인 요금, **Savings Plans 및 예약 인스턴스를 공유할 수 있다는 것입니다**. 예를 들어 한 계정에서 월별 사용량이 부족하여 할인 가격을 적용받지 못할 수 있습니다. 그러나 여러 계정을 결합하는 경우 사용량이 집계되므로 혜택이 조직의 모든 계정에 적용될 수 있습니다.



# AWS 예산

[**AWS 예산**](https://aws.amazon.com/aws-cost-management/aws-budgets)에서 예산을 생성하여 서비스 사용, 서비스 비용 및 인스턴스 예약을 계획할 수 있습니다.

AWS 예산에서는 정보가 하루에 세 번 업데이트됩니다. 그러므로 사용량이 예산 금액 또는 AWS 프리 티어 한도에 얼마나 근접한지 정확하게 파악할 수 있습니다.

AWS 예산에서 사용량이 예산 금액을 초과하거나 초과할 것으로 예상되면 알려주는 사용자 지정 알림을 설정할 수도 있습니다.



# AWS Cost Explorer

[**AWS Cost Explorer**](https://aws.amazon.com/aws-cost-management/aws-cost-explorer/)는 시간 경과에 따라 AWS 비용 및 사용량을 시각화, 이해, 관리할 수 있는 도구입니다.

AWS Cost Explorer에는 발생 비용 기준 상위 5개 AWS 서비스의 비용 및 사용량에 대한 기본 보고서가 포함되어 있습니다. 사용자 지정 필터 및 그룹을 적용하여 데이터를 분석할 수 있습니다. 예를 들어 시간별로 리소스 사용량을 확인할 수 있습니다

# AWS Support 플랜

AWS는 문제를 해결하고 비용을 절감하며 AWS 서비스를 효율적으로 사용하는 데 도움이 되는 네 가지 [**Support 플랜**](https://aws.amazon.com/premiumsupport/plans/)을 제공합니다. 

회사의 요구 사항을 충족하기 위해 다음 Support 플랜 중에서 선택할 수 있습니다. 

- Basic
- Developer
- Business
- Enterprise



### **Basic Support**



### Developer Support

- 모범 사례 지침
- 클라이언트 측 진단 도구
- AWS 제품, 기능 및 서비스를 함께 사용하는 방법에 대한 지침으로 구성된 빌딩 블록 아키텍처 지원

### Business Support

- 특정 요구 사항을 가장 잘 지원할 수 있는 AWS 제품, 기능 및 서비스를 식별하기 위한 사용 사례 지침
- 모든 **AWS Trusted Advisor** 검사
- 일반적인 운영 체제 및 애플리케이션 스택 구성 요소와 같은 타사 소프트웨어에 대한 제한된 지원
- SLA 15분
- 엔지니어에 전화 가능

### Enterprise Support

- 회사의 특정 사용 사례 및 애플리케이션을 지원하기 위한 컨설팅 관계인 애플리케이션 아키텍처 지침
- 인프라 이벤트 관리 지원: 회사가 사용 사례를 더 잘 이해할 수 있도록 돕는 AWS Support와의 단기 계약입니다. 또한 회사에 아키텍처 및 확장 지침도 제공합니다.
- 기술 계정 관리자
- SLA 15분
- 

## **기술 지원 관리자(TAM)**

Enterprise Support 플랜에는 **기술 지원 관리자(TAM)**에 대한 액세스가 포함됩니다.

회사에 Enterprise Support 플랜이 있는 경우 TAM이 AWS 측 주 연락 창구입니다. 여러분이 애플리케이션을 계획, 배포, 최적화할 때 TAM이 지속적으로 커뮤니케이션하면서 권장 사항, 아키텍처 검토를 제공합니다. 

well-Architected 제공





## AWS Marketplace

Independent Software Vendor(ISV)의 소프트웨어 리스팅 수천 개가 포함된 디지털 카탈로그입니다. AWS Marketplace를 이용하여 AWS에서 실행되는 소프트웨어를 검색하고 평가하고 구매할 수 있습니다. 

AWS Marketplace의 각 리스팅에 대해 요금 옵션, 사용 가능한 지원 및 다른 AWS 고객의 리뷰 등 자세한 정보에 액세스할 수 있습니다.



































































