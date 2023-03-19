package centro_comercial;
import base_datos.conexion;
import java.sql.*;
import com.mysql.jdbc.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class JFVistaFactura extends javax.swing.JFrame {
    public static Connection con = null;
    public static ResultSet rs;
    public static PreparedStatement ps;
    public static String consulta = "";
    DefaultTableModel tabla = null;
    
    public JFVistaFactura() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public void llenar(String PK) {
        con = (Connection) conexion.conectar();
        if (con != null) {
            try {
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM ENCABEZADO_FAC WHERE CODIGO=" + PK);
                rs = ps.executeQuery();
                rs.next();
                codigo.setText("" + rs.getInt(1));
                int FK_suc = rs.getInt(2);
                int FK_emp = rs.getInt(3);
                int FK_cli = rs.getInt(4);
                fecha.setText(rs.getDate(5).toString());
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM SUCURSAL WHERE ID=" + FK_suc);
                rs = ps.executeQuery();
                rs.next();
                sucursal.setText(rs.getString(2));
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM EMPLEADO WHERE ID=" + FK_emp);
                rs = ps.executeQuery();
                rs.next();
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA=" + rs.getString(2));
                rs = ps.executeQuery();
                rs.next();
                empleado.setText(rs.getString(2) + " " + rs.getString(3));
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM CLIENTE WHERE ID=" + FK_cli);
                rs = ps.executeQuery();
                rs.next();
                cedula.setText(rs.getString(2));

                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PERSONA WHERE CEDULA=" + rs.getString(2));
                rs = ps.executeQuery();
                rs.next();
                cliente.setText(rs.getString(2) + " " + rs.getString(3));
                direccion.setText(rs.getString(8));
                telefono.setText(rs.getString(6));
                email.setText(rs.getString(7));

                //cargar detalles pertenecientes a la factura
                String[] colum_det = {"Código", "C. Producto", "Cantidad", "Subtotal", "C. Factura"};
                tabla = new DefaultTableModel(null, colum_det);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM DETALLE_FAC WHERE CODIGO_ENC=" + PK);
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getInt(5)});
                }

                JTdetalles.setModel(tabla);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM PAGO_FAC WHERE CODIGO_ENC=" + PK);
                rs = ps.executeQuery();
                rs.next();
                subtotal_sin_impuesto.setText("$" + rs.getDouble(2));
                double total = (Math.rint((rs.getDouble(5) - rs.getDouble(2)) * 100.0) / 100.0); //lo deja con 2 decimales
                iva.setText("$" + total);
                valor_total.setText("$" + rs.getDouble(5));
                int FK_iva = rs.getInt(4);

                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM FORMA_PAGO WHERE ID=" + rs.getInt(3));
                rs = ps.executeQuery();
                rs.next();
                forma_pago.setText(rs.getString(2));

                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM IVA WHERE ID=" + FK_iva);
                rs = ps.executeQuery();
                rs.next();
                jl_iva.setText("IVA " + rs.getInt(2) + "%");
                this.setVisible(true);
            } catch (SQLException e) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        VISTA_FACTURA = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        codigo = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        sucursal = new javax.swing.JLabel();
        empleado = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        cedula = new javax.swing.JLabel();
        cliente = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        direccion = new javax.swing.JLabel();
        telefono = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        email = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jsTabla_cat3 = new javax.swing.JScrollPane();
        JTdetalles = new javax.swing.JTable();
        jlAgregar_fac = new javax.swing.JLabel();
        elim_iva = new javax.swing.JButton();
        jp_7 = new javax.swing.JPanel();
        jl_titulo6 = new javax.swing.JLabel();
        jp_10 = new javax.swing.JPanel();
        jl_titulo9 = new javax.swing.JLabel();
        jp_11 = new javax.swing.JPanel();
        jl_titulo10 = new javax.swing.JLabel();
        jp_12 = new javax.swing.JPanel();
        jl_titulo11 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        subtotal_sin_impuesto = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        iva = new javax.swing.JLabel();
        jl_iva = new javax.swing.JLabel();
        valor_total = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        forma_pago = new javax.swing.JLabel();
        jp_6 = new javax.swing.JPanel();
        jl_cerrar5 = new javax.swing.JLabel();
        jl_titulo5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
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

        jLabel42.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel42.setText("No.");

        codigo.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        codigo.setText("00000");

        fecha.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha.setText("yyyy-mm-dd");

        jLabel44.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel44.setText("Fecha de emisión:");

        jLabel45.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel45.setText("Sucursal:");

        sucursal.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        sucursal.setText(" ");

        empleado.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        empleado.setText("Nombre_Apellido_emp");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel44)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addComponent(jLabel42)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(codigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel45)
                    .addComponent(sucursal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(empleado, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(codigo))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(empleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addGap(30, 30, 30))
                            .addComponent(sucursal, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel10.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel48.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel48.setText("Cédula:");

        cedula.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        cedula.setText(" ");

        cliente.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        cliente.setText(" ");

        jLabel49.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel49.setText("Razón Social:");

        jLabel50.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel50.setText("Dirección:");

        direccion.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        direccion.setText(" ");

        telefono.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        telefono.setText(" ");

        jLabel54.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel54.setText("Teléfono:");

        email.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        email.setText(" ");

        jLabel55.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel55.setText("Email:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cedula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(direccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(cedula)
                    .addComponent(jLabel54)
                    .addComponent(telefono))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(cliente)
                    .addComponent(jLabel55)
                    .addComponent(email))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addComponent(direccion))
                .addGap(6, 6, 6))
        );

        JTdetalles = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdetalles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdetalles.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
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
        JTdetalles.setSelectionBackground(new java.awt.Color(51, 51, 51));
        JTdetalles.getTableHeader().setResizingAllowed(false);
        JTdetalles.getTableHeader().setReorderingAllowed(false);
        JTdetalles.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTdetallesMouseClicked(evt);
            }
        });
        jsTabla_cat3.setViewportView(JTdetalles);

        jlAgregar_fac.setFont(new java.awt.Font("Jokerman", 0, 14)); // NOI18N
        jlAgregar_fac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAgregar_fac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_fac.png"))); // NOI18N
        jlAgregar_fac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlAgregar_fac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAgregar_facMouseClicked(evt);
            }
        });

        elim_iva.setBackground(new java.awt.Color(204, 0, 0));
        elim_iva.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_iva.setForeground(new java.awt.Color(255, 255, 255));
        elim_iva.setText("INACTIVAR FACTURA");
        elim_iva.setBorder(null);
        elim_iva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_iva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_ivaActionPerformed(evt);
            }
        });

        jp_7.setBackground(new java.awt.Color(51, 153, 255));
        jp_7.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_titulo6.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo6.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo6.setText("Datos del cliente:");

        javax.swing.GroupLayout jp_7Layout = new javax.swing.GroupLayout(jp_7);
        jp_7.setLayout(jp_7Layout);
        jp_7Layout.setHorizontalGroup(
            jp_7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo6, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );
        jp_7Layout.setVerticalGroup(
            jp_7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jp_10.setBackground(new java.awt.Color(51, 153, 255));
        jp_10.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_titulo9.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo9.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo9.setText("Detalles:");

        javax.swing.GroupLayout jp_10Layout = new javax.swing.GroupLayout(jp_10);
        jp_10.setLayout(jp_10Layout);
        jp_10Layout.setHorizontalGroup(
            jp_10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo9, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
        );
        jp_10Layout.setVerticalGroup(
            jp_10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jp_11.setBackground(new java.awt.Color(51, 153, 255));
        jp_11.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_titulo10.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo10.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo10.setText("Totales:");

        javax.swing.GroupLayout jp_11Layout = new javax.swing.GroupLayout(jp_11);
        jp_11.setLayout(jp_11Layout);
        jp_11Layout.setHorizontalGroup(
            jp_11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jp_11Layout.setVerticalGroup(
            jp_11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jp_12.setBackground(new java.awt.Color(51, 153, 255));
        jp_12.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_titulo11.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo11.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo11.setText("Forma de pago:");

        javax.swing.GroupLayout jp_12Layout = new javax.swing.GroupLayout(jp_12);
        jp_12.setLayout(jp_12Layout);
        jp_12Layout.setHorizontalGroup(
            jp_12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo11, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
        );
        jp_12Layout.setVerticalGroup(
            jp_12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jl_titulo11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel11.setPreferredSize(new java.awt.Dimension(2, 100));

        subtotal_sin_impuesto.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        subtotal_sin_impuesto.setText(" ");
        subtotal_sin_impuesto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel53.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel53.setText("SUBTOTAL SIN IMPUESTO:");
        jLabel53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        iva.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        iva.setText(" ");
        iva.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jl_iva.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jl_iva.setText("IVA x%:");
        jl_iva.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        valor_total.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        valor_total.setText(" ");
        valor_total.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel59.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel59.setText("VALOR TOTAL:");
        jLabel59.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(subtotal_sin_impuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jl_iva, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(valor_total, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subtotal_sin_impuesto))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jl_iva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iva))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(valor_total)))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel12.setPreferredSize(new java.awt.Dimension(2, 100));

        forma_pago.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        forma_pago.setText(" ");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(forma_pago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(forma_pago)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout VISTA_FACTURALayout = new javax.swing.GroupLayout(VISTA_FACTURA);
        VISTA_FACTURA.setLayout(VISTA_FACTURALayout);
        VISTA_FACTURALayout.setHorizontalGroup(
            VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jp_7, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jp_10, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                            .addComponent(jsTabla_cat3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VISTA_FACTURALayout.createSequentialGroup()
                                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jp_12, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                                    .addComponent(jp_11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)))))
                    .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                        .addComponent(jlAgregar_fac, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(elim_iva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        VISTA_FACTURALayout.setVerticalGroup(
            VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(VISTA_FACTURALayout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(elim_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jlAgregar_fac, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jp_7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jp_10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jsTabla_cat3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jp_11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jp_12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(VISTA_FACTURALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jp_6.setBackground(new java.awt.Color(51, 153, 255));
        jp_6.setPreferredSize(new java.awt.Dimension(560, 40));

        jl_cerrar5.setFont(new java.awt.Font("Yu Gothic UI", 1, 24)); // NOI18N
        jl_cerrar5.setForeground(new java.awt.Color(255, 255, 255));
        jl_cerrar5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_cerrar5.setText("×");
        jl_cerrar5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jl_cerrar5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jl_cerrar5MouseClicked(evt);
            }
        });

        jl_titulo5.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jl_titulo5.setForeground(new java.awt.Color(255, 255, 255));
        jl_titulo5.setText("VISTA FACTURA");

        javax.swing.GroupLayout jp_6Layout = new javax.swing.GroupLayout(jp_6);
        jp_6.setLayout(jp_6Layout);
        jp_6Layout.setHorizontalGroup(
            jp_6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jp_6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jl_titulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
                .addComponent(jl_cerrar5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jp_6Layout.setVerticalGroup(
            jp_6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jp_6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jp_6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jl_titulo5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jl_cerrar5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(VISTA_FACTURA, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addComponent(jp_6, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jp_6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(VISTA_FACTURA, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JTdetallesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTdetallesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_JTdetallesMouseClicked

    private void jlAgregar_facMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAgregar_facMouseClicked
        PRINCIPAL.MENU.setSelectedIndex(0);
        PRINCIPAL.INICIO.setSelectedIndex(0);
        this.dispose();
    }//GEN-LAST:event_jlAgregar_facMouseClicked

    private void elim_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_ivaActionPerformed
        con = conexion.conectar();
        if (con != null) {
            try {
                if (JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea continuar con esta acción?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    ps = (PreparedStatement) con.prepareStatement("UPDATE ENCABEZADO_FAC SET ESTADO=? WHERE CODIGO=?");
                    ps.setString(1, "INACTIVO");
                    ps.setInt(2, (Integer.parseInt(codigo.getText())));
                    ps.executeUpdate(); //ejecuta la consulta
                    this.dispose();
                    JOptionPane.showMessageDialog(null, "¡Inactivado correctamente!");
                }

            } catch (SQLException ex) {
            }
        }

    }//GEN-LAST:event_elim_ivaActionPerformed

    private void jl_cerrar5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jl_cerrar5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jl_cerrar5MouseClicked

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
    public static javax.swing.JTable JTdetalles;
    private javax.swing.JPanel VISTA_FACTURA;
    private javax.swing.JLabel cedula;
    private javax.swing.JLabel cliente;
    private javax.swing.JLabel codigo;
    private javax.swing.JLabel direccion;
    private javax.swing.JButton elim_iva;
    private javax.swing.JLabel email;
    private javax.swing.JLabel empleado;
    private javax.swing.JLabel fecha;
    private javax.swing.JLabel forma_pago;
    private javax.swing.JLabel iva;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlAgregar_fac;
    private javax.swing.JLabel jl_cerrar5;
    private javax.swing.JLabel jl_iva;
    public static javax.swing.JLabel jl_titulo10;
    public static javax.swing.JLabel jl_titulo11;
    public static javax.swing.JLabel jl_titulo5;
    public static javax.swing.JLabel jl_titulo6;
    public static javax.swing.JLabel jl_titulo9;
    public static javax.swing.JPanel jp_10;
    public static javax.swing.JPanel jp_11;
    public static javax.swing.JPanel jp_12;
    public static javax.swing.JPanel jp_6;
    public static javax.swing.JPanel jp_7;
    private javax.swing.JScrollPane jsTabla_cat3;
    private javax.swing.JLabel subtotal_sin_impuesto;
    private javax.swing.JLabel sucursal;
    private javax.swing.JLabel telefono;
    private javax.swing.JLabel valor_total;
    // End of variables declaration//GEN-END:variables
}
