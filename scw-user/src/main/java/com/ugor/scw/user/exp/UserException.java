package com.ugor.scw.user.exp;

import com.google.common.base.Enums;
import com.ugor.scw.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {

	public UserException() {

	}

	public UserException(UserExceptionEnum userSaveError) {
		super(userSaveError.getMessage());
	}

}
