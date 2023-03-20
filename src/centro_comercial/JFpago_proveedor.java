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
import javax.swing.table.DefaultTableModel;
import otros.BotonTabla;

public class JFpago_proveedor extends javax.swing.JFrame implements Runnable {
    public boolean actualizado = false;
    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    public static DefaultTableModel tabla = null, tabla_detalle = null;
    public static String nombre_propio;
    public static String FK_emp;
    public static String FK_prov;
    ArrayList<String> detalles = new ArrayList<>();
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
                    JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                }
                for (int j = 1; j > JTdetalles.getRowCount(); j--) {
                        tabla_detalle.removeRow(j - 1);
                    }
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
        Bcat = new javax.swing.JTextField();
        Lcat = new javax.swing.JLabel();
        jsTabla_ciu10 = new javax.swing.JScrollPane();
        JTprov = new javax.swing.JTable();
        jl_titulo25 = new javax.swing.JLabel();
        Bcat2 = new javax.swing.JTextField();
        jsTabla_ciu11 = new javax.swing.JScrollPane();
        JTemp = new javax.swing.JTable();
        Lcat2 = new javax.swing.JLabel();
        empleado = new javax.swing.JTextField();
        jsTabla_ciu12 = new javax.swing.JScrollPane();
        JTpro = new javax.swing.JTable();
        Bcat3 = new javax.swing.JTextField();
        Lcat3 = new javax.swing.JLabel();
        jl_titulo26 = new javax.swing.JLabel();
        jsTabla_ciu13 = new javax.swing.JScrollPane();
        JTdetalles = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();

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

        jl_titulo23.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo23.setText("Proveedores disponibles");
        jl_titulo23.setIconTextGap(10);
        jl_titulo23.setVerifyInputWhenFocusTarget(false);

        Bcat.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bcat.setText("Buscar");
        Bcat.setMinimumSize(new java.awt.Dimension(317, 31));
        Bcat.setPreferredSize(new java.awt.Dimension(317, 35));
        Bcat.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bcat.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bcat.setSelectionEnd(0);
        Bcat.setSelectionStart(0);
        Bcat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BcatMouseClicked(evt);
            }
        });
        Bcat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BcatKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BcatKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BcatKeyTyped(evt);
            }
        });

        Lcat.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lcat.setForeground(new java.awt.Color(0, 102, 102));
        Lcat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lcat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lcat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lcat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LcatMouseClicked(evt);
            }
        });

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

        jl_titulo25.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo25.setText("Empleados disponibles");
        jl_titulo25.setIconTextGap(10);
        jl_titulo25.setVerifyInputWhenFocusTarget(false);

        Bcat2.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bcat2.setText("Buscar");
        Bcat2.setMinimumSize(new java.awt.Dimension(317, 31));
        Bcat2.setPreferredSize(new java.awt.Dimension(317, 35));
        Bcat2.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bcat2.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bcat2.setSelectionEnd(0);
        Bcat2.setSelectionStart(0);
        Bcat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Bcat2MouseClicked(evt);
            }
        });
        Bcat2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Bcat2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Bcat2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Bcat2KeyTyped(evt);
            }
        });

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

        Lcat2.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lcat2.setForeground(new java.awt.Color(0, 102, 102));
        Lcat2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lcat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lcat2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lcat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Lcat2MouseClicked(evt);
            }
        });

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

        Bcat3.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bcat3.setText("Buscar");
        Bcat3.setMinimumSize(new java.awt.Dimension(317, 31));
        Bcat3.setPreferredSize(new java.awt.Dimension(317, 35));
        Bcat3.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bcat3.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bcat3.setSelectionEnd(0);
        Bcat3.setSelectionStart(0);
        Bcat3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Bcat3MouseClicked(evt);
            }
        });
        Bcat3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Bcat3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Bcat3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                Bcat3KeyTyped(evt);
            }
        });

        Lcat3.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lcat3.setForeground(new java.awt.Color(0, 102, 102));
        Lcat3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lcat3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lcat3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lcat3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Lcat3MouseClicked(evt);
            }
        });

        jl_titulo26.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo26.setText("Productos disponibles");
        jl_titulo26.setIconTextGap(10);
        jl_titulo26.setVerifyInputWhenFocusTarget(false);

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

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Detalles:");

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

        javax.swing.GroupLayout jp_2Layout = new javax.swing.GroupLayout(jp_2);
        jp_2.setLayout(jp_2Layout);
        jp_2Layout.setHorizontalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(309, 309, 309))
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jl_titulo23, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jl_titulo25, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jp_2Layout.createSequentialGroup()
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jp_2Layout.createSequentialGroup()
                                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Bcat2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jsTabla_ciu11, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addComponent(Lcat2))
                                .addGroup(jp_2Layout.createSequentialGroup()
                                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Bcat, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jsTabla_ciu10, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addComponent(Lcat)))
                            .addGap(55, 55, 55)
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                                .addComponent(empleado)))
                        .addGroup(jp_2Layout.createSequentialGroup()
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jl_titulo26, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jp_2Layout.createSequentialGroup()
                                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Bcat3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jsTabla_ciu12, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)
                                    .addComponent(Lcat3)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jsTabla_ciu13, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(41, 41, 41))
        );
        jp_2Layout.setVerticalGroup(
            jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jl_titulo23)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jp_2Layout.createSequentialGroup()
                                .addComponent(Bcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jsTabla_ciu10, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Lcat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(jl_titulo25)
                .addGap(18, 18, 18)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addComponent(Bcat2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jsTabla_ciu11, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Lcat2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_titulo26)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1)))
                .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jp_2Layout.createSequentialGroup()
                        .addComponent(Bcat3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jsTabla_ciu12, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Lcat3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsTabla_ciu13, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jb_Ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );

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

    private void BcatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BcatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BcatMouseClicked

    private void BcatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BcatKeyPressed

    private void BcatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BcatKeyReleased

    private void BcatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BcatKeyTyped

    private void LcatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LcatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LcatMouseClicked

    private void Bcat2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Bcat2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat2MouseClicked

    private void Bcat2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat2KeyPressed

    private void Bcat2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat2KeyReleased

    private void Bcat2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat2KeyTyped

    private void Lcat2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Lcat2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Lcat2MouseClicked

    private void empleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empleadoActionPerformed

    private void empleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_empleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_empleadoKeyPressed

    private void Bcat3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Bcat3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat3MouseClicked

    private void Bcat3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat3KeyPressed

    private void Bcat3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat3KeyReleased

    private void Bcat3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Bcat3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_Bcat3KeyTyped

    private void Lcat3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Lcat3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Lcat3MouseClicked

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
    public static javax.swing.JTextField Bcat;
    public static javax.swing.JTextField Bcat2;
    public static javax.swing.JTextField Bcat3;
    public static javax.swing.JTable JTdetalles;
    public static javax.swing.JTable JTemp;
    public static javax.swing.JTable JTpro;
    public static javax.swing.JTable JTprov;
    private javax.swing.JLabel Lcat;
    private javax.swing.JLabel Lcat2;
    private javax.swing.JLabel Lcat3;
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
