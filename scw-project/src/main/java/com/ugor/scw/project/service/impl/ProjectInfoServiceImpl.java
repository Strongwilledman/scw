package com.ugor.scw.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ugor.scw.project.bean.TProject;
import com.ugor.scw.project.bean.TProjectImages;
import com.ugor.scw.project.bean.TProjectImagesExample;
import com.ugor.scw.project.bean.TReturn;
import com.ugor.scw.project.bean.TReturnExample;
import com.ugor.scw.project.bean.TTag;
import com.ugor.scw.project.bean.TType;
import com.ugor.scw.project.mapper.TProjectImagesMapper;
import com.ugor.scw.project.mapper.TProjectMapper;
import com.ugor.scw.project.mapper.TReturnMapper;
import com.ugor.scw.project.mapper.TTagMapper;
import com.ugor.scw.project.mapper.TTypeMapper;
import com.ugor.scw.project.service.ProjectInfoService;



@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
	@Autowired
	TTypeMapper typeMapper;
	
	@Autowired
	TTagMapper tagMapper;
	@Autowired
	TProjectMapper projectMapper;
	
	@Autowired
	TProjectImagesMapper imageMapper; 

	@Autowired
	TReturnMapper returnMapper;
	@Override
	public List<TType> getProjectTypes() {
		return typeMapper.selectByExample(null);
	}

	@Override
	public List<TTag> getAllProjectTags() {
		return tagMapper.selectByExample(null);
	}

	@Override
	public TProject getProjectInfo(Integer projectId) {
		TProject project = projectMapper.selectByPrimaryKey(projectId);
		return project;
	}

	@Override
	public List<TReturn> getProjectReturns(Integer projectId) {
		TReturnExample example = new TReturnExample();
		example.createCriteria().andProjectidEqualTo(projectId);
		return returnMapper.selectByExample(example);
	}

	@Override
	public List<TProject> getAllProjects() {
		return projectMapper.selectByExample(null);
	}

	@Override
	public List<TProjectImages> getProjectImages(Integer id) {
		TProjectImagesExample example = new TProjectImagesExample();
		example.createCriteria().andProjectidEqualTo(id);
		return imageMapper.selectByExample(example);
	}

	@Override
	public TReturn getProjectReturnById(Integer retId) {
//		return returnMapper.selectByPrimaryKey(retId);
		return null;
	}

}
