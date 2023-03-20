package centro_comercial;
import base_datos.conexion;
import javax.swing.JOptionPane;
import otros.validar;
import java.sql.*;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import javax.swing.table.DefaultTableModel;

public class JFpago_proveedor extends javax.swing.JFrame {

    public static String forma;
    public static ResultSet rs;
    public static Connection con = null;
    public static PreparedStatement ps;
    DefaultTableModel tabla = null;
    public static String nombre_propio;
    public static int FK_prov, FK_emp;
    public JFpago_proveedor() {
        initComponents();
        setLocationRelativeTo(null);
//        visualizar();
    }
    
    public static void limpiar(){
        proveedor.setText("");
    }

     //cargar los datos en las tablas:
    public void visualizar(int num) {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                switch (num) {
                        case 1://proveedores
                            String[] c_prov = {"RUC", "NOMBRE EMPRESA"};
                            tabla = new DefaultTableModel(null, c_prov);
                            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PROVEEDOR");
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
                            ps = (PreparedStatement) con.prepareStatement("SELECT * from producto WHERE RUC_PROV ="+FK_prov);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4)});
                            }
                            JTprov.setModel(tabla);
                            break;
                    }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    
    public void registrar() {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CATEGORIA WHERE NOMBRE='" + proveedor.getText().toUpperCase() + "'");
                if (ps.executeQuery().next()) {
                    JOptionPane.showMessageDialog(null, "¡La categoría '" + proveedor.getText().toUpperCase() + "' ya está en uso!");
                } else {
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO CATEGORIA (NOMBRE, DESCRIPCION) VALUES (?,?)");
                    ps.setString(1, proveedor.getText().toUpperCase());
                    //ps.setString(2, jta_descripcion.getText().toUpperCase());
                    ps.executeUpdate(); //Ejecuta la consulta
                    JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
                    PRINCIPAL.actualizado = false;
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
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CATEGORIA WHERE NOMBRE='" + proveedor.getText().toUpperCase()+"'");
                if (ps.executeQuery().next() && !proveedor.getText().toUpperCase().equals(nombre_propio)) {
                    JOptionPane.showMessageDialog(null, "¡La categoría '" + proveedor.getText().toUpperCase() + "' ya está en uso!");
                } else {
                    ps = (PreparedStatement) con.prepareStatement("UPDATE CATEGORIA SET NOMBRE=?,DESCRIPCION=? WHERE ID=?");
                    ps.setString(1, proveedor.getText().toUpperCase());
//                    ps.setString(2, jta_descripcion.getText().toUpperCase());
                    //ps.setInt(3, Integer.parseInt(id.getText()));
                    ps.executeUpdate(); //Ejecuta la consulta
                    JOptionPane.showMessageDialog(null, "¡Modificado correctamente!");
                    PRINCIPAL.actualizado = false;
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
        jt_nombre2 = new javax.swing.JTextField();

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
        jsTabla_ciu13.setViewportView(JTdetalles);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Detalles:");

        jt_nombre2.setEditable(false);
        jt_nombre2.setBackground(new java.awt.Color(255, 255, 255));
        jt_nombre2.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jt_nombre2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Yu Gothic UI Light", 0, 14), new java.awt.Color(51, 51, 51))); // NOI18N
        jt_nombre2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jt_nombre2ActionPerformed(evt);
            }
        });
        jt_nombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jt_nombre2KeyPressed(evt);
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
                    .addComponent(jt_nombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jl_titulo23, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jl_titulo25, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jp_2Layout.createSequentialGroup()
                            .addComponent(jl_titulo26, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(28, 28, 28)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_2Layout.createSequentialGroup()
                            .addGroup(jp_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Bcat3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jsTabla_ciu12, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6, 6, 6)
                            .addComponent(Lcat3)
                            .addGap(44, 44, 44)
                            .addComponent(jsTabla_ciu13, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(empleado)))))
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
                .addComponent(jt_nombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jl_cerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrarMouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrarMouseClicked

    private void jb_EjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_EjecutarActionPerformed
       //String desc = jta_descripcion.getText().replaceAll("\\s+", "");
//        if (proveedor.getText().equals("") || desc.equals("")) {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(rootPane, "¡Aún hay campos por completar!");
//        } else {
//            if (forma.equals("registrar")) {
//                registrar();
//            } else if(forma.equals("modificar")){
//                modificar();
//            }
//        }

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

    private void jt_nombre2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jt_nombre2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_nombre2ActionPerformed

    private void jt_nombre2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jt_nombre2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jt_nombre2KeyPressed

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
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFpago_proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new JFpago_proveedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField Bcat;
    public static javax.swing.JTextField Bcat2;
    public static javax.swing.JTextField Bcat3;
    private javax.swing.JTable JTdetalles;
    private javax.swing.JTable JTemp;
    private javax.swing.JTable JTpro;
    private javax.swing.JTable JTprov;
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
    public static javax.swing.JTextField jt_nombre2;
    public static javax.swing.JTextField proveedor;
    // End of variables declaration//GEN-END:variables
}
