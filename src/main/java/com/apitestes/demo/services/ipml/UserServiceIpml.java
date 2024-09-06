package com.apitestes.demo.services.ipml;

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
    public User findById(UUID id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElseThrow(()-> new ObjectNotFoundException("Objeto não encontrado"));
    }

    @Override
    public List<User> findAllUsers() {
        List<User> list = userRepository.findAll();
        return list;
    }

    @Override
    public User create(UserDto user) {
        validUser(user);
        return userRepository.save(mapper.map(user, User.class));

    }

    @Override
    public User update(UserDto user) {
        User userFiltred = validUser(user);

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
        findById(id);
        userRepository.deleteById(id);
    }

    public User validUser(UserDto user){
        
        Optional<User> resultEmail = userRepository.findByEmail(user.getEmail());
        if(resultEmail.isPresent() && !resultEmail.get().getId().equals(user.getId())){  
            throw new DataIntegratyViolationException("Email já cadastrado");
        }

        if(user.getId()!=null){
            Optional<User> resultId =  userRepository.findById(user.getId());
            if(resultId.isEmpty()){
                throw new ObjectNotFoundException("Objeto não encontrado");
            }
            return resultId.get();
        }else{
            return mapper.map(user, User.class);
        }
    }
}
