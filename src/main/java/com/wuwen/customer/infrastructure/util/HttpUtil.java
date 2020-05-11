package com.wuwen.customer.infrastructure.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/** @author wuwen */
@Slf4j
public class HttpUtil {
  private static RestTemplate restTemplate = new RestTemplate();

  public static JSONObject post(String url, Map<String, String> header, String requestBody) {
    return post(url, header, requestBody, 1);
  }

  public static JSONObject post(
      String url, Map<String, String> header, String requestBody, Integer retryTimes) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf("application/json;charset=utf-8"));
    headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));

    if (header != null) {
      headers.setAll(header);
    }

    HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
    MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<String, String>();
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParam);

    return doExchange(builder, httpEntity, HttpMethod.POST, retryTimes);
  }

  public static JSONObject get(
      String url,
      Map<String, String> header,
      MultiValueMap<String, String> queryParam,
      Integer retryTimes) {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.valueOf("application/json;charset=utf-8"));
    headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
    headers.setAll(header);

    HttpEntity<String> httpEntity = new HttpEntity<>(headers);

    if (queryParam == null) {
      queryParam = new LinkedMultiValueMap<>();
    }

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParam);

    return doExchange(builder, httpEntity, HttpMethod.GET, retryTimes);
  }

  private static JSONObject doExchange(
      UriComponentsBuilder builder,
      HttpEntity<String> httpEntity,
      HttpMethod method,
      Integer retryTimes) {
    int retries = 0;
    URI uri = builder.build().encode().toUri();
    while (true) {
      try {
        if (retries > 0) {
          log.warn("接口【{}】正在尝试第{}次请求...", uri, retries + 1);
        }

        return restTemplate.exchange(uri, method, httpEntity, JSONObject.class).getBody();
      } catch (RestClientException e) {
        log.warn("接口【{}】在第{}次请求失败...", uri, retries + 1);

        if (retries <= retryTimes - 1) {
          log.error("请求失败{}", e.getMessage());
        }
      } finally {
        retries++;
      }
    }
  }
}
