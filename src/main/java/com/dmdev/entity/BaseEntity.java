package com.dmdev.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//@MappedSuperclass
//@Getter
//@Setter
//public abstract class BaseEntity<T extends Serializable> {
public interface BaseEntity<T extends Serializable> {

    void setId(T id);
    T getId();
}
