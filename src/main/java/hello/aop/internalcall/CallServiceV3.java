package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV3 {
    /**
     * 내부 호출이 발생하지 않도록 구조 자체를 변경(분리)하자.
     */

    private final InternalService internalService;

    public CallServiceV3(InternalService internalService) {
        this.internalService = internalService;
    }

    public void external() {
        log.info("call external");
        internalService.internal();     //외부 메서드 호출.
    }
    //아래 코드를 InternalService로 가져간다.
    /*public void internal() {
        log.info("call internal");
    }*/
}
