package centro_comercial;

import base_datos.base;
import clases.Producto;
import com.db4o.ObjectSet;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
//import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import otros.Codigos;
import otros.fechas;
import otros.validar;



public class JFproducto extends javax.swing.JFrame {
    
    public static ObjectSet resultado;
    public static String url;
    public static double xprecio;
    public static String forma = "registrar";

    public JFproducto() {
        initComponents();
        setLocationRelativeTo(null);

        iniciar();
    }

    public static void iniciar() {
        ((JSpinner.DefaultEditor) jt_existencias.getEditor()).getTextField().setEditable(false);
        jt_existencias.setBackground(Color.white);
        jlCodigo.setVisible(false);
        codigo.setVisible(false);

    }

    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            color = new Color(0,204,102);
            codigo.setVisible(false);
            jlCodigo.setVisible(false);
            jl_titulo.setText("Registrar producto");
            jb_Ejecutar.setText("¡Registrar!");

        } else if(forma.equals("modificar")){
            color = new Color(0, 153, 255);
            codigo.setVisible(true);
            jlCodigo.setVisible(true);
            jl_titulo.setText("Modificar producto");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 6; i++) {
            switch (i) {
                case 1:
                    titulo = "Nombre:";
                    break;
                case 2:
                    titulo = "Precio:";
                    break;
                case 3:
                    titulo = "Existencias:";
                    break;
                case 4:
                    titulo = "Categoria:";
                    break;
                case 5:
                    titulo = "Proveedor:";
                    break;
                case 6:
                    titulo = "Subir imagen (opcional):";
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
                    jt_precio.setBorder(tb);
                    break;
                case 3:
                    jt_existencias.setBorder(tb);
                    break;
                case 4:
                    jt_categoria.setBorder(tb);
                    break;
                case 5:
                    jt_proveedor.setBorder(tb);
                    break;
                case 6:
                    ximagen.setText(titulo);
                    ximagen.setForeground(color);
                    break;
            }
        }
    }

    public static void limpiar() {
        jt_nombre.setText("");
        jt_precio.setText("");
        jt_proveedor.setText("¡Click para seleccionar!");
        jt_existencias.setValue(0);
        jt_categoria.setText("¡Click para seleccionar!");
        url = "";
        rsscalelabel.RSScaleLabel.setScaleLabel(jl_imagen, url);
    }

    public void llenar(int cod) {
        base.abrir();
        Producto p = new Producto(cod, null, 0, 0, null, null, null, null);
        resultado = base.gettear(p);
        if (resultado.isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Registro no existente!", null, JOptionPane.ERROR_MESSAGE);
        } else {
            codigo.setText(String.valueOf(p.getCodigo()));
            p = (Producto) resultado.next();
            jt_nombre.setText(p.getNombre());
            jt_precio.setText(String.valueOf(p.getPrecio()));
            jt_proveedor.setText(p.getRUC_prov());
            jt_existencias.setValue(p.getExistencias());
            jt_categoria.setText(p.getNombre_cat());
            url = p.getUrl_imagen();
            rsscalelabel.RSScaleLabel.setScaleLabel(jl_imagen,url);
            this.setVisible(true);
        }
        base.cerrar();
    }

    public void registrar() {
        base.abrir();
        Producto p = new Producto(Codigos.obtener_codigo("Producto"), jt_nombre.getText(), xprecio, Integer.parseInt(jt_existencias.getValue().toString()),
                jt_categoria.getText(), fechas.obtener_fecha(), jt_proveedor.getText(), url);
        base.settear(p);
        JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
        limpiar();
        SISTEMA.actualizado = false;
        this.dispose();
        base.cerrar();
    }

    public void modificar() {
        base.abrir();
        Producto p = new Producto(Integer.parseInt(codigo.getText()), null, 0, 0, null, null, null, null);
        resultado = base.gettear(p);
        if (!resultado.isEmpty()) {
            p = (Producto) resultado.next();
            p.setNombre(jt_nombre.getText());
            p.setPrecio(xprecio);
            p.setExistencias(Integer.parseInt(jt_existencias.getValue().toString()));
            p.setNombre_cat(jt_categoria.getText());
            p.setRUC_prov(jt_proveedor.getText());
            p.setUrl_imagen(url);
            base.settear(p);
            JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
            limpiar();
        }
        SISTEMA.actualizado = false;
        this.dispose();
        base.cerrar();
    }


   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();
        jp_2 = new javax.swing.JPanel();
        jt_nombre = new javax.swing.JTextField();
        jt_existencias = new javax.swing.JSpinner();
        jt_precio = new javax.swing.JTextField();
        jt_proveedor = new javax.swing.JTextField();
        jb_Ejecutar = new javax.swing.JButton();
        ximagen = new javax.swing.JLabel();
        jl_imagen = new javax.swing.JLabel();
        jl_subir_imagen = new javax.swing.JLabel();
        jlCodigo = new javax.swing.JLabel();
        codigo = new javax.swing.JLabel();
        jt_categoria = new javax.swing.JTextField();

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
        jl_titulo.setText("Registrar producto");

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

        jt_nombre.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_nombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nombre:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_nombreKeyPressed(evt);
            }
        });

        jt_existencias.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_existencias.setModel(new javax.swing.SpinnerNumberModel(0, 0, 200, 1));
        jt_existencias.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Existencias:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_existencias.setEditor(new javax.swing.JSpinner.NumberEditor(jt_existencias, ""));
        jt_existencias.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jt_existenciasStateChanged(evt);
            }
        });
        jt_existencias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_existenciasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jt_existenciasKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jt_existenciasKeyTyped(evt);
            }
        });

        jt_precio.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_precio.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Precio:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_precioKeyPressed(evt);
            }
        });

        jt_proveedor.setEditable(false);
        jt_proveedor.setBackground(new java.awt.Color(255, 255, 255));
        jt_proveedor.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_proveedor.setForeground(new java.awt.Color(0, 153, 153));
        jt_proveedor.setText("¡Click para seleccionar!");
        jt_proveedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedor:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_proveedorMouseClicked(evt);
            }
        });

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

        ximagen.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        ximagen.setForeground(new java.awt.Color(0, 204, 102));
        ximagen.setText("Subir imagen (opcional):");

        jl_imagen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jl_subir_imagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        jl_subir_imagen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jl_subir_imagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jl_subir_imagenMouseClicked(evt);
            }
        });

        jlCodigo.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlCodigo.setForeground(new java.awt.Color(0, 204, 102));
        jlCodigo.setText("Código:");

        codigo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        codigo.setText("000");

        jt_categoria.setEditable(false);
        jt_categoria.setBackground(new java.awt.Color(255, 255, 255));
        jt_categoria.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_categoria.setForeground(new java.awt.Color(0, 153, 153));
        jt_categoria.setText("¡Click para seleccionar!");
        jt_categoria.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Categoría:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_categoria.setPreferredSize(new java.awt.Dimension(64, 52));
        jt_categoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_categoriaMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jt_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jt_existencias, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jp_2Layout.createSequentialGroup()
                            .addComponent(jlCodigo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ximagen, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jl_imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(18, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jl_subir_imagen)
                        .addGap(65, 65, 65))))
        );
        jp_2Layout.setVerticalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addComponent(ximagen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jl_imagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jt_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jt_precio))
                        .addGap(15, 15, 15)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jt_existencias, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jt_proveedor))
                        .addGap(15, 15, 15)))
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_subir_imagen)
                    .addComponent(jt_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codigo)
                            .addComponent(jlCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jt_existenciasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jt_existenciasStateChanged

    }//GEN-LAST:event_jt_existenciasStateChanged

    private void jt_existenciasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasKeyPressed

    }//GEN-LAST:event_jt_existenciasKeyPressed

    private void jt_existenciasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasKeyReleased

    }//GEN-LAST:event_jt_existenciasKeyReleased

    private void jt_existenciasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasKeyTyped

    }//GEN-LAST:event_jt_existenciasKeyTyped

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        try {
            xprecio = Double.parseDouble(jt_precio.getText());
        } catch (NumberFormatException e) {
            xprecio = 0;
        }
        if (jt_nombre.getText().equals("") || jt_precio.getText().equals("") || jt_existencias.getValue().equals(0)
                ||jt_proveedor.getText().equals("¡Click para seleccionar!")||jt_categoria.getText().equals("¡Click para seleccionar!")) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            if (xprecio > 0) {
                if (forma.equals("registrar")) {
                    registrar();
                } else if(forma.equals("modificar")){
                    modificar();
                }
                this.dispose();
            } else {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡Precio inválido!");
                jt_precio.setText("");
            }
        }
        
    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void jl_subir_imagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_subir_imagenMouseClicked
        JFileChooser jf = new JFileChooser();
        jf.setMultiSelectionEnabled(false);
        jf.setDialogTitle("Seleccione una imagen");

        if (jf.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            url = jf.getSelectedFile().toString();
            rsscalelabel.RSScaleLabel.setScaleLabel(jl_imagen, url);
        }
    }//GEN-LAST:event_jl_subir_imagenMouseClicked

    private void jt_nombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_nombreKeyPressed
        validar.nombre_compuesto(jt_nombre,30);
    }//GEN-LAST:event_jt_nombreKeyPressed

    private void jt_precioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_precioKeyPressed
        validar.dinero(jt_precio,6);
    }//GEN-LAST:event_jt_precioKeyPressed

    private void jt_proveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_proveedorMouseClicked
        SISTEMA.MENU.setSelectedIndex(7);
        this.setVisible(false);
    }//GEN-LAST:event_jt_proveedorMouseClicked

    private void jt_categoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_categoriaMouseClicked
        SISTEMA.MENU.setSelectedIndex(1);
        this.setVisible(false);
    }//GEN-LAST:event_jt_categoriaMouseClicked

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
            java.util.logging.Logger.getLogger(JFproducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFproducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFproducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFproducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFproducto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel codigo;
    public static javax.swing.JButton jb_Ejecutar;
    private static javax.swing.JLabel jlCodigo;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_imagen;
    public javax.swing.JLabel jl_subir_imagen;
    public static javax.swing.JLabel jl_titulo;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField jt_categoria;
    public static javax.swing.JSpinner jt_existencias;
    public static javax.swing.JTextField jt_nombre;
    public static javax.swing.JTextField jt_precio;
    public static javax.swing.JTextField jt_proveedor;
    public static javax.swing.JLabel ximagen;
    // End of variables declaration//GEN-END:variables
}
