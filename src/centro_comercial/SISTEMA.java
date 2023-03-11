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
import otros.ImagenTabla;

public class SISTEMA extends javax.swing.JFrame implements Runnable {
    //variables para consultas SQL:
    public static Connection con = null;
    public static ResultSet rs;
    public static PreparedStatement ps;
    public static String consulta = "";
    //variables que guardan el número de registros:
    public static int cat, ciu, cli, dep, des, det, emp, enc, fp, gen, iva, mar, pe, p_f, per, pro, prov, provi, pue, suc;

    //otras variables útiles:
    public static boolean actualizado = false;
    
    DefaultTableModel tabla = null, tabla_detalle = null;
    TableRowSorter sorter;
    public static String modo_prov = "producto";
    
    ArrayList<String> detalles = new ArrayList<>();
    JButton boton1 = new JButton();

    String hora, minutos, segundos;
    Thread hilo;
    
    //variables para factura:
    public static int descuento = 0 , num_det = 0;
    public static double subtotal = 0, total_descuento = 0, total = 0; 
    public static int xcolum,xrow;
    
    //instancias de los frames:
//    public static JFcategoria JFcat = new JFcategoria();//fr1
//    public static JFciudad JFciu = new JFciudad();//fr2
    public static JFcliente JFcli = new JFcliente(); //fr3
//    public static JFdescuento JFdes = new JFdescuento();//fr4
//    public static JFempleado JFemp = new JFempleado();//fr6
//    public static JFpagos JFpag = new JFpagos();//8
//    public static JFproducto JFpro = new JFproducto();//fr9
//    public static JFproveedor JFprov = new JFproveedor(); //fr10

    public SISTEMA() {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
    }

    public final void iniciar() {
        //JPcli.setVisible(false);
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
        lim_cat.setVisible(false);
        lim_ciu.setVisible(false);
        lim_cli.setVisible(false);
        lim_des.setVisible(false);
        lim_det.setVisible(false);
        lim_emp.setVisible(false);
        lim_enc.setVisible(false);
        //lim_gas.setVisible(false);
        lim_pro.setVisible(false);
        lim_prov.setVisible(false);

        JBseleccionar_pro.setEnabled(false);
        JBcrear_factura.setEnabled(false);

    }

    //método para cargar los datos en las tablas:
    public void visualizar() {
        con = conexion.conectar();
        if (con != null) {
            try {
                consulta = "SELECT * FROM ";
                for (int i = 1; i <= 20; i++) {
                    switch (i) {
                        case 1://categoria
                            String[] c_cat = {"ID", "NOMBRE", "DESCRIPCIÓN"};
                            tabla = new DefaultTableModel(null, c_cat);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "categoria");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getString(3)});
                            }
                            JTcategorias.setModel(tabla);
                            cat = tabla.getRowCount();
                            res_cat.setText("Resultados: "+cat+" de "+cat);
                            break;
                        case 2://ciudad
                            String[] c_ciu = {"ID", "NOMBRE", "ID_PROVI"};
                            tabla = new DefaultTableModel(null, c_ciu);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "ciudad");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            JTciudades.setModel(tabla);
                            ciu = tabla.getRowCount();
                            res_ciu.setText("Resultados: "+ciu+" de "+ciu);
                            break;
                        case 3://cliente
                            String[] c_cli = {"ID", "CÉDULA_PER", "ID_DES"};
                            tabla = new DefaultTableModel(null, c_cli);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "cliente");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            JTcli.setModel(tabla);
                            cli = tabla.getRowCount();
                            res_cli.setText("Resultados: "+cli+" de "+cli);
                            break;
                        case 4://departamento
                            String[] c_dep = {"ID", "NOMBRE", "DESCRIPCIÓN"};
                            tabla = new DefaultTableModel(null, c_dep);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "departamento");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getString(3)});
                            }
                            //JTdepartamentos.setModel(tabla);
                            dep = tabla.getRowCount();
                            //res_dep.setText("Resultados: "+dep+" de "+dep);
                            break;
                        case 5://descuento
                            String[] c_des = {"ID", "NOMBRE", "PORCENTAJE"};
                            tabla = new DefaultTableModel(null, c_des);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "descuento");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            JTdescuentos.setModel(tabla);
                            des = tabla.getRowCount();
                            res_des.setText("Resultados: "+des+" de "+des);
                            break;
                        case 6://detalle_fac
                            String[] c_det = {"CÓDIGO", "CÓDIGO_PRO", "CANTIDAD", "SUBTOTAL", "CÓDIGO_ENC"};
                            tabla = new DefaultTableModel(null, c_det);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "detalle_fac JOIN encabezado_fac WHERE encabezado_fac.ESTADO = 'ACTIVO'");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2),rs.getInt(3),rs.getDouble(4),rs.getInt(5)});
                            }
                            JTdet_fac.setModel(tabla);
                            det = tabla.getRowCount();
                            res_det.setText("Resultados: "+det+" de "+det);
                            break;
                        case 7://empleado
                            String[] c_emp = {"ID", "CÉDULA_PER", "ID_DEP","ID_PUE"};
                            tabla = new DefaultTableModel(null, c_emp);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "EMPLEADO");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(4),rs.getInt(5)});
                            }
                            JTemp.setModel(tabla);
                            emp = tabla.getRowCount();
                            res_emp.setText("Resultados: "+emp+" de "+emp);
                            break;
                        case 8://encabezado_fac
                            String[] c_enc = {"CODIGO", "ID_SUC", "ID_EMP", "ID_CLI", "FECHA_REG", "ESTADO"};
                            tabla = new DefaultTableModel(null, c_enc);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "encabezado_fac WHERE ESTADO = 'ACTIVO'");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2),rs.getInt(3),rs.getInt(4),fechas.transformar(rs.getDate(5)), rs.getString(6)});
                            }
                            JTenc_fac.setModel(tabla);
                            enc = tabla.getRowCount();
                            res_enc.setText("Resultados: "+enc+" de "+enc);
                            break;
                        case 9://forma_pago
                            String[] c_fp = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_fp);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "FORMA_PAGO");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            //JTfp.setModel(tabla);
                            fp = tabla.getRowCount();
                            //res_fp.setText("Resultados: "+fp+" de "+fp);
                            break;
                        case 10://genero
                            String[] c_gen = {"ID", "SEXO"};
                            tabla = new DefaultTableModel(null, c_gen);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "GENERO");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            JTgeneros.setModel(tabla);
                            gen = tabla.getRowCount();
                            res_gen.setText("Resultados: "+gen+" de "+gen);
                            break;
                        case 11://iva
                            String[] c_iva = {"ID", "IMPUESTO"};
                            tabla = new DefaultTableModel(null, c_iva);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "iva");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2)});
                            }
                            //JTiva.setModel(tabla);
                            iva = tabla.getRowCount();
                            //res_iva.setText("Resultados: "+iva+" de "+iva);
                            break;
                        case 12://marca
                            String[] c_mar = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_mar);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "marca");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            //JTmarcas.setModel(tabla);
                            mar = tabla.getRowCount();
                            //res_mar.setText("Resultados: "+mar+" de "+mar);
                            break;
                        case 13://pago_empleado
                            String[] c_pe = {"NUMERO", "ID_REMITENTE", "ID_DESTINATARIO", "TOTAL", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_pe);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_empleado");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), fechas.transformar(rs.getDate(5))});
                            }
                            //JTpe.setModel(tabla);
                            pe = tabla.getRowCount();
                            //res_pe.setText("Resultados: "+pe+" de "+pe);
                            break;
                        case 14://pago_fac
                            String[] c_pf = {"NUMERO", "TOTAL_SIN_IVA", "ID_FOR", "ID_IVA", "TOTAL_MAS_IVA", "CODIGO_ENC"};
                            tabla = new DefaultTableModel(null, c_pf);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_fac");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getDouble(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5), rs.getInt(6)});
                            }
                            //JTpf.setModel(tabla);
                            pe = tabla.getRowCount();
                            //res_pf.setText("Resultados: "+pf+" de "+pf);
                            break;
                        case 15://persona
                            String[] c_per = {"CEDULA", "NOMBRE", "APELLIDO", "FECHA_NAC", "ID_SEXO", "CELULAR", "EMAIL", "DIRECCION", "ID_CIUDAD", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_per);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "persona");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), fechas.transformar(rs.getDate(4)), 
                                    rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9),fechas.transformar(rs.getDate(10))});
                            }
                            JTper.setModel(tabla);
                            per = tabla.getRowCount();
                            res_per.setText("Resultados: "+per+" de "+per);
                            break;
                        case 16://producto
                            JTproductos.setDefaultRenderer(Object.class, new ImagenTabla());
                            String[] c_pro = {"CODIGO", "NOMBRE", "URL_IMG", "ID_MAR", "PRECIO", "EXIS_MAX", "EXIS_MIN", "STOK", "ID_CAT", "FECHA_REG", "RUC_PROV"};
                            tabla = new DefaultTableModel(null, c_pro);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "producto");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                JLabel lb = new JLabel();
                                lb.setSize(125, 60);
                                rsscalelabel.RSScaleLabel.setScaleLabel(lb, rs.getString(3));
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), lb, rs.getInt(4),rs.getDouble(5),rs.getInt(6),
                                    rs.getInt(7),rs.getInt(8),rs.getInt(9),fechas.transformar(rs.getDate(10)),rs.getInt(11)});
                            }
                            JTproductos.setModel(tabla);
                            pro = tabla.getRowCount();
                            res_pro.setText("Resultados: "+pro+" de "+pro);
                            break;
                        case 17://proveedor
                            String[] c_prov = {"RUC", "NOMBRE_EMPRESA", "CELULAR", "EMAIL", "ID_CIU", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_prov);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "proveedor");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),fechas.transformar(rs.getDate(6))});
                            }
                            JTproveedores.setModel(tabla);
                            prov = tabla.getRowCount();
                            res_prov.setText("Resultados: "+prov+" de "+prov);
                            break;
                        case 18://provincia
                            String[] c_provi = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_provi);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "provincia");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            //JTprovi.setModel(tabla);
                            provi = tabla.getRowCount();
                            //res_provi.setText("Resultados: "+provi+" de "+provi);
                            break;
                        case 19://puesto
                            String[] c_pue = {"ID", "NOMBRE", "SUELDO"};
                            tabla = new DefaultTableModel(null, c_pue);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "puesto");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getDouble(3)});
                            }
                            //JTpue.setModel(tabla);
                            pue = tabla.getRowCount();
                            //res_pue.setText("Resultados: "+pue+" de "+pue);
                            break;
                        case 20://sucursal
                            String[] c_suc = {"ID", "NOMBRE", "ID_CIU"};
                            tabla = new DefaultTableModel(null, c_suc);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "sucursal");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            //JTsuc.setModel(tabla);
                            suc = tabla.getRowCount();
                            //res_suc.setText("Resultados: "+suc+" de "+suc);
                            break;
                    }
                }
                
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    //método para buscar registros de cualquier tabla:
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
//        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea continuar con esta acción?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
//            boolean eliminado = true;
//            try {
//                base.abrir();
//                switch (clase) {
//                    case 1: //eliminar categoria
//                        Categoria cate = new Categoria(jlNombre_cat.getText(), null);
//                        resultado = base.gettear(cate);
//                        Producto prod = new Producto(0, null, 0, 0, jlNombre_cat.getText(), null, null, null);
//                        resultado2 = base.gettear(prod);
//                        if (resultado2.isEmpty()) {
//                            cate = (Categoria) resultado.next();
//                            base.eliminar(cate);
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar la categoría ya que se encuentra asignado a un producto!");
//                            eliminado = false;
//                        }
//
//                        break;
//                    case 2: //eliminar ciudad
//                        Ciudad c = new Ciudad(Integer.parseInt(jlCodigo_ciu.getText()), null, null);
//                        resultado = base.gettear(c);
//                        Proveedor prov = new Proveedor(null, null, jlCodigo_ciu.getText(), null, null, null);
//                        resultado2 = base.gettear(prov);
//                        if (resultado2.isEmpty()) {
//                            c = (Ciudad) resultado.next();
//                            base.eliminar(c);
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar la ciudad ya que se encuentra asignado a un proveedor!");
//                            eliminado = false;
//                        }
//
//                        break;
//                    case 3: //eliminar cliente
//                        Cliente cl = new Cliente(null, jlCedula_cli.getText(), null, null, null, null, null, null, null, null);
//                        resultado = base.gettear(cl);
//                        Encabezado_fac fact = new Encabezado_fac(0, jlCedula_cli.getText(), null, 0, null);
//                        resultado2 = base.gettear(fact);
//                        if (resultado2.isEmpty()) {
//                            cl = (Cliente) resultado.next();
//                            base.eliminar(cl);
//                            reiniciar_factura(); //por precaución
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar el cliente ya que se encuentra asignado a una factura!");
//                            eliminado = false;
//                        }        
//                        break;
//                    case 4: //eliminar descuento
//                        Descuento d = new Descuento(jlNombre_des.getText(), 0);
//                        resultado = base.gettear(d);
//                        Cliente cli2 = new Cliente(jlNombre_des.getText(), null, null, null, null, null, null, null, null, null);
//                        resultado2 = base.gettear(cli2);
//                        if (resultado2.isEmpty()) {
//                            d = (Descuento) resultado.next();
//                            base.eliminar(d);
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar el descuento ya que se encuentra asignado a un cliente!");
//                            eliminado = false;
//                        }
//                        break;
//                    case 6: //eliminar empleado
//                        Empleado e = new Empleado(0, jlCedula_emp.getText(), null, null, null, null, null, null, null, null);
//                        resultado = base.gettear(e);
//                        Pago_prov pp = new Pago_prov(0, 0, jlCedula_emp.getText(), null, null, null);
//                        resultado2 = base.gettear(pp);
//                        if (resultado2.isEmpty()) {
//                            e = (Empleado) resultado.next();
//                            base.eliminar(e);
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar el empleado ya que se encuentra asignado a un gasto!");
//                            eliminado = false;
//                        }
//                        break;
//                    case 7: //ocultar factura
//                        Encabezado_fac en = new Encabezado_fac(Integer.parseInt(VF_CODIGO.getText()), null, null, 0, null);
//                        resultado = base.gettear(en);
//                        en = (Encabezado_fac) resultado.next();
//                        en.setEstado("INACTIVO");
//                        base.settear(en);
//                        break;
//                    case 8: //eliminar pago a proveedor
//                        Pago_prov p_p = new Pago_prov(Integer.parseInt(jlCodigo_pag.getText()), 0, null, null, null, null);
//                        resultado = base.gettear(p_p);
//                        p_p = (Pago_prov) resultado.next();
//                        base.eliminar(p_p);
//                        break;
//                    case 9: //eliminar producto
//                        Producto pr = new Producto(Integer.parseInt(jlCodigo_pro.getText()), null, 0, 0, null, null, null, null);
//                        resultado = base.gettear(pr);
//                        Detalle_fac enca = new Detalle_fac(0, Integer.parseInt(jlCodigo_pro.getText()), 0, 0,0);
//                        resultado2 = base.gettear(enca);
//                        if (resultado2.isEmpty()) {
//                            pr = (Producto) resultado.next();
//                            base.eliminar(pr);
//                            reiniciar_factura(); //por precaución
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Imposible eliminar el producto ya que se encuentra asignado a un detalle!");
//                            eliminado = false;
//                        }
//
//                        break;
//                    case 10: //eliminar proveedor
//                        Proveedor p = new Proveedor(jlRUC.getText(), null, null, null, null, null);
//                        resultado = base.gettear(p);
//                        Producto q = new Producto(0, null, 0, 0, null, null, jlRUC.getText(), null);
//                        resultado2 = base.gettear(q);
//                        Pago_prov r = new Pago_prov(0, 0, null, jlRUC.getText(), null, null);
//                        resultado3 = base.gettear(r);
//
//                        if (resultado2.isEmpty() && resultado3.isEmpty()) {
//                            p = (Proveedor) resultado.next();
//                            base.eliminar(p);
//                            limpiar(clase);
//                        } else {
//                            if (!resultado2.isEmpty()) {
//                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el proveedor ya que se encuentra asignado a un producto!");
//                            } 
//                            if (!resultado3.isEmpty()) {
//                                JOptionPane.showMessageDialog(null, "¡Imposible eliminar el proveedor ya que se encuentra asignado a un pago!");
//                            }
//                            eliminado = false;
//                        }
//                        break;
//
//                }
//            } catch (Exception e) {
//                eliminado = false;
//                getToolkit().beep();
//                JOptionPane.showMessageDialog(rootPane, "¡Registro no existente!", null, JOptionPane.ERROR_MESSAGE);
//            } finally {
//                base.cerrar();
//                if (eliminado) {
//                    JOptionPane.showMessageDialog(null, "¡Eliminado correctamente!");
//                }
//                limpiar(clase);
//                visualizar();
//            }
//        }
    }
    
    //método para registrar una factura (encabezado y detalles):
    public void generar_factura() {
//        base.abrir();
//        try {
//            //registra el encabezado de la factura
//            Encabezado_fac enc_fac = new Encabezado_fac(Codigos.obtener_codigo("Encabezado_fac"),enc_cedula.getText(),fechas.obtener_fecha(),total,"ACTIVO");
//            base.settear(enc_fac);
//            enc_codigo.setText("" + enc_fac.getCodigo());
//
//            for (int i = 0; i < num_det; i++) {
//                int xcodigo_pro = Integer.parseInt(JTdetalle.getValueAt(i, 0).toString());
//                int xcantidad = Integer.parseInt(JTdetalle.getValueAt(i, 2).toString());
//                double xsubtotal = Double.parseDouble(JTdetalle.getValueAt(i, 6).toString());
//
//                Detalle_fac det_fac = new Detalle_fac(Codigos.obtener_codigo("Detalle_fac"), xcodigo_pro, xcantidad, xsubtotal, enc_fac.getCodigo());
//                base.settear(det_fac);
//                
//                //actualiza las existencias del producto vendido
//                Producto p = new Producto(xcodigo_pro,null,0,0,null,null,null,null);
//                resultado = base.gettear(p);
//                p = (Producto)resultado.next();
//                p.setExistencias(p.getExistencias()-xcantidad);
//                base.settear(p);
//            }
//            JOptionPane.showMessageDialog(null, "¡Registrado correctamente!");
//            
//        } catch (Exception a) {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "¡Error al registrar!");
//        }
//        base.cerrar();
    }
    
    //limpia los campos de texto y los reinicia valores:
    public void reiniciar_factura() {
        for (int i = num_det; i > 0; i--) {
            tabla_detalle.removeRow(i - 1);
        }
        num_det = 0;
        total = 0;
        total_descuento = 0;
        detalles.clear();
        subtotal = 0;
        
        jl_total.setText("Total: $0");
        jl_num_det.setText("Detalles: 0");

        fec_enc.setText("");
        fec_enc.setBackground(Color.red);
        enc_codigo.setText("Autogenerado");
        enc_cedula.setText("");
        enc_cedula.setBackground(Color.red);
        enc_nombre_apellido.setText("");
        enc_direccion.setText("");
        enc_telefono.setText("");
        enc_correo.setText("");
        
        jlSubtotal.setText("$0");
        jlTotal_descuento.setText("- $0");
        jlTotal.setText("$0");
        
        enc_cedula.setEnabled(false);
        JBseleccionar_pro.setEnabled(false);
        JBcrear_factura.setEnabled(false);
        jScrollPane.getVerticalScrollBar().setValue(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MENU = new javax.swing.JTabbedPane();
        INICIO = new javax.swing.JTabbedPane();
        jScrollPane = new javax.swing.JScrollPane();
        JPfactura = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        enc_codigo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        enc_cedula = new javax.swing.JTextField();
        enc_nombre_apellido = new javax.swing.JTextField();
        enc_direccion = new javax.swing.JTextField();
        enc_telefono = new javax.swing.JTextField();
        enc_correo = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        fec_enc = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jsTabla_ciu1 = new javax.swing.JScrollPane();
        JTdetalle = new javax.swing.JTable();
        JBseleccionar_pro = new javax.swing.JButton();
        jl_num_det = new javax.swing.JLabel();
        jl_total = new javax.swing.JLabel();
        JBcrear_factura1 = new javax.swing.JButton();
        JBcrear_factura = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jl_num_det1 = new javax.swing.JLabel();
        jl_total1 = new javax.swing.JLabel();
        jl_num_det2 = new javax.swing.JLabel();
        jlSubtotal = new javax.swing.JLabel();
        jlTotal_descuento = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPanew = new javax.swing.JScrollPane();
        JPventas = new javax.swing.JPanel();
        jsTabla_ciu2 = new javax.swing.JScrollPane();
        JTenc_fac = new javax.swing.JTable();
        jsTabla_ciu3 = new javax.swing.JScrollPane();
        JTdet_fac = new javax.swing.JTable();
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
        jcBuscar_enc = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        jtBuscar_enc = new javax.swing.JTextField();
        lim_enc = new javax.swing.JLabel();
        res_enc = new javax.swing.JLabel();
        jl_titulo6 = new javax.swing.JLabel();
        jl_titulo8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel45 = new javax.swing.JLabel();
        res_det = new javax.swing.JLabel();
        jcBuscar_det = new javax.swing.JComboBox<>();
        jtBuscar_det = new javax.swing.JTextField();
        lim_det = new javax.swing.JLabel();
        jbEliminar_cat1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        R3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        R3_T2 = new javax.swing.JLabel();
        R3_T1 = new javax.swing.JLabel();
        R3_T3 = new javax.swing.JLabel();
        R3_A1 = new javax.swing.JLabel();
        R3_B1 = new javax.swing.JLabel();
        R3_C1 = new javax.swing.JLabel();
        R3_D1 = new javax.swing.JLabel();
        R3_E1 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        R1 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        R1_A1 = new javax.swing.JLabel();
        R1_B1 = new javax.swing.JLabel();
        R1_C1 = new javax.swing.JLabel();
        R1_A2 = new javax.swing.JLabel();
        R1_B2 = new javax.swing.JLabel();
        R1_C2 = new javax.swing.JLabel();
        R1_A3 = new javax.swing.JLabel();
        R1_B3 = new javax.swing.JLabel();
        R1_C3 = new javax.swing.JLabel();
        R1_A4 = new javax.swing.JLabel();
        R1_B4 = new javax.swing.JLabel();
        R1_C4 = new javax.swing.JLabel();
        R1_C5 = new javax.swing.JLabel();
        R1_B5 = new javax.swing.JLabel();
        R1_A5 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jl_titulo9 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        clientes_con_factura11 = new javax.swing.JLabel();
        clientes_sin_factura11 = new javax.swing.JLabel();
        t_facturas_activas11 = new javax.swing.JLabel();
        clientes_con_factura12 = new javax.swing.JLabel();
        clientes_sin_factura12 = new javax.swing.JLabel();
        t_facturas_activas12 = new javax.swing.JLabel();
        clientes_con_factura13 = new javax.swing.JLabel();
        clientes_sin_factura13 = new javax.swing.JLabel();
        t_facturas_activas13 = new javax.swing.JLabel();
        clientes_con_factura14 = new javax.swing.JLabel();
        clientes_sin_factura14 = new javax.swing.JLabel();
        t_facturas_activas14 = new javax.swing.JLabel();
        t_facturas_activas15 = new javax.swing.JLabel();
        clientes_sin_factura15 = new javax.swing.JLabel();
        clientes_con_factura15 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        R2 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        R2_A1 = new javax.swing.JLabel();
        R2_B1 = new javax.swing.JLabel();
        R2_C1 = new javax.swing.JLabel();
        R2_A2 = new javax.swing.JLabel();
        R2_B2 = new javax.swing.JLabel();
        R2_C2 = new javax.swing.JLabel();
        R2_A3 = new javax.swing.JLabel();
        R2_B3 = new javax.swing.JLabel();
        R2_C3 = new javax.swing.JLabel();
        R2_A4 = new javax.swing.JLabel();
        R2_B4 = new javax.swing.JLabel();
        R2_C4 = new javax.swing.JLabel();
        R2_C5 = new javax.swing.JLabel();
        R2_B5 = new javax.swing.JLabel();
        R2_A5 = new javax.swing.JLabel();
        subir_1 = new javax.swing.JLabel();
        JPpagos = new javax.swing.JPanel();
        res_num_pag = new javax.swing.JLabel();
        jpDatos_pro1 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jlR7 = new javax.swing.JLabel();
        jb_Eliminar_pag = new javax.swing.JButton();
        jbModificar_pag = new javax.swing.JButton();
        jlC5 = new javax.swing.JLabel();
        jlF4 = new javax.swing.JLabel();
        jlCodigo_pag = new javax.swing.JLabel();
        jlPrecio_pag = new javax.swing.JLabel();
        jlReg_pag = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jbRegistrar_pag = new javax.swing.JButton();
        jlProveedor_pag = new javax.swing.JLabel();
        jlE18 = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        jlE19 = new javax.swing.JLabel();
        jlceduempleado = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaDescripcion_pag = new javax.swing.JTextArea();
        jlN7 = new javax.swing.JLabel();
        jlE20 = new javax.swing.JLabel();
        jlempleado_nom = new javax.swing.JLabel();
        jcBuscar_pag = new javax.swing.JComboBox<>();
        jLabel57 = new javax.swing.JLabel();
        jtBuscar_pag = new javax.swing.JTextField();
        lim_pag = new javax.swing.JLabel();
        jsTabla_pro1 = new javax.swing.JScrollPane();
        JTpagos = new javax.swing.JTable();
        jl_titulo15 = new javax.swing.JLabel();
        JSpersonas = new javax.swing.JScrollPane();
        JPpersonas = new javax.swing.JPanel();
        jsTabla_ciu4 = new javax.swing.JScrollPane();
        JTcli = new javax.swing.JTable();
        jcBuscar_enc1 = new javax.swing.JComboBox<>();
        jLabel72 = new javax.swing.JLabel();
        jtBuscar_enc1 = new javax.swing.JTextField();
        lim_cli = new javax.swing.JLabel();
        res_cli = new javax.swing.JLabel();
        jl_titulo17 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jPanel23 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jsTabla_ciu5 = new javax.swing.JScrollPane();
        JTemp = new javax.swing.JTable();
        jl_titulo20 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jcBuscar_enc3 = new javax.swing.JComboBox<>();
        jtBuscar_enc3 = new javax.swing.JTextField();
        lim_enc3 = new javax.swing.JLabel();
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
        eliminar_cli = new javax.swing.JButton();
        registrar_cli = new javax.swing.JButton();
        modificar_cli = new javax.swing.JButton();
        jtBuscar_enc4 = new javax.swing.JTextField();
        jcBuscar_enc4 = new javax.swing.JComboBox<>();
        jLabel93 = new javax.swing.JLabel();
        jl_titulo19 = new javax.swing.JLabel();
        res_emp = new javax.swing.JLabel();
        lim_emp = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
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
        jPanel24 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        eliminar_cli1 = new javax.swing.JButton();
        modificar_cli1 = new javax.swing.JButton();
        registrar_cli1 = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        JPgeneros = new javax.swing.JPanel();
        jl_titulo16 = new javax.swing.JLabel();
        jpDatos_cat2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jlR8 = new javax.swing.JLabel();
        jlN8 = new javax.swing.JLabel();
        jlNombre_cat1 = new javax.swing.JLabel();
        jSeparator22 = new javax.swing.JSeparator();
        jbEnviar_cat1 = new javax.swing.JButton();
        jbEliminar_cat2 = new javax.swing.JButton();
        jbModificar_cat1 = new javax.swing.JButton();
        jbRegistrar_cat1 = new javax.swing.JButton();
        jSeparator23 = new javax.swing.JSeparator();
        jlNombre_cat2 = new javax.swing.JLabel();
        jcBuscar_gen = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jtBuscar_gen = new javax.swing.JTextField();
        lim_cat1 = new javax.swing.JLabel();
        jsTabla_cat1 = new javax.swing.JScrollPane();
        JTgeneros = new javax.swing.JTable();
        res_gen = new javax.swing.JLabel();
        JPcategorias = new javax.swing.JPanel();
        jl_titulo5 = new javax.swing.JLabel();
        jpDatos_cat = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jlR4 = new javax.swing.JLabel();
        jlN4 = new javax.swing.JLabel();
        jlNombre_cat = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaDescripcion_cat = new javax.swing.JTextArea();
        jbEnviar_cat = new javax.swing.JButton();
        jbEliminar_cat = new javax.swing.JButton();
        jbModificar_cat = new javax.swing.JButton();
        jbRegistrar_cat = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        jcBuscar_cat = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        jtBuscar_cat = new javax.swing.JTextField();
        lim_cat = new javax.swing.JLabel();
        jsTabla_cat = new javax.swing.JScrollPane();
        JTcategorias = new javax.swing.JTable();
        res_cat = new javax.swing.JLabel();
        JPciudades = new javax.swing.JPanel();
        res_ciu = new javax.swing.JLabel();
        jcBuscar_ciu = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        jtBuscar_ciu = new javax.swing.JTextField();
        lim_ciu = new javax.swing.JLabel();
        jsTabla_ciu = new javax.swing.JScrollPane();
        JTciudades = new javax.swing.JTable();
        jl_titulo7 = new javax.swing.JLabel();
        jpDatos_cat1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jlR1 = new javax.swing.JLabel();
        jlN1 = new javax.swing.JLabel();
        jlC1 = new javax.swing.JLabel();
        jlCodigo_ciu = new javax.swing.JLabel();
        jlNombre_ciu = new javax.swing.JLabel();
        jlProvincia_ciu = new javax.swing.JLabel();
        jbRegistrar_ciu = new javax.swing.JButton();
        jbEnviar_ciu = new javax.swing.JButton();
        jbEliminar_ciu = new javax.swing.JButton();
        jbModificar_ciu = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        JPdescuentos = new javax.swing.JPanel();
        res_des = new javax.swing.JLabel();
        jpDatos_des = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jlR3 = new javax.swing.JLabel();
        jbEnviar_des = new javax.swing.JButton();
        jbEliminar_des = new javax.swing.JButton();
        jbModificar_des = new javax.swing.JButton();
        jlN3 = new javax.swing.JLabel();
        jlNombre_des = new javax.swing.JLabel();
        jlPorcentaje_des = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jbRegistrar_des = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        jcBuscar_des = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jtBuscar_des = new javax.swing.JTextField();
        lim_des = new javax.swing.JLabel();
        jsTabla_des = new javax.swing.JScrollPane();
        JTdescuentos = new javax.swing.JTable();
        jl_titulo14 = new javax.swing.JLabel();
        JPproductos = new javax.swing.JPanel();
        res_pro = new javax.swing.JLabel();
        jpDatos_pro = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jlR6 = new javax.swing.JLabel();
        jbEnviar_pro = new javax.swing.JButton();
        jb_Eliminar_pro = new javax.swing.JButton();
        jbModificar_pro = new javax.swing.JButton();
        jlN6 = new javax.swing.JLabel();
        jlC4 = new javax.swing.JLabel();
        jlF3 = new javax.swing.JLabel();
        jlCodigo_pro = new javax.swing.JLabel();
        jlNombre_pro = new javax.swing.JLabel();
        jlPrecio_pro = new javax.swing.JLabel();
        jlReg_pro = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jbRegistrar_pro = new javax.swing.JButton();
        jlGen_emp1 = new javax.swing.JLabel();
        jlExistencias_pro = new javax.swing.JLabel();
        jlE15 = new javax.swing.JLabel();
        jlCategoria_pro = new javax.swing.JLabel();
        jlProveedor_pro = new javax.swing.JLabel();
        jlE16 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        jcBuscar_pro = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jtBuscar_pro = new javax.swing.JTextField();
        lim_pro = new javax.swing.JLabel();
        jsTabla_pro = new javax.swing.JScrollPane();
        JTproductos = new javax.swing.JTable();
        jl_titulo12 = new javax.swing.JLabel();
        JPproveedores = new javax.swing.JPanel();
        res_prov = new javax.swing.JLabel();
        jpDatos_prov = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jlR = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jbEnviar_prov = new javax.swing.JButton();
        jb_Eliminar_prov = new javax.swing.JButton();
        jbModificar_prov = new javax.swing.JButton();
        jlN = new javax.swing.JLabel();
        jlC = new javax.swing.JLabel();
        jlT = new javax.swing.JLabel();
        jlE = new javax.swing.JLabel();
        jlF = new javax.swing.JLabel();
        jlRUC = new javax.swing.JLabel();
        jlNombre = new javax.swing.JLabel();
        jlCiudad = new javax.swing.JLabel();
        jlTelefono = new javax.swing.JLabel();
        jlFecha_reg = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jbRegistrar_prov = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jcBuscar_prov = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jtBuscar_prov = new javax.swing.JTextField();
        lim_prov = new javax.swing.JLabel();
        jsTabla_cat6 = new javax.swing.JScrollPane();
        JTproveedores = new javax.swing.JTable();
        jl_titulo13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        sistema_titulo = new javax.swing.JLabel();
        FECHA_HORA = new javax.swing.JLabel();
        USUARIO = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        SALIR = new javax.swing.JLabel();
        FONDO = new javax.swing.JLabel();

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
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MENU.setBackground(new java.awt.Color(255, 255, 255));
        MENU.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        MENU.setMinimumSize(new java.awt.Dimension(980, 600));
        MENU.setPreferredSize(new java.awt.Dimension(980, 600));

        INICIO.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        INICIO.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 14)); // NOI18N
        INICIO.setMinimumSize(new java.awt.Dimension(1075, 600));
        INICIO.setPreferredSize(new java.awt.Dimension(1075, 600));

        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPfactura.setBackground(new java.awt.Color(255, 255, 255));
        JPfactura.setMaximumSize(new java.awt.Dimension(980, 1125));
        JPfactura.setMinimumSize(new java.awt.Dimension(980, 1125));
        JPfactura.setPreferredSize(new java.awt.Dimension(980, 1125));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setBackground(new java.awt.Color(0, 51, 153));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 0, 204));
        jLabel3.setText("Factura");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Fecha de emisión:");

        enc_codigo.setEditable(false);
        enc_codigo.setBackground(new java.awt.Color(255, 255, 255));
        enc_codigo.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        enc_codigo.setText("Autogenerado");
        enc_codigo.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel12.setText("Código de factura:");

        jPanel4.setBackground(new java.awt.Color(0, 51, 153));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Datos del cliente ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel13.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel13.setText("Identificación:");

        enc_cedula.setEditable(false);
        enc_cedula.setBackground(new java.awt.Color(255, 0, 0));
        enc_cedula.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_cedula.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        enc_cedula.setEnabled(false);
        enc_cedula.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enc_cedulaMouseClicked(evt);
            }
        });
        enc_cedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enc_cedulaActionPerformed(evt);
            }
        });

        enc_nombre_apellido.setEditable(false);
        enc_nombre_apellido.setBackground(new java.awt.Color(255, 255, 255));
        enc_nombre_apellido.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_nombre_apellido.setText(" ");

        enc_direccion.setEditable(false);
        enc_direccion.setBackground(new java.awt.Color(255, 255, 255));
        enc_direccion.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_direccion.setText(" ");

        enc_telefono.setEditable(false);
        enc_telefono.setBackground(new java.awt.Color(255, 255, 255));
        enc_telefono.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_telefono.setText(" ");

        enc_correo.setEditable(false);
        enc_correo.setBackground(new java.awt.Color(255, 255, 255));
        enc_correo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        enc_correo.setText(" ");

        jLabel16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel16.setText("Teléfono:");

        jLabel15.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel15.setText("Dirección:");

        jLabel14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel14.setText("Razón social:");

        jLabel18.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel18.setText("Correo electrónico:");

        fec_enc.setEditable(false);
        fec_enc.setBackground(new java.awt.Color(255, 0, 0));
        fec_enc.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        fec_enc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        fec_enc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fec_encMouseClicked(evt);
            }
        });
        fec_enc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fec_encActionPerformed(evt);
            }
        });

        jLabel1.setText("Click para generar fecha de emisión...");

        jLabel2.setText("Click seleccionar o cambiar cliente...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(enc_correo))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(40, 40, 40)
                                .addComponent(enc_telefono))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(40, 40, 40)
                                .addComponent(enc_direccion))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(40, 40, 40)
                                .addComponent(enc_nombre_apellido))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(40, 40, 40)
                                .addComponent(enc_cedula))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel4))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fec_enc)
                                    .addComponent(enc_codigo)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fec_enc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(enc_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(enc_cedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(enc_nombre_apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(enc_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(enc_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enc_correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(0, 51, 153));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Detalle/s");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 6, 852, -1));

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

        jPanel5.add(jsTabla_ciu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 46, 852, 160));

        JBseleccionar_pro.setBackground(new java.awt.Color(0, 51, 153));
        JBseleccionar_pro.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBseleccionar_pro.setForeground(new java.awt.Color(255, 255, 255));
        JBseleccionar_pro.setText("+    Seleccionar producto");
        JBseleccionar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBseleccionar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBseleccionar_proActionPerformed(evt);
            }
        });
        jPanel5.add(JBseleccionar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 216, -1, 38));

        jl_num_det.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_num_det.setText("Detalles: 0");
        jPanel5.add(jl_num_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(734, 212, 150, -1));

        jl_total.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jl_total.setText("Total: $0");
        jPanel5.add(jl_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(534, 212, 150, -1));

        JBcrear_factura1.setBackground(new java.awt.Color(255, 0, 0));
        JBcrear_factura1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBcrear_factura1.setForeground(new java.awt.Color(255, 255, 255));
        JBcrear_factura1.setText("VOLVER A EMPEZAR");
        JBcrear_factura1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBcrear_factura1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBcrear_factura1ActionPerformed(evt);
            }
        });
        jPanel5.add(JBcrear_factura1, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 506, -1, 50));

        JBcrear_factura.setBackground(new java.awt.Color(0, 204, 102));
        JBcrear_factura.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        JBcrear_factura.setForeground(new java.awt.Color(255, 255, 255));
        JBcrear_factura.setText("CREAR FACTURA");
        JBcrear_factura.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JBcrear_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBcrear_facturaActionPerformed(evt);
            }
        });
        jPanel5.add(JBcrear_factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 506, 206, 50));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(0, 51, 153));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Totales");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 20, -1, -1));

        jl_num_det1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det1.setText("Total descuento:");
        jPanel7.add(jl_num_det1, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 120, -1, 28));

        jl_total1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_total1.setText("Subtotal sin descuento:");
        jPanel7.add(jl_total1, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 74, -1, 28));

        jl_num_det2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jl_num_det2.setText("Valor a pagar:");
        jPanel7.add(jl_num_det2, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 162, -1, 36));

        jlSubtotal.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jlSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlSubtotal.setText("$0");
        jPanel7.add(jlSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 74, 146, -1));

        jlTotal_descuento.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jlTotal_descuento.setForeground(new java.awt.Color(255, 51, 51));
        jlTotal_descuento.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlTotal_descuento.setText("- $0");
        jPanel7.add(jlTotal_descuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 120, 146, -1));

        jlTotal.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jlTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlTotal.setText("$0");
        jPanel7.add(jlTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(314, 162, 146, -1));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 216, 120, 10));

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(238, 260, -1, -1));

        javax.swing.GroupLayout JPfacturaLayout = new javax.swing.GroupLayout(JPfactura);
        JPfactura.setLayout(JPfacturaLayout);
        JPfacturaLayout.setHorizontalGroup(
            JPfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfacturaLayout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JPfacturaLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        JPfacturaLayout.setVerticalGroup(
            JPfacturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfacturaLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane.setViewportView(JPfactura);

        INICIO.addTab("FACTURAR", jScrollPane);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        jScrollPanew.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPanew.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPventas.setBackground(new java.awt.Color(234, 234, 234));
        JPventas.setMaximumSize(new java.awt.Dimension(980, 1400));
        JPventas.setMinimumSize(new java.awt.Dimension(980, 1400));
        JPventas.setPreferredSize(new java.awt.Dimension(980, 1400));
        JPventas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTenc_fac = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTenc_fac.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTenc_fac.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        JTenc_fac.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTenc_fac.setFocusable(false);
        JTenc_fac.setGridColor(new java.awt.Color(255, 255, 255));
        JTenc_fac.setOpaque(false);
        JTenc_fac.setRowHeight(30);
        JTenc_fac.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTenc_fac.getTableHeader().setResizingAllowed(false);
        JTenc_fac.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu2.setViewportView(JTenc_fac);

        JPventas.add(jsTabla_ciu2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 152, 380, 170));

        JTdet_fac = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdet_fac.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdet_fac.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        JTdet_fac.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdet_fac.setFocusable(false);
        JTdet_fac.setGridColor(new java.awt.Color(255, 255, 255));
        JTdet_fac.setOpaque(false);
        JTdet_fac.setRowHeight(30);
        JTdet_fac.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdet_fac.getTableHeader().setResizingAllowed(false);
        JTdet_fac.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu3.setViewportView(JTdet_fac);

        JPventas.add(jsTabla_ciu3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 490, 380, 165));

        VISTA_FACTURA.setBackground(new java.awt.Color(255, 255, 255));
        VISTA_FACTURA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        VISTA_FACTURA.setMaximumSize(new java.awt.Dimension(485, 520));
        VISTA_FACTURA.setMinimumSize(new java.awt.Dimension(485, 520));
        VISTA_FACTURA.setPreferredSize(new java.awt.Dimension(485, 520));
        VISTA_FACTURA.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        VISTA_FACTURA.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

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

        VISTA_FACTURA.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 152, 464, 102));

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

        VISTA_FACTURA.add(jsTabla_cat3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 464, 140));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        VISTA_FACTURA.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 406, 464, 47));

        jlAgregar_fac.setFont(new java.awt.Font("Jokerman", 0, 14)); // NOI18N
        jlAgregar_fac.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAgregar_fac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar_fac.png"))); // NOI18N
        jlAgregar_fac.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlAgregar_fac.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlAgregar_facMouseClicked(evt);
            }
        });
        VISTA_FACTURA.add(jlAgregar_fac, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 11, 113, 135));

        JPventas.add(VISTA_FACTURA, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jcBuscar_enc.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_enc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        jcBuscar_enc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_encItemStateChanged(evt);
            }
        });
        JPventas.add(jcBuscar_enc, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 74, 130, 35));

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Buscar por");
        JPventas.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 74, -1, 34));

        jtBuscar_enc.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_enc.setText("Buscar");
        jtBuscar_enc.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_enc.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_enc.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_enc.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_enc.setSelectionEnd(0);
        jtBuscar_enc.setSelectionStart(0);
        jtBuscar_enc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_encMouseClicked(evt);
            }
        });
        jtBuscar_enc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_encKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_encKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_encKeyTyped(evt);
            }
        });
        JPventas.add(jtBuscar_enc, new org.netbeans.lib.awtextra.AbsoluteConstraints(759, 73, 150, -1));

        lim_enc.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_enc.setForeground(new java.awt.Color(0, 102, 102));
        lim_enc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_enc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_enc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_enc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_encMouseClicked(evt);
            }
        });
        JPventas.add(lim_enc, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 73, -1, 34));

        res_enc.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_enc.setText("Resultados: 0 de 0");
        JPventas.add(res_enc, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 121, 207, -1));

        jl_titulo6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo6.setText("Encabezados de factura");
        jl_titulo6.setIconTextGap(10);
        jl_titulo6.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 26, 369, -1));

        jl_titulo8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo8.setText("Detalles de factura");
        jl_titulo8.setIconTextGap(10);
        jl_titulo8.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo8, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 364, 372, -1));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPventas.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 340, 380, 12));

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Buscar por");
        JPventas.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 412, -1, 34));

        res_det.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_det.setText("Resultados: 0 de 0");
        JPventas.add(res_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 460, 207, -1));

        jcBuscar_det.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_det.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Producto", "Cantidad", "Subtotal", "C. Factura" }));
        jcBuscar_det.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_detItemStateChanged(evt);
            }
        });
        JPventas.add(jcBuscar_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(617, 412, 130, 35));

        jtBuscar_det.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_det.setText("Buscar");
        jtBuscar_det.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_det.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_det.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_det.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_det.setSelectionEnd(0);
        jtBuscar_det.setSelectionStart(0);
        jtBuscar_det.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_detMouseClicked(evt);
            }
        });
        jtBuscar_det.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_detKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_detKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_detKeyTyped(evt);
            }
        });
        JPventas.add(jtBuscar_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(759, 411, 150, -1));

        lim_det.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_det.setForeground(new java.awt.Color(0, 102, 102));
        lim_det.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_det.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_det.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_det.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_detMouseClicked(evt);
            }
        });
        JPventas.add(lim_det, new org.netbeans.lib.awtextra.AbsoluteConstraints(912, 411, -1, 34));

        jbEliminar_cat1.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_cat1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jbEliminar_cat1.setForeground(new java.awt.Color(255, 255, 255));
        jbEliminar_cat1.setText("ELIMINAR ESTA FACTURA");
        jbEliminar_cat1.setBorder(null);
        jbEliminar_cat1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_cat1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_cat1MouseExited(evt);
            }
        });
        jbEliminar_cat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_cat1ActionPerformed(evt);
            }
        });
        JPventas.add(jbEliminar_cat1, new org.netbeans.lib.awtextra.AbsoluteConstraints(139, 558, 220, 45));

        jPanel13.setBackground(new java.awt.Color(51, 204, 0));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel13.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel55.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("Estadísticas generales");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPventas.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 1150, 670, 47));

        R3.setBackground(new java.awt.Color(255, 255, 255));
        R3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Clientes con factura");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Clientes sin factura");
        jLabel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_T2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        R3_T2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_T2.setText("$ en x F.Inactivas");
        R3_T2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_T2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_T1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        R3_T1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_T1.setText("$ en x F. Activas");
        R3_T1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_T1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_T3.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        R3_T3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_T3.setText("$ en x facturas");
        R3_T3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_T3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_A1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R3_A1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_A1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_A1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_B1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R3_B1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_B1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_B1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_C1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R3_C1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_C1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_C1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_D1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R3_D1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_D1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_D1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        R3_E1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R3_E1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R3_E1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R3_E1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout R3Layout = new javax.swing.GroupLayout(R3);
        R3.setLayout(R3Layout);
        R3Layout.setHorizontalGroup(
            R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(R3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(R3Layout.createSequentialGroup()
                        .addComponent(R3_A1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(R3_B1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(R3_C1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(R3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(R3_T1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(R3_T2, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(R3_D1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6)
                .addGroup(R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(R3_T3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_E1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        R3Layout.setVerticalGroup(
            R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(R3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_T1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_T2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_T3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(R3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(R3_A1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_B1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_C1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_D1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R3_E1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        JPventas.add(R3, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 1181, 700, 98));

        jPanel14.setBackground(new java.awt.Color(0, 204, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel14.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel56.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("Clientes con más compras");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPventas.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 820, 392, 47));

        R1.setBackground(new java.awt.Color(255, 255, 255));
        R1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1.setPreferredSize(new java.awt.Dimension(2, 100));
        R1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Cédula");
        jLabel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 21, 119, 35));

        jLabel28.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Nombre y Apellido");
        jLabel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 21, 165, 35));

        jLabel30.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Compras");
        jLabel30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 21, 119, 35));

        R1_A1.setBackground(new java.awt.Color(255, 255, 255));
        R1_A1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_A1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_A1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_A1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_A1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 56, 119, 35));

        R1_B1.setBackground(new java.awt.Color(255, 255, 255));
        R1_B1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_B1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_B1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_B1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_B1, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 56, 165, 35));

        R1_C1.setBackground(new java.awt.Color(255, 255, 255));
        R1_C1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_C1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_C1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_C1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 56, 119, 35));

        R1_A2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_A2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_A2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_A2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_A2, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 91, 119, 35));

        R1_B2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_B2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_B2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_B2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_B2, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 91, 165, 35));

        R1_C2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_C2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_C2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_C2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 91, 119, 35));

        R1_A3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_A3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_A3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_A3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_A3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 126, 119, 35));

        R1_B3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_B3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_B3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_B3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_B3, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 126, 165, 35));

        R1_C3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_C3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_C3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_C3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 126, 119, 35));

        R1_A4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_A4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_A4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_A4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_A4, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 161, 119, 35));

        R1_B4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_B4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_B4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_B4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_B4, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 161, 165, 35));

        R1_C4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_C4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_C4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_C4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 161, 119, 35));

        R1_C5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_C5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_C5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_C5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 196, 119, 35));

        R1_B5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_B5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_B5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_B5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_B5, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 196, 165, 35));

        R1_A5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R1_A5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R1_A5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R1_A5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R1.add(R1_A5, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 196, 119, 35));

        JPventas.add(R1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 852, 429, 238));

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPventas.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 689, 980, 12));

        jl_titulo9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/resumen.png"))); // NOI18N
        jl_titulo9.setText("RESUMEN");
        jl_titulo9.setIconTextGap(10);
        jl_titulo9.setVerifyInputWhenFocusTarget(false);
        JPventas.add(jl_titulo9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 720, 890, 60));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel18.setPreferredSize(new java.awt.Dimension(2, 100));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Cédula");
        jLabel33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 21, 119, 35));

        jLabel46.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Nombre y Apellido");
        jLabel46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel46.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 21, 165, 35));

        jLabel47.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Compras");
        jLabel47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel47.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 21, 119, 35));

        clientes_con_factura11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_con_factura11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_con_factura11.setText("0");
        clientes_con_factura11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_con_factura11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_con_factura11, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 56, 119, 35));

        clientes_sin_factura11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_sin_factura11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_sin_factura11.setText("0");
        clientes_sin_factura11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_sin_factura11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_sin_factura11, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 56, 165, 35));

        t_facturas_activas11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        t_facturas_activas11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_facturas_activas11.setText("0");
        t_facturas_activas11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        t_facturas_activas11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(t_facturas_activas11, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 56, 119, 35));

        clientes_con_factura12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_con_factura12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_con_factura12.setText("0");
        clientes_con_factura12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_con_factura12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_con_factura12, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 91, 119, 35));

        clientes_sin_factura12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_sin_factura12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_sin_factura12.setText("0");
        clientes_sin_factura12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_sin_factura12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_sin_factura12, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 91, 165, 35));

        t_facturas_activas12.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        t_facturas_activas12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_facturas_activas12.setText("0");
        t_facturas_activas12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        t_facturas_activas12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(t_facturas_activas12, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 91, 119, 35));

        clientes_con_factura13.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_con_factura13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_con_factura13.setText("0");
        clientes_con_factura13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_con_factura13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_con_factura13, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 126, 119, 35));

        clientes_sin_factura13.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_sin_factura13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_sin_factura13.setText("0");
        clientes_sin_factura13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_sin_factura13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_sin_factura13, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 126, 165, 35));

        t_facturas_activas13.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        t_facturas_activas13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_facturas_activas13.setText("0");
        t_facturas_activas13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        t_facturas_activas13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(t_facturas_activas13, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 126, 119, 35));

        clientes_con_factura14.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_con_factura14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_con_factura14.setText("0");
        clientes_con_factura14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_con_factura14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_con_factura14, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 161, 119, 35));

        clientes_sin_factura14.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_sin_factura14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_sin_factura14.setText("0");
        clientes_sin_factura14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_sin_factura14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_sin_factura14, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 161, 165, 35));

        t_facturas_activas14.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        t_facturas_activas14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_facturas_activas14.setText("0");
        t_facturas_activas14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        t_facturas_activas14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(t_facturas_activas14, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 161, 119, 35));

        t_facturas_activas15.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        t_facturas_activas15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_facturas_activas15.setText("0");
        t_facturas_activas15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        t_facturas_activas15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(t_facturas_activas15, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 196, 119, 35));

        clientes_sin_factura15.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_sin_factura15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_sin_factura15.setText("0");
        clientes_sin_factura15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_sin_factura15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_sin_factura15, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 196, 165, 35));

        clientes_con_factura15.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        clientes_con_factura15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientes_con_factura15.setText("0");
        clientes_con_factura15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        clientes_con_factura15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel18.add(clientes_con_factura15, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 196, 119, 35));

        JPventas.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 852, 429, 238));

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel19.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel58.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("Clientes con más compras");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPventas.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 820, 392, 47));

        jPanel21.setBackground(new java.awt.Color(51, 153, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanel21.setPreferredSize(new java.awt.Dimension(2, 100));

        jLabel61.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("Productos más vendidos");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPventas.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 820, 392, 47));

        R2.setBackground(new java.awt.Color(255, 255, 255));
        R2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2.setPreferredSize(new java.awt.Dimension(2, 100));
        R2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel54.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Código");
        jLabel54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel54.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 21, 119, 35));

        jLabel59.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("Nombre");
        jLabel59.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel59.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 21, 165, 35));

        jLabel60.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Ventas");
        jLabel60.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel60.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 21, 119, 35));

        R2_A1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_A1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_A1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_A1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_A1, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 56, 119, 35));

        R2_B1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_B1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_B1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_B1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_B1, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 56, 165, 35));

        R2_C1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_C1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_C1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_C1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 56, 119, 35));

        R2_A2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_A2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_A2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_A2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_A2, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 91, 119, 35));

        R2_B2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_B2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_B2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_B2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_B2, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 91, 165, 35));

        R2_C2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_C2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_C2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_C2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 91, 119, 35));

        R2_A3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_A3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_A3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_A3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_A3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 126, 119, 35));

        R2_B3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_B3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_B3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_B3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_B3, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 126, 165, 35));

        R2_C3.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_C3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_C3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_C3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 126, 119, 35));

        R2_A4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_A4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_A4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_A4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_A4, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 161, 119, 35));

        R2_B4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_B4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_B4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_B4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_B4, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 161, 165, 35));

        R2_C4.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_C4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_C4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_C4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 161, 119, 35));

        R2_C5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_C5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_C5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_C5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 196, 119, 35));

        R2_B5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_B5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_B5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_B5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_B5, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 196, 165, 35));

        R2_A5.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        R2_A5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        R2_A5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        R2_A5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        R2.add(R2_A5, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 196, 119, 35));

        JPventas.add(R2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 850, 429, 238));

        subir_1.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 14)); // NOI18N
        subir_1.setText("ir arriba");
        subir_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        subir_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subir_1MouseClicked(evt);
            }
        });
        JPventas.add(subir_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 1320, 50, -1));

        jScrollPanew.setViewportView(JPventas);

        INICIO.addTab("VENTAS", jScrollPanew);
        jScrollPanew.getVerticalScrollBar().setUnitIncrement(16);

        JPpagos.setBackground(new java.awt.Color(204, 255, 255));
        JPpagos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        res_num_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_num_pag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_num_pag.setText("Resultados: 0 de 0");
        JPpagos.add(res_num_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 357, 622, -1));

        jpDatos_pro1.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_pro1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_pro1.setMaximumSize(new java.awt.Dimension(275, 408));
        jpDatos_pro1.setMinimumSize(new java.awt.Dimension(275, 408));
        jpDatos_pro1.setPreferredSize(new java.awt.Dimension(275, 408));
        jpDatos_pro1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Pago seleccionado:");
        jpDatos_pro1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 289, -1));

        jlR7.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR7.setForeground(new java.awt.Color(51, 51, 51));
        jlR7.setText("Número:");
        jpDatos_pro1.add(jlR7, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 52, -1, -1));

        jb_Eliminar_pag.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_pag.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_pag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_pag.setText(" Eliminar");
        jb_Eliminar_pag.setBorder(null);
        jb_Eliminar_pag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_pag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_pagMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_pagMouseExited(evt);
            }
        });
        jb_Eliminar_pag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_pagActionPerformed(evt);
            }
        });
        jpDatos_pro1.add(jb_Eliminar_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 354, 129, 44));

        jbModificar_pag.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_pag.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_pag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_pag.setText(" Modificar");
        jbModificar_pag.setBorder(null);
        jbModificar_pag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_pag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_pagMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_pagMouseExited(evt);
            }
        });
        jbModificar_pag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_pagActionPerformed(evt);
            }
        });
        jpDatos_pro1.add(jbModificar_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 354, 114, 44));

        jlC5.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC5.setForeground(new java.awt.Color(51, 51, 51));
        jlC5.setText("Monto: $");
        jpDatos_pro1.add(jlC5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 156, -1, -1));

        jlF4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF4.setForeground(new java.awt.Color(51, 51, 51));
        jlF4.setText("Fecha de registro:");
        jpDatos_pro1.add(jlF4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 182, -1, -1));

        jlCodigo_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCodigo_pag.setText(" ");
        jpDatos_pro1.add(jlCodigo_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 52, 185, -1));

        jlPrecio_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlPrecio_pag.setText(" ");
        jpDatos_pro1.add(jlPrecio_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 156, 179, -1));

        jlReg_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlReg_pag.setText(" ");
        jpDatos_pro1.add(jlReg_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 182, 119, -1));

        jSeparator20.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_pro1.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 36, 257, 10));

        jbRegistrar_pag.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_pag.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_pag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_pag.setText("Agregar");
        jbRegistrar_pag.setBorder(null);
        jbRegistrar_pag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_pag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_pagMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_pagMouseExited(evt);
            }
        });
        jbRegistrar_pag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_pagActionPerformed(evt);
            }
        });
        jpDatos_pro1.add(jbRegistrar_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 306, 250, 44));

        jlProveedor_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlProveedor_pag.setText(" ");
        jpDatos_pro1.add(jlProveedor_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 78, 172, -1));

        jlE18.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE18.setForeground(new java.awt.Color(51, 51, 51));
        jlE18.setText("Proveedor:");
        jpDatos_pro1.add(jlE18, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 78, -1, -1));

        jSeparator21.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_pro1.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 404, 1, -1));

        jlE19.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE19.setForeground(new java.awt.Color(51, 51, 51));
        jlE19.setText("Cedula emp:");
        jpDatos_pro1.add(jlE19, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 130, -1, -1));

        jlceduempleado.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlceduempleado.setText(" ");
        jpDatos_pro1.add(jlceduempleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 130, 159, -1));

        jScrollPane3.setBorder(null);

        jtaDescripcion_pag.setEditable(false);
        jtaDescripcion_pag.setColumns(20);
        jtaDescripcion_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jtaDescripcion_pag.setLineWrap(true);
        jtaDescripcion_pag.setRows(5);
        jtaDescripcion_pag.setWrapStyleWord(true);
        jtaDescripcion_pag.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        jtaDescripcion_pag.setFocusable(false);
        jScrollPane3.setViewportView(jtaDescripcion_pag);

        jpDatos_pro1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 234, 250, 60));

        jlN7.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN7.setText("Descripción:");
        jpDatos_pro1.add(jlN7, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 208, -1, -1));

        jlE20.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE20.setForeground(new java.awt.Color(51, 51, 51));
        jlE20.setText("Nombre emp:");
        jpDatos_pro1.add(jlE20, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 104, -1, -1));

        jlempleado_nom.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlempleado_nom.setText(" ");
        jpDatos_pro1.add(jlempleado_nom, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 104, 151, -1));

        JPpagos.add(jpDatos_pro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(683, 6, -1, -1));

        jcBuscar_pag.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_pag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Número", "Monto", "C. Empleado", "RUC proveedor", "Descripción", "F. Registro" }));
        jcBuscar_pag.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_pagItemStateChanged(evt);
            }
        });
        JPpagos.add(jcBuscar_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(514, 16, 128, 35));

        jLabel57.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Buscar pago por");
        JPpagos.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 18, -1, 30));

        jtBuscar_pag.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_pag.setText("Buscar");
        jtBuscar_pag.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_pag.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_pag.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_pag.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_pag.setSelectionEnd(0);
        jtBuscar_pag.setSelectionStart(0);
        jtBuscar_pag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_pagMouseClicked(evt);
            }
        });
        jtBuscar_pag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_pagActionPerformed(evt);
            }
        });
        jtBuscar_pag.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_pagKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_pagKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_pagKeyTyped(evt);
            }
        });
        JPpagos.add(jtBuscar_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 57, 244, -1));

        lim_pag.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_pag.setForeground(new java.awt.Color(0, 102, 102));
        lim_pag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_pag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_pag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_pag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_pagMouseClicked(evt);
            }
        });
        JPpagos.add(lim_pag, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 57, 20, 35));

        JTpagos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpagos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpagos.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTpagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTpagos.setFocusable(false);
        JTpagos.setGridColor(new java.awt.Color(255, 255, 255));
        JTpagos.setOpaque(false);
        JTpagos.setRowHeight(60);
        JTpagos.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTpagos.getTableHeader().setResizingAllowed(false);
        JTpagos.getTableHeader().setReorderingAllowed(false);
        jsTabla_pro1.setViewportView(JTpagos);

        JPpagos.add(jsTabla_pro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 98, 622, 253));

        jl_titulo15.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo15.setText("Lista de pagos");
        jl_titulo15.setIconTextGap(10);
        jl_titulo15.setVerifyInputWhenFocusTarget(false);
        JPpagos.add(jl_titulo15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 17, -1, 75));

        INICIO.addTab("PAGOS A PROVEEDORES", JPpagos);

        MENU.addTab("INICIO", INICIO);

        JSpersonas.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JSpersonas.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPpersonas.setBackground(new java.awt.Color(252, 240, 219));
        JPpersonas.setMaximumSize(new java.awt.Dimension(980, 1050));
        JPpersonas.setMinimumSize(new java.awt.Dimension(980, 1050));
        JPpersonas.setPreferredSize(new java.awt.Dimension(980, 1050));
        JPpersonas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jsTabla_ciu4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTcli = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcli.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcli.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JPpersonas.add(jsTabla_ciu4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 378, 420, 170));

        jcBuscar_enc1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_enc1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        jcBuscar_enc1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_enc1ItemStateChanged(evt);
            }
        });
        JPpersonas.add(jcBuscar_enc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(97, 331, 142, 35));

        jLabel72.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("Buscar por");
        JPpersonas.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 331, -1, 34));

        jtBuscar_enc1.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_enc1.setText("Buscar");
        jtBuscar_enc1.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_enc1.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_enc1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_enc1.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_enc1.setSelectionEnd(0);
        jtBuscar_enc1.setSelectionStart(0);
        jtBuscar_enc1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_enc1MouseClicked(evt);
            }
        });
        jtBuscar_enc1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_enc1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_enc1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_enc1KeyTyped(evt);
            }
        });
        JPpersonas.add(jtBuscar_enc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 331, 195, -1));

        lim_cli.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_cli.setForeground(new java.awt.Color(0, 102, 102));
        lim_cli.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_cli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_cliMouseClicked(evt);
            }
        });
        JPpersonas.add(lim_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(443, 331, -1, 35));

        res_cli.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_cli.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_cli.setText("Resultados: 0 de 0");
        JPpersonas.add(res_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(236, 287, 204, 32));

        jl_titulo17.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo17.setText("Clientes");
        jl_titulo17.setIconTextGap(10);
        jl_titulo17.setVerifyInputWhenFocusTarget(false);
        JPpersonas.add(jl_titulo17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 287, 204, -1));

        jSeparator7.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPpersonas.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 257, 899, 12));

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

        JPpersonas.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 569, 420, 47));

        jsTabla_ciu5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTemp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTemp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTemp.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JPpersonas.add(jsTabla_ciu5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 378, 422, 170));

        jl_titulo20.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo20.setText("PERSONAS");
        jl_titulo20.setIconTextGap(10);
        jl_titulo20.setVerifyInputWhenFocusTarget(false);
        JPpersonas.add(jl_titulo20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 200, 35));

        jLabel74.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel74.setText("Buscar por");
        JPpersonas.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 20, -1, 35));

        jcBuscar_enc3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_enc3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        jcBuscar_enc3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_enc3ItemStateChanged(evt);
            }
        });
        JPpersonas.add(jcBuscar_enc3, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 20, 142, 35));

        jtBuscar_enc3.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_enc3.setText("Buscar");
        jtBuscar_enc3.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_enc3.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_enc3.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_enc3.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_enc3.setSelectionEnd(0);
        jtBuscar_enc3.setSelectionStart(0);
        jtBuscar_enc3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_enc3MouseClicked(evt);
            }
        });
        jtBuscar_enc3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_enc3KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_enc3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_enc3KeyTyped(evt);
            }
        });
        JPpersonas.add(jtBuscar_enc3, new org.netbeans.lib.awtextra.AbsoluteConstraints(457, 20, 185, -1));

        lim_enc3.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_enc3.setForeground(new java.awt.Color(0, 102, 102));
        lim_enc3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_enc3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_enc3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_enc3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_enc3MouseClicked(evt);
            }
        });
        JPpersonas.add(lim_enc3, new org.netbeans.lib.awtextra.AbsoluteConstraints(645, 20, -1, 35));

        jsTabla_ciu6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTper = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTper.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTper.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JPpersonas.add(jsTabla_ciu6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 899, 170));

        res_per.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_per.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_per.setText("Resultados: 0 de 0");
        JPpersonas.add(res_per, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 20, 212, 35));

        JPcli.setBackground(new java.awt.Color(255, 255, 255));

        jLabel63.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel63.setText("Cédula:");

        cedula_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        cedula_cli.setText("0");

        jLabel66.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel66.setText("ID:");

        id_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_cli.setText("0");

        jLabel68.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel68.setText("Nombre:");

        nombre_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_cli.setText("0");

        apellido_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        apellido_cli.setText("0");

        jLabel71.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel71.setText("Apellido:");

        jLabel75.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel75.setText("Celular:");

        celular_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        celular_cli.setText("0");

        jLabel77.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel77.setText("Fecha de nacimiento:");

        fecha_nac_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_nac_cli.setText("0");

        jLabel79.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel79.setText("Sexo:");

        sexo_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        sexo_cli.setText("0");

        jLabel83.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel83.setText("Descuento:");

        descuento_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        descuento_cli.setText("0");

        jLabel85.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel85.setText("Fecha de registro:");

        fecha_reg_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_reg_cli.setText("0");

        jLabel87.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel87.setText("Ciudad:");

        ciudad_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        ciudad_cli.setText("0");

        jLabel89.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel89.setText("Dirección:");

        direccion_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        direccion_cli.setText("0");

        jLabel91.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel91.setText("Email:");

        email_cli.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        email_cli.setText("0");

        javax.swing.GroupLayout JPcliLayout = new javax.swing.GroupLayout(JPcli);
        JPcli.setLayout(JPcliLayout);
        JPcliLayout.setHorizontalGroup(
            JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcliLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel66)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciudad_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(direccion_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel77)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_nac_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(celular_cli)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(JPcliLayout.createSequentialGroup()
                            .addComponent(jLabel63)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cedula_cli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(JPcliLayout.createSequentialGroup()
                            .addComponent(jLabel71)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(apellido_cli, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel91)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel79)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sexo_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel83)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descuento_cli))
                    .addGroup(JPcliLayout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_reg_cli)))
                .addGap(20, 20, 20))
        );
        JPcliLayout.setVerticalGroup(
            JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcliLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(cedula_cli)
                    .addComponent(jLabel66)
                    .addComponent(id_cli))
                .addGap(15, 15, 15)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel68)
                    .addComponent(nombre_cli)
                    .addComponent(jLabel71)
                    .addComponent(apellido_cli))
                .addGap(15, 15, 15)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(fecha_nac_cli)
                    .addComponent(jLabel79)
                    .addComponent(sexo_cli))
                .addGap(15, 15, 15)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(celular_cli)
                    .addComponent(jLabel91)
                    .addComponent(email_cli))
                .addGap(15, 15, 15)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(direccion_cli)
                    .addComponent(jLabel83)
                    .addComponent(descuento_cli))
                .addGap(15, 15, 15)
                .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel85)
                        .addComponent(fecha_reg_cli))
                    .addGroup(JPcliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel87)
                        .addComponent(ciudad_cli)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPpersonas.add(JPcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 616, 420, -1));

        eliminar_cli.setBackground(new java.awt.Color(255, 0, 51));
        eliminar_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        eliminar_cli.setForeground(new java.awt.Color(255, 255, 255));
        eliminar_cli.setText("x    Eliminar");
        eliminar_cli.setBorder(null);
        eliminar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eliminar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eliminar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eliminar_cliMouseExited(evt);
            }
        });
        eliminar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminar_cliActionPerformed(evt);
            }
        });
        JPpersonas.add(eliminar_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 851, 130, 40));

        registrar_cli.setBackground(new java.awt.Color(0, 204, 102));
        registrar_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        registrar_cli.setForeground(new java.awt.Color(255, 255, 255));
        registrar_cli.setText("+    Registrar");
        registrar_cli.setBorder(null);
        registrar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registrar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrar_cliMouseExited(evt);
            }
        });
        registrar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrar_cliActionPerformed(evt);
            }
        });
        JPpersonas.add(registrar_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 851, 130, 40));

        modificar_cli.setBackground(new java.awt.Color(51, 204, 255));
        modificar_cli.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        modificar_cli.setForeground(new java.awt.Color(255, 255, 255));
        modificar_cli.setText("¡    Modificar");
        modificar_cli.setBorder(null);
        modificar_cli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        modificar_cli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modificar_cliMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modificar_cliMouseExited(evt);
            }
        });
        modificar_cli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_cliActionPerformed(evt);
            }
        });
        JPpersonas.add(modificar_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 851, 136, 40));

        jtBuscar_enc4.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_enc4.setText("Buscar");
        jtBuscar_enc4.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_enc4.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_enc4.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_enc4.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_enc4.setSelectionEnd(0);
        jtBuscar_enc4.setSelectionStart(0);
        jtBuscar_enc4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_enc4MouseClicked(evt);
            }
        });
        jtBuscar_enc4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_enc4KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_enc4KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_enc4KeyTyped(evt);
            }
        });
        JPpersonas.add(jtBuscar_enc4, new org.netbeans.lib.awtextra.AbsoluteConstraints(725, 330, 197, -1));

        jcBuscar_enc4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_enc4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        jcBuscar_enc4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_enc4ItemStateChanged(evt);
            }
        });
        JPpersonas.add(jcBuscar_enc4, new org.netbeans.lib.awtextra.AbsoluteConstraints(577, 331, 142, 35));

        jLabel93.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel93.setText("Buscar por");
        JPpersonas.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 330, -1, 34));

        jl_titulo19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo19.setText("Empleados");
        jl_titulo19.setIconTextGap(10);
        jl_titulo19.setVerifyInputWhenFocusTarget(false);
        JPpersonas.add(jl_titulo19, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 287, 181, -1));

        res_emp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_emp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_emp.setText("Resultados: 0 de 0");
        JPpersonas.add(res_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 291, 204, -1));

        lim_emp.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_emp.setForeground(new java.awt.Color(0, 102, 102));
        lim_emp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_emp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_emp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_emp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_empMouseClicked(evt);
            }
        });
        JPpersonas.add(lim_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 330, -1, 36));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel64.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel64.setText("Cédula:");

        cedula_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        cedula_emp.setText("0");

        jLabel67.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel67.setText("ID:");

        id_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_emp.setText("0");

        jLabel69.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel69.setText("Nombre:");

        nombre_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_emp.setText("0");

        apellido_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        apellido_emp.setText("0");

        jLabel73.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel73.setText("Apellido:");

        jLabel76.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel76.setText("Celular:");

        celular_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        celular_emp.setText("0");

        jLabel78.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel78.setText("Fecha de nacimiento:");

        fecha_nac_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_nac_emp.setText("0");

        jLabel82.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel82.setText("Sexo:");

        sexo_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        sexo_emp.setText("0");

        jLabel84.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel84.setText("Departamento:");

        departamento_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        departamento_emp.setText("0");

        fecha_reg_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_reg_emp.setText("0");

        jLabel88.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel88.setText("Ciudad:");

        ciudad_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        ciudad_emp.setText("0");

        jLabel90.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel90.setText("Dirección:");

        direccion_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        direccion_emp.setText("0");

        jLabel92.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel92.setText("Email:");

        email_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        email_emp.setText("0");

        jLabel95.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel95.setText("Puesto:");

        puesto_emp.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        puesto_emp.setText("0");

        jLabel96.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel96.setText("Fecha de registro:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel67)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciudad_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(direccion_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_nac_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel76)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(celular_emp)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel64)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cedula_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel73)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(apellido_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel92)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel82)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sexo_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel84)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(departamento_emp))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel96)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_reg_emp, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel95)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(puesto_emp)))
                .addGap(20, 20, 20))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(cedula_emp)
                    .addComponent(jLabel67)
                    .addComponent(id_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(nombre_emp)
                    .addComponent(jLabel73)
                    .addComponent(apellido_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(fecha_nac_emp)
                    .addComponent(jLabel82)
                    .addComponent(sexo_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(celular_emp)
                    .addComponent(jLabel92)
                    .addComponent(email_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(direccion_emp)
                    .addComponent(jLabel84)
                    .addComponent(departamento_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(ciudad_emp)
                    .addComponent(jLabel95)
                    .addComponent(puesto_emp))
                .addGap(15, 15, 15)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fecha_reg_emp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        JPpersonas.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 617, -1, -1));

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

        JPpersonas.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 570, 422, 47));

        eliminar_cli1.setBackground(new java.awt.Color(255, 0, 51));
        eliminar_cli1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        eliminar_cli1.setForeground(new java.awt.Color(255, 255, 255));
        eliminar_cli1.setText("x    Eliminar");
        eliminar_cli1.setBorder(null);
        eliminar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        eliminar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eliminar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eliminar_cli1MouseExited(evt);
            }
        });
        eliminar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminar_cli1ActionPerformed(evt);
            }
        });
        JPpersonas.add(eliminar_cli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 887, 130, 40));

        modificar_cli1.setBackground(new java.awt.Color(51, 204, 255));
        modificar_cli1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        modificar_cli1.setForeground(new java.awt.Color(255, 255, 255));
        modificar_cli1.setText("¡    Modificar");
        modificar_cli1.setBorder(null);
        modificar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        modificar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modificar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modificar_cli1MouseExited(evt);
            }
        });
        modificar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificar_cli1ActionPerformed(evt);
            }
        });
        JPpersonas.add(modificar_cli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(644, 887, 136, 40));

        registrar_cli1.setBackground(new java.awt.Color(0, 204, 102));
        registrar_cli1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        registrar_cli1.setForeground(new java.awt.Color(255, 255, 255));
        registrar_cli1.setText("+    Registrar");
        registrar_cli1.setBorder(null);
        registrar_cli1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registrar_cli1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrar_cli1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrar_cli1MouseExited(evt);
            }
        });
        registrar_cli1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrar_cli1ActionPerformed(evt);
            }
        });
        JPpersonas.add(registrar_cli1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 887, 132, 40));

        jLabel86.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("subir");
        jLabel86.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel86MouseClicked(evt);
            }
        });
        JPpersonas.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(442, 965, 59, -1));

        jSeparator24.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator24.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator24.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        JPpersonas.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(471, 287, -1, 640));

        JSpersonas.setViewportView(JPpersonas);

        MENU.addTab("PERSONAS", JSpersonas);
        JSpersonas.getVerticalScrollBar().setUnitIncrement(16);

        JPgeneros.setBackground(new java.awt.Color(204, 255, 255));

        jl_titulo16.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo16.setText("Lista de géneros");
        jl_titulo16.setIconTextGap(10);
        jl_titulo16.setVerifyInputWhenFocusTarget(false);

        jpDatos_cat2.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cat2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cat2.setPreferredSize(new java.awt.Dimension(317, 396));

        jLabel21.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Género seleccionado:");

        jlR8.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR8.setText("ID:");

        jlN8.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN8.setText("SEXO:");

        jlNombre_cat1.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_cat1.setText(" ");

        jSeparator22.setForeground(new java.awt.Color(0, 0, 0));

        jbEnviar_cat1.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_cat1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_cat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_cat1.setText(" Enviar");
        jbEnviar_cat1.setBorder(null);
        jbEnviar_cat1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_cat1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_cat1MouseExited(evt);
            }
        });
        jbEnviar_cat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_cat1ActionPerformed(evt);
            }
        });

        jbEliminar_cat2.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_cat2.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_cat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_cat2.setText(" Eliminar");
        jbEliminar_cat2.setBorder(null);
        jbEliminar_cat2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_cat2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_cat2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_cat2MouseExited(evt);
            }
        });
        jbEliminar_cat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_cat2ActionPerformed(evt);
            }
        });

        jbModificar_cat1.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_cat1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_cat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_cat1.setText(" Modificar");
        jbModificar_cat1.setBorder(null);
        jbModificar_cat1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_cat1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_cat1MouseExited(evt);
            }
        });
        jbModificar_cat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_cat1ActionPerformed(evt);
            }
        });

        jbRegistrar_cat1.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_cat1.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_cat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_cat1.setText("Agregar");
        jbRegistrar_cat1.setBorder(null);
        jbRegistrar_cat1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_cat1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_cat1MouseExited(evt);
            }
        });
        jbRegistrar_cat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_cat1ActionPerformed(evt);
            }
        });

        jSeparator23.setForeground(new java.awt.Color(0, 0, 0));

        jlNombre_cat2.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_cat2.setText(" ");

        javax.swing.GroupLayout jpDatos_cat2Layout = new javax.swing.GroupLayout(jpDatos_cat2);
        jpDatos_cat2.setLayout(jpDatos_cat2Layout);
        jpDatos_cat2Layout.setHorizontalGroup(
            jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jpDatos_cat2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_cat2Layout.createSequentialGroup()
                            .addComponent(jlN8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlNombre_cat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpDatos_cat2Layout.createSequentialGroup()
                            .addComponent(jlR8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jlNombre_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpDatos_cat2Layout.createSequentialGroup()
                        .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jbModificar_cat1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(jbRegistrar_cat1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbEnviar_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbEliminar_cat2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jpDatos_cat2Layout.setVerticalGroup(
            jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDatos_cat2Layout.createSequentialGroup()
                .addComponent(jLabel21)
                .addGap(6, 6, 6)
                .addComponent(jSeparator22, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlR8)
                    .addComponent(jlNombre_cat1))
                .addGap(25, 25, 25)
                .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlN8)
                    .addComponent(jlNombre_cat2))
                .addGap(44, 44, 44)
                .addComponent(jSeparator23, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbRegistrar_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEnviar_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpDatos_cat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbModificar_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbEliminar_cat2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jcBuscar_gen.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_gen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "SEXO" }));
        jcBuscar_gen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_genItemStateChanged(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel62.setText("Buscar género por");

        jtBuscar_gen.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_gen.setText("Buscar");
        jtBuscar_gen.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_gen.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_gen.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_gen.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_gen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_genMouseClicked(evt);
            }
        });
        jtBuscar_gen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_genKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_genKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_genKeyTyped(evt);
            }
        });

        lim_cat1.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_cat1.setForeground(new java.awt.Color(0, 102, 102));
        lim_cat1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_cat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_cat1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_cat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_cat1MouseClicked(evt);
            }
        });

        JTgeneros = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTgeneros.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTgeneros.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTgeneros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTgeneros.setFocusable(false);
        JTgeneros.setGridColor(new java.awt.Color(255, 255, 255));
        JTgeneros.setOpaque(false);
        JTgeneros.setRowHeight(30);
        JTgeneros.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTgeneros.getTableHeader().setResizingAllowed(false);
        JTgeneros.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat1.setViewportView(JTgeneros);

        res_gen.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_gen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_gen.setText("Resultados: 0 de 0");

        javax.swing.GroupLayout JPgenerosLayout = new javax.swing.GroupLayout(JPgeneros);
        JPgeneros.setLayout(JPgenerosLayout);
        JPgenerosLayout.setHorizontalGroup(
            JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgenerosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(res_gen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPgenerosLayout.createSequentialGroup()
                        .addComponent(jl_titulo16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(JPgenerosLayout.createSequentialGroup()
                                .addComponent(jLabel62)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcBuscar_gen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jtBuscar_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jsTabla_cat1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(lim_cat1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jpDatos_cat2, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        JPgenerosLayout.setVerticalGroup(
            JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgenerosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPgenerosLayout.createSequentialGroup()
                        .addGroup(JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo16, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPgenerosLayout.createSequentialGroup()
                                .addGroup(JPgenerosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcBuscar_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addComponent(jtBuscar_gen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPgenerosLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(lim_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsTabla_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_cat2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(res_gen)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        MENU.addTab("GÉNEROS", JPgeneros);

        JPcategorias.setBackground(new java.awt.Color(204, 255, 255));

        jl_titulo5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo5.setText("Lista de categorías");
        jl_titulo5.setIconTextGap(10);
        jl_titulo5.setVerifyInputWhenFocusTarget(false);

        jpDatos_cat.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cat.setPreferredSize(new java.awt.Dimension(317, 396));
        jpDatos_cat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Categoría seleccionada:");
        jpDatos_cat.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 301, -1));

        jlR4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR4.setText("Nombre:");
        jpDatos_cat.add(jlR4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 48, -1, -1));

        jlN4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN4.setText("Descripción:");
        jpDatos_cat.add(jlN4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, -1, -1));

        jlNombre_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_cat.setText(" ");
        jpDatos_cat.add(jlNombre_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 48, 197, -1));

        jSeparator12.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_cat.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 32, 301, 10));

        jScrollPane2.setBorder(null);

        jtaDescripcion_cat.setEditable(false);
        jtaDescripcion_cat.setColumns(20);
        jtaDescripcion_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jtaDescripcion_cat.setLineWrap(true);
        jtaDescripcion_cat.setRows(5);
        jtaDescripcion_cat.setWrapStyleWord(true);
        jtaDescripcion_cat.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        jtaDescripcion_cat.setFocusable(false);
        jScrollPane2.setViewportView(jtaDescripcion_cat);

        jpDatos_cat.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 123, 262, 83));

        jbEnviar_cat.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_cat.setText(" Enviar");
        jbEnviar_cat.setBorder(null);
        jbEnviar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_catMouseExited(evt);
            }
        });
        jbEnviar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_catActionPerformed(evt);
            }
        });
        jpDatos_cat.add(jbEnviar_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 252, 125, 44));

        jbEliminar_cat.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_cat.setText(" Eliminar");
        jbEliminar_cat.setBorder(null);
        jbEliminar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_catMouseExited(evt);
            }
        });
        jbEliminar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_catActionPerformed(evt);
            }
        });
        jpDatos_cat.add(jbEliminar_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 314, 125, 44));

        jbModificar_cat.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_cat.setText(" Modificar");
        jbModificar_cat.setBorder(null);
        jbModificar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_catMouseExited(evt);
            }
        });
        jbModificar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_catActionPerformed(evt);
            }
        });
        jpDatos_cat.add(jbModificar_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 314, 125, 44));

        jbRegistrar_cat.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_cat.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_cat.setText("Agregar");
        jbRegistrar_cat.setBorder(null);
        jbRegistrar_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_catMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_catMouseExited(evt);
            }
        });
        jbRegistrar_cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_catActionPerformed(evt);
            }
        });
        jpDatos_cat.add(jbRegistrar_cat, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 252, 125, 44));

        jSeparator19.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_cat.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 224, 301, 10));

        jcBuscar_cat.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Descripción" }));
        jcBuscar_cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_catItemStateChanged(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("Buscar categoría por");

        jtBuscar_cat.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_cat.setText("Buscar");
        jtBuscar_cat.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_cat.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_cat.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_cat.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_cat.setSelectionEnd(0);
        jtBuscar_cat.setSelectionStart(0);
        jtBuscar_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_catMouseClicked(evt);
            }
        });
        jtBuscar_cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_catKeyTyped(evt);
            }
        });

        lim_cat.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_cat.setForeground(new java.awt.Color(0, 102, 102));
        lim_cat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_cat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_cat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_catMouseClicked(evt);
            }
        });

        JTcategorias = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcategorias.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcategorias.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTcategorias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTcategorias.setFocusable(false);
        JTcategorias.setGridColor(new java.awt.Color(255, 255, 255));
        JTcategorias.setOpaque(false);
        JTcategorias.setRowHeight(30);
        JTcategorias.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTcategorias.getTableHeader().setResizingAllowed(false);
        JTcategorias.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat.setViewportView(JTcategorias);

        res_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_cat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_cat.setText("Resultados: 0 de 0");

        javax.swing.GroupLayout JPcategoriasLayout = new javax.swing.GroupLayout(JPcategorias);
        JPcategorias.setLayout(JPcategoriasLayout);
        JPcategoriasLayout.setHorizontalGroup(
            JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcategoriasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(res_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(JPcategoriasLayout.createSequentialGroup()
                        .addComponent(jl_titulo5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPcategoriasLayout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(6, 6, 6)
                                .addComponent(jcBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jsTabla_cat))
                .addGap(2, 2, 2)
                .addComponent(lim_cat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jpDatos_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        JPcategoriasLayout.setVerticalGroup(
            JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPcategoriasLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPcategoriasLayout.createSequentialGroup()
                        .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPcategoriasLayout.createSequentialGroup()
                                .addGroup(JPcategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addComponent(jtBuscar_cat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPcategoriasLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(lim_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsTabla_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(res_cat))
                    .addComponent(jpDatos_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        MENU.addTab("CATEGORÍAS", JPcategorias);

        JPciudades.setBackground(new java.awt.Color(204, 255, 255));

        res_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_ciu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_ciu.setText("Resultados: 0 de 0");

        jcBuscar_ciu.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_ciu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nombre", "Provincia" }));
        jcBuscar_ciu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_ciuItemStateChanged(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("Buscar ciudad por");

        jtBuscar_ciu.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 16)); // NOI18N
        jtBuscar_ciu.setText("Buscar");
        jtBuscar_ciu.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_ciu.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_ciu.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_ciu.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_ciu.setSelectionEnd(0);
        jtBuscar_ciu.setSelectionStart(0);
        jtBuscar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_ciuMouseClicked(evt);
            }
        });
        jtBuscar_ciu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_ciuKeyTyped(evt);
            }
        });

        lim_ciu.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_ciu.setForeground(new java.awt.Color(0, 102, 102));
        lim_ciu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_ciuMouseClicked(evt);
            }
        });

        JTciudades = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTciudades.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTciudades.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTciudades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTciudades.setFocusable(false);
        JTciudades.setGridColor(new java.awt.Color(255, 255, 255));
        JTciudades.setOpaque(false);
        JTciudades.setRowHeight(30);
        JTciudades.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTciudades.getTableHeader().setResizingAllowed(false);
        JTciudades.getTableHeader().setReorderingAllowed(false);
        jsTabla_ciu.setViewportView(JTciudades);

        jl_titulo7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo7.setText("Lista de ciudades");
        jl_titulo7.setIconTextGap(10);
        jl_titulo7.setVerifyInputWhenFocusTarget(false);

        jpDatos_cat1.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_cat1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_cat1.setPreferredSize(new java.awt.Dimension(317, 396));
        jpDatos_cat1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Ciudad seleccionada:");
        jpDatos_cat1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 1, 280, -1));

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_cat1.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 32, 280, 10));

        jlR1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR1.setForeground(new java.awt.Color(51, 51, 51));
        jlR1.setText("Código:");
        jpDatos_cat1.add(jlR1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 48, -1, -1));

        jlN1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN1.setForeground(new java.awt.Color(51, 51, 51));
        jlN1.setText("Nombre:");
        jpDatos_cat1.add(jlN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 95, -1, -1));

        jlC1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC1.setForeground(new java.awt.Color(51, 51, 51));
        jlC1.setText("Provincia:");
        jpDatos_cat1.add(jlC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 142, -1, -1));

        jlCodigo_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCodigo_ciu.setText(" ");
        jpDatos_cat1.add(jlCodigo_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 48, 190, -1));

        jlNombre_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_ciu.setText(" ");
        jpDatos_cat1.add(jlNombre_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 95, 182, -1));

        jlProvincia_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlProvincia_ciu.setText(" ");
        jpDatos_cat1.add(jlProvincia_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(91, 142, 177, -1));

        jbRegistrar_ciu.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_ciu.setText("Agregar");
        jbRegistrar_ciu.setBorder(null);
        jbRegistrar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_ciuMouseExited(evt);
            }
        });
        jbRegistrar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_ciuActionPerformed(evt);
            }
        });
        jpDatos_cat1.add(jbRegistrar_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 221, 128, 44));

        jbEnviar_ciu.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_ciu.setText(" Enviar");
        jbEnviar_ciu.setBorder(null);
        jbEnviar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_ciuMouseExited(evt);
            }
        });
        jbEnviar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_ciuActionPerformed(evt);
            }
        });
        jpDatos_cat1.add(jbEnviar_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 221, 128, 44));

        jbEliminar_ciu.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_ciu.setText(" Eliminar");
        jbEliminar_ciu.setBorder(null);
        jbEliminar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_ciuMouseExited(evt);
            }
        });
        jbEliminar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_ciuActionPerformed(evt);
            }
        });
        jpDatos_cat1.add(jbEliminar_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 283, 128, 44));

        jbModificar_ciu.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_ciu.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_ciu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_ciu.setText(" Modificar");
        jbModificar_ciu.setBorder(null);
        jbModificar_ciu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_ciu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_ciuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_ciuMouseExited(evt);
            }
        });
        jbModificar_ciu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_ciuActionPerformed(evt);
            }
        });
        jpDatos_cat1.add(jbModificar_ciu, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 283, 128, 44));

        jSeparator18.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_cat1.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 182, 300, 10));

        javax.swing.GroupLayout JPciudadesLayout = new javax.swing.GroupLayout(JPciudades);
        JPciudades.setLayout(JPciudadesLayout);
        JPciudadesLayout.setHorizontalGroup(
            JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciudadesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPciudadesLayout.createSequentialGroup()
                        .addComponent(jl_titulo7)
                        .addGap(27, 27, 27)
                        .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPciudadesLayout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(res_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsTabla_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(lim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jpDatos_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        JPciudadesLayout.setVerticalGroup(
            JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciudadesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPciudadesLayout.createSequentialGroup()
                        .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPciudadesLayout.createSequentialGroup()
                                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPciudadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jtBuscar_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jl_titulo7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jsTabla_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jpDatos_cat1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(res_ciu)
                .addGap(85, 85, 85))
        );

        MENU.addTab("CIUDADES", JPciudades);

        JPdescuentos.setBackground(new java.awt.Color(204, 255, 255));

        res_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_des.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_des.setText("Resultados: 0 de 0");

        jpDatos_des.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_des.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_des.setPreferredSize(new java.awt.Dimension(317, 396));
        jpDatos_des.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Descuento seleccionado:");
        jpDatos_des.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 293, -1));

        jlR3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR3.setForeground(new java.awt.Color(51, 51, 51));
        jlR3.setText("Nombre:");
        jpDatos_des.add(jlR3, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 48, -1, -1));

        jbEnviar_des.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_des.setText(" Enviar");
        jbEnviar_des.setBorder(null);
        jbEnviar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_desMouseExited(evt);
            }
        });
        jbEnviar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_desActionPerformed(evt);
            }
        });
        jpDatos_des.add(jbEnviar_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 237, 255, 44));

        jbEliminar_des.setBackground(new java.awt.Color(255, 0, 51));
        jbEliminar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEliminar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbEliminar_des.setText(" Eliminar");
        jbEliminar_des.setBorder(null);
        jbEliminar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEliminar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEliminar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEliminar_desMouseExited(evt);
            }
        });
        jbEliminar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEliminar_desActionPerformed(evt);
            }
        });
        jpDatos_des.add(jbEliminar_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 293, 115, 44));

        jbModificar_des.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_des.setText(" Modificar");
        jbModificar_des.setBorder(null);
        jbModificar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_desMouseExited(evt);
            }
        });
        jbModificar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_desActionPerformed(evt);
            }
        });
        jpDatos_des.add(jbModificar_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 293, 128, 44));

        jlN3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN3.setForeground(new java.awt.Color(51, 51, 51));
        jlN3.setText("Porcentaje: %");
        jpDatos_des.add(jlN3, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 95, -1, -1));

        jlNombre_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_des.setText(" ");
        jpDatos_des.add(jlNombre_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(86, 48, 189, -1));

        jlPorcentaje_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlPorcentaje_des.setText(" ");
        jpDatos_des.add(jlPorcentaje_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 95, 157, -1));

        jSeparator10.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_des.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 32, 293, 10));

        jbRegistrar_des.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_des.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_des.setText("Agregar");
        jbRegistrar_des.setBorder(null);
        jbRegistrar_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_desMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_desMouseExited(evt);
            }
        });
        jbRegistrar_des.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_desActionPerformed(evt);
            }
        });
        jpDatos_des.add(jbRegistrar_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 181, 255, 44));

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_des.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 153, 293, 10));

        jcBuscar_des.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_des.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Porcentaje" }));
        jcBuscar_des.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_desItemStateChanged(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText("Buscar descuento por");

        jtBuscar_des.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_des.setText("Buscar");
        jtBuscar_des.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_des.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_des.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_des.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_des.setSelectionEnd(0);
        jtBuscar_des.setSelectionStart(0);
        jtBuscar_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_desMouseClicked(evt);
            }
        });
        jtBuscar_des.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_desKeyTyped(evt);
            }
        });

        lim_des.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_des.setForeground(new java.awt.Color(0, 102, 102));
        lim_des.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_des.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_des.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_des.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_desMouseClicked(evt);
            }
        });

        JTdescuentos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdescuentos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdescuentos.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTdescuentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTdescuentos.setFocusable(false);
        JTdescuentos.setGridColor(new java.awt.Color(255, 255, 255));
        JTdescuentos.setOpaque(false);
        JTdescuentos.setRowHeight(30);
        JTdescuentos.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTdescuentos.getTableHeader().setResizingAllowed(false);
        JTdescuentos.getTableHeader().setReorderingAllowed(false);
        jsTabla_des.setViewportView(JTdescuentos);

        jl_titulo14.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo14.setText("Lista de descuetos");
        jl_titulo14.setIconTextGap(10);
        jl_titulo14.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPdescuentosLayout = new javax.swing.GroupLayout(JPdescuentos);
        JPdescuentos.setLayout(JPdescuentosLayout);
        JPdescuentosLayout.setHorizontalGroup(
            JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdescuentosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addComponent(res_des, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPdescuentosLayout.createSequentialGroup()
                                .addComponent(jl_titulo14)
                                .addGap(27, 27, 27)
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addComponent(jtBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(lim_des))
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addComponent(jLabel37)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jsTabla_des, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                        .addComponent(jpDatos_des, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );
        JPdescuentosLayout.setVerticalGroup(
            JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdescuentosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                        .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPdescuentosLayout.createSequentialGroup()
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPdescuentosLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jcBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPdescuentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtBuscar_des, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lim_des, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jl_titulo14, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jsTabla_des, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jpDatos_des, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(res_des))
        );

        MENU.addTab("DESCUENTOS", JPdescuentos);

        JPproductos.setBackground(new java.awt.Color(204, 255, 255));

        res_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_pro.setText("Resultados: 0 de 0");

        jpDatos_pro.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_pro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_pro.setMaximumSize(new java.awt.Dimension(264, 388));
        jpDatos_pro.setMinimumSize(new java.awt.Dimension(264, 388));
        jpDatos_pro.setPreferredSize(new java.awt.Dimension(264, 388));
        jpDatos_pro.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Producto seleccionado:");
        jpDatos_pro.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 262, -1));

        jlR6.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR6.setForeground(new java.awt.Color(51, 51, 51));
        jlR6.setText("Código:");
        jpDatos_pro.add(jlR6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 48, -1, -1));

        jbEnviar_pro.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_pro.setText(" Enviar");
        jbEnviar_pro.setBorder(null);
        jbEnviar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_proMouseExited(evt);
            }
        });
        jbEnviar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_proActionPerformed(evt);
            }
        });
        jpDatos_pro.add(jbEnviar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 266, 113, 44));

        jb_Eliminar_pro.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_pro.setText(" Eliminar");
        jb_Eliminar_pro.setBorder(null);
        jb_Eliminar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_proMouseExited(evt);
            }
        });
        jb_Eliminar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_proActionPerformed(evt);
            }
        });
        jpDatos_pro.add(jb_Eliminar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 322, 113, 44));

        jbModificar_pro.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_pro.setText(" Modificar");
        jbModificar_pro.setBorder(null);
        jbModificar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_proMouseExited(evt);
            }
        });
        jbModificar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_proActionPerformed(evt);
            }
        });
        jpDatos_pro.add(jbModificar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 322, 116, 44));

        jlN6.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN6.setForeground(new java.awt.Color(51, 51, 51));
        jlN6.setText("Nombre:");
        jpDatos_pro.add(jlN6, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 76, -1, -1));

        jlC4.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC4.setForeground(new java.awt.Color(51, 51, 51));
        jlC4.setText("Precio: $");
        jpDatos_pro.add(jlC4, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 104, -1, -1));

        jlF3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF3.setForeground(new java.awt.Color(51, 51, 51));
        jlF3.setText("Fecha de registro:");
        jpDatos_pro.add(jlF3, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 216, -1, -1));

        jlCodigo_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCodigo_pro.setText(" ");
        jpDatos_pro.add(jlCodigo_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 48, 185, -1));

        jlNombre_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre_pro.setText(" ");
        jpDatos_pro.add(jlNombre_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 76, 177, -1));

        jlPrecio_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlPrecio_pro.setText(" ");
        jpDatos_pro.add(jlPrecio_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 104, 179, -1));

        jlReg_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlReg_pro.setText(" ");
        jpDatos_pro.add(jlReg_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 216, 118, -1));

        jSeparator16.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_pro.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 32, 262, 10));

        jbRegistrar_pro.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_pro.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_pro.setText("Agregar");
        jbRegistrar_pro.setBorder(null);
        jbRegistrar_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_proMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_proMouseExited(evt);
            }
        });
        jbRegistrar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_proActionPerformed(evt);
            }
        });
        jpDatos_pro.add(jbRegistrar_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 266, 116, 44));

        jlGen_emp1.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlGen_emp1.setForeground(new java.awt.Color(51, 51, 51));
        jlGen_emp1.setText("Existencias:");
        jpDatos_pro.add(jlGen_emp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 132, -1, -1));

        jlExistencias_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlExistencias_pro.setText(" ");
        jpDatos_pro.add(jlExistencias_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 132, 163, -1));

        jlE15.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE15.setForeground(new java.awt.Color(51, 51, 51));
        jlE15.setText("Categoría:");
        jpDatos_pro.add(jlE15, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 160, -1, -1));

        jlCategoria_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCategoria_pro.setText(" ");
        jpDatos_pro.add(jlCategoria_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 160, 168, -1));

        jlProveedor_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlProveedor_pro.setText(" ");
        jpDatos_pro.add(jlProveedor_pro, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 188, 164, -1));

        jlE16.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE16.setForeground(new java.awt.Color(51, 51, 51));
        jlE16.setText("Proveedor:");
        jpDatos_pro.add(jlE16, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 188, -1, -1));

        jSeparator17.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_pro.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 256, 262, 10));

        jcBuscar_pro.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_pro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Nombre", "Precio", "Existencias", "Categoría", "Proveedor", "F. Registro" }));
        jcBuscar_pro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_proItemStateChanged(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setText("Buscar producto por");

        jtBuscar_pro.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_pro.setText("Buscar");
        jtBuscar_pro.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_pro.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_pro.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_pro.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_pro.setSelectionEnd(0);
        jtBuscar_pro.setSelectionStart(0);
        jtBuscar_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_proMouseClicked(evt);
            }
        });
        jtBuscar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_proActionPerformed(evt);
            }
        });
        jtBuscar_pro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_proKeyTyped(evt);
            }
        });

        lim_pro.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_pro.setForeground(new java.awt.Color(0, 102, 102));
        lim_pro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_pro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_proMouseClicked(evt);
            }
        });

        JTproductos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTproductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTproductos.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTproductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTproductos.setFocusable(false);
        JTproductos.setGridColor(new java.awt.Color(255, 255, 255));
        JTproductos.setOpaque(false);
        JTproductos.setRowHeight(60);
        JTproductos.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTproductos.getTableHeader().setResizingAllowed(false);
        JTproductos.getTableHeader().setReorderingAllowed(false);
        jsTabla_pro.setViewportView(JTproductos);

        jl_titulo12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo12.setText("Lista de productos");
        jl_titulo12.setIconTextGap(10);
        jl_titulo12.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPproductosLayout = new javax.swing.GroupLayout(JPproductos);
        JPproductos.setLayout(JPproductosLayout);
        JPproductosLayout.setHorizontalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(res_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jsTabla_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addComponent(jl_titulo12, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(JPproductosLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jtBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, 0)
                .addComponent(lim_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpDatos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        JPproductosLayout.setVerticalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JPproductosLayout.createSequentialGroup()
                                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jcBuscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtBuscar_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jl_titulo12, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jsTabla_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(res_pro)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        MENU.addTab("PRODUCTOS", JPproductos);

        JPproveedores.setBackground(new java.awt.Color(204, 255, 255));

        res_prov.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_prov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        res_prov.setText("Resultados: 0 de 0");

        jpDatos_prov.setBackground(new java.awt.Color(255, 255, 255));
        jpDatos_prov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jpDatos_prov.setPreferredSize(new java.awt.Dimension(317, 396));
        jpDatos_prov.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Proveedor seleccionado:");
        jpDatos_prov.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 301, -1));

        jlR.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlR.setForeground(new java.awt.Color(51, 51, 51));
        jlR.setText("RUC:");
        jpDatos_prov.add(jlR, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 48, -1, -1));

        jlEmail.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlEmail.setText(" ");
        jpDatos_prov.add(jlEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 160, 216, -1));

        jbEnviar_prov.setBackground(new java.awt.Color(255, 102, 51));
        jbEnviar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbEnviar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/compartir.png"))); // NOI18N
        jbEnviar_prov.setText(" Enviar");
        jbEnviar_prov.setBorder(null);
        jbEnviar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbEnviar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbEnviar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbEnviar_provMouseExited(evt);
            }
        });
        jbEnviar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEnviar_provActionPerformed(evt);
            }
        });
        jpDatos_prov.add(jbEnviar_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 241, 132, 44));

        jb_Eliminar_prov.setBackground(new java.awt.Color(255, 0, 51));
        jb_Eliminar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jb_Eliminar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jb_Eliminar_prov.setText(" Eliminar");
        jb_Eliminar_prov.setBorder(null);
        jb_Eliminar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jb_Eliminar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jb_Eliminar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jb_Eliminar_provMouseExited(evt);
            }
        });
        jb_Eliminar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_Eliminar_provActionPerformed(evt);
            }
        });
        jpDatos_prov.add(jb_Eliminar_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 297, 132, 44));

        jbModificar_prov.setBackground(new java.awt.Color(0, 153, 255));
        jbModificar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbModificar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbModificar_prov.setText(" Modificar");
        jbModificar_prov.setBorder(null);
        jbModificar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbModificar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbModificar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbModificar_provMouseExited(evt);
            }
        });
        jbModificar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbModificar_provActionPerformed(evt);
            }
        });
        jpDatos_prov.add(jbModificar_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 297, 127, 44));

        jlN.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlN.setForeground(new java.awt.Color(51, 51, 51));
        jlN.setText("Nombre:");
        jpDatos_prov.add(jlN, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 76, -1, -1));

        jlC.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlC.setForeground(new java.awt.Color(51, 51, 51));
        jlC.setText("Ciudad:");
        jpDatos_prov.add(jlC, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 104, -1, -1));

        jlT.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlT.setForeground(new java.awt.Color(51, 51, 51));
        jlT.setText("Teléfono:");
        jpDatos_prov.add(jlT, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 132, -1, -1));

        jlE.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlE.setForeground(new java.awt.Color(51, 51, 51));
        jlE.setText("Correo:");
        jpDatos_prov.add(jlE, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 160, -1, -1));

        jlF.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jlF.setForeground(new java.awt.Color(51, 51, 51));
        jlF.setText("Fecha de registro:");
        jpDatos_prov.add(jlF, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 188, -1, -1));

        jlRUC.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlRUC.setText(" ");
        jpDatos_prov.add(jlRUC, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 48, 232, -1));

        jlNombre.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlNombre.setText(" ");
        jpDatos_prov.add(jlNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(81, 76, 206, -1));

        jlCiudad.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlCiudad.setText(" ");
        jpDatos_prov.add(jlCiudad, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 104, 214, -1));

        jlTelefono.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlTelefono.setText(" ");
        jpDatos_prov.add(jlTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 132, 204, -1));

        jlFecha_reg.setFont(new java.awt.Font("Yu Gothic UI", 1, 16)); // NOI18N
        jlFecha_reg.setText(" ");
        jpDatos_prov.add(jlFecha_reg, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 188, 146, -1));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_prov.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 32, 301, 10));

        jbRegistrar_prov.setBackground(new java.awt.Color(0, 204, 102));
        jbRegistrar_prov.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
        jbRegistrar_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/agregar.png"))); // NOI18N
        jbRegistrar_prov.setText("Agregar");
        jbRegistrar_prov.setBorder(null);
        jbRegistrar_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRegistrar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jbRegistrar_provMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jbRegistrar_provMouseExited(evt);
            }
        });
        jbRegistrar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRegistrar_provActionPerformed(evt);
            }
        });
        jpDatos_prov.add(jbRegistrar_prov, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 241, 127, 44));

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jpDatos_prov.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 222, 301, 10));

        jcBuscar_prov.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jcBuscar_prov.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RUC", "Nombre", "Ciudad", "Teléfono", "Correo", "F. Registro" }));
        jcBuscar_prov.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcBuscar_provItemStateChanged(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("Buscar proveedor por");

        jtBuscar_prov.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        jtBuscar_prov.setText("Buscar");
        jtBuscar_prov.setMinimumSize(new java.awt.Dimension(317, 31));
        jtBuscar_prov.setPreferredSize(new java.awt.Dimension(317, 35));
        jtBuscar_prov.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jtBuscar_prov.setSelectionColor(new java.awt.Color(153, 204, 255));
        jtBuscar_prov.setSelectionEnd(0);
        jtBuscar_prov.setSelectionStart(0);
        jtBuscar_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtBuscar_provMouseClicked(evt);
            }
        });
        jtBuscar_prov.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtBuscar_provActionPerformed(evt);
            }
        });
        jtBuscar_prov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtBuscar_provKeyTyped(evt);
            }
        });

        lim_prov.setFont(new java.awt.Font("PMingLiU-ExtB", 0, 18)); // NOI18N
        lim_prov.setForeground(new java.awt.Color(0, 102, 102));
        lim_prov.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lim_prov.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close.png"))); // NOI18N
        lim_prov.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lim_prov.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lim_provMouseClicked(evt);
            }
        });

        JTproveedores = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTproveedores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTproveedores.setFont(new java.awt.Font("Calibri Light", 1, 14)); // NOI18N
        JTproveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        JTproveedores.setFocusable(false);
        JTproveedores.setGridColor(new java.awt.Color(255, 255, 255));
        JTproveedores.setOpaque(false);
        JTproveedores.setRowHeight(30);
        JTproveedores.setSelectionBackground(new java.awt.Color(0, 204, 204));
        JTproveedores.getTableHeader().setResizingAllowed(false);
        JTproveedores.getTableHeader().setReorderingAllowed(false);
        jsTabla_cat6.setViewportView(JTproveedores);

        jl_titulo13.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo13.setText("Lista de proveedores");
        jl_titulo13.setIconTextGap(10);
        jl_titulo13.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout JPproveedoresLayout = new javax.swing.GroupLayout(JPproveedores);
        JPproveedores.setLayout(JPproveedoresLayout);
        JPproveedoresLayout.setHorizontalGroup(
            JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproveedoresLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addComponent(res_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPproveedoresLayout.createSequentialGroup()
                                .addComponent(jl_titulo13)
                                .addGap(28, 28, 28)
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addComponent(jtBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(2, 2, 2)
                                        .addComponent(lim_prov))
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jcBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jsTabla_cat6, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpDatos_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))))
        );
        JPproveedoresLayout.setVerticalGroup(
            JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproveedoresLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                        .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(JPproveedoresLayout.createSequentialGroup()
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPproveedoresLayout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jcBuscar_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(JPproveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lim_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jtBuscar_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jl_titulo13, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addComponent(jsTabla_cat6, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jpDatos_prov, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(res_prov))
        );

        MENU.addTab("PROVEEDORES", JPproveedores);

        getContentPane().add(MENU, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 980, 530));

        jPanel1.setBackground(new java.awt.Color(153, 0, 51));
        jPanel1.setMinimumSize(new java.awt.Dimension(1020, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(1020, 40));

        sistema_titulo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        sistema_titulo.setForeground(new java.awt.Color(255, 255, 255));
        sistema_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sistema_titulo.setText("Centro Comercial");

        FECHA_HORA.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        FECHA_HORA.setForeground(new java.awt.Color(255, 255, 255));
        FECHA_HORA.setText("FECHA_HORA");

        USUARIO.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        USUARIO.setForeground(new java.awt.Color(255, 255, 255));
        USUARIO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario-de-perfil.png"))); // NOI18N
        USUARIO.setText("USUARIO");
        USUARIO.setIconTextGap(10);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        SALIR.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        SALIR.setForeground(new java.awt.Color(255, 255, 255));
        SALIR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cerrar-sesion (1).png"))); // NOI18N
        SALIR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SALIR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SALIRMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(sistema_titulo)
                .addGap(170, 170, 170)
                .addComponent(FECHA_HORA, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(USUARIO, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(SALIR)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sistema_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FECHA_HORA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(USUARIO, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(SALIR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        FONDO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/abstracto_rojo.jpeg"))); // NOI18N
        FONDO.setMaximumSize(new java.awt.Dimension(1020, 625));
        FONDO.setMinimumSize(new java.awt.Dimension(1020, 625));
        FONDO.setPreferredSize(new java.awt.Dimension(1020, 625));
        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbEnviar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_provActionPerformed
//        if (!jlRUC.getText().equals(" ")) {
//            if (modo_prov.equals("producto")) {
//                JFproducto.jt_proveedor.setText(jlRUC.getText());
//                SISTEMA.MENU.setSelectedIndex(6);
//                JFpro.setVisible(true);
//            } else {
//                JFpagos.jt_proveedor.setText(jlRUC.getText());
//                SISTEMA.MENU.setSelectedIndex(0);
//                INICIO.setSelectedIndex(2);
//                JFpag.setVisible(true);
//            }
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }

    }//GEN-LAST:event_jbEnviar_provActionPerformed

    private void jb_Eliminar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_provActionPerformed
        if (!jlRUC.getText().equals(" ")) {
            eliminar(10);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_provActionPerformed

    private void jbModificar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_provActionPerformed
//        if (!jlRUC.getText().equals(" ")) {
//            JFproveedor.forma = "modificar";
//            JFproveedor.cambiar_diseño();
//            JFprov.llenar(jlRUC.getText());
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }

    }//GEN-LAST:event_jbModificar_provActionPerformed

    private void jcBuscar_provItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_provItemStateChanged

    }//GEN-LAST:event_jcBuscar_provItemStateChanged

    private void jtBuscar_provMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_provMouseClicked
        if (jtBuscar_prov.getText().equals("Buscar")) {
            jtBuscar_prov.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_provMouseClicked

    private void jtBuscar_provKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyPressed
        if (jtBuscar_prov.getText().equals("Buscar")) {
            jtBuscar_prov.setText("");
            lim_prov.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_provKeyPressed

    private void jtBuscar_provKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyReleased
        if (!jtBuscar_prov.getText().equals("")) {
            buscar(JTproveedores, jtBuscar_prov, res_prov, prov, jcBuscar_prov);
        } else {
            lim_prov.setVisible(false);
            jtBuscar_prov.setText("Buscar");
            jtBuscar_prov.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_provKeyReleased

    private void jtBuscar_provKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_provKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_provKeyTyped

    private void lim_provMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_provMouseClicked
        lim_prov.setVisible(false);
        jtBuscar_prov.setText("Buscar");
        jtBuscar_prov.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_provMouseClicked

    private void jbEnviar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_provMouseEntered
        jbEnviar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_provMouseEntered

    private void jbEnviar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_provMouseExited
        jbEnviar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_provMouseExited

    private void jbModificar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_provMouseEntered
        jbModificar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_provMouseEntered

    private void jbModificar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_provMouseExited
        jbModificar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_provMouseExited

    private void jb_Eliminar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_provMouseEntered
        jb_Eliminar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jb_Eliminar_provMouseEntered

    private void jb_Eliminar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_provMouseExited
        jb_Eliminar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jb_Eliminar_provMouseExited

    private void jbRegistrar_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_provMouseEntered
        jbRegistrar_prov.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_provMouseEntered

    private void jbRegistrar_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_provMouseExited
        jbRegistrar_prov.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_provMouseExited

    private void jbRegistrar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_provActionPerformed
//        JFproveedor.forma = "registrar";
//        JFproveedor.cambiar_diseño();
//        JFproveedor.limpiar();
//        JFprov.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_provActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if (actualizado == false) {
            visualizar();
            actualizado = true;
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void jcBuscar_ciuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_ciuItemStateChanged

    }//GEN-LAST:event_jcBuscar_ciuItemStateChanged

    private void jtBuscar_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_provActionPerformed

    private void lim_ciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_ciuMouseClicked
        lim_ciu.setVisible(false);
        jtBuscar_ciu.setText("Buscar");
        jtBuscar_ciu.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_ciuMouseClicked

    private void jtBuscar_ciuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_ciuKeyTyped

    private void jtBuscar_ciuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyReleased
        if (!jtBuscar_ciu.getText().equals("")) {
            buscar(JTciudades, jtBuscar_ciu, res_ciu, ciu, jcBuscar_ciu);
        } else {
            lim_ciu.setVisible(false);
            jtBuscar_ciu.setText("Buscar");
            jtBuscar_ciu.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_ciuKeyReleased

    private void jtBuscar_ciuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_ciuKeyPressed
        if (jtBuscar_ciu.getText().equals("Buscar")) {
            jtBuscar_ciu.setText("");
            lim_ciu.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_ciuKeyPressed

    private void jtBuscar_ciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_ciuMouseClicked
        if (jtBuscar_ciu.getText().equals("Buscar")) {
            jtBuscar_ciu.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_ciuMouseClicked

    private void jbEnviar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_desMouseEntered

    private void jbEnviar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_desMouseExited

    private void jbEnviar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_desActionPerformed
        if (!jlNombre_des.getText().equals(" ")) {
            JFcliente.descuento.setText(jlNombre_des.getText());
            SISTEMA.MENU.setSelectedIndex(3);
            JFcli.setVisible(true);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_desActionPerformed

    private void jbEliminar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_desMouseEntered

    private void jbEliminar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_desMouseExited

    private void jbEliminar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_desActionPerformed
        if (!jlNombre_des.getText().equals(" ")) {
            eliminar(4);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_desActionPerformed

    private void jbModificar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_desMouseEntered

    private void jbModificar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_desMouseExited

    private void jbModificar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_desActionPerformed
//        if (!jlNombre_des.getText().equals(" ")) {
//            JFdescuento.forma = "modificar";
//            JFdescuento.cambiar_diseño();
//            JFdes.llenar(jlNombre_des.getText());
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbModificar_desActionPerformed

    private void jbRegistrar_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_desMouseEntered

    private void jbRegistrar_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_desMouseExited

    private void jbRegistrar_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_desActionPerformed
//        JFdescuento.forma = "registrar";
//        JFdescuento.cambiar_diseño();
//        JFdescuento.limpiar();
//        JFdes.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_desActionPerformed

    private void jcBuscar_desItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_desItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_desItemStateChanged

    private void jtBuscar_desMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_desMouseClicked
        if (jtBuscar_des.getText().equals("Buscar")) {
            jtBuscar_des.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_desMouseClicked

    private void jtBuscar_desKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyPressed
        if (jtBuscar_des.getText().equals("Buscar")) {
            jtBuscar_des.setText("");
            lim_des.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_desKeyPressed

    private void jtBuscar_desKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyReleased
        if (!jtBuscar_des.getText().equals("")) {
            buscar(JTdescuentos, jtBuscar_des, res_des, des, jcBuscar_des);
        } else {
            lim_des.setVisible(false);
            jtBuscar_des.setText("Buscar");
            jtBuscar_des.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_desKeyReleased

    private void jtBuscar_desKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_desKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_desKeyTyped

    private void lim_desMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_desMouseClicked
        lim_des.setVisible(false);
        jtBuscar_des.setText("Buscar");
        jtBuscar_des.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_desMouseClicked

    private void jbEliminar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_catMouseEntered
        jbEliminar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbEliminar_catMouseEntered

    private void jbEliminar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_catMouseExited
        jbEliminar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbEliminar_catMouseExited

    private void jbEliminar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_catActionPerformed
        if (!jlNombre_cat.getText().equals(" ")) {
            eliminar(1);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_catActionPerformed

    private void jbModificar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_catMouseEntered
        jbModificar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_catMouseEntered

    private void jbModificar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_catMouseExited
        jbModificar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_catMouseExited

    private void jbModificar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_catActionPerformed
//        if (!jlNombre_cat.getText().equals(" ")) {
//            JFcategoria.forma = "modificar";
//            JFcategoria.cambiar_diseño();
//            JFcat.llenar(jlNombre_cat.getText());
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbModificar_catActionPerformed

    private void jbRegistrar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_catMouseEntered
        jbRegistrar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_catMouseEntered

    private void jbRegistrar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_catMouseExited
        jbRegistrar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_catMouseExited

    private void jbRegistrar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_catActionPerformed
//        JFcategoria.forma = "registrar";
//        JFcategoria.cambiar_diseño();
//        JFcategoria.limpiar();
//        JFcat.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_catActionPerformed

    private void jcBuscar_catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_catItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_catItemStateChanged

    private void jtBuscar_catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_catMouseClicked
        if (jtBuscar_cat.getText().equals("Buscar")) {
            jtBuscar_cat.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_catMouseClicked

    private void jtBuscar_catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyPressed
        if (jtBuscar_cat.getText().equals("Buscar")) {
            jtBuscar_cat.setText("");
            lim_cat.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_catKeyPressed

    private void jtBuscar_catKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyReleased
        if (!jtBuscar_cat.getText().equals("")) {
            buscar(JTcategorias, jtBuscar_cat, res_cat, cat, jcBuscar_cat);
        } else {
            lim_cat.setVisible(false);
            jtBuscar_cat.setText("Buscar");
            jtBuscar_cat.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_catKeyReleased

    private void jtBuscar_catKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_catKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_catKeyTyped

    private void lim_catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_catMouseClicked
        lim_cat.setVisible(false);
        jtBuscar_cat.setText("Buscar");
        jtBuscar_cat.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_catMouseClicked

    private void jbEnviar_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_catActionPerformed
//        if (!jlNombre_cat.getText().equals(" ")) {
//            JFproducto.jt_categoria.setText(jlNombre_cat.getText());
//            SISTEMA.MENU.setSelectedIndex(6);
//            JFpro.setVisible(true);
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbEnviar_catActionPerformed

    private void jbEnviar_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_catMouseExited
        jbEnviar_cat.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_catMouseExited

    private void jbEnviar_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_catMouseEntered
        jbEnviar_cat.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_catMouseEntered

    private void enc_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enc_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enc_cedulaActionPerformed

    private void jbEnviar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_proMouseEntered

    private void jbEnviar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_proMouseExited

    private void jbEnviar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_proActionPerformed
        if (!jlCodigo_pro.getText().equals(" ")) {
            if (enc_cedula.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "¡Primero selecione un cliente antes enviar productos!");
            } else {
                int existencias = Integer.parseInt(jlExistencias_pro.getText());
                if (existencias > 0) {
                    boolean repetido = false;
                    for (int i = 0; i < detalles.size(); i++) {
                        if (detalles.get(i).equals(jlCodigo_pro.getText())) {
                            repetido = true;
                            break;
                        }
                    }
                    if (repetido) {
                        JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
                            if (cantidad > 0 && cantidad <= Integer.parseInt(jlExistencias_pro.getText())) {
                                Double xprecio = Double.valueOf(jlPrecio_pro.getText());
                                JTdetalle.setDefaultRenderer(Object.class, new BotonTabla());
                                Object detalle[] = {jlCodigo_pro.getText(), jlNombre_pro.getText(), cantidad, xprecio, descuento + "%", xprecio - ((descuento * xprecio) / 100), Math.round((cantidad * (xprecio - ((descuento * xprecio) / 100))) * 100.0) / 100.0, boton1};

                                detalles.add(jlCodigo_pro.getText());
                                //-------------- Agrega un detalle a la tabla
                                tabla_detalle.addRow(detalle);
                                JTdetalle.setModel(tabla_detalle);
                                //Confirmar_venta.setEnabled(true);
                                //--------------- Actualiza variables
                                num_det++;

                                subtotal += xprecio * cantidad;
                                subtotal = Math.rint(subtotal * 100.0) / 100.0; //lo deja con 2 decimales

                                double xsubtotal = cantidad * (xprecio - ((descuento * xprecio) / 100));
                                total += xsubtotal;
                                total = (Math.rint(total * 100.0) / 100.0); //lo deja con 2 decimales

                                total_descuento += (xprecio * cantidad) - (xsubtotal);
                                total_descuento = (Math.rint(total_descuento * 100.0) / 100.0); //lo deja con 2 decimales

                                jl_num_det.setText("Detalles: " + num_det);
                                jl_total.setText("Total: $" + total);
                                jlSubtotal.setText("$" + subtotal);
                                jlTotal_descuento.setText("- $" + total_descuento);
                                jlTotal.setText("$" + (total));

                                //---------- Redireccionar
                                JBcrear_factura.setEnabled(true);
                                MENU.setSelectedIndex(0);
                                INICIO.setSelectedIndex(0);
                            } else {
                                if (cantidad > Integer.parseInt(jlExistencias_pro.getText())) {
                                    JOptionPane.showMessageDialog(null, "¡Solo existen '" + jlExistencias_pro.getText() + "' de este producto!", null, JOptionPane.WARNING_MESSAGE);
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
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEnviar_proActionPerformed

    private void jb_Eliminar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_proMouseEntered

    private void jb_Eliminar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_proMouseExited

    private void jb_Eliminar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_proActionPerformed
        if (!jlCodigo_pro.getText().equals(" ")) {
            eliminar(9);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_proActionPerformed

    private void jbModificar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_proMouseEntered

    private void jbModificar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_proMouseExited

    private void jbModificar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_proActionPerformed
//        modo_prov = "producto";
//        if (!jlCodigo_pro.getText().equals(" ")) {JFproducto.forma = "modificar";
//            JFproducto.cambiar_diseño();
//            JFpro.llenar(Integer.parseInt(jlCodigo_pro.getText()));
//            reiniciar_factura(); //por precaución
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbModificar_proActionPerformed

    private void jbRegistrar_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_proMouseEntered

    private void jbRegistrar_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_proMouseExited

    private void jbRegistrar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_proActionPerformed
//        modo_prov = "producto";
//        JFproducto.forma = "registrar";
//        JFproducto.cambiar_diseño();
//        JFproducto.limpiar();
//        JFpro.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_proActionPerformed

    private void jcBuscar_proItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_proItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_proItemStateChanged

    private void jtBuscar_proMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_proMouseClicked
        if (jtBuscar_pro.getText().equals("Buscar")) {
            jtBuscar_pro.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_proMouseClicked

    private void jtBuscar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_proActionPerformed

    private void jtBuscar_proKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyPressed
        if (jtBuscar_pro.getText().equals("Buscar")) {
            jtBuscar_pro.setText("");
            lim_pro.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_proKeyPressed

    private void jtBuscar_proKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyReleased
        if (!jtBuscar_pro.getText().equals("")) {
            buscar(JTproductos, jtBuscar_pro, res_pro, pro, jcBuscar_pro);
        } else {
            lim_pro.setVisible(false);
            jtBuscar_pro.setText("Buscar");
            jtBuscar_pro.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_proKeyReleased

    private void jtBuscar_proKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_proKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_proKeyTyped

    private void lim_proMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_proMouseClicked
        lim_pro.setVisible(false);
        jtBuscar_pro.setText("Buscar");
        jtBuscar_pro.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_proMouseClicked

    private void jbRegistrar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuMouseEntered
        jbRegistrar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbRegistrar_ciuMouseEntered

    private void jbRegistrar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuMouseExited
        jbRegistrar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbRegistrar_ciuMouseExited

    private void jbRegistrar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_ciuActionPerformed
//        JFciudad.forma = "registrar";
//        JFciudad.cambiar_diseño();
//        JFciudad.limpiar();
//        JFciudad.cargar_ciudades();
//        JFciu.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_ciuActionPerformed

    private void jbEnviar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_ciuMouseEntered
        jbEnviar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbEnviar_ciuMouseEntered

    private void jbEnviar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_ciuMouseExited
        jbEnviar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbEnviar_ciuMouseExited

    private void jbEnviar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_ciuActionPerformed
//        if (!jlCodigo_ciu.getText().equals(" ")) {
//            JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
//            SISTEMA.MENU.setSelectedIndex(7);
//            JFprov.setVisible(true);
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbEnviar_ciuActionPerformed

    private void jbModificar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_ciuMouseEntered
        jbModificar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbModificar_ciuMouseEntered

    private void jbModificar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_ciuMouseExited
        jbModificar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbModificar_ciuMouseExited

    private void jbModificar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_ciuActionPerformed
//        if (!jlCodigo_ciu.getText().equals(" ")) {
//            JFciudad.forma = "modificar";
//            JFciudad.cambiar_diseño();
//            JFciudad.cargar_ciudades_mod(jlNombre_ciu.getText());
//            JFciu.llenar(Integer.parseInt(jlCodigo_ciu.getText()));
//        } else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbModificar_ciuActionPerformed

    private void jbEliminar_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_ciuMouseEntered
        jbEliminar_ciu.setForeground(Color.white);
    }//GEN-LAST:event_jbEliminar_ciuMouseEntered

    private void jbEliminar_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_ciuMouseExited
        jbEliminar_ciu.setForeground(Color.black);
    }//GEN-LAST:event_jbEliminar_ciuMouseExited

    private void jbEliminar_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_ciuActionPerformed
        if (!jlCodigo_ciu.getText().equals(" ")) {
            eliminar(2);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_ciuActionPerformed

    private void JBseleccionar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBseleccionar_proActionPerformed
        MENU.setSelectedIndex(6);
    }//GEN-LAST:event_JBseleccionar_proActionPerformed

    private void enc_cedulaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enc_cedulaMouseClicked
        if (enc_cedula.isEnabled()) {
            MENU.setSelectedIndex(3);
        } else{
            JOptionPane.showMessageDialog(null, "!Primero genere una fecha de emisión!");
        }
        
    }//GEN-LAST:event_enc_cedulaMouseClicked

    private void JBcrear_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcrear_facturaActionPerformed
        if (JBcrear_factura.isEnabled()) {
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
                    total -= c6;
                    total = Math.rint(total*100.0)/100.0; //deja al valor con dos decimales
                    
                    double c3 = Double.parseDouble(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 3).toString());
                    int c2 = Integer.parseInt(JTdetalle.getValueAt(JTdetalle.getSelectedRow(), 2).toString());
                    
                    subtotal -= c2*c3;
                    subtotal = Math.rint(subtotal*100.0)/100.0; //deja al valor con dos decimales 
                    
                    total_descuento -= (c2 * c3) - c6;
                    total_descuento = Math.rint(total_descuento * 100.0) / 100.0; //deja al valor con dos decimales 

                    jl_num_det.setText("Detalles: " + num_det);
                    jl_total.setText("Total: $" + total);
                    jlSubtotal.setText("$" + subtotal);
                    jlTotal_descuento.setText("- $" + total_descuento);
                    jlTotal.setText("$" + (total));

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
                    
                    if (tabla_detalle.getRowCount() == 0) {
                        JBcrear_factura.setEnabled(false);
                    }
                    JOptionPane.showMessageDialog(null, "¡Producto '" + codigo_pro + "' removido!", null, JOptionPane.INFORMATION_MESSAGE);
                    
                }
            }
        }
    }//GEN-LAST:event_JTdetalleMouseClicked

    private void JBcrear_factura1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBcrear_factura1ActionPerformed
        reiniciar_factura();
    }//GEN-LAST:event_JBcrear_factura1ActionPerformed

    private void fec_encMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fec_encMouseClicked
        fec_enc.setText(fechas.transformar(fechas.obtener()));
        fec_enc.setBackground(Color.green);
        enc_cedula.setEnabled(true);
    }//GEN-LAST:event_fec_encMouseClicked

    private void fec_encActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fec_encActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fec_encActionPerformed

    private void jcBuscar_encItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_encItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_encItemStateChanged

    private void jtBuscar_encMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_encMouseClicked
        if (jtBuscar_enc.getText().equals("Buscar")) {
            jtBuscar_enc.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_encMouseClicked

    private void jtBuscar_encKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_encKeyPressed
        if (jtBuscar_enc.getText().equals("Buscar")) {
            jtBuscar_enc.setText("");
            lim_enc.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_encKeyPressed

    private void jtBuscar_encKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_encKeyReleased
        if (!jtBuscar_enc.getText().equals("")) {
            buscar(JTenc_fac, jtBuscar_enc, res_enc, enc, jcBuscar_enc);
        } else {
            lim_enc.setVisible(false);
            jtBuscar_enc.setText("Buscar");
            jtBuscar_enc.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_encKeyReleased

    private void jtBuscar_encKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_encKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_encKeyTyped

    private void lim_encMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_encMouseClicked
        lim_enc.setVisible(false);
        jtBuscar_enc.setText("Buscar");
        jtBuscar_enc.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_encMouseClicked

    private void jlAgregar_facMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlAgregar_facMouseClicked
        reiniciar_factura();
        INICIO.setSelectedIndex(0);
    }//GEN-LAST:event_jlAgregar_facMouseClicked

    private void VF_DETALLESMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VF_DETALLESMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_VF_DETALLESMouseClicked

    private void jcBuscar_detItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_detItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_detItemStateChanged

    private void jtBuscar_detMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_detMouseClicked
         if (jtBuscar_det.getText().equals("Buscar")) {
            jtBuscar_det.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_detMouseClicked

    private void jtBuscar_detKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_detKeyPressed
        if (jtBuscar_det.getText().equals("Buscar")) {
            jtBuscar_det.setText("");
            lim_det.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_detKeyPressed

    private void jtBuscar_detKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_detKeyReleased
        if (!jtBuscar_det.getText().equals("")) {
            buscar(JTdet_fac, jtBuscar_det, res_det, det, jcBuscar_det);
        } else {
            lim_det.setVisible(false);
            jtBuscar_det.setText("Buscar");
            jtBuscar_det.select(0, 0);
            visualizar();
        }
    }//GEN-LAST:event_jtBuscar_detKeyReleased

    private void jtBuscar_detKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_detKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_detKeyTyped

    private void lim_detMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_detMouseClicked
        lim_det.setVisible(false);
        jtBuscar_det.setText("Buscar");
        jtBuscar_det.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_detMouseClicked

    private void jbEliminar_cat1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_cat1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_cat1MouseEntered

    private void jbEliminar_cat1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_cat1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_cat1MouseExited

    private void jbEliminar_cat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_cat1ActionPerformed
     
        if (!VF_CODIGO.getText().equals(" ")) {
            eliminar(7);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jbEliminar_cat1ActionPerformed

    private void subir_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subir_1MouseClicked
        jScrollPanew.getVerticalScrollBar().setValue(0);
    }//GEN-LAST:event_subir_1MouseClicked

    private void jb_Eliminar_pagMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_pagMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_pagMouseEntered

    private void jb_Eliminar_pagMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jb_Eliminar_pagMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jb_Eliminar_pagMouseExited

    private void jb_Eliminar_pagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_Eliminar_pagActionPerformed
        if (!jlCodigo_pag.getText().equals(" ")) {
            eliminar(8);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_jb_Eliminar_pagActionPerformed

    private void jbModificar_pagMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_pagMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_pagMouseEntered

    private void jbModificar_pagMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_pagMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_pagMouseExited

    private void jbModificar_pagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_pagActionPerformed
//        if (!jlCodigo_pag.getText().equals(" ")) {
//            modo_prov = "pagos_prov";
//            JFpagos.forma = "modificar";
//            JFpagos.cambiar_diseño();
//            JFpag.llenar(Integer.parseInt(jlCodigo_pag.getText()));
//        }else {
//            getToolkit().beep();
//            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
//        }
    }//GEN-LAST:event_jbModificar_pagActionPerformed

    private void jbRegistrar_pagMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_pagMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_pagMouseEntered

    private void jbRegistrar_pagMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_pagMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_pagMouseExited

    private void jbRegistrar_pagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_pagActionPerformed
//        modo_prov = "pagos_prov";
//        JFpagos.forma = "registrar";
//        JFpagos.cambiar_diseño();
//        JFpagos.limpiar();
//        JFpag.setVisible(true);
    }//GEN-LAST:event_jbRegistrar_pagActionPerformed

    private void jcBuscar_pagItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_pagItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_pagItemStateChanged

    private void jtBuscar_pagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_pagMouseClicked
        if (jtBuscar_pag.getText().equals("Buscar")) {
            jtBuscar_pag.select(0, 0);
        }
    }//GEN-LAST:event_jtBuscar_pagMouseClicked

    private void jtBuscar_pagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtBuscar_pagActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_pagActionPerformed

    private void jtBuscar_pagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_pagKeyPressed
        if (jtBuscar_pag.getText().equals("Buscar")) {
            jtBuscar_pag.setText("");
            lim_pag.setVisible(true);
        }
    }//GEN-LAST:event_jtBuscar_pagKeyPressed

    private void jtBuscar_pagKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_pagKeyReleased
//        if (!jtBuscar_pag.getText().equals("")) {
//            buscar(JTpagos, jtBuscar_pag, res_num_pag, pag, jcBuscar_pag);
//        } else {
//            lim_pag.setVisible(false);
//            jtBuscar_pag.setText("Buscar");
//            jtBuscar_pag.select(0, 0);
//            visualizar();
//        }
    }//GEN-LAST:event_jtBuscar_pagKeyReleased

    private void jtBuscar_pagKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_pagKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_pagKeyTyped

    private void lim_pagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_pagMouseClicked
        lim_pag.setVisible(false);
        jtBuscar_pag.setText("Buscar");
        jtBuscar_pag.select(0, 0);
        visualizar();
    }//GEN-LAST:event_lim_pagMouseClicked

    private void SALIRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SALIRMouseClicked
        this.dispose();
        JFlogin sesión = new JFlogin();
        sesión.setVisible(true);
    }//GEN-LAST:event_SALIRMouseClicked

    private void jbEnviar_cat1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cat1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_cat1MouseEntered

    private void jbEnviar_cat1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEnviar_cat1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_cat1MouseExited

    private void jbEnviar_cat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEnviar_cat1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEnviar_cat1ActionPerformed

    private void jbEliminar_cat2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_cat2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_cat2MouseEntered

    private void jbEliminar_cat2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbEliminar_cat2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_cat2MouseExited

    private void jbEliminar_cat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEliminar_cat2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbEliminar_cat2ActionPerformed

    private void jbModificar_cat1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cat1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_cat1MouseEntered

    private void jbModificar_cat1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbModificar_cat1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_cat1MouseExited

    private void jbModificar_cat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbModificar_cat1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbModificar_cat1ActionPerformed

    private void jbRegistrar_cat1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cat1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_cat1MouseEntered

    private void jbRegistrar_cat1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbRegistrar_cat1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_cat1MouseExited

    private void jbRegistrar_cat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRegistrar_cat1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbRegistrar_cat1ActionPerformed

    private void jcBuscar_genItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_genItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_genItemStateChanged

    private void jtBuscar_genMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_genMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_genMouseClicked

    private void jtBuscar_genKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_genKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_genKeyPressed

    private void jtBuscar_genKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_genKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_genKeyReleased

    private void jtBuscar_genKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_genKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_genKeyTyped

    private void lim_cat1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_cat1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lim_cat1MouseClicked

    private void jcBuscar_enc1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_enc1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_enc1ItemStateChanged

    private void jtBuscar_enc1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_enc1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc1MouseClicked

    private void jtBuscar_enc1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc1KeyPressed

    private void jtBuscar_enc1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc1KeyReleased

    private void jtBuscar_enc1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc1KeyTyped

    private void lim_cliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_cliMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lim_cliMouseClicked

    private void jcBuscar_enc3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_enc3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_enc3ItemStateChanged

    private void jtBuscar_enc3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_enc3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc3MouseClicked

    private void jtBuscar_enc3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc3KeyPressed

    private void jtBuscar_enc3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc3KeyReleased

    private void jtBuscar_enc3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc3KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc3KeyTyped

    private void lim_enc3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_enc3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lim_enc3MouseClicked

    private void eliminar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminar_cliActionPerformed
        if (JPcli.isVisible()) {
            eliminar(3);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_eliminar_cliActionPerformed

    private void eliminar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminar_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminar_cliMouseExited

    private void eliminar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminar_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminar_cliMouseEntered

    private void registrar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrar_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_registrar_cliMouseEntered

    private void registrar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrar_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_registrar_cliMouseExited

    private void registrar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrar_cliActionPerformed
        JFcliente.forma = "registrar";
        JFcliente.cambiar_diseño();
        JFcliente.limpiar();
        JFcli.setVisible(true);
    }//GEN-LAST:event_registrar_cliActionPerformed

    private void modificar_cliMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificar_cliMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_cliMouseEntered

    private void modificar_cliMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificar_cliMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_cliMouseExited

    private void modificar_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_cliActionPerformed
        if (JPcli.isVisible()) {
            JFcliente.forma = "modificar";
            JFcliente.cambiar_diseño();
            JFcli.llenar(cedula_cli.getText());
            reiniciar_factura(); //por precaución
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_modificar_cliActionPerformed

    private void jtBuscar_enc4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtBuscar_enc4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc4MouseClicked

    private void jtBuscar_enc4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc4KeyPressed

    private void jtBuscar_enc4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc4KeyReleased

    private void jtBuscar_enc4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtBuscar_enc4KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtBuscar_enc4KeyTyped

    private void jcBuscar_enc4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcBuscar_enc4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcBuscar_enc4ItemStateChanged

    private void lim_empMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lim_empMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lim_empMouseClicked

    private void eliminar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminar_cli1MouseEntered

    private void eliminar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eliminar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminar_cli1MouseExited

    private void eliminar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminar_cli1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eliminar_cli1ActionPerformed

    private void modificar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_cli1MouseEntered

    private void modificar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modificar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_cli1MouseExited

    private void modificar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificar_cli1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modificar_cli1ActionPerformed

    private void registrar_cli1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrar_cli1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_registrar_cli1MouseEntered

    private void registrar_cli1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrar_cli1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_registrar_cli1MouseExited

    private void registrar_cli1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrar_cli1ActionPerformed
        
    }//GEN-LAST:event_registrar_cli1ActionPerformed

    private void jLabel86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel86MouseClicked
        JSpersonas.getVerticalScrollBar().setValue(0);
    }//GEN-LAST:event_jLabel86MouseClicked

    
    public void InsertarIcono(JButton bot, String ruta){ //insertar icono en boton:
        bot.setIcon(new javax.swing.ImageIcon(getClass().getResource(ruta)));
    }
    
    
    //método para dar click o doble click en las tablas:
    public void seleccionar() {
//        JTcategorias.addMouseListener(new MouseAdapter() { //categorias(1)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlNombre_cat.setText(JTcategorias.getValueAt(JTcategorias.getSelectedRow(), 0).toString());
//                    jtaDescripcion_cat.setText(JTcategorias.getValueAt(JTcategorias.getSelectedRow(), 1).toString());
//
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    JFproducto.jt_categoria.setText(jlNombre_cat.getText());
//                    SISTEMA.MENU.setSelectedIndex(6);
//                    JFpro.setVisible(true);
//
//                }
//            }
//        });
//        JTciudades.addMouseListener(new MouseAdapter() { //ciudades(2)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlCodigo_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 0).toString());
//                    jlNombre_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 1).toString());
//                    jlProvincia_ciu.setText(JTciudades.getValueAt(JTciudades.getSelectedRow(), 2).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    JFproveedor.jt_ciudad.setText(jlCodigo_ciu.getText());
//                    SISTEMA.MENU.setSelectedIndex(7);
//                    JFprov.setVisible(true);
//                }
//            }
//        });
//        JTclientes.addMouseListener(new MouseAdapter() { //clientes(3)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlCedula_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 0).toString());
//                    jlNombre_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 1).toString());
//                    jlApellido_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 2).toString());
//                    jlNac_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 3).toString());
//                    jlGenero_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 4).toString());
//                    jlTelefono_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 5).toString());
//                    jlCorreo_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 6).toString());
//                    jlDireccion_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 7).toString());
//                    jlDescuento_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 8).toString());
//                    jlReg_cli.setText(JTclientes.getValueAt(JTclientes.getSelectedRow(), 9).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    if (fec_enc.getText().equals("")) {
//                        JOptionPane.showMessageDialog(null, "¡Primero genere una fecha antes enviar clientes!");
//
//                    } else {
//                        base.abrir();
//                        Cliente c = new Cliente(null, jlCedula_cli.getText(), null, null, null, null, null, null, null, null);
//                        resultado = base.gettear(c);
//                        if (!resultado.isEmpty()) {
//                            c = (Cliente) resultado.next();
//                            enc_cedula.setText(c.getCedula());
//                            enc_cedula.setBackground(Color.green);
//                            JBseleccionar_pro.setEnabled(true);
//                            enc_nombre_apellido.setText(c.getNombre() + " " + c.getApellido());
//                            enc_direccion.setText(c.getDireccion());
//                            enc_telefono.setText(c.getTelefono());
//                            enc_correo.setText(c.getCorreo());
//                            Descuento d = new Descuento(c.getDescuento(), 0);
//                            resultado = base.gettear(d);
//                            d = (Descuento) resultado.next();
//                            descuento = d.getPorcentaje();
//
//                            MENU.setSelectedIndex(0);
//                            INICIO.setSelectedIndex(0);
//                        }
//                        base.cerrar();
//                    }
//                }
//            }
//        });
//        JTdescuentos.addMouseListener(new MouseAdapter() { //descuentos(4)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlNombre_des.setText(JTdescuentos.getValueAt(JTdescuentos.getSelectedRow(), 0).toString());
//                    jlPorcentaje_des.setText(JTdescuentos.getValueAt(JTdescuentos.getSelectedRow(), 1).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    JFcliente.descuento.setText(jlNombre_des.getText());
//                    SISTEMA.MENU.setSelectedIndex(3);
//                    JFcli.setVisible(true);
//                }
//            }
//        });
//        JTempleados.addMouseListener(new MouseAdapter() { //empleados(6)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlCedula_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 0).toString());
//                    jlNombre_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 1).toString());
//                    jlApellido_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 2).toString());
//                    jlNac_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 3).toString());
//                    jlGenero_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 4).toString());
//                    jlTelefono_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 5).toString());
//                    jlCorreo_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 6).toString());
//                    jlDireccion_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 7).toString());
//                    jlSueldo_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 8).toString());
//                    jlReg_emp.setText(JTempleados.getValueAt(JTempleados.getSelectedRow(), 9).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    JFpagos.jt_empleado.setText(jlCedula_emp.getText());
//                    MENU.setSelectedIndex(0);
//                    INICIO.setSelectedIndex(2);
//                    JFpag.setVisible(true);
//                }
//            }
//        });

//        JTenc_fac.addMouseListener(new MouseAdapter() { //encabezados(7)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    VF_CODIGO.setText(JTenc_fac.getValueAt(JTenc_fac.getSelectedRow(), 0).toString());
//                    VF_CEDULA.setText(JTenc_fac.getValueAt(JTenc_fac.getSelectedRow(), 1).toString());
//                    VF_FECHA.setText(JTenc_fac.getValueAt(JTenc_fac.getSelectedRow(), 2).toString());
//                    VF_TOTAL.setText("$" + JTenc_fac.getValueAt(JTenc_fac.getSelectedRow(), 3).toString());
//                    base.abrir();
//                    Cliente c = new Cliente(null, VF_CEDULA.getText(), null, null, null, null, null, null, null, null);
//                    resultado = base.gettear(c);
//                    c = (Cliente) resultado.next();
//
//                    VF_NOMBRE_APELLIDO.setText(c.getNombre() + " " + c.getApellido());
//                    VF_DIRECCION.setText(c.getDireccion());
//                    VF_TELEFONO.setText(c.getTelefono());
//                    VF_CORREO.setText(c.getCorreo());
//                    //cargar detalles pertenecientes a la factura
//                    String[] colum_det = {"Código","C. Producto", "Cantidad", "Subtotal", "C. Factura"};
//                    tabla = new DefaultTableModel(null, colum_det);
//                    Detalle_fac DF = new Detalle_fac(0, 0, 0, 0, Integer.parseInt(VF_CODIGO.getText()));
//                    resultado = base.gettear(DF);                  
//                    for (int i = 0; i < resultado.size(); i++) {
//                        
//                        DF = (Detalle_fac) resultado.next();
//                        
//                      
//                        
//                        tabla.addRow(new Object[]{DF.getCodigo(), DF.getCodigo_pro(), DF.getCant(), DF.getSubtotal(), DF.getCodigo_fac()});
//
//                    }
//                    VF_DETALLES.setModel(tabla);
//                    base.cerrar();
//                }
//            }
//        });

//        JTpagos.addMouseListener(new MouseAdapter() { //pagos a prov(8)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlCodigo_pag.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 0).toString());
//                    jlPrecio_pag.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 1).toString());
//                    jlceduempleado.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 2).toString());
//                    jlProveedor_pag.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 3).toString());
//                    jtaDescripcion_pag.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 4).toString());
//                    jlReg_pag.setText(JTpagos.getValueAt(JTpagos.getSelectedRow(), 5).toString());
//
//                    base.abrir();
//                    Empleado c = new Empleado(0, jlceduempleado.getText(), null, null, null, null, null, null, null, null);
//                    resultado = base.gettear(c);
//                    c = (Empleado) resultado.next();
//                    jlempleado_nom.setText(c.getNombre() + " " + c.getApellido());
//                    base.cerrar();
//                }
//
//            }
//        });

        JTproductos.addMouseListener(new MouseAdapter() { //productos(9)
            @Override
            public void mousePressed(MouseEvent Mouse_evt) {
                if (Mouse_evt.getClickCount() == 1) {
                    jlCodigo_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 0).toString());
                    jlNombre_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 1).toString());
                    jlPrecio_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 2).toString());
                    jlExistencias_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 3).toString());
                    jlCategoria_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 4).toString());
                    jlProveedor_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 5).toString());
                    jlReg_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 6).toString());
                }
                if (Mouse_evt.getClickCount() == 2) {
                    if (enc_cedula.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "¡Primero selecione un cliente antes enviar productos!");
                    } else {
                        int existencias = Integer.parseInt(jlExistencias_pro.getText());
                        if (existencias > 0) {
                            boolean repetido = false;
                            for (int i = 0; i < detalles.size(); i++) {
                                if (detalles.get(i).equals(jlCodigo_pro.getText())) {
                                    repetido = true;
                                    break;
                                }
                            }
                            if (repetido) {
                                JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
                            } else {
                                try {
                                    int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
                                    if (cantidad > 0 && cantidad <= Integer.parseInt(jlExistencias_pro.getText())) {
                                        Double xprecio = Double.valueOf(jlPrecio_pro.getText());
                                        JTdetalle.setDefaultRenderer(Object.class, new BotonTabla());
                                        Object detalle[] = {jlCodigo_pro.getText(), jlNombre_pro.getText(), cantidad, xprecio, descuento + "%", xprecio - ((descuento * xprecio) / 100), Math.round((cantidad * (xprecio - ((descuento * xprecio) / 100))) * 100.0) / 100.0, boton1};

                                        detalles.add(jlCodigo_pro.getText());
                                        //-------------- Agrega un detalle a la tabla
                                        tabla_detalle.addRow(detalle);
                                        JTdetalle.setModel(tabla_detalle);
                                        //Confirmar_venta.setEnabled(true);
                                        //--------------- Actualiza variables
                                        num_det++;

                                        subtotal += xprecio * cantidad;
                                        subtotal = Math.rint(subtotal * 100.0) / 100.0; //lo deja con 2 decimales

                                        double xsubtotal = cantidad * (xprecio - ((descuento * xprecio) / 100));
                                        total += xsubtotal;
                                        total = (Math.rint(total * 100.0) / 100.0); //lo deja con 2 decimales

                                        total_descuento += (xprecio * cantidad) - (xsubtotal);
                                        total_descuento = (Math.rint(total_descuento * 100.0) / 100.0); //lo deja con 2 decimales

                                        jl_num_det.setText("Detalles: " + num_det);
                                        jl_total.setText("Total: $" + total);
                                        jlSubtotal.setText("$" + subtotal);
                                        jlTotal_descuento.setText("- $" + total_descuento);
                                        jlTotal.setText("$" + (total));

                                        //---------- Redireccionar
                                        JBcrear_factura.setEnabled(true);
                                        MENU.setSelectedIndex(0);
                                        INICIO.setSelectedIndex(0);
                                    } else {
                                        if (cantidad > Integer.parseInt(jlExistencias_pro.getText())) {
                                            JOptionPane.showMessageDialog(null, "¡Solo existen '" + jlExistencias_pro.getText() + "' de este producto!", null, JOptionPane.WARNING_MESSAGE);
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
//        JTproveedores.addMouseListener(new MouseAdapter() { //10 proveedor
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlRUC.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 0).toString());
//                    jlNombre.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 1).toString());
//                    jlCiudad.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 2).toString());
//                    jlTelefono.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 3).toString());
//                    jlEmail.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 4).toString());
//                    jlFecha_reg.setText(JTproveedores.getValueAt(JTproveedores.getSelectedRow(), 5).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    if (modo_prov.equals("producto")) {
//                        JFproducto.jt_proveedor.setText(jlRUC.getText());
//                        SISTEMA.MENU.setSelectedIndex(6);
//                        JFpro.setVisible(true);
//                    } else {
//                        JFpagos.jt_proveedor.setText(jlRUC.getText());
//                        SISTEMA.MENU.setSelectedIndex(0);
//                        INICIO.setSelectedIndex(2);
//                        JFpag.setVisible(true);
//                    }
//                }
//            }
//        });

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
            FECHA_HORA.setText("Ecuador, " + fechas.transformar(fechas.obtener()) + " - " + hora + ":" + minutos + ":" + segundos);
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
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SISTEMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new SISTEMA().setVisible(true);
            }
        });
      
    }
     public void ocultar(int num, boolean visible) {
        switch (num) {
            case 1: JPcli.setVisible(visible);break;
        }
    }

    public void resumen() {
        //limpiar_resumen();
        int clientes_con_fac = 0;
        int clientes_sin_fac = 0;
        
        //CLIENTES CON MÁS COMPRAS:
//        ArrayList clientes_top = new ArrayList<>();
//        Cliente c = new Cliente(null, null, null, null, null, null, null, null, null, null);
//        resultado = base.gettear(c);
//        for (int i = 0; i < resultado.size(); i++) {
//            c = (Cliente) resultado.next();
//            Encabezado_fac fac_cli = new Encabezado_fac(0, c.getCedula(), null, 0, "ACTIVO");
//            ObjectSet res = base.gettear(fac_cli);
//            if (!res.isEmpty()) {
//                clientes_con_fac++;
//                clientes_top.add(new Clientes_Facturas((c.getCedula()), c.getNombre(), c.getApellido(), res.size()));
//            } else {
//                clientes_sin_fac++;
//            }
//        } //OREDENA DE MAYOR A MENOR DEPENDIENDO EL NÚMERO DE COMPRAS(FACTURAS "ACTIVAS")
//        Collections.sort(clientes_top, (Clientes_Facturas c1, Clientes_Facturas c2) -> Integer.valueOf(c2.getNum_fac()).compareTo(c1.getNum_fac()));
//        
//        for (int i = 1; i <= clientes_top.size(); i++) {
//            Clientes_Facturas x = (Clientes_Facturas) clientes_top.get(i - 1);
//            switch (i) {
//                case 1:
//                    R1_A1.setText(x.getCedula());
//                    R1_B1.setText(x.getNombre() + " " + x.getApellido());
//                    R1_C1.setText("" + x.getNum_fac());
//                    break;
//                case 2:
//                    R1_A2.setText(x.getCedula());
//                    R1_B2.setText(x.getNombre() + " " + x.getApellido());
//                    R1_C2.setText("" + x.getNum_fac());
//                    break;
//                case 3:
//                    R1_A3.setText(x.getCedula());
//                    R1_B3.setText(x.getNombre() + " " + x.getApellido());
//                    R1_C3.setText("" + x.getNum_fac());
//                    break;
//                case 4:
//                    R1_A4.setText(x.getCedula());
//                    R1_B4.setText(x.getNombre() + " " + x.getApellido());
//                    R1_C4.setText("" + x.getNum_fac());
//                    break;
//                case 5:
//                    R1_A5.setText(x.getCedula());
//                    R1_B5.setText(x.getNombre() + " " + x.getApellido());
//                    R1_C5.setText("" + x.getNum_fac());
//                    break;
//            }
//        }
//        
//        //PRODUCTOS MÁS VENDIDOS:
//        ArrayList productos_top = new ArrayList<>();
//        
//        Producto p = new Producto(0, null, 0, 0, null, null, null, null);
//        resultado = base.gettear(p);
//
//        for (int i = 0; i < resultado.size(); i++) {
//            int ventas = 0;
//            p = (Producto)resultado.next();
//            Encabezado_fac fac_act = new Encabezado_fac(0, null, null, 0, "ACTIVO");
//            ObjectSet res1 = base.gettear(fac_act);
//            for (int j = 0; j < res1.size(); j++) {
//                fac_act = (Encabezado_fac) res1.next();
//                Detalle_fac det_pro = new Detalle_fac(0, p.getCodigo(), 0, 0, fac_act.getCodigo());
//                ObjectSet res = base.gettear(det_pro);
//                if (!res.isEmpty()) {
//                   ventas++; 
//                }
//            }
//            if (ventas >0) {
//                productos_top.add(new Productos_Detalles(p.getCodigo(), p.getNombre(), ventas));
//            }
//            
//        }
//        
//        //OREDENA DE MAYOR A MENOR DEPENDIENDO EL NÚMERO DE VENTAS(DETALLES)
//        Collections.sort(productos_top, (Productos_Detalles p1, Productos_Detalles p2) -> Integer.valueOf(p2.getVentas()).compareTo(p1.getVentas()));
//        for (int i = 1; i <= productos_top.size(); i++) {
//            Productos_Detalles x = (Productos_Detalles) productos_top.get(i - 1);
//            switch (i) {
//                case 1:
//                    R2_A1.setText(""+x.getCodigo());
//                    R2_B1.setText(x.getNombre());
//                    R2_C1.setText("" + x.getVentas());
//                    break;
//                case 2:
//                    R2_A2.setText(""+x.getCodigo());
//                    R2_B2.setText(x.getNombre());
//                    R2_C2.setText("" + x.getVentas());
//                    break;
//                case 3:
//                    R2_A3.setText(""+x.getCodigo());
//                    R2_B3.setText(x.getNombre());
//                    R2_C3.setText("" + x.getVentas());
//                    break;
//                case 4:
//                    R2_A4.setText(""+x.getCodigo());
//                    R2_B4.setText(x.getNombre());
//                    R2_C4.setText("" + x.getVentas());
//                    break;
//                case 5:
//                    R2_A5.setText(""+x.getCodigo());
//                    R2_B5.setText(x.getNombre());
//                    R2_C5.setText("" + x.getVentas());
//                    break;
//            }
//        }
//
//        //ESTADÍSTICAS GENERALES
//        int facturas = 0;
//        double acum = 0;
//        double acum_total = 0;
//        R3_A1.setText("" + clientes_con_fac);
//        R3_B1.setText("" + clientes_sin_fac);
//        //$ total de facturas activas:
//        Encabezado_fac fac_act = new Encabezado_fac(0, null, null, 0, "ACTIVO");
//        resultado = base.gettear(fac_act);
//        for (int i = 0; i < resultado.size(); i++) {
//            fac_act = (Encabezado_fac) resultado.next();
//            acum += fac_act.getTotal();
//        }
//        acum = Math.round(acum * 100.0) / 100.0; //lo deja con 2 decimales
//
//        R3_T1.setText("$ de " + resultado.size() + " F. Activas");
//        R3_C1.setText("$" + acum);
//        facturas = resultado.size();
//        acum_total = acum;
//        //$ total de facturas incactivas:
//        acum = 0;
//        Encabezado_fac fac_inac = new Encabezado_fac(0, null, null, 0, "INACTIVO");
//        resultado = base.gettear(fac_inac);
//        for (int i = 0; i < resultado.size(); i++) {
//            fac_inac = (Encabezado_fac) resultado.next();
//            acum += fac_inac.getTotal();
//        }
//        acum = Math.round(acum * 100.0) / 100.0; //lo deja con 2 decimales
//        R3_T2.setText("$ de " + resultado.size() + " F. Inactivas");
//        R3_D1.setText("$" + acum);
//        facturas += resultado.size();
//        acum_total += acum;
//        acum_total = Math.round(acum_total * 100.0) / 100.0; //lo deja con 2 decimales
//        //$ total de facturas:
//        R3_T3.setText("$ de " + facturas + " Facturas");
//        R3_E1.setText("$" + acum_total);
//    }
//    
//    public void limpiar_resumen(){
//        //Tabla R1
//        R1_A1.setText(null);
//        R1_B1.setText(null);
//        R1_C1.setText(null);
//        R1_A2.setText(null);
//        R1_B2.setText(null);
//        R1_C2.setText(null);
//        R1_A3.setText(null);
//        R1_B3.setText(null);
//        R1_C3.setText(null);
//        R1_A4.setText(null);
//        R1_B4.setText(null);
//        R1_C4.setText(null);
//        R1_A5.setText(null);
//        R1_B5.setText(null);
//        R1_C5.setText(null);
//        //Tabla R2
//        R2_A1.setText(null);
//        R2_B1.setText(null);
//        R2_C1.setText(null);
//        R2_A2.setText(null);
//        R2_B2.setText(null);
//        R2_C2.setText(null);
//        R2_A3.setText(null);
//        R2_B3.setText(null);
//        R2_C3.setText(null);
//        R2_A4.setText(null);
//        R2_B4.setText(null);
//        R2_C4.setText(null);
//        R2_A5.setText(null);
//        R2_B5.setText(null);
//        R2_C5.setText(null);
//        //Tabla R3
//        R3_A1.setText(null);
//        R3_B1.setText(null);
//        R3_C1.setText(null);
//        R3_D1.setText(null);
    }
//    
//    //PARA ELIMINAR FACTURAS Y DETALLES PARA SIEMPRE (inrrecuperable):
//    Encabezado_fac enca = new Encabezado_fac(0, null, null, 0, null);
//        resultado = base.gettear(enca);
//        for (int i = 0; i < resultado.size(); i++) {
//            enca = (Encabezado_fac) resultado.next();
//            base.eliminar(enca);
//            Detalle_fac dtf = new Detalle_fac(0, 0, 0, 0,enca.getCodigo());
//            ObjectSet rdf = base.gettear(dtf);
//            for (int j = 0; j < rdf.size(); j++) {
//                dtf = (Detalle_fac)rdf.next();
//                base.eliminar(dtf);
//            }
//        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FECHA_HORA;
    private javax.swing.JLabel FONDO;
    private javax.swing.JTabbedPane INICIO;
    private javax.swing.JButton JBcrear_factura;
    private javax.swing.JButton JBcrear_factura1;
    public transient javax.swing.JButton JBseleccionar_pro;
    private javax.swing.JPanel JPcategorias;
    private javax.swing.JPanel JPciudades;
    private javax.swing.JPanel JPcli;
    private javax.swing.JPanel JPdescuentos;
    private javax.swing.JPanel JPfactura;
    private javax.swing.JPanel JPgeneros;
    private javax.swing.JPanel JPpagos;
    private javax.swing.JPanel JPpersonas;
    private javax.swing.JPanel JPproductos;
    private javax.swing.JPanel JPproveedores;
    private javax.swing.JPanel JPventas;
    private javax.swing.JScrollPane JSpersonas;
    private javax.swing.JTable JTcategorias;
    private javax.swing.JTable JTciudades;
    private javax.swing.JTable JTcli;
    private javax.swing.JTable JTdescuentos;
    private javax.swing.JTable JTdet_fac;
    public transient javax.swing.JTable JTdetalle;
    private javax.swing.JTable JTemp;
    private javax.swing.JTable JTenc_fac;
    private javax.swing.JTable JTgeneros;
    public static javax.swing.JTable JTpagos;
    private javax.swing.JTable JTper;
    public static javax.swing.JTable JTproductos;
    public static javax.swing.JTable JTproveedores;
    public static javax.swing.JTabbedPane MENU;
    private javax.swing.JPanel R1;
    private javax.swing.JLabel R1_A1;
    private javax.swing.JLabel R1_A2;
    private javax.swing.JLabel R1_A3;
    private javax.swing.JLabel R1_A4;
    private javax.swing.JLabel R1_A5;
    private javax.swing.JLabel R1_B1;
    private javax.swing.JLabel R1_B2;
    private javax.swing.JLabel R1_B3;
    private javax.swing.JLabel R1_B4;
    private javax.swing.JLabel R1_B5;
    private javax.swing.JLabel R1_C1;
    private javax.swing.JLabel R1_C2;
    private javax.swing.JLabel R1_C3;
    private javax.swing.JLabel R1_C4;
    private javax.swing.JLabel R1_C5;
    private javax.swing.JPanel R2;
    private javax.swing.JLabel R2_A1;
    private javax.swing.JLabel R2_A2;
    private javax.swing.JLabel R2_A3;
    private javax.swing.JLabel R2_A4;
    private javax.swing.JLabel R2_A5;
    private javax.swing.JLabel R2_B1;
    private javax.swing.JLabel R2_B2;
    private javax.swing.JLabel R2_B3;
    private javax.swing.JLabel R2_B4;
    private javax.swing.JLabel R2_B5;
    private javax.swing.JLabel R2_C1;
    private javax.swing.JLabel R2_C2;
    private javax.swing.JLabel R2_C3;
    private javax.swing.JLabel R2_C4;
    private javax.swing.JLabel R2_C5;
    private javax.swing.JPanel R3;
    private javax.swing.JLabel R3_A1;
    private javax.swing.JLabel R3_B1;
    private javax.swing.JLabel R3_C1;
    private javax.swing.JLabel R3_D1;
    private javax.swing.JLabel R3_E1;
    private javax.swing.JLabel R3_T1;
    private javax.swing.JLabel R3_T2;
    private javax.swing.JLabel R3_T3;
    private javax.swing.JLabel SALIR;
    public static javax.swing.JLabel USUARIO;
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
    private javax.swing.JLabel apellido_cli;
    private javax.swing.JLabel apellido_emp;
    private javax.swing.JLabel cedula_cli;
    private javax.swing.JLabel cedula_emp;
    private javax.swing.JLabel celular_cli;
    private javax.swing.JLabel celular_emp;
    private javax.swing.JLabel ciudad_cli;
    private javax.swing.JLabel ciudad_emp;
    private javax.swing.JLabel clientes_con_factura11;
    private javax.swing.JLabel clientes_con_factura12;
    private javax.swing.JLabel clientes_con_factura13;
    private javax.swing.JLabel clientes_con_factura14;
    private javax.swing.JLabel clientes_con_factura15;
    private javax.swing.JLabel clientes_sin_factura11;
    private javax.swing.JLabel clientes_sin_factura12;
    private javax.swing.JLabel clientes_sin_factura13;
    private javax.swing.JLabel clientes_sin_factura14;
    private javax.swing.JLabel clientes_sin_factura15;
    private javax.swing.JLabel departamento_emp;
    private javax.swing.JLabel descuento_cli;
    private javax.swing.JLabel direccion_cli;
    private javax.swing.JLabel direccion_emp;
    private javax.swing.JButton eliminar_cli;
    private javax.swing.JButton eliminar_cli1;
    private javax.swing.JLabel email_cli;
    private javax.swing.JLabel email_emp;
    private javax.swing.JTextField enc_cedula;
    private javax.swing.JTextField enc_codigo;
    private javax.swing.JTextField enc_correo;
    private javax.swing.JTextField enc_direccion;
    private javax.swing.JTextField enc_nombre_apellido;
    private javax.swing.JTextField enc_telefono;
    private javax.swing.JTextField fec_enc;
    private javax.swing.JLabel fecha_nac_cli;
    private javax.swing.JLabel fecha_nac_emp;
    private javax.swing.JLabel fecha_reg_cli;
    private javax.swing.JLabel fecha_reg_emp;
    private javax.swing.JLabel id_cli;
    private javax.swing.JLabel id_emp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPanew;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JButton jbEliminar_cat;
    private javax.swing.JButton jbEliminar_cat1;
    private javax.swing.JButton jbEliminar_cat2;
    private javax.swing.JButton jbEliminar_ciu;
    private javax.swing.JButton jbEliminar_des;
    private javax.swing.JButton jbEnviar_cat;
    private javax.swing.JButton jbEnviar_cat1;
    private javax.swing.JButton jbEnviar_ciu;
    private javax.swing.JButton jbEnviar_des;
    private javax.swing.JButton jbEnviar_pro;
    private javax.swing.JButton jbEnviar_prov;
    private javax.swing.JButton jbModificar_cat;
    private javax.swing.JButton jbModificar_cat1;
    private javax.swing.JButton jbModificar_ciu;
    private javax.swing.JButton jbModificar_des;
    private javax.swing.JButton jbModificar_pag;
    private javax.swing.JButton jbModificar_pro;
    private javax.swing.JButton jbModificar_prov;
    private javax.swing.JButton jbRegistrar_cat;
    private javax.swing.JButton jbRegistrar_cat1;
    private javax.swing.JButton jbRegistrar_ciu;
    private javax.swing.JButton jbRegistrar_des;
    private javax.swing.JButton jbRegistrar_pag;
    private javax.swing.JButton jbRegistrar_pro;
    private javax.swing.JButton jbRegistrar_prov;
    private javax.swing.JButton jb_Eliminar_pag;
    private javax.swing.JButton jb_Eliminar_pro;
    private javax.swing.JButton jb_Eliminar_prov;
    private javax.swing.JComboBox<String> jcBuscar_cat;
    private javax.swing.JComboBox<String> jcBuscar_ciu;
    private javax.swing.JComboBox<String> jcBuscar_des;
    private javax.swing.JComboBox<String> jcBuscar_det;
    private javax.swing.JComboBox<String> jcBuscar_enc;
    private javax.swing.JComboBox<String> jcBuscar_enc1;
    private javax.swing.JComboBox<String> jcBuscar_enc3;
    private javax.swing.JComboBox<String> jcBuscar_enc4;
    private javax.swing.JComboBox<String> jcBuscar_gen;
    private javax.swing.JComboBox<String> jcBuscar_pag;
    private javax.swing.JComboBox<String> jcBuscar_pro;
    private javax.swing.JComboBox<String> jcBuscar_prov;
    private javax.swing.JLabel jlAgregar_fac;
    private javax.swing.JLabel jlC;
    private javax.swing.JLabel jlC1;
    private javax.swing.JLabel jlC4;
    private javax.swing.JLabel jlC5;
    private javax.swing.JLabel jlCategoria_pro;
    private javax.swing.JLabel jlCiudad;
    private javax.swing.JLabel jlCodigo_ciu;
    private javax.swing.JLabel jlCodigo_pag;
    private javax.swing.JLabel jlCodigo_pro;
    private javax.swing.JLabel jlE;
    private javax.swing.JLabel jlE15;
    private javax.swing.JLabel jlE16;
    private javax.swing.JLabel jlE18;
    private javax.swing.JLabel jlE19;
    private javax.swing.JLabel jlE20;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlExistencias_pro;
    private javax.swing.JLabel jlF;
    private javax.swing.JLabel jlF3;
    private javax.swing.JLabel jlF4;
    private javax.swing.JLabel jlFecha_reg;
    private javax.swing.JLabel jlGen_emp1;
    private javax.swing.JLabel jlN;
    private javax.swing.JLabel jlN1;
    private javax.swing.JLabel jlN3;
    private javax.swing.JLabel jlN4;
    private javax.swing.JLabel jlN6;
    private javax.swing.JLabel jlN7;
    private javax.swing.JLabel jlN8;
    private javax.swing.JLabel jlNombre;
    private javax.swing.JLabel jlNombre_cat;
    private javax.swing.JLabel jlNombre_cat1;
    private javax.swing.JLabel jlNombre_cat2;
    private javax.swing.JLabel jlNombre_ciu;
    private javax.swing.JLabel jlNombre_des;
    private javax.swing.JLabel jlNombre_pro;
    private javax.swing.JLabel jlPorcentaje_des;
    private javax.swing.JLabel jlPrecio_pag;
    private javax.swing.JLabel jlPrecio_pro;
    private javax.swing.JLabel jlProveedor_pag;
    private javax.swing.JLabel jlProveedor_pro;
    private javax.swing.JLabel jlProvincia_ciu;
    private javax.swing.JLabel jlR;
    private javax.swing.JLabel jlR1;
    private javax.swing.JLabel jlR3;
    private javax.swing.JLabel jlR4;
    private javax.swing.JLabel jlR6;
    private javax.swing.JLabel jlR7;
    private javax.swing.JLabel jlR8;
    private javax.swing.JLabel jlRUC;
    private javax.swing.JLabel jlReg_pag;
    private javax.swing.JLabel jlReg_pro;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlT;
    private javax.swing.JLabel jlTelefono;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlTotal_descuento;
    private javax.swing.JLabel jl_num_det;
    private javax.swing.JLabel jl_num_det1;
    private javax.swing.JLabel jl_num_det2;
    private javax.swing.JLabel jl_titulo12;
    private javax.swing.JLabel jl_titulo13;
    private javax.swing.JLabel jl_titulo14;
    private javax.swing.JLabel jl_titulo15;
    private javax.swing.JLabel jl_titulo16;
    private javax.swing.JLabel jl_titulo17;
    private javax.swing.JLabel jl_titulo19;
    private javax.swing.JLabel jl_titulo20;
    private javax.swing.JLabel jl_titulo5;
    private javax.swing.JLabel jl_titulo6;
    private javax.swing.JLabel jl_titulo7;
    private javax.swing.JLabel jl_titulo8;
    private javax.swing.JLabel jl_titulo9;
    private javax.swing.JLabel jl_total;
    private javax.swing.JLabel jl_total1;
    private javax.swing.JLabel jlceduempleado;
    private javax.swing.JLabel jlempleado_nom;
    public static javax.swing.JPanel jpDatos_cat;
    public static javax.swing.JPanel jpDatos_cat1;
    public static javax.swing.JPanel jpDatos_cat2;
    public static javax.swing.JPanel jpDatos_des;
    public static javax.swing.JPanel jpDatos_pro;
    public static javax.swing.JPanel jpDatos_pro1;
    public static javax.swing.JPanel jpDatos_prov;
    private javax.swing.JScrollPane jsTabla_cat;
    private javax.swing.JScrollPane jsTabla_cat1;
    private javax.swing.JScrollPane jsTabla_cat3;
    private javax.swing.JScrollPane jsTabla_cat6;
    private javax.swing.JScrollPane jsTabla_ciu;
    private javax.swing.JScrollPane jsTabla_ciu1;
    private javax.swing.JScrollPane jsTabla_ciu2;
    private javax.swing.JScrollPane jsTabla_ciu3;
    private javax.swing.JScrollPane jsTabla_ciu4;
    private javax.swing.JScrollPane jsTabla_ciu5;
    private javax.swing.JScrollPane jsTabla_ciu6;
    private javax.swing.JScrollPane jsTabla_des;
    private javax.swing.JScrollPane jsTabla_pro;
    private javax.swing.JScrollPane jsTabla_pro1;
    public static javax.swing.JTextField jtBuscar_cat;
    public static javax.swing.JTextField jtBuscar_ciu;
    public static javax.swing.JTextField jtBuscar_des;
    public static javax.swing.JTextField jtBuscar_det;
    public static javax.swing.JTextField jtBuscar_enc;
    public static javax.swing.JTextField jtBuscar_enc1;
    public static javax.swing.JTextField jtBuscar_enc3;
    public static javax.swing.JTextField jtBuscar_enc4;
    public static javax.swing.JTextField jtBuscar_gen;
    public static javax.swing.JTextField jtBuscar_pag;
    public static javax.swing.JTextField jtBuscar_pro;
    public static javax.swing.JTextField jtBuscar_prov;
    public static javax.swing.JTextArea jtaDescripcion_cat;
    public static javax.swing.JTextArea jtaDescripcion_pag;
    private javax.swing.JLabel lim_cat;
    private javax.swing.JLabel lim_cat1;
    private javax.swing.JLabel lim_ciu;
    private javax.swing.JLabel lim_cli;
    private javax.swing.JLabel lim_des;
    private javax.swing.JLabel lim_det;
    private javax.swing.JLabel lim_emp;
    private javax.swing.JLabel lim_enc;
    private javax.swing.JLabel lim_enc3;
    private javax.swing.JLabel lim_pag;
    private javax.swing.JLabel lim_pro;
    private javax.swing.JLabel lim_prov;
    private javax.swing.JButton modificar_cli;
    private javax.swing.JButton modificar_cli1;
    private javax.swing.JLabel nombre_cli;
    private javax.swing.JLabel nombre_emp;
    private javax.swing.JLabel puesto_emp;
    private javax.swing.JButton registrar_cli;
    private javax.swing.JButton registrar_cli1;
    private javax.swing.JLabel res_cat;
    private javax.swing.JLabel res_ciu;
    private javax.swing.JLabel res_cli;
    private javax.swing.JLabel res_des;
    private javax.swing.JLabel res_det;
    private javax.swing.JLabel res_emp;
    private javax.swing.JLabel res_enc;
    private javax.swing.JLabel res_gen;
    private javax.swing.JLabel res_num_pag;
    private javax.swing.JLabel res_per;
    private javax.swing.JLabel res_pro;
    private javax.swing.JLabel res_prov;
    private javax.swing.JLabel sexo_cli;
    private javax.swing.JLabel sexo_emp;
    public static javax.swing.JLabel sistema_titulo;
    private javax.swing.JLabel subir_1;
    private javax.swing.JLabel t_facturas_activas11;
    private javax.swing.JLabel t_facturas_activas12;
    private javax.swing.JLabel t_facturas_activas13;
    private javax.swing.JLabel t_facturas_activas14;
    private javax.swing.JLabel t_facturas_activas15;
    // End of variables declaration//GEN-END:variables
}
