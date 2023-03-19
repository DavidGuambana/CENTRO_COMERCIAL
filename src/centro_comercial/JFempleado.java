package centro_comercial;
import base_datos.conexion;
import java.awt.Color;
import java.util.Date;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.border.TitledBorder;
import otros.validar;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class JFempleado extends javax.swing.JFrame {
    public static String forma = "registrar";
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    long d;
    public static final String FK = "Seleccionar...";
    
    public static int FK_gen, FK_ciu, FK_dep,FK_pue;
    String [] arreglo = {"Registrar con los datos ya existentes", "Registrar con los nuevos datos"};
    
    public JFempleado() {
        initComponents();
        cambiar_diseño();
        setLocationRelativeTo(null);
    }
    
    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            cedula.setEditable(true);
            color = new Color(51,51,51);
            jl_titulo.setText("Registrar empleado");
            jb_Ejecutar.setText("¡Registrar!");
        } else if (forma.equals("modificar")) {
            cedula.setEditable(false);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar empleado");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 12; i++) {
            switch (i) {
                case 1:titulo = "Cédula:"; break;
                case 2:titulo = "Nombre:"; break;
                case 3:titulo = "Apellido:";  break;
                case 4:titulo = "Fecha de nacimiento:"; break;
                case 5:titulo = "Género:"; break;
                case 6:titulo = "N°. Celular:";  break;
                case 7:titulo = "Email:";break;
                case 8:titulo = "Dirección:";break;
                case 9:titulo = "Ciudad:"; break;
                case 10:titulo = "Departamento:";break;
                case 11:titulo = "Puesto:";break;
                case 12:titulo = "Contraseña:"; break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:cedula.setBorder(tb);
                    break;
                case 2:nombre.setBorder(tb);
                    break;
                case 3: apellido.setBorder(tb);
                    break;
                case 4: nacimiento.setBorder(tb);
                    break;
                case 5: genero.setBorder(tb);
                    break;
                case 6:celular.setBorder(tb);
                    break;
                case 7:email.setBorder(tb);
                    break;
                case 8: direccion.setBorder(tb);
                    break;
                case 9: ciudad.setBorder(tb);
                    break;
                case 10: departamento.setBorder(tb);
                    break;
                case 11: puesto.setBorder(tb);
                    break;
                case 12: password.setBorder(tb);
                    break;
            }
        }
    }
      
    public static void limpiar(){   
        cedula.setText("");
        nombre.setText("");
        apellido.setText("");
        nacimiento.setDate(null);
        genero.setText(FK);
        celular.setText("");
        email.setText("");
        direccion.setText("");
        ciudad.setText(FK);
        departamento.setText(FK);
        puesto.setText(FK);
        password.setText("");
    }
    public void llenar(String xcedula){
        con =  (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA='" + xcedula + "'");
                rs = ps.executeQuery();
                rs.next();
                cedula.setText(rs.getString(1));
                nombre.setText(rs.getString(2));
                apellido.setText(rs.getString(3));
                nacimiento.setDate(rs.getDate(4));
                FK_gen = rs.getInt(5);
                celular.setText(rs.getString(6));
                email.setText(rs.getString(7));
                direccion.setText(rs.getString(8));
                FK_ciu = rs.getInt(9);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE CEDULA_PER='" + xcedula + "'");
                rs = ps.executeQuery();
                rs.next();
                password.setText(rs.getString(3));
                FK_dep = rs.getInt(4);
                FK_pue = rs.getInt(5);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM GENERO WHERE ID=" + FK_gen);
                rs = ps.executeQuery();
                rs.next();
                genero.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD WHERE ID=" + FK_ciu);
                rs = ps.executeQuery();
                rs.next();
                ciudad.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM DEPARTAMENTO WHERE ID=" + FK_dep);
                rs = ps.executeQuery();
                rs.next();
                departamento.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PUESTO WHERE ID=" + FK_pue);
                rs = ps.executeQuery();
                rs.next();
                puesto.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡El cliente '" + xcedula + "' no existe!");
            }
        }
    }

    public void registrar() {
        boolean registrado = false;
        boolean emp_existente = false;
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA = '" + cedula.getText() + "'");
                if (ps.executeQuery().next()) {
                    ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE CEDULA_PER = '" + cedula.getText() + "'");
                    if (ps.executeQuery().next()) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(rootPane, "¡El empleado '" + cedula.getText() + "' ya existe!");
                        emp_existente = true;
                    } else{
                        int opcion = -1;
                        do {
                            opcion = JOptionPane.showOptionDialog(null, "Se ha encontrado a la persona '" + cedula.getText() + "' ya existente! ¿Qué desea hacer?", "Persona existente", 0, JOptionPane.QUESTION_MESSAGE, null, arreglo, null);
                        } while (opcion == -1);
                        if (opcion == 1) {
                            ps = (PreparedStatement) con.prepareStatement("UPDATE PERSONA SET NOMBRE=?,APELLIDO=?,FECHA_NAC=?,ID_SEXO=?,CELULAR=?,EMAIL=?,DIRECCION=?,ID_CIUDAD=? WHERE CEDULA=?");
                            ps.setString(1, nombre.getText().toUpperCase());
                            ps.setString(2, apellido.getText().toUpperCase());
                            java.sql.Date nac = new java.sql.Date(d);
                            ps.setDate(3, nac);
                            ps.setInt(4, FK_gen);
                            ps.setString(5, celular.getText());
                            ps.setString(6, email.getText());
                            ps.setString(7, direccion.getText());
                            ps.setInt(8, FK_ciu);
                            ps.setString(9, cedula.getText());
                            ps.executeUpdate(); //ACTUALIZA DATOS DE PERSONA
                        }
                    }
                } else {
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO PERSONA (CEDULA,NOMBRE,APELLIDO,FECHA_NAC,ID_SEXO,CELULAR,EMAIL,DIRECCION,ID_CIUDAD) VALUES (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, cedula.getText());
                    ps.setString(2, nombre.getText().toUpperCase());
                    ps.setString(3, apellido.getText().toUpperCase());
                    java.sql.Date nac = new java.sql.Date(d);
                    ps.setDate(4, nac);
                    ps.setInt(5, FK_gen);
                    ps.setString(6, celular.getText());
                    ps.setString(7, email.getText());
                    ps.setString(8, direccion.getText());
                    ps.setInt(9, FK_ciu);
                    ps.executeUpdate(); //REGISTRA NUEVA PERSONA
                }
                if (!emp_existente) {
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO EMPLEADO (CEDULA_PER, PASSWORD, ID_DEP, ID_PUE) VALUES (?,?,?,?)");
                    ps.setString(1, cedula.getText());
                    ps.setString(2, password.getText());
                    ps.setInt(3, FK_dep);
                    ps.setInt(4, FK_pue);
                    ps.executeUpdate(); //REGISTRA NUEVO EMPLEADO
                    registrado = true;
                }
                if (registrado) {
                    JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                    PRINCIPAL.actualizado = false;
                    this.dispose();
                }
            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al registrar!");
            }
        }
    }

    public void modificar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("UPDATE PERSONA SET NOMBRE=?,APELLIDO=?,FECHA_NAC=?,ID_SEXO=?,CELULAR=?,EMAIL=?,DIRECCION=?,ID_CIUDAD=? WHERE CEDULA=?");
                ps.setString(1, nombre.getText().toUpperCase());
                ps.setString(2, apellido.getText().toUpperCase());
                java.sql.Date nac = new java.sql.Date(d);
                ps.setDate(3, nac);
                ps.setInt(4, FK_gen);
                ps.setString(5, celular.getText());
                ps.setString(6, email.getText());
                ps.setString(7, direccion.getText());
                ps.setInt(8, FK_ciu);
                ps.setString(9, cedula.getText());
                ps.executeUpdate(); //Ejecuta la consulta

                ps = (PreparedStatement) con.prepareStatement("UPDATE EMPLEADO SET PASSWORD=?, ID_DEP=?, ID_PUE=? WHERE CEDULA_PER=?");
                ps.setString(1, password.getText());
                ps.setInt(2, FK_dep);
                ps.setInt(3, FK_pue);
                ps.setString(4, cedula.getText());
                ps.executeUpdate(); //ejecuta la consulta
                JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
                PRINCIPAL.actualizado = false;
                this.dispose();

            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al modificar!");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cedula = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        apellido = new javax.swing.JTextField();
        celular = new javax.swing.JTextField();
        direccion = new javax.swing.JTextField();
        nacimiento = new com.toedter.calendar.JDateChooser();
        email = new javax.swing.JTextField();
        ciudad = new javax.swing.JTextField();
        departamento = new javax.swing.JTextField();
        genero = new javax.swing.JTextField();
        jb_Ejecutar = new javax.swing.JButton();
        puesto = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(658, 437));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(638, 365));
        jPanel1.setMinimumSize(new java.awt.Dimension(638, 365));

        cedula.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        cedula.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cédula:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        cedula.setMinimumSize(new java.awt.Dimension(100, 70));
        cedula.setPreferredSize(new java.awt.Dimension(100, 70));
        cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cedulaKeyPressed(evt);
            }
        });

        nombre.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        nombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nombre:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombreKeyPressed(evt);
            }
        });

        apellido.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        apellido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Apellido:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        apellido.setPreferredSize(new java.awt.Dimension(64, 58));
        apellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apellidoActionPerformed(evt);
            }
        });
        apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                apellidoKeyPressed(evt);
            }
        });

        celular.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        celular.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "N°. Celular:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        celular.setPreferredSize(new java.awt.Dimension(64, 58));
        celular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                celularKeyPressed(evt);
            }
        });

        direccion.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        direccion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dirección:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        direccion.setPreferredSize(new java.awt.Dimension(64, 58));
        direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                direccionKeyPressed(evt);
            }
        });

        nacimiento.setBackground(new java.awt.Color(255, 255, 255));
        nacimiento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fecha de nacimiento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        nacimiento.setDateFormatString("yyyy-MM-dd");
        nacimiento.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N

        email.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        email.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        email.setPreferredSize(new java.awt.Dimension(64, 58));
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emailKeyPressed(evt);
            }
        });

        ciudad.setEditable(false);
        ciudad.setBackground(new java.awt.Color(255, 255, 255));
        ciudad.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        ciudad.setForeground(new java.awt.Color(0, 153, 153));
        ciudad.setText("Seleccionar...");
        ciudad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ciudad:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        ciudad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ciudad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ciudadMouseClicked(evt);
            }
        });
        ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ciudadActionPerformed(evt);
            }
        });
        ciudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ciudadKeyPressed(evt);
            }
        });

        departamento.setEditable(false);
        departamento.setBackground(new java.awt.Color(255, 255, 255));
        departamento.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        departamento.setForeground(new java.awt.Color(0, 153, 153));
        departamento.setText("Seleccionar...");
        departamento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Departamento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        departamento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        departamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                departamentoMouseClicked(evt);
            }
        });
        departamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departamentoActionPerformed(evt);
            }
        });
        departamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                departamentoKeyPressed(evt);
            }
        });

        genero.setEditable(false);
        genero.setBackground(new java.awt.Color(255, 255, 255));
        genero.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        genero.setForeground(new java.awt.Color(0, 153, 153));
        genero.setText("Seleccionar...");
        genero.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Género:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        genero.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        genero.setMinimumSize(new java.awt.Dimension(201, 52));
        genero.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generoMouseClicked(evt);
            }
        });
        genero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                generoKeyPressed(evt);
            }
        });

        jb_Ejecutar.setBackground(new java.awt.Color(51, 51, 51));
        jb_Ejecutar.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        jb_Ejecutar.setForeground(new java.awt.Color(255, 255, 255));
        jb_Ejecutar.setText("¡Registrar!");
        jb_Ejecutar.setBorder(null);
        jb_Ejecutar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_EjecutarActionPerformed(evt);
            }
        });

        puesto.setEditable(false);
        puesto.setBackground(new java.awt.Color(255, 255, 255));
        puesto.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        puesto.setForeground(new java.awt.Color(0, 153, 153));
        puesto.setText("Seleccionar...");
        puesto.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Puesto:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        puesto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        puesto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                puestoMouseClicked(evt);
            }
        });
        puesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puestoActionPerformed(evt);
            }
        });
        puesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                puestoKeyPressed(evt);
            }
        });

        password.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contraseña:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(genero, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(celular, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(puesto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(178, 178, 178))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(249, 249, 249))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genero, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celular, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departamento, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(puesto, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jp_1.setBackground(new java.awt.Color(51, 51, 51));
        jp_1.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_cerrar.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jl_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        jl_cerrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_cerrar.setText("×");
        jl_cerrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jl_cerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jl_cerrarMouseClicked(evt);
            }
        });

        jl_titulo.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo.setText("Registrar empleado");

        javax.swing.GroupLayout jp_1Layout = new javax.swing.GroupLayout(jp_1);
        jp_1.setLayout(jp_1Layout);
        jp_1Layout.setHorizontalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 361, Short.MAX_VALUE)
                .addComponent(jl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jp_1Layout.setVerticalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_cerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
            .addComponent(jl_titulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        try {
            Date fecha = nacimiento.getDate();
            d = fecha.getTime();
            if (cedula.getText().equals("")||nombre.getText().equals("")||apellido.getText().equals("")||genero.getText().equals(FK)||celular.getText().equals("")||
                    email.getText().equals("") || direccion.getText().equals("") || ciudad.getText().equals(FK)||departamento.getText().equals(FK)||puesto.getText().equals(FK)||password.getText().equals("")) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
            } else {
                boolean ced_corr = validar.V_cedula(cedula.getText());
                boolean nac_corr = validar.V_edad(nacimiento.getDate());
                boolean tel_corr = validar.V_telefono(celular.getText());
                boolean email_corr = validar.V_correo(email);
                if (ced_corr && nac_corr && tel_corr && email_corr) {
                    if (forma.equals("registrar")) {
                        registrar();
                    } else if(forma.equals("modificar")){
                        modificar();
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    if (!ced_corr) {
                        JOptionPane.showMessageDialog(null, "¡Cédula incorrecta!");
                    }
                    if (!nac_corr) {
                        JOptionPane.showMessageDialog(null, "¡Fecha de nacimiento incorrecta!");
                    }
                    if (!tel_corr) {
                        JOptionPane.showMessageDialog(null, "¡Número de teléfono incorrecto!");
                    }
                    if (!email_corr) {
                        JOptionPane.showMessageDialog(null, "¡Correo incorrecto!");
                    }
                }
            }
        } catch (Exception e) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Fecha de nacimiento incorrecta!");
        }


    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void cedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cedulaKeyPressed
        validar.V_numero(cedula,10);
    }//GEN-LAST:event_cedulaKeyPressed

    private void nombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreKeyPressed
        validar.V_letras(nombre,20);
    }//GEN-LAST:event_nombreKeyPressed

    private void apellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellidoKeyPressed
        validar.V_letras(apellido,20);
    }//GEN-LAST:event_apellidoKeyPressed

    private void celularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_celularKeyPressed
        validar.V_numero(celular,10);
        
    }//GEN-LAST:event_celularKeyPressed

    private void direccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccionKeyPressed
        validar.nombre_compuesto(direccion,80);
    }//GEN-LAST:event_direccionKeyPressed

    private void emailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailKeyPressed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void ciudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ciudadKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ciudadKeyPressed

    private void apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_apellidoActionPerformed

    private void departamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_departamentoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_departamentoKeyPressed

    private void generoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_generoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_generoKeyPressed

    private void ciudadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ciudadMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(6);
        this.setVisible(false);
    }//GEN-LAST:event_ciudadMouseClicked

    private void generoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generoMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(1);
        PRINCIPAL.PERSONAS.setSelectedIndex(1);
        this.setVisible(false);
    }//GEN-LAST:event_generoMouseClicked

    private void ciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ciudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ciudadActionPerformed

    private void departamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departamentoActionPerformed

    private void departamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_departamentoMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(2);
        this.setVisible(false);
    }//GEN-LAST:event_departamentoMouseClicked

    private void puestoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_puestoMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(2);
        this.setVisible(false);
    }//GEN-LAST:event_puestoMouseClicked

    private void puestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_puestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_puestoActionPerformed

    private void puestoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_puestoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_puestoKeyPressed

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        validar.V_contraseña(password, 30);
    }//GEN-LAST:event_passwordKeyPressed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFempleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFempleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFempleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFempleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFempleado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField apellido;
    public static javax.swing.JTextField cedula;
    public static javax.swing.JTextField celular;
    public static javax.swing.JTextField ciudad;
    public static javax.swing.JTextField departamento;
    public static javax.swing.JTextField direccion;
    public static javax.swing.JTextField email;
    public static javax.swing.JTextField genero;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    public static javax.swing.JPanel jp_1;
    public static com.toedter.calendar.JDateChooser nacimiento;
    public static javax.swing.JTextField nombre;
    public static javax.swing.JPasswordField password;
    public static javax.swing.JTextField puesto;
    // End of variables declaration//GEN-END:variables
}
