package backend.database.implementazioniNeonDBDAO;

import backend.database.dao.UserDAO;
import backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    //Normale implementazione dao
    @Autowired
    private DataSource dataSource;

}
