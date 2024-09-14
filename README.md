# Smartpay

## 목차
1. [프로젝트 구조](#1-프로젝트-구조)
2. [ERD](#2-erd)
3. [프로젝트 설명](#3-프로젝트-설명)
4. [결제 프로세스](#4-결제-프로세스)
5. [AI 프로세스](#5-ai-프로세스)

## 1. 프로젝트 구조

### (1) 폴더 구조
```
smartpay
 ┣ config
 ┃ ┣ ChatGptClientConfig.java
 ┃ ┣ RedisConfig.java
 ┃ ┣ SecurityConfig.java
 ┃ ┣ WebClientConfig.java
 ┃ ┣ WebConfig.java
 ┃ ┗ WebSocketConfig.java
 ┣ controller
 ┃ ┣ BinController.java
 ┃ ┣ CardController.java
 ┃ ┣ ChatGptController.java
 ┃ ┣ MemberController.java
 ┃ ┣ PaymentController.java
 ┃ ┣ QrCodeController.java
 ┃ ┣ SellerController.java
 ┃ ┗ SmsController.java
 ┣ dto
 ┃ ┣ BinTableDTO.java
 ┃ ┣ CardDTO.java
 ┃ ┣ CardInfoDTO.java
 ┃ ┣ CardRecommendDTO.java
 ┃ ┣ HistoryDTO.java
 ┃ ┣ MemberDTO.java
 ┃ ┣ MyStaticDTO.java
 ┃ ┣ OrderCancelDTO.java
 ┃ ┣ OrderDTO.java
 ┃ ┣ PageRequestDTO.java
 ┃ ┣ PageResultDTO.java
 ┃ ┣ PayDTO.java
 ┃ ┣ PayInfoDTO.java
 ┃ ┗ SellerDTO.java
 ┣ entity
 ┃ ┣ BaseEntity.java
 ┃ ┣ BinTable.java
 ┃ ┣ Card.java
 ┃ ┣ CardBenefits.java
 ┃ ┣ CardInfo.java
 ┃ ┣ History.java
 ┃ ┣ Member.java
 ┃ ┣ MemberRole.java
 ┃ ┣ Order.java
 ┃ ┗ PayInfo.java
 ┣ filter
 ┃ ┗ CORSFilter.java
 ┣ repository
 ┃ ┣ BinTableRepository.java
 ┃ ┣ CardBenefitsRepository.java
 ┃ ┣ CardInfoRepository.java
 ┃ ┣ CardRepository.java
 ┃ ┣ MemberRepository.java
 ┃ ┣ OrderRepository.java
 ┃ ┣ PayHistoryRepository.java
 ┃ ┣ PayInfoRepository.java
 ┃ ┣ PayInfoRepositoryCustom.java
 ┃ ┗ PayInfoRepositoryCustomImpl.java
 ┣ security
 ┃ ┣ dto
 ┃ ┃ ┣ MemberAuthDTO.java
 ┃ ┃ ┗ MemberRegisterDTO.java
 ┃ ┣ filter
 ┃ ┃ ┣ CustomJsonUsernamePasswordAuthenticationFilter.java
 ┃ ┃ ┗ JwtAuthenticationProcessingFilter.java
 ┃ ┣ handler
 ┃ ┃ ┣ LoginFailureHandler.java
 ┃ ┃ ┗ LoginSuccessHandler.java
 ┃ ┣ oauth
 ┃ ┃ ┣ handler
 ┃ ┃ ┃ ┣ OAuth2LoginFailureHandler.java
 ┃ ┃ ┃ ┗ OAuth2LoginSuccessHandler.java
 ┃ ┃ ┣ service
 ┃ ┃ ┃ ┗ CustomOAuth2UserService.java
 ┃ ┃ ┣ userinfo
 ┃ ┃ ┃ ┣ KakaoOAuth2UserInfo.java
 ┃ ┃ ┃ ┗ OAuth2UserInfo.java
 ┃ ┃ ┣ CustomOAuth2User.java
 ┃ ┃ ┗ OAuthAttributes.java
 ┃ ┣ service
 ┃ ┃ ┣ JwtService.java
 ┃ ┃ ┗ LoginService.java
 ┃ ┗ util
 ┃ ┃ ┗ PasswordUtil.java
 ┣ service
 ┃ ┣ CardInfoService.java
 ┃ ┣ CardService.java
 ┃ ┣ CardServiceImpl.java
 ┃ ┣ ChatGptService.java
 ┃ ┣ ChatGptServiceImpl.java
 ┃ ┣ CoolSmsServiceImpl.java
 ┃ ┣ MemberService.java
 ┃ ┣ MemberServiceImpl.java
 ┃ ┣ OrderService.java
 ┃ ┣ OrderServiceImpl.java
 ┃ ┣ PaymentService.java
 ┃ ┣ PaymentServiceImpl.java
 ┃ ┗ VerificationServiceImpl.java
 ┣ websocket
 ┃ ┣ ChatMessage.java
 ┃ ┣ CustomStompSessionHandler.java
 ┃ ┗ WebSocketController.java
 ┗ SmartpayApplication.java
```

### (2) 인프라 구조
![](img/shds2_project2.drawio.png)

### (3) 네트워크 구조
![](img/shds_network.drawio.png)

## 2. ERD
![](img/erd.png)

## 3. 프로젝트 설명

### (1) 배경
- 소비자는 다양한 카드사의 다양한 카드 상품을 가진다
- 소비자는 자신의 결제 형태에 맞는 최적의 카드를 탐색하기 어렵다 
- 소비자들의 사소한 경제적 이익을 극대화하고자 하는 욕구가 증가하고 있다 
- Open AI API등 고성능 AI 모델의 접근성 확대 
- 현대 소비자들은 간편하고 직관적인 사용자 경험을 선호한다. 
### (2) 목표
- 사용자에게 최적의 카드를 추천하여 결제 혜택을 극대화할 수 있도록 한다.
- 복잡한 카드 혜택 정보를 간단하고 직관적으로 제공한다. 
- AI와 고객별 결제 내역을 활용한 개인화된 맞춤형 서비스 제공
- 단순하고 편리한 UI를 통해 만족도 높은 서비스를 제공



## 4. 결제 프로세스

```mermaid
sequenceDiagram
    participant Buyer as Buyer (React)
    participant Server as Server (Spring Boot)
    participant Seller as Seller (React)
    
    Buyer->>Server: QR 코드 요청 (Websocket 연결)
    Server->>Buyer: QR 코드 응답 (QR 이미지)
    Buyer-->>Seller: QR 코드 인식 (판매자 페이지로 이동)
    
    loop Buyer가 대기
        Buyer->>Server: 서버에 결제 상태 확인 (Websocket 대기)
        Server-->>Buyer: 아직 결제 대기 중
    end
    
    Seller->>Server: 결제 요청
    Server->>Buyer: 결제 요청 정보 전달
    
    Buyer->>Buyer: 결제 요청 화면 표시
    
    Buyer->>Server: 결제 확인 (Websocket 종료)
    
    Server->>Server: 결제 프로세스
    
    Server->>Buyer: 결제 완료 알림    
    Server->>Seller: 결제 완료 알림
```


## 5. AI 프로세스
![](img/ai_process.png)