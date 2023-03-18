package centro_comercial;
import base_datos.conexion;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import otros.validar;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class JFsucursal extends javax.swing.JFrame {

    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static int FK_ciu;
    public static final String FK = "Seleccionar...";
    
    public static String nombre_propio;
    public JFsucursal() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public static void cambiar_diseño() {
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            id.setVisible(false);
            jlid.setVisible(false);
            color = new Color(51,51,51);
            jl_titulo.setText("Registrar sucursal");
            jb_Ejecutar.setText("¡Registrar!");
        } else if(forma.equals("modificar")){
            id.setVisible(true);
            jlid.setVisible(true);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar sucursal");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);
        tb = new TitledBorder("Nombre:");
        tb.setTitleJustification(0);
        tb.setTitlePosition(1);
        tb.setTitleColor(color);
        tb.setTitleFont(font);
        jt_nombre.setBorder(tb);
        jt_ciudad.setBorder(tb);

    }
    public static void limpiar(){
        jt_nombre.setText("");
        jt_ciudad.setText("Seleccionar...");      
        id.setText("000");
        
    }

    public void llenar(int PK) {
        con =  (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM SUCURSAL WHERE ID=" + PK);
                rs = ps.executeQuery();
                rs.next();
                id.setText(""+rs.getInt(1));
                jt_nombre.setText(rs.getString(2).toUpperCase());
                FK_ciu = rs.getInt(3);
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD WHERE ID=" + FK_ciu);
                rs = ps.executeQuery();
                rs.next();
                jt_ciudad.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡La marca '" + PK + "' no existe!");
            }
        }
    }
    
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO `sucursal`(`NOMBRE`, `ID_CIU`) VALUES (?,?);");
                
                ps.setString(1,jt_nombre.getText().toUpperCase());
                ps.setInt(2, FK_ciu);
//                System.out.println(FK_ciu);
//                System.out.println(jt_nombre.getText().toUpperCase());
                ps.executeUpdate(); //Ejecuta la consulta
                JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                SISTEMA.actualizado = false;
                this.dispose();

            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al registrar!");
            }
        }
    }
    public void modificar(){
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                    
                    ps = (PreparedStatement) con.prepareStatement("UPDATE SUCURSAL SET NOMBRE=?,ID_CIU=? WHERE ID=?");
                    ps.setString(1,jt_nombre.getText().toUpperCase());
                    ps.setInt(2,FK_ciu);                  
                    ps.setInt(3, Integer.parseInt(id.getText()));
                    ps.executeUpdate(); //Ejecuta la consulta
                    JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
                    SISTEMA.actualizado = false;
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
        jlid = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jt_nombre = new javax.swing.JTextField();
        jt_ciudad = new javax.swing.JTextField();

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
        jl_titulo.setText("Registrar sucusal");

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
        jp_2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jp_2.add(jb_Ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 144, 42));

        jlid.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlid.setForeground(new java.awt.Color(0, 204, 102));
        jlid.setText("Código:");
        jp_2.add(jlid, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, -1, 29));

        id.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id.setText("000");
        jp_2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 70, -1));

        jt_nombre.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_nombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nombre:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_nombreActionPerformed(evt);
            }
        });
        jt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_nombreKeyPressed(evt);
            }
        });
        jp_2.add(jt_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, 75));

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
        jp_2.add(jt_ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 300, 70));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
            .addComponent(jp_2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jp_2, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
       
        if (jt_nombre.getText().equals("")||jt_ciudad.getText().equals(FK)) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            if (forma.equals("registrar")) {
                registrar();
            } else if(forma.equals("modificar")){
                modificar();
            }
        }

    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void jt_nombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_nombreKeyPressed
        validar.V_letras(jt_nombre,30);
    }//GEN-LAST:event_jt_nombreKeyPressed

    private void jt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_nombreActionPerformed

    private void jt_ciudadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_ciudadMouseClicked
        SISTEMA.MENU.setSelectedIndex(6);
        this.setVisible(false);
    }//GEN-LAST:event_jt_ciudadMouseClicked

    private void jt_ciudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_ciudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_ciudadActionPerformed

    private void jt_ciudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_ciudadKeyPressed

    }//GEN-LAST:event_jt_ciudadKeyPressed

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
            java.util.logging.Logger.getLogger(JFsucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFsucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFsucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFsucursal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFsucursal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel id;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    private static javax.swing.JLabel jlid;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField jt_ciudad;
    public static javax.swing.JTextField jt_nombre;
    // End of variables declaration//GEN-END:variables
}
