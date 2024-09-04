package com.apitestes.demo.services.ipml;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apitestes.demo.dto.UserDto;
import com.apitestes.demo.entity.User;
import com.apitestes.demo.exceptions.exceptionsType.DataIntegratyViolationException;
import com.apitestes.demo.exceptions.exceptionsType.ObjectNotFoundException;
import com.apitestes.demo.repositories.UserRepository;
import com.apitestes.demo.services.UserService;

@Service
public class UserServiceIpml implements UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findbyid(UUID id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException("Usuario não encontrado"));
    }

    @Override
    public List<User> findAllUsers() {
        List<User> list = userRepository.findAll();
        return list;
    }

    @Override
    public User create(UserDto user) {
        validEmail(user);
        return userRepository.save(mapper.map(user, User.class));

    }

    @Override
    public User update(UserDto user) {
        validEmail(user);
        User userFiltred = findbyid(user.getId());

        TypeMap<UserDto, User> propertyMapper = mapper.getTypeMap(UserDto.class, User.class);

        if (propertyMapper == null) {
            propertyMapper = mapper.createTypeMap(UserDto.class, User.class);
            propertyMapper.setPropertyCondition(Conditions.isNotNull());
        }
        mapper.map(user, userFiltred);
        return userRepository.save(userFiltred);
    }

    @Override
    public void delete(UUID id) {
        findbyid(id);
        userRepository.deleteById(id);
    }

    public boolean hasEmail(UserDto user) {
        Class<?> clazz = user.getClass();
        Field[] fields = clazz.getDeclaredFields();
    
        for (Field field : fields) {
            if (field.getName().equals("email")) {
                return true;
            }
        }
        return false; 
    }

    public void validEmail(UserDto user){

        if (hasEmail(user)){
            Optional<User> result = userRepository.findByEmail(user.getEmail());
            if(result.isPresent() && !result.get().getId().equals(user.getId())){
                throw new DataIntegratyViolationException("Email já cadastrado");
            }
        }
    }

}
