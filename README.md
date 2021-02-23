# jpashpop_v2

- h2 세팅
    - 맥, 리눅스 실행 버전: https://h2database.com/h2-2019-10-14.zip

- h2 로컬 실행 

    ```bin$ ./h2.sh```
    
    권한 이슈 있으면  ``chmod 755 h2.sh ```

- 데이터베이스 파일 생성 방법
    - localhost:8082 접속
    - jdbc:h2:~/jpashop (최소 한번)
    - ~/jpashop.mv.db 파일 생성 확인(사실 안해도 그만)
    - 이후 부터는 jdbc:h2:tcp://localhost/~/jpashop 이렇게 접속
    
- dev-001 브랜치
    - 쿼리 날라가는 개수 세는 모듈 추가