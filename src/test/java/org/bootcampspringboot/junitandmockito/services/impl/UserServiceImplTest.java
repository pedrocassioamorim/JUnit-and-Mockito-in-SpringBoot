package org.bootcampspringboot.junitandmockito.services.impl;

import org.bootcampspringboot.junitandmockito.domain.dto.UserDTO;
import org.bootcampspringboot.junitandmockito.domain.entites.User;
import org.bootcampspringboot.junitandmockito.repositories.UserRepository;
import org.bootcampspringboot.junitandmockito.services.exceptions.DataIntegratyViolationException;
import org.bootcampspringboot.junitandmockito.services.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    private static final Integer ID = 1;
    private static final String name = "Pedro Dias";
    private static final String email = "amorim@dias";
    private static final String password = "123";
    private static final String userNotFoundInDb = "User not found in DB";
    private static final String emailOnDB = "E-mail already on database";

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;

    private UserDTO userDTO;

    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(ID, name, email, password);
        userDTO = new UserDTO(ID, name, email, password);
        optionalUser = Optional.of(new User(ID, name, email, password));
    }

    @Test
    void whenFindByIdThenReturnUserInstance() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);

        User response = service.findById(ID);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException(){

        when(repository.findById(anyInt())).thenThrow(new UserNotFoundException(userNotFoundInDb));
        try{
            service.findById(ID);
        }catch (Exception e){
            assertEquals(UserNotFoundException.class, e.getClass());
            assertEquals(userNotFoundInDb, e.getMessage());
        }
    }

    @Test
    void whenFindAllThenReturnAListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(user));
        List<User> response = service.findAll();
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());

        assertEquals(ID, response.get(0).getId());
        assertEquals(name, response.get(0).getName());
        assertEquals(email, response.get(0).getEmail());
        assertEquals(password, response.get(0).getPassword());
    }

    @Test
    void whenCreateThenReturnSucess() {
        when(repository.save(any())).thenReturn(user);
        User response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(password, response.getPassword());
    }

    @Test
    void whenCreateThenReturnADataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);
        try{
            optionalUser.get().setId(2);
            service.create(userDTO);

        }catch (Exception e){
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals(emailOnDB, e.getMessage());
        }
    }

    @Test
    void whenUpdateThenReturnSucess() {
        when(repository.save(any())).thenReturn(user);
        User response = service.update(userDTO);

        assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(password, response.getPassword());
    }

    @Test
    void whenUpdateThenReturnADataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);
        try{
            optionalUser.get().setId(2);
            service.update(userDTO);

        }catch (Exception e){
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals(emailOnDB, e.getMessage());
        }
    }


    @Test
    void whenDeleteWithSuccess() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);
        doNothing().when(repository).deleteById(anyInt());
        service.delete(ID);
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void whenDeleteWithUserNotFoundException() {
        when(repository.findById(anyInt())).thenThrow(new UserNotFoundException(userNotFoundInDb));
        try{
            service.delete(ID);
        }catch (Exception e){
            assertEquals(UserNotFoundException.class, e.getClass());
            assertEquals(userNotFoundInDb, e.getMessage());

        }
    }


}