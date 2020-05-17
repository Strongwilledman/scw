package com.ugor.scw.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.ugor.scw.project.bean.TProject;
import com.ugor.scw.project.bean.TProjectImages;
import com.ugor.scw.project.bean.TProjectTag;
import com.ugor.scw.project.bean.TProjectType;
import com.ugor.scw.project.bean.TReturn;
import com.ugor.scw.project.constant.ProjectConstant;
import com.ugor.scw.project.mapper.TProjectImagesMapper;
import com.ugor.scw.project.mapper.TProjectMapper;
import com.ugor.scw.project.mapper.TProjectTagMapper;
import com.ugor.scw.project.mapper.TProjectTypeMapper;
import com.ugor.scw.project.mapper.TReturnMapper;
import com.ugor.scw.project.service.TProjectService;
import com.ugor.scw.project.vo.req.ProjectRedisStorageVo;
import com.ugor.scw.utils.AppDateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TProjectServiceImpl implements TProjectService {

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	TProjectMapper projectMapper;
	
	@Autowired
	TProjectImagesMapper projectImageMapper;
	
	@Autowired
	TReturnMapper returnMapper;
	
	@Autowired
	TProjectTypeMapper projectTypeMapper;
	
	@Autowired
	TProjectTagMapper projectTagMapper;
	
	@Transactional
	@Override
	public void saveProject(String accessToken, String projectToken, byte code) {
		
		String memberId = stringRedisTemplate.opsForValue().get(accessToken);
		
		//1.从redis中获取bigVo数据
		String bigStr = stringRedisTemplate.opsForValue().get(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken);
		ProjectRedisStorageVo bigVo = JSON.parseObject(bigStr,ProjectRedisStorageVo.class);
		//2.保存项目
		TProject project = new TProject();
		project.setName(bigVo.getName());
		project.setRemark(bigVo.getRemark());
		project.setMoney(bigVo.getMoney());
		project.setDay(bigVo.getDay());
		project.setStatus(code+"");
		project.setMemberid(Integer.parseInt(memberId));
		project.setCreatedate(AppDateUtils.getFormatTime());
		
		projectMapper.insertSelective(project);
		//主键回填,project在上句保存后就有值可以取回
		Integer projectId = project.getId();
		log.debug("projectId-{}",projectId);
		
		//3.保存图片
		TProjectImages projectImage = new TProjectImages();
		projectImage.setProjectid(projectId);
		projectImage.setImgurl(bigVo.getHeaderImage());
		projectImage.setImgtype((byte)0);
		projectImageMapper.insertSelective(projectImage);
		
		List<String> detailsImage = bigVo.getDetailsImage();
		for (String imgPath : detailsImage) {
			TProjectImages pi = new TProjectImages();
			pi.setProjectid(projectId);
			pi.setImgurl(bigVo.getHeaderImage());
			pi.setImgtype((byte)10);
			projectImageMapper.insertSelective(pi);	
		}
		
		//4.保存回报
		List<TReturn> projectReturns = bigVo.getProjectReturns();
		for (TReturn retObj : projectReturns) {
			retObj.setProjectid(projectId);
			returnMapper.insertSelective(retObj);
		}
		
		//5.保存项目和分类关系
		List<Integer> typeids = bigVo.getTypeids();
		for (Integer typeid : typeids) {
			TProjectType projectType = new TProjectType();
			projectType.setId(typeid);
			projectType.setProjectid(projectId);
			projectTypeMapper.insertSelective(projectType);
		}
		
		//6.保存项目和标签关系
		List<Integer> tagids = bigVo.getTagids();
		for (Integer tagid : tagids) {
			TProjectTag projectTag = new TProjectTag();
			projectTag.setId(tagid);
			projectTag.setProjectid(projectId);
			projectTagMapper.insertSelective(projectTag);
		}
		//7.保存发起人

		//8.保存法人
		
		//9.清理redis
		stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken);
	}

}
