package centro_comercial;
import base_datos.conexion;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import otros.BotonTabla;
import otros.fechas;
import java.sql.*;
import com.mysql.jdbc.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class PRINCIPAL extends javax.swing.JFrame implements Runnable {
    
    //variables para consultas SQL:
    public static Connection con = null;
    public static ResultSet rs;
    public static ResultSet rs2;
    public static PreparedStatement ps, ps2,ps3,ps4;
    public static String consulta = "";
    public static String consulta2 = "";
    //variables que guardan el número de registros:
    public static int cat, ciu, cli, dep, des, det, emp, enc, fp, gen, iva, mar, pe, pag, per, pro, prov, provi, pue, suc, e_pp, d_pp;

    //otras variables útiles:
    public static boolean actualizado = false;
    public static String pk = "";
    String [] arreglo = {"Cliente", "Empleado", "Proveedor", "Sucursal"};
    String [] arreglo2 = {"Cliente", "Empleado"};
    String [] arreglo3 = {"Factura", "Pagos sueldos (remitente)", "Pagos sueldos (destinatario)"};
    DefaultTableModel tabla = null, tabla_detalle = null;
    TableRowSorter sorter;
    
    public static String modo_prov = "producto";
    
    ArrayList<String> detalles = new ArrayList<>();
    JButton boton1 = new JButton();

    String hora, minutos, segundos;
    Thread hilo;
    
    //variables para factura:
    public static int descuento = 0 , num_det = 0, IVA;
    public static double total_descuento = 0, subtotal = 0,total = 0; 
    public static int xcolum,xrow;
    public static int FK_suc, FK_emp;
    public static int FK_iva, FK_fp, FK_enc;
    
    //instancias de los frames:
    public static JFcategoria JFcat = new JFcategoria();
    public static JFciudad JFciu = new JFciudad();
    public static JFcliente JFcli = new JFcliente();
    public static JFdepartamento JFdep=new JFdepartamento();
    public static JFdescuento JFdes = new JFdescuento();
    public static JFempleado JFemp = new JFempleado();
    public static JFpago_proveedor JFpp = new JFpago_proveedor();
    public static JFVistaFactura JFvf = new JFVistaFactura();
    public static JFforma_pago JFfp = new JFforma_pago();
    public static JFgenero JFgen = new JFgenero();
    public static JFiva JFIVA = new JFiva();
    public static JFmarca JFmar = new JFmarca();
    public static JFpago_empleado JFpe = new JFpago_empleado();
    public static JFproducto JFpro = new JFproducto();
    public static JFproveedor JFprov = new JFproveedor();
    public static JFprovincia JFprovi = new JFprovincia();
    public static JFpuesto JFpue = new JFpuesto();
    public static JFsucursal JFsuc = new JFsucursal();

    public PRINCIPAL() {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
    }

    public final void iniciar() {
        ocultar_paneles();
        //para la fecha y hora
        hilo = new Thread(this);
        hilo.start();
        //para seleccionar filas de las tablas
        seleccionar();
        //para icono en el boton y modelo en tabla JTdetalle
        InsertarIcono(boton1, "/imagenes/elim.png");
        tabla_detalle = new DefaultTableModel(null, new Object[]{"C. Producto", "Descipción", "Cantidad", "Precio unitario",
            "Descuento", "Precio con descuento", "Valor total", "Aciones"});
        JTdetalle.setModel(tabla_detalle);
        //ocultar o bloquear componentes
        Lcat.setVisible(false);
        Lciu.setVisible(false);
        Lcli.setVisible(false);
        Ldep.setVisible(false);
        Ldes.setVisible(false);
        Ldet.setVisible(false);
        Ldpp.setVisible(false);
        Lemp.setVisible(false);
        Lenc.setVisible(false);
        Lfp.setVisible(false);
        Lgen.setVisible(false);
        Liva.setVisible(false);
        Lmar.setVisible(false);
        Lpag.setVisible(false);
        Lpe.setVisible(false);
        Lepp.setVisible(false);
        Lper.setVisible(false);
        Lpro.setVisible(false);
        Lprov.setVisible(false);
        Lprovi.setVisible(false);
        Lpue.setVisible(false);
        Lsuc.setVisible(false);

    }

    //cargar los datos en las tablas:
    public void visualizar() {
        con = conexion.conectar();
        if (con != null) {
            try {
                consulta = "SELECT * FROM ";
                for (int i = 1; i <= 22; i++) {
                    switch (i) {
                        case 1://categoria
                            String[] c_cat = {"ID", "NOMBRE", "DESCRIPCIÓN"};
                            tabla = new DefaultTableModel(null, c_cat);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "categoria");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
                            }
                            JTcat.setModel(tabla);
                            cat = tabla.getRowCount();
                            res_cat.setText("Resultados: " + cat + " de " + cat);
                            break;
                        case 2://ciudad
                            String[] c_ciu = {"ID", "NOMBRE", "ID_PROVI"};
                            tabla = new DefaultTableModel(null, c_ciu);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "ciudad");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3)});
                            }
                            JTciu.setModel(tabla);
                            ciu = tabla.getRowCount();
                            res_ciu.setText("Resultados: " + ciu + " de " + ciu);
                            break;
                        case 3://cliente
                            String[] c_cli = {"ID", "CÉDULA_PER", "ID_DES"};
                            tabla = new DefaultTableModel(null, c_cli);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "cliente");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3)});
                            }
                            JTcli.setModel(tabla);
                            cli = tabla.getRowCount();
                            res_cli.setText("Resultados: " + cli + " de " + cli);
                            break;
                        case 4://departamento
                            String[] c_dep = {"ID", "NOMBRE", "DESCRIPCIÓN"};
                            tabla = new DefaultTableModel(null, c_dep);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "departamento");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
                            }
                            JTdep.setModel(tabla);
                            dep = tabla.getRowCount();
                            res_dep.setText("Resultados: " + dep + " de " + dep);
                            break;
                        case 5://descuento
                            String[] c_des = {"ID", "NOMBRE", "PORCENTAJE"};
                            tabla = new DefaultTableModel(null, c_des);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "descuento");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3)});
                            }
                            JTdes.setModel(tabla);
                            des = tabla.getRowCount();
                            res_des.setText("Resultados: " + des + " de " + des);
                            break;
                        case 6://detalle_fac
                            String[] c_det = {"CÓDIGO", "CÓDIGO_PRO", "CANTIDAD", "SUBTOTAL", "CÓDIGO_ENC"};
                            tabla = new DefaultTableModel(null, c_det);
                            ps = (PreparedStatement) con.prepareStatement("SELECT D.CODIGO, D.CODIGO_PRO, D.CANTIDAD, D.SUBTOTAL, D.CODIGO_ENC FROM detalle_fac D "
                                    + "INNER JOIN encabezado_fac E WHERE D.CODIGO_ENC = E.CODIGO AND E.ESTADO ='ACTIVO'");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getInt(5)});
                            }
                            JTdet.setModel(tabla);
                            det = tabla.getRowCount();
                            res_det.setText("Resultados: " + det + " de " + det);
                            break;
                        case 7://empleado
                            String[] c_emp = {"ID", "CÉDULA_PER", "ID_DEP", "ID_PUE"};
                            tabla = new DefaultTableModel(null, c_emp);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "EMPLEADO");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(4), rs.getInt(5)});
                            }
                            JTemp.setModel(tabla);
                            emp = tabla.getRowCount();
                            res_emp.setText("Resultados: " + emp + " de " + emp);
                            break;
                        case 8://encabezado_fac
                            String[] c_enc = {"CODIGO", "ID_SUC", "ID_EMP", "ID_CLI", "FECHA_REG", "ESTADO"};
                            tabla = new DefaultTableModel(null, c_enc);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "encabezado_fac WHERE ESTADO = 'ACTIVO'");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), fechas.transformar(rs.getDate(5)), rs.getString(6)});
                            }
                            JTenc.setModel(tabla);
                            enc = tabla.getRowCount();
                            res_enc.setText("Resultados: " + enc + " de " + enc);
                            break;
                        case 9://forma_pago
                            String[] c_fp = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_fp);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "FORMA_PAGO ORDER BY NOMBRE ASC");
                            rs = ps.executeQuery();
                            f_forma_pago.removeAllItems();
                            f_forma_pago.addItem("Seleccione...");
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                                f_forma_pago.addItem(rs.getString(2));
                            }
                            JTfp.setModel(tabla);
                            fp = tabla.getRowCount();
                            res_fp.setText("Resultados: " + fp + " de " + fp);
                            break;
                        case 10://genero
                            String[] c_gen = {"ID", "SEXO"};
                            tabla = new DefaultTableModel(null, c_gen);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "GENERO");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            JTgen.setModel(tabla);
                            gen = tabla.getRowCount();
                            res_gen.setText("Resultados: " + gen + " de " + gen);
                            break;
                        case 11://iva
                            ArrayList <String> ivas = new ArrayList<>();
                            ivas.add("0%");
                            String[] c_iva = {"ID", "IMPUESTO"};
                            tabla = new DefaultTableModel(null, c_iva);
                            ps = (PreparedStatement) con.prepareStatement(consulta+"IVA ORDER BY IMPUESTO ASC");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2)});
                                ivas.add(""+rs.getInt(2)+"%");
                            }
                            String[] ivass = ivas.toArray(new String[0]);
                            f_iva.setModel(new DefaultComboBoxModel<>(ivass));
                            f_iva.setBackground(Color.red);
                            JTiva.setModel(tabla);
                            iva = tabla.getRowCount();
                            res_iva.setText("Resultados: " + iva + " de " + iva);
                            break;
                        case 12://marca
                            String[] c_mar = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_mar);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "marca ORDER BY NOMBRE ASC");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            JTmar.setModel(tabla);
                            mar = tabla.getRowCount();
                            res_mar.setText("Resultados: " + mar + " de " + mar);
                            break;
                        case 13://pago_empleado
                            String[] c_pe = {"NUMERO", "ID_REMITENTE", "ID_DESTINATARIO", "TOTAL", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_pe);
                            ps = (PreparedStatement) con.prepareStatement(consulta +"pago_empleado ORDER BY FECHA_REG DESC");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), fechas.transformar(rs.getDate(5))});
                            }
                            JTpe.setModel(tabla);
                            pe = tabla.getRowCount();
                            res_pe.setText("Resultados: " + pe + " de " + pe);
                            break;
                        case 14://pago_fac
                            String[] c_pf = {"NUMERO", "TOTAL_SIN_IVA", "ID_FOR", "ID_IVA", "TOTAL_MAS_IVA", "CODIGO_ENC"};
                            tabla = new DefaultTableModel(null, c_pf);
                            ps = (PreparedStatement) con.prepareStatement("SELECT P.NUMERO, P.TOTAL_SIN_IVA, P.ID_FOR, P.ID_IVA, P.TOTAL_MAS_IVA, P.CODIGO_ENC FROM pago_fac P INNER JOIN encabezado_fac E WHERE P.CODIGO_ENC = E.CODIGO AND E.ESTADO = 'ACTIVO'");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getDouble(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5), rs.getInt(6)});
                            }
                            JTpag.setModel(tabla);
                            pag = tabla.getRowCount();
                            res_pag.setText("Resultados: " + pag + " de " + pag);
                            break;
                        case 15://persona
                            String[] c_per = {"CEDULA", "NOMBRE", "APELLIDO", "FECHA_NAC", "ID_SEXO", "CELULAR", "EMAIL", "DIRECCION", "ID_CIUDAD", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_per);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "persona ORDER BY FECHA_REG DESC");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), fechas.transformar(rs.getDate(4)),
                                    rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), fechas.transformar(rs.getDate(10))});
                            }
                            JTper.setModel(tabla);
                            per = tabla.getRowCount();
                            res_per.setText("Resultados: " + per + " de " + per);
                            break;
                        case 16://producto
                            String[] c_pro = {"CODIGO", "NOMBRE", "ID_MAR", "PRECIO", "EXIS_MAX", "EXIS_MIN", "STOK", "ID_CAT", "FECHA_REG", "RUC_PROV"};
                            tabla = new DefaultTableModel(null, c_pro);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "producto ORDER BY NOMBRE ASC");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(4), rs.getDouble(5), rs.getInt(6),
                                    rs.getInt(7), rs.getInt(8), rs.getInt(9), fechas.transformar(rs.getDate(10)), rs.getString(11)});
                            }
                            JTpro.setModel(tabla);
                            pro = tabla.getRowCount();
                            res_pro.setText("Resultados: " + pro + " de " + pro);
                            break;
                        case 17://proveedor
                            String[] c_prov = {"RUC", "NOMBRE_EMPRESA", "CELULAR", "EMAIL", "ID_CIU", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_prov);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "proveedor");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), fechas.transformar(rs.getDate(6))});
                            }
                            JTprov.setModel(tabla);
                            prov = tabla.getRowCount();
                            res_prov.setText("Resultados: " + prov + " de " + prov);
                            break;
                        case 18://provincia
                            String[] c_provi = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_provi);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "provincia");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            JTprovi.setModel(tabla);
                            provi = tabla.getRowCount();
                            res_provi.setText("Resultados: " + provi + " de " + provi);
                            break;
                        case 19://puesto
                            String[] c_pue = {"ID", "NOMBRE", "SUELDO"};
                            tabla = new DefaultTableModel(null, c_pue);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "puesto");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(3)});
                            }
                            JTpue.setModel(tabla);
                            pue = tabla.getRowCount();
                            res_pue.setText("Resultados: " + pue + " de " + pue);
                            break;
                        case 20://sucursal
                            String[] c_suc = {"ID", "NOMBRE", "ID_CIU"};
                            tabla = new DefaultTableModel(null, c_suc);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "sucursal");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(3)});
                            }
                            JTsuc.setModel(tabla);
                            suc = tabla.getRowCount();
                            res_suc.setText("Resultados: " + suc + " de " + suc);
                            break;
                        case 21://encabezado_pago_prov
                            String[] c_encapag = {"CODIGO", "RUC", "TOTAL", "EMPLEADO", "FECHA"};
                            tabla = new DefaultTableModel(null, c_encapag);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "ENCABEZADO_PP");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(5), fechas.transformar(rs.getDate(5))});
                            }
                            JTenc_pp.setModel(tabla);
                            e_pp = tabla.getRowCount();
                            res_enc_pp.setText("Resultados: " + e_pp + " de " + e_pp);
                            break;
                        case 22://detallepagp_proveedores
                            String[] c_detapag = {"CODIGO", "PRODUCTO", "CANTIDAD", "SUBTOTAL", "NUM_COMPRA"};
                            tabla = new DefaultTableModel(null, c_detapag);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "DETALLE_PP");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getInt(5)});
                            }
                            JTdet_pp.setModel(tabla);
                            d_pp = tabla.getRowCount();
                            res_det_pp.setText("Resultados: " + d_pp + " de " + d_pp);
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    //buscar registros de cualquier tabla:
    public void buscar(JTable tab, JTextField tex, JLabel a, int b, JComboBox c) {
        DefaultTableModel modelo = (DefaultTableModel) tab.getModel();
        sorter = new TableRowSorter<>(modelo);
        tab.setAutoCreateRowSorter(true);
        tab.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(tex.getText(), c.getSelectedIndex()));
        a.setText("Resultados: " + String.valueOf(tab.getRowCount()) + " de " + b);
    }

    //método para eliminar registros de la base de datos:
     public void eliminar(int clase) {
        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea continuar con esta acción?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            if (conexion.conectar() != null) {
                boolean eliminado = true;
                consulta = "SELECT * FROM ";
                consulta2 = "DELETE FROM ";
                try {
                    switch (clase) {
                        case 1://categoria
                            ps = (PreparedStatement) con.prepareStatement(consulta + "producto WHERE ID_CAT = " + id_cat.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la categoría ya que se encuentra asignado a un producto!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "categoria WHERE ID = " + id_cat.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 2://ciudad
                            ps = (PreparedStatement) con.prepareStatement(consulta + "persona WHERE ID_CIUDAD = " + id_ciu.getText());
                            ps2 = (PreparedStatement) con.prepareStatement(consulta + "sucursal WHERE ID_CIU = " + id_ciu.getText());
                            ps3 = (PreparedStatement) con.prepareStatement(consulta + "proveedor WHERE ID_CIU = " + id_ciu.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la ciudad ya que se encuentra asignado a una persona!");
                                eliminado = false;
                                if (ps2.executeQuery().next()) {
                                    JOptionPane.showMessageDialog(null, "¡Imposible eliminar la ciudad ya que se encuentra asignado a una sucursal!");
                                    eliminado = false;
                                    if (ps3.executeQuery().next()) {
                                        JOptionPane.showMessageDialog(null, "¡Imposible eliminar la ciudad ya que se encuentra asignado a un proveedor!");
                                        eliminado = false;
                                    }else {
                                        ps3 = (PreparedStatement) con.prepareStatement(consulta2 + "ciudad WHERE ID = " + id_ciu.getText());
                                        ps3.executeUpdate();
                                    }
                                }else {
                                    ps2 = (PreparedStatement) con.prepareStatement(consulta2 + "ciudad WHERE ID = " + id_ciu.getText());
                                    ps2.executeUpdate();
                                }
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "ciudad WHERE ID = " + id_ciu.getText());
                                ps.executeUpdate();
                            }
                            
                            break;
                        case 3://cliente
                            ps = (PreparedStatement) con.prepareStatement(consulta + "encabezado_fac WHERE ID_CLI = " + id_cli.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el cliente ya que se encuentra asignado a una factura!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "CLIENTE WHERE ID = " + id_cli.getText());
                                ps.executeUpdate();
                            }
                                    
                            break;
                        case 4://departamento
                            ps = (PreparedStatement) con.prepareStatement(consulta + "empleado WHERE ID_DEP = " + id_dep.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el departamento ya que se encuentra asignado a un empleado!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "departamento WHERE ID = " + id_dep.getText());
                                ps.executeUpdate();
                            }
                            break;
                         
                        case 5://descuento
                             ps = (PreparedStatement) con.prepareStatement(consulta + "cliente WHERE ID_DES = " + id_des.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el descuento ya que se encuentra asignado a un cliente!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "descuento WHERE ID = " + id_des.getText());
                                ps.executeUpdate();
                            }

                            break;
                        case 8://empleado
                            ps = (PreparedStatement) con.prepareStatement(consulta + "encabezado_fac WHERE ID_EMP = " + id_emp.getText());
                            ps2 = (PreparedStatement) con.prepareStatement(consulta + "encabezado_pp WHERE ID_EMP = " + id_emp.getText());
                            ps3 = (PreparedStatement) con.prepareStatement(consulta + "PAGO_EMPLEADO WHERE ID_REMITENTE  = " + id_emp.getText());
                            ps4 = (PreparedStatement) con.prepareStatement(consulta + "PAGO_EMPLEADO WHERE ID_DESTINATARIO = " + id_emp.getText());

                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el empleado ya que se encuentra asignado a una factura!");
                                eliminado = false;
                            }
                            if (ps2.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el empleado ya que se encuentra asignado a una compra de articulos!");
                                eliminado = false;
                            }
                            if (ps3.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el empleado ya que se encuentra asignado al pago de un remitente!");
                                eliminado = false;
                            }
                            if (ps4.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el empleado ya que se encuentra asignado al pago de un destinatario!");
                                eliminado = false;
                            }
                            if (!ps.executeQuery().next() && !ps2.executeQuery().next() && !ps3.executeQuery().next() && !ps4.executeQuery().next()) {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "empleado WHERE ID = " + id_emp.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 11://forma_pago
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_fac WHERE ID_FOR = " + id_fp.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la forma de pago ya que se encuentra asignado a un pago de una factura!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "forma_pago WHERE ID = " + id_fp.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 12://genero
                            ps = (PreparedStatement) con.prepareStatement(consulta + "persona WHERE ID_SEXO = " + id_gen.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el genero ya que se encuentra asignado a una persona!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "genero WHERE ID = " + id_gen.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 13://iva
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_fac WHERE ID_IVA = " + id_iva.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el iva ya que se encuentra asignado a un pago de una factura!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "iva WHERE ID = " + id_iva.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 14://marca
                             ps = (PreparedStatement) con.prepareStatement(consulta + "producto WHERE ID_MAR = " + id_mar.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la marca ya que se encuentra asignado a un producto!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "marca WHERE ID = " + id_mar.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 18://producto
                            ps = (PreparedStatement) con.prepareStatement(consulta + "detalle_fac WHERE CODIGO_PRO = " + codigo_pro.getText());
                            ps2 = (PreparedStatement) con.prepareStatement(consulta + "detalle_pp WHERE CODIGO_PRO = " + codigo_pro.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el producto ya que se encuentra asignado a un detalle de factura!");
                                eliminado = false;
                                }
                            if (ps2.executeQuery().next()) {
                                    JOptionPane.showMessageDialog(null, "¡Imposible eliminar el producto ya que se encuentra asignado a una compra !");
                                    eliminado = false;
                                }
                            if (!ps.executeQuery().next() && !ps2.executeQuery().next()) {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "producto WHERE CODIGO = " + codigo_pro.getText());
                               
                                ps.executeUpdate();
                            }

                            break;
                        case 19://proveedor
                            ps = (PreparedStatement) con.prepareStatement(consulta + "producto WHERE RUC_PROV = " + ruc_prov.getText());
                            ps2 = (PreparedStatement) con.prepareStatement(consulta + "encabezado_pp WHERE RUC_PROV = " + ruc_prov.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el proveedor ya que se encuentra asignado a un producto!");
                                eliminado = false;
                            }
                            if (ps2.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el proveedor ya que se encuentra asignado a una compra de productos!");
                                eliminado = false;
                            }
                            if (!ps.executeQuery().next() && !ps2.executeQuery().next()) {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "proveedor WHERE RUC = " + ruc_prov.getText());
                                ps.executeUpdate();
                            }
                          
                            
                            break;
                        case 20://provincia
                            ps = (PreparedStatement) con.prepareStatement(consulta + "ciudad WHERE ID_PROVI = " + id_provi.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la provincia ya que se encuentra asignado a una ciudad!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "provincia WHERE ID = " + id_provi.getText());
                                ps.executeUpdate();
                            }
                            
                            break;
                        case 21://puesto
                             ps = (PreparedStatement) con.prepareStatement(consulta + "empleado WHERE ID_PUE = " + id_pue.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el puesto ya que se encuentra asignado a un empleado!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "puesto WHERE ID = " + id_pue.getText());
                                ps.executeUpdate();
                            }
                            break;
                        case 22://sucursal
                             ps = (PreparedStatement) con.prepareStatement(consulta + "encabezado_fac WHERE ID_SUC = " + id_suc.getText());
                            if (ps.executeQuery().next()) {
                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar la sucursal ya que se encuentra asignado a un encabezado de factura!");
                                eliminado = false;
                            } else {
                                ps = (PreparedStatement) con.prepareStatement(consulta2 + "sucursal WHERE ID = " + id_suc.getText());
                                ps.executeUpdate();
                            }
                            break;
                    }
                } catch (Exception e) {
                    eliminado = false;
                    getToolkit().beep();
                    JOptionPane.showMessageDialog(rootPane, "¡Registro no existente!", null, JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (eliminado) {
                        JOptionPane.showMessageDialog(null, "¡Eliminado correctamente!");
                    }
                    ver_panel(clase, false);
                    visualizar();
                }
            }
        }
    }

    //método para registrar una factura (encabezado y detalles):
    public void generar_factura() {
        con = conexion.conectar();
        if (con != null) {
            try {
                
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM IVA WHERE IMPUESTO="+IVA);
                rs = ps.executeQuery();
                rs.next();
                FK_iva = rs.getInt(1);
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM FORMA_PAGO WHERE NOMBRE='"+f_forma_pago.getSelectedItem().toString()+"'");
                rs = ps.executeQuery();
                rs.next();
                FK_fp = rs.getInt(1);
                //registra el encabezado de la factura
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO encabezado_fac (ID_SUC, ID_EMP, ID_CLI, ESTADO) VALUES (?,?,?,?)");
                ps.setInt(1, FK_suc);
                ps.setInt(2, FK_emp);
                ps.setInt(3,Integer.parseInt(f_id_cli.getText()));
                ps.setString(4, "ACTIVO");
                ps.executeUpdate();
                //toma el último registro de encabezado
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM  encabezado_fac ORDER BY CODIGO DESC LIMIT 1");
                rs = ps.executeQuery();
                rs.next();
                FK_enc = rs.getInt(1);
                //registra detalles de la factura
                for (int i = 0; i < num_det; i++) {
                    int xcodigo_pro = Integer.parseInt(JTdetalle.getValueAt(i, 0).toString());
                    int xcantidad = Integer.parseInt(JTdetalle.getValueAt(i, 2).toString());
                    double xsubtotal = Double.parseDouble(JTdetalle.getValueAt(i, 6).toString());
                    ps = (PreparedStatement) con.prepareStatement("INSERT INTO detalle_fac (CODIGO_PRO, CANTIDAD, SUBTOTAL, CODIGO_ENC) VALUES (?,?,?,?)");
                    ps.setInt(1, xcodigo_pro);
                    ps.setInt(2, xcantidad);
                    ps.setDouble(3, xsubtotal);
                    ps.setInt(4, FK_enc);
                    ps.executeUpdate();
                }
                ps = (PreparedStatement) con.prepareStatement("SELECT * FROM FORMA_PAGO WHERE NOMBRE='"+f_forma_pago.getSelectedItem().toString()+"'");
                rs = ps.executeQuery();
                rs.next();
                
                //registra el pago de la factura
                ps = (PreparedStatement) con.prepareStatement("INSERT INTO pago_fac (TOTAL_SIN_IVA, ID_FOR, ID_IVA, TOTAL_MAS_IVA, CODIGO_ENC) VALUES (?,?,?,?,?)");
                ps.setDouble(1, subtotal);
                ps.setInt(2, FK_fp);
                ps.setInt(3, FK_iva);
                ps.setDouble(4, total);
                ps.setInt(5, FK_enc);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
            } catch (SQLException ex) {
                getToolkit().beep();
                JOptionPane.showMessageDialog(null, "¡Error al registrar!");
            }
        }
    }
    
    //limpia los campos de texto y los reinicia valores:
    public void reiniciar_factura() {
        for (int i = num_det; i > 0; i--) {
            tabla_detalle.removeRow(i - 1);
        }
        num_det = 0;
        total_descuento = 0;
        subtotal = 0;
        total = 0;
        
        detalles.clear();
        f_num_det.setText("Detalles: 0");
        f_descuento.setText("- $0");
        f_subtotal.setText("$0");
        f_iva.setSelectedIndex(0);
        f_total.setText("$0");
        f_forma_pago.setSelectedIndex(0);
        
        f_fecha.setText("");
        f_fecha.setBackground(Color.red);
        f_codigo.setText("Autogenerado");
        f_suc.setText("");
        f_suc.setBackground(Color.red);
        f_emp.setText("");
        f_emp.setBackground(Color.red);
        f_ced_cli.setText("");
        f_ced_cli.setBackground(Color.red);
        f_id_cli.setText("");
        f_nom_ape.setText(""); 
        f_dir.setText(""); 
        f_tel.setText("");
        f_email.setText("");
        f_des.setText("");
        JBseleccionar_pro.setEnabled(false);
        JSfacturar.getVerticalScrollBar().setValue(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fecha_hora = new javax.swing.JLabel();
        usuario = new javax.swing.JLabel();
        empresa = new javax.swing.JLabel();
        cerrar_sesion = new javax.swing.JLabel();
        MENU = new javax.swing.JTabbedPane();
        INICIO = new javax.swing.JTabbedPane();
        JSfacturar = new javax.swing.JScrollPane();
        JPfacturar = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        f_codigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        f_ced_cli = new javax.swing.JTextField();
        f_nom_ape = new javax.swing.JTextField();
        f_dir = new javax.swing.JTextField();
        f_tel = new javax.swing.JTextField();
        f_email = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        f_fecha = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        f_id_cli = new javax.swing.JTextField();
        jsTabla_ciu1 = new javax.swing.JScrollPane();
        JTdetalle = new javax.swing.JTable();
        JBseleccionar_pro = new javax.swing.JButton();
        f_num_det = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jl_num_det1 = new javax.swing.JLabel();
        jl_total1 = new javax.swing.JLabel();
        jl_num_det2 = new javax.swing.JLabel();
        f_descuento = new javax.swing.JLabel();
        f_total = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        f_iva = new javax.swing.JComboBox<>();
        jl_num_det3 = new javax.swing.JLabel();
        f_subtotal = new javax.swing.JLabel();
        l_des = new javax.swing.JLabel();
        f_des = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        f_emp = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        f_suc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        JBlimpiar_factura = new javax.swing.JButton();
        JBcrear_factura = new javax.swing.JButton();
        f_forma_pago = new javax.swing.JComboBox<>();
        JSventas = new javax.swing.JScrollPane();
        JPventas = new javax.swing.JPanel();
        jsTabla_ciu2 = new javax.swing.JScrollPane();
        JTenc = new javax.swing.JTable();
        jsTabla_ciu3 = new javax.swing.JScrollPane();
        JTdet = new javax.swing.JTable();
        JCenc = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        Benc = new javax.swing.JTextField();
        Lenc = new javax.swing.JLabel();
        res_enc = new javax.swing.JLabel();
        jl_titulo6 = new javax.swing.JLabel();
        jl_titulo8 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        res_det = new javax.swing.JLabel();
        JCdet = new javax.swing.JComboBox<>();
        Bdet = new javax.swing.JTextField();
        Ldet = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jsTabla_ciu17 = new javax.swing.JScrollPane();
        JTpag = new javax.swing.JTable();
        Lpag = new javax.swing.JLabel();
        Bpag = new javax.swing.JTextField();
        JCpag = new javax.swing.JComboBox<>();
        jl_titulo10 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        res_pag = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        JSpagos = new javax.swing.JScrollPane();
        JPpagos = new javax.swing.JPanel();
        jl_titulo29 = new javax.swing.JLabel();
        jsTabla_ciu16 = new javax.swing.JScrollPane();
        JTpe = new javax.swing.JTable();
        jLabel147 = new javax.swing.JLabel();
        JCpe = new javax.swing.JComboBox<>();
        Bpe = new javax.swing.JTextField();
        Lpe = new javax.swing.JLabel();
        res_pe = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel148 = new javax.swing.JLabel();
        JPpe = new javax.swing.JPanel();
        jLabel149 = new javax.swing.JLabel();
        total_pe = new javax.swing.JLabel();
        jLabel154 = new javax.swing.JLabel();
        numero_pe = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        remitente_pe = new javax.swing.JLabel();
        fecha_pag_pe = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        destinatario_pe = new javax.swing.JLabel();
        reg_pe = new javax.swing.JButton();
        JPpp = new javax.swing.JPanel();
        jLabel159 = new javax.swing.JLabel();
        empleado_pp = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        codigo_pp = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        proveedor_pp = new javax.swing.JLabel();
        fecha_pag_pp = new javax.swing.JLabel();
        jLabel179 = new javax.swing.JLabel();
        jLabel180 = new javax.swing.JLabel();
        total_pp = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel181 = new javax.swing.JLabel();
        jsTabla_ciu22 = new javax.swing.JScrollPane();
        JTenc_pp = new javax.swing.JTable();
        res_enc_pp = new javax.swing.JLabel();
        Lepp = new javax.swing.JLabel();
        Bepp = new javax.swing.JTextField();
        JCepp = new javax.swing.JComboBox<>();
        jLabel182 = new javax.swing.JLabel();
        jl_titulo30 = new javax.swing.JLabel();
        reg_pp = new javax.swing.JButton();
        jl_titulo35 = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        jsTabla_ciu23 = new javax.swing.JScrollPane();
        JTdet_pp = new javax.swing.JTable();
        JCdpp = new javax.swing.JComboBox<>();
        Bdpp = new javax.swing.JTextField();
        Ldpp = new javax.swing.JLabel();
        res_det_pp = new javax.swing.JLabel();
        JPiva_fp = new javax.swing.JPanel();
        jsTabla_ciu20 = new javax.swing.JScrollPane();
        JTiva = new javax.swing.JTable();
        JCiva = new javax.swing.JComboBox<>();
        jLabel169 = new javax.swing.JLabel();
        Biva = new javax.swing.JTextField();
        Liva = new javax.swing.JLabel();
        res_iva = new javax.swing.JLabel();
        jl_titulo33 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel170 = new javax.swing.JLabel();
        jsTabla_ciu21 = new javax.swing.JScrollPane();
        JTfp = new javax.swing.JTable();
        JPiva = new javax.swing.JPanel();
        jLabel171 = new javax.swing.JLabel();
        id_iva = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        impuesto_iva = new javax.swing.JLabel();
        elim_iva = new javax.swing.JButton();
        reg_iva = new javax.swing.JButton();
        Bfp = new javax.swing.JTextField();
        JCfp = new javax.swing.JComboBox<>();
        jLabel173 = new javax.swing.JLabel();
        jl_titulo34 = new javax.swing.JLabel();
        res_fp = new javax.swing.JLabel();
        Lfp = new javax.swing.JLabel();
        jSeparator31 = new javax.swing.JSeparator();
        jPanel40 = new javax.swing.JPanel();
        jLabel174 = new javax.swing.JLabel();
        JPfp = new javax.swing.JPanel();
        jLabel176 = new javax.swing.JLabel();
        jLabel177 = new javax.swing.JLabel();
        nombre_fp = new javax.swing.JLabel();
        id_fp = new javax.swing.JLabel();
        elim_fp = new javax.swing.JButton();
        mod_fp = new javax.swing.JButton();
        reg_fp = new javax.swing.JButton();
        PERSONAS = new javax.swing.JTabbedPane();
        JScli_emp = new javax.swing.JScrollPane();
        JPcli_emp = new javax.swing.JPanel();
        jsTabla_ciu4 = new javax.swing.JScrollPane();
        JTcli = new javax.swing.JTable();
        JCcli = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        Bcli = new javax.swing.JTextField();
        Lcli = new javax.swing.JLabel();
        res_cli = new javax.swing.JLabel();
        jl_titulo17 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel23 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jsTabla_ciu5 = new javax.swing.JScrollPane();
        JTemp = new javax.swing.JTable();
        jl_titulo20 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        JCper = new javax.swing.JComboBox<>();
        Bper = new javax.swing.JTextField();
        Lper = new javax.swing.JLabel();
        jsTabla_ciu6 = new javax.swing.JScrollPane();
        JTper = new javax.swing.JTable();
        res_per = new javax.swing.JLabel();
        JPcli = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        cedula_cli = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        id_cli = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        nombre_cli = new javax.swing.JLabel();
        apellido_cli = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        celular_cli = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        fecha_nac_cli = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        sexo_cli = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        descuento_cli = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        fecha_reg_cli = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        ciudad_cli = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        direccion_cli = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        email_cli = new javax.swing.JLabel();
        elim_cli = new javax.swing.JButton();
        reg_cli = new javax.swing.JButton();
        mod_cli = new javax.swing.JButton();
        Bemp = new javax.swing.JTextField();
        JCemp = new javax.swing.JComboBox<>();
        jLabel93 = new javax.swing.JLabel();
        jl_titulo19 = new javax.swing.JLabel();
        res_emp = new javax.swing.JLabel();
        Lemp = new javax.swing.JLabel();
        JPemp = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        cedula_emp = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        id_emp = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        nombre_emp = new javax.swing.JLabel();
        apellido_emp = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        celular_emp = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        fecha_nac_emp = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        sexo_emp = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        departamento_emp = new javax.swing.JLabel();
        fecha_reg_emp = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        ciudad_emp = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        direccion_emp = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        email_emp = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        puesto_emp = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        sueldo_emp = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        elim_emp = new javax.swing.JButton();
        mod_emp = new javax.swing.JButton();
        reg_emp = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        JPgen_des = new javax.swing.JPanel();
        jsTabla_ciu18 = new javax.swing.JScrollPane();
        JTgen = new javax.swing.JTable();
        JCgen = new javax.swing.JComboBox<>();
        jLabel160 = new javax.swing.JLabel();
        Bgen = new javax.swing.JTextField();
        Lgen = new javax.swing.JLabel();
        res_gen = new javax.swing.JLabel();
        jl_titulo31 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel161 = new javax.swing.JLabel();
        jsTabla_ciu19 = new javax.swing.JScrollPane();
        JTdes = new javax.swing.JTable();
        JPgen = new javax.swing.JPanel();
        jLabel162 = new javax.swing.JLabel();
        id_gen = new javax.swing.JLabel();
        jLabel163 = new javax.swing.JLabel();
        sexo_gen = new javax.swing.JLabel();
        elim_gen = new javax.swing.JButton();
        reg_gen = new javax.swing.JButton();
        mod_gen = new javax.swing.JButton();
        Bdes = new javax.swing.JTextField();
        JCdes = new javax.swing.JComboBox<>();
        jLabel164 = new javax.swing.JLabel();
        jl_titulo32 = new javax.swing.JLabel();
        res_des = new javax.swing.JLabel();
        Ldes = new javax.swing.JLabel();
        jSeparator30 = new javax.swing.JSeparator();
        jPanel38 = new javax.swing.JPanel();
        jLabel165 = new javax.swing.JLabel();
        JPdes = new javax.swing.JPanel();
        jLabel166 = new javax.swing.JLabel();
        jLabel167 = new javax.swing.JLabel();
        porcentaje_des = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        nombre_des = new javax.swing.JLabel();
        id_des = new javax.swing.JLabel();
        elim_des = new javax.swing.JButton();
        mod_des = new javax.swing.JButton();
        reg_des = new javax.swing.JButton();
        JPpue_dep = new javax.swing.JPanel();
        jsTabla_ciu7 = new javax.swing.JScrollPane();
        JTpue = new javax.swing.JTable();
        JCpue = new javax.swing.JComboBox<>();
        jLabel81 = new javax.swing.JLabel();
        Bpue = new javax.swing.JTextField();
        Lpue = new javax.swing.JLabel();
        res_pue = new javax.swing.JLabel();
        jl_titulo18 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jsTabla_ciu8 = new javax.swing.JScrollPane();
        JTdep = new javax.swing.JTable();
        JPpue = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        sueldo_pue = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        id_pue = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        nombre_pue = new javax.swing.JLabel();
        elim_pue = new javax.swing.JButton();
        reg_pue = new javax.swing.JButton();
        mod_pue = new javax.swing.JButton();
        Bdep = new javax.swing.JTextField();
        JCdep = new javax.swing.JComboBox<>();
        jLabel109 = new javax.swing.JLabel();
        jl_titulo22 = new javax.swing.JLabel();
        res_dep = new javax.swing.JLabel();
        Ldep = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JSeparator();
        jPanel30 = new javax.swing.JPanel();
        jLabel126 = new javax.swing.JLabel();
        JPdep = new javax.swing.JPanel();
        jLabel128 = new javax.swing.JLabel();
        id_dep = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        nombre_dep = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        descripcion_dep = new javax.swing.JTextArea();
        jLabel157 = new javax.swing.JLabel();
        reg_dep = new javax.swing.JButton();
        mod_dep = new javax.swing.JButton();
        elim_dep = new javax.swing.JButton();
        JPproductos = new javax.swing.JPanel();
        jl_titulo24 = new javax.swing.JLabel();
        jsTabla_ciu11 = new javax.swing.JScrollPane();
        JTpro = new javax.swing.JTable();
        jLabel114 = new javax.swing.JLabel();
        JCpro = new javax.swing.JComboBox<>();
        Bpro = new javax.swing.JTextField();
        Lpro = new javax.swing.JLabel();
        res_pro = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel103 = new javax.swing.JLabel();
        JPpro = new javax.swing.JPanel();
        jLabel104 = new javax.swing.JLabel();
        maximos_pro = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        codigo_pro = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        nombre_pro = new javax.swing.JLabel();
        minimos_pro = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        precio_pro = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        marca_pro = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        stock_pro = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        fecha_reg_pro = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        categoria_pro = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        proveedor_pro = new javax.swing.JLabel();
        imagen_pro = new javax.swing.JLabel();
        elim_pro = new javax.swing.JButton();
        mod_pro = new javax.swing.JButton();
        reg_pro = new javax.swing.JButton();
        JPmar_cat = new javax.swing.JPanel();
        jsTabla_ciu9 = new javax.swing.JScrollPane();
        JTmar = new javax.swing.JTable();
        JCmar = new javax.swing.JComboBox<>();
        jLabel113 = new javax.swing.JLabel();
        Bmar = new javax.swing.JTextField();
        Lmar = new javax.swing.JLabel();
        res_mar = new javax.swing.JLabel();
        jl_titulo21 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel115 = new javax.swing.JLabel();
        jsTabla_ciu10 = new javax.swing.JScrollPane();
        JTcat = new javax.swing.JTable();
        JPmar = new javax.swing.JPanel();
        jLabel119 = new javax.swing.JLabel();
        id_mar = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        nombre_mar = new javax.swing.JLabel();
        elim_mar = new javax.swing.JButton();
        reg_mar = new javax.swing.JButton();
        mod_mar = new javax.swing.JButton();
        Bcat = new javax.swing.JTextField();
        JCcat = new javax.swing.JComboBox<>();
        jLabel121 = new javax.swing.JLabel();
        jl_titulo23 = new javax.swing.JLabel();
        res_cat = new javax.swing.JLabel();
        Lcat = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JSeparator();
        jPanel29 = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        JPcat = new javax.swing.JPanel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        id_cat = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        nombre_cat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion_cat = new javax.swing.JTextArea();
        elim_cat = new javax.swing.JButton();
        mod_cat = new javax.swing.JButton();
        reg_cat = new javax.swing.JButton();
        JPprov_suc = new javax.swing.JPanel();
        jsTabla_ciu12 = new javax.swing.JScrollPane();
        JTprov = new javax.swing.JTable();
        JCprov = new javax.swing.JComboBox<>();
        jLabel118 = new javax.swing.JLabel();
        Bprov = new javax.swing.JTextField();
        Lprov = new javax.swing.JLabel();
        res_prov = new javax.swing.JLabel();
        jl_titulo25 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel130 = new javax.swing.JLabel();
        jsTabla_ciu13 = new javax.swing.JScrollPane();
        JTsuc = new javax.swing.JTable();
        JPprov = new javax.swing.JPanel();
        jLabel131 = new javax.swing.JLabel();
        ruc_prov = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        nombre_prov = new javax.swing.JLabel();
        celular_prov = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        fecha_reg_prov = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        email_prov = new javax.swing.JLabel();
        jLabel140 = new javax.swing.JLabel();
        ciudad_prov = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        elim_prov = new javax.swing.JButton();
        reg_prov = new javax.swing.JButton();
        mod_prov = new javax.swing.JButton();
        Bsuc = new javax.swing.JTextField();
        JCsuc = new javax.swing.JComboBox<>();
        jLabel133 = new javax.swing.JLabel();
        jl_titulo26 = new javax.swing.JLabel();
        res_suc = new javax.swing.JLabel();
        Lsuc = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JSeparator();
        jPanel34 = new javax.swing.JPanel();
        jLabel143 = new javax.swing.JLabel();
        JPsuc = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        id_suc = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        nombre_suc = new javax.swing.JLabel();
        ciudad_suc = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        reg_suc = new javax.swing.JButton();
        mod_suc = new javax.swing.JButton();
        elim_suc = new javax.swing.JButton();
        JPciu_provi = new javax.swing.JPanel();
        jsTabla_ciu14 = new javax.swing.JScrollPane();
        JTciu = new javax.swing.JTable();
        JCciu = new javax.swing.JComboBox<>();
        jLabel134 = new javax.swing.JLabel();
        Bciu = new javax.swing.JTextField();
        Lciu = new javax.swing.JLabel();
        res_ciu = new javax.swing.JLabel();
        jl_titulo27 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel135 = new javax.swing.JLabel();
        jsTabla_ciu15 = new javax.swing.JScrollPane();
        JTprovi = new javax.swing.JTable();
        JPciu = new javax.swing.JPanel();
        jLabel136 = new javax.swing.JLabel();
        id_ciu = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        nombre_ciu = new javax.swing.JLabel();
        id_provi_ciu = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        elim_ciu = new javax.swing.JButton();
        reg_ciu = new javax.swing.JButton();
        mod_ciu = new javax.swing.JButton();
        Bprovi = new javax.swing.JTextField();
        JCprovi = new javax.swing.JComboBox<>();
        jLabel150 = new javax.swing.JLabel();
        jl_titulo28 = new javax.swing.JLabel();
        res_provi = new javax.swing.JLabel();
        Lprovi = new javax.swing.JLabel();
        jSeparator28 = new javax.swing.JSeparator();
        jPanel35 = new javax.swing.JPanel();
        jLabel151 = new javax.swing.JLabel();
        JPprovi = new javax.swing.JPanel();
        jLabel152 = new javax.swing.JLabel();
        id_provi = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        nombre_provi = new javax.swing.JLabel();
        reg_provi = new javax.swing.JButton();
        mod_provi = new javax.swing.JButton();
        elim_provi = new javax.swing.JButton();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proyecto Final");
        setBackground(new java.awt.Color(0, 0, 0));
        setMinimumSize(new java.awt.Dimension(1020, 625));
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fecha_hora.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        fecha_hora.setForeground(new java.awt.Color(255, 255, 255));
        fecha_hora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fecha_hora.setText("fecha_hora");
        getContentPane().add(fecha_hora, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 580, 250, 20));

        usuario.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verificado.png"))); // NOI18N
        usuario.setText("usuario");
        usuario.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        getContentPane().add(usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 380, 40));

        empresa.setFont(new java.awt.Font("Kristen ITC", 1, 24)); // NOI18N
        empresa.setText("CENTRO COMERCIAL");
        empresa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        getContentPane().add(empresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 340, 40));

        cerrar_sesion.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cerrar_sesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar-sesion (1).png"))); // NOI18N
        cerrar_sesion.setText("Cerrar Sesión");
        cerrar_sesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cerrar_sesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cerrar_sesionMouseClicked(evt);
            }
        });
        getContentPane().add(cerrar_sesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 0, 130, 40));

        MENU.setBackground(new java.awt.Color(255, 255, 255));
        MENU.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        MENU.setMinimumSize(new java.awt.Dimension(980, 600));
        MENU.setPreferredSize(new java.awt.Dimension(980, 600));
        MENU.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MENUKeyPressed(evt);
            }
        });

        INICIO.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        INICIO.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        INICIO.setMinimumSize(new java.awt.Dimension(1075, 600));
        INICIO.setPreferredSize(new java.awt.Dimension(1075, 600));

        JSfacturar.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPfacturar.setBackground(new java.awt.Color(252, 240, 219));
        JPfacturar.setMaximumSize(new java.awt.Dimension(980, 1125));
        JPfacturar.setMinimumSize(new java.awt.Dimension(980, 1125));
        JPfacturar.setPreferredSize(new java.awt.Dimension(980, 1125));

        jPanel2.setBackground(new java.awt.Color(224, 255, 244));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setBackground(new java.awt.Color(0, 51, 153));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 51));
        jLabel3.setText("Factura");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 26, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Fecha de emisión:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 93, 158, -1));

        f_codigo.setEditable(false);
        f_codigo.setBackground(new java.awt.Color(255, 255, 255));
        f_codigo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        f_codigo.setText("Autogenerado");
        f_codigo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel2.add(f_codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 138, 234, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setText("Código de factura:");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 141, 158, -1));

        jPanel4.setBackground(new java.awt.Color(153, 0, 51));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Datos del cliente ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 20, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel13.setText("Cédula:");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 86, 111, 28));

        f_ced_cli.setEditable(false);
        f_ced_cli.setBackground(new java.awt.Color(255, 0, 0));
        f_ced_cli.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_ced_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_ced_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f_ced_cliMouseClicked(evt);
            }
        });
        f_ced_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_ced_cliActionPerformed(evt);
            }
        });
        jPanel2.add(f_ced_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 86, 302, -1));

        f_nom_ape.setEditable(false);
        f_nom_ape.setBackground(new java.awt.Color(255, 255, 255));
        f_nom_ape.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_nom_ape.setText(" ");
        jPanel2.add(f_nom_ape, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 172, 303, -1));

        f_dir.setEditable(false);
        f_dir.setBackground(new java.awt.Color(255, 255, 255));
        f_dir.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_dir.setText(" ");
        jPanel2.add(f_dir, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 215, 303, -1));

        f_tel.setEditable(false);
        f_tel.setBackground(new java.awt.Color(255, 255, 255));
        f_tel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_tel.setText(" ");
        jPanel2.add(f_tel, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 258, 303, -1));

        f_email.setEditable(false);
        f_email.setBackground(new java.awt.Color(255, 255, 255));
        f_email.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_email.setText(" ");
        jPanel2.add(f_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 301, 303, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel16.setText("Teléfono:");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 261, 109, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel15.setText("Dirección:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 215, 109, 26));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel14.setText("Razón social:");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 172, -1, 27));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel18.setText("Email:");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 301, 109, 28));

        f_fecha.setEditable(false);
        f_fecha.setBackground(new java.awt.Color(255, 0, 0));
        f_fecha.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_fecha.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_fecha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f_fechaMouseClicked(evt);
            }
        });
        f_fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_fechaActionPerformed(evt);
            }
        });
        jPanel2.add(f_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 90, 234, -1));

        jLabel1.setText("Click para generar fecha de emisión...");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 74, 234, -1));

        jLabel2.setText("Click para seleccionar o cambiar cliente...");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, 302, -1));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel19.setText("ID de cliente:");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 130, 111, 27));

        f_id_cli.setEditable(false);
        f_id_cli.setBackground(new java.awt.Color(255, 255, 255));
        f_id_cli.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_id_cli.setText(" ");
        jPanel2.add(f_id_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 129, 302, -1));

        JTdetalle = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdetalle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdetalle.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        JTdetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdetalle.setFocusable(false);
        JTdetalle.setGridColor(new java.awt.Color(255, 255, 255));
        JTdetalle.setOpaque(false);
        JTdetalle.setRowHeight(30);
        JTdetalle.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdetalle.getTableHeader().setResizingAllowed(false);
        JTdetalle.getTableHeader().setReorderingAllowed(false);
        JTdetalle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTdetalleMouseClicked(evt);
            }
        });
        jsTabla_ciu1.setViewportView(JTdetalle);

        jPanel2.add(jsTabla_ciu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 432, 901, 160));

        JBseleccionar_pro.setBackground(new java.awt.Color(153, 0, 51));
        JBseleccionar_pro.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBseleccionar_pro.setForeground(new java.awt.Color(255, 255, 255));
        JBseleccionar_pro.setText("+    Seleccionar producto");
        JBseleccionar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBseleccionar_pro.setEnabled(false);
        JBseleccionar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBseleccionar_proActionPerformed(evt);
            }
        });
        jPanel2.add(JBseleccionar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 602, -1, 38));

        f_num_det.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        f_num_det.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        f_num_det.setText("Detalles: 0");
        jPanel2.add(f_num_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(786, 602, 147, -1));

        jPanel6.setBackground(new java.awt.Color(153, 0, 51));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Detalle/s");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 402, 901, -1));

        jPanel7.setBackground(new java.awt.Color(252, 240, 219));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(153, 0, 51));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Totales");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 427, -1));

        jl_num_det1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det1.setText("Subtotal:");
        jPanel7.add(jl_num_det1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 96, 151, 28));

        jl_total1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total1.setText("Descuento:");
        jPanel7.add(jl_total1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 51, 151, 28));

        jl_num_det2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det2.setText("Total a pagar:");
        jPanel7.add(jl_num_det2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 204, 151, 36));

        f_descuento.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        f_descuento.setForeground(new java.awt.Color(255, 51, 51));
        f_descuento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        f_descuento.setText("- $0");
        jPanel7.add(f_descuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 49, 130, -1));

        f_total.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        f_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        f_total.setText("$0");
        jPanel7.add(f_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 195, 130, -1));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(223, 246, 160, 10));

        f_iva.setBackground(new java.awt.Color(255, 0, 0));
        f_iva.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        f_iva.setForeground(new java.awt.Color(255, 255, 255));
        f_iva.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                f_ivaItemStateChanged(evt);
            }
        });
        jPanel7.add(f_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 139, 130, 36));

        jl_num_det3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det3.setText("Tipo de impositivo:");
        jPanel7.add(jl_num_det3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 139, -1, 36));

        f_subtotal.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        f_subtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        f_subtotal.setText("$0");
        jPanel7.add(f_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 94, 130, -1));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 680, -1, 270));

        l_des.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        l_des.setText("Descuento:");
        jPanel2.add(l_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(493, 344, 109, 28));

        f_des.setEditable(false);
        f_des.setBackground(new java.awt.Color(255, 255, 255));
        f_des.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_des.setText(" ");
        jPanel2.add(f_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 344, 303, -1));

        jPanel9.setBackground(new java.awt.Color(153, 0, 51));
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Forma de pago");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(514, 680, -1, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel22.setText("Forma de pago:");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(514, 743, -1, 40));

        jLabel20.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel20.setText("Empleado:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 255, 116, 26));

        f_emp.setEditable(false);
        f_emp.setBackground(new java.awt.Color(255, 0, 0));
        f_emp.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f_empMouseClicked(evt);
            }
        });
        f_emp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_empActionPerformed(evt);
            }
        });
        jPanel2.add(f_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 253, 276, -1));

        jLabel5.setText("Click para seleccionar o cambiar empleado...");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 237, 234, -1));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel23.setText("Sucursal:");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 199, 116, 26));

        f_suc.setEditable(false);
        f_suc.setBackground(new java.awt.Color(255, 0, 0));
        f_suc.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        f_suc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        f_suc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                f_sucMouseClicked(evt);
            }
        });
        f_suc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f_sucActionPerformed(evt);
            }
        });
        jPanel2.add(f_suc, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 197, 276, -1));

        jLabel7.setText("Click para seleccionar o cambiar sucursal...");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 181, 276, -1));

        JBlimpiar_factura.setBackground(new java.awt.Color(255, 0, 0));
        JBlimpiar_factura.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBlimpiar_factura.setForeground(new java.awt.Color(255, 255, 255));
        JBlimpiar_factura.setText("VOLVER A EMPEZAR");
        JBlimpiar_factura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBlimpiar_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBlimpiar_facturaActionPerformed(evt);
            }
        });
        jPanel2.add(JBlimpiar_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(613, 886, 210, 50));

        JBcrear_factura.setBackground(new java.awt.Color(0, 204, 0));
        JBcrear_factura.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBcrear_factura.setForeground(new java.awt.Color(255, 255, 255));
        JBcrear_factura.setText("CREAR FACTURA");
        JBcrear_factura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBcrear_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBcrear_facturaActionPerformed(evt);
            }
        });
        jPanel2.add(JBcrear_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(613, 824, 210, 50));

        f_forma_pago.setBackground(new java.awt.Color(255, 0, 0));
        f_forma_pago.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        f_forma_pago.setForeground(new java.awt.Color(255, 255, 255));
        f_forma_pago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione..." }));
        f_forma_pago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                f_forma_pagoItemStateChanged(evt);
            }
        });
        jPanel2.add(f_forma_pago, new org.netbeans.lib.awtextra.AbsoluteConstraints(654, 743, 279, 40));

        javax.swing.GroupLayout JPfacturarLayout = new javax.swing.GroupLayout(JPfacturar);
        JPfacturar.setLayout(JPfacturarLayout);
        JPfacturarLayout.setHorizontalGroup(
            JPfacturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        JPfacturarLayout.setVerticalGroup(
            JPfacturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        JSfacturar.setViewportView(JPfacturar);

        INICIO.addTab("Facturar", JSfacturar);
        JSfacturar.getVerticalScrollBar().setUnitIncrement(16);

        JSventas.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JSventas.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPventas.setBackground(new java.awt.Color(224, 255, 244));
        JPventas.setMaximumSize(new java.awt.Dimension(980, 650));
        JPventas.setMinimumSize(new java.awt.Dimension(980, 650));
        JPventas.setPreferredSize(new java.awt.Dimension(980, 650));
        JPventas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTenc = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTenc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTenc.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTenc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTenc.setFocusable(false);
        JTenc.setGridColor(new java.awt.Color(255, 255, 255));
        JTenc.setOpaque(false);
        JTenc.setRowHeight(30);
        JTenc.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTenc.getTableHeader().setResizingAllowed(false);
        JTenc.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu2.setViewportView(JTenc);

        JPventas.add(jsTabla_ciu2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 68, 877, 140));

        JTdet = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdet.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTdet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdet.setFocusable(false);
        JTdet.setGridColor(new java.awt.Color(255, 255, 255));
        JTdet.setOpaque(false);
        JTdet.setRowHeight(30);
        JTdet.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdet.getTableHeader().setResizingAllowed(false);
        JTdet.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu3.setViewportView(JTdet);

        JPventas.add(jsTabla_ciu3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 412, 380, 165));

        JCenc.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCenc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CODIGO", "ID_SUCURSAL", "ID_EMPLEADO", "ID_CLIENTE", "FECHA_REGISTRO", "ESTADO" }));
        JCenc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCencItemStateChanged(evt);
            }
        });
        JPventas.add(JCenc, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 21, 130, 35));

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Buscar por");
        JPventas.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(548, 21, -1, 34));

        Benc.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Benc.setText("Buscar");
        Benc.setMinimumSize(new java.awt.Dimension(317, 31));
        Benc.setPreferredSize(new java.awt.Dimension(317, 35));
        Benc.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Benc.setSelectionColor(new java.awt.Color(153, 204, 255));
        Benc.setSelectionEnd(0);
        Benc.setSelectionStart(0);
        Benc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BencMouseClicked(evt);
            }
        });
        Benc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BencKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BencKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BencKeyTyped(evt);
            }
        });
        JPventas.add(Benc, new org.netbeans.lib.awtextra.AbsoluteConstraints(767, 20, 150, -1));

        Lenc.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lenc.setForeground(new java.awt.Color(0, 102, 102));
        Lenc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lenc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lenc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lenc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LencMouseClicked(evt);
            }
        });
        JPventas.add(Lenc, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, -1, 34));

        res_enc.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_enc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_enc.setText("Resultados: 0 de 0");
        JPventas.add(res_enc, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 214, 877, -1));

        jl_titulo6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo6.setText("Encabezados de factura");
        jl_titulo6.setIconTextGap(10);
        jl_titulo6.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 24, -1, -1));

        jl_titulo8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo8.setText("Detalles de factura");
        jl_titulo8.setIconTextGap(10);
        jl_titulo8.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 287, 372, -1));

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Buscar por");
        JPventas.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 335, -1, 34));

        res_det.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_det.setText("Resultados: 0 de 0");
        JPventas.add(res_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 382, 207, -1));

        JCdet.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CODIGO", "CÓDIGO_PRODUCTO", "CANTIDAD", "SUBTOTAL", "CODIGO_ENCABEZADO" }));
        JCdet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdetItemStateChanged(evt);
            }
        });
        JPventas.add(JCdet, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 335, 130, 35));

        Bdet.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bdet.setText("Buscar");
        Bdet.setMinimumSize(new java.awt.Dimension(317, 31));
        Bdet.setPreferredSize(new java.awt.Dimension(317, 35));
        Bdet.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bdet.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bdet.setSelectionEnd(0);
        Bdet.setSelectionStart(0);
        Bdet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BdetMouseClicked(evt);
            }
        });
        Bdet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BdetKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BdetKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BdetKeyTyped(evt);
            }
        });
        JPventas.add(Bdet, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 334, 161, -1));

        Ldet.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Ldet.setForeground(new java.awt.Color(0, 102, 102));
        Ldet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ldet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Ldet.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ldet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LdetMouseClicked(evt);
            }
        });
        JPventas.add(Ldet, new org.netbeans.lib.awtextra.AbsoluteConstraints(423, 334, -1, 34));

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPventas.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 259, 877, 16));

        JTpag = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpag.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTpag.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTpag.setFocusable(false);
        JTpag.setGridColor(new java.awt.Color(255, 255, 255));
        JTpag.setOpaque(false);
        JTpag.setRowHeight(30);
        JTpag.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTpag.getTableHeader().setResizingAllowed(false);
        JTpag.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu17.setViewportView(JTpag);

        JPventas.add(jsTabla_ciu17, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 413, 412, 165));

        Lpag.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lpag.setForeground(new java.awt.Color(0, 102, 102));
        Lpag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lpag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lpag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lpag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LpagMouseClicked(evt);
            }
        });
        JPventas.add(Lpag, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 334, -1, 34));

        Bpag.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bpag.setText("Buscar");
        Bpag.setMinimumSize(new java.awt.Dimension(317, 31));
        Bpag.setPreferredSize(new java.awt.Dimension(317, 35));
        Bpag.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bpag.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bpag.setSelectionEnd(0);
        Bpag.setSelectionStart(0);
        Bpag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BpagMouseClicked(evt);
            }
        });
        Bpag.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BpagKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BpagKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BpagKeyTyped(evt);
            }
        });
        JPventas.add(Bpag, new org.netbeans.lib.awtextra.AbsoluteConstraints(732, 334, 185, -1));

        JCpag.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NUMERO", "TOTAL_SIN_IVA", "ID_FORMA PAGO", "ID_IVA", "TOTAL_MAS_IVA", "CODIGO_ENC" }));
        JCpag.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpagItemStateChanged(evt);
            }
        });
        JPventas.add(JCpag, new org.netbeans.lib.awtextra.AbsoluteConstraints(582, 335, 138, 35));

        jl_titulo10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo10.setText("Pagos de factura");
        jl_titulo10.setIconTextGap(10);
        jl_titulo10.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo10, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 287, 412, -1));

        jLabel57.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Buscar por");
        JPventas.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 335, -1, 34));

        res_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pag.setText("Resultados: 0 de 0");
        JPventas.add(res_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 382, 242, -1));

        jSeparator29.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator29.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator29.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPventas.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 287, 19, 291));

        JSventas.setViewportView(JPventas);

        INICIO.addTab("Ventas", JSventas);
        JSventas.getVerticalScrollBar().setUnitIncrement(16);

        JPpagos.setBackground(new java.awt.Color(224, 255, 244));
        JPpagos.setMaximumSize(new java.awt.Dimension(980, 500));
        JPpagos.setMinimumSize(new java.awt.Dimension(980, 500));
        JPpagos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_titulo29.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo29.setText("Pagos a empleados");
        jl_titulo29.setIconTextGap(10);
        jl_titulo29.setVerifyInputWhenFocusTarget(false);
        JPpagos.add(jl_titulo29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, 35));

        jsTabla_ciu16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpe = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpe.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTpe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTpe.setFocusable(false);
        JTpe.setGridColor(new java.awt.Color(255, 255, 255));
        JTpe.setOpaque(false);
        JTpe.setRowHeight(30);
        JTpe.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTpe.getTableHeader().setResizingAllowed(false);
        JTpe.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu16.setViewportView(JTpe);

        JPpagos.add(jsTabla_ciu16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 899, 150));

        jLabel147.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("Buscar por");
        JPpagos.add(jLabel147, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 20, -1, 35));

        JCpe.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NUMERO", "ID_REMITENTE", "ID_DESTINATARIO", "TOTAL", "FECHA_REG" }));
        JCpe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpeItemStateChanged(evt);
            }
        });
        JPpagos.add(JCpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 20, 142, 35));

        Bpe.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bpe.setText("Buscar");
        Bpe.setMinimumSize(new java.awt.Dimension(317, 31));
        Bpe.setPreferredSize(new java.awt.Dimension(317, 35));
        Bpe.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bpe.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bpe.setSelectionEnd(0);
        Bpe.setSelectionStart(0);
        Bpe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BpeMouseClicked(evt);
            }
        });
        Bpe.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BpeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BpeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BpeKeyTyped(evt);
            }
        });
        JPpagos.add(Bpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(543, 20, 185, -1));

        Lpe.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lpe.setForeground(new java.awt.Color(0, 102, 102));
        Lpe.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lpe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lpe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lpe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LpeMouseClicked(evt);
            }
        });
        JPpagos.add(Lpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(731, 20, -1, 35));

        res_pe.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pe.setText("Resultados: 0 de 0");
        JPpagos.add(res_pe, new org.netbeans.lib.awtextra.AbsoluteConstraints(753, 20, 166, 35));

        jPanel27.setBackground(new java.awt.Color(153, 0, 51));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel27.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel148.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(255, 255, 255));
        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel148.setText("PAGO A EMPLEADO SELECCIONADO");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPpagos.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 239, 611, 47));

        JPpe.setBackground(new java.awt.Color(255, 255, 255));

        jLabel149.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel149.setText("Total:  $");

        total_pe.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        total_pe.setText("0");

        jLabel154.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel154.setText("Número:");

        numero_pe.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        numero_pe.setText("0");

        jLabel155.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel155.setText("Remitente:");

        remitente_pe.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        remitente_pe.setText("0");

        fecha_pag_pe.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_pag_pe.setText("0");

        jLabel156.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel156.setText("Fecha de pago:");

        jLabel158.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel158.setText("Destinatario:");

        destinatario_pe.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        destinatario_pe.setText("0");

        javax.swing.GroupLayout JPpeLayout = new javax.swing.GroupLayout(JPpe);
        JPpe.setLayout(JPpeLayout);
        JPpeLayout.setHorizontalGroup(
            JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel155)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remitente_pe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel158)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(destinatario_pe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel154)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numero_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel156)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_pag_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel149)
                        .addGap(3, 3, 3)
                        .addComponent(total_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        JPpeLayout.setVerticalGroup(
            JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpeLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel149)
                    .addComponent(total_pe)
                    .addComponent(jLabel154)
                    .addComponent(numero_pe))
                .addGap(15, 15, 15)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel155)
                    .addComponent(remitente_pe)
                    .addComponent(jLabel156)
                    .addComponent(fecha_pag_pe))
                .addGap(15, 15, 15)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel158)
                    .addComponent(destinatario_pe))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        JPpagos.add(JPpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(166, 286, -1, -1));

        reg_pe.setBackground(new java.awt.Color(0, 204, 102));
        reg_pe.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_pe.setForeground(new java.awt.Color(255, 255, 255));
        reg_pe.setText("+    Registrar");
        reg_pe.setBorder(null);
        reg_pe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_pe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_peMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_peMouseExited(evt);
            }
        });
        reg_pe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_peActionPerformed(evt);
            }
        });
        JPpagos.add(reg_pe, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 404, 309, 40));

        JPpp.setBackground(new java.awt.Color(255, 255, 255));

        jLabel159.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel159.setText("Empleado:");

        empleado_pp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        empleado_pp.setText("0");

        jLabel175.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel175.setText("Código:");

        codigo_pp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        codigo_pp.setText("0");

        jLabel178.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel178.setText("Proveedor:");

        proveedor_pp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        proveedor_pp.setText("0");

        fecha_pag_pp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_pag_pp.setText("0");

        jLabel179.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel179.setText("Fecha de pago:");

        jLabel180.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel180.setText("Total:");

        total_pp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        total_pp.setText("0");

        javax.swing.GroupLayout JPppLayout = new javax.swing.GroupLayout(JPpp);
        JPpp.setLayout(JPppLayout);
        JPppLayout.setHorizontalGroup(
            JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPppLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPppLayout.createSequentialGroup()
                        .addComponent(jLabel178)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(proveedor_pp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPppLayout.createSequentialGroup()
                        .addComponent(jLabel180)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(total_pp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPppLayout.createSequentialGroup()
                        .addComponent(jLabel175)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigo_pp, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPppLayout.createSequentialGroup()
                        .addComponent(jLabel179)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_pag_pp, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPppLayout.createSequentialGroup()
                        .addComponent(jLabel159)
                        .addGap(3, 3, 3)
                        .addComponent(empleado_pp, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPppLayout.setVerticalGroup(
            JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPppLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel159)
                    .addComponent(empleado_pp)
                    .addComponent(jLabel175)
                    .addComponent(codigo_pp))
                .addGap(15, 15, 15)
                .addGroup(JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel178)
                    .addComponent(proveedor_pp)
                    .addComponent(jLabel179)
                    .addComponent(fecha_pag_pp))
                .addGap(15, 15, 15)
                .addGroup(JPppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel180)
                    .addComponent(total_pp))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        JPpagos.add(JPpp, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 850, 611, -1));

        jPanel33.setBackground(new java.awt.Color(153, 0, 51));
        jPanel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel33.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel181.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel181.setForeground(new java.awt.Color(255, 255, 255));
        jLabel181.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel181.setText("PAGO A PROVEEDOR SELECCIONADO");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel181, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel181, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPpagos.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(192, 803, 611, 47));

        jsTabla_ciu22.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTenc_pp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTenc_pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTenc_pp.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTenc_pp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTenc_pp.setFocusable(false);
        JTenc_pp.setGridColor(new java.awt.Color(255, 255, 255));
        JTenc_pp.setOpaque(false);
        JTenc_pp.setRowHeight(30);
        JTenc_pp.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTenc_pp.getTableHeader().setResizingAllowed(false);
        JTenc_pp.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu22.setViewportView(JTenc_pp);

        JPpagos.add(jsTabla_ciu22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 601, 428, 150));

        res_enc_pp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_enc_pp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_enc_pp.setText("Resultados: 0 de 0");
        JPpagos.add(res_enc_pp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 751, 428, 35));

        Lepp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lepp.setForeground(new java.awt.Color(0, 102, 102));
        Lepp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lepp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lepp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lepp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LeppMouseClicked(evt);
            }
        });
        JPpagos.add(Lepp, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 560, -1, 35));

        Bepp.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bepp.setText("Buscar");
        Bepp.setMinimumSize(new java.awt.Dimension(317, 31));
        Bepp.setPreferredSize(new java.awt.Dimension(317, 35));
        Bepp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bepp.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bepp.setSelectionEnd(0);
        Bepp.setSelectionStart(0);
        Bepp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BeppMouseClicked(evt);
            }
        });
        Bepp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BeppKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BeppKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BeppKeyTyped(evt);
            }
        });
        JPpagos.add(Bepp, new org.netbeans.lib.awtextra.AbsoluteConstraints(246, 560, 202, -1));

        JCepp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCepp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CODIGO", "RUC", "TOTAL", "ID_EMPLEADO", "FECHA" }));
        JCepp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCeppItemStateChanged(evt);
            }
        });
        JPpagos.add(JCepp, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 560, 137, 35));

        jLabel182.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel182.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel182.setText("Buscar por");
        JPpagos.add(jLabel182, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, -1, 35));

        jl_titulo30.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo30.setText("Encabezados P. proveedores");
        jl_titulo30.setIconTextGap(10);
        jl_titulo30.setVerifyInputWhenFocusTarget(false);
        JPpagos.add(jl_titulo30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 512, 428, 35));

        reg_pp.setBackground(new java.awt.Color(0, 204, 102));
        reg_pp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_pp.setForeground(new java.awt.Color(255, 255, 255));
        reg_pp.setText("+    Registrar");
        reg_pp.setBorder(null);
        reg_pp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_pp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_ppMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_ppMouseExited(evt);
            }
        });
        reg_pp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_ppActionPerformed(evt);
            }
        });
        JPpagos.add(reg_pp, new org.netbeans.lib.awtextra.AbsoluteConstraints(343, 968, 309, 40));

        jl_titulo35.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo35.setText("Detalles P. proveedores");
        jl_titulo35.setIconTextGap(10);
        jl_titulo35.setVerifyInputWhenFocusTarget(false);
        JPpagos.add(jl_titulo35, new org.netbeans.lib.awtextra.AbsoluteConstraints(509, 511, 428, 35));

        jLabel183.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel183.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel183.setText("Buscar por");
        JPpagos.add(jLabel183, new org.netbeans.lib.awtextra.AbsoluteConstraints(509, 559, -1, 35));

        jsTabla_ciu23.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdet_pp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdet_pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdet_pp.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTdet_pp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdet_pp.setFocusable(false);
        JTdet_pp.setGridColor(new java.awt.Color(255, 255, 255));
        JTdet_pp.setOpaque(false);
        JTdet_pp.setRowHeight(30);
        JTdet_pp.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdet_pp.getTableHeader().setResizingAllowed(false);
        JTdet_pp.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu23.setViewportView(JTdet_pp);

        JPpagos.add(jsTabla_ciu23, new org.netbeans.lib.awtextra.AbsoluteConstraints(509, 600, 428, 150));

        JCdpp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdpp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CODIGO", "PRODUCTO", "CANTIDAD", "SUBTOTAL", "NUM_COMPRA" }));
        JCdpp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdppItemStateChanged(evt);
            }
        });
        JPpagos.add(JCdpp, new org.netbeans.lib.awtextra.AbsoluteConstraints(586, 559, 137, 35));

        Bdpp.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bdpp.setText("Buscar");
        Bdpp.setMinimumSize(new java.awt.Dimension(317, 31));
        Bdpp.setPreferredSize(new java.awt.Dimension(317, 35));
        Bdpp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bdpp.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bdpp.setSelectionEnd(0);
        Bdpp.setSelectionStart(0);
        Bdpp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BdppMouseClicked(evt);
            }
        });
        Bdpp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BdppKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BdppKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BdppKeyTyped(evt);
            }
        });
        JPpagos.add(Bdpp, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 559, 202, -1));

        Ldpp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Ldpp.setForeground(new java.awt.Color(0, 102, 102));
        Ldpp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ldpp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Ldpp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ldpp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LdppMouseClicked(evt);
            }
        });
        JPpagos.add(Ldpp, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 559, -1, 35));

        res_det_pp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_det_pp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_det_pp.setText("Resultados: 0 de 0");
        JPpagos.add(res_det_pp, new org.netbeans.lib.awtextra.AbsoluteConstraints(509, 750, 428, 35));

        JSpagos.setViewportView(JPpagos);

        INICIO.addTab("Pagos", JSpagos);
        JSpagos.getVerticalScrollBar().setUnitIncrement(16);

        JPiva_fp.setBackground(new java.awt.Color(224, 255, 244));
        JPiva_fp.setMaximumSize(new java.awt.Dimension(980, 500));
        JPiva_fp.setMinimumSize(new java.awt.Dimension(980, 500));
        JPiva_fp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu20.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTiva = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTiva.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTiva.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTiva.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTiva.setFocusable(false);
        JTiva.setGridColor(new java.awt.Color(255, 255, 255));
        JTiva.setOpaque(false);
        JTiva.setRowHeight(30);
        JTiva.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTiva.getTableHeader().setResizingAllowed(false);
        JTiva.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu20.setViewportView(JTiva);

        JPiva_fp.add(jsTabla_ciu20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 420, 150));

        JCiva.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCiva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "IMPUESTO" }));
        JCiva.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCivaItemStateChanged(evt);
            }
        });
        JPiva_fp.add(JCiva, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel169.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel169.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel169.setText("Buscar por");
        JPiva_fp.add(jLabel169, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, -1, 34));

        Biva.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Biva.setText("Buscar");
        Biva.setMinimumSize(new java.awt.Dimension(317, 31));
        Biva.setPreferredSize(new java.awt.Dimension(317, 35));
        Biva.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Biva.setSelectionColor(new java.awt.Color(153, 204, 255));
        Biva.setSelectionEnd(0);
        Biva.setSelectionStart(0);
        Biva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BivaMouseClicked(evt);
            }
        });
        Biva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BivaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BivaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BivaKeyTyped(evt);
            }
        });
        JPiva_fp.add(Biva, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 64, 195, -1));

        Liva.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Liva.setForeground(new java.awt.Color(0, 102, 102));
        Liva.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Liva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Liva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Liva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LivaMouseClicked(evt);
            }
        });
        JPiva_fp.add(Liva, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 64, -1, 35));

        res_iva.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_iva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_iva.setText("Resultados: 0 de 0");
        JPiva_fp.add(res_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 20, 204, 32));

        jl_titulo33.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo33.setText("IVA-s");
        jl_titulo33.setIconTextGap(10);
        jl_titulo33.setVerifyInputWhenFocusTarget(false);
        JPiva_fp.add(jl_titulo33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 204, -1));

        jPanel39.setBackground(new java.awt.Color(153, 0, 51));
        jPanel39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel39.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel170.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel170.setForeground(new java.awt.Color(255, 255, 255));
        jLabel170.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel170.setText("IVA SELECCIONADO");

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel170, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel170, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPiva_fp.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 420, 47));

        jsTabla_ciu21.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTfp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTfp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTfp.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTfp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTfp.setFocusable(false);
        JTfp.setGridColor(new java.awt.Color(255, 255, 255));
        JTfp.setOpaque(false);
        JTfp.setRowHeight(30);
        JTfp.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTfp.getTableHeader().setResizingAllowed(false);
        JTfp.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu21.setViewportView(JTfp);

        JPiva_fp.add(jsTabla_ciu21, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 111, 422, 150));

        JPiva.setBackground(new java.awt.Color(255, 255, 255));

        jLabel171.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel171.setText("ID:");

        id_iva.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_iva.setText("0");

        jLabel172.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel172.setText("Impuesto: ");

        impuesto_iva.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        impuesto_iva.setText("0");

        javax.swing.GroupLayout JPivaLayout = new javax.swing.GroupLayout(JPiva);
        JPiva.setLayout(JPivaLayout);
        JPivaLayout.setHorizontalGroup(
            JPivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPivaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPivaLayout.createSequentialGroup()
                        .addComponent(jLabel171)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPivaLayout.createSequentialGroup()
                        .addComponent(jLabel172)
                        .addGap(3, 3, 3)
                        .addComponent(impuesto_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPivaLayout.setVerticalGroup(
            JPivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPivaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel171)
                    .addComponent(id_iva))
                .addGap(15, 15, 15)
                .addGroup(JPivaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel172)
                    .addComponent(impuesto_iva))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPiva_fp.add(JPiva, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 420, -1));

        elim_iva.setBackground(new java.awt.Color(255, 0, 51));
        elim_iva.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_iva.setForeground(new java.awt.Color(255, 255, 255));
        elim_iva.setText("x    Eliminar");
        elim_iva.setBorder(null);
        elim_iva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_iva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_ivaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_ivaMouseExited(evt);
            }
        });
        elim_iva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_ivaActionPerformed(evt);
            }
        });
        JPiva_fp.add(elim_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 425, 201, 40));

        reg_iva.setBackground(new java.awt.Color(0, 204, 102));
        reg_iva.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_iva.setForeground(new java.awt.Color(255, 255, 255));
        reg_iva.setText("+    Registrar");
        reg_iva.setBorder(null);
        reg_iva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_iva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_ivaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_ivaMouseExited(evt);
            }
        });
        reg_iva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_ivaActionPerformed(evt);
            }
        });
        JPiva_fp.add(reg_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 425, 201, 40));

        Bfp.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bfp.setText("Buscar");
        Bfp.setMinimumSize(new java.awt.Dimension(317, 31));
        Bfp.setPreferredSize(new java.awt.Dimension(317, 35));
        Bfp.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bfp.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bfp.setSelectionEnd(0);
        Bfp.setSelectionStart(0);
        Bfp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BfpMouseClicked(evt);
            }
        });
        Bfp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BfpKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BfpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BfpKeyTyped(evt);
            }
        });
        JPiva_fp.add(Bfp, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 63, 197, -1));

        JCfp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCfp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE" }));
        JCfp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCfpItemStateChanged(evt);
            }
        });
        JPiva_fp.add(JCfp, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 64, 142, 35));

        jLabel173.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel173.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel173.setText("Buscar por");
        JPiva_fp.add(jLabel173, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 63, -1, 34));

        jl_titulo34.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo34.setText("Formas de pago");
        jl_titulo34.setIconTextGap(10);
        jl_titulo34.setVerifyInputWhenFocusTarget(false);
        JPiva_fp.add(jl_titulo34, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 249, -1));

        res_fp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_fp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_fp.setText("Resultados: 0 de 0");
        JPiva_fp.add(res_fp, new org.netbeans.lib.awtextra.AbsoluteConstraints(755, 24, 167, -1));

        Lfp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lfp.setForeground(new java.awt.Color(0, 102, 102));
        Lfp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lfp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lfp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lfp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LfpMouseClicked(evt);
            }
        });
        JPiva_fp.add(Lfp, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 63, -1, 36));

        jSeparator31.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator31.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator31.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPiva_fp.add(jSeparator31, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 20, -1, 446));

        jPanel40.setBackground(new java.awt.Color(153, 0, 51));
        jPanel40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel40.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel174.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel174.setForeground(new java.awt.Color(255, 255, 255));
        jLabel174.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel174.setText("FORMA DE PAGO SELECCIONADA");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel174, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel174, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPiva_fp.add(jPanel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 283, 422, 47));

        JPfp.setBackground(new java.awt.Color(255, 255, 255));

        jLabel176.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel176.setText("ID:");

        jLabel177.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel177.setText("Nombre:");

        nombre_fp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_fp.setText("0");

        id_fp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_fp.setText("0");

        javax.swing.GroupLayout JPfpLayout = new javax.swing.GroupLayout(JPfp);
        JPfp.setLayout(JPfpLayout);
        JPfpLayout.setHorizontalGroup(
            JPfpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfpLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPfpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPfpLayout.createSequentialGroup()
                        .addComponent(jLabel176)
                        .addGap(6, 6, 6)
                        .addComponent(id_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPfpLayout.createSequentialGroup()
                        .addComponent(jLabel177)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPfpLayout.setVerticalGroup(
            JPfpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfpLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPfpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel176)
                    .addComponent(id_fp))
                .addGap(15, 15, 15)
                .addGroup(JPfpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel177)
                    .addComponent(nombre_fp))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        JPiva_fp.add(JPfp, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 422, -1));

        elim_fp.setBackground(new java.awt.Color(255, 0, 51));
        elim_fp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_fp.setForeground(new java.awt.Color(255, 255, 255));
        elim_fp.setText("x    Eliminar");
        elim_fp.setBorder(null);
        elim_fp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_fp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_fpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_fpMouseExited(evt);
            }
        });
        elim_fp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_fpActionPerformed(evt);
            }
        });
        JPiva_fp.add(elim_fp, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 426, 130, 40));

        mod_fp.setBackground(new java.awt.Color(51, 204, 255));
        mod_fp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_fp.setForeground(new java.awt.Color(255, 255, 255));
        mod_fp.setText("¡    Modificar");
        mod_fp.setBorder(null);
        mod_fp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_fp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_fpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_fpMouseExited(evt);
            }
        });
        mod_fp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_fpActionPerformed(evt);
            }
        });
        JPiva_fp.add(mod_fp, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 426, 136, 40));

        reg_fp.setBackground(new java.awt.Color(0, 204, 102));
        reg_fp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_fp.setForeground(new java.awt.Color(255, 255, 255));
        reg_fp.setText("+    Registrar");
        reg_fp.setBorder(null);
        reg_fp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_fp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_fpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_fpMouseExited(evt);
            }
        });
        reg_fp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_fpActionPerformed(evt);
            }
        });
        JPiva_fp.add(reg_fp, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 426, 130, 40));

        INICIO.addTab("IVA-s y Formas de pago", JPiva_fp);

        MENU.addTab("Inicio", INICIO);

        PERSONAS.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        PERSONAS.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N

        JScli_emp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JScli_emp.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPcli_emp.setBackground(new java.awt.Color(252, 240, 219));
        JPcli_emp.setMaximumSize(new java.awt.Dimension(980, 1050));
        JPcli_emp.setMinimumSize(new java.awt.Dimension(980, 1050));
        JPcli_emp.setPreferredSize(new java.awt.Dimension(980, 1050));
        JPcli_emp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTcli = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcli.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTcli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTcli.setFocusable(false);
        JTcli.setGridColor(new java.awt.Color(255, 255, 255));
        JTcli.setOpaque(false);
        JTcli.setRowHeight(30);
        JTcli.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTcli.getTableHeader().setResizingAllowed(false);
        JTcli.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu4.setViewportView(JTcli);

        JPcli_emp.add(jsTabla_ciu4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 378, 420, 170));

        JCcli.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCcli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "CEDULA", "ID_DESCUENTO" }));
        JCcli.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCcliItemStateChanged(evt);
            }
        });
        JPcli_emp.add(JCcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 331, 142, 35));

        jLabel72.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("Buscar por");
        JPcli_emp.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 331, -1, 34));

        Bcli.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bcli.setText("Buscar");
        Bcli.setMinimumSize(new java.awt.Dimension(317, 31));
        Bcli.setPreferredSize(new java.awt.Dimension(317, 35));
        Bcli.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bcli.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bcli.setSelectionEnd(0);
        Bcli.setSelectionStart(0);
        Bcli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BcliMouseClicked(evt);
            }
        });
        Bcli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BcliKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BcliKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BcliKeyTyped(evt);
            }
        });
        JPcli_emp.add(Bcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 331, 195, -1));

        Lcli.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lcli.setForeground(new java.awt.Color(0, 102, 102));
        Lcli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lcli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lcli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lcli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LcliMouseClicked(evt);
            }
        });
        JPcli_emp.add(Lcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 331, -1, 35));

        res_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_cli.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_cli.setText("Resultados: 0 de 0");
        JPcli_emp.add(res_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 287, 204, 32));

        jl_titulo17.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo17.setText("Clientes");
        jl_titulo17.setIconTextGap(10);
        jl_titulo17.setVerifyInputWhenFocusTarget(false);
        JPcli_emp.add(jl_titulo17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 287, 204, -1));

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPcli_emp.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 257, 899, 12));

        jPanel23.setBackground(new java.awt.Color(153, 0, 51));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel23.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel80.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("CLIENTE SELECCIONADO");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPcli_emp.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 569, 420, 47));

        jsTabla_ciu5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

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
        jsTabla_ciu5.setViewportView(JTemp);

        JPcli_emp.add(jsTabla_ciu5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 378, 422, 170));

        jl_titulo20.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo20.setText("PERSONAS");
        jl_titulo20.setIconTextGap(10);
        jl_titulo20.setVerifyInputWhenFocusTarget(false);
        JPcli_emp.add(jl_titulo20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 35));

        jLabel74.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel74.setText("Buscar por");
        JPcli_emp.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 20, -1, 35));

        JCper.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCper.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CEDULA", "NOMBRE", "APELLIDO", "FECHA_NAC", "ID_SEXO", "CELULAR", "EMAIL", "DIRECCION", "ID_CIUDAD", "FECHA_REGISTRO" }));
        JCper.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCperItemStateChanged(evt);
            }
        });
        JPcli_emp.add(JCper, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 20, 142, 35));

        Bper.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bper.setText("Buscar");
        Bper.setMinimumSize(new java.awt.Dimension(317, 31));
        Bper.setPreferredSize(new java.awt.Dimension(317, 35));
        Bper.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bper.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bper.setSelectionEnd(0);
        Bper.setSelectionStart(0);
        Bper.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BperMouseClicked(evt);
            }
        });
        Bper.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BperKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BperKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BperKeyTyped(evt);
            }
        });
        JPcli_emp.add(Bper, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 20, 185, -1));

        Lper.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lper.setForeground(new java.awt.Color(0, 102, 102));
        Lper.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lper.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lper.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LperMouseClicked(evt);
            }
        });
        JPcli_emp.add(Lper, new org.netbeans.lib.awtextra.AbsoluteConstraints(645, 20, -1, 35));

        jsTabla_ciu6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTper = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTper.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTper.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTper.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTper.setFocusable(false);
        JTper.setGridColor(new java.awt.Color(255, 255, 255));
        JTper.setOpaque(false);
        JTper.setRowHeight(30);
        JTper.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTper.getTableHeader().setResizingAllowed(false);
        JTper.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu6.setViewportView(JTper);

        JPcli_emp.add(jsTabla_ciu6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 899, 170));

        res_per.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_per.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_per.setText("Resultados: 0 de 0");
        JPcli_emp.add(res_per, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 20, 212, 35));

        JPcli.setBackground(new java.awt.Color(255, 255, 255));
        JPcli.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel63.setText("Cédula:");
        JPcli.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 20, -1, -1));

        cedula_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        cedula_cli.setText("0");
        JPcli.add(cedula_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 20, 123, -1));

        jLabel66.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel66.setText("ID:");
        JPcli.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        id_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        id_cli.setText("0");
        JPcli.add(id_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 20, 188, -1));

        jLabel68.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel68.setText("Nombre:");
        JPcli.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 53, -1, -1));

        nombre_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        nombre_cli.setText("0");
        JPcli.add(nombre_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 53, 154, -1));

        apellido_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        apellido_cli.setText("0");
        JPcli.add(apellido_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 53, 114, -1));

        jLabel71.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel71.setText("Apellido:");
        JPcli.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 53, -1, -1));

        jLabel75.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel75.setText("Celular:");
        JPcli.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 119, -1, -1));

        celular_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        celular_cli.setText("0");
        JPcli.add(celular_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 119, 161, -1));

        jLabel77.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel77.setText("Fecha de nacimiento:");
        JPcli.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 86, -1, -1));

        fecha_nac_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        fecha_nac_cli.setText("0");
        JPcli.add(fecha_nac_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 86, 81, -1));

        jLabel79.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel79.setText("Sexo:");
        JPcli.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 86, -1, -1));

        sexo_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        sexo_cli.setText("0");
        JPcli.add(sexo_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 86, 134, -1));

        jLabel83.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel83.setText("Descuento:");
        JPcli.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 152, -1, -1));

        descuento_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        descuento_cli.setText("0");
        JPcli.add(descuento_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 152, 101, -1));

        jLabel85.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel85.setText("Fecha de registro:");
        JPcli.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 185, -1, -1));

        fecha_reg_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        fecha_reg_cli.setText("0");
        JPcli.add(fecha_reg_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 185, 70, -1));

        jLabel87.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel87.setText("Ciudad:");
        JPcli.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 185, -1, -1));

        ciudad_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ciudad_cli.setText("0");
        JPcli.add(ciudad_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 185, 162, -1));

        jLabel89.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel89.setText("Dirección:");
        JPcli.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 152, -1, -1));

        direccion_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        direccion_cli.setText("0");
        JPcli.add(direccion_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 152, 147, -1));

        jLabel91.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel91.setText("Email:");
        JPcli.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 119, -1, -1));

        email_cli.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        email_cli.setText("0");
        JPcli.add(email_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 119, 131, -1));

        JPcli_emp.add(JPcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 616, 420, 240));

        elim_cli.setBackground(new java.awt.Color(255, 0, 51));
        elim_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_cli.setForeground(new java.awt.Color(255, 255, 255));
        elim_cli.setText("x    Eliminar");
        elim_cli.setBorder(null);
        elim_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_cliMouseExited(evt);
            }
        });
        elim_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_cliActionPerformed(evt);
            }
        });
        JPcli_emp.add(elim_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 860, 130, 40));

        reg_cli.setBackground(new java.awt.Color(0, 204, 102));
        reg_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_cli.setForeground(new java.awt.Color(255, 255, 255));
        reg_cli.setText("+    Registrar");
        reg_cli.setBorder(null);
        reg_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_cliMouseExited(evt);
            }
        });
        reg_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_cliActionPerformed(evt);
            }
        });
        JPcli_emp.add(reg_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 860, 130, 40));

        mod_cli.setBackground(new java.awt.Color(51, 204, 255));
        mod_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_cli.setForeground(new java.awt.Color(255, 255, 255));
        mod_cli.setText("¡    Modificar");
        mod_cli.setBorder(null);
        mod_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_cliMouseExited(evt);
            }
        });
        mod_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_cliActionPerformed(evt);
            }
        });
        JPcli_emp.add(mod_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 860, 136, 40));

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
        JPcli_emp.add(Bemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 330, 197, -1));

        JCemp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCemp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "CEDULA", "ID_DEPARTAMENTO", "ID_PUESTO" }));
        JCemp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCempItemStateChanged(evt);
            }
        });
        JPcli_emp.add(JCemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 331, 142, 35));

        jLabel93.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel93.setText("Buscar por");
        JPcli_emp.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, -1, 34));

        jl_titulo19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo19.setText("Empleados");
        jl_titulo19.setIconTextGap(10);
        jl_titulo19.setVerifyInputWhenFocusTarget(false);
        JPcli_emp.add(jl_titulo19, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 287, 181, -1));

        res_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_emp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_emp.setText("Resultados: 0 de 0");
        JPcli_emp.add(res_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 291, 204, -1));

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
        JPcli_emp.add(Lemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 330, -1, 36));

        JPemp.setBackground(new java.awt.Color(255, 255, 255));
        JPemp.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel64.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel64.setText("Cédula:");
        JPemp.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 20, 44, -1));

        cedula_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        cedula_emp.setText("0");
        JPemp.add(cedula_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 20, 132, -1));

        jLabel67.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel67.setText("ID:");
        JPemp.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        id_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        id_emp.setText("0");
        JPemp.add(id_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 20, 188, -1));

        jLabel69.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel69.setText("Nombre:");
        JPemp.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 51, 52, -1));

        nombre_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        nombre_emp.setText("0");
        JPemp.add(nombre_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 51, 154, -1));

        apellido_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        apellido_emp.setText("0");
        JPemp.add(apellido_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 51, 123, -1));

        jLabel73.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel73.setText("Apellido:");
        JPemp.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 51, -1, -1));

        jLabel76.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel76.setText("Celular:");
        JPemp.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 113, -1, -1));

        celular_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        celular_emp.setText("0");
        JPemp.add(celular_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 113, 161, -1));

        jLabel78.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel78.setText("Fecha de nacimiento:");
        JPemp.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 82, 125, -1));

        fecha_nac_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        fecha_nac_emp.setText("0");
        JPemp.add(fecha_nac_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 82, 81, -1));

        jLabel82.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel82.setText("Sexo:");
        JPemp.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 82, -1, -1));

        sexo_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        sexo_emp.setText("0");
        JPemp.add(sexo_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 82, 143, -1));

        jLabel84.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel84.setText("Departamento:");
        JPemp.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 206, 91, -1));

        departamento_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        departamento_emp.setText("0");
        JPemp.add(departamento_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 206, 111, -1));

        fecha_reg_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        fecha_reg_emp.setText("0");
        JPemp.add(fecha_reg_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 206, 72, -1));

        jLabel88.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel88.setText("Ciudad:");
        JPemp.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 175, 45, -1));

        ciudad_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ciudad_emp.setText("0");
        JPemp.add(ciudad_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 175, 162, -1));

        jLabel90.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel90.setText("Dirección:");
        JPemp.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 144, 59, -1));

        direccion_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        direccion_emp.setText("0");
        JPemp.add(direccion_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 144, 147, -1));

        jLabel92.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel92.setText("Email:");
        JPemp.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 113, -1, -1));

        email_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        email_emp.setText("0");
        JPemp.add(email_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 113, 140, -1));

        jLabel95.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel95.setText("Puesto:");
        JPemp.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 144, 45, -1));

        puesto_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        puesto_emp.setText("0");
        JPemp.add(puesto_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 144, 132, -1));

        jLabel96.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel96.setText("Fecha de registro:");
        JPemp.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 206, 105, -1));

        sueldo_emp.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        sueldo_emp.setText("0");
        JPemp.add(sueldo_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 175, 132, -1));

        jLabel98.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel98.setText("Sueldo:");
        JPemp.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 175, 45, -1));

        JPcli_emp.add(JPemp, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 617, 420, 240));

        jPanel24.setBackground(new java.awt.Color(153, 0, 51));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel24.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel94.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(255, 255, 255));
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel94.setText("EMPLEADO SELECCIONADO");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel94, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPcli_emp.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 570, 422, 47));

        elim_emp.setBackground(new java.awt.Color(255, 0, 51));
        elim_emp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_emp.setForeground(new java.awt.Color(255, 255, 255));
        elim_emp.setText("x    Eliminar");
        elim_emp.setBorder(null);
        elim_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_empMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_empMouseExited(evt);
            }
        });
        elim_emp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_empActionPerformed(evt);
            }
        });
        JPcli_emp.add(elim_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 860, 130, 40));

        mod_emp.setBackground(new java.awt.Color(51, 204, 255));
        mod_emp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_emp.setForeground(new java.awt.Color(255, 255, 255));
        mod_emp.setText("¡    Modificar");
        mod_emp.setBorder(null);
        mod_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_empMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_empMouseExited(evt);
            }
        });
        mod_emp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_empActionPerformed(evt);
            }
        });
        JPcli_emp.add(mod_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 860, 136, 40));

        reg_emp.setBackground(new java.awt.Color(0, 204, 102));
        reg_emp.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_emp.setForeground(new java.awt.Color(255, 255, 255));
        reg_emp.setText("+    Registrar");
        reg_emp.setBorder(null);
        reg_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_empMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_empMouseExited(evt);
            }
        });
        reg_emp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_empActionPerformed(evt);
            }
        });
        JPcli_emp.add(reg_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 860, 132, 40));

        jLabel86.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("subir");
        jLabel86.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel86MouseClicked(evt);
            }
        });
        JPcli_emp.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 965, 59, -1));

        jSeparator24.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator24.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator24.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPcli_emp.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 287, -1, 640));

        JScli_emp.setViewportView(JPcli_emp);

        PERSONAS.addTab("Clientes y Empleados", JScli_emp);
        JScli_emp.getVerticalScrollBar().setUnitIncrement(16);

        JPgen_des.setBackground(new java.awt.Color(252, 240, 219));
        JPgen_des.setMaximumSize(new java.awt.Dimension(980, 500));
        JPgen_des.setMinimumSize(new java.awt.Dimension(980, 500));
        JPgen_des.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTgen = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTgen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTgen.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTgen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTgen.setFocusable(false);
        JTgen.setGridColor(new java.awt.Color(255, 255, 255));
        JTgen.setOpaque(false);
        JTgen.setRowHeight(30);
        JTgen.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTgen.getTableHeader().setResizingAllowed(false);
        JTgen.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu18.setViewportView(JTgen);

        JPgen_des.add(jsTabla_ciu18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 420, 150));

        JCgen.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCgen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "SEXO" }));
        JCgen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCgenItemStateChanged(evt);
            }
        });
        JPgen_des.add(JCgen, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel160.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel160.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel160.setText("Buscar por");
        JPgen_des.add(jLabel160, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, -1, 34));

        Bgen.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bgen.setText("Buscar");
        Bgen.setMinimumSize(new java.awt.Dimension(317, 31));
        Bgen.setPreferredSize(new java.awt.Dimension(317, 35));
        Bgen.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bgen.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bgen.setSelectionEnd(0);
        Bgen.setSelectionStart(0);
        Bgen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BgenMouseClicked(evt);
            }
        });
        Bgen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BgenKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BgenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BgenKeyTyped(evt);
            }
        });
        JPgen_des.add(Bgen, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 64, 195, -1));

        Lgen.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lgen.setForeground(new java.awt.Color(0, 102, 102));
        Lgen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lgen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lgen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lgen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LgenMouseClicked(evt);
            }
        });
        JPgen_des.add(Lgen, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 64, -1, 35));

        res_gen.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_gen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_gen.setText("Resultados: 0 de 0");
        JPgen_des.add(res_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 20, 204, 32));

        jl_titulo31.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo31.setText("Géneros");
        jl_titulo31.setIconTextGap(10);
        jl_titulo31.setVerifyInputWhenFocusTarget(false);
        JPgen_des.add(jl_titulo31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 204, -1));

        jPanel37.setBackground(new java.awt.Color(153, 0, 51));
        jPanel37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel37.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel161.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel161.setText("GÉNERO SELECCIONADO");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel161, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel161, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPgen_des.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 420, 47));

        jsTabla_ciu19.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdes.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTdes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdes.setFocusable(false);
        JTdes.setGridColor(new java.awt.Color(255, 255, 255));
        JTdes.setOpaque(false);
        JTdes.setRowHeight(30);
        JTdes.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdes.getTableHeader().setResizingAllowed(false);
        JTdes.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu19.setViewportView(JTdes);

        JPgen_des.add(jsTabla_ciu19, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 111, 422, 150));

        JPgen.setBackground(new java.awt.Color(255, 255, 255));

        jLabel162.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel162.setText("ID:");

        id_gen.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_gen.setText("0");

        jLabel163.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel163.setText("Género:");

        sexo_gen.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        sexo_gen.setText("0");

        javax.swing.GroupLayout JPgenLayout = new javax.swing.GroupLayout(JPgen);
        JPgen.setLayout(JPgenLayout);
        JPgenLayout.setHorizontalGroup(
            JPgenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgenLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPgenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPgenLayout.createSequentialGroup()
                        .addComponent(jLabel163)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sexo_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPgenLayout.createSequentialGroup()
                        .addComponent(jLabel162)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPgenLayout.setVerticalGroup(
            JPgenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgenLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPgenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel162)
                    .addComponent(id_gen))
                .addGap(15, 15, 15)
                .addGroup(JPgenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel163)
                    .addComponent(sexo_gen))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPgen_des.add(JPgen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 420, -1));

        elim_gen.setBackground(new java.awt.Color(255, 0, 51));
        elim_gen.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_gen.setForeground(new java.awt.Color(255, 255, 255));
        elim_gen.setText("x    Eliminar");
        elim_gen.setBorder(null);
        elim_gen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_gen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_genMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_genMouseExited(evt);
            }
        });
        elim_gen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_genActionPerformed(evt);
            }
        });
        JPgen_des.add(elim_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 425, 130, 40));

        reg_gen.setBackground(new java.awt.Color(0, 204, 102));
        reg_gen.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_gen.setForeground(new java.awt.Color(255, 255, 255));
        reg_gen.setText("+    Registrar");
        reg_gen.setBorder(null);
        reg_gen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_gen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_genMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_genMouseExited(evt);
            }
        });
        reg_gen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_genActionPerformed(evt);
            }
        });
        JPgen_des.add(reg_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 425, 130, 40));

        mod_gen.setBackground(new java.awt.Color(51, 204, 255));
        mod_gen.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_gen.setForeground(new java.awt.Color(255, 255, 255));
        mod_gen.setText("¡    Modificar");
        mod_gen.setBorder(null);
        mod_gen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_gen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_genMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_genMouseExited(evt);
            }
        });
        mod_gen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_genActionPerformed(evt);
            }
        });
        JPgen_des.add(mod_gen, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 425, 136, 40));

        Bdes.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bdes.setText("Buscar");
        Bdes.setMinimumSize(new java.awt.Dimension(317, 31));
        Bdes.setPreferredSize(new java.awt.Dimension(317, 35));
        Bdes.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bdes.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bdes.setSelectionEnd(0);
        Bdes.setSelectionStart(0);
        Bdes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BdesMouseClicked(evt);
            }
        });
        Bdes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BdesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BdesKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BdesKeyTyped(evt);
            }
        });
        JPgen_des.add(Bdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 63, 197, -1));

        JCdes.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "PORCENTAJE" }));
        JCdes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdesItemStateChanged(evt);
            }
        });
        JPgen_des.add(JCdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 64, 142, 35));

        jLabel164.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel164.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel164.setText("Buscar por");
        JPgen_des.add(jLabel164, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 63, -1, 34));

        jl_titulo32.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo32.setText("Descuentos");
        jl_titulo32.setIconTextGap(10);
        jl_titulo32.setVerifyInputWhenFocusTarget(false);
        JPgen_des.add(jl_titulo32, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        res_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_des.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_des.setText("Resultados: 0 de 0");
        JPgen_des.add(res_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 24, 204, -1));

        Ldes.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Ldes.setForeground(new java.awt.Color(0, 102, 102));
        Ldes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ldes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Ldes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ldes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LdesMouseClicked(evt);
            }
        });
        JPgen_des.add(Ldes, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 63, -1, 36));

        jSeparator30.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator30.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator30.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPgen_des.add(jSeparator30, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 20, -1, 446));

        jPanel38.setBackground(new java.awt.Color(153, 0, 51));
        jPanel38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel38.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel165.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel165.setText("DESCUENTO SELECCIONADO");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel165, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel165, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPgen_des.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 283, 422, 47));

        JPdes.setBackground(new java.awt.Color(255, 255, 255));

        jLabel166.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel166.setText("Porcentaje:");

        jLabel167.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel167.setText("ID:");

        porcentaje_des.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        porcentaje_des.setText("0");

        jLabel168.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel168.setText("Nombre:");

        nombre_des.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_des.setText("0");

        id_des.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_des.setText("0");

        javax.swing.GroupLayout JPdesLayout = new javax.swing.GroupLayout(JPdes);
        JPdes.setLayout(JPdesLayout);
        JPdesLayout.setHorizontalGroup(
            JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPdesLayout.createSequentialGroup()
                        .addComponent(jLabel167)
                        .addGap(6, 6, 6)
                        .addComponent(id_des, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(122, 122, 122)
                        .addComponent(jLabel166)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(porcentaje_des, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPdesLayout.createSequentialGroup()
                        .addComponent(jLabel168)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_des, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        JPdesLayout.setVerticalGroup(
            JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdesLayout.createSequentialGroup()
                .addGroup(JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdesLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel167)
                            .addComponent(id_des)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPdesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel166)
                            .addComponent(porcentaje_des))))
                .addGap(15, 15, 15)
                .addGroup(JPdesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel168)
                    .addComponent(nombre_des))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        JPgen_des.add(JPdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 422, -1));

        elim_des.setBackground(new java.awt.Color(255, 0, 51));
        elim_des.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_des.setForeground(new java.awt.Color(255, 255, 255));
        elim_des.setText("x    Eliminar");
        elim_des.setBorder(null);
        elim_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_desMouseExited(evt);
            }
        });
        elim_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_desActionPerformed(evt);
            }
        });
        JPgen_des.add(elim_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 426, 130, 40));

        mod_des.setBackground(new java.awt.Color(51, 204, 255));
        mod_des.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_des.setForeground(new java.awt.Color(255, 255, 255));
        mod_des.setText("¡    Modificar");
        mod_des.setBorder(null);
        mod_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_desMouseExited(evt);
            }
        });
        mod_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_desActionPerformed(evt);
            }
        });
        JPgen_des.add(mod_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 426, 136, 40));

        reg_des.setBackground(new java.awt.Color(0, 204, 102));
        reg_des.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_des.setForeground(new java.awt.Color(255, 255, 255));
        reg_des.setText("+    Registrar");
        reg_des.setBorder(null);
        reg_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_desMouseExited(evt);
            }
        });
        reg_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_desActionPerformed(evt);
            }
        });
        JPgen_des.add(reg_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 426, 130, 40));

        PERSONAS.addTab("Géneros y Descuentos", JPgen_des);

        MENU.addTab("Personas", PERSONAS);

        JPpue_dep.setBackground(new java.awt.Color(252, 240, 219));
        JPpue_dep.setMaximumSize(new java.awt.Dimension(980, 500));
        JPpue_dep.setMinimumSize(new java.awt.Dimension(980, 500));
        JPpue_dep.setPreferredSize(new java.awt.Dimension(980, 500));
        JPpue_dep.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpue = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpue.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTpue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTpue.setFocusable(false);
        JTpue.setGridColor(new java.awt.Color(255, 255, 255));
        JTpue.setOpaque(false);
        JTpue.setRowHeight(30);
        JTpue.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTpue.getTableHeader().setResizingAllowed(false);
        JTpue.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu7.setViewportView(JTpue);

        JPpue_dep.add(jsTabla_ciu7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 420, 150));

        JCpue.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "SUELDO" }));
        JCpue.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpueItemStateChanged(evt);
            }
        });
        JPpue_dep.add(JCpue, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel81.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel81.setText("Buscar por");
        JPpue_dep.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, -1, 34));

        Bpue.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bpue.setText("Buscar");
        Bpue.setMinimumSize(new java.awt.Dimension(317, 31));
        Bpue.setPreferredSize(new java.awt.Dimension(317, 35));
        Bpue.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bpue.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bpue.setSelectionEnd(0);
        Bpue.setSelectionStart(0);
        Bpue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BpueMouseClicked(evt);
            }
        });
        Bpue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BpueKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BpueKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BpueKeyTyped(evt);
            }
        });
        JPpue_dep.add(Bpue, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 64, 195, -1));

        Lpue.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lpue.setForeground(new java.awt.Color(0, 102, 102));
        Lpue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lpue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lpue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lpue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LpueMouseClicked(evt);
            }
        });
        JPpue_dep.add(Lpue, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 64, -1, 35));

        res_pue.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pue.setText("Resultados: 0 de 0");
        JPpue_dep.add(res_pue, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 20, 204, 32));

        jl_titulo18.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo18.setText("Puestos");
        jl_titulo18.setIconTextGap(10);
        jl_titulo18.setVerifyInputWhenFocusTarget(false);
        JPpue_dep.add(jl_titulo18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 204, -1));

        jPanel25.setBackground(new java.awt.Color(153, 0, 51));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel25.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel97.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel97.setText("PUESTO SELECCIONADO");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPpue_dep.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 420, 47));

        jsTabla_ciu8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdep = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdep.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdep.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTdep.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdep.setFocusable(false);
        JTdep.setGridColor(new java.awt.Color(255, 255, 255));
        JTdep.setOpaque(false);
        JTdep.setRowHeight(30);
        JTdep.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdep.getTableHeader().setResizingAllowed(false);
        JTdep.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu8.setViewportView(JTdep);

        JPpue_dep.add(jsTabla_ciu8, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 111, 422, 150));

        JPpue.setBackground(new java.awt.Color(255, 255, 255));

        jLabel65.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel65.setText("Sueldo:");

        sueldo_pue.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        sueldo_pue.setText("0");

        jLabel70.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel70.setText("ID:");

        id_pue.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_pue.setText("0");

        jLabel99.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel99.setText("Nombre:");

        nombre_pue.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_pue.setText("0");

        javax.swing.GroupLayout JPpueLayout = new javax.swing.GroupLayout(JPpue);
        JPpue.setLayout(JPpueLayout);
        JPpueLayout.setHorizontalGroup(
            JPpueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpueLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPpueLayout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sueldo_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPpueLayout.createSequentialGroup()
                        .addComponent(jLabel99)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_pue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        JPpueLayout.setVerticalGroup(
            JPpueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpueLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(sueldo_pue)
                    .addComponent(jLabel70)
                    .addComponent(id_pue))
                .addGap(15, 15, 15)
                .addGroup(JPpueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(nombre_pue))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPpue_dep.add(JPpue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        elim_pue.setBackground(new java.awt.Color(255, 0, 51));
        elim_pue.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_pue.setForeground(new java.awt.Color(255, 255, 255));
        elim_pue.setText("x    Eliminar");
        elim_pue.setBorder(null);
        elim_pue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_pue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_pueMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_pueMouseExited(evt);
            }
        });
        elim_pue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_pueActionPerformed(evt);
            }
        });
        JPpue_dep.add(elim_pue, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 425, 130, 40));

        reg_pue.setBackground(new java.awt.Color(0, 204, 102));
        reg_pue.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_pue.setForeground(new java.awt.Color(255, 255, 255));
        reg_pue.setText("+    Registrar");
        reg_pue.setBorder(null);
        reg_pue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_pue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_pueMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_pueMouseExited(evt);
            }
        });
        reg_pue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_pueActionPerformed(evt);
            }
        });
        JPpue_dep.add(reg_pue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 425, 130, 40));

        mod_pue.setBackground(new java.awt.Color(51, 204, 255));
        mod_pue.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_pue.setForeground(new java.awt.Color(255, 255, 255));
        mod_pue.setText("¡    Modificar");
        mod_pue.setBorder(null);
        mod_pue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_pue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_pueMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_pueMouseExited(evt);
            }
        });
        mod_pue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_pueActionPerformed(evt);
            }
        });
        JPpue_dep.add(mod_pue, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 425, 136, 40));

        Bdep.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bdep.setText("Buscar");
        Bdep.setMinimumSize(new java.awt.Dimension(317, 31));
        Bdep.setPreferredSize(new java.awt.Dimension(317, 35));
        Bdep.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bdep.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bdep.setSelectionEnd(0);
        Bdep.setSelectionStart(0);
        Bdep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BdepMouseClicked(evt);
            }
        });
        Bdep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BdepKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BdepKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BdepKeyTyped(evt);
            }
        });
        JPpue_dep.add(Bdep, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 63, 197, -1));

        JCdep.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "DESCRIPCIÓN" }));
        JCdep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdepItemStateChanged(evt);
            }
        });
        JPpue_dep.add(JCdep, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 64, 142, 35));

        jLabel109.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel109.setText("Buscar por");
        JPpue_dep.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 63, -1, 34));

        jl_titulo22.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo22.setText("Departamentos");
        jl_titulo22.setIconTextGap(10);
        jl_titulo22.setVerifyInputWhenFocusTarget(false);
        JPpue_dep.add(jl_titulo22, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        res_dep.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_dep.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_dep.setText("Resultados: 0 de 0");
        JPpue_dep.add(res_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 24, 204, -1));

        Ldep.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Ldep.setForeground(new java.awt.Color(0, 102, 102));
        Ldep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Ldep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Ldep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Ldep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LdepMouseClicked(evt);
            }
        });
        JPpue_dep.add(Ldep, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 63, -1, 36));

        jSeparator25.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator25.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator25.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPpue_dep.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 20, -1, 445));

        jPanel30.setBackground(new java.awt.Color(153, 0, 51));
        jPanel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel30.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel126.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(255, 255, 255));
        jLabel126.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel126.setText("DEPARTAMENTO SELECCIONADO");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel126, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel126, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPpue_dep.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 283, 422, 47));

        JPdep.setBackground(new java.awt.Color(255, 255, 255));

        jLabel128.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel128.setText("ID:");

        id_dep.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_dep.setText("0");

        jLabel129.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel129.setText("Nombre:");

        nombre_dep.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_dep.setText("0");

        jScrollPane3.setBorder(null);

        descripcion_dep.setEditable(false);
        descripcion_dep.setBackground(new java.awt.Color(255, 255, 255));
        descripcion_dep.setColumns(20);
        descripcion_dep.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        descripcion_dep.setLineWrap(true);
        descripcion_dep.setRows(5);
        descripcion_dep.setWrapStyleWord(true);
        descripcion_dep.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        descripcion_dep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcion_depKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(descripcion_dep);

        jLabel157.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel157.setText("Descripción:");

        javax.swing.GroupLayout JPdepLayout = new javax.swing.GroupLayout(JPdep);
        JPdep.setLayout(JPdepLayout);
        JPdepLayout.setHorizontalGroup(
            JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdepLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addComponent(jLabel129)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addComponent(jLabel128)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel157)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        JPdepLayout.setVerticalGroup(
            JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdepLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addComponent(jLabel157)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel128)
                            .addComponent(id_dep))
                        .addGap(15, 15, 15)
                        .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel129)
                            .addComponent(nombre_dep))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPpue_dep.add(JPdep, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 422, -1));

        reg_dep.setBackground(new java.awt.Color(0, 204, 102));
        reg_dep.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_dep.setForeground(new java.awt.Color(255, 255, 255));
        reg_dep.setText("+    Registrar");
        reg_dep.setBorder(null);
        reg_dep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_dep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_depMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_depMouseExited(evt);
            }
        });
        reg_dep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_depActionPerformed(evt);
            }
        });
        JPpue_dep.add(reg_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 425, 130, 40));

        mod_dep.setBackground(new java.awt.Color(51, 204, 255));
        mod_dep.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_dep.setForeground(new java.awt.Color(255, 255, 255));
        mod_dep.setText("¡    Modificar");
        mod_dep.setBorder(null);
        mod_dep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_dep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_depMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_depMouseExited(evt);
            }
        });
        mod_dep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_depActionPerformed(evt);
            }
        });
        JPpue_dep.add(mod_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 425, 136, 40));

        elim_dep.setBackground(new java.awt.Color(255, 0, 51));
        elim_dep.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_dep.setForeground(new java.awt.Color(255, 255, 255));
        elim_dep.setText("x    Eliminar");
        elim_dep.setBorder(null);
        elim_dep.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_dep.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_depMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_depMouseExited(evt);
            }
        });
        elim_dep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_depActionPerformed(evt);
            }
        });
        JPpue_dep.add(elim_dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 425, 130, 40));

        MENU.addTab("Puestos y Departamentos", JPpue_dep);

        JPproductos.setBackground(new java.awt.Color(252, 240, 219));
        JPproductos.setMaximumSize(new java.awt.Dimension(980, 500));
        JPproductos.setMinimumSize(new java.awt.Dimension(980, 500));
        JPproductos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jl_titulo24.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo24.setText("Productos");
        jl_titulo24.setIconTextGap(10);
        jl_titulo24.setVerifyInputWhenFocusTarget(false);
        JPproductos.add(jl_titulo24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 35));

        jsTabla_ciu11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpro = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpro.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
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
        jsTabla_ciu11.setViewportView(JTpro);

        JPproductos.add(jsTabla_ciu11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 899, 150));

        jLabel114.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("Buscar por");
        JPproductos.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 20, -1, 35));

        JCpro.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CODIGO", "NOMBRE", "ID_MARCA", "PRECIO", "EXIS_MAX", "EXIS_MIN", "STOK", "ID_CATEGORIA", "FECHA_REGISTRO", "RUC_PROVEEDOR" }));
        JCpro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCproItemStateChanged(evt);
            }
        });
        JPproductos.add(JCpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 20, 142, 35));

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
        JPproductos.add(Bpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 20, 185, -1));

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
        JPproductos.add(Lpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(645, 20, -1, 35));

        res_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pro.setText("Resultados: 0 de 0");
        JPproductos.add(res_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 20, 212, 35));

        jPanel26.setBackground(new java.awt.Color(153, 0, 51));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel26.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel103.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(255, 255, 255));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel103.setText("PRODUCTO SELECCIONADO");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPproductos.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 239, 782, 47));

        JPpro.setBackground(new java.awt.Color(255, 255, 255));

        jLabel104.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel104.setText("Existencias máximas:");

        maximos_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        maximos_pro.setText("0");

        jLabel105.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel105.setText("Código:");

        codigo_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        codigo_pro.setText("0");

        jLabel106.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel106.setText("Nombre:");

        nombre_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_pro.setText("0");

        minimos_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        minimos_pro.setText("0");

        jLabel107.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel107.setText("Existencias mínimas:");

        jLabel108.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel108.setText("Precio:");

        precio_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        precio_pro.setText("0");

        jLabel110.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel110.setText("Marca:");

        marca_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        marca_pro.setText("0");

        jLabel111.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel111.setText("Stock:");

        stock_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        stock_pro.setText("0");

        jLabel112.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel112.setText("Fecha de registro:");

        fecha_reg_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_reg_pro.setText("0");

        jLabel116.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel116.setText("Categoría:");

        categoria_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        categoria_pro.setText("0");

        jLabel117.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel117.setText("Proveedor:");

        proveedor_pro.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        proveedor_pro.setText("0");

        imagen_pro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 51), 2));
        imagen_pro.setMaximumSize(new java.awt.Dimension(160, 160));
        imagen_pro.setMinimumSize(new java.awt.Dimension(160, 160));
        imagen_pro.setPreferredSize(new java.awt.Dimension(160, 160));

        javax.swing.GroupLayout JPproLayout = new javax.swing.GroupLayout(JPpro);
        JPpro.setLayout(JPproLayout);
        JPproLayout.setHorizontalGroup(
            JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel105)
                        .addGap(6, 6, 6)
                        .addComponent(codigo_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel106)
                        .addGap(6, 6, 6)
                        .addComponent(nombre_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel110)
                        .addGap(6, 6, 6)
                        .addComponent(marca_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel108)
                        .addGap(3, 3, 3)
                        .addComponent(precio_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel116)
                        .addGap(6, 6, 6)
                        .addComponent(categoria_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addComponent(imagen_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel104)
                        .addGap(6, 6, 6)
                        .addComponent(maximos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel107, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(minimos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addGap(6, 6, 6)
                        .addComponent(stock_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel117)
                        .addGap(6, 6, 6)
                        .addComponent(proveedor_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel112)
                        .addGap(6, 6, 6)
                        .addComponent(fecha_reg_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        JPproLayout.setVerticalGroup(
            JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel105)
                            .addComponent(codigo_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel106)
                            .addComponent(nombre_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel110)
                            .addComponent(marca_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel108)
                            .addComponent(precio_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel116)
                            .addComponent(categoria_pro)))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel104)
                            .addComponent(maximos_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel107)
                            .addComponent(minimos_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel111)
                            .addComponent(stock_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel117)
                            .addComponent(proveedor_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel112)
                            .addComponent(fecha_reg_pro)))
                    .addComponent(imagen_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        JPproductos.add(JPpro, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 286, 780, 200));

        elim_pro.setBackground(new java.awt.Color(255, 0, 51));
        elim_pro.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_pro.setForeground(new java.awt.Color(255, 255, 255));
        elim_pro.setText("x    Eliminar");
        elim_pro.setBorder(null);
        elim_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_proMouseExited(evt);
            }
        });
        elim_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_proActionPerformed(evt);
            }
        });
        JPproductos.add(elim_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 378, 117, 40));

        mod_pro.setBackground(new java.awt.Color(51, 204, 255));
        mod_pro.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_pro.setForeground(new java.awt.Color(255, 255, 255));
        mod_pro.setText("¡    Modificar");
        mod_pro.setBorder(null);
        mod_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_proMouseExited(evt);
            }
        });
        mod_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_proActionPerformed(evt);
            }
        });
        JPproductos.add(mod_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 332, 117, 40));

        reg_pro.setBackground(new java.awt.Color(0, 204, 102));
        reg_pro.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_pro.setForeground(new java.awt.Color(255, 255, 255));
        reg_pro.setText("+    Registrar");
        reg_pro.setBorder(null);
        reg_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_proMouseExited(evt);
            }
        });
        reg_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_proActionPerformed(evt);
            }
        });
        JPproductos.add(reg_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(802, 286, 117, 40));

        MENU.addTab("Productos", JPproductos);

        JPmar_cat.setBackground(new java.awt.Color(252, 240, 219));
        JPmar_cat.setMaximumSize(new java.awt.Dimension(980, 500));
        JPmar_cat.setMinimumSize(new java.awt.Dimension(980, 500));
        JPmar_cat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTmar = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTmar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTmar.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTmar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTmar.setFocusable(false);
        JTmar.setGridColor(new java.awt.Color(255, 255, 255));
        JTmar.setOpaque(false);
        JTmar.setRowHeight(30);
        JTmar.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTmar.getTableHeader().setResizingAllowed(false);
        JTmar.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu9.setViewportView(JTmar);

        JPmar_cat.add(jsTabla_ciu9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 420, 150));

        JCmar.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCmar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE" }));
        JCmar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCmarItemStateChanged(evt);
            }
        });
        JPmar_cat.add(JCmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel113.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel113.setText("Buscar por");
        JPmar_cat.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, -1, 34));

        Bmar.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bmar.setText("Buscar");
        Bmar.setMinimumSize(new java.awt.Dimension(317, 31));
        Bmar.setPreferredSize(new java.awt.Dimension(317, 35));
        Bmar.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bmar.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bmar.setSelectionEnd(0);
        Bmar.setSelectionStart(0);
        Bmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BmarMouseClicked(evt);
            }
        });
        Bmar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BmarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BmarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BmarKeyTyped(evt);
            }
        });
        JPmar_cat.add(Bmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 64, 195, -1));

        Lmar.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lmar.setForeground(new java.awt.Color(0, 102, 102));
        Lmar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LmarMouseClicked(evt);
            }
        });
        JPmar_cat.add(Lmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 64, -1, 35));

        res_mar.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_mar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_mar.setText("Resultados: 0 de 0");
        JPmar_cat.add(res_mar, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 20, 204, 32));

        jl_titulo21.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo21.setText("Marcas");
        jl_titulo21.setIconTextGap(10);
        jl_titulo21.setVerifyInputWhenFocusTarget(false);
        JPmar_cat.add(jl_titulo21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 204, -1));

        jPanel28.setBackground(new java.awt.Color(153, 0, 51));
        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel28.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel115.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel115.setForeground(new java.awt.Color(255, 255, 255));
        jLabel115.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel115.setText("MARCA SELECCIONADA");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel115, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel115, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPmar_cat.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 420, 47));

        jsTabla_ciu10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTcat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcat.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTcat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTcat.setFocusable(false);
        JTcat.setGridColor(new java.awt.Color(255, 255, 255));
        JTcat.setOpaque(false);
        JTcat.setRowHeight(30);
        JTcat.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTcat.getTableHeader().setResizingAllowed(false);
        JTcat.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu10.setViewportView(JTcat);

        JPmar_cat.add(jsTabla_ciu10, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 111, 422, 150));

        JPmar.setBackground(new java.awt.Color(255, 255, 255));

        jLabel119.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel119.setText("ID:");

        id_mar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_mar.setText("0");

        jLabel120.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel120.setText("Nombre:");

        nombre_mar.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_mar.setText("0");

        javax.swing.GroupLayout JPmarLayout = new javax.swing.GroupLayout(JPmar);
        JPmar.setLayout(JPmarLayout);
        JPmarLayout.setHorizontalGroup(
            JPmarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPmarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPmarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPmarLayout.createSequentialGroup()
                        .addComponent(jLabel120)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPmarLayout.createSequentialGroup()
                        .addComponent(jLabel119)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        JPmarLayout.setVerticalGroup(
            JPmarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPmarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPmarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel119)
                    .addComponent(id_mar))
                .addGap(15, 15, 15)
                .addGroup(JPmarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel120)
                    .addComponent(nombre_mar))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPmar_cat.add(JPmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        elim_mar.setBackground(new java.awt.Color(255, 0, 51));
        elim_mar.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_mar.setForeground(new java.awt.Color(255, 255, 255));
        elim_mar.setText("x    Eliminar");
        elim_mar.setBorder(null);
        elim_mar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_mar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_marMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_marMouseExited(evt);
            }
        });
        elim_mar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_marActionPerformed(evt);
            }
        });
        JPmar_cat.add(elim_mar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 425, 130, 40));

        reg_mar.setBackground(new java.awt.Color(0, 204, 102));
        reg_mar.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_mar.setForeground(new java.awt.Color(255, 255, 255));
        reg_mar.setText("+    Registrar");
        reg_mar.setBorder(null);
        reg_mar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_mar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_marMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_marMouseExited(evt);
            }
        });
        reg_mar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_marActionPerformed(evt);
            }
        });
        JPmar_cat.add(reg_mar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 425, 130, 40));

        mod_mar.setBackground(new java.awt.Color(51, 204, 255));
        mod_mar.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_mar.setForeground(new java.awt.Color(255, 255, 255));
        mod_mar.setText("¡    Modificar");
        mod_mar.setBorder(null);
        mod_mar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_mar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_marMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_marMouseExited(evt);
            }
        });
        mod_mar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_marActionPerformed(evt);
            }
        });
        JPmar_cat.add(mod_mar, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 425, 136, 40));

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
        JPmar_cat.add(Bcat, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 63, 197, -1));

        JCcat.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCcat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "DESCRIPCIÓN" }));
        JCcat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCcatItemStateChanged(evt);
            }
        });
        JPmar_cat.add(JCcat, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 64, 142, 35));

        jLabel121.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel121.setText("Buscar por");
        JPmar_cat.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 63, -1, 34));

        jl_titulo23.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo23.setText("Categorías");
        jl_titulo23.setIconTextGap(10);
        jl_titulo23.setVerifyInputWhenFocusTarget(false);
        JPmar_cat.add(jl_titulo23, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        res_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_cat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_cat.setText("Resultados: 0 de 0");
        JPmar_cat.add(res_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 24, 204, -1));

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
        JPmar_cat.add(Lcat, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 63, -1, 36));

        jSeparator26.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator26.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPmar_cat.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 20, -1, 445));

        jPanel29.setBackground(new java.awt.Color(153, 0, 51));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel29.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel122.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel122.setText("CATEGORÍA SELECCIONADA");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel122, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel122, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPmar_cat.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 283, 422, 47));

        JPcat.setBackground(new java.awt.Color(255, 255, 255));

        jLabel123.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel123.setText("Descripción:");

        jLabel124.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel124.setText("ID:");

        id_cat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_cat.setText("0");

        jLabel125.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel125.setText("Nombre:");

        nombre_cat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_cat.setText("0");

        jScrollPane1.setBorder(null);

        descripcion_cat.setEditable(false);
        descripcion_cat.setBackground(new java.awt.Color(255, 255, 255));
        descripcion_cat.setColumns(20);
        descripcion_cat.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        descripcion_cat.setLineWrap(true);
        descripcion_cat.setRows(5);
        descripcion_cat.setWrapStyleWord(true);
        descripcion_cat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        descripcion_cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcion_catKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(descripcion_cat);

        javax.swing.GroupLayout JPcatLayout = new javax.swing.GroupLayout(JPcat);
        JPcat.setLayout(JPcatLayout);
        JPcatLayout.setHorizontalGroup(
            JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcatLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcatLayout.createSequentialGroup()
                        .addComponent(jLabel124)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPcatLayout.createSequentialGroup()
                        .addComponent(jLabel125)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel123)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        JPcatLayout.setVerticalGroup(
            JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcatLayout.createSequentialGroup()
                .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcatLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel124)
                            .addComponent(id_cat)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPcatLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel123)))
                .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcatLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(JPcatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel125)
                            .addComponent(nombre_cat)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        JPmar_cat.add(JPcat, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, -1, -1));

        elim_cat.setBackground(new java.awt.Color(255, 0, 51));
        elim_cat.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_cat.setForeground(new java.awt.Color(255, 255, 255));
        elim_cat.setText("x    Eliminar");
        elim_cat.setBorder(null);
        elim_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_catMouseExited(evt);
            }
        });
        elim_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_catActionPerformed(evt);
            }
        });
        JPmar_cat.add(elim_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 420, 130, 40));

        mod_cat.setBackground(new java.awt.Color(51, 204, 255));
        mod_cat.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_cat.setForeground(new java.awt.Color(255, 255, 255));
        mod_cat.setText("¡    Modificar");
        mod_cat.setBorder(null);
        mod_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_catMouseExited(evt);
            }
        });
        mod_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_catActionPerformed(evt);
            }
        });
        JPmar_cat.add(mod_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 420, 136, 40));

        reg_cat.setBackground(new java.awt.Color(0, 204, 102));
        reg_cat.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_cat.setForeground(new java.awt.Color(255, 255, 255));
        reg_cat.setText("+    Registrar");
        reg_cat.setBorder(null);
        reg_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_catMouseExited(evt);
            }
        });
        reg_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_catActionPerformed(evt);
            }
        });
        JPmar_cat.add(reg_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 420, 130, 40));

        MENU.addTab("Marcas y Categorías", JPmar_cat);

        JPprov_suc.setBackground(new java.awt.Color(252, 240, 219));
        JPprov_suc.setMaximumSize(new java.awt.Dimension(980, 500));
        JPprov_suc.setMinimumSize(new java.awt.Dimension(980, 500));
        JPprov_suc.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTprov = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTprov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTprov.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
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
        jsTabla_ciu12.setViewportView(JTprov);

        JPprov_suc.add(jsTabla_ciu12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 469, 150));

        JCprov.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCprov.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RUC", "NOMBRE_EMPRESA", "CELULAR", "EMAIL", "ID_CIUDAD", "FECHA_REGISTRO" }));
        JCprov.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCprovItemStateChanged(evt);
            }
        });
        JPprov_suc.add(JCprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel118.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel118.setText("Buscar por");
        JPprov_suc.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, -1, 34));

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
        JPprov_suc.add(Bprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 63, 228, -1));

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
        JPprov_suc.add(Lprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 63, -1, 36));

        res_prov.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_prov.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_prov.setText("Resultados: 0 de 0");
        JPprov_suc.add(res_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 20, 228, 32));

        jl_titulo25.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo25.setText("Proveedores");
        jl_titulo25.setIconTextGap(10);
        jl_titulo25.setVerifyInputWhenFocusTarget(false);
        JPprov_suc.add(jl_titulo25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 219, -1));

        jPanel31.setBackground(new java.awt.Color(153, 0, 51));
        jPanel31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel31.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel130.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel130.setText("PROVEEDOR SELECCIONADO");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel130, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel130, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPprov_suc.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 469, 47));

        jsTabla_ciu13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTsuc = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTsuc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTsuc.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTsuc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTsuc.setFocusable(false);
        JTsuc.setGridColor(new java.awt.Color(255, 255, 255));
        JTsuc.setOpaque(false);
        JTsuc.setRowHeight(30);
        JTsuc.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTsuc.getTableHeader().setResizingAllowed(false);
        JTsuc.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu13.setViewportView(JTsuc);

        JPprov_suc.add(jsTabla_ciu13, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 111, 370, 150));

        JPprov.setBackground(new java.awt.Color(255, 255, 255));

        jLabel131.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel131.setText("RUC:");

        ruc_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ruc_prov.setText("0");

        jLabel132.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel132.setText("Nombre:");

        nombre_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        nombre_prov.setText("0");

        celular_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        celular_prov.setText("0");

        jLabel138.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel138.setText("Celular:");

        fecha_reg_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        fecha_reg_prov.setText("0");

        jLabel139.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel139.setText("Email:");

        email_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        email_prov.setText("0");

        jLabel140.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel140.setText("Ciudad:");

        ciudad_prov.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ciudad_prov.setText("0");

        jLabel141.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel141.setText("Fecha de registro:");

        javax.swing.GroupLayout JPprovLayout = new javax.swing.GroupLayout(JPprov);
        JPprov.setLayout(JPprovLayout);
        JPprovLayout.setHorizontalGroup(
            JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPprovLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(JPprovLayout.createSequentialGroup()
                            .addComponent(jLabel138)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(celular_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(JPprovLayout.createSequentialGroup()
                            .addComponent(jLabel131)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ruc_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel132)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel141)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_reg_prov, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel140)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciudad_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        JPprovLayout.setVerticalGroup(
            JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPprovLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel139)
                            .addComponent(email_prov))
                        .addGap(15, 15, 15)
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel140)
                            .addComponent(ciudad_prov))
                        .addGap(15, 15, 15)
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel141)
                            .addComponent(fecha_reg_prov)))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel131)
                            .addComponent(ruc_prov))
                        .addGap(15, 15, 15)
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel132)
                            .addComponent(nombre_prov))
                        .addGap(15, 15, 15)
                        .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel138)
                            .addComponent(celular_prov))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        JPprov_suc.add(JPprov, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        elim_prov.setBackground(new java.awt.Color(255, 0, 51));
        elim_prov.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_prov.setForeground(new java.awt.Color(255, 255, 255));
        elim_prov.setText("x    Eliminar");
        elim_prov.setBorder(null);
        elim_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_provMouseExited(evt);
            }
        });
        elim_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_provActionPerformed(evt);
            }
        });
        JPprov_suc.add(elim_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 449, 147, 40));

        reg_prov.setBackground(new java.awt.Color(0, 204, 102));
        reg_prov.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_prov.setForeground(new java.awt.Color(255, 255, 255));
        reg_prov.setText("+    Registrar");
        reg_prov.setBorder(null);
        reg_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_provMouseExited(evt);
            }
        });
        reg_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_provActionPerformed(evt);
            }
        });
        JPprov_suc.add(reg_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 449, 146, 40));

        mod_prov.setBackground(new java.awt.Color(51, 204, 255));
        mod_prov.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_prov.setForeground(new java.awt.Color(255, 255, 255));
        mod_prov.setText("¡    Modificar");
        mod_prov.setBorder(null);
        mod_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_provMouseExited(evt);
            }
        });
        mod_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_provActionPerformed(evt);
            }
        });
        JPprov_suc.add(mod_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 449, 140, 40));

        Bsuc.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bsuc.setText("Buscar");
        Bsuc.setMinimumSize(new java.awt.Dimension(317, 31));
        Bsuc.setPreferredSize(new java.awt.Dimension(317, 35));
        Bsuc.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bsuc.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bsuc.setSelectionEnd(0);
        Bsuc.setSelectionStart(0);
        Bsuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BsucMouseClicked(evt);
            }
        });
        Bsuc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BsucKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BsucKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BsucKeyTyped(evt);
            }
        });
        JPprov_suc.add(Bsuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(783, 63, 145, -1));

        JCsuc.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCsuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "ID_CIU" }));
        JCsuc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCsucItemStateChanged(evt);
            }
        });
        JPprov_suc.add(JCsuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(635, 64, 120, 35));

        jLabel133.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel133.setText("Buscar por");
        JPprov_suc.add(jLabel133, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 63, -1, 34));

        jl_titulo26.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo26.setText("Sucursales");
        jl_titulo26.setIconTextGap(10);
        jl_titulo26.setVerifyInputWhenFocusTarget(false);
        JPprov_suc.add(jl_titulo26, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 20, -1, -1));

        res_suc.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_suc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_suc.setText("Resultados: 0 de 0");
        JPprov_suc.add(res_suc, new org.netbeans.lib.awtextra.AbsoluteConstraints(743, 24, 185, -1));

        Lsuc.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lsuc.setForeground(new java.awt.Color(0, 102, 102));
        Lsuc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lsuc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lsuc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lsuc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LsucMouseClicked(evt);
            }
        });
        JPprov_suc.add(Lsuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(931, 63, -1, 36));

        jSeparator27.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator27.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator27.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPprov_suc.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(526, 20, 14, 469));

        jPanel34.setBackground(new java.awt.Color(153, 0, 51));
        jPanel34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel34.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel143.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel143.setText("SUCURSAL SELECCIONADA");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel143, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel143, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPprov_suc.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 283, 370, 47));

        JPsuc.setBackground(new java.awt.Color(255, 255, 255));

        jLabel144.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel144.setText("ID:");

        id_suc.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        id_suc.setText("0");

        jLabel145.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel145.setText("Nombre:");

        nombre_suc.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        nombre_suc.setText("0");

        ciudad_suc.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ciudad_suc.setText("0");

        jLabel146.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel146.setText("Ciudad:");

        javax.swing.GroupLayout JPsucLayout = new javax.swing.GroupLayout(JPsuc);
        JPsuc.setLayout(JPsucLayout);
        JPsucLayout.setHorizontalGroup(
            JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPsucLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPsucLayout.createSequentialGroup()
                        .addComponent(jLabel146)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciudad_suc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPsucLayout.createSequentialGroup()
                        .addComponent(jLabel145)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_suc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPsucLayout.createSequentialGroup()
                        .addComponent(jLabel144)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPsucLayout.setVerticalGroup(
            JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPsucLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel144)
                    .addComponent(id_suc))
                .addGap(15, 15, 15)
                .addGroup(JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(nombre_suc))
                .addGap(15, 15, 15)
                .addGroup(JPsucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel146)
                    .addComponent(ciudad_suc))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        JPprov_suc.add(JPsuc, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 330, 370, -1));

        reg_suc.setBackground(new java.awt.Color(0, 204, 102));
        reg_suc.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_suc.setForeground(new java.awt.Color(255, 255, 255));
        reg_suc.setText("+    Registrar");
        reg_suc.setBorder(null);
        reg_suc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_suc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_sucMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_sucMouseExited(evt);
            }
        });
        reg_suc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_sucActionPerformed(evt);
            }
        });
        JPprov_suc.add(reg_suc, new org.netbeans.lib.awtextra.AbsoluteConstraints(558, 449, 116, 40));

        mod_suc.setBackground(new java.awt.Color(51, 204, 255));
        mod_suc.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_suc.setForeground(new java.awt.Color(255, 255, 255));
        mod_suc.setText("¡    Modificar");
        mod_suc.setBorder(null);
        mod_suc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_suc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_sucMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_sucMouseExited(evt);
            }
        });
        mod_suc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_sucActionPerformed(evt);
            }
        });
        JPprov_suc.add(mod_suc, new org.netbeans.lib.awtextra.AbsoluteConstraints(686, 449, 117, 40));

        elim_suc.setBackground(new java.awt.Color(255, 0, 51));
        elim_suc.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_suc.setForeground(new java.awt.Color(255, 255, 255));
        elim_suc.setText("x    Eliminar");
        elim_suc.setBorder(null);
        elim_suc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_suc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_sucMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_sucMouseExited(evt);
            }
        });
        elim_suc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_sucActionPerformed(evt);
            }
        });
        JPprov_suc.add(elim_suc, new org.netbeans.lib.awtextra.AbsoluteConstraints(815, 449, 113, 40));

        MENU.addTab("Proveedores y Sucursales", JPprov_suc);

        JPciu_provi.setBackground(new java.awt.Color(252, 240, 219));
        JPciu_provi.setMaximumSize(new java.awt.Dimension(980, 500));
        JPciu_provi.setMinimumSize(new java.awt.Dimension(980, 500));
        JPciu_provi.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTciu = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTciu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTciu.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTciu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTciu.setFocusable(false);
        JTciu.setGridColor(new java.awt.Color(255, 255, 255));
        JTciu.setOpaque(false);
        JTciu.setRowHeight(30);
        JTciu.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTciu.getTableHeader().setResizingAllowed(false);
        JTciu.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu14.setViewportView(JTciu);

        JPciu_provi.add(jsTabla_ciu14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 111, 420, 150));

        JCciu.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCciu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE", "ID_PROVICIA" }));
        JCciu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCciuItemStateChanged(evt);
            }
        });
        JPciu_provi.add(JCciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 64, 142, 35));

        jLabel134.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel134.setText("Buscar por");
        JPciu_provi.add(jLabel134, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, -1, 34));

        Bciu.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bciu.setText("Buscar");
        Bciu.setMinimumSize(new java.awt.Dimension(317, 31));
        Bciu.setPreferredSize(new java.awt.Dimension(317, 35));
        Bciu.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bciu.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bciu.setSelectionEnd(0);
        Bciu.setSelectionStart(0);
        Bciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BciuMouseClicked(evt);
            }
        });
        Bciu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BciuKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BciuKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BciuKeyTyped(evt);
            }
        });
        JPciu_provi.add(Bciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 64, 195, -1));

        Lciu.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lciu.setForeground(new java.awt.Color(0, 102, 102));
        Lciu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LciuMouseClicked(evt);
            }
        });
        JPciu_provi.add(Lciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 64, -1, 35));

        res_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_ciu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_ciu.setText("Resultados: 0 de 0");
        JPciu_provi.add(res_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 20, 204, 32));

        jl_titulo27.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo27.setText("Ciudades");
        jl_titulo27.setIconTextGap(10);
        jl_titulo27.setVerifyInputWhenFocusTarget(false);
        JPciu_provi.add(jl_titulo27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 204, -1));

        jPanel32.setBackground(new java.awt.Color(153, 0, 51));
        jPanel32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel32.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel135.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel135.setText("CIUDAD SELECCIONADA");

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel135, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel135, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPciu_provi.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 283, 420, 47));

        jsTabla_ciu15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTprovi = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTprovi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTprovi.setFont(new java.awt.Font("Calibri Light", 1, 16)); // NOI18N
        JTprovi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTprovi.setFocusable(false);
        JTprovi.setGridColor(new java.awt.Color(255, 255, 255));
        JTprovi.setOpaque(false);
        JTprovi.setRowHeight(30);
        JTprovi.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTprovi.getTableHeader().setResizingAllowed(false);
        JTprovi.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu15.setViewportView(JTprovi);

        JPciu_provi.add(jsTabla_ciu15, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 111, 422, 150));

        JPciu.setBackground(new java.awt.Color(255, 255, 255));

        jLabel136.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel136.setText("ID:");

        id_ciu.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_ciu.setText("0");

        jLabel137.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel137.setText("Ciudad:");

        nombre_ciu.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_ciu.setText("0");

        id_provi_ciu.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_provi_ciu.setText("0");

        jLabel142.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel142.setText("ID provincia:");

        javax.swing.GroupLayout JPciuLayout = new javax.swing.GroupLayout(JPciu);
        JPciu.setLayout(JPciuLayout);
        JPciuLayout.setHorizontalGroup(
            JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciuLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPciuLayout.createSequentialGroup()
                        .addComponent(jLabel142)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_provi_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPciuLayout.createSequentialGroup()
                        .addComponent(jLabel136)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPciuLayout.createSequentialGroup()
                        .addComponent(jLabel137)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_ciu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(183, 183, 183))
        );
        JPciuLayout.setVerticalGroup(
            JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciuLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel136)
                    .addComponent(id_ciu))
                .addGap(15, 15, 15)
                .addGroup(JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel137)
                    .addComponent(nombre_ciu))
                .addGap(15, 15, 15)
                .addGroup(JPciuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel142)
                    .addComponent(id_provi_ciu))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        JPciu_provi.add(JPciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 420, -1));

        elim_ciu.setBackground(new java.awt.Color(255, 0, 51));
        elim_ciu.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_ciu.setForeground(new java.awt.Color(255, 255, 255));
        elim_ciu.setText("x    Eliminar");
        elim_ciu.setBorder(null);
        elim_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_ciuMouseExited(evt);
            }
        });
        elim_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_ciuActionPerformed(evt);
            }
        });
        JPciu_provi.add(elim_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 455, 130, 40));

        reg_ciu.setBackground(new java.awt.Color(0, 204, 102));
        reg_ciu.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_ciu.setForeground(new java.awt.Color(255, 255, 255));
        reg_ciu.setText("+    Registrar");
        reg_ciu.setBorder(null);
        reg_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_ciuMouseExited(evt);
            }
        });
        reg_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_ciuActionPerformed(evt);
            }
        });
        JPciu_provi.add(reg_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 455, 130, 40));

        mod_ciu.setBackground(new java.awt.Color(51, 204, 255));
        mod_ciu.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_ciu.setForeground(new java.awt.Color(255, 255, 255));
        mod_ciu.setText("¡    Modificar");
        mod_ciu.setBorder(null);
        mod_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_ciuMouseExited(evt);
            }
        });
        mod_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_ciuActionPerformed(evt);
            }
        });
        JPciu_provi.add(mod_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 455, 136, 40));

        Bprovi.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        Bprovi.setText("Buscar");
        Bprovi.setMinimumSize(new java.awt.Dimension(317, 31));
        Bprovi.setPreferredSize(new java.awt.Dimension(317, 35));
        Bprovi.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        Bprovi.setSelectionColor(new java.awt.Color(153, 204, 255));
        Bprovi.setSelectionEnd(0);
        Bprovi.setSelectionStart(0);
        Bprovi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BproviMouseClicked(evt);
            }
        });
        Bprovi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BproviKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BproviKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BproviKeyTyped(evt);
            }
        });
        JPciu_provi.add(Bprovi, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 63, 197, -1));

        JCprovi.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCprovi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "NOMBRE" }));
        JCprovi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCproviItemStateChanged(evt);
            }
        });
        JPciu_provi.add(JCprovi, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 64, 142, 35));

        jLabel150.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("Buscar por");
        JPciu_provi.add(jLabel150, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 63, -1, 34));

        jl_titulo28.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo28.setText("Provincias");
        jl_titulo28.setIconTextGap(10);
        jl_titulo28.setVerifyInputWhenFocusTarget(false);
        JPciu_provi.add(jl_titulo28, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, -1));

        res_provi.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_provi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_provi.setText("Resultados: 0 de 0");
        JPciu_provi.add(res_provi, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 24, 204, -1));

        Lprovi.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        Lprovi.setForeground(new java.awt.Color(0, 102, 102));
        Lprovi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Lprovi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        Lprovi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Lprovi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LproviMouseClicked(evt);
            }
        });
        JPciu_provi.add(Lprovi, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 63, -1, 36));

        jSeparator28.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator28.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator28.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPciu_provi.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 20, -1, 475));

        jPanel35.setBackground(new java.awt.Color(153, 0, 51));
        jPanel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel35.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel151.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel151.setText("PROVINCIA SELECCIONADA");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel151, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPciu_provi.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 283, 422, 47));

        JPprovi.setBackground(new java.awt.Color(255, 255, 255));

        jLabel152.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel152.setText("ID:");

        id_provi.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_provi.setText("0");

        jLabel153.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel153.setText("Provincia:");

        nombre_provi.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_provi.setText("0");

        javax.swing.GroupLayout JPproviLayout = new javax.swing.GroupLayout(JPprovi);
        JPprovi.setLayout(JPproviLayout);
        JPproviLayout.setHorizontalGroup(
            JPproviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproviLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPproviLayout.createSequentialGroup()
                        .addComponent(jLabel153)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_provi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPproviLayout.createSequentialGroup()
                        .addComponent(jLabel152)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        JPproviLayout.setVerticalGroup(
            JPproviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproviLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel152)
                    .addComponent(id_provi))
                .addGap(15, 15, 15)
                .addGroup(JPproviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel153)
                    .addComponent(nombre_provi))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        JPciu_provi.add(JPprovi, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, 422, -1));

        reg_provi.setBackground(new java.awt.Color(0, 204, 102));
        reg_provi.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        reg_provi.setForeground(new java.awt.Color(255, 255, 255));
        reg_provi.setText("+    Registrar");
        reg_provi.setBorder(null);
        reg_provi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        reg_provi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reg_proviMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reg_proviMouseExited(evt);
            }
        });
        reg_provi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reg_proviActionPerformed(evt);
            }
        });
        JPciu_provi.add(reg_provi, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 455, 130, 40));

        mod_provi.setBackground(new java.awt.Color(51, 204, 255));
        mod_provi.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_provi.setForeground(new java.awt.Color(255, 255, 255));
        mod_provi.setText("¡    Modificar");
        mod_provi.setBorder(null);
        mod_provi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_provi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_proviMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_proviMouseExited(evt);
            }
        });
        mod_provi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_proviActionPerformed(evt);
            }
        });
        JPciu_provi.add(mod_provi, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 455, 136, 40));

        elim_provi.setBackground(new java.awt.Color(255, 0, 51));
        elim_provi.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_provi.setForeground(new java.awt.Color(255, 255, 255));
        elim_provi.setText("x    Eliminar");
        elim_provi.setBorder(null);
        elim_provi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_provi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_proviMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_proviMouseExited(evt);
            }
        });
        elim_provi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_proviActionPerformed(evt);
            }
        });
        JPciu_provi.add(elim_provi, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 455, 130, 40));

        MENU.addTab("Ciudades y Provincias", JPciu_provi);

        getContentPane().add(MENU, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 530));

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/abstracto_rojo.jpeg"))); // NOI18N
        getContentPane().add(fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1020, 570));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if (actualizado == false) {
            visualizar();
            actualizado = true;
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void f_ced_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_ced_cliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_f_ced_cliActionPerformed

    private void JBseleccionar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBseleccionar_proActionPerformed
        MENU.setSelectedIndex(3);
    }//GEN-LAST:event_JBseleccionar_proActionPerformed

    private void f_ced_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_f_ced_cliMouseClicked
        MENU.setSelectedIndex(1);
        PERSONAS.setSelectedIndex(0);
    }//GEN-LAST:event_f_ced_cliMouseClicked

    private void JBcrear_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcrear_facturaActionPerformed
        if (f_fecha.getText().isEmpty() || f_suc.getText().isEmpty() || f_emp.getText().isEmpty()||num_det==0||f_iva.getSelectedIndex()==0||f_forma_pago.getSelectedIndex() == 0) {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Aún hay campos por completar!");
        } else {
            int valor = JOptionPane.showConfirmDialog(this, "¿Desea continuar con la creación de la factura?", "Crear factura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (valor == JOptionPane.YES_OPTION) {
                generar_factura();
                reiniciar_factura();
                visualizar();
                INICIO.setSelectedIndex(1);
            }
        }
    }//GEN-LAST:event_JBcrear_facturaActionPerformed

    private void JTdetalleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JTdetalleMouseClicked
        xcolum = JTdetalle.getColumnModel().getColumnIndexAtX(evt.getX());
        xrow = evt.getY() / JTdetalle.getRowHeight();
        
        if (xcolum <= JTdetalle.getColumnCount() && xcolum >= 0 && xrow <= JTdetalle.getRowCount() && xrow >= 0) {
            Object obj = JTdetalle.getValueAt(xrow, xcolum);
            if (obj instanceof JButton) {
                int valor = JOptionPane.showConfirmDialog(this, "¿Desea remover este producto?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (valor == JOptionPane.YES_OPTION) {
                    int codigo_pro = Integer.parseInt(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 0).toString());

                    //Actualiza variables:
                    num_det--;
                    
                    double c6 = Double.parseDouble(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 6).toString());
                    subtotal -= c6;
                    subtotal = Math.rint(subtotal*100.0)/100.0; //deja al valor con dos decimales
                    
                    double c3 = Double.parseDouble(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 3).toString());
                    int c2 = Integer.parseInt(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 2).toString());
                    
                    total_descuento -= (c2 * c3) - c6;
                    total_descuento = Math.rint(total_descuento * 100.0) / 100.0; //deja al valor con dos decimales 

                    f_num_det.setText("Detalles: " + num_det);
                    f_descuento.setText("- $" + total_descuento);
                    f_subtotal.setText("$" + subtotal);
                    calcular_total();

                    //----> Eliminar un detalle a la tabla:
                    tabla_detalle.removeRow(JTdetalle.getSelectedRow());
                    JTdetalle.setModel(tabla_detalle);
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
    }//GEN-LAST:event_JTdetalleMouseClicked

    private void JBlimpiar_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBlimpiar_facturaActionPerformed
        reiniciar_factura();
    }//GEN-LAST:event_JBlimpiar_facturaActionPerformed

    private void f_fechaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_f_fechaMouseClicked
        f_fecha.setText(fechas.transformar(fechas.obtener()));
        f_fecha.setBackground(Color.green);
    }//GEN-LAST:event_f_fechaMouseClicked

    private void f_fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_fechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_f_fechaActionPerformed

    private void JCcliItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCcliItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCcliItemStateChanged

    private void BcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BcliMouseClicked
        if (Bcli.getText().equals("Buscar")) {
            Bcli.select(0, 0);
        }
    }//GEN-LAST:event_BcliMouseClicked

    private void BcliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyPressed
        if (Bcli.getText().equals("Buscar")) {
            Bcli.setText("");
            Lcli.setVisible(true);
        }
    }//GEN-LAST:event_BcliKeyPressed

    private void BcliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyReleased
       if (!Bcli.getText().equals("")) {
            buscar(JTcli, Bcli, res_cli, cli, JCcli);
        } else {
            Lcli.setVisible(false);
            Bcli.setText("Buscar");
            Bcli.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BcliKeyReleased

    private void BcliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BcliKeyTyped

    private void LcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LcliMouseClicked
        Lcli.setVisible(false);
        Bcli.setText("Buscar");
        Bcli.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LcliMouseClicked

    private void JCperItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCperItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCperItemStateChanged

    private void BperMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BperMouseClicked
        if (Bper.getText().equals("Buscar")) {
            Bper.select(0, 0);
        }
    }//GEN-LAST:event_BperMouseClicked

    private void BperKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyPressed
        if (Bper.getText().equals("Buscar")) {
            Bper.setText("");
            Lper.setVisible(true);
        }
    }//GEN-LAST:event_BperKeyPressed

    private void BperKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyReleased
      if (!Bper.getText().equals("")) {
            buscar(JTper, Bper, res_per, per, JCper);
        } else {
            Lper.setVisible(false);
            Bper.setText("Buscar");
            Bper.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BperKeyReleased

    private void BperKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BperKeyTyped

    private void LperMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LperMouseClicked
        Lper.setVisible(false);
        Bper.setText("Buscar");
        Bper.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LperMouseClicked

    private void elim_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_cliActionPerformed
        if (JPcli.isVisible()) {
            eliminar(3);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_cliActionPerformed

    private void elim_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_cliMouseExited

    private void elim_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_cliMouseEntered

    private void reg_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_cliMouseEntered

    private void reg_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_cliMouseExited

    private void reg_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_cliActionPerformed
        JFcliente.forma = "registrar";
        JFcliente.cambiar_diseño();
        JFcliente.limpiar();
        JFcli.setVisible(true);
    }//GEN-LAST:event_reg_cliActionPerformed

    private void mod_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_cliMouseEntered

    private void mod_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_cliMouseExited

    private void mod_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_cliActionPerformed
        if (JPcli.isVisible()) {
            JFcliente.forma = "modificar";
            JFcliente.cambiar_diseño();
            JFcli.llenar(cedula_cli.getText());
            reiniciar_factura(); //por precaución
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_cliActionPerformed

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
            buscar(JTemp, Bemp, res_emp, emp, JCemp);
        } else {
            Lemp.setVisible(false);
            Bemp.setText("Buscar");
            Bemp.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BempKeyReleased

    private void BempKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BempKeyTyped

    private void JCempItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCempItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCempItemStateChanged

    private void LempMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LempMouseClicked
        Lemp.setVisible(false);
        Bemp.setText("Buscar");
        Bemp.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LempMouseClicked

    private void elim_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_empMouseEntered

    private void elim_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_empMouseExited

    private void elim_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_empActionPerformed
        if (JPemp.isVisible()) {
            eliminar(8);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_empActionPerformed

    private void mod_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_empMouseEntered

    private void mod_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_empMouseExited

    private void mod_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_empActionPerformed
        if (JPemp.isVisible()) {
            JFempleado.forma = "modificar";
            JFempleado.cambiar_diseño();
            JFemp.llenar(cedula_emp.getText());
            reiniciar_factura(); //por precaución
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_empActionPerformed

    private void reg_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_empMouseEntered

    private void reg_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_empMouseExited

    private void reg_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_empActionPerformed
        JFempleado.forma = "registrar";
        JFempleado.cambiar_diseño();
        JFempleado.limpiar();
        JFemp.setVisible(true);
    }//GEN-LAST:event_reg_empActionPerformed

    private void jLabel86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel86MouseClicked
        JScli_emp.getVerticalScrollBar().setValue(0);
    }//GEN-LAST:event_jLabel86MouseClicked

    private void LdepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdepMouseClicked
        Ldep.setVisible(false);
        Bdep.setText("Buscar");
        Bdep.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LdepMouseClicked

    private void JCdepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdepItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdepItemStateChanged

    private void BdepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdepKeyTyped

    private void BdepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyReleased
        if (!Bdep.getText().equals("")) {
            buscar(JTdep, Bdep, res_dep, dep, JCdep);
        } else {
            Ldep.setVisible(false);
            Bdep.setText("Buscar");
            Bdep.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BdepKeyReleased

    private void BdepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyPressed
        if (Bdep.getText().equals("Buscar")) {
            Bdep.setText("");
            Ldep.setVisible(true);
        }
    }//GEN-LAST:event_BdepKeyPressed

    private void BdepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdepMouseClicked
        if (Bdep.getText().equals("Buscar")) {
            Bdep.select(0, 0);
        }
    }//GEN-LAST:event_BdepMouseClicked

    private void mod_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_pueActionPerformed
        if (JPpue.isVisible()) {
            JFpuesto.forma = "modificar";
            JFpuesto.cambiar_diseño();
            JFpue.llenar(id_pue.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_pueActionPerformed

    private void mod_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_pueMouseExited

    private void mod_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_pueMouseEntered

    private void reg_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_pueActionPerformed
        JFpuesto.forma = "registrar";
        JFpuesto.cambiar_diseño();
        JFpuesto.limpiar();
        JFpue.setVisible(true);
    }//GEN-LAST:event_reg_pueActionPerformed

    private void reg_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_pueMouseExited

    private void reg_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_pueMouseEntered

    private void elim_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_pueActionPerformed
        if (JPpue.isVisible()) {
            eliminar(21);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_pueActionPerformed

    private void elim_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_pueMouseExited

    private void elim_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_pueMouseEntered

    private void LpueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpueMouseClicked
        Lpue.setVisible(false);
        Bpue.setText("Buscar");
        Bpue.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LpueMouseClicked

    private void BpueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpueKeyTyped

    private void BpueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyReleased
        if (!Bpue.getText().equals("")) {
            buscar(JTpue, Bpue, res_pue, pue, JCpue);
        } else {
            Lpue.setVisible(false);
            Bpue.setText("Buscar");
            Bpue.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BpueKeyReleased

    private void BpueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyPressed
        if (Bpue.getText().equals("Buscar")) {
            Bpue.setText("");
            Lpue.setVisible(true);
        }
    }//GEN-LAST:event_BpueKeyPressed

    private void BpueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpueMouseClicked
        if (Bpue.getText().equals("Buscar")) {
            Bpue.select(0, 0);
        }
    }//GEN-LAST:event_BpueMouseClicked

    private void JCpueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpueItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpueItemStateChanged

    private void JCproItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCproItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCproItemStateChanged

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
            buscar(JTpro, Bpro, res_pro, pro, JCpro);
        } else {
            Lpro.setVisible(false);
            Bpro.setText("Buscar");
            Bpro.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BproKeyReleased

    private void BproKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BproKeyTyped

    private void LproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LproMouseClicked
        Lpro.setVisible(false);
        Bpro.setText("Buscar");
        Bpro.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LproMouseClicked

    private void elim_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proMouseEntered

    private void elim_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proMouseExited

    private void elim_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_proActionPerformed
        if (JPpro.isVisible()) {
            eliminar(18);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_proActionPerformed

    private void mod_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proMouseEntered

    private void mod_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proMouseExited

    private void mod_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_proActionPerformed
        if (JPpro.isVisible()) {
            JFproducto.forma = "modificar";
            JFproducto.cambiar_diseño();
            JFpro.llenar(Integer.parseInt(codigo_pro.getText()));
            reiniciar_factura(); //por precaución
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_proActionPerformed

    private void reg_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proMouseEntered

    private void reg_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proMouseExited

    private void reg_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_proActionPerformed
        JFproducto.forma = "registrar";
        JFproducto.cambiar_diseño();
        JFproducto.limpiar();
        JFpro.setVisible(true);
    }//GEN-LAST:event_reg_proActionPerformed

    private void JCmarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCmarItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCmarItemStateChanged

    private void BmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BmarMouseClicked
        if (Bmar.getText().equals("Buscar")) {
            Bmar.select(0, 0);
        }
    }//GEN-LAST:event_BmarMouseClicked

    private void BmarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyPressed
        if (Bmar.getText().equals("Buscar")) {
            Bmar.setText("");
            Lmar.setVisible(true);
        }
    }//GEN-LAST:event_BmarKeyPressed

    private void BmarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyReleased
        if (!Bmar.getText().equals("")) {
            buscar(JTmar, Bmar, res_mar, mar, JCmar);
        } else {
            Lmar.setVisible(false);
            Bmar.setText("Buscar");
            Bmar.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BmarKeyReleased

    private void BmarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BmarKeyTyped

    private void LmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LmarMouseClicked
        Lmar.setVisible(false);
        Bmar.setText("Buscar");
        Bmar.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LmarMouseClicked

    private void elim_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_marMouseEntered

    private void elim_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_marMouseExited

    private void elim_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_marActionPerformed
        if (JPmar.isVisible()) {
            eliminar(14);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_marActionPerformed

    private void reg_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_marMouseEntered

    private void reg_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_marMouseExited

    private void reg_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_marActionPerformed
        JFmarca.forma = "registrar";
        JFmarca.cambiar_diseño();
        JFmarca.limpiar();
        JFmar.setVisible(true);
    }//GEN-LAST:event_reg_marActionPerformed

    private void mod_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_marMouseEntered

    private void mod_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_marMouseExited

    private void mod_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_marActionPerformed
        if (JPmar.isVisible()) {
            JFmarca.forma = "modificar";
            JFmarca.cambiar_diseño();
            JFmar.llenar(Integer.parseInt(id_mar.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_marActionPerformed

    private void BcatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BcatMouseClicked
        if (Bcat.getText().equals("Buscar")) {
            Bcat.select(0, 0);
        }
    }//GEN-LAST:event_BcatMouseClicked

    private void BcatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyPressed
        if (Bcat.getText().equals("Buscar")) {
            Bcat.setText("");
            Lcat.setVisible(true);
        }
    }//GEN-LAST:event_BcatKeyPressed

    private void BcatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyReleased
        if (!Bcat.getText().equals("")) {
            buscar(JTcat, Bcat, res_cat, cat, JCcat);
        } else {
            Lcat.setVisible(false);
            Bcat.setText("Buscar");
            Bcat.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BcatKeyReleased

    private void BcatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcatKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BcatKeyTyped

    private void JCcatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCcatItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCcatItemStateChanged

    private void LcatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LcatMouseClicked
        Lcat.setVisible(false);
        Bcat.setText("Buscar");
        Bcat.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LcatMouseClicked

    private void elim_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_catMouseEntered

    private void elim_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_catMouseExited

    private void elim_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_catActionPerformed
        if (JPcat.isVisible()) {
            eliminar(1);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }

    }//GEN-LAST:event_elim_catActionPerformed

    private void mod_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_catMouseEntered

    private void mod_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_catMouseExited

    private void mod_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_catActionPerformed
        if (JPcat.isVisible()) {
            JFcategoria.forma = "modificar";
            JFcategoria.cambiar_diseño();
            JFcat.llenar(id_cat.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_catActionPerformed

    private void reg_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_catMouseEntered

    private void reg_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_catMouseExited

    private void reg_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_catActionPerformed
        JFcategoria.forma = "registrar";
        JFcategoria.cambiar_diseño();
        JFcategoria.limpiar();
        JFcat.setVisible(true);
    }//GEN-LAST:event_reg_catActionPerformed

    private void descripcion_catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcion_catKeyPressed

    }//GEN-LAST:event_descripcion_catKeyPressed

    private void reg_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_depMouseEntered

    private void reg_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_depMouseExited

    private void reg_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_depActionPerformed
        JFdepartamento.forma = "registrar";
        JFdepartamento.cambiar_diseño();
        JFdepartamento.limpiar();
        JFdep.setVisible(true);
    }//GEN-LAST:event_reg_depActionPerformed

    private void mod_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_depMouseEntered

    private void mod_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_depMouseExited

    private void mod_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_depActionPerformed
        if (JPdep.isVisible()) {
            JFdepartamento.forma = "modificar";
            JFdepartamento.cambiar_diseño();
            JFdep.llenar(id_dep.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_depActionPerformed

    private void elim_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_depMouseEntered

    private void elim_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_depMouseExited

    private void elim_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_depActionPerformed
        if (JPdep.isVisible()) {
            eliminar(4);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_depActionPerformed

    private void JCprovItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCprovItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCprovItemStateChanged

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
            buscar(JTprov, Bprov, res_prov, prov, JCprov);
        } else {
            Lprov.setVisible(false);
            Bprov.setText("Buscar");
            Bprov.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BprovKeyReleased

    private void BprovKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovKeyTyped

    private void LprovMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LprovMouseClicked
        Lprov.setVisible(false);
        Bprov.setText("Buscar");
        Bprov.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LprovMouseClicked

    private void elim_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_provMouseEntered

    private void elim_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_provMouseExited

    private void elim_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_provActionPerformed
        if (JPprov.isVisible()) {
            eliminar(19);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_provActionPerformed

    private void reg_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_provMouseEntered

    private void reg_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_provMouseExited

    private void reg_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_provActionPerformed
        JFproveedor.forma = "registrar";
        JFproveedor.cambiar_diseño();
        JFproveedor.limpiar();
        JFprov.setVisible(true);
    }//GEN-LAST:event_reg_provActionPerformed

    private void mod_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_provMouseEntered

    private void mod_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_provMouseExited

    private void mod_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_provActionPerformed
        if (JPprov.isVisible()) {
            JFproveedor.forma = "modificar";
            JFproveedor.cambiar_diseño();
            JFprov.llenar(ruc_prov.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_provActionPerformed

    private void BsucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BsucMouseClicked
        if (Bsuc.getText().equals("Buscar")) {
            Bsuc.select(0, 0);
        }
    }//GEN-LAST:event_BsucMouseClicked

    private void BsucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyPressed
        if (Bsuc.getText().equals("Buscar")) {
            Bsuc.setText("");
            Lsuc.setVisible(true);
        }
    }//GEN-LAST:event_BsucKeyPressed

    private void BsucKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyReleased
        if (!Bsuc.getText().equals("")) {
            buscar(JTsuc, Bsuc, res_suc, suc, JCsuc);
        } else {
            Lsuc.setVisible(false);
            Bsuc.setText("Buscar");
            Bsuc.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BsucKeyReleased

    private void BsucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BsucKeyTyped

    private void JCsucItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCsucItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCsucItemStateChanged

    private void LsucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LsucMouseClicked
        Lsuc.setVisible(false);
        Bsuc.setText("Buscar");
        Bsuc.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LsucMouseClicked

    private void reg_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_sucMouseEntered

    private void reg_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_sucMouseExited

    private void reg_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_sucActionPerformed
        JFsucursal.forma = "registrar";
        JFsucursal.cambiar_diseño();
        JFsucursal.limpiar();
        JFsuc.setVisible(true);
    }//GEN-LAST:event_reg_sucActionPerformed

    private void mod_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_sucMouseEntered

    private void mod_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_sucMouseExited

    private void mod_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_sucActionPerformed
        if (JPsuc.isVisible()) {
            JFsucursal.forma = "modificar";
            JFsucursal.cambiar_diseño();
            JFsuc.llenar(Integer.parseInt(id_suc.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_sucActionPerformed

    private void elim_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_sucMouseEntered

    private void elim_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_sucMouseExited

    private void elim_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_sucActionPerformed
        if (JPsuc.isVisible()) {
            eliminar(22);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_sucActionPerformed

    private void JCciuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCciuItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCciuItemStateChanged

    private void BciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BciuMouseClicked
        if (Bciu.getText().equals("Buscar")) {
            Bciu.select(0, 0);
        }
    }//GEN-LAST:event_BciuMouseClicked

    private void BciuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyPressed
        if (Bciu.getText().equals("Buscar")) {
            Bciu.setText("");
            Lciu.setVisible(true);
        }
    }//GEN-LAST:event_BciuKeyPressed

    private void BciuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyReleased
        if (!Bciu.getText().equals("")) {
            buscar(JTciu, Bciu, res_ciu, ciu, JCciu);
        } else {
            Lciu.setVisible(false);
            Bciu.setText("Buscar");
            Bciu.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BciuKeyReleased

    private void BciuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BciuKeyTyped

    private void LciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LciuMouseClicked
        Lciu.setVisible(false);
        Bciu.setText("Buscar");
        Bciu.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LciuMouseClicked

    private void elim_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ciuMouseEntered

    private void elim_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ciuMouseExited

    private void elim_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_ciuActionPerformed
        if (JPciu.isVisible()) {
            eliminar(2);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_ciuActionPerformed

    private void reg_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ciuMouseEntered

    private void reg_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ciuMouseExited

    private void reg_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_ciuActionPerformed
        JFciudad.forma = "registrar";
        JFciudad.cambiar_diseño();
        JFciudad.limpiar();
        JFciudad.cargar_ciudades();
        JFciu.setVisible(true);
    }//GEN-LAST:event_reg_ciuActionPerformed

    private void mod_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ciuMouseEntered

    private void mod_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ciuMouseExited

    private void mod_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_ciuActionPerformed
        if (JPciu.isVisible()) {
            JFciudad.forma = "modificar";
            JFciudad.cambiar_diseño();
            JFciudad.cargar_ciudades_mod(nombre_ciu.getText());
            JFciu.llenar(Integer.parseInt(id_ciu.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_ciuActionPerformed

    private void BproviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BproviMouseClicked
        if (Bprovi.getText().equals("Buscar")) {
            Bprovi.select(0, 0);
        }
    }//GEN-LAST:event_BproviMouseClicked

    private void BproviKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyPressed
        if (Bprovi.getText().equals("Buscar")) {
            Bprovi.setText("");
            Lprovi.setVisible(true);
        }
    }//GEN-LAST:event_BproviKeyPressed

    private void BproviKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyReleased
        if (!Bprovi.getText().equals("")) {
            buscar(JTprovi, Bprovi, res_provi, provi, JCprovi);
        } else {
            Lprovi.setVisible(false);
            Bprovi.setText("Buscar");
            Bprovi.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BproviKeyReleased

    private void BproviKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BproviKeyTyped

    private void JCproviItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCproviItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCproviItemStateChanged

    private void LproviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LproviMouseClicked
        Lprovi.setVisible(false);
        Bprovi.setText("Buscar");
        Bprovi.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LproviMouseClicked

    private void reg_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proviMouseEntered

    private void reg_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proviMouseExited

    private void reg_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_proviActionPerformed
        JFprovincia.forma = "registrar";
        JFprovincia.cambiar_diseño();
        JFprovincia.limpiar();
        JFprovincia.cargar_provincias();
        JFprovi.setVisible(true);
    }//GEN-LAST:event_reg_proviActionPerformed

    private void mod_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proviMouseEntered

    private void mod_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proviMouseExited

    private void mod_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_proviActionPerformed
        if (JPprovi.isVisible()) {
            JFprovincia.forma = "modificar";
            JFprovincia.cambiar_diseño();
            JFprovincia.cargar_provincias_mod(nombre_provi.getText());
            JFprovi.llenar(Integer.parseInt(id_provi.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_proviActionPerformed

    private void elim_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proviMouseEntered

    private void elim_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proviMouseExited

    private void elim_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_proviActionPerformed
        if (JPprovi.isVisible()) {
            eliminar(20);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_proviActionPerformed

    private void JCgenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCgenItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCgenItemStateChanged

    private void BgenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BgenMouseClicked
        if (Bgen.getText().equals("Buscar")) {
            Bgen.select(0, 0);
        }
    }//GEN-LAST:event_BgenMouseClicked

    private void BgenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyPressed
        if (Bgen.getText().equals("Buscar")) {
            Bgen.setText("");
            Lgen.setVisible(true);
        }
    }//GEN-LAST:event_BgenKeyPressed

    private void BgenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyReleased
        if (!Bgen.getText().equals("")) {
            buscar(JTgen, Bgen, res_gen, gen, JCgen);
        } else {
            Lgen.setVisible(false);
            Bgen.setText("Buscar");
            Bgen.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BgenKeyReleased

    private void BgenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BgenKeyTyped

    private void LgenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LgenMouseClicked
        Lgen.setVisible(false);
        Bgen.setText("Buscar");
        Bgen.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LgenMouseClicked

    private void elim_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_genMouseEntered

    private void elim_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_genMouseExited

    private void elim_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_genActionPerformed
        if (JPgen.isVisible()) {
            eliminar(12);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_genActionPerformed

    private void reg_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_genMouseEntered

    private void reg_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_genMouseExited

    private void reg_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_genActionPerformed
        JFgenero.forma = "registrar";
        JFgenero.cambiar_diseño();
        JFgenero.limpiar();
        JFgen.setVisible(true);
    }//GEN-LAST:event_reg_genActionPerformed

    private void mod_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_genMouseEntered

    private void mod_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_genMouseExited

    private void mod_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_genActionPerformed
        if (JPgen.isVisible()) {
            JFgenero.forma = "modificar";
            JFgenero.cambiar_diseño();
            JFgen.llenar(Integer.parseInt(id_gen.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_genActionPerformed

    private void BdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdesMouseClicked
        if (Bdes.getText().equals("Buscar")) {
            Bdes.select(0, 0);
        }
    }//GEN-LAST:event_BdesMouseClicked

    private void BdesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyPressed
        if (Bdes.getText().equals("Buscar")) {
            Bdes.setText("");
            Ldes.setVisible(true);
        }
    }//GEN-LAST:event_BdesKeyPressed

    private void BdesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyReleased
        if (!Bdes.getText().equals("")) {
            buscar(JTdes, Bdes, res_des, des, JCdes);
        } else {
            Ldes.setVisible(false);
            Bdes.setText("Buscar");
            Bdes.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BdesKeyReleased

    private void BdesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdesKeyTyped

    private void JCdesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdesItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdesItemStateChanged

    private void LdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdesMouseClicked
        Ldes.setVisible(false);
        Bdes.setText("Buscar");
        Bdes.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LdesMouseClicked

    private void elim_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_desMouseEntered

    private void elim_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_desMouseExited

    private void elim_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_desActionPerformed
        if (JPdes.isVisible()) {
            eliminar(5);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_desActionPerformed

    private void mod_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_desMouseEntered

    private void mod_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_desMouseExited

    private void mod_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_desActionPerformed
        if (JPdes.isVisible()) {
            JFdescuento.forma = "modificar";
            JFdescuento.cambiar_diseño();
            JFdes.llenar(Integer.parseInt(id_des.getText()));
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_desActionPerformed

    private void reg_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_desMouseEntered

    private void reg_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_desMouseExited

    private void reg_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_desActionPerformed
        JFdescuento.forma = "registrar";
        JFdescuento.cambiar_diseño();
        JFdescuento.limpiar();
        JFdes.setVisible(true);
    }//GEN-LAST:event_reg_desActionPerformed

    private void JCpeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpeItemStateChanged

    private void BpeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpeMouseClicked
        if (Bpe.getText().equals("Buscar")) {
            Bpe.select(0, 0);
        }
    }//GEN-LAST:event_BpeMouseClicked

    private void BpeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyPressed
        if (Bpe.getText().equals("Buscar")) {
            Bpe.setText("");
            Lpe.setVisible(true);
        }
    }//GEN-LAST:event_BpeKeyPressed

    private void BpeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyReleased
        if (!Bpe.getText().equals("")) {
            buscar(JTpe, Bpe, res_pe, pe, JCpe);
        } else {
            Lpe.setVisible(false);
            Bpe.setText("Buscar");
            Bpe.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BpeKeyReleased

    private void BpeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpeKeyTyped

    private void LpeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpeMouseClicked
        Lpe.setVisible(false);
        Bpe.setText("Buscar");
        Bpe.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LpeMouseClicked

    private void reg_peMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_peMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_peMouseEntered

    private void reg_peMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_peMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_peMouseExited

    private void reg_peActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_peActionPerformed
        JFpago_empleado.forma = "registrar";
        JFpago_empleado.cambiar_diseño();
        JFpago_empleado.limpiar();
        JFpe.setVisible(true);
    }//GEN-LAST:event_reg_peActionPerformed

    private void JCivaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCivaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCivaItemStateChanged

    private void BivaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BivaMouseClicked
        if (Biva.getText().equals("Buscar")) {
            Biva.select(0, 0);
        }
    }//GEN-LAST:event_BivaMouseClicked

    private void BivaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyPressed
        if (Biva.getText().equals("Buscar")) {
            Biva.setText("");
            Liva.setVisible(true);
        }
    }//GEN-LAST:event_BivaKeyPressed

    private void BivaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyReleased
        if (!Biva.getText().equals("")) {
            buscar(JTiva, Biva, res_iva, iva, JCiva);
        } else {
            Liva.setVisible(false);
            Biva.setText("Buscar");
            Biva.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BivaKeyReleased

    private void BivaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BivaKeyTyped

    private void LivaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LivaMouseClicked
        Liva.setVisible(false);
        Biva.setText("Buscar");
        Biva.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LivaMouseClicked

    private void elim_ivaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ivaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ivaMouseEntered

    private void elim_ivaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ivaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ivaMouseExited

    private void elim_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_ivaActionPerformed
        if (JPiva.isVisible()) {
            eliminar(13);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_ivaActionPerformed

    private void reg_ivaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ivaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ivaMouseEntered

    private void reg_ivaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ivaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ivaMouseExited

    private void reg_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_ivaActionPerformed
        JFiva.limpiar();
        JFIVA.setVisible(true);
    }//GEN-LAST:event_reg_ivaActionPerformed

    private void BfpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BfpMouseClicked
        if (Bfp.getText().equals("Buscar")) {
            Bfp.select(0, 0);
        }
    }//GEN-LAST:event_BfpMouseClicked

    private void BfpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyPressed
        if (Bfp.getText().equals("Buscar")) {
            Bfp.setText("");
            Lfp.setVisible(true);
        }
    }//GEN-LAST:event_BfpKeyPressed

    private void BfpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyReleased
        if (!Bfp.getText().equals("")) {
            buscar(JTfp, Bfp, res_fp, fp, JCfp);
        } else {
            Lfp.setVisible(false);
            Bfp.setText("Buscar");
            Bfp.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BfpKeyReleased

    private void BfpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BfpKeyTyped

    private void JCfpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCfpItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCfpItemStateChanged

    private void LfpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LfpMouseClicked
        Lfp.setVisible(false);
        Bfp.setText("Buscar");
        Bfp.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LfpMouseClicked

    private void descripcion_depKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcion_depKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcion_depKeyPressed

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        actualizado = false;
    }//GEN-LAST:event_formWindowLostFocus

    private void elim_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_fpActionPerformed
        if (JPfp.isVisible()) {
            eliminar(11);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_elim_fpActionPerformed

    private void elim_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_fpMouseExited

    private void elim_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_fpMouseEntered

    private void mod_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_fpActionPerformed
        if (JPfp.isVisible()) {
            JFforma_pago.forma = "modificar";
            JFforma_pago.cambiar_diseño();
            JFfp.llenar(id_fp.getText());
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "¡Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_fpActionPerformed

    private void mod_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_fpMouseExited

    private void mod_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_fpMouseEntered

    private void reg_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_fpActionPerformed
        JFforma_pago.forma = "registrar";
        JFforma_pago.cambiar_diseño();
        JFforma_pago.limpiar();
        JFfp.setVisible(true);
    }//GEN-LAST:event_reg_fpActionPerformed

    private void reg_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_fpMouseExited

    private void reg_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_fpMouseEntered

    private void f_empMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_f_empMouseClicked
        MENU.setSelectedIndex(1);
        PERSONAS.setSelectedIndex(0);
    }//GEN-LAST:event_f_empMouseClicked

    private void f_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_empActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_f_empActionPerformed

    private void f_sucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_f_sucMouseClicked
        MENU.setSelectedIndex(5);
    }//GEN-LAST:event_f_sucMouseClicked

    private void f_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f_sucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_f_sucActionPerformed

    private void cerrar_sesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cerrar_sesionMouseClicked
        this.dispose();
        JFlogin sesión = new JFlogin();
        sesión.setVisible(true);
    }//GEN-LAST:event_cerrar_sesionMouseClicked

    private void f_ivaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_f_ivaItemStateChanged
        calcular_total();
    }//GEN-LAST:event_f_ivaItemStateChanged

    private void f_forma_pagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_f_forma_pagoItemStateChanged
        if (f_forma_pago.getSelectedIndex() != 0) {
            f_forma_pago.setBackground(Color.green);
        } else {
            f_forma_pago.setBackground(Color.red);
        }
    }//GEN-LAST:event_f_forma_pagoItemStateChanged

    private void LeppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LeppMouseClicked
        Lepp.setVisible(false);
        Bepp.setText("Buscar");
        Bepp.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LeppMouseClicked

    private void BeppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BeppMouseClicked
        if (Bepp.getText().equals("Buscar")) {
            Bepp.select(0, 0);
        }
    }//GEN-LAST:event_BeppMouseClicked

    private void BeppKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BeppKeyPressed
        if (Bepp.getText().equals("Buscar")) {
            Bepp.setText("");
            Lepp.setVisible(true);
        }
    }//GEN-LAST:event_BeppKeyPressed

    private void BeppKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BeppKeyReleased
        if (!Bepp.getText().equals("")) {
            buscar(JTenc_pp, Bepp, res_enc_pp, e_pp, JCepp);
        } else {
            Lepp.setVisible(false);
            Bepp.setText("Buscar");
            Bepp.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BeppKeyReleased

    private void BeppKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BeppKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BeppKeyTyped

    private void JCeppItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCeppItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCeppItemStateChanged

    private void reg_ppMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ppMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ppMouseEntered

    private void reg_ppMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ppMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ppMouseExited

    private void reg_ppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_ppActionPerformed
        JFpago_proveedor.limpiar();
        JFpago_proveedor.visualizar(1);
        JFpago_proveedor.visualizar(2);
        JFpp.setVisible(true);
    }//GEN-LAST:event_reg_ppActionPerformed

    private void JCdppItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdppItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdppItemStateChanged

    private void BdppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdppMouseClicked
        if (Bdpp.getText().equals("Buscar")) {
            Bdpp.select(0, 0);
        }
    }//GEN-LAST:event_BdppMouseClicked

    private void BdppKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdppKeyPressed
        if (Bdpp.getText().equals("Buscar")) {
            Bdpp.setText("");
            Ldpp.setVisible(true);
        }
    }//GEN-LAST:event_BdppKeyPressed

    private void BdppKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdppKeyReleased
        if (!Bdpp.getText().equals("")) {
            buscar(JTdet_pp, Bdpp, res_det_pp, d_pp, JCdpp);
        } else {
            Ldpp.setVisible(false);
            Bdpp.setText("Buscar");
            Bdpp.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BdppKeyReleased

    private void BdppKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdppKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdppKeyTyped

    private void LdppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdppMouseClicked
        Ldpp.setVisible(false);
        Bdpp.setText("Buscar");
        Bdpp.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LdppMouseClicked

    private void JCpagItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpagItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpagItemStateChanged

    private void BpagKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpagKeyTyped

    private void BpagKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyReleased
        if (!Bpag.getText().equals("")) {
            buscar(JTpag, Bpag, res_pag, pag, JCpag);
        } else {
            Lpag.setVisible(false);
            Bpag.setText("Buscar");
            Bpag.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BpagKeyReleased

    private void BpagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyPressed
        if (Bpag.getText().equals("Buscar")) {
            Bpag.setText("");
            Lpag.setVisible(true);
        }
    }//GEN-LAST:event_BpagKeyPressed

    private void BpagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpagMouseClicked
        if (Bpag.getText().equals("Buscar")) {
            Bpag.select(0, 0);
        }
    }//GEN-LAST:event_BpagMouseClicked

    private void LpagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpagMouseClicked
        Lpag.setVisible(false);
        Bpag.setText("Buscar");
        Bpag.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LpagMouseClicked

    private void LdetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdetMouseClicked
        Ldet.setVisible(false);
        Bdet.setText("Buscar");
        Bdet.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LdetMouseClicked

    private void BdetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdetKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdetKeyTyped

    private void BdetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdetKeyReleased
        if (!Bdet.getText().equals("")) {
            buscar(JTdet, Bdet, res_det, det, JCdet);
        } else {
            Ldet.setVisible(false);
            Bdet.setText("Buscar");
            Bdet.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BdetKeyReleased

    private void BdetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdetKeyPressed
        if (Bdet.getText().equals("Buscar")) {
            Bdet.setText("");
            Ldet.setVisible(true);
        }
    }//GEN-LAST:event_BdetKeyPressed

    private void BdetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdetMouseClicked
        if (Bdet.getText().equals("Buscar")) {
            Bdet.select(0, 0);
        }
    }//GEN-LAST:event_BdetMouseClicked

    private void JCdetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdetItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdetItemStateChanged

    private void LencMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LencMouseClicked
        Lenc.setVisible(false);
        Benc.setText("Buscar");
        Benc.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LencMouseClicked

    private void BencKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BencKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BencKeyTyped

    private void BencKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BencKeyReleased
        if (!Benc.getText().equals("")) {
            buscar(JTenc, Benc, res_enc, enc, JCenc);
        } else {
            Lenc.setVisible(false);
            Benc.setText("Buscar");
            Benc.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_BencKeyReleased

    private void BencKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BencKeyPressed
        if (Benc.getText().equals("Buscar")) {
            Benc.setText("");
            Lenc.setVisible(true);
        }
    }//GEN-LAST:event_BencKeyPressed

    private void BencMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BencMouseClicked
        if (Benc.getText().equals("Buscar")) {
            Benc.select(0, 0);
        }
    }//GEN-LAST:event_BencMouseClicked

    private void JCencItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCencItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCencItemStateChanged

    private void MENUKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MENUKeyPressed
        
    }//GEN-LAST:event_MENUKeyPressed

    
    public void InsertarIcono(JButton bot, String ruta){ //insertar icono en boton:
        bot.setIcon(new javax.swing.ImageIcon(getClass().getResource(ruta)));
    }
    
    
    //click o doble click en las tablas:
    public void seleccionar() {
        con = conexion.conectar();
        if (con != null) {
            JTcat.addMouseListener(new MouseAdapter() { //categoría - 1
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTcat.getValueAt(JTcat.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"CATEGORIA WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_cat.setText(""+rs.getInt(1));
                            nombre_cat.setText(rs.getString(2));
                            descripcion_cat.setText(rs.getString(3));
                            ver_panel(1,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFproducto.FK_cat = Integer.parseInt(pk);
                        JFproducto.jt_categoria.setText(""+pk+" - "+nombre_cat.getText());
                        MENU.setSelectedIndex(3);
                        JFpro.setVisible(true);
                    }
                }
            });
            JTciu.addMouseListener(new MouseAdapter() { //ciudad - 2
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTciu.getValueAt(JTciu.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"CIUDAD WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_ciu.setText(""+rs.getInt(1));
                            nombre_ciu.setText(rs.getString(2));
                            id_provi_ciu.setText(""+rs.getInt(3));
                            ver_panel(2,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        int opcion = JOptionPane.showOptionDialog(null, "¿A dónde desea enviar esta ciudad?", "Enviar ciudad", 0, JOptionPane.QUESTION_MESSAGE, null, arreglo, "Cliente");
                        switch(opcion) {
                            case 0://cliente
                                JFcliente.FK_ciu = Integer.parseInt(pk);
                                JFcliente.ciudad.setText(""+pk+" - "+nombre_ciu.getText());
                                MENU.setSelectedIndex(1);
                                PERSONAS.setSelectedIndex(0);
                                JFcli.setVisible(true);
                                break;
                            case 1://empleado
                                JFempleado.FK_ciu = Integer.parseInt(pk);
                                JFempleado.ciudad.setText(""+pk+" - "+nombre_ciu.getText());
                                MENU.setSelectedIndex(1);
                                PERSONAS.setSelectedIndex(0);
                                JFemp.setVisible(true);
                                break;

                            case 2://proveedor
                                JFproveedor.FK_ciu = Integer.parseInt(pk);
                                JFproveedor.jt_ciudad.setText("" + pk + " - " + nombre_ciu.getText());
                                MENU.setSelectedIndex(5);
                                JFprov.setVisible(true);
                                break;
                            case 3://sucursal
                                JFsucursal.FK_ciu = Integer.parseInt(pk);
                                JFsucursal.ciudad.setText("" + pk + " - " + nombre_ciu.getText());
                                MENU.setSelectedIndex(5);
                                JFsuc.setVisible(true);
                                break;
                        }
                    }
                }
            });
            JTcli.addMouseListener(new MouseAdapter() { //clientes - 3
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTcli.getValueAt(JTcli.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"CLIENTE WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_cli.setText(""+rs.getInt(1));
                            cedula_cli.setText(rs.getString(2));
                            ps = (PreparedStatement) con.prepareStatement(consulta+"DESCUENTO WHERE ID="+rs.getInt(3));
                            rs = ps.executeQuery();
                            rs.next();
                            descuento_cli.setText(rs.getString(2)+" ("+rs.getInt(3)+"%)");
                            descuento = rs.getInt(3);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA='"+cedula_cli.getText()+"'");
                            rs = ps.executeQuery();
                            rs.next();
                            
                            nombre_cli.setText(rs.getString(2));
                            apellido_cli.setText(rs.getString(3));
                            fecha_nac_cli.setText(rs.getDate(4).toString());
                            celular_cli.setText(rs.getString(6));
                            email_cli.setText(rs.getString(7));
                            direccion_cli.setText(rs.getString(8));
                            fecha_reg_cli.setText(rs.getDate(10).toString());
                           
                            ps = (PreparedStatement) con.prepareStatement(consulta + "GENERO WHERE ID="+rs.getInt(5));
                            rs2 = ps.executeQuery();
                            rs2.next();
                            sexo_cli.setText(rs2.getString(2));
                            ps = (PreparedStatement) con.prepareStatement(consulta + "CIUDAD WHERE ID="+rs.getInt(9));
                            rs = ps.executeQuery();
                            rs.next();
                            ciudad_cli.setText(rs.getString(2));
                            ver_panel(3,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        f_ced_cli.setText(cedula_cli.getText());
                        f_ced_cli.setBackground(Color.green);
                        f_id_cli.setText(pk);
                        f_nom_ape.setText(nombre_cli.getText() + " " + apellido_cli.getText());
                        f_dir.setText(direccion_cli.getText());
                        f_tel.setText(celular_cli.getText());
                        f_email.setText(email_cli.getText());
                        f_des.setText(descuento_cli.getText());
                        JBseleccionar_pro.setEnabled(true);
                        MENU.setSelectedIndex(0);
                        INICIO.setSelectedIndex(0);
                    }
                }
            });
            JTdep.addMouseListener(new MouseAdapter() { //departamento - 4
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTdep.getValueAt(JTdep.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"DEPARTAMENTO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_dep.setText(""+rs.getInt(1));
                            nombre_dep.setText(rs.getString(2));
                            descripcion_dep.setText(rs.getString(3));
                            ver_panel(4,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFempleado.FK_dep = Integer.parseInt(pk);
                        JFempleado.departamento.setText("" + pk + " - " + nombre_dep.getText());
                        MENU.setSelectedIndex(1);
                        PERSONAS.setSelectedIndex(0);
                        JFemp.setVisible(true);
                    }
                }
            });
            JTemp.addMouseListener(new MouseAdapter() { //empleados - 8
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTemp.getValueAt(JTemp.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"EMPLEADO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_emp.setText(""+rs.getInt(1));
                            cedula_emp.setText(rs.getString(2));
                            int FK_dep = rs.getInt(4);
                            int FK_pue = rs.getInt(5);
                            ps = (PreparedStatement) con.prepareStatement(consulta+"DEPARTAMENTO WHERE ID="+FK_dep);
                            rs = ps.executeQuery();
                            rs.next();
                            departamento_emp.setText(rs.getString(2));
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PUESTO WHERE ID="+FK_pue);
                            rs = ps.executeQuery();
                            rs.next();
                            puesto_emp.setText(rs.getString(2));
                            sueldo_emp.setText("$"+rs.getDouble(3));
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA='"+cedula_emp.getText()+"'");
                            rs = ps.executeQuery();
                            rs.next();
                            nombre_emp.setText(rs.getString(2));
                            apellido_emp.setText(rs.getString(3));
                            fecha_nac_emp.setText(rs.getDate(4).toString());
                            celular_emp.setText(rs.getString(6));
                            email_emp.setText(rs.getString(7));
                            direccion_emp.setText(rs.getString(8));
                            fecha_reg_emp.setText(rs.getDate(10).toString());
                            ps = (PreparedStatement) con.prepareStatement(consulta + "GENERO WHERE ID="+rs.getInt(5));
                            rs2 = ps.executeQuery();
                            rs2.next();
                            sexo_emp.setText(rs2.getString(2));
                            ps = (PreparedStatement) con.prepareStatement(consulta + "CIUDAD WHERE ID="+rs.getInt(9));
                            rs = ps.executeQuery();
                            rs.next();
                            ciudad_emp.setText(rs.getString(2));
                            ver_panel(8,true);
                        } catch (SQLException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        try {
                            int opcion = JOptionPane.showOptionDialog(null, "¿A dónde desea enviar este empleado?", "Enviar empleado", 0, JOptionPane.QUESTION_MESSAGE, null, arreglo3, null);
                            switch(opcion) {
                                case 0://factura
                                    FK_emp = Integer.parseInt(pk);
                                    f_emp.setText(nombre_emp.getText()+" "+apellido_emp.getText());
                                    f_emp.setBackground(Color.green);
                                    MENU.setSelectedIndex(0);
                                    INICIO.setSelectedIndex(0);
                                    break;
                                case 1://pagos sueldos (remitente)
                                    ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA='" + cedula_emp.getText() + "'");
                                    rs = ps.executeQuery();
                                    rs.next();
                                    nombre_emp.setText(rs.getString(2));
                                    JFpago_empleado.FK_paga = Integer.parseInt(pk);
                                    JFpago_empleado.id_paga.setText("" + pk + " - " + nombre_emp.getText()+" "+ apellido_emp.getText());
                                    MENU.setSelectedIndex(0);
                                    INICIO.setSelectedIndex(2);
                                    JFpe.setVisible(true);
                                    break;
                                    
                                case 2://pagos sueldos (destinatario)
                                     ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA='" + cedula_emp.getText() + "'");
                                    rs = ps.executeQuery();
                                    rs.next();
                                    nombre_emp.setText(rs.getString(2));
                                    JFpago_empleado.FK_recibe = Integer.parseInt(pk);
                                    JFpago_empleado.id_recibe.setText("" + pk + " - " + nombre_emp.getText()+" "+ apellido_emp.getText());
                                    ps = (PreparedStatement) con.prepareStatement(consulta + "EMPLEADO WHERE ID=" + pk);
                                    rs = ps.executeQuery();
                                    rs.next();
                                    int id_puesto=(rs.getInt(5));
                                    ps = (PreparedStatement) con.prepareStatement(consulta + "PUESTO WHERE ID='" + id_puesto + "'");
                                    rs = ps.executeQuery();
                                    rs.next();
                                    JFpago_empleado.totalsueldo.setText(String.valueOf(rs.getDouble(3)));
                                    MENU.setSelectedIndex(0);
                                    INICIO.setSelectedIndex(2);
                                    JFpe.setVisible(true);
                                    break;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(PRINCIPAL.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            JTenc.addMouseListener(new MouseAdapter() { //facturas - 9
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 2) {
                        pk = JTenc.getValueAt(JTenc.getSelectedRow(), 0).toString();
                        JFvf.llenar(pk);
                    }
                }
            });
            JTdes.addMouseListener(new MouseAdapter() { //descuento - 5
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTdes.getValueAt(JTdes.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"DESCUENTO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_des.setText(""+rs.getInt(1));
                            nombre_des.setText(rs.getString(2));
                            porcentaje_des.setText(""+rs.getInt(3)+"%");
                            ver_panel(5,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFcliente.FK_des = Integer.parseInt(pk);
                        JFcliente.descuento.setText("" + pk + " - " + nombre_des.getText());
                        MENU.setSelectedIndex(1);
                        PERSONAS.setSelectedIndex(0);
                        JFcli.setVisible(true);
                    }
                }
            });
            
            JTenc_pp.addMouseListener(new MouseAdapter() { //ENCABEZADO PP - 10
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTenc_pp.getValueAt(JTenc_pp.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"ENCABEZADO_PP WHERE CODIGO="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            codigo_pp.setText(pk);
                            total_pp.setText(String.valueOf(rs.getDouble(4)));
                            fecha_pag_pp.setText(String.valueOf(rs.getDate(5)));
                            String ruc_prov=rs.getString(2);
                            ps = (PreparedStatement) con.prepareStatement(consulta+"EMPLEADO WHERE ID="+rs.getInt(4));
                            rs = ps.executeQuery();
                            rs.next();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PERSONA WHERE CEDULA="+rs.getString(2));
                            rs = ps.executeQuery();
                            rs.next();
                            empleado_pp.setText(rs.getString(2)+" "+rs.getString(3));
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PROVEEDOR WHERE RUC="+ruc_prov);
                            rs = ps.executeQuery();
                            rs.next();
                            proveedor_pp.setText(rs.getString(2));
                        } catch (SQLException ex) {
                            
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        
                    }
                }
            });
            JTfp.addMouseListener(new MouseAdapter() { //FORMA DE PAGO - 11
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            System.out.println("aqui");
                            pk = JTfp.getValueAt(JTfp.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"FORMA_PAGO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_fp.setText(pk);
                            nombre_fp.setText(rs.getString(2));
                            ver_panel(11,true);
                        } catch (SQLException ex) {
                            
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        
                    }
                }
            });
            JTgen.addMouseListener(new MouseAdapter() { //género - 12
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTgen.getValueAt(JTgen.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"GENERO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_gen.setText(""+rs.getInt(1));
                            sexo_gen.setText(rs.getString(2));
                            ver_panel(12,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        int opcion = JOptionPane.showOptionDialog(null, "¿A dónde desea enviar esta género?", "Enviar género", 0, JOptionPane.QUESTION_MESSAGE, null, arreglo2, "Cliente");
                        switch(opcion) {
                            case 0://cliente;
                                JFcliente.FK_gen = Integer.parseInt(pk);
                                JFcliente.genero.setText(""+pk+" - "+sexo_gen.getText());
                                MENU.setSelectedIndex(1);
                                PERSONAS.setSelectedIndex(0);
                                JFcli.setVisible(true);
                                break;
                            case 1://empleado
                                JFempleado.FK_gen = Integer.parseInt(pk);
                                JFempleado.genero.setText(""+pk+" - "+sexo_gen.getText());
                                MENU.setSelectedIndex(1);
                                PERSONAS.setSelectedIndex(0);
                                JFemp.setVisible(true);
                                break;
                        }
                    }
                }
            });
            JTiva.addMouseListener(new MouseAdapter() { //IVA - 13
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTiva.getValueAt(JTiva.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"IVA WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_iva.setText(pk);
                            impuesto_iva.setText(""+rs.getInt(2)+"%");
                            ver_panel(13,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        
                    }
                }
            });
            JTmar.addMouseListener(new MouseAdapter() { //marca - 14
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTmar.getValueAt(JTmar.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta + "MARCA WHERE ID=" + pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_mar.setText("" + rs.getInt(1));
                            nombre_mar.setText(rs.getString(2));
                            ver_panel(14, true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFproducto.FK_mar = Integer.parseInt(pk);
                        JFproducto.jt_marca.setText("" + pk + " - " + nombre_mar.getText());
                        MENU.setSelectedIndex(3);
                        JFpro.setVisible(true);
                    }
                }
            });
            JTpe.addMouseListener(new MouseAdapter() { //pago_empleados - 15
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTpe.getValueAt(JTpe.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PAGO_EMPLEADO WHERE NUMERO=" + pk);
                            rs = ps.executeQuery();
                            rs.next();
                            numero_pe.setText("" + rs.getInt(1));
                            int paga = rs.getInt(2);
                            int recibe = rs.getInt(3);
                            total_pe.setText(String.valueOf(rs.getDouble(4)));
                            fecha_pag_pe.setText(rs.getDate(5).toString());
                            ps = (PreparedStatement) con.prepareStatement(consulta + "EMPLEADO WHERE ID=" + paga);
                            rs = ps.executeQuery();
                            rs.next();
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA=" + rs.getString(2));
                            rs = ps.executeQuery();
                            rs.next();
                            remitente_pe.setText(rs.getString(2) + " " + rs.getString(3));
                            ///////////////////////////////
                            ps = (PreparedStatement) con.prepareStatement(consulta + "EMPLEADO WHERE ID=" + recibe);
                            rs = ps.executeQuery();
                            rs.next();
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PERSONA WHERE CEDULA=" + rs.getString(2));
                            rs = ps.executeQuery();
                            rs.next();
                            destinatario_pe.setText(rs.getString(2) + " " + rs.getString(3));

                            ver_panel(15, true);
                        } catch (SQLException ex) {
                        }
                    }
                }
            });
            JTpro.addMouseListener(new MouseAdapter() { //producto - 18
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTpro.getValueAt(JTpro.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta + "PRODUCTO WHERE CODIGO=" + pk);
                            rs = ps.executeQuery();
                            rs.next();
                            codigo_pro.setText("" + rs.getInt(1));
                            nombre_pro.setText(rs.getString(2));
                            rsscalelabel.RSScaleLabel.setScaleLabel(imagen_pro, rs.getString(3));
                            int FK_mar = rs.getInt(4);
                            precio_pro.setText("$" + rs.getDouble(5));
                            maximos_pro.setText("" + rs.getInt(6));
                            minimos_pro.setText("" + rs.getInt(7));
                            stock_pro.setText("" + rs.getInt(8));
                            int FK_cat = rs.getInt(9);
                            fecha_reg_pro.setText(rs.getDate(10).toString());
                            String FK_prov = rs.getString(11);

                            ps = (PreparedStatement) con.prepareStatement(consulta + "MARCA WHERE ID=" + FK_mar);
                            rs = ps.executeQuery();
                            rs.next();
                            marca_pro.setText(rs.getString(2));

                            ps = (PreparedStatement) con.prepareStatement(consulta + "CATEGORIA WHERE ID=" + FK_cat);
                            rs = ps.executeQuery();
                            rs.next();
                            categoria_pro.setText(rs.getString(2));

                            ps = (PreparedStatement) con.prepareStatement(consulta + "PROVEEDOR WHERE RUC=" + FK_prov);
                            rs = ps.executeQuery();
                            rs.next();
                            proveedor_pro.setText(rs.getString(2));

                            ver_panel(18, true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        if (f_ced_cli.getText().isEmpty()) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(rootPane, "¡Primero seleccione un cliente!");
                        } else {
                            int existencias = Integer.parseInt(stock_pro.getText());
                            if (existencias > 0) {
                                boolean repetido = false;
                                for (int i = 0; i < detalles.size(); i++) {
                                    if (detalles.get(i).equals(codigo_pro.getText())) {
                                        repetido = true;
                                        break;
                                    }
                                }
                                if (repetido) {
                                    JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                                } else {
                                    try {
                                        int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
                                        if (cantidad > 0 && cantidad <= existencias) {
                                            Double xprecio = Double.valueOf(precio_pro.getText().replace("$", ""));
                                            JTdetalle.setDefaultRenderer(Object.class, new BotonTabla());
                                            Object detalle[] = {codigo_pro.getText(), nombre_pro.getText(), cantidad, xprecio, descuento + "%", xprecio - ((descuento * xprecio) / 100), Math.round((cantidad * (xprecio - ((descuento * xprecio) / 100))) * 100.0) / 100.0, boton1};
                                            detalles.add(codigo_pro.getText());
                                            //-------------- Agrega un detalle a la tabla
                                            tabla_detalle.addRow(detalle);
                                            JTdetalle.setModel(tabla_detalle);
                                            //--------------- Actualiza variables
                                            num_det++;
                                            double xsubtotal = cantidad * (xprecio - ((descuento * xprecio) / 100));
                                            subtotal += xsubtotal;
                                            subtotal = (Math.rint(subtotal * 100.0) / 100.0); //lo deja con 2 decimales

                                            total_descuento += (xprecio * cantidad) - (xsubtotal);
                                            total_descuento = (Math.rint(total_descuento * 100.0) / 100.0); //lo deja con 2 decimales

                                            f_num_det.setText("Detalles: " + num_det);
                                            f_descuento.setText("- $" + total_descuento);
                                            f_subtotal.setText("$" + (subtotal));
                                            calcular_total();
                                            MENU.setSelectedIndex(0);
                                            INICIO.setSelectedIndex(0);
                                        } else {
                                            if (cantidad > existencias) {
                                                JOptionPane.showMessageDialog(null, "¡Solo existen '" + existencias + "' de este producto!", null, JOptionPane.WARNING_MESSAGE);
                                            }
                                            if (cantidad <= 0) {
                                                JOptionPane.showMessageDialog(null, "¡El mínimo de venta es de 1!", null, JOptionPane.WARNING_MESSAGE);
                                            }
                                        }
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, "¡Cantiadad inválida!", null, JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "¡Producto agotado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                }
            });
            JTprov.addMouseListener(new MouseAdapter() { //proveedor - 19
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTprov.getValueAt(JTprov.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PROVEEDOR WHERE RUC="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            ruc_prov.setText(rs.getString(1));
                            nombre_prov.setText(rs.getString(2));
                            celular_prov.setText(rs.getString(3));
                            email_prov.setText(rs.getString(4));
                            int FK_ciu = rs.getInt(5);
                            fecha_reg_prov.setText(rs.getDate(6).toString());
                            ps = (PreparedStatement) con.prepareStatement(consulta+"CIUDAD WHERE ID="+FK_ciu);
                            rs = ps.executeQuery();
                            rs.next();
                            ciudad_prov.setText(rs.getString(2));
                            ver_panel(19,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFproducto.FK_prov = pk;
                        JFproducto.jt_proveedor.setText(nombre_prov.getText());
                        MENU.setSelectedIndex(3);
                        JFpro.setVisible(true);
                    }
                }
            });
            JTprovi.addMouseListener(new MouseAdapter() { //provincia - 20
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTprovi.getValueAt(JTprovi.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PROVINCIA WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_provi.setText(""+rs.getInt(1));
                            nombre_provi.setText(rs.getString(2));
                            ver_panel(20,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFciudad.jt_provincia.setText(id_provi.getText());
                        JFciu.setVisible(true);
                    }
                }
            });
            JTpue.addMouseListener(new MouseAdapter() { //puesto - 21
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTpue.getValueAt(JTpue.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"PUESTO WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_pue.setText(""+rs.getInt(1));
                            nombre_pue.setText(rs.getString(2));
                            sueldo_pue.setText("$"+(rs.getDouble(3)));
                            ver_panel(21,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        JFempleado.FK_pue = Integer.parseInt(pk);
                        JFempleado.puesto.setText("" + pk + " - " + nombre_pue.getText());
                        MENU.setSelectedIndex(1);
                        PERSONAS.setSelectedIndex(0);
                        JFemp.setVisible(true);
                    }
                }
            });
            JTsuc.addMouseListener(new MouseAdapter() { //sucursal - 22
                @Override
                public void mousePressed(MouseEvent Mouse_evt) {
                    if (Mouse_evt.getClickCount() == 1) {
                        try {
                            pk = JTsuc.getValueAt(JTsuc.getSelectedRow(), 0).toString();
                            ps = (PreparedStatement) con.prepareStatement(consulta+"SUCURSAL WHERE ID="+pk);
                            rs = ps.executeQuery();
                            rs.next();
                            id_suc.setText(""+rs.getInt(1));
                            nombre_suc.setText(rs.getString(2));
                            ps = (PreparedStatement) con.prepareStatement(consulta+"CIUDAD WHERE ID="+rs.getInt(3));
                            rs = ps.executeQuery();
                            rs.next();
                            ciudad_suc.setText(rs.getString(2));
                            ver_panel(22,true);
                        } catch (SQLException ex) {
                        }
                    }
                    if (Mouse_evt.getClickCount() == 2) {
                        FK_suc = Integer.parseInt(pk);
                        f_suc.setText(nombre_suc.getText());
                        f_suc.setBackground(Color.green);
                        MENU.setSelectedIndex(0);
                        INICIO.setSelectedIndex(0);
                    }
                }
            });
        }
    }

    public void hora() {
        Calendar calendario = new GregorianCalendar();
        Date hora_actual = new Date();
        calendario.setTime(hora_actual);
        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }

    public void run() {
        Thread current = Thread.currentThread();
        while (current == hilo) {
            hora();
            fecha_hora.setText("Ecuador, " + fechas.transformar(fechas.obtener()) + " - " + hora + ":" + minutos + ":" + segundos);
        }
    }
    
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
            java.util.logging.Logger.getLogger(PRINCIPAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PRINCIPAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PRINCIPAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PRINCIPAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(JFlogin.class.getName()).log(Level.SEVERE, null, ex);
                }
                new PRINCIPAL().setVisible(true);
            }
        });
      
    }
     public void ver_panel(int num, boolean visible) {
        switch (num) {
            case 1: JPcat.setVisible(visible); break;
            case 2: JPciu.setVisible(visible);break;
            case 3: JPcli.setVisible(visible); break;
            case 4: JPdep.setVisible(visible); break;
            case 5: JPdes.setVisible(visible); break;
            case 8: JPemp.setVisible(visible); break;
            case 11: JPfp.setVisible(visible); break;
            case 12: JPgen.setVisible(visible); break;
            case 13: JPiva.setVisible(visible); break;
            case 14: JPmar.setVisible(visible); break;
            case 15: JPpe.setVisible(visible); break;
            case 18: JPpro.setVisible(visible); break;
            case 19: JPprov.setVisible(visible); break;
            case 20: JPprovi.setVisible(visible); break;
            case 21: JPpue.setVisible(visible); break;
            case 22: JPsuc.setVisible(visible); break;
         }
    }

    public void ocultar_paneles() {
        JPcat.setVisible(false);
        JPciu.setVisible(false);
        JPcli.setVisible(false);
        JPdep.setVisible(false);
        JPdes.setVisible(false);
        JPemp.setVisible(false);
        JPfp.setVisible(false);
        JPgen.setVisible(false);
        JPiva.setVisible(false);
        JPmar.setVisible(false);
        JPpe.setVisible(false);
        JPpro.setVisible(false);
        JPprov.setVisible(false);
        JPprovi.setVisible(false);
        JPpue.setVisible(false);
        JPsuc.setVisible(false);
    }

    public void calcular_total() {
        IVA = Integer.parseInt(f_iva.getSelectedItem().toString().replace("%", ""));
        total = 0;
        total = subtotal+((subtotal * IVA)/100);
        total = (Math.rint(total * 100.0) / 100.0); //lo deja con 2 decimales
        f_total.setText("$"+total);
        if (f_iva.getSelectedIndex()!=0) {
            f_iva.setBackground(Color.green);
        } else{
            f_iva.setBackground(Color.red);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField Bcat;
    public static javax.swing.JTextField Bciu;
    public static javax.swing.JTextField Bcli;
    public static javax.swing.JTextField Bdep;
    public static javax.swing.JTextField Bdes;
    public static javax.swing.JTextField Bdet;
    public static javax.swing.JTextField Bdpp;
    public static javax.swing.JTextField Bemp;
    public static javax.swing.JTextField Benc;
    public static javax.swing.JTextField Bepp;
    public static javax.swing.JTextField Bfp;
    public static javax.swing.JTextField Bgen;
    public static javax.swing.JTextField Biva;
    public static javax.swing.JTextField Bmar;
    public static javax.swing.JTextField Bpag;
    public static javax.swing.JTextField Bpe;
    public static javax.swing.JTextField Bper;
    public static javax.swing.JTextField Bpro;
    public static javax.swing.JTextField Bprov;
    public static javax.swing.JTextField Bprovi;
    public static javax.swing.JTextField Bpue;
    public static javax.swing.JTextField Bsuc;
    public static javax.swing.JTabbedPane INICIO;
    private javax.swing.JButton JBcrear_factura;
    private javax.swing.JButton JBlimpiar_factura;
    public transient javax.swing.JButton JBseleccionar_pro;
    private javax.swing.JComboBox<String> JCcat;
    private javax.swing.JComboBox<String> JCciu;
    private javax.swing.JComboBox<String> JCcli;
    private javax.swing.JComboBox<String> JCdep;
    private javax.swing.JComboBox<String> JCdes;
    private javax.swing.JComboBox<String> JCdet;
    private javax.swing.JComboBox<String> JCdpp;
    private javax.swing.JComboBox<String> JCemp;
    private javax.swing.JComboBox<String> JCenc;
    private javax.swing.JComboBox<String> JCepp;
    private javax.swing.JComboBox<String> JCfp;
    private javax.swing.JComboBox<String> JCgen;
    private javax.swing.JComboBox<String> JCiva;
    private javax.swing.JComboBox<String> JCmar;
    private javax.swing.JComboBox<String> JCpag;
    private javax.swing.JComboBox<String> JCpe;
    private javax.swing.JComboBox<String> JCper;
    private javax.swing.JComboBox<String> JCpro;
    private javax.swing.JComboBox<String> JCprov;
    private javax.swing.JComboBox<String> JCprovi;
    private javax.swing.JComboBox<String> JCpue;
    private javax.swing.JComboBox<String> JCsuc;
    private javax.swing.JPanel JPcat;
    private javax.swing.JPanel JPciu;
    private javax.swing.JPanel JPciu_provi;
    private javax.swing.JPanel JPcli;
    private javax.swing.JPanel JPcli_emp;
    private javax.swing.JPanel JPdep;
    private javax.swing.JPanel JPdes;
    private javax.swing.JPanel JPemp;
    private javax.swing.JPanel JPfacturar;
    private javax.swing.JPanel JPfp;
    private javax.swing.JPanel JPgen;
    private javax.swing.JPanel JPgen_des;
    private javax.swing.JPanel JPiva;
    private javax.swing.JPanel JPiva_fp;
    private javax.swing.JPanel JPmar;
    private javax.swing.JPanel JPmar_cat;
    private javax.swing.JPanel JPpagos;
    private javax.swing.JPanel JPpe;
    private javax.swing.JPanel JPpp;
    private javax.swing.JPanel JPpro;
    private javax.swing.JPanel JPproductos;
    private javax.swing.JPanel JPprov;
    private javax.swing.JPanel JPprov_suc;
    private javax.swing.JPanel JPprovi;
    private javax.swing.JPanel JPpue;
    private javax.swing.JPanel JPpue_dep;
    private javax.swing.JPanel JPsuc;
    private javax.swing.JPanel JPventas;
    private javax.swing.JScrollPane JScli_emp;
    private javax.swing.JScrollPane JSfacturar;
    private javax.swing.JScrollPane JSpagos;
    private javax.swing.JScrollPane JSventas;
    private javax.swing.JTable JTcat;
    private javax.swing.JTable JTciu;
    private javax.swing.JTable JTcli;
    private javax.swing.JTable JTdep;
    private javax.swing.JTable JTdes;
    private javax.swing.JTable JTdet;
    private javax.swing.JTable JTdet_pp;
    public transient javax.swing.JTable JTdetalle;
    private javax.swing.JTable JTemp;
    private javax.swing.JTable JTenc;
    private javax.swing.JTable JTenc_pp;
    private javax.swing.JTable JTfp;
    private javax.swing.JTable JTgen;
    private javax.swing.JTable JTiva;
    private javax.swing.JTable JTmar;
    private javax.swing.JTable JTpag;
    private javax.swing.JTable JTpe;
    private javax.swing.JTable JTper;
    private javax.swing.JTable JTpro;
    private javax.swing.JTable JTprov;
    private javax.swing.JTable JTprovi;
    private javax.swing.JTable JTpue;
    private javax.swing.JTable JTsuc;
    private javax.swing.JLabel Lcat;
    private javax.swing.JLabel Lciu;
    private javax.swing.JLabel Lcli;
    private javax.swing.JLabel Ldep;
    private javax.swing.JLabel Ldes;
    private javax.swing.JLabel Ldet;
    private javax.swing.JLabel Ldpp;
    private javax.swing.JLabel Lemp;
    private javax.swing.JLabel Lenc;
    private javax.swing.JLabel Lepp;
    private javax.swing.JLabel Lfp;
    private javax.swing.JLabel Lgen;
    private javax.swing.JLabel Liva;
    private javax.swing.JLabel Lmar;
    private javax.swing.JLabel Lpag;
    private javax.swing.JLabel Lpe;
    private javax.swing.JLabel Lper;
    private javax.swing.JLabel Lpro;
    private javax.swing.JLabel Lprov;
    private javax.swing.JLabel Lprovi;
    private javax.swing.JLabel Lpue;
    private javax.swing.JLabel Lsuc;
    public static javax.swing.JTabbedPane MENU;
    public static javax.swing.JTabbedPane PERSONAS;
    private javax.swing.JLabel apellido_cli;
    private javax.swing.JLabel apellido_emp;
    private javax.swing.JLabel categoria_pro;
    private javax.swing.JLabel cedula_cli;
    private javax.swing.JLabel cedula_emp;
    private javax.swing.JLabel celular_cli;
    private javax.swing.JLabel celular_emp;
    private javax.swing.JLabel celular_prov;
    private javax.swing.JLabel cerrar_sesion;
    private javax.swing.JLabel ciudad_cli;
    private javax.swing.JLabel ciudad_emp;
    private javax.swing.JLabel ciudad_prov;
    private javax.swing.JLabel ciudad_suc;
    private javax.swing.JLabel codigo_pp;
    private javax.swing.JLabel codigo_pro;
    private javax.swing.JLabel departamento_emp;
    public static javax.swing.JTextArea descripcion_cat;
    public static javax.swing.JTextArea descripcion_dep;
    private javax.swing.JLabel descuento_cli;
    private javax.swing.JLabel destinatario_pe;
    private javax.swing.JLabel direccion_cli;
    private javax.swing.JLabel direccion_emp;
    private javax.swing.JButton elim_cat;
    private javax.swing.JButton elim_ciu;
    private javax.swing.JButton elim_cli;
    private javax.swing.JButton elim_dep;
    private javax.swing.JButton elim_des;
    private javax.swing.JButton elim_emp;
    private javax.swing.JButton elim_fp;
    private javax.swing.JButton elim_gen;
    private javax.swing.JButton elim_iva;
    private javax.swing.JButton elim_mar;
    private javax.swing.JButton elim_pro;
    private javax.swing.JButton elim_prov;
    private javax.swing.JButton elim_provi;
    private javax.swing.JButton elim_pue;
    private javax.swing.JButton elim_suc;
    private javax.swing.JLabel email_cli;
    private javax.swing.JLabel email_emp;
    private javax.swing.JLabel email_prov;
    private javax.swing.JLabel empleado_pp;
    private javax.swing.JLabel empresa;
    private javax.swing.JTextField f_ced_cli;
    private javax.swing.JTextField f_codigo;
    private javax.swing.JTextField f_des;
    private javax.swing.JLabel f_descuento;
    private javax.swing.JTextField f_dir;
    private javax.swing.JTextField f_email;
    private javax.swing.JTextField f_emp;
    private javax.swing.JTextField f_fecha;
    private javax.swing.JComboBox<String> f_forma_pago;
    private javax.swing.JTextField f_id_cli;
    public static javax.swing.JComboBox<String> f_iva;
    private javax.swing.JTextField f_nom_ape;
    private javax.swing.JLabel f_num_det;
    private javax.swing.JLabel f_subtotal;
    private javax.swing.JTextField f_suc;
    private javax.swing.JTextField f_tel;
    private javax.swing.JLabel f_total;
    private javax.swing.JLabel fecha_hora;
    private javax.swing.JLabel fecha_nac_cli;
    private javax.swing.JLabel fecha_nac_emp;
    private javax.swing.JLabel fecha_pag_pe;
    private javax.swing.JLabel fecha_pag_pp;
    private javax.swing.JLabel fecha_reg_cli;
    private javax.swing.JLabel fecha_reg_emp;
    private javax.swing.JLabel fecha_reg_pro;
    private javax.swing.JLabel fecha_reg_prov;
    private javax.swing.JLabel fondo;
    private javax.swing.JLabel id_cat;
    private javax.swing.JLabel id_ciu;
    private javax.swing.JLabel id_cli;
    private javax.swing.JLabel id_dep;
    private javax.swing.JLabel id_des;
    private javax.swing.JLabel id_emp;
    private javax.swing.JLabel id_fp;
    private javax.swing.JLabel id_gen;
    private javax.swing.JLabel id_iva;
    private javax.swing.JLabel id_mar;
    private javax.swing.JLabel id_provi;
    private javax.swing.JLabel id_provi_ciu;
    private javax.swing.JLabel id_pue;
    private javax.swing.JLabel id_suc;
    private javax.swing.JLabel imagen_pro;
    private javax.swing.JLabel impuesto_iva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel jl_num_det1;
    private javax.swing.JLabel jl_num_det2;
    private javax.swing.JLabel jl_num_det3;
    private javax.swing.JLabel jl_titulo10;
    private javax.swing.JLabel jl_titulo17;
    private javax.swing.JLabel jl_titulo18;
    private javax.swing.JLabel jl_titulo19;
    private javax.swing.JLabel jl_titulo20;
    private javax.swing.JLabel jl_titulo21;
    private javax.swing.JLabel jl_titulo22;
    private javax.swing.JLabel jl_titulo23;
    private javax.swing.JLabel jl_titulo24;
    private javax.swing.JLabel jl_titulo25;
    private javax.swing.JLabel jl_titulo26;
    private javax.swing.JLabel jl_titulo27;
    private javax.swing.JLabel jl_titulo28;
    private javax.swing.JLabel jl_titulo29;
    private javax.swing.JLabel jl_titulo30;
    private javax.swing.JLabel jl_titulo31;
    private javax.swing.JLabel jl_titulo32;
    private javax.swing.JLabel jl_titulo33;
    private javax.swing.JLabel jl_titulo34;
    private javax.swing.JLabel jl_titulo35;
    private javax.swing.JLabel jl_titulo6;
    private javax.swing.JLabel jl_titulo8;
    private javax.swing.JLabel jl_total1;
    private javax.swing.JScrollPane jsTabla_ciu1;
    private javax.swing.JScrollPane jsTabla_ciu10;
    private javax.swing.JScrollPane jsTabla_ciu11;
    private javax.swing.JScrollPane jsTabla_ciu12;
    private javax.swing.JScrollPane jsTabla_ciu13;
    private javax.swing.JScrollPane jsTabla_ciu14;
    private javax.swing.JScrollPane jsTabla_ciu15;
    private javax.swing.JScrollPane jsTabla_ciu16;
    private javax.swing.JScrollPane jsTabla_ciu17;
    private javax.swing.JScrollPane jsTabla_ciu18;
    private javax.swing.JScrollPane jsTabla_ciu19;
    private javax.swing.JScrollPane jsTabla_ciu2;
    private javax.swing.JScrollPane jsTabla_ciu20;
    private javax.swing.JScrollPane jsTabla_ciu21;
    private javax.swing.JScrollPane jsTabla_ciu22;
    private javax.swing.JScrollPane jsTabla_ciu23;
    private javax.swing.JScrollPane jsTabla_ciu3;
    private javax.swing.JScrollPane jsTabla_ciu4;
    private javax.swing.JScrollPane jsTabla_ciu5;
    private javax.swing.JScrollPane jsTabla_ciu6;
    private javax.swing.JScrollPane jsTabla_ciu7;
    private javax.swing.JScrollPane jsTabla_ciu8;
    private javax.swing.JScrollPane jsTabla_ciu9;
    private javax.swing.JLabel l_des;
    private javax.swing.JLabel marca_pro;
    private javax.swing.JLabel maximos_pro;
    private javax.swing.JLabel minimos_pro;
    private javax.swing.JButton mod_cat;
    private javax.swing.JButton mod_ciu;
    private javax.swing.JButton mod_cli;
    private javax.swing.JButton mod_dep;
    private javax.swing.JButton mod_des;
    private javax.swing.JButton mod_emp;
    private javax.swing.JButton mod_fp;
    private javax.swing.JButton mod_gen;
    private javax.swing.JButton mod_mar;
    private javax.swing.JButton mod_pro;
    private javax.swing.JButton mod_prov;
    private javax.swing.JButton mod_provi;
    private javax.swing.JButton mod_pue;
    private javax.swing.JButton mod_suc;
    private javax.swing.JLabel nombre_cat;
    private javax.swing.JLabel nombre_ciu;
    private javax.swing.JLabel nombre_cli;
    private javax.swing.JLabel nombre_dep;
    private javax.swing.JLabel nombre_des;
    private javax.swing.JLabel nombre_emp;
    private javax.swing.JLabel nombre_fp;
    private javax.swing.JLabel nombre_mar;
    private javax.swing.JLabel nombre_pro;
    private javax.swing.JLabel nombre_prov;
    private javax.swing.JLabel nombre_provi;
    private javax.swing.JLabel nombre_pue;
    private javax.swing.JLabel nombre_suc;
    private javax.swing.JLabel numero_pe;
    private javax.swing.JLabel porcentaje_des;
    private javax.swing.JLabel precio_pro;
    private javax.swing.JLabel proveedor_pp;
    private javax.swing.JLabel proveedor_pro;
    private javax.swing.JLabel puesto_emp;
    private javax.swing.JButton reg_cat;
    private javax.swing.JButton reg_ciu;
    private javax.swing.JButton reg_cli;
    private javax.swing.JButton reg_dep;
    private javax.swing.JButton reg_des;
    private javax.swing.JButton reg_emp;
    private javax.swing.JButton reg_fp;
    private javax.swing.JButton reg_gen;
    private javax.swing.JButton reg_iva;
    private javax.swing.JButton reg_mar;
    private javax.swing.JButton reg_pe;
    private javax.swing.JButton reg_pp;
    private javax.swing.JButton reg_pro;
    private javax.swing.JButton reg_prov;
    private javax.swing.JButton reg_provi;
    private javax.swing.JButton reg_pue;
    private javax.swing.JButton reg_suc;
    private javax.swing.JLabel remitente_pe;
    private javax.swing.JLabel res_cat;
    private javax.swing.JLabel res_ciu;
    private javax.swing.JLabel res_cli;
    private javax.swing.JLabel res_dep;
    private javax.swing.JLabel res_des;
    private javax.swing.JLabel res_det;
    private javax.swing.JLabel res_det_pp;
    private javax.swing.JLabel res_emp;
    private javax.swing.JLabel res_enc;
    private javax.swing.JLabel res_enc_pp;
    private javax.swing.JLabel res_fp;
    private javax.swing.JLabel res_gen;
    private javax.swing.JLabel res_iva;
    private javax.swing.JLabel res_mar;
    private javax.swing.JLabel res_pag;
    private javax.swing.JLabel res_pe;
    private javax.swing.JLabel res_per;
    private javax.swing.JLabel res_pro;
    private javax.swing.JLabel res_prov;
    private javax.swing.JLabel res_provi;
    private javax.swing.JLabel res_pue;
    private javax.swing.JLabel res_suc;
    private javax.swing.JLabel ruc_prov;
    private javax.swing.JLabel sexo_cli;
    private javax.swing.JLabel sexo_emp;
    private javax.swing.JLabel sexo_gen;
    private javax.swing.JLabel stock_pro;
    private javax.swing.JLabel sueldo_emp;
    private javax.swing.JLabel sueldo_pue;
    private javax.swing.JLabel total_pe;
    private javax.swing.JLabel total_pp;
    public static javax.swing.JLabel usuario;
    // End of variables declaration//GEN-END:variables
}
