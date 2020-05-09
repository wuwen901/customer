package com.wuwen.customer.infrastructure.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wuwen.customer.infrastructure.enums.ErrorEnum;
import com.wuwen.customer.infrastructure.exception.ParamException;
import com.wuwen.customer.infrastructure.exception.ServiceException;
import com.wuwen.customer.infrastructure.util.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.UUID;

/** @author wuwen */
@Slf4j
@Component
@Aspect
public class ParamAspect {
  private static final String UTF_8 = "utf-8";
  private static final String REQID = "reqId";
  private static final String GET = "GET";

  @Pointcut("execution(* com.wuwen.customer.controller.*.*(..))")
  public void excludeService() {}

  @Around("excludeService()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    long startTime = System.currentTimeMillis();
    RequestAttributes ra = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes sra = (ServletRequestAttributes) ra;

    HttpServletRequest request = sra.getRequest();
    String remoteAddr = request.getRemoteAddr();
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String queryString = request.getQueryString();
    Object[] args = pjp.getArgs();
    String params = "";
    // result的值就是被拦截方法的返回值
    Object result = pjp.proceed();
    MDC.put(REQID, UUID.randomUUID().toString());

    try {
      // 获取请求参数集合并进行遍历拼接
      if (args.length > 0) {
        if (GET.equals(method)) {
          params = queryString;
        } else {
          Object object = args[0];
          params = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
        }
        params = URLDecoder.decode(params, UTF_8);
      }
      log.info("requestMethod:{},remoteAddr:{},url:{},params:{}", method, remoteAddr, uri, params);
    } catch (ParamException | IllegalArgumentException | ServiceException e) {
      log.info("param异常" + e.getMessage(), e);
      return RpcResult.buildFail(ErrorEnum.PARAM_ERROR.getCode(), e.getMessage());
    } catch (Exception e) {
      log.info("服务请求处理异常" + e.getMessage(), e);
      return RpcResult.buildFail(ErrorEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    } finally {
      long endTime = System.currentTimeMillis();
      log.info(
          "responseBody:{},elapsed:{}ms.",
          JSON.toJSONString(result, SerializerFeature.WriteMapNullValue),
          (endTime - startTime));
    }

    MDC.remove(REQID);
    MDC.clear();

    return result;
  }
}
