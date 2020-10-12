package com.etc.feigninters;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "springclouduser",fallback = FeignClientFallback.class)
public interface UserFeignClient {
    @RequestMapping(value = "/user/get/{uid}",method = RequestMethod.GET)
    public Map<String,Object> getUserById(@PathVariable("uid") Integer uid);

}
