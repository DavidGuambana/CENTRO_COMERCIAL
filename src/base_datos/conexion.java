
package base_datos;

import java.sql.*;
import com.sun.jdi.connect.spi.Connection;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

public class conexion {
    String usuario = "root";
    String clave = "";
    String url = "jdbc:mysql://localhost:3306/centrocomercial";

    public Connection conectar_base() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(url, usuario, clave);
            
        } catch (Exception e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡No se pudo conectar a la base de datos!", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
        return con;
    }
}
