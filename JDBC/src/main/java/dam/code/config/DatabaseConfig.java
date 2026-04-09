package dam.code.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {


    //Son private (nadie las ve desde fuera) y final (nunca cambian).
    private static final String URL = "jdbc:postgresql://localhost:5432/pruebas";
    private static final String USER = "postgres";
    private static final String PASSWORD = "posgres";

    //Es el método que abre la conexión
    //El throws SQLException significa que si algo falla (contraseña mal, base de datos apagada...) lanzará un error avisando del problema.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

/*
*Cada vez que el programa quiera hacer algo en la base de datos, llama a DatabaseConfig.getConnection()
* y obtiene la conexión. Sin esto, todo lo demás no funciona.
* */