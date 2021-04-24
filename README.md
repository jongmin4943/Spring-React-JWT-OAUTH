# Spring-React-JWT-OAUTH

Spring JWT 서버로 OAuth까지 React와 연동

Client Credentails Grant Type 방식 사용

https://www.notion.so/4-9-OAUTH-c8138737c53b4b38871ef6bd5fdca4ea

https://www.notion.so/4-23-OAUTH-ff690624fb7a4ee7989aef2c28475c10

Spingboot서버는 JWT토큰 생성

OAuth2.0 로그인은 React에서 함.

React에서 로그인되면 그 정보를 그대로 스프링으로 던짐

스프링에서 그걸 받아서 회원가입 시키고 회원정보를 세션에 담음

그리고 JWT 토큰 만들어서 response의 body에 담아서 전송

React에서 JWT 토큰 받아서 localStorage에 저장.

인증이 필요한 요청시마다 header에 JWT토큰 담아서 요청.


git에서 내려 받으면 npm install 해서 node-module 폴더 생성


Axios 문서

https://yamoo9.github.io/axios/guide/api.html
