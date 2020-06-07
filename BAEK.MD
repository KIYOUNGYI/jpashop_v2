# 스프링 부트 원리
+ 의존성관리
+ 자동설정
+ 내장웹서버
+ JAR 패키징

# 스프링 부트 활용
+ 핵심 기능
+ 여러 기술 연동


# 스프링부트 운영
<pre><code>compile("org.springframework.boot:spring-boot-starter-actuator")</code></pre>

<pre><code>
terminal
$ jconsole
</code>
</pre>

## VisualVM 사용하기
+ https://visualvm.github.io/download.html

## HTTP 사용하기
+ /actuator
+ health와 info를 제외한 대부분의 Endpoint가 기본적으로 ​비공개​ 상태
+ 공개옵션조정
    + management.endpoints.web.exposure.include=*
    + management.endpoints.web.exposure.exclude=env,beans

시큐리티 설정하고, 권한 갖은 사람만 접근하는게 옳음.

그런데, jconsole 이나 VisualVM 둘 다, 보기가 너무 난해하고, 어떤 지표가 중요한 것인지도 잘 모르겠다.


# Actuator
+ https://github.com/codecentric/spring-boot-admin
스프링 부트 Actuator UI 제공
dependency
<pre><code>​spring-boot-admin-starter-server</code></pre>
<pre><code>@EnableAdminServer</code></pre>


