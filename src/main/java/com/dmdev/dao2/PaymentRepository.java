package com.dmdev.dao2;

import com.dmdev.entity.Payment;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

   public PaymentRepository(EntityManager entityManagery){
       super(Payment.class, entityManagery);
   }

}
