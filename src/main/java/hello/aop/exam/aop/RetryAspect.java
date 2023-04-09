package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    //얘는 Around를 써야해 왜냐면, 재시도할 때 언제 내가 조인포인트에 프로시드를 호출할지 정해야해서.

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {        //Throwable은 Exception도 Exception이지만, oom같은 것도 포함되기 때문에 이건 어쩔수 없어서 그냥 밖으로 던진다.
        log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;
        for(int retryCount=1; retryCount<=maxRetry; retryCount++){
            try {
                log.info("[retry] try count={}/{}", retryCount, maxRetry);
                return joinPoint.proceed();    //이걸 해서 깔끔하게 수행되면 좋은데 만약에 예외가 터져?
            }catch(Exception e){
                exceptionHolder = e;    //리턴횟수가 넘어가면(maxRetry값까지 루프를 다 돌면) 그냥 예외를 던져야해.
                //예외가 오면 다시 루프를 돌아야해. 그래서 예외를 어딘가에 담아놔야 한다.
            }
        }
        //예외 던지자.
        throw exceptionHolder;

    }
}
