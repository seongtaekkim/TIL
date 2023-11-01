# 05. storage and database



- 스토리지 및 데이터베이스의 기본 개념을 요약할 수 있습니다.
- Amazon Elastic Block Store(Amazon EBS)의 이점을 설명할 수 있습니다.
- Amazon Simple Storage Solution(Amazon S3)의 이점을 설명할 수 있습니다.
- Amazon Elastic File System(Amazon EFS)의 이점을 설명할 수 있습니다.
- 다양한 스토리지 솔루션을 요약할 수 있습니다.
- Amazon Relational Database Service(RDS)의 이점을 설명할 수 있습니다.
- Amazon DynamoDB의 이점을 설명할 수 있습니다.
- 다양한 데이터베이스 서비스를 요약할 수 있습니다.



## EBS



**인스턴스 스토어**



## **Amazon EBS 스냅샷**







## S3

## **Amazon Simple Storage Service(Amazon S3)**



## **Amazon S3 스토리지 클래스**





### EBS vs S3

### EBS

- 최대 16테라, EC2 종료 후 생존, SSD, HDD옵션,  블락단위
- 큰 파일의 잦은 수정,추가가 잇으면 유리
  - 저장할 때, 블락단위로 가능.



### S3

- 웹 지원, 리전 별 분산, 비용절감효과, 서버리스
- 무제한 스토리지, 최대 5TB 개별객체, 한번쓰기 여러번읽기 (WORM), 내구성 99.99999 (백업전략 필요없음)
- 전체 객체를 읽고, 수정,추가가 거의 없으면 유리.
  - 저장할 때, 무조건 5테라 단위로 저장해야 함.



## Amazon Elastic File System(Amazon EFS)

**파일 스토리지**에서는 여러 클라이언트(예: 사용자, 애플리케이션, 서버 등)가 **공유 파일 폴더**에 저장된 데이터에 액세스할 수 있습니다. 이 접근 방식에서는 스토리지 서버가 블록 스토리지를 로컬 파일 시스템과 함께 사용하여 파일을 구성합니다. 클라이언트는 **파일 경로**를 통해 데이터에 액세스합니다.

블록 스토리지 및 객체 스토리지와 비교하면, **파일 스토리지는 많은 수의 서비스 및 리소스가 동시에 동일한 데이터에 액세스해야 하는 사용 사례에 이상적입니다.**





### EBS vs EFS

Amazon EBS

- Amazon EBS 볼륨은 **단일** 가용 영역에 데이터를 저장합니다. 
- Amazon EC2 인스턴스를 EBS 볼륨에 연결하려면 Amazon EC2 인스턴스와 EBS 볼륨 모두 동일한 가용 영역에 상주해야 합니다.
- 볼륨이 자동 확장되지 않는다.



Amazon EFS는 리전별 서비스입니다. 이 서비스는 **여러** 가용 영역에 걸쳐 데이터를 저장합니다. 

- 중복 스토리지를 사용하면 파일 시스템이 위치한 리전의 모든 가용 영역에서 동시에 데이터에 액세스할 수 있습니다. 또한 온프레미스 서버는 AWS Direct Connect를 사용하여 Amazon EFS에 액세스할 수 있습니다.
- 볼륨이 자동 확장된다.
- EC2 데이터를 모두 입력 가능함.



## Amazon Relational Database Service(Amazon RDS)

[**Amazon Relational Database Service(Amazon RDS)**](https://aws.amazon.com/rds/)는 AWS 클라우드에서 관계형 데이터베이스를 실행할 수 있는 서비스입니다.

- Amazon Aurora
- PostgreSQL
- MySQL
- MariaDB
- Oracle Database
- Microsoft SQL Server

### **Amazon Aurora**

[**Amazon Aurora**](https://aws.amazon.com/rds/aurora/)는 엔터프라이즈급 관계형 데이터베이스입니다. 이 데이터베이스는 MySQL 및 PostgreSQL 관계형 데이터베이스와 호환됩니다. 표준 MySQL 데이터베이스보다 최대 5배 빠르며 표준 PostgreSQL 데이터베이스보다 최대 3배 빠릅니다.

워크로드에 고가용성이 필요한 경우 Amazon Aurora를 고려하십시오. 이 데이터베이스는 **6개**의 데이터 복사본을 3개의 가용 영역에 복제하고 지속적으로 Amazon S3에 데이터를 백업합니다.





## Amazon DynamoDB

**비관계형 데이터베이스**에서는 테이블을 생성합니다. 테이블은 데이터를 저장하고 쿼리할 수 있는 장소입니다.



비관계형 데이터베이스는 행과 열이 아닌 구조를 사용하여 데이터를 구성하기 때문에 ‘NoSQL 데이터베이스’라고도 합니다. 비관계형 데이터베이스의 구조적 접근 방식 중 한 유형은 키-값 페어입니다. 키-값 페어에서는 데이터가 항목(키)으로 구성되고 항목은 속성(값)을 갖습니다. 속성을 데이터의 여러 기능으로 생각할 수 있습니다.



[**Amazon DynamoDB**](https://aws.amazon.com/dynamodb/)는 키-값 데이터베이스 서비스입니다. 모든 규모에서 한 자릿수 밀리초의 성능을 제공합니다.



### 서버리스

- DynamoDB는 서버를 사용하지 않으므로 서버를 프로비저닝, 패치 적용 또는 관리할 필요가 없습니다. 
- 또한 소프트웨어를 설치, 유지 관리, 운영할 필요도 없습니다.

### 자동조정

- 데이터베이스 크기가 축소 또는 확장되면 DynamoDB는 용량 변화에 맞춰 자동으로 크기를 조정하면서도 일관된 성능을 유지합니다. 
- 따라서 크기를 조정하는 동안에도 고성능이 필요한 사용 사례에 적합한 선택입니다.





##  Amazon Redshift



[**Amazon Redshift**](https://aws.amazon.com/redshift)는 빅 데이터 분석에 사용할 수 있는 데이터 웨어하우징 서비스입니다. 이 서비스는 여러 원본에서 데이터를 수집하여 데이터 간의 관계 및 추세를 파악하는 데 도움이 되는 기능을 제공합니다.

운영분석이 아닌 기록분석 (과거데이터)을 할대 데이터 웨어하우징이 유리하다.

- RDB 에 요구사항이 복잡하고 많을수록 부하가 심해지기 때문에 웨어하우스를 이용하는 것임.





## AWS Database Migration Service



### 개발 및 테스트 데이터베이스 마이그레이션

- 프로덕션 사용자에게 영향을 주지 않고 개발자가 프로덕션 데이터에서 애플리케이션을 테스트할 수 있도록 지원

### 데이터베이스 통합

- 여러 데이터베이스를 단일 데이터베이스로 결합

### 연속 복제

- 일회성 마이그레이션을 수행하는 것이 아니라 데이터의 진행 중 복제본을 다른 대상 원본으로 전송





## 추가 데이터베이스 서비스



### Amazon DocumentDB

- MongoDB 워크로드를 지원하는 문서 데이터베이스 서비스입니다. (MongoDB는 문서 데이터베이스 프로그램입니다.)

### Amazon Neptune

- 그래프 데이터베이스 서비스입니다. 
- Amazon Neptune을 사용하여 추천 엔진, 사기 탐지, 지식 그래프와 같이 고도로 연결된 데이터 세트로 작동하는 애플리케이션을 빌드하고 실행할 수 있습니다

### Amazon Quantum Ledger Database(Amazon QLDB)

- 원장 데이터베이스 서비스입니다. 
- Amazon QLDB를 사용하여 애플리케이션 데이터에 발생한 모든 변경 사항의 전체 기록을 검토할 수 있습니다.

### Amazon Managed Blockchain

- 오픈 소스 프레임워크를 사용하여 블록체인 네트워크를 생성하고 관리하는 데 사용할 수 있는 서비스입니다. 
- 블록체인은 여러 당사자가 중앙 기관 없이 거래를 실행하고 데이터를 공유할 수 있는 분산형 원장 시스템입니다.

### Amazon ElastiCache

- 자주 사용되는 요청의 읽기 시간을 향상시키기 위해 데이터베이스 위에 캐싱 계층을 추가하는 서비스입니다. 
- 이 서비스는 두 가지 데이터 저장소 Redis 및 Memcached를 지원합니다.

### Amazon DynamoDB Accelerator

- DynamoDB용 인 메모리 캐시입니다. 
- 응답 시간을 한 자릿수 밀리초에서 마이크로초까지 향상시킬 수 있습니다.















































