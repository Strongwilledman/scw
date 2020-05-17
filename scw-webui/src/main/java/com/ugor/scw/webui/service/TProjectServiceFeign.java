package com.ugor.scw.webui.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ugor.scw.vo.resp.AppResponse;
import com.ugor.scw.webui.service.exp.handler.TProjectServiceFeignExceptionHandler;
import com.ugor.scw.webui.vo.resp.ProjectDetailVo;
import com.ugor.scw.webui.vo.resp.ProjectVo;
import com.ugor.scw.webui.vo.resp.ReturnPayConfirmVo;

@FeignClient(value="SCW-PROJECT",fallback=TProjectServiceFeignExceptionHandler.class)
public interface TProjectServiceFeign {

	@GetMapping("/project/all")
	public AppResponse<List<ProjectVo>> all() ;
	
	@GetMapping("/project/details/info/{projectId}")
	public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId);

	@GetMapping("/project/support/returnConfirm/{projectId}/{retId}")
	public AppResponse<ReturnPayConfirmVo> confirmProjectReturnPayInfo(@PathVariable("projectId") Integer projectId, @PathVariable("retId") Integer retId);
	
}
