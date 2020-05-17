package com.ugor.scw.user.service;

import com.ugor.scw.user.vo.req.UserRegistVo;
import com.ugor.scw.user.vo.resp.UserRespVo;

public interface TMemberService {

	int saveTMember(UserRegistVo vo);

	UserRespVo getUserByLogin(String loginacct, String password);

}
