package com.zplus.services;

import com.zplus.models.User;

import java.util.List;

public interface UserService {
    User getById(long id);

    List getAllUserByParentId(Integer parentId);

    User getByUserId(Long id);
}
