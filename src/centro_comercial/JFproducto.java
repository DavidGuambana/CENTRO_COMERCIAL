package centro_comercial;

import base_datos.conexion;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import otros.validar;



public class JFproducto extends javax.swing.JFrame {
    
    public static String forma = "registrar";
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static final String FK = "Seleccionar...";
    public static String url;
    public static double xprecio;
    
    public static int FK_cat, FK_mar;
    public static String FK_prov;
    public JFproducto() {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
    }

    public static void iniciar() {
        ((JSpinner.DefaultEditor) jt_existencias.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) jt_existenciasmax.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) jt_existenciasmin.getEditor()).getTextField().setEditable(false);
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
            jl_titulo.setText("Registrar producto");
            jb_Ejecutar.setText("¡Registrar!");

        } else if(forma.equals("modificar")){
            id.setVisible(true);
            jlid.setVisible(true);
            color = new Color(0, 153, 255);
            jl_titulo.setText("Modificar producto");
            jb_Ejecutar.setText("¡Modificar!");
        }
        jb_Ejecutar.setBackground(color);
        jp_1.setBackground(color);

        for (int i = 1; i <= 9; i++) {
            switch (i) {
                case 1:
                    titulo = "Nombre:";
                    break;
                case 2:
                    titulo = "Proveedor:";
                    break;
                case 3:
                    titulo = "Categoría:";
                    break;
                case 4:
                    titulo = "Marca:";
                    break;
                case 5:
                    titulo = "Precio:";
                    break;
                case 6:
                    titulo = "Existencias:";
                    break;
                case 7:
                    titulo = "Existencias MAX:";
                    break;
                case 8:
                    titulo = "Existencias MIN:";
                    break;
                case 9:
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
                    jt_proveedor.setBorder(tb);
                    break;
                case 3:
                    jt_categoria.setBorder(tb);
                    break;
                case 4:
                    jt_marca.setBorder(tb);
                    break;
                case 5:
                    jt_precio.setBorder(tb);
                    break;
                case 6:
                    jt_existencias.setBorder(tb);
                    break;
                case 7:
                    jt_existenciasmax.setBorder(tb);
                    break;
                case 8:
                    jt_existenciasmin.setBorder(tb);
                    break;
                case 9:
                    ximagen.setText(titulo);
                    ximagen.setForeground(color);
                    break;
            }
        }
    }

    public static void limpiar() {
        jt_nombre.setText("");
        jt_precio.setText("");
        jt_proveedor.setText(FK);
        jt_existencias.setValue(0);
        jt_existenciasmax.setValue(0);
        jt_existenciasmin.setValue(0);
        jt_categoria.setText(FK);
        jt_marca.setText(FK);
        url = "";
        rsscalelabel.RSScaleLabel.setScaleLabel(jl_imagen, url);
    }

    public void llenar(int PK) {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PRODUCTO WHERE CODIGO=" + PK);
                rs = ps.executeQuery();
                rs.next();
                id.setText(""+rs.getInt(1));
                jt_nombre.setText(rs.getString(2));
                url =rs.getString(3);
                rsscalelabel.RSScaleLabel.setScaleLabel(jl_imagen,url);
                FK_mar = rs.getInt(4);
                jt_precio.setText(""+rs.getDouble(5));
                jt_existenciasmax.setValue(rs.getInt(6));
                jt_existenciasmin.setValue(rs.getInt(7));
                jt_existencias.setValue(rs.getInt(8));
                FK_cat = rs.getInt(9);
                FK_prov = rs.getString(11);
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM MARCA WHERE ID=" + FK_mar);
                rs = ps.executeQuery();
                rs.next();
                jt_marca.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CATEGORIA WHERE ID=" + FK_cat);
                rs = ps.executeQuery();
                rs.next();
                jt_categoria.setText(""+rs.getInt(1)+" - "+rs.getString(2));
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PROVEEDOR WHERE RUC='" + FK_prov+"'");
                rs = ps.executeQuery();
                rs.next();
                jt_proveedor.setText(""+rs.getString(2));
                this.setVisible(true);
            } catch (SQLException e) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(rootPane, "¡El producto '" + PK + "' no existe!");
            }
        }
    }

    public void registrar() {
     con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO PRODUCTO (NOMBRE,URL_IMG,ID_MAR,PRECIO,EXIS_MAX,EXIS_MIN,STOK,ID_CAT,RUC_PROV) VALUES (?,?,?,?,?,?,?,?,?)");
                ps.setString(1, jt_nombre.getText().toUpperCase());
                ps.setString(2, url);
                ps.setInt(3, FK_mar);
                ps.setDouble(4, Double.parseDouble(jt_precio.getText()));
                ps.setInt(5, (int) jt_existenciasmax.getValue());
                ps.setInt(6, (int) jt_existenciasmin.getValue());
                ps.setInt(7, (int) jt_existencias.getValue());
                ps.setInt(8, FK_cat);
                ps.setString(9, FK_prov);
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
      

    public void modificar() {
    con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("UPDATE PRODUCTO SET NOMBRE=?,URL_IMG=?,ID_MAR=?,PRECIO=?,EXIS_MAX=?,EXIS_MIN=?,STOK=?,ID_CAT=?,RUC_PROV=? WHERE CODIGO=?");
                ps.setString(1, jt_nombre.getText().toUpperCase());
                ps.setString(2, url);
                ps.setInt(3, FK_mar);
                ps.setDouble(4, Double.parseDouble(jt_precio.getText()));
                ps.setInt(5, (int) jt_existenciasmax.getValue());
                ps.setInt(6, (int) jt_existenciasmin.getValue());
                ps.setInt(7, (int) jt_existencias.getValue());
                ps.setInt(8, FK_cat);
                ps.setString(9, FK_prov);
                ps.setInt(10,Integer.parseInt(id.getText()));
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
        jt_nombre = new javax.swing.JTextField();
        jt_existencias = new javax.swing.JSpinner();
        jt_precio = new javax.swing.JTextField();
        jt_proveedor = new javax.swing.JTextField();
        jb_Ejecutar = new javax.swing.JButton();
        ximagen = new javax.swing.JLabel();
        jl_imagen = new javax.swing.JLabel();
        jl_subir_imagen = new javax.swing.JLabel();
        jlid = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jt_categoria = new javax.swing.JTextField();
        jt_marca = new javax.swing.JTextField();
        jt_existenciasmin = new javax.swing.JSpinner();
        jt_existenciasmax = new javax.swing.JSpinner();

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
        jl_titulo.setText("Registrar producto");

        javax.swing.GroupLayout jp_1Layout = new javax.swing.GroupLayout(jp_1);
        jp_1.setLayout(jp_1Layout);
        jp_1Layout.setHorizontalGroup(
            jp_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jl_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
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
        jt_proveedor.setText("Seleccionar...");
        jt_proveedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedor:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_proveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jt_proveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_proveedorMouseClicked(evt);
            }
        });
        jt_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_proveedorActionPerformed(evt);
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

        jlid.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jlid.setForeground(new java.awt.Color(0, 204, 102));
        jlid.setText("Código:");

        id.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        id.setText("000");

        jt_categoria.setEditable(false);
        jt_categoria.setBackground(new java.awt.Color(255, 255, 255));
        jt_categoria.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_categoria.setForeground(new java.awt.Color(0, 153, 153));
        jt_categoria.setText("Seleccionar...");
        jt_categoria.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Categoría:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_categoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jt_categoria.setPreferredSize(new java.awt.Dimension(64, 52));
        jt_categoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_categoriaMouseClicked(evt);
            }
        });
        jt_categoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_categoriaActionPerformed(evt);
            }
        });

        jt_marca.setEditable(false);
        jt_marca.setBackground(new java.awt.Color(255, 255, 255));
        jt_marca.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jt_marca.setForeground(new java.awt.Color(0, 153, 153));
        jt_marca.setText("Seleccionar...");
        jt_marca.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Marca:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_marca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jt_marca.setPreferredSize(new java.awt.Dimension(64, 52));
        jt_marca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jt_marcaMouseClicked(evt);
            }
        });
        jt_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_marcaActionPerformed(evt);
            }
        });

        jt_existenciasmin.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_existenciasmin.setModel(new javax.swing.SpinnerNumberModel(0, 0, 200, 1));
        jt_existenciasmin.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Existencias MIN:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_existenciasmin.setEditor(new javax.swing.JSpinner.NumberEditor(jt_existenciasmin, ""));
        jt_existenciasmin.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jt_existenciasminStateChanged(evt);
            }
        });
        jt_existenciasmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_existenciasminKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jt_existenciasminKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jt_existenciasminKeyTyped(evt);
            }
        });

        jt_existenciasmax.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_existenciasmax.setModel(new javax.swing.SpinnerNumberModel(0, 0, 200, 1));
        jt_existenciasmax.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Existencias MAX:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(0, 204, 102))); // NOI18N
        jt_existenciasmax.setEditor(new javax.swing.JSpinner.NumberEditor(jt_existenciasmax, ""));
        jt_existenciasmax.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jt_existenciasmaxStateChanged(evt);
            }
        });
        jt_existenciasmax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_existenciasmaxKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jt_existenciasmaxKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jt_existenciasmaxKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jt_marca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jt_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jt_existencias, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                            .addComponent(jt_precio))
                        .addGap(20, 20, 20)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ximagen, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jl_imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jp_2Layout.createSequentialGroup()
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jt_existenciasmax)
                            .addComponent(jt_existenciasmin, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                                .addComponent(jlid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                                .addComponent(jl_subir_imagen)
                                .addGap(45, 45, 45)))))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(208, 208, 208))
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
                        .addGap(20, 20, 20)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jt_existencias, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jt_proveedor))
                        .addGap(20, 20, 20)))
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addComponent(jl_subir_imagen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(id)
                            .addComponent(jlid, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jt_categoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jt_existenciasmax, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jt_existenciasmin, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jt_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(30, 30, 30)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
            .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jp_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        if (jt_nombre.getText().equals("") || jt_precio.getText().equals("") || jt_existencias.getValue().equals(0) ||jt_existenciasmax.getValue().equals(0)||
                jt_existenciasmin.getValue().equals(0)||jt_proveedor.getText().equals(FK)||jt_categoria.getText().equals(FK)|| jt_marca.getText().equals(FK)) {
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
        SISTEMA.MENU.setSelectedIndex(5);
        this.setVisible(false);
    }//GEN-LAST:event_jt_proveedorMouseClicked

    private void jt_categoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_categoriaMouseClicked
        SISTEMA.MENU.setSelectedIndex(4);
        this.setVisible(false);
    }//GEN-LAST:event_jt_categoriaMouseClicked

    private void jt_marcaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jt_marcaMouseClicked
        // TODO add your handling code here:
        SISTEMA.MENU.setSelectedIndex(4);
        this.setVisible(false);
    }//GEN-LAST:event_jt_marcaMouseClicked

    private void jt_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_categoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_categoriaActionPerformed

    private void jt_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_proveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_proveedorActionPerformed

    private void jt_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_marcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_marcaActionPerformed

    private void jt_existenciasminStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jt_existenciasminStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasminStateChanged

    private void jt_existenciasminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasminKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasminKeyPressed

    private void jt_existenciasminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasminKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasminKeyReleased

    private void jt_existenciasminKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasminKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasminKeyTyped

    private void jt_existenciasmaxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jt_existenciasmaxStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasmaxStateChanged

    private void jt_existenciasmaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasmaxKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasmaxKeyPressed

    private void jt_existenciasmaxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasmaxKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasmaxKeyReleased

    private void jt_existenciasmaxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_existenciasmaxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_existenciasmaxKeyTyped

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
    public static javax.swing.JLabel id;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_imagen;
    public javax.swing.JLabel jl_subir_imagen;
    public static javax.swing.JLabel jl_titulo;
    private static javax.swing.JLabel jlid;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    public static javax.swing.JTextField jt_categoria;
    public static javax.swing.JSpinner jt_existencias;
    public static javax.swing.JSpinner jt_existenciasmax;
    public static javax.swing.JSpinner jt_existenciasmin;
    public static javax.swing.JTextField jt_marca;
    public static javax.swing.JTextField jt_nombre;
    public static javax.swing.JTextField jt_precio;
    public static javax.swing.JTextField jt_proveedor;
    public static javax.swing.JLabel ximagen;
    // End of variables declaration//GEN-END:variables
}
