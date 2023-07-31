package com.dmdev;

import com.dmdev.entity.Company;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class HibernateProxyTest {
    @Test
    void testDynamicProxy() {
        Company company = new Company();
        Proxy.newProxyInstance(company.getClass().getClassLoader(),
                company.getClass().getInterfaces(),     // if entity hasn't interfaces then dynamic proxy doesn't work
                (proxy, method, args) -> {
                    return method.invoke(company, args); // or could using cache or other variants
                }
        );
    }
}
