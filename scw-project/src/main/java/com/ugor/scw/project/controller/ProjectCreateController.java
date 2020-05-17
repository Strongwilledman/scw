package com.ugor.scw.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ugor.scw.project.bean.TReturn;
import com.ugor.scw.project.component.OssTemplate;
import com.ugor.scw.project.constant.ProjectConstant;
import com.ugor.scw.project.service.TProjectService;
import com.ugor.scw.project.vo.req.BaseVo;
import com.ugor.scw.project.vo.req.ProjectBaseInfoVo;
import com.ugor.scw.project.vo.req.ProjectRedisStorageVo;
import com.ugor.scw.project.vo.req.ProjectReturnVo;
//import com.ugor.scw.project.constant.ProjectConstant;
//import com.ugor.scw.project.vo.req.BaseVo;
//import com.ugor.scw.project.vo.req.ProjectRedisStorageVo;
import com.ugor.scw.vo.resp.AppResponse;
import com.ugor.user.enums.ProjectStatusEnume;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = "项目发起模块")
@RequestMapping("/project/create")
@RestController
public class ProjectCreateController {

	@Autowired
	OssTemplate ossTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	TProjectService projectService;

	@ApiOperation(value = "1-项目初始创建")
	@PostMapping("/init")
	public AppResponse<Object> init(BaseVo vo) {

		try {
			String accessToken = vo.getAccessToken();

			if (StringUtils.isEmpty(accessToken)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请求必须提供assessToken值");
				return resp;
			}

			String memberId = stringRedisTemplate.opsForValue().get(accessToken);

			if (StringUtils.isEmpty(memberId)) {
				AppResponse resp = AppResponse.fail(null);
				resp.setMsg("请先登录系统,再发布项目！");
				return resp;
			}

			ProjectRedisStorageVo bigVO = new ProjectRedisStorageVo();

			BeanUtils.copyProperties(vo, bigVO);

			String projectToken = UUID.randomUUID().toString().replaceAll("-", "");
			bigVO.setProjectToken(projectToken);

			String bigStr = JSON.toJSONString(bigVO); // fastjson组件

			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken, bigStr);

			log.debug("大VO数据：{}", bigVO);

			return AppResponse.ok(bigVO); // jackson组件
		} catch (BeansException e) {
			e.printStackTrace();
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "2-项目基本信息")
	@PostMapping("/baseinfo")
	public AppResponse<Object> baseinfo(ProjectBaseInfoVo vo) {
		try {
			//1、验证
			String accessToken = vo.getAccessToken();
			if(StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请求必须提供assessToken的值");
				return response;
			}
			String memberId = stringRedisTemplate.opsForValue().get(accessToken);
			if(StringUtils.isEmpty(memberId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录系统再发布项目!");
				return response;
			}
			
			//2、从redis获取bigVO,将小vo封装到大vo中
			String bigStr = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken());
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigStr,ProjectRedisStorageVo.class);
			
			BeanUtils.copyProperties(vo, bigVo);
					
			//3、再放回redis
			bigStr = JSON.toJSONString(bigVo);
			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken(), bigStr);
			log.debug("大VO数据：{}", bigVo);
			return AppResponse.ok("ok");
		} catch (BeansException e) {
			e.printStackTrace();
			log.error("表单处理失败-{}",e.getMessage());
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "3-添加项目回报档位")
	@PostMapping("/return")
	public AppResponse<Object> returnDetail(@RequestBody List<ProjectReturnVo> pro) {
		try {
			//1、验证
			String accessToken = pro.get(0).getAccessToken();
			if(StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请求必须提供assessToken的值");
				return response;
			}
			String memberId = stringRedisTemplate.opsForValue().get(accessToken);
			if(StringUtils.isEmpty(memberId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录系统再发布项目!");
				return response;
			}
			
			//2、从redis获取bigVO,将小vo封装到大vo中
			String bigStr = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+pro.get(0).getProjectToken());
			ProjectRedisStorageVo bigVo = JSON.parseObject(bigStr,ProjectRedisStorageVo.class);
					
			List<TReturn> projectReturns = bigVo.getProjectReturns();
			for (ProjectReturnVo projectReturnVo : pro) {
				TReturn returnObj = new TReturn();
				BeanUtils.copyProperties(projectReturnVo, returnObj);
				projectReturns.add(returnObj);
			}
		
			//3、再放回redis
			bigStr = JSON.toJSONString(bigVo);
			stringRedisTemplate.opsForValue().set(ProjectConstant.TEMP_PROJECT_PREFIX+pro.get(0).getProjectToken(), bigStr);
			
			log.debug("大VO数据：{}", bigVo);
			return AppResponse.ok("ok");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("表单处理失败-{}",e.getMessage());
			return AppResponse.fail(null);
		}
	}

	/**
	 * 文件上传表单要求： ①method="post" ②enctype="multipart/form-data" ③type="file"
	 * 
	 * SpringMVC框架集成commons-fileupload和commons-io组件，完成文件上传操作。 SpringMVC提供文件上传解析器。
	 * Controller处理文件上传时，通过MultipartFile接受文件。
	 */
	@ApiOperation(value = "上传图片")
	@PostMapping("/upload")
	public AppResponse<Object> upload(@RequestParam("uploadfile") MultipartFile[] files) {

		try {
			List<String> filepathList = new ArrayList<String>();

			for (MultipartFile multipartFile : files) {
				String filename = multipartFile.getOriginalFilename();

				filename = UUID.randomUUID().toString().replaceAll("-", "") + "_" + filename;

				String filepath = ossTemplate.upload(filename, multipartFile.getInputStream());
				filepathList.add(filepath);
			}

			log.debug("上传文件路径={}", filepathList);

			return AppResponse.ok(filepathList);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("上传文件出现异常");
			return AppResponse.fail(null);
		}
	}

	@ApiOperation(value = "4-项目提交审核申请")
	@PostMapping("/submit")
	public AppResponse<Object> submit(String accessToken,String projectToken,String ops) {
		try {
			//1、验证
			if(StringUtils.isEmpty(accessToken)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请求必须提供assessToken的值");
				return response;
			}
			String memberId = stringRedisTemplate.opsForValue().get(accessToken);
			if(StringUtils.isEmpty(memberId)) {
				AppResponse response = AppResponse.fail(null);
				response.setMsg("请先登录系统再发布项目!");
				return response;
			}
			
			if("0".equals(ops)) {//保存草稿
				projectService.saveProject(accessToken, projectToken, ProjectStatusEnume.DRAFT.getCode());

				return AppResponse.ok("ok");
			}else if("1".equals(ops)) {//保存提交审核
				projectService.saveProject(accessToken, projectToken, ProjectStatusEnume.SUBMIT_AUTH.getCode());
				
				return AppResponse.ok("ok");
			}else {
				log.error("请求方式不支持");
				return AppResponse.fail(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("提供法人数据出现异常-{}",e.getMessage());
			return AppResponse.fail(null);
		}
	}

//	@ApiOperation(value = "删除项目回报档位")
//	@DeleteMapping("/return")
//	public AppResponse<Object> deleteReturnDetail() {
//		return AppResponse.ok("ok");
//	}

//	@ApiOperation(value = "4-确认项目法人信息")
//	@PostMapping("/confirm/legal")
//	public AppResponse<Object> legal() {
//		return AppResponse.ok("ok");
//	}

//	@ApiOperation(value = "项目草稿保存")
//	@PostMapping("/tempsave")
//	public AppResponse<Object> tempsave() {
//		return AppResponse.ok("ok");
//	}
}
