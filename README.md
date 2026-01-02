# Hexagonal Architecture Example

실제 서비스 환경에서 헥사고날 아키텍처(Hexagonal Architecture)를 **어디까지 적용하는 것이 합리적인지** 고민하며 구현한 예제 프로젝트입니다.

> 💡 **자세한 설계 원칙과 선택 이유는 아래 블로그 글을 참고해주세요.**  
> [헥사고날 아키텍처, 어디까지 적용해야 할까](https://yearnlune.github.io/general/hexagonal-architecture)

## 📋 목차

- [개요](#개요)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [아키텍처 개요](#아키텍처-개요)
- [도메인 구조](#도메인-구조)

## 📖 개요

이 프로젝트는 헥사고날 아키텍처를 완전하게 구현하기보다는, 실제 서비스 환경에서 **어디까지 적용하는 것이 합리적인지** 고민하며 선택한 구조와 설계 원칙을 보여주는 예제입니다.

### 핵심 원칙

1. **도메인 규칙이 기술 변화에 휘둘리지 않도록 보호**
2. **변경 가능성이 낮은 지점에는 불필요한 추상화를 두지 않음**
3. **구조 자체보다, 팀이 이해하고 유지할 수 있는 설명 가능한 구조**

## 🛠 기술 스택

- **Language**: Kotlin 2.1.20
- **Framework**: Spring Boot 3.5.9
- **Java**: 21
- **Database**: Spring Data JPA
- **Cache**: Spring Data Redis
- **HTTP Client**: Spring Cloud OpenFeign
- **Build Tool**: Gradle (Kotlin DSL)
- **Code Style**: ktlint

## 📁 프로젝트 구조

```
src/main/kotlin/io/github/yearnlune/hexagonal/
├── global/              # 공통 설정 (Config, Entity, Exception)
├── order/               # 주문 도메인
│   ├── presentation/    # REST Controller, DTO, Mapper
│   ├── application/     # UseCase, Command/Query, Result
│   ├── domain/          # Order, OrderItem 엔티티 및 도메인 로직
│   └── infrastructure/  # Repository 구현
├── payment/             # 결제 도메인
│   ├── presentation/
│   ├── application/
│   ├── domain/          # payment/, refund/ 서브 도메인
│   └── infrastructure/  # Repository, 외부 API Client (Toss)
└── product/             # 상품 도메인
    ├── presentation/
    ├── application/     # UseCase, CommandService, QueryService
    ├── domain/
    └── infrastructure/
```

## 🏗 아키텍처 개요

각 도메인은 다음 4개의 레이어로 구성됩니다:

```
{domain}/
├── presentation/    # 외부 인터페이스 (REST Controller, DTO)
├── application/     # 유스케이스, 비즈니스 플로우 조율
├── domain/          # 핵심 비즈니스 로직, 엔티티, 도메인 서비스
└── infrastructure/  # 기술적 구현 (DB, Repository, 외부 API)
```

### 의존성 방향

- `domain` 레이어는 어떤 레이어에도 의존하지 않음
- `application`은 도메인을 조합해 유스케이스를 완성
- `presentation`은 application만 호출
- `infrastructure`는 기술적 세부 사항을 담당

### 도메인 간 통신: CQRS 패턴

다른 도메인의 정보가 필요할 때는 **QueryService / CommandService만 허용**합니다.

```
Controller → UseCase → (같은 도메인 다른 UseCase / 다른 도메인 QueryService/CommandService)
```

- ❌ 타 도메인의 UseCase 직접 호출 금지
- ✅ QueryService: Snapshot/View/Summary 반환
- ✅ CommandService: Command 입력, Result/id/Unit 반환

## 🎯 도메인 구조

### Order (주문)

- 주문 생성, 조회, 취소
- 배송 상태 관리
- Product 도메인의 QueryService/CommandService를 통해 상품 정보 조회 및 재고 관리

### Payment (결제)

- 결제 생성, 승인, 취소
- 환불 처리 (Refund 서브 도메인)
- 외부 결제 API (Toss Payments) 연동
- PaymentHistory를 통한 결제 이력 관리

### Product (상품)

- 상품 생성, 조회, 수정
- 재고 관리 (증가/감소)
- ProductSnapshot을 통한 스냅샷 조회 (외부 도메인용)

## 📚 참고 자료

- [헥사고날 아키텍처, 어디까지 적용해야 할까](https://yearnlune.github.io/general/hexagonal-architecture) - 본 프로젝트의 설계 원칙과 선택 이유에 대한 상세한 설명
