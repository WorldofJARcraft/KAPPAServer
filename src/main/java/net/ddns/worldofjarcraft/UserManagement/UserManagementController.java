package net.ddns.worldofjarcraft.UserManagement;

import net.ddns.worldofjarcraft.DatabaseRepresentation.Benutzer;
import net.ddns.worldofjarcraft.DatabaseRepresentation.ErrorClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserManagementController {
    private final String INSERT_SQL = "INSERT INTO benutzer(EMail, Passwort) values (?,?);";
    @Autowired
    JdbcTemplate template;
    @Autowired
    NamedParameterJdbcTemplate namedTemplate;
    @RequestMapping("/user/create")
    public ResponseEntity createUser(@RequestParam(value = "EMail", required = true) String EMail, @RequestParam(value = "Password", required = true) String Password){
        Benutzer b = new Benutzer(EMail,Password);
        if(new ArrayList(template.query("SELECT * FROM Benutzer WHERE EMail=?;", new Object[] { b.getEMail() }, (rs, RowNum) -> new Benutzer(rs.getString("EMail"), rs.getString("Passwort")))).isEmpty()){

            KeyHolder holder = new GeneratedKeyHolder();
            template.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, b.getEMail());
                    ps.setString(2, b.getPasswort());
                    return ps;
                }
            }, holder);
        //System.out.println("Inserted user "+holder.getKey().intValue());
        return new ResponseEntity(b,HttpStatus.OK);
        }
        else {
            ErrorClass error = new ErrorClass("User exists!");
            return new ResponseEntity(error,HttpStatus.CONFLICT);
        }
    }
}
