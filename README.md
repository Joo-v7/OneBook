# 📖 OneBook
> 한 권의 책, 무한한 이야기

---

## 프로젝트 소개
> 2024.12.03. ~ 2025.01.24
> 
> Java 기반의 Spring Framework를 활용하여, 온라인에서 도서를 쉽게 구매할 수 있는 직관적이고 종합적인 인터넷 서점 쇼핑몰
> 
> [Github Organization](https://github.com/nhnacademy-be8-OneBook)


## 팀원

<div align="center" dir="auto">
  <div class="markdown-heading" dir="auto">
<!--     <h3 class="heading-element" dir="auto"> NHN Academy 8기 - OneBook 팀 </h3> -->
    <a id="user-content--nhn-academy-8기---OneBook-팀-" class="anchor" aria-label="Permalink:  NHN Academy 8기 - OneBook 팀"></a>
  </div>
  <markdown-accessiblity-table data-catalyst="">
    <table>
      <thead>
        <tr>
          <th><a href="https://github.com/kimseonj"><img alt="Image" src="https://github.com/user-attachments/assets/1c3c5027-ccf3-4e8f-9ccf-1918fd710692" width="80px" style="max-width: 100%;"><br>김선준</a></th>
          <th><a href="https://github.com/tnvs99"><img alt="Image" src="https://github.com/user-attachments/assets/b594a38f-4f10-49fa-a8f3-40c772c616c7" width="85px" style="max-width: 100%;"><br>김영훈</a></th>
          <th><a href="https://github.com/Joo-v7"><img alt="Image" src="https://github.com/user-attachments/assets/9587c485-99ad-4ecc-b00c-2e09f1721195" width="80px" style="max-width: 100%;"><br>김주혁</a></th>
          <th><a href="https://github.com/Hodu-moon"><img src="https://github.com/Hodu-moon.png" width="100px" style="max-width: 100%;"><br>문영호</a></th>
          <th><a href="https://github.com/tndus165"> <img src="https://github.com/tndus165.png" width="100px" style="max-width: 100%;"><br>박수연</a></th>
          <th><a href="https://github.com/pangpangE123"> <img src="https://github.com/pangpangE123.png" width="100px" style="max-width: 100%;"><br>변상우</a></th>
          <th><a href="https://github.com/LDS4546"> <img alt="Image" src="https://github.com/user-attachments/assets/fef3cc3c-d29d-453e-a446-06c2675d9cd6" width="68px" style="max-width: 100%;"><br>이동수</a></th>
        </tr>
      </thead>
    </table>
</markdown-accessiblity-table>
</div>

---

<!-- #  OneBook_Repository -->

<!-- - [FRONT](https://github.com/nhnacademy-be8-OneBook/Onebook-frontapi) -->
<!-- - [AUTHENTICATION](https://github.com/nhnacademy-be8-OneBook/Onebook-accountapi) -->
<!-- - [TASK](https://github.com/nhnacademy-be8-OneBook/Onebook-taskapi) -->
<!-- - [GATEWAY](https://github.com/nhnacademy-be8-OneBook/Onebook-gateway) -->
<!-- - [EUREKA](https://github.com/nhnacademy-be8-OneBook/Onebook-eureka) -->

## 프로젝트 아키텍처
![Image](https://github.com/user-attachments/assets/43eda360-4f79-4c9e-9d82-c9d303e81bfb)
> - MSA 구조로 각 서비스가 독립적으로 배포 및 관리되며, IaaS로 NHN Cloud를 활용했습니다.
> <br></br>
> - Nginx
>    - 사용자의 요청이 Nginx로 들어오면, Nginx는 라운드 로빈 방식으로 프론트 서버에 요청을 로드밸런싱 해줍니다.
>    - 로드 밸런싱과 서버 이중화로 트래픽이 분산되고, 사용자와 서버 간 통신은 SSL로 암호화되어 보안성을 높입니다.
> - Front Server
>    - 사용자의 요청에 맞는 웹 페이지나 서비스를 제공합니다.
>    - Redis를 활용하여 장바구니, Dooray 인증번호 등의 임시 데이터를 캐시하거나 세션 데이터를 관리합니다.
> - Gateway Server
>    - 백엔드 서버들의 단일 진입점으로, 프론트 서버의 요청을 적절한 백엔드 서버로 라우팅 합니다.
> - Authentication Server
>    - JWT 기반 인증을 위해 JWT 발급, 검증, 파싱 서비스를 제공합니다.
> - Task Server
>    - DB와 연결되어 쇼핑몰 운영에 필요한 데이터 처리와 비즈니스 로직을 제공합니다.

---

## CI/CD
### Port
<table>
  <tr>
    <th></th>
    <th>Main</th>  
    <th>Develop</th>
    <th>배포서버</th>
  </tr>

  <tr>
    <td>front-api_1</td>
    <td>8100</td>
    <td>8110</td>
    <td>10110</td>
  </tr>

  <tr>
    <td>front-api_2</td>
    <td>8101</td>
    <td>8111</td>
    <td>10111</td>
  </tr>

  <tr>
    <td>gateway-api</td>
    <td>8200</td>
    <td>8210</td>
    <td>10112</td>
  </tr>

  <tr>
    <td>eureka-api</td>
    <td>8300</td>
    <td>8310</td>
    <td>10113</td>
  </tr>

  <tr>
    <td>authentication-api_1</td>
    <td>8400</td>
    <td>8410</td>
    <td>10114</td>
  </tr>

  <tr>
    <td>authentication-api_2</td>
    <td>8400</td>
    <td>8410</td>
    <td>10115</td>
  </tr>
  
  <tr>
    <td>task-api_1</td>
    <td>8500</td>
    <td>8510</td>
    <td>10116</td>
  </tr>
  
  <tr>
    <td>task-api_2</td>
    <td>8400</td>
    <td>8410</td>
    <td>10117</td>
  </tr>
</table>

---

## ER-Diagram
https://www.erdcloud.com/d/PoKgPpyiHAvGwdsNA

---

## 프로젝트 관리

### 백로그 작성
![image](https://github.com/user-attachments/assets/9951f1a5-0d88-404e-bc39-a7c96799efec)

<br></br>

### 스크럼
![image](https://github.com/user-attachments/assets/4436df46-d967-418b-9cdd-e1ea55b5b7eb)

![image](https://github.com/user-attachments/assets/98c411e9-1629-4e1e-b36a-d6be5bce0d92)

<br></br>

### 일정관리

- *Road Map, Kanban* 활용

*Road Map*
![image](https://github.com/user-attachments/assets/4c1cbd0a-0cc2-4ddb-a49f-a83611fbb24c)


*Kanban*
![image](https://github.com/user-attachments/assets/1333a682-235b-4e77-8388-62a3816f9927)

---

### 테스트 커버리지

- 정적 코드 분석 도구인 `SonarQube`를 활용
- 목표 : Coverage 80%

---

## 개인별 구현 상세내역

---
## ⚙ 기술 스택
### Language
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=Thymeleaf&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white)
![Java](https://img.shields.io/badge/Java-E34F26?style=flat&logo=Java&logoColor=white)


### Framework
![Spring](https://img.shields.io/badge/spring-6DB33F?style=flat&logo=spring&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring%20boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![BootStrap5](https://img.shields.io/badge/BootStrap5-4430A1?style=flat&logo=Spring&logoColor=white)

### Database
![MySQL](http://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white)
![Redis](http://img.shields.io/badge/Redis-C71A36?style=flat&logo=Redis&logoColor=white)


### Build Tool
![ApacheMaven](https://img.shields.io/badge/Maven-000000?style=flat&logo=ApacheMaven&logoColor=white)

### CI/CD
![Github Action](https://img.shields.io/badge/Github%20Action-2088FF?style=flat&logo=githubactions&logoColor=white)
![NHN Cloud](https://img.shields.io/badge/-NHN%20Cloud-blue?style=flat&logo=iCloud&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E98CD?style=flat&logo=SonarQube&logoColor=white)

### ETC
![intellijidea](https://img.shields.io/badge/intellij-000000?style=flat&logo=intellijidea&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white)
##
- 개발기간 : 2024.12.02 ~ 2025.01.24

## front template

[front template 주황색 ](https://wpthemesgrid.com/themes/eshop/index4.html)
