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
import java.util.logging.Level;
import java.util.logging.Logger;



public class JFciudad extends javax.swing.JFrame {
    
    public static ArrayList <String> ciudades = new ArrayList();
    public static boolean copiado = false;
    
    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static final String FK = "Seleccionar...";
    public JFciudad() {
        initComponents();
        setLocationRelativeTo(null);
        cargar_ciudades();
    }

    public static void cargar_ciudades() {
        if (!copiado) {
            for (int i = 0; i < jc_ciudad.getItemCount(); i++) {
                ciudades.add(jc_ciudad.getItemAt(i));
            }
            copiado = true;
        } else {
            jc_ciudad.removeAllItems();
            for (int i = 0; i < ciudades.size(); i++) {
                jc_ciudad.addItem(ciudades.get(i));
            }
            con = (Connection) conexion.conectar();
            if (con != null) {
                try {
                    ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        for (int i = 0; i < jc_ciudad.getItemCount(); i++) {
                            if (rs.getString(2).equals(jc_ciudad.getItemAt(i))) {
                                jc_ciudad.removeItemAt(i);
                            }
                        }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JFciudad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static void cargar_ciudades_mod(String ciudad) {
        if (!copiado) {
            for (int i = 0; i < jc_ciudad.getItemCount(); i++) {
                ciudades.add(jc_ciudad.getItemAt(i));
            }
            copiado = true;
        } else {
            jc_ciudad.removeAllItems();
            for (int i = 0; i < ciudades.size(); i++) {
                jc_ciudad.addItem(ciudades.get(i));
            }

            con = (Connection) conexion.conectar();
            if (con != null) {
                try {
                    ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        for (int i = 0; i < jc_ciudad.getItemCount(); i++) {
                            if (rs.getString(2).equals(jc_ciudad.getItemAt(i)) && !jc_ciudad.getItemAt(i).equals(ciudad)) {
                                jc_ciudad.removeItemAt(i);
                            }
                        }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JFciudad.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

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
            jl_titulo.setText("Registrar ciudad");
            jb_Ejecutar.setText("¡Registrar!");
        } else if(forma.equals("modificar")){
            id.setVisible(true);
            jlid.setVisible(true);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar ciudad");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 2; i++) {
            switch (i) {
                case 1:
                    titulo = "Ciudad:";
                    break;
                case 2:
                    titulo = "ID provincia:";
                    break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    jc_ciudad.setBorder(tb);
                    break;
                case 2:
                    jt_provincia.setBorder(tb);
                    break;
            }
        }
    }
    public static void limpiar(){
        jc_ciudad.setSelectedIndex(0);
        jt_provincia.setText(FK);
    }

    public void llenar(int PK) {
        con =  (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CIUDAD WHERE ID=" + PK);
                rs = ps.executeQuery();
                rs.next();
                jc_ciudad.setSelectedItem(rs.getString(2));
                jt_provincia.setText(""+rs.getInt(3));
                id.setText(String.valueOf(""+rs.getInt(1)));
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡La ciudad '" + PK + "' no existe!");
            }
        }
    }
    
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO CIUDAD (NOMBRE, ID_PROVI) VALUES (?,?)");
                ps.setString(1, jc_ciudad.getSelectedItem().toString());
                ps.setInt(2, Integer.parseInt(jt_provincia.getText()));
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
                ps = (PreparedStatement) con.prepareStatement("UPDATE CIUDAD SET NOMBRE=?, ID_PROVI=? WHERE ID=?");
                ps.setString(1, jc_ciudad.getSelectedItem().toString());
                ps.setInt(2, Integer.parseInt(jt_provincia.getText()));
                ps.setInt(3, Integer.parseInt(id.getText()));
                ps.executeUpdate(); //ejecuta la consulta
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
        jc_ciudad = new javax.swing.JComboBox<>();
        jlid = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jt_provincia = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jp_1.setBackground(new java.awt.Color(0, 204, 102));
        jp_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
        jl_titulo.setText("Registrar ciudad");

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
        jp_2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
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
        jp_2.add(jb_Ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 263, 144, 42));

        jc_ciudad.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jc_ciudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "Alamor", "Alausí", "Alfredo Baquerizo Moreno", "Amaluza", "Ambato", "Arajuno", "Archidona", "Arenillas", "Atacames", "Atuntaqui", "Azogues", "Baba", "Babahoyo", "Baeza", "Bahía de Caráquez", "Balao", "Balsas", "Balzar", "Baños de Agua Santa", "Biblián", "Bolívar", "Buena Fe", "Calceta", "Caluma", "Camilo Ponce Enríquez", "Canton Guano", "Cañar", "Cariamanga", "Carlos Julio Arosemena Tola", "Catacocha", "Catamayo", "Catarama", "Cayambe", "Celica", "Cevallos", "Chaguarpamba", "Chambo", "Chilla", "Chillanes", "Chimbo", "Chone", "Chordeleg", "Chunchi", "Cnel. Marcelino Maridueña", "Colimes", "Cotacachi", "Cuenca", "Cumandá", "Daule", "Déleg", "Durán", "Echeandía", "El Ángel", "El Carmen", "El Chaco", "El Corazón", "El Dorado de Cascales", "El Guabo", "El Pan", "El Pangui", "El Tambo", "El Triunfo", "Esmeraldas", "Flavio Alfaro", "General Villamil", "Girón", "Gonzanamá", "Gral. Antonio Elizalde (Bucay)", "Gral. Leonidas Plaza Gutiérrez (Limón)", "Guachapala", "Gualaceo", "Gualaquiza", "Guamote", "Guaranda", "Guayaquil", "Guayzimi", "Huaca", "Huamboya", "Huaquillas", "Ibarra", "Isidro Ayora", "Jama", "Jaramijó", "Jipijapa", "Junín", "La Bonita", "La Concordia", "La Joya de los Sachas", "La Libertad", "La Maná", "La Troncal", "La Victoria", "Las Naves", "Latacunga", "Logroño", "Loja", "Lomas de Sargentillo", "Loreto", "Lumbaqui", "Macará", "Macas", "Machachi", "Machala", "Manta", "Marcabelí", "Mera", "Milagro", "Mira", "Mocache", "Mocha", "Montalvo", "Montecristi", "Muisne", "Nabón", "Naranjal", "Naranjito", "Nobol", "Nueva Loja", "Olmedo", "Olmedo", "Oña", "Otavalo", "Pablo Sexto", "Paccha", "Paján", "Palanda", "Palenque", "Palestina", "Pallatanga", "Palora", "Paquisha", "Pasaje", "Patate", "Paute", "Pedernales", "Pedro Carbo", "Pedro Vicente Maldonado", "Pelileo", "Penipe", "Pichincha", "Píllaro", "Pimampiro", "Pindal", "Piñas", "Portovelo", "Portoviejo", "Pucará", "Puebloviejo", "Puerto Ayora", "Puerto Baquerizo Moreno", "Puerto El Carmen de Putumayo", "Puerto Francisco de Orellana", "Puerto López", "Puerto Quito", "Puerto Villamil", "Pujilí", "Puyo", "Quero", "Quevedo", "Quilanga", "Quinsaloma", "Quito", "Riobamba", "Rioverde", "Rocafuerte", "Rosa Zárate", "Salinas", "Salitre", "Samborondón", "San Fernando", "San Gabriel", "San Juan Bosco", "San Lorenzo", "San Miguel", "San Miguel de Los Bancos", "San Miguel de Salcedo", "San Vicente", "Sangolquí", "Santa Ana", "Santa Clara", "Santa Elena", "Santa Isabel", "Santa Lucía", "Santa Rosa", "Santiago", "Santiago de Méndez", "Santo Domingo", "Saquisilí", "Saraguro", "Sevilla de Oro", "Shushufindi", "Sigchos", "Sígsig", "Simón Bolívar", "Sozoranga", "Sucre", "Sucúa", "Suscal", "Tabacundo", "Taisha", "Tarapoa", "Tena", "Tiputini", "Tisaleo", "Tosagua", "Tulcán", "Urcuquí", "Valdez (Limones)", "Valencia", "Velasco Ibarra", "Ventanas", "Villa La Unión (Cajabamba)", "Vinces", "Yacuambi", "Yaguachi", "Yantzaza", "Zamora", "Zapotillo", "Zaruma", "Zumba", "Zumbi" }));
        jc_ciudad.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ciudad:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jp_2.add(jc_ciudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 21, -1, 75));

        jlid.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlid.setForeground(new java.awt.Color(0, 204, 102));
        jlid.setText("Código:");
        jp_2.add(jlid, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 204, -1, 29));

        id.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id.setText("000");
        jp_2.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 204, 70, -1));

        jt_provincia.setEditable(false);
        jt_provincia.setBackground(new java.awt.Color(255, 255, 255));
        jt_provincia.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_provincia.setForeground(new java.awt.Color(0, 153, 153));
        jt_provincia.setText("Seleccionar...");
        jt_provincia.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ID provincia:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_provincia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jt_provincia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_provinciaMouseClicked(evt);
            }
        });
        jt_provincia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_provinciaKeyPressed(evt);
            }
        });
        jp_2.add(jt_provincia, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 111, 362, 75));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        if (jc_ciudad.getSelectedIndex() == 0 || jt_provincia.getText().equals(FK)) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            if (forma.equals("registrar")) {
                registrar();
            } else if (forma.equals("modificar")) {
                modificar();
            }
        }

    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void jt_provinciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_provinciaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_provinciaKeyPressed

    private void jt_provinciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_provinciaMouseClicked
        SISTEMA.MENU.setSelectedIndex(6);
        this.setVisible(false);
    }//GEN-LAST:event_jt_provinciaMouseClicked

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
            java.util.logging.Logger.getLogger(JFciudad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFciudad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFciudad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFciudad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFciudad().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel id;
    public static javax.swing.JButton jb_Ejecutar;
    public static javax.swing.JComboBox<String> jc_ciudad;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    private static javax.swing.JLabel jlid;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField jt_provincia;
    // End of variables declaration//GEN-END:variables
}
