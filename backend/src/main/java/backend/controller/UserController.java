package backend.controller;


import backend.database.dao.UserDAO;
import backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    //ESEMPIO DI CONTROLLER REST


    @Autowired
    private UserDAO userDao;


    public UserController(UserDAO userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public List<User> getUsers() {
        //return userDao.findAll();
        return new ArrayList<>();
    }

    @PostMapping
    public String addUser(@RequestBody User user) {
        //userDao.save(user);
        return "Utente salvato!";
    }



}
