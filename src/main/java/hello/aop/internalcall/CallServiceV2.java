package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

    /**
     * 지연해서 CallServiceV2를 쓰자.
     */
    //private final ApplicationContext applicationContext;        //applicationContext는 스프링이 그냥 주입 받을 수 있게 제공해준다.

    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }

    public void external() {
        log.info("call external");
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();      //provider에서 스프링으로 등록된 빈을 조회하는게 목적이고 그걸 꺼내쓰면 된다.
        //CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);  //callServiceV2를 좀 나중에 꺼내면 된다.
        callServiceV2.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
