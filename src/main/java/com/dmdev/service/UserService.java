package com.dmdev.service;

import com.dmdev.dao2.UserRepository;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.Mapper;
import com.dmdev.mapper.UserCreateMapper;
import com.dmdev.mapper.UserReadMapper;
import com.dmdev.validation.UpdateCheck;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        // 1. validation Dto-level (in UserCreateDto -> @Valid and @NotNull)
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        // Все поля, имеющие аннотации с этой группы, будут дополнительно валидироваться
        // Если группа UpdateCheck не будет указана - мы не будем дополнительно валидировать это поле (Role в UserCreateDto)
        Set<ConstraintViolation<UserCreateDto>> validationResult = validator.validate(userDto, UpdateCheck.class);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }

        // 2. map to Entity
        User userEntity = userCreateMapper.mapFrom(userDto);

        return userRepository.save(userEntity).getId();
    }


    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        // оптимизация N+1
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(),
                userRepository.getEntityManager().getEntityGraph("WithCompany")
        );

        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

//    public Optional<UserReadDto> findById(Long id) {
//        // оптимизация N+1
//        Map<String, Object> properties = Map.of(
//                GraphSemantic.LOAD.getJpaHintName(),
//                userRepository.getEntityManager().getEntityGraph("WithCompany")
//        );
//
//        return userRepository.findById(id, properties)
//                .map(userReadMapper::mapFrom);
//    }

    @Transactional
    public boolean delete (Long id){
        Optional<User> mayBeUser = userRepository.findById(id);
        mayBeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return mayBeUser.isPresent();
    }
}
