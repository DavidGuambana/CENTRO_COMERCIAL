package centro_comercial;
import base_datos.conexion;
import javax.swing.JOptionPane;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import otros.validar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class JFlogin extends javax.swing.JFrame {

    public static PRINCIPAL sesion = new PRINCIPAL();
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public JFlogin() {
        sesion.setVisible(false);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelbackround = new javax.swing.JPanel();
        panelesquina = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        labeltitulo1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labeltitulo = new javax.swing.JLabel();
        usuario = new javax.swing.JLabel();
        login = new javax.swing.JLabel();
        cedula = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        usuario1 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        panelbtnlog = new javax.swing.JPanel();
        labelbtnlogin = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelbackround.setBackground(new java.awt.Color(255, 255, 255));
        panelbackround.setMinimumSize(new java.awt.Dimension(750, 600));
        panelbackround.setPreferredSize(new java.awt.Dimension(750, 600));
        panelbackround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelesquina.setBackground(new java.awt.Color(255, 102, 51));
        panelesquina.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/centro-comercial.png"))); // NOI18N
        panelesquina.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 260, 230));

        labeltitulo1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        labeltitulo1.setForeground(new java.awt.Color(255, 255, 255));
        labeltitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeltitulo1.setText("CENTRO");
        panelesquina.add(labeltitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 210, 40));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        labeltitulo.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        labeltitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labeltitulo.setText("COMERCIAL");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labeltitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labeltitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
        );

        panelesquina.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 260, 40));

        panelbackround.add(panelesquina, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 300, 370));

        usuario.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        usuario.setText("Cédula de identidad:");
        panelbackround.add(usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, 20));

        login.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        login.setText("INICIAR SESIÓN");
        panelbackround.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));

        cedula.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cedula.setForeground(new java.awt.Color(51, 51, 51));
        cedula.setBorder(null);
        cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cedulaActionPerformed(evt);
            }
        });
        cedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cedulaKeyTyped(evt);
            }
        });
        panelbackround.add(cedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 400, 40));
        panelbackround.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 380, 30));

        usuario1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        usuario1.setText("Contraseña:");
        panelbackround.add(usuario1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, 20));

        password.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        password.setBorder(null);
        password.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordKeyTyped(evt);
            }
        });
        panelbackround.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 400, 40));

        panelbtnlog.setBackground(new java.awt.Color(255, 102, 51));

        labelbtnlogin.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        labelbtnlogin.setForeground(new java.awt.Color(255, 255, 255));
        labelbtnlogin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelbtnlogin.setText("INGRESAR");
        labelbtnlogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelbtnlogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelbtnloginMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelbtnlogLayout = new javax.swing.GroupLayout(panelbtnlog);
        panelbtnlog.setLayout(panelbtnlogLayout);
        panelbtnlogLayout.setHorizontalGroup(
            panelbtnlogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelbtnlogin, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );
        panelbtnlogLayout.setVerticalGroup(
            panelbtnlogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelbtnlogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        panelbackround.add(panelbtnlog, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 220, 50));
        panelbackround.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 380, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelbackround, javax.swing.GroupLayout.PREFERRED_SIZE, 772, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelbackround, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cedulaActionPerformed

    private void labelbtnloginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelbtnloginMouseClicked
        if (cedula.getText().isEmpty() || password.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "¡Aún hay campos por completar!");
        } else {
            con = (Connection) conexion.conectar();
            if (con != null) {
                try {
                    ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE CEDULA_PER='"+cedula.getText()+"'");
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        if (rs.getString(3).equals(password.getText())) {
                            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM DEPARTAMENTO WHERE ID="+rs.getInt(4));
                            rs = ps.executeQuery();
                            rs.next();
                            String departamento = "("+rs.getString(2)+")  -  ";
                            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA='"+cedula.getText()+"'");
                            rs = ps.executeQuery();
                            rs.next();
                            PRINCIPAL.usuario.setText(departamento+rs.getString(2)+" "+rs.getString(3));
                            sesion.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "¡Contraseña incorrecta!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "¡Usuario no registrado!");
                    }
                } catch (SQLException ex) {
                }
            }
        }
    }//GEN-LAST:event_labelbtnloginMouseClicked

    private void cedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cedulaKeyTyped
        // TODO add your handling code here:
        char validar = evt.getKeyChar();
        if (Character.isDigit(validar)) {
            if (cedula.getText().length() >= 10) {
                evt.consume();
            }
        } else {
            evt.consume();
        }
    }//GEN-LAST:event_cedulaKeyTyped

    private void passwordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyTyped
        
    }//GEN-LAST:event_passwordKeyTyped

    private void passwordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordKeyPressed
        validar.V_contraseña(password, 30);
    }//GEN-LAST:event_passwordKeyPressed


    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(JFlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new JFlogin().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cedula;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelbtnlogin;
    private javax.swing.JLabel labeltitulo;
    private javax.swing.JLabel labeltitulo1;
    private javax.swing.JLabel login;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel panelbackround;
    private javax.swing.JPanel panelbtnlog;
    private javax.swing.JPanel panelesquina;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel usuario;
    private javax.swing.JLabel usuario1;
    // End of variables declaration//GEN-END:variables
}
