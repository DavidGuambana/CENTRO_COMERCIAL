package centro_comercial;
import base_datos.base;
import clases.Empleado;
import com.db4o.ObjectSet;
import java.awt.Color;
import java.util.Date;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.border.TitledBorder;
import otros.fechas;
import otros.validar;

public class JFempleado extends javax.swing.JFrame {
    public static String forma = "registrar";
    public ObjectSet resultado;
    Date fecha_nac;
    double xsueldo = 0;
    
    public JFempleado() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public static void cambiar_diseño() {
        String titulo = "";
        Color color = null;
        Font font = new Font("Yu Gothic UI Light", 0, 14);
        TitledBorder tb;
        if (forma.equals("registrar")) {
            cedula.setEditable(true);
            color = new Color(0, 204, 102);
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

        for (int i = 1; i <= 9; i++) {
            switch (i) {
                case 1:
                    titulo = "Cédula:";
                    break;
                case 2:
                    titulo = "Nombre:";
                    break;
                case 3:
                    titulo = "Apellido:";
                    break;
                case 4:
                    titulo = "Fecha de nacimiento:";
                    break;
                case 5:
                    titulo = "Género:";
                    break;
                case 6:
                    titulo = "Sueldo de empleado:";
                    break;
                case 7:
                    titulo = "Teléfono:";
                    break;
                case 8:
                    titulo = "Correo:";
                    break;
                case 9:
                    titulo = "Dirección:";
                    break;
            }
            tb = new TitledBorder(titulo);
            tb.setTitleJustification(0);
            tb.setTitlePosition(1);
            tb.setTitleColor(color);
            tb.setTitleFont(font);
            switch (i) {
                case 1:
                    cedula.setBorder(tb);
                    break;
                case 2:
                    nombre.setBorder(tb);
                    break;
                case 3:
                    apellido.setBorder(tb);
                    break;
                case 4:
                    nacimiento.setBorder(tb);
                    break;
                case 5:
                    genero.setBorder(tb);
                    break;
                case 6:
                    sueldo.setBorder(tb);
                    break;
                case 7:
                    telefono.setBorder(tb);
                    break;
                case 8:
                    correo.setBorder(tb);
                    break;
                case 9:
                    direccion.setBorder(tb);
                    break;

            }
        }
    }
      
    public static void limpiar(){   
        cedula.setText("");
        nombre.setText("");
        apellido.setText("");
        nacimiento.setDate(null);
        genero.setSelectedIndex(0);
        sueldo.setText("");
        telefono.setText("");
        correo.setText("");
        direccion.setText("");
    }
    public void llenar(String ced){
        base.abrir();
        Empleado emp = new Empleado(0,ced, null, null, null, null, null, null, null, null);
        resultado = base.gettear(emp);
        if (resultado.isEmpty()) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡El empleado '"+ced+"' no existe!");
        } else{
            emp = (Empleado) resultado.next();
            cedula.setText(emp.getCedula());
            nombre.setText(emp.getNombre());
            apellido.setText(emp.getApellido());
            nacimiento.setDate(emp.getFecha_nac());
            genero.setSelectedItem(emp.getGenero());
            sueldo.setText(String.valueOf(emp.getSueldo()));
            telefono.setText(emp.getTelefono());
            correo.setText(emp.getCorreo());
            direccion.setText(emp.getDireccion());
            this.setVisible(true);
        }
        base.cerrar();
    }
    public void registrar() {
        base.abrir();
        Empleado emp = new Empleado(0, cedula.getText(), null, null, null, null, null, null, null, null);
        if (base.gettear(emp).isEmpty()) {
            emp = new Empleado(Double.parseDouble(sueldo.getText()), cedula.getText(), nombre.getText().toUpperCase(), apellido.getText().toUpperCase(),
                     direccion.getText().toUpperCase(), telefono.getText(), fecha_nac, fechas.obtener_fecha(), genero.getSelectedItem().toString(), correo.getText());
            base.settear(emp);
            JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
            SISTEMA.actualizado = false;
            this.dispose();
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡El empleado '" + cedula.getText() + "' ya existe!");
        }
        base.cerrar();
    }
    public void modificar() {
        base.abrir();
        Empleado emp = new Empleado(0, cedula.getText(), null, null, null, null, null, null, null, null);
        resultado = base.gettear(emp);
        if (!resultado.isEmpty()) {
            emp = (Empleado) resultado.next();
            emp.setNombre(nombre.getText().toUpperCase());
            emp.setApellido(apellido.getText().toUpperCase());
            emp.setFecha_nac(fecha_nac);
            emp.setGenero(genero.getSelectedItem().toString());          
            emp.setTelefono(telefono.getText());
            emp.setCorreo(correo.getText());
            emp.setDireccion(direccion.getText().toUpperCase());
            emp.setSueldo(Double.parseDouble(sueldo.getText()));
            base.settear(emp);
            JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
        }
        SISTEMA.actualizado = false;
        this.dispose();
        base.cerrar();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cedula = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        apellido = new javax.swing.JTextField();
        telefono = new javax.swing.JTextField();
        direccion = new javax.swing.JTextField();
        genero = new javax.swing.JComboBox<>();
        jb_Ejecutar = new javax.swing.JButton();
        nacimiento = new com.toedter.calendar.JDateChooser();
        correo = new javax.swing.JTextField();
        sueldo = new javax.swing.JTextField();
        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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
        apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                apellidoKeyPressed(evt);
            }
        });

        telefono.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        telefono.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Teléfono:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        telefono.setPreferredSize(new java.awt.Dimension(64, 58));
        telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                telefonoKeyPressed(evt);
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

        genero.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        genero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "FEMENINO", "MASCULINO" }));
        genero.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Género:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N

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

        nacimiento.setBackground(new java.awt.Color(255, 255, 255));
        nacimiento.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fecha de nacimiento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        nacimiento.setDateFormatString("yyyy-MM-dd");
        nacimiento.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N

        correo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        correo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Correo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        correo.setPreferredSize(new java.awt.Dimension(64, 58));
        correo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                correoActionPerformed(evt);
            }
        });
        correo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                correoKeyPressed(evt);
            }
        });

        sueldo.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        sueldo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suedo de empleado:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        sueldo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        sueldo.setPreferredSize(new java.awt.Dimension(64, 58));
        sueldo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sueldoMouseClicked(evt);
            }
        });
        sueldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sueldoActionPerformed(evt);
            }
        });
        sueldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sueldoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cedula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(telefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(correo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nombre)
                            .addComponent(genero, 0, 190, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sueldo, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                            .addComponent(apellido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(direccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cedula, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(genero, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sueldo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(correo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

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
        jl_titulo.setText("Registrar empleado");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
        try {
            xsueldo = Double.parseDouble(sueldo.getText());
        } catch (NumberFormatException e) {
            xsueldo = 0;
        }
        try {           
            fecha_nac = nacimiento.getDate();
            if (cedula.getText().equals("") || nombre.getText().equals("") || apellido.getText().equals("")
                    || genero.getSelectedIndex() == 0 || sueldo.getText().equals("")
                    || telefono.getText().equals("") || correo.getText().equals("") || direccion.getText().equals("")) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
            } else {
                boolean ced_corr = validar.V_cedula(cedula.getText());
                boolean nac_corr = validar.V_edad(nacimiento.getDate());
                boolean tel_corr = validar.V_telefono(telefono.getText());
                boolean email_corr = validar.V_correo(correo);
                
                if (ced_corr && nac_corr && tel_corr && email_corr && xsueldo>0) {                
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
                    if (xsueldo==0) {
                        JOptionPane.showMessageDialog(null, "¡Sueldo incorrecto!");
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

    private void telefonoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telefonoKeyPressed
        validar.V_numero(telefono,10);
        
    }//GEN-LAST:event_telefonoKeyPressed

    private void direccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccionKeyPressed
        validar.nombre_compuesto(direccion,80);
    }//GEN-LAST:event_direccionKeyPressed

    private void correoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_correoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_correoKeyPressed

    private void correoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_correoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_correoActionPerformed

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void sueldoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sueldoKeyPressed
        validar.dinero(sueldo,6);
    }//GEN-LAST:event_sueldoKeyPressed

    private void sueldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sueldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sueldoActionPerformed

    private void sueldoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sueldoMouseClicked
        /*
        SISTEMA.MENU.setSelectedIndex(4);
        this.setVisible(false);
        */
    }//GEN-LAST:event_sueldoMouseClicked

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
    public static javax.swing.JTextField correo;
    public static javax.swing.JTextField direccion;
    public static javax.swing.JComboBox<String> genero;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    public static javax.swing.JPanel jp_1;
    public static com.toedter.calendar.JDateChooser nacimiento;
    public static javax.swing.JTextField nombre;
    public static javax.swing.JTextField sueldo;
    public static javax.swing.JTextField telefono;
    // End of variables declaration//GEN-END:variables
}
