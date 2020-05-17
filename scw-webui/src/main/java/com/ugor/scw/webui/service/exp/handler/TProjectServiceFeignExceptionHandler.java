package com.ugor.scw.webui.service.exp.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ugor.scw.vo.resp.AppResponse;
import com.ugor.scw.webui.service.TProjectServiceFeign;
import com.ugor.scw.webui.vo.resp.ProjectDetailVo;
import com.ugor.scw.webui.vo.resp.ProjectVo;
import com.ugor.scw.webui.vo.resp.ReturnPayConfirmVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TProjectServiceFeignExceptionHandler implements TProjectServiceFeign {

	@Override
	public AppResponse<List<ProjectVo>> all() {
		AppResponse resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【查询首页热点项目】失败");
		log.error("调用远程服务【查询首页热点项目】失败");
		return resp;
	}

	@Override
	public AppResponse<ProjectDetailVo> detailsInfo(Integer projectId) {
		AppResponse resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【查询热点项目详细信息】失败");
		log.error("调用远程服务【查询热点项目详细信息】失败");
		return resp;
	}

	@Override
	public AppResponse<ReturnPayConfirmVo> confirmProjectReturnPayInfo(Integer projectId, Integer retId) {
		AppResponse resp = AppResponse.fail(null);
		resp.setMsg("调用远程服务【查询项目回报】失败");
		log.error("调用远程服务【查询项目回报】失败");
		return resp;
	}

}
