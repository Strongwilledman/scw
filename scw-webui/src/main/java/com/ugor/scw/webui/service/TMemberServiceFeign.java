package com.ugor.scw.webui.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ugor.scw.vo.resp.AppResponse;
import com.ugor.scw.webui.service.exp.handler.TMemberServiceFeignExceptionHandler;
import com.ugor.scw.webui.vo.resp.UserRespVo;

@FeignClient(value="SCW-USER",fallback=TMemberServiceFeignExceptionHandler.class) //fallback处理异常
public interface TMemberServiceFeign {

	@PostMapping("/user/login")
	public AppResponse<UserRespVo> login(@RequestParam("loginacct") String loginacct,@RequestParam("password") String password);
	
}
