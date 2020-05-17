package com.ugor.scw.project.service;

import java.util.List;

import com.ugor.scw.project.bean.TProject;
import com.ugor.scw.project.bean.TProjectImages;
import com.ugor.scw.project.bean.TReturn;
import com.ugor.scw.project.bean.TTag;
import com.ugor.scw.project.bean.TType;

public interface ProjectInfoService {

	List<TType> getProjectTypes();

	List<TTag> getAllProjectTags(); 

	TProject getProjectInfo(Integer projectId);

	List<TReturn> getProjectReturns(Integer projectId);

	List<TProject> getAllProjects();

	List<TProjectImages> getProjectImages(Integer id);

	TReturn getProjectReturnById(Integer retId);

}
