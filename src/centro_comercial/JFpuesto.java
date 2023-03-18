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

public class JFpuesto extends javax.swing.JFrame {

    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    
    public static String nombre_propio;

    public JFpuesto() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            id.setVisible(false);
            jlid.setVisible(false);
            color = new Color(51,51,51);
            jl_titulo.setText("Registrar puesto");
            jb_Ejecutar.setText("¡Registrar!");
        } else if(forma.equals("modificar")){
            id.setVisible(true);
            jlid.setVisible(true);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar puesto");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 2; i++) {
            switch (i) {
                case 1:
                    titulo = "Nombre:";
                    break;
                case 2:
                    titulo = "Descipción:";
                    break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    jt_nombre.setBorder(tb);
                    break;
                case 2:
                    jt_sueldo.setForeground(color);
                    break;
            }
        }
    }
    public static void limpiar(){
        jt_nombre.setText("");
        jt_sueldo.setText("");
    }

    public void llenar(String PK) {
        con =  (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PUESTO WHERE ID=" + PK);
                rs = ps.executeQuery();
                rs.next();
                id.setText(String.valueOf(""+rs.getInt(1)));
                jt_nombre.setText(rs.getString(2).toUpperCase());
                nombre_propio = rs.getString(2).toUpperCase();
                jt_sueldo.setText(String.valueOf(rs.getDouble(3)));
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡El puesto '" + PK + "' no existe!");
            }
        }
    }
    
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PUESTO WHERE NOMBRE='" + jt_nombre.getText().toUpperCase() + "'");
                if (ps.executeQuery().next()) {
                    JOptionPane.showMessageDialog(null, "¡El nombre del puesto '" + jt_nombre.getText().toUpperCase() + "' ya está en uso!");
                } else {
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO PUESTO (NOMBRE, SUELDO) VALUES (?,?)");
                    ps.setString(1, jt_nombre.getText().toUpperCase());
                    ps.setDouble(2, Double.parseDouble(jt_sueldo.getText()));
                    ps.executeUpdate(); //Ejecuta la consulta
                    JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                    SISTEMA.actualizado = false;
                    this.dispose();
                }
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
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PUESTO WHERE NOMBRE='" + jt_nombre.getText().toUpperCase()+"'");
                if (ps.executeQuery().next() && !jt_nombre.getText().toUpperCase().equals(nombre_propio)) {
                    JOptionPane.showMessageDialog(null, "¡El puesto '" + jt_nombre.getText().toUpperCase() + "' ya está en uso!");
                } else {
                    ps = (PreparedStatement) con.prepareStatement("UPDATE PUESTO SET NOMBRE=?,SUELDO=? WHERE ID=?");
                    ps.setString(1, jt_nombre.getText().toUpperCase());
                    ps.setString(2, jt_sueldo.getText().toUpperCase());
                    ps.setInt(3, Integer.parseInt(id.getText()));
                    ps.executeUpdate(); //Ejecuta la consulta
                    JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
                    SISTEMA.actualizado = false;
                    this.dispose();
                }
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
        jt_nombre = new javax.swing.JTextField();
        jlid = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jt_sueldo = new javax.swing.JTextField();

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
        jl_titulo.setText("Registrar departamento");

        javax.swing.GroupLayout jp_1Layout = new javax.swing.GroupLayout(jp_1);
        jp_1.setLayout(jp_1Layout);
        jp_1Layout.setHorizontalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
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

        jlid.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlid.setForeground(new java.awt.Color(0, 204, 102));
        jlid.setText("Código:");

        id.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id.setText("000");

        jt_sueldo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_sueldo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sueldo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_sueldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_sueldoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jt_sueldo))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addComponent(jlid)
                        .addGap(6, 6, 6)
                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(94, 94, 94))
        );
        jp_2Layout.setVerticalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jt_sueldo, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlid, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(id))
                .addGap(30, 30, 30)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

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
        String desc = jt_sueldo.getText().replaceAll("\\s+", "");
        if (jt_nombre.getText().equals("") || desc.equals("")) {
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

    private void jt_sueldoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_sueldoKeyPressed
        validar.dinero(jt_sueldo,6);
    }//GEN-LAST:event_jt_sueldoKeyPressed

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
            java.util.logging.Logger.getLogger(JFpuesto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFpuesto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFpuesto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFpuesto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    
      
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFpuesto().setVisible(true);
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
    public static javax.swing.JTextField jt_nombre;
    public static javax.swing.JTextField jt_sueldo;
    // End of variables declaration//GEN-END:variables
}
