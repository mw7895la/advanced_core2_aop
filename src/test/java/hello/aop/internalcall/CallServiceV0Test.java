package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;        //얘는 프록시다.

    @Test
    void external() {
        log.info("target ={}", callServiceV0.getClass());
        callServiceV0.external();
        /** external() 호출시 타겟에서 internal()을 호출하는데 그건 프록시 적용 안된다.*/
    }

    @Test
    void internal() {
        callServiceV0.internal();

    }
}