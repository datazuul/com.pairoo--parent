package com.pairoo.frontend.webapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Aspect: Logging of method calls.
 * 
 * @author Ralf Eichinger
 */
@Aspect
@Component
public class AOPMethodLogger {

    private static Logger logger = LoggerFactory.getLogger(AOPMethodLogger.class); // NOSONAR

    @Pointcut("execution(public * com.pairoo.business.api..*.*Service.*(..))")
    public void businessMethods() {
    }

    @Pointcut("execution(public * com.pairoo.backend.dao..*.*Dao.*(..))"
	    + " || execution(public * com.pairoo.backend.dao..*.*Sao.*(..))")
    public void backendMethods() {
    }

    /**
     * Protokolliert die Ausführung eines join points und dessen Parameter.
     * Parameter werden nur angezeigt, wenn der Wert nicht null ist.
     * 
     * In aspect-oriented programming a set of join points is called a pointcut.
     * A join point is a specification of when, in the corresponding main
     * program, the aspect code should be executed. The join point is a point of
     * execution in the base code where the advice specified in a corresponding
     * pointcut is applied.
     * 
     * @param joinPoint
     *            Das Event des Advices
     */
    @Before("businessMethods() || backendMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
	String targetClassName = joinPoint.getTarget().getClass().getName();
	String logMsg = createMsgForLogMethodCall(joinPoint);
	Logger targetLog = LoggerFactory.getLogger(targetClassName);
	targetLog.info(logMsg);
    }

    String createMsgForLogMethodCall(JoinPoint joinPoint) {
	StringBuilder buffer = new StringBuilder();
	String targetMethodName = joinPoint.getSignature().getName();
	buffer.append(targetMethodName);
	buffer.append("(");
	final Object[] args = joinPoint.getArgs();
	CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
	String[] argNames = codeSignature.getParameterNames();
	appendMethodArgs(args, argNames, buffer, true);
	buffer.append(")");
	return buffer.toString();
    }

    /**
     * 
     * Protokolliert die Laufzeit des join points. Das logging ist nur aktiv,
     * wenn {@link #logger} mindestens debug-Level hat.
     * 
     * @param call
     * @return
     * @throws Throwable
     */
    @Around("businessMethods() || backendMethods()")
    public Object logMethodDuration(ProceedingJoinPoint call) throws Throwable { // NOSONAR
	// proceed benötigt das Weiterreichen der Exception (throws
	// Throwable)...
	Object returnValue;
	if (logger.isDebugEnabled()) {
	    String targetClassName = call.getTarget().getClass().getName();
	    String targetMethodName = call.getSignature().getName();
	    Logger targetLog = LoggerFactory.getLogger(targetClassName);
	    Logger laufzeitLog = LoggerFactory.getLogger("LAUFZEITMESSUNG");

	    if (targetLog.isDebugEnabled() || laufzeitLog.isDebugEnabled()) {
		StopWatch clock = new StopWatch(getClass().getName());
		try {
		    clock.start(call.toShortString());
		    returnValue = call.proceed();
		} finally {
		    clock.stop();

		    final Object[] args = call.getArgs();
		    CodeSignature codeSignature = (CodeSignature) call.getSignature();
		    String[] argNames = codeSignature.getParameterNames();

		    if (targetLog.isDebugEnabled()) {
			StringBuilder msgBuffer = new StringBuilder();
			appendMethodArgs(args, argNames, msgBuffer, true);
			String msg = createMsgForLogMethodDuration(targetMethodName, msgBuffer.toString(),
				clock.getTotalTimeMillis());
			targetLog.debug(msg);
		    }
		    if (laufzeitLog.isDebugEnabled()) {
			StringBuilder msgBuffer = new StringBuilder();
			appendMethodArgs(args, argNames, msgBuffer, false);
			String msg = createMsgForLogMethodDuration(targetMethodName, msgBuffer.toString(),
				clock.getTotalTimeMillis());
			laufzeitLog.debug(targetClassName + "." + msg);
		    }
		}
	    } else {
		returnValue = call.proceed();
	    }
	} else {
	    returnValue = call.proceed();
	}
	return returnValue;
    }

    String createMsgForLogMethodDuration(String targetMethodName, String targetMethodArgs, long duration) {
	final StringBuilder sb = new StringBuilder();
	sb.append(targetMethodName).append("(");
	if (targetMethodArgs != null) {
	    sb.append(targetMethodArgs);
	}
	sb.append("): ");
	sb.append("duration ").append(duration).append(" ms");
	return sb.toString();
    }

    /**
     * Existiert zum Testen, so kann ein Logger-Mock gesetzt werden.
     * 
     * @param logger
     */
    static void setLogger(Logger logger) {
	AOPMethodLogger.logger = logger;
    }

    protected void appendMethodArgs(final Object[] args, String[] argNames, StringBuilder buffer, boolean verboseArgs) {
	for (int i = 0; i < args.length; i++) {
	    final Object arg = args[i];
	    if (arg != null) {
		if (argNames != null) {
		    String argName = argNames[i];
		    if (argName != null) {
			buffer.append(argNames[i]);
			buffer.append("=");
		    }
		}
		if (verboseArgs) {
		    buffer.append(arg.toString());
		} else {
		    buffer.append(arg.getClass().getSimpleName());
		}
		if (i < (args.length - 1)) {
		    buffer.append(",");
		    if (verboseArgs) {
			buffer.append(" ");
		    }
		}
	    }
	}
    }
}