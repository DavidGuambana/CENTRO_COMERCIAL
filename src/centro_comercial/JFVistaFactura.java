package centro_comercial;

public class JFVistaFactura extends javax.swing.JFrame {

    public JFVistaFactura() {
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        VISTA_FACTURA = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        VF_CODIGO = new javax.swing.JLabel();
        VF_FECHA = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        VF_NOMBRE_APELLIDO = new javax.swing.JLabel();
        VF_CEDULA = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        VF_DIRECCION = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        VF_TELEFONO = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        VF_CORREO = new javax.swing.JLabel();
        jsTabla_cat3 = new javax.swing.JScrollPane();
        VF_DETALLES = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        VF_TOTAL = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jlAgregar_fac = new javax.swing.JLabel();
        elim_iva = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        VISTA_FACTURA.setBackground(new java.awt.Color(255, 255, 255));
        VISTA_FACTURA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        VISTA_FACTURA.setMaximumSize(new java.awt.Dimension(485, 520));
        VISTA_FACTURA.setMinimumSize(new java.awt.Dimension(485, 520));
        VISTA_FACTURA.setPreferredSize(new java.awt.Dimension(485, 520));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel41.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel41.setText("F A C T U R A");

        jLabel42.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel42.setText("No.");

        VF_CODIGO.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        VF_CODIGO.setText(" ");

        VF_FECHA.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        VF_FECHA.setText(" ");

        jLabel44.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel44.setText("Fecha de emisión:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(3, 3, 3)
                                .addComponent(VF_CODIGO, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(86, 86, 86))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VF_FECHA, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel41)
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(VF_CODIGO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(VF_FECHA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel10.setPreferredSize(new java.awt.Dimension(2, 100));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel48.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel48.setText("Razon social:");
        jPanel10.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        VF_NOMBRE_APELLIDO.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_NOMBRE_APELLIDO.setText(" ");
        jPanel10.add(VF_NOMBRE_APELLIDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 40, 160, -1));

        VF_CEDULA.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_CEDULA.setText(" ");
        jPanel10.add(VF_CEDULA, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 90, -1));

        jLabel49.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel49.setText("Identificación:");
        jPanel10.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel50.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel50.setText("Dirección:");
        jPanel10.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, -1));

        VF_DIRECCION.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_DIRECCION.setText(" ");
        jPanel10.add(VF_DIRECCION, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 190, -1));

        jLabel51.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel51.setText("Teléfono:");
        jPanel10.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        VF_TELEFONO.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_TELEFONO.setText(" ");
        jPanel10.add(VF_TELEFONO, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 120, -1));

        jLabel53.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel53.setText("Correo:");
        jPanel10.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 60, -1, -1));

        VF_CORREO.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_CORREO.setText(" ");
        jPanel10.add(VF_CORREO, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 190, -1));

        VF_DETALLES = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        VF_DETALLES.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        VF_DETALLES.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        VF_DETALLES.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        VF_DETALLES.setFocusable(false);
        VF_DETALLES.setGridColor(new java.awt.Color(255, 255, 255));
        VF_DETALLES.setOpaque(false);
        VF_DETALLES.setRowHeight(30);
        VF_DETALLES.setSelectionBackground(new java.awt.Color(51, 51, 51));
        VF_DETALLES.getTableHeader().setResizingAllowed(false);
        VF_DETALLES.getTableHeader().setReorderingAllowed(false);
        VF_DETALLES.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VF_DETALLESMouseClicked(evt);
            }
        });
        jsTabla_cat3.setViewportView(VF_DETALLES);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel11.setPreferredSize(new java.awt.Dimension(2, 100));

        VF_TOTAL.setFont(new java.awt.Font("Calibri", 0, 20)); // NOI18N
        VF_TOTAL.setText("$0.00");

        jLabel52.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel52.setText("TOTAL PAGADO:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel52)
                .addGap(12, 12, 12)
                .addComponent(VF_TOTAL, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(VF_TOTAL))
                .addGap(10, 10, 10))
        );

        jlAgregar_fac.setFont(new java.awt.Font("Jokerman", 0, 14)); // NOI18N
        jlAgregar_fac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAgregar_fac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_fac.png"))); // NOI18N
        jlAgregar_fac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlAgregar_fac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAgregar_facMouseClicked(evt);
            }
        });

        elim_iva.setBackground(new java.awt.Color(255, 0, 51));
        elim_iva.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_iva.setForeground(new java.awt.Color(255, 255, 255));
        elim_iva.setText("x    Eliminar");
        elim_iva.setBorder(null);
        elim_iva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        javax.swing.GroupLayout VISTA_FACTURALayout = new javax.swing.GroupLayout(VISTA_FACTURA);
        VISTA_FACTURA.setLayout(VISTA_FACTURALayout);
        VISTA_FACTURALayout.setHorizontalGroup(
            VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jlAgregar_fac, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jsTabla_cat3, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(elim_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        VISTA_FACTURALayout.setVerticalGroup(
            VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlAgregar_fac, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jsTabla_cat3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(elim_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VISTA_FACTURA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VISTA_FACTURA, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void VF_DETALLESMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VF_DETALLESMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_VF_DETALLESMouseClicked

    private void jlAgregar_facMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAgregar_facMouseClicked
//        reiniciar_factura();
//        INICIO.setSelectedIndex(0);
    }//GEN-LAST:event_jlAgregar_facMouseClicked

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
            java.util.logging.Logger.getLogger(JFVistaFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFVistaFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFVistaFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFVistaFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFVistaFactura().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel VF_CEDULA;
    private javax.swing.JLabel VF_CODIGO;
    private javax.swing.JLabel VF_CORREO;
    public static javax.swing.JTable VF_DETALLES;
    private javax.swing.JLabel VF_DIRECCION;
    private javax.swing.JLabel VF_FECHA;
    private javax.swing.JLabel VF_NOMBRE_APELLIDO;
    private javax.swing.JLabel VF_TELEFONO;
    private javax.swing.JLabel VF_TOTAL;
    private javax.swing.JPanel VISTA_FACTURA;
    private javax.swing.JButton elim_iva;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlAgregar_fac;
    private javax.swing.JScrollPane jsTabla_cat3;
    // End of variables declaration//GEN-END:variables
}
