package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;        // 자기 자신을 가지고 있다.

    //생성자 주입을 쓰면 안된다. 왜냐, 자기 자신 CallService1가 생성도 안되었는데, 주입받으려고 하니까 예외가 터진다.

    //해결방법, setter로 의존관계 주입해라
    // <<빈 라이프 사이클>>
    //1. 스프링 컨테이너 생성
    //2. 스프링 빈 생성 (생성자 주입이 이단계에서 어느정도 일어남)
    //3. 의존관계 주입( 세터 , 필드 주입이 해당)
    //4. 초기화 콜백
    //5. 사용 ( 실제 애플리케이션 동작하면서 사용)
    //6. 소멸전 콜백
    //7. 스프링 종료
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        //프록시를 주입받게 된다.
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal();     //내부  메서드 호출 this.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
