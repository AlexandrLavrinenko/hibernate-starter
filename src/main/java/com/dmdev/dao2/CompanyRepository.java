package com.dmdev.dao2;

import com.dmdev.entity.Company;
import com.dmdev.entity.Payment;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

   public CompanyRepository(EntityManager entityManagery){
       super(Company.class, entityManagery);
   }

}
