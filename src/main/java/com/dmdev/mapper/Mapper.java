package com.dmdev.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);

    F mapTo(T entity);
}
