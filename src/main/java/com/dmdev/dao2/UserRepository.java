package com.dmdev.dao2;

import com.dmdev.entity.User;

import javax.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Long, User> {

   public UserRepository(EntityManager entityManagery){
       super(User.class, entityManagery);
   }

}
