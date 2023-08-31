package com.dmdev;

import com.dmdev.dao2.CompanyRepository;
import com.dmdev.dao2.UserRepository;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.PersonalInfo;
import com.dmdev.entity.Role;
import com.dmdev.interceptor.TransactionInterceptor;
import com.dmdev.mapper.CompanyReadMapper;
import com.dmdev.mapper.UserCreateMapper;
import com.dmdev.mapper.UserReadMapper;
import com.dmdev.service.UserService;
import com.dmdev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = (Session) Proxy.newProxyInstance(
                    SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            session.beginTransaction();

            UserRepository userRepository = new UserRepository(session);
            CompanyRepository companyRepository = new CompanyRepository(session);

            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);

            UserCreateMapper userCreateMapper = new UserCreateMapper(companyRepository);

//            UserService userService = new UserService(userRepository, userReadMapper, userCreateMapper);
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

            // Proxy UserService-object
            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname("Liza")
                            .lastname("Trio")
//                            .birthDate(LocalDate.now())
                            .build(),
                    "liza3@gmail.com",
                    null,
                    Role.USER,
                    1
                    );
            userService.create(userCreateDto);

            userService.findById(1l).ifPresent(System.out::println);

//            session.getTransaction().commit();
        }
    }
}
