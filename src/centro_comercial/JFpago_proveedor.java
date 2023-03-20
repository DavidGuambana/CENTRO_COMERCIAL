package centro_comercial;
import base_datos.conexion;
import javax.swing.JOptionPane;
import otros.validar;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import otros.BotonTabla;

public class JFpago_proveedor extends javax.swing.JFrame implements Runnable {
    public boolean actualizado = false;
    public static String forma;
    public static ResultSet rs, rs2;
    public static Connection con = null;
    public static PreparedStatement ps, ps2;
    public static DefaultTableModel tabla = null, tabla_detalle = null;
    public static String nombre_propio;
    public static String FK_emp;
    public static String FK_prov;
    public static ArrayList<String> detalles = new ArrayList<>();
    JButton boton1 = new JButton();
    double xtotal;
    
     public static int xcolum,xrow;
    public JFpago_proveedor() {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
    }
    public final void iniciar() {
        seleccionar();
        InsertarIcono(boton1, "/imagenes/elim.png");
        tabla_detalle = new DefaultTableModel(null, new Object[]{"C. Producto", "Descipción", "Cantidad", "Precio","Subtotal", "Aciones"});
        JTdetalles.setModel(tabla_detalle);
        visualizar(1);
        visualizar(2);
        Lprov.setVisible(false);
        Lemp.setVisible(false);
        Lpro.setVisible(false);
    }
    TableRowSorter sorter;
    //buscar registros de cualquier tabla:
    public void buscar(JTable tab, JTextField tex, int numero) {
        DefaultTableModel modelo = (DefaultTableModel) tab.getModel();
        sorter = new TableRowSorter<>(modelo);
        tab.setAutoCreateRowSorter(true);
        tab.setRowSorter(sorter);
        switch (numero) {
            case 1://proveedor
                sorter.setRowFilter(RowFilter.regexFilter(tex.getText(), 0, 1));
                break;
            case 2://empleado
                sorter.setRowFilter(RowFilter.regexFilter(tex.getText(), 0, 1,2,3));
                break;
            case 3://producto
                sorter.setRowFilter(RowFilter.regexFilter(tex.getText(), 0, 1,2,3));
                break;
        }

    }

    public void InsertarIcono(JButton bot, String ruta){ //insertar icono en boton:
        bot.setIcon(new javax.swing.ImageIcon(getClass().getResource(ruta)));
    }
    public static void limpiar(){
        proveedor.setText("");
        proveedor.setBackground(Color.red);
        empleado.setText("");
        empleado.setBackground(Color.red);
    }
    //cargar los datos en las tablas:
    public static void visualizar(int num) {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                switch (num) {
                    case 1://proveedores
                        String[] c_prov = {"RUC", "NOMBRE EMPRESA"};
                        tabla = new DefaultTableModel(null, c_prov);
                        ps = (PreparedStatement) con.prepareStatement("SELECT DISTINCT proveedor.RUC, proveedor.NOMBRE_EMPRESA FROM proveedor,producto WHERE proveedor.RUC=producto.RUC_PROV;");
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tabla.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                        }
                        JTprov.setModel(tabla);
                        break;
                    case 2://empleados
                        String[] c_emp = {"ID", "CÉDULA", "NOMBRE", "APELLIDO"};
                        tabla = new DefaultTableModel(null, c_emp);
                        ps = (PreparedStatement) con.prepareStatement("SELECT E.id, P.cedula, P.nombre, P.apellido from empleado E INNER JOIN persona P WHERE E.CEDULA_PER = P.CEDULA");
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)});
                        }
                        JTemp.setModel(tabla);
                        break;
                    case 3://productos
                        String[] c_pro = {"CÓDIGO", "NOMBRE", "PRECIO", "PROVEEDOR"};
                        tabla = new DefaultTableModel(null, c_pro);
                        ps = (PreparedStatement) con.prepareStatement("SELECT * from producto WHERE RUC_PROV =" + FK_prov);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(5), rs.getString(11)});
                        }
                        JTpro.setModel(tabla);
                        break;
                }
            } catch (Exception e) {
            }
        }
    }
    public void registrar() {
        if (con != null) {
            try {
                //registra el encabezado de la factura
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO encabezado_pp (RUC_PROV, TOTAL, ID_EMP) VALUES (?,?,?)");
                ps.setString(1, FK_prov);
                ps.setDouble(2, xtotal);
                ps.setInt(3,Integer.parseInt(FK_emp));
                ps.executeUpdate();
                //toma el último registro de encabezado
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM  encabezado_pp ORDER BY CODIGO DESC LIMIT 1");
                rs = ps.executeQuery();
                rs.next();
                //registra detalles de la factura
                for (int i = 0; i < JTdetalles.getRowCount(); i++) {
                    int xcodigo_pro = Integer.parseInt(JTdetalles.getValueAt(i, 0).toString());
                    int xcantidad = Integer.parseInt(JTdetalles.getValueAt(i, 2).toString());
                    double xsubtotal = Double.parseDouble(JTdetalles.getValueAt(i, 4).toString());
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO detalle_pp (CODIGO_PRO, CANTIDAD, SUBTOTOTAL, CODIGO_ENC_PP) VALUES (?,?,?,?)");
                    ps.setInt(1, xcodigo_pro);
                    ps.setInt(2, xcantidad);
                    ps.setDouble(3, xsubtotal);
                    ps.setInt(4, rs.getInt(1));
                    ps.executeUpdate();
                    ps2 = (PreparedStatement) con.prepareStatement("SELECT * FROM PRODUCTO WHERE CODIGO=" + xcodigo_pro);
                    rs2 = ps2.executeQuery();
                    rs2.next();
                    int cant = rs2.getInt(8) + xcantidad;
                    ps2 = (PreparedStatement) con.prepareStatement("UPDATE PRODUCTO SET STOK=? WHERE CODIGO=?");
                    ps2.setInt(1, cant);
                    ps2.setInt(2, xcodigo_pro);
                    ps2.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                this.dispose();
            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al registrar!");
            }
        }
    }

    public void seleccionar() {
        JTpro.addMouseListener(new MouseAdapter() { //productos
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 2) {
                    if (proveedor.getText().equals("")) {
                        getToolkit().beep();
                        JOptionPane.showMessageDialog(rootPane, "¡Primero seleccione un proveedor!");
                    } else {
                        boolean repetido = false;
                        int codigo_pro = Integer.parseInt(JTpro.getValueAt(JTpro.getSelectedRow(), 0).toString());
                        String nombre_pro = JTpro.getValueAt(JTpro.getSelectedRow(), 1).toString();
                        Double precio = Double.valueOf(JTpro.getValueAt(JTpro.getSelectedRow(), 2).toString());
                        for (int i = 0; i < detalles.size(); i++) {
                            if (detalles.get(i).equals(""+codigo_pro)) {
                                repetido = true;
                                break;
                            }
                        }
                        if (repetido) {
                            JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                        } else {
                            try {
                                int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
                                if (cantidad > 0) {
                                    JTdetalles.setDefaultRenderer(Object.class, new BotonTabla());
                                    Object detalle[] = {codigo_pro, nombre_pro, cantidad, precio, Math.round((cantidad * precio) * 100.0) / 100.0, boton1};
                                    detalles.add("" + codigo_pro);
                                    //-------------- Agrega un detalle a la tabla
                                    tabla_detalle.addRow(detalle);
                                    JTdetalles.setModel(tabla_detalle);
                                    //--------------- Actualiza variables
                                    xtotal += cantidad * precio;
                                    xtotal = Math.rint(xtotal * 100.0) / 100.0;
                                    total.setText("$" + xtotal);

                                } else {
                                    if (cantidad <= 0) {
                                        JOptionPane.showMessageDialog(null, "¡El mínimo de compra es de 1!", null, JOptionPane.WARNING_MESSAGE);
                                    }

                                }
                            } catch (HeadlessException | NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "¡Cantiadad inválida!", null, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
        JTprov.addMouseListener(new MouseAdapter() { //proveedor
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    FK_prov = JTprov.getValueAt(JTprov.getSelectedRow(), 0).toString();
                    proveedor.setText(JTprov.getValueAt(JTprov.getSelectedRow(), 1).toString());
                    proveedor.setBackground(Color.green);
                    visualizar(3);
                }
            }
        });
        JTemp.addMouseListener(new MouseAdapter() { //empleados
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    FK_emp = JTemp.getValueAt(JTemp.getSelectedRow(), 0).toString();
                    empleado.setText((JTemp.getValueAt(JTemp.getSelectedRow(), 2).toString())+" "+JTemp.getValueAt(JTemp.getSelectedRow(), 3).toString());
                    empleado.setBackground(Color.green);
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jp_1 = new javax.swing.JPanel();
        jl_cerrar = new javax.swing.JLabel();
        jl_titulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jp_2 = new javax.swing.JPanel();
        jb_Ejecutar = new javax.swing.JButton();
        proveedor = new javax.swing.JTextField();
        jl_titulo23 = new javax.swing.JLabel();
        Bprov = new javax.swing.JTextField();
        Lprov = new javax.swing.JLabel();
        jsTabla_ciu10 = new javax.swing.JScrollPane();
        JTprov = new javax.swing.JTable();
        jl_titulo25 = new javax.swing.JLabel();
        Bemp = new javax.swing.JTextField();
        jsTabla_ciu11 = new javax.swing.JScrollPane();
        JTemp = new javax.swing.JTable();
        Lemp = new javax.swing.JLabel();
        empleado = new javax.swing.JTextField();
        jsTabla_ciu12 = new javax.swing.JScrollPane();
        JTpro = new javax.swing.JTable();
        Bpro = new javax.swing.JTextField();
        Lpro = new javax.swing.JLabel();
        jl_titulo26 = new javax.swing.JLabel();
        jsTabla_ciu13 = new javax.swing.JScrollPane();
        JTdetalles = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });

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
        jl_titulo.setText("Registrar pago a proveedor");

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

        jp_2.setBackground(new java.awt.Color(221, 243, 221));
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
        jp_2.add(jb_Ejecutar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 730, 144, 42));

        proveedor.setEditable(false);
        proveedor.setBackground(new java.awt.Color(255, 255, 255));
        proveedor.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        proveedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Proveedor:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(51, 51, 51))); // NOI18N
        proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proveedorActionPerformed(evt);
            }
        });
        proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                proveedorKeyPressed(evt);
            }
        });
        jp_2.add(proveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(487, 97, 253, 70));

        jl_titulo23.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo23.setText("Proveedores disponibles");
        jl_titulo23.setIconTextGap(10);
        jl_titulo23.setVerifyInputWhenFocusTarget(false);
        jp_2.add(jl_titulo23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 390, -1));

        Bprov.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bprov.setText("Buscar");
        Bprov.setMinimumSize(new java.awt.Dimension(317, 31));
        Bprov.setPreferredSize(new java.awt.Dimension(317, 35));
        Bprov.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bprov.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bprov.setSelectionEnd(0);
        Bprov.setSelectionStart(0);
        Bprov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BprovMouseClicked(evt);
            }
        });
        Bprov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BprovKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BprovKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BprovKeyTyped(evt);
            }
        });
        jp_2.add(Bprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 390, -1));

        Lprov.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lprov.setForeground(new java.awt.Color(0, 102, 102));
        Lprov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lprov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lprov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lprov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LprovMouseClicked(evt);
            }
        });
        jp_2.add(Lprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(416, 70, -1, 35));

        jsTabla_ciu10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTprov = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTprov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTprov.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTprov.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTprov.setFocusable(false);
        JTprov.setGridColor(new java.awt.Color(255, 255, 255));
        JTprov.setOpaque(false);
        JTprov.setRowHeight(30);
        JTprov.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTprov.getTableHeader().setResizingAllowed(false);
        JTprov.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu10.setViewportView(JTprov);

        jp_2.add(jsTabla_ciu10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 105, 390, 122));

        jl_titulo25.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo25.setText("Empleados disponibles");
        jl_titulo25.setIconTextGap(10);
        jl_titulo25.setVerifyInputWhenFocusTarget(false);
        jp_2.add(jl_titulo25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 256, 390, -1));

        Bemp.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bemp.setText("Buscar");
        Bemp.setMinimumSize(new java.awt.Dimension(317, 31));
        Bemp.setPreferredSize(new java.awt.Dimension(317, 35));
        Bemp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bemp.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bemp.setSelectionEnd(0);
        Bemp.setSelectionStart(0);
        Bemp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BempMouseClicked(evt);
            }
        });
        Bemp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BempKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BempKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BempKeyTyped(evt);
            }
        });
        jp_2.add(Bemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 306, 390, -1));

        jsTabla_ciu11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTemp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTemp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTemp.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTemp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTemp.setFocusable(false);
        JTemp.setGridColor(new java.awt.Color(255, 255, 255));
        JTemp.setOpaque(false);
        JTemp.setRowHeight(30);
        JTemp.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTemp.getTableHeader().setResizingAllowed(false);
        JTemp.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu11.setViewportView(JTemp);

        jp_2.add(jsTabla_ciu11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 341, 390, 122));

        Lemp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lemp.setForeground(new java.awt.Color(0, 102, 102));
        Lemp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lemp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lemp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LempMouseClicked(evt);
            }
        });
        jp_2.add(Lemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(416, 306, -1, 35));

        empleado.setEditable(false);
        empleado.setBackground(new java.awt.Color(255, 255, 255));
        empleado.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        empleado.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Empleado:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(51, 51, 51))); // NOI18N
        empleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empleadoActionPerformed(evt);
            }
        });
        empleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                empleadoKeyPressed(evt);
            }
        });
        jp_2.add(empleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(487, 333, 253, 70));

        jsTabla_ciu12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpro = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpro.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTpro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTpro.setFocusable(false);
        JTpro.setGridColor(new java.awt.Color(255, 255, 255));
        JTpro.setOpaque(false);
        JTpro.setRowHeight(30);
        JTpro.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTpro.getTableHeader().setResizingAllowed(false);
        JTpro.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu12.setViewportView(JTpro);

        jp_2.add(jsTabla_ciu12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 575, 352, 122));

        Bpro.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bpro.setText("Buscar");
        Bpro.setMinimumSize(new java.awt.Dimension(317, 31));
        Bpro.setPreferredSize(new java.awt.Dimension(317, 35));
        Bpro.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bpro.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bpro.setSelectionEnd(0);
        Bpro.setSelectionStart(0);
        Bpro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BproMouseClicked(evt);
            }
        });
        Bpro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BproKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BproKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BproKeyTyped(evt);
            }
        });
        jp_2.add(Bpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 352, -1));

        Lpro.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lpro.setForeground(new java.awt.Color(0, 102, 102));
        Lpro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lpro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lpro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lpro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LproMouseClicked(evt);
            }
        });
        jp_2.add(Lpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(378, 540, -1, 35));

        jl_titulo26.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo26.setText("Productos disponibles");
        jl_titulo26.setIconTextGap(10);
        jl_titulo26.setVerifyInputWhenFocusTarget(false);
        jp_2.add(jl_titulo26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 390, -1));

        jsTabla_ciu13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdetalles = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdetalles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdetalles.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTdetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdetalles.setFocusable(false);
        JTdetalles.setGridColor(new java.awt.Color(255, 255, 255));
        JTdetalles.setOpaque(false);
        JTdetalles.setRowHeight(30);
        JTdetalles.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdetalles.getTableHeader().setResizingAllowed(false);
        JTdetalles.getTableHeader().setReorderingAllowed(false);
        JTdetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTdetallesMouseClicked(evt);
            }
        });
        jsTabla_ciu13.setViewportView(JTdetalles);

        jp_2.add(jsTabla_ciu13, new org.netbeans.lib.awtextra.AbsoluteConstraints(422, 540, 318, 157));

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Detalles:");
        jp_2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(422, 520, 318, -1));

        total.setEditable(false);
        total.setBackground(new java.awt.Color(255, 255, 255));
        total.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        total.setText("$0");
        total.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(51, 51, 51))); // NOI18N
        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });
        total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalKeyPressed(evt);
            }
        });
        jp_2.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(606, 697, 134, 60));

        jScrollPane1.setViewportView(jp_2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jp_1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
       
        if (proveedor.getText().equals("") || empleado.getText().equals("")||xtotal<=0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
        } else {
            registrar();
        }

    }//GEN-LAST:event_jb_EjecutarActionPerformed

    private void proveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proveedorKeyPressed
        validar.V_letras(proveedor,30);
    }//GEN-LAST:event_proveedorKeyPressed

    private void proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proveedorActionPerformed

    private void BprovMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BprovMouseClicked
        if (Bprov.getText().equals("Buscar")) {
            Bprov.select(0, 0);
        }
    }//GEN-LAST:event_BprovMouseClicked

    private void BprovKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyPressed
        if (Bprov.getText().equals("Buscar")) {
            Bprov.setText("");
            Lprov.setVisible(true);
        }
    }//GEN-LAST:event_BprovKeyPressed

    private void BprovKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyReleased
        if (!Bprov.getText().equals("")) {
            buscar(JTprov, Bprov, 1);
        } else {
            Lprov.setVisible(false);
            Bprov.setText("Buscar");
            Bprov.select(0, 0);
            visualizar(1);
        }
    }//GEN-LAST:event_BprovKeyReleased

    private void BprovKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovKeyTyped

    private void LprovMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LprovMouseClicked
        Lprov.setVisible(false);
        Bprov.setText("Buscar");
        Bprov.select(0, 0);
        visualizar(1);
    }//GEN-LAST:event_LprovMouseClicked

    private void BempMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BempMouseClicked
        if (Bemp.getText().equals("Buscar")) {
            Bemp.select(0, 0);
        }
    }//GEN-LAST:event_BempMouseClicked

    private void BempKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyPressed
        if (Bemp.getText().equals("Buscar")) {
            Bemp.setText("");
            Lemp.setVisible(true);
        }
    }//GEN-LAST:event_BempKeyPressed

    private void BempKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyReleased
        if (!Bemp.getText().equals("")) {
            buscar(JTemp, Bemp, 2);
        } else {
            Lemp.setVisible(false);
            Bemp.setText("Buscar");
            Bemp.select(0, 0);
            visualizar(2);
        }
    }//GEN-LAST:event_BempKeyReleased

    private void BempKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BempKeyTyped

    private void LempMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LempMouseClicked
        Lemp.setVisible(false);
        Bemp.setText("Buscar");
        Bemp.select(0, 0);
        visualizar(2);
    }//GEN-LAST:event_LempMouseClicked

    private void empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empleadoActionPerformed

    private void empleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_empleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_empleadoKeyPressed

    private void BproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BproMouseClicked
        if (Bpro.getText().equals("Buscar")) {
            Bpro.select(0, 0);
        }
    }//GEN-LAST:event_BproMouseClicked

    private void BproKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyPressed
        if (Bpro.getText().equals("Buscar")) {
            Bpro.setText("");
            Lpro.setVisible(true);
        }
    }//GEN-LAST:event_BproKeyPressed

    private void BproKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyReleased
        if (!Bpro.getText().equals("")) {
            buscar(JTpro, Bpro, 3);
        } else {
            Lpro.setVisible(false);
            Bpro.setText("Buscar");
            Bpro.select(0, 0);
            visualizar(3);
        }
    }//GEN-LAST:event_BproKeyReleased

    private void BproKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BproKeyTyped

    private void LproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LproMouseClicked
        Lpro.setVisible(false);
        Bpro.setText("Buscar");
        Bpro.select(0, 0);
        visualizar(3);
    }//GEN-LAST:event_LproMouseClicked

    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalActionPerformed

    private void totalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalKeyPressed

    private void JTdetallesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTdetallesMouseClicked
        xcolum = JTdetalles.getColumnModel().getColumnIndexAtX(evt.getX());
        xrow = evt.getY() / JTdetalles.getRowHeight();
        
        if (xcolum <= JTdetalles.getColumnCount() && xcolum >= 0 && xrow <= JTdetalles.getRowCount() && xrow >= 0) {
            Object obj = JTdetalles.getValueAt(xrow, xcolum);
            if (obj instanceof JButton) {
                int valor = JOptionPane.showConfirmDialog(this, "¿Desea remover este producto?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (valor == JOptionPane.YES_OPTION) {
                    int codigo_pro = Integer.parseInt(JTdetalles.getValueAt(JTdetalles.getSelectedRow(), 0).toString());
                    double c6 = Double.parseDouble(JTdetalles.getValueAt(JTdetalles.getSelectedRow(), 4).toString());
                    xtotal -= c6;
                    xtotal = Math.rint(xtotal*100.0)/100.0; //deja al valor con dos decimales
                    total.setText("$"+xtotal);
                    
                    //----> Eliminar un detalle a la tabla:
                    tabla_detalle.removeRow(JTdetalles.getSelectedRow());
                    JTdetalles.setModel(tabla_detalle);
                    //-- > Deseleccinar al producto eliminado
                    for (int i = 0; i < detalles.size(); i++) {
                        if (detalles.get(i).equals(String.valueOf(codigo_pro))) {
                            detalles.remove(String.valueOf(codigo_pro));
                            i = detalles.size();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "¡Producto '" + codigo_pro + "' removido!", null, JOptionPane.INFORMATION_MESSAGE);
                    
                }
            }
        }
    }//GEN-LAST:event_JTdetallesMouseClicked

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
         if (actualizado == false) {
            visualizar(1);
            visualizar(2);
            actualizado = true;
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        actualizado = false;
    }//GEN-LAST:event_formWindowLostFocus

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFpago_proveedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField Bemp;
    public static javax.swing.JTextField Bpro;
    public static javax.swing.JTextField Bprov;
    public static javax.swing.JTable JTdetalles;
    public static javax.swing.JTable JTemp;
    public static javax.swing.JTable JTpro;
    public static javax.swing.JTable JTprov;
    private javax.swing.JLabel Lemp;
    private javax.swing.JLabel Lpro;
    private javax.swing.JLabel Lprov;
    public static javax.swing.JTextField empleado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JButton jb_Ejecutar;
    private javax.swing.JLabel jl_cerrar;
    public static javax.swing.JLabel jl_titulo;
    private javax.swing.JLabel jl_titulo23;
    private javax.swing.JLabel jl_titulo25;
    private javax.swing.JLabel jl_titulo26;
    public static javax.swing.JPanel jp_1;
    private javax.swing.JPanel jp_2;
    private javax.swing.JScrollPane jsTabla_ciu10;
    private javax.swing.JScrollPane jsTabla_ciu11;
    private javax.swing.JScrollPane jsTabla_ciu12;
    private javax.swing.JScrollPane jsTabla_ciu13;
    public static javax.swing.JTextField proveedor;
    public static javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
