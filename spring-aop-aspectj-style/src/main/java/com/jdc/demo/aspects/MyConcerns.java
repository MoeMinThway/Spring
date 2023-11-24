package com.jdc.demo.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import com.jdc.demo.dto.Result;

@Aspect
@Configuration
public class MyConcerns {

	@Pointcut("bean(myService)")
	void myServiceBean() {
		
	}
//	@Before("myServiceBean()")
	@Before(value= "bean(myService) && args(value, .. )",argNames = "value")
	void beforeLog(String value) {
		System.out.println("Before Execution");
		System.out.println("Value is %s ".formatted(value));
	}

	@AfterReturning(
			pointcut = "bean(myService) && execution(com.jdc..Result *(..)) && args(name,count)",
			returning = "result",
			argNames = "result,name,count"
			)
	void afterReturning(Result result,String name,int count) {
		System.out.println("Return value"); // If Return ok return
		System.out.println("Return name  ::  is %s ".formatted(name));
		System.out.println("Retrun count ::  is %d ".formatted(count));
		System.out.println(result);
	}
//	@AfterThrowing(pointcut = "bean(myService) ")
	@AfterThrowing(pointcut = "bean(myService) && args(a,b)",
			argNames = "exception,a,b",
			throwing = "exception")
	void afterThrowing(RuntimeException exception,int a, int b) {
		
		System.out.println("After Throwing value"); // If Return ok return
		
		System.out.println("Argument a %d: ".formatted(a));
		System.out.println("Argument b %d: ".formatted(b));
		
	
		
		System.out.println(exception.getClass().getSimpleName());
		System.out.println(exception.getMessage());
	}
	@After(value="bean(myService) && args(*,count)",argNames = "count")
	void afterAll ( int count) {
//		System.out.println("After Finally ");
		System.out.println("After Finally Count is %d ".formatted(count));
	}
	@Around(value= "bean(myService) && args(value,count)",argNames = "value,count")
	Object aroundInvoke(ProceedingJoinPoint jointPoint,
			String value,
			int count) {
		Object result = null ;
		System.out.println("Around before invoke");
		System.out.println("Value is %s ".formatted(value));
		System.out.println("count is %s ".formatted(count));
		try {
			result =  jointPoint.proceed();
			
		} catch (Throwable e) {
			System.out.println("Around after throwing");
			throw new RuntimeException(e);
		}finally {
			System.out.println("Around Finally");
		}
		return result;
	}
}
