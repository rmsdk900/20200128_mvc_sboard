package net.koreate.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AOPAdvice {
	@Around("execution(* net.koreate.*.controller.*.*(..))")
	public Object checkControllerObject(ProceedingJoinPoint pjp) throws Throwable {
		
		System.out.println("AOP CheckController START");
		
		for(Object o : pjp.getArgs()) {
			System.out.println(o);
		}
		
		Object o = pjp.proceed();
		
		System.out.println(o);
		
		System.out.println("AOP CheckController END");
		
		return o;
	}
}
