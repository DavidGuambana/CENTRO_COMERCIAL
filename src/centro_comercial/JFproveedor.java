package centro_comercial;

import base_datos.conexion;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import otros.validar;



public class JFproveedor extends javax.swing.JFrame {
    
    public static String forma = "registrar";
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static final String FK = "Seleccionar...";
    public static int FK_ciu;
    
    public JFproveedor() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            jt_ruc.setEditable(true);
            color = new Color(51,51,51);
            jl_titulo.setText("Registrar proveedor");
            jb_Ejecutar.setText("¡Registrar!");
        } else if(forma.equals("modificar")){
            jt_ruc.setEditable(false);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar proveedor");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 5; i++) {
            switch (i) {
                case 1:
                    titulo = "RUC:";
                    break;
                case 2:
                    titulo = "Nombre de la empresa:";
                    break;
                case 3:
                    titulo = "Ciudad:";
                    break;
                case 4:
                    titulo = "N°. Celular:";
                    break;
                case 5:
                    titulo = "Email:";
                    break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    jt_ruc.setBorder(tb);
                    break;
                case 2:
                    jt_nombre.setBorder(tb);
                    break;
                case 3:
                    jt_ciudad.setBorder(tb);
                    break;
                case 4:
                    jt_celular.setBorder(tb);
                    break;
                case 5:
                    jt_email.setBorder(tb);
                    break;
            }
        }
    }
    public static void limpiar(){
        jt_ruc.setText("");
        jt_celular.setText("");
        jt_nombre.setText("");
        jt_ciudad.setText(FK);
        jt_email.setText("");
    }
    public void llenar(String PK){
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PROVEEDOR WHERE RUC=" + PK);
                rs = ps.executeQuery();
                rs.next();
                jt_ruc.setText(rs.getString(1));
                jt_nombre.setText(rs.getString(2));
                jt_celular.setText(rs.getString(3));
                jt_email.setText(rs.getString(4));
                FK_ciu = rs.getInt(5);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD WHERE ID=" + FK_ciu);
                rs = ps.executeQuery();
                rs.next();
                jt_ciudad.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡El proveedor '" + PK + "' no existe!");
            }
        }
    }
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PROVEEDOR WHERE RUC='" + jt_ruc.getText() + "'");
                rs = ps.executeQuery();
                if (rs.next()) {
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(rootPane, "¡El proveedor '" + jt_ruc.getText() + "' ya existe!");
                } else {
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO PROVEEDOR (RUC, NOMBRE_EMPRESA, CELULAR , EMAIL, ID_CIU) VALUES (?,?,?,?,?)");
                    ps.setString(1, jt_ruc.getText());
                    ps.setString(2, jt_nombre.getText().toUpperCase());
                    ps.setString(3, jt_celular.getText());
                    ps.setString(4, jt_email.getText());
                    ps.setInt(5, FK_ciu);
                    ps.executeUpdate(); //Ejecuta la consulta
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
                ps = (PreparedStatement) con.prepareStatement("UPDATE PROVEEDOR SET NOMBRE_EMPRESA=?,CELULAR=?,EMAIL=?,ID_CIU=? WHERE RUC=?");
                ps.setString(5, jt_ruc.getText());
                ps.setString(1, jt_nombre.getText().toUpperCase());
                ps.setString(2, jt_celular.getText());
                ps.setString(3, jt_email.getText());
                ps.setInt(4, FK_ciu);
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

        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();
        jp_2 = new javax.swing.JPanel();
        jb_Ejecutar = new javax.swing.JButton();
        jt_celular = new javax.swing.JTextField();
        jt_nombre = new javax.swing.JTextField();
        jt_ciudad = new javax.swing.JTextField();
        jt_ruc = new javax.swing.JTextField();
        jt_email = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jp_1.setBackground(new java.awt.Color(0, 204, 102));
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
        jl_titulo.setText("Registrar proveedor");

        javax.swing.GroupLayout jp_1Layout = new javax.swing.GroupLayout(jp_1);
        jp_1.setLayout(jp_1Layout);
        jp_1Layout.setHorizontalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jl_cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jp_1Layout.setVerticalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jl_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jl_cerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
        );

        jp_2.setBackground(new java.awt.Color(255, 255, 255));

        jb_Ejecutar.setBackground(new java.awt.Color(0, 204, 102));
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

        jt_celular.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_celular.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "N°. Celular:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_celular.setPreferredSize(new java.awt.Dimension(64, 58));
        jt_celular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_celularKeyPressed(evt);
            }
        });

        jt_nombre.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_nombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nombre de la empresa:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_nombreKeyPressed(evt);
            }
        });

        jt_ciudad.setEditable(false);
        jt_ciudad.setBackground(new java.awt.Color(255, 255, 255));
        jt_ciudad.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_ciudad.setForeground(new java.awt.Color(0, 153, 153));
        jt_ciudad.setText("Seleccionar...");
        jt_ciudad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ciudad:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_ciudad.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jt_ciudad.setPreferredSize(new java.awt.Dimension(64, 58));
        jt_ciudad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_ciudadMouseClicked(evt);
            }
        });
        jt_ciudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_ciudadActionPerformed(evt);
            }
        });
        jt_ciudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_ciudadKeyPressed(evt);
            }
        });

        jt_ruc.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_ruc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RUC:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_ruc.setMinimumSize(new java.awt.Dimension(100, 70));
        jt_ruc.setPreferredSize(new java.awt.Dimension(100, 70));
        jt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_rucKeyPressed(evt);
            }
        });

        jt_email.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_email.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Email:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_email.setPreferredSize(new java.awt.Dimension(64, 58));
        jt_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_emailKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jt_nombre, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jp_2Layout.createSequentialGroup()
                                    .addComponent(jt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(15, 15, 15)
                                    .addComponent(jt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jt_ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                        .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144))))
        );
        jp_2Layout.setVerticalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_celular, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jt_ciudad, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jp_1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
                    .addComponent(jp_2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        if (jt_ruc.getText().equals("") || jt_nombre.getText().equals("") || jt_celular.getText().equals("") || jt_ciudad.getText().equals(FK)) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            boolean ruc_corr = validar.V_RUC(jt_ruc.getText());
            boolean tel_corr = validar.V_telefono(jt_celular.getText());
            boolean email_corr = validar.V_correo(jt_email);
            if (ruc_corr && tel_corr && email_corr) {
                if (forma.equals("registrar")) {
                    registrar();
                } else if(forma.equals("modificar")){
                    modificar();
                }
            } else {
                getToolkit().beep();
                if (!ruc_corr) {
                    JOptionPane.showMessageDialog(null, "¡RUC incorrecto!");
                }
                if (!tel_corr) {
                    JOptionPane.showMessageDialog(null, "¡Número de celular incorrecto!");
                }
                if (!email_corr) {
                    JOptionPane.showMessageDialog(null, "¡Email incorrecto!");
                }
            }
        }

    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void jt_celularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_celularKeyPressed
        validar.V_numero(jt_celular,10);

    }//GEN-LAST:event_jt_celularKeyPressed

    private void jt_nombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_nombreKeyPressed
        validar.nombre_compuesto(jt_nombre,30);
    }//GEN-LAST:event_jt_nombreKeyPressed

    private void jt_ciudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_ciudadKeyPressed
        
    }//GEN-LAST:event_jt_ciudadKeyPressed

    private void jt_rucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_rucKeyPressed
        validar.V_numero(jt_ruc,13);
    }//GEN-LAST:event_jt_rucKeyPressed

    private void jt_emailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_emailKeyPressed
        
    }//GEN-LAST:event_jt_emailKeyPressed

    private void jt_ciudadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_ciudadMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(6);
        this.setVisible(false);
    }//GEN-LAST:event_jt_ciudadMouseClicked

    private void jt_ciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_ciudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_ciudadActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFproveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFproveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFproveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFproveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFproveedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField jt_celular;
    public static javax.swing.JTextField jt_ciudad;
    public static javax.swing.JTextField jt_email;
    public static javax.swing.JTextField jt_nombre;
    public static javax.swing.JTextField jt_ruc;
    // End of variables declaration//GEN-END:variables
}
