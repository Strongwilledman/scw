package com.ugor.scw.webui.service.exp.handler;

import org.springframework.stereotype.Component;

import com.ugor.scw.vo.resp.AppResponse;
import com.ugor.scw.webui.service.TMemberServiceFeign;
import com.ugor.scw.webui.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TMemberServiceFeignExceptionHandler implements TMemberServiceFeign {

	@Override
	public AppResponse<UserRespVo> login(String loginacct, String password) {
		AppResponse<UserRespVo> resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【登录】失败");
		
		log.error("调用远程服务【登录】失败");
		
		return resp;
	}

}
