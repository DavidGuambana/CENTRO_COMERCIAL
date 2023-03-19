package centro_comercial;
import base_datos.conexion;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.util.ArrayList;
import otros.validar;



public class JFpago_empleado extends javax.swing.JFrame {
    
    public static ArrayList <String> ciudades = new ArrayList();
    public static boolean copiado = false;
    
    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static final String FK = "Seleccionar...";
    public static int FK_paga, FK_recibe;
    public JFpago_empleado() {
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
            jl_titulo.setText("Registrar pago de sueldo");
            jb_Ejecutar.setText("¡Registrar!");
        } else if(forma.equals("modificar")){
            id.setVisible(true);
            jlid.setVisible(true);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar pago de sueldo");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 3; i++) {
            switch (i) {
                case 1:
                    titulo = "Paga:";
                    break;
                case 2:
                    titulo = "Recibe:";
                    break;
                case 3:
                    titulo = "Sueldo:";
                    break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    id_paga.setBorder(tb);
                    break;
                case 2:
                    id_recibe.setBorder(tb);
                    break;
                case 3:
                    totalsueldo.setBorder(tb);
                    break;
            }
        }
    }
    public static void limpiar(){
        id_recibe.setText(FK);
        id_paga.setText(FK);
        totalsueldo.setText("");
    }

    public void llenar(int PK) {
         con =  (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PAGO_EMPLEADO WHERE NUMERO=" + PK);
                rs = ps.executeQuery();
                rs.next();
                FK_paga = rs.getInt(2);
                FK_recibe = rs.getInt(3);
                id.setText("" + rs.getInt(1));
                totalsueldo.setText(String.valueOf(rs.getDouble(4)));
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE ID=" + FK_paga);
                rs = ps.executeQuery();
                rs.next();
                String cedula=rs.getString(2);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA=" +cedula);
                rs = ps.executeQuery();
                rs.next();
                id_paga.setText("" + PK + " - " + rs.getString(2));
                ///////////////////////////////////////
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE ID=" + FK_recibe);
                rs = ps.executeQuery();
                rs.next();
                 cedula=rs.getString(2);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA=" + cedula);
                rs = ps.executeQuery();
                rs.next();
                id_recibe.setText("" + PK + " - " + rs.getString(2));

             
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡El pago '" + PK + "' no existe!");
            }
        }
    }
    
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO PAGO_EMPLEADO (ID_REMITENTE, ID_DESTINATARIO, TOTAL) VALUES (?,?,?)");
                ps.setInt(1, FK_paga);
                ps.setInt(2, FK_recibe);
                ps.setDouble(3,Double.parseDouble(totalsueldo.getText()) );
                ps.executeUpdate(); //Ejecuta la consulta
                JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                PRINCIPAL.actualizado = false;
                this.dispose();

            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al registrar!");
            }
        }

    }
    public void modificar() {
        con = (Connection) conexion.conectar();
        try {

            ps = (PreparedStatement) con.prepareStatement("UPDATE PAGO_EMPLEADO SET ID_REMITENTE=?, ID_DESTINATARIO=?,TOTAL=?  WHERE NUMERO=?");

            ps.setInt(1, (FK_paga));
            ps.setInt(2, (FK_recibe));
            ps.setDouble(3, Double.parseDouble(totalsueldo.getText()));
            ps.setInt(4, Integer.parseInt(id.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
            PRINCIPAL.actualizado = false;
            this.dispose();

        } catch (SQLException ex) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Error al modificar!");
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
        id_paga = new javax.swing.JTextField();
        id_recibe = new javax.swing.JTextField();
        totalsueldo = new javax.swing.JTextField();

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
        jl_titulo.setText("Registrar pago a empleado");

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
        jp_2.add(jb_Ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 329, 150, 42));

        jlid.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlid.setForeground(new java.awt.Color(0, 204, 102));
        jlid.setText("Número de pago:");
        jp_2.add(jlid, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 280, -1, 29));

        id.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id.setText("000");
        jp_2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 280, 40, -1));

        id_paga.setEditable(false);
        id_paga.setBackground(new java.awt.Color(255, 255, 255));
        id_paga.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id_paga.setForeground(new java.awt.Color(0, 153, 153));
        id_paga.setText("Seleccionar...");
        id_paga.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Paga:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        id_paga.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        id_paga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id_pagaMouseClicked(evt);
            }
        });
        id_paga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_pagaActionPerformed(evt);
            }
        });
        id_paga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id_pagaKeyPressed(evt);
            }
        });
        jp_2.add(id_paga, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 361, 75));

        id_recibe.setEditable(false);
        id_recibe.setBackground(new java.awt.Color(255, 255, 255));
        id_recibe.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id_recibe.setForeground(new java.awt.Color(0, 153, 153));
        id_recibe.setText("Seleccionar...");
        id_recibe.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recibe:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        id_recibe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        id_recibe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                id_recibeMouseClicked(evt);
            }
        });
        id_recibe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id_recibeKeyPressed(evt);
            }
        });
        jp_2.add(id_recibe, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 360, 75));

        totalsueldo.setEditable(false);
        totalsueldo.setBackground(new java.awt.Color(255, 255, 255));
        totalsueldo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        totalsueldo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sueldo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        totalsueldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalsueldoKeyPressed(evt);
            }
        });
        jp_2.add(totalsueldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 204, 70));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                    .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        if (id_recibe.getText().equals(FK) || id_paga.getText().equals(FK)) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            if (forma.equals("registrar")) {
                registrar();
            } else if (forma.equals("modificar")) {
                //modificar(); no se puede modificar una transacción de dinero
            }
        }

    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void id_pagaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_pagaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_pagaKeyPressed

    private void id_pagaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id_pagaMouseClicked
       PRINCIPAL.MENU.setSelectedIndex(1);
        this.setVisible(false);
    }//GEN-LAST:event_id_pagaMouseClicked

    private void id_recibeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_id_recibeMouseClicked
        // TODO add your handling code here:
        PRINCIPAL.MENU.setSelectedIndex(1);
        this.setVisible(false);
    }//GEN-LAST:event_id_recibeMouseClicked

    private void id_recibeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_recibeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_recibeKeyPressed

    private void totalsueldoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalsueldoKeyPressed
        validar.dinero(totalsueldo,6);
    }//GEN-LAST:event_totalsueldoKeyPressed

    private void id_pagaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_pagaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_pagaActionPerformed

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
            java.util.logging.Logger.getLogger(JFpago_empleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFpago_empleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFpago_empleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFpago_empleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
    
       

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                new JFpago_empleado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel id;
    public static javax.swing.JTextField id_paga;
    public static javax.swing.JTextField id_recibe;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    private static javax.swing.JLabel jlid;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField totalsueldo;
    // End of variables declaration//GEN-END:variables
}
