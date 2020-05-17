package com.ugor.scw.user.service.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ugor.scw.user.bean.TMember;
import com.ugor.scw.user.bean.TMemberExample;
import com.ugor.scw.user.enums.UserExceptionEnum;
import com.ugor.scw.user.exp.UserException;
import com.ugor.scw.user.mapper.TMemberMapper;
import com.ugor.scw.user.service.TMemberService;
import com.ugor.scw.user.vo.req.UserRegistVo;
import com.ugor.scw.user.vo.resp.UserRespVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TMemberServiceImpl implements TMemberService {
	@Autowired
	TMemberMapper memberMapper;

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
	@Override
	public int saveTMember(UserRegistVo vo) {
		
		try {
			//将vo对象拷到do对象中
			TMember member = new TMember();
			BeanUtils.copyProperties(vo, member);
			member.setUsername(vo.getLoginacct());
			
			String userpswd = vo.getUserpswd();
			member.setUserpswd(userpswd);
			
			int i = memberMapper.insertSelective(member);
			log.debug("注册会员成功-{}",member);
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("注册会员失败-{}",e.getMessage());
			throw new UserException(UserExceptionEnum.USER_SAVE_ERROR);
		}
	}

	@Override
	public UserRespVo getUserByLogin(String loginacct, String password) {
		UserRespVo vo = new UserRespVo();
		
		TMemberExample example = new TMemberExample();
		example.createCriteria().andLoginacctEqualTo(loginacct);
		List<TMember> list = memberMapper.selectByExample(example);
		if(list==null && list.size()==0) {
			throw new UserException(UserExceptionEnum.USER_EXISTS);
		}
		TMember member = list.get(0);
		
		
		if(!member.getUserpswd().equals(password)) {
			throw new UserException(UserExceptionEnum.USER_PASSWORD_ERROR);
		}
		BeanUtils.copyProperties(member, vo);
		String accessToken = UUID.randomUUID().toString().replaceAll("-", "");
		vo.setAccessToken(accessToken);
		stringRedisTemplate.opsForValue().set(accessToken, member.getId().toString());
		return vo;
	}
}
