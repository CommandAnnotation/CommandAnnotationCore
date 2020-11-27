## CommandAnnotation<br>

CommandAnnotation 라이브러리는 더 편하고 쉽게 명령어를 등록할 수 있게 도와주는 라이브러리입니다.
어노테이션을 통해 명령어를 등록하고, 조절하며, 예외를 처리합니다.


#### 어떻게 작동하는가?
CommandAnnotation은 초기화를 요청한 플러그인의 모든 클래스를 불러와 순차적으로 처리합니다.
`@ApplyClass`, `@ApplyGlobal`과 같은 전역 처리 핸들러가 존재하며, 대부분의 예외등은 해당 전역 핸들러를 통해 처리할 수 있습니다.