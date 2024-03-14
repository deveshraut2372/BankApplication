package com.zplus.services.impl;

import com.zplus.models.User;
import com.zplus.repository.UserRepository;
import com.zplus.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getById(long id) {
        User user=new User();
        try {
            Optional<User> user1=userRepository.findById(id);
            user1.ifPresent(settingMaster -> BeanUtils.copyProperties(settingMaster, user));
            System.out.println("user......"+user.toString());
            return user;
        }
        catch (Exception e) {
            e.printStackTrace();
            return user;
        }
    }

    @Override
    public List getAllUserByParentId(Integer parentId) {
      List list=userRepository.getAllUserByParentId(parentId);
        return list;
    }

    @Override
    public User getByUserId(Long id) {
        User user=userRepository.getByUserId(id);
        return user;
    }
}
