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
//    rsscalelabel.RSScaleLabel.setScaleLabel(imagen_pro, rs.getString(3));
    
    //variables para consultas SQL:
    public static Connection con = null;
    public static ResultSet rs;
    public static PreparedStatement ps;
    public static String consulta = "";
    //variables que guardan el número de registros:
    public static int cat, ciu, cli, dep, des, det, emp, enc, fp, gen, iva, mar, pe, pag, per, pro, prov, provi, pue, suc;

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
//        lim_cat.setVisible(false);
//        lim_ciu.setVisible(false);
//        Lcli.setVisible(false);
//        lim_des.setVisible(false);
//        lim_det.setVisible(false);
//        Lemp.setVisible(false);
//        lim_enc.setVisible(false);
//        //lim_gas.setVisible(false);
//        lim_pro.setVisible(false);
//        lim_prov.setVisible(false);

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
                            JTcat.setModel(tabla);
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
                            JTciu.setModel(tabla);
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
                            JTdep.setModel(tabla);
                            dep = tabla.getRowCount();
                            res_dep.setText("Resultados: "+dep+" de "+dep);
                            break;
                        case 5://descuento
                            String[] c_des = {"ID", "NOMBRE", "PORCENTAJE"};
                            tabla = new DefaultTableModel(null, c_des);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "descuento");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            JTdes.setModel(tabla);
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
                            JTdet.setModel(tabla);
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
                            JTenc.setModel(tabla);
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
                            JTfp.setModel(tabla);
                            fp = tabla.getRowCount();
                            res_fp.setText("Resultados: "+fp+" de "+fp);
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
                            JTiva.setModel(tabla);
                            iva = tabla.getRowCount();
                            res_iva.setText("Resultados: "+iva+" de "+iva);
                            break;
                        case 12://marca
                            String[] c_mar = {"ID", "NOMBRE"};
                            tabla = new DefaultTableModel(null, c_mar);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "marca");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2)});
                            }
                            JTmar.setModel(tabla);
                            mar = tabla.getRowCount();
                            res_mar.setText("Resultados: "+mar+" de "+mar);
                            break;
                        case 13://pago_empleado
                            String[] c_pe = {"NUMERO", "ID_REMITENTE", "ID_DESTINATARIO", "TOTAL", "FECHA_REG"};
                            tabla = new DefaultTableModel(null, c_pe);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_empleado");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), fechas.transformar(rs.getDate(5))});
                            }
                            JTpe.setModel(tabla);
                            pe = tabla.getRowCount();
                            res_pe.setText("Resultados: "+pe+" de "+pe);
                            break;
                        case 14://pago_fac
                            String[] c_pf = {"NUMERO", "TOTAL_SIN_IVA", "ID_FOR", "ID_IVA", "TOTAL_MAS_IVA", "CODIGO_ENC"};
                            tabla = new DefaultTableModel(null, c_pf);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "pago_fac");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getDouble(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5), rs.getInt(6)});
                            }
                            JTpag.setModel(tabla);
                            pag = tabla.getRowCount();
                            res_pag.setText("Resultados: "+pag+" de "+pag);
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
                            String[] c_pro = {"CODIGO", "NOMBRE", "ID_MAR", "PRECIO", "EXIS_MAX", "EXIS_MIN", "STOK", "ID_CAT", "FECHA_REG", "RUC_PROV"};
                            tabla = new DefaultTableModel(null, c_pro);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "producto");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getInt(4),rs.getDouble(5),rs.getInt(6),
                                    rs.getInt(7),rs.getInt(8),rs.getInt(9),fechas.transformar(rs.getDate(10)),rs.getInt(11)});
                            }
                            JTpro.setModel(tabla);
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
                            JTprov.setModel(tabla);
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
                            JTprovi.setModel(tabla);
                            provi = tabla.getRowCount();
                            res_provi.setText("Resultados: "+provi+" de "+provi);
                            break;
                        case 19://puesto
                            String[] c_pue = {"ID", "NOMBRE", "SUELDO"};
                            tabla = new DefaultTableModel(null, c_pue);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "puesto");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getDouble(3)});
                            }
                            JTpue.setModel(tabla);
                            pue = tabla.getRowCount();
                            res_pue.setText("Resultados: "+pue+" de "+pue);
                            break;
                        case 20://sucursal
                            String[] c_suc = {"ID", "NOMBRE", "ID_CIU"};
                            tabla = new DefaultTableModel(null, c_suc);
                            ps = (PreparedStatement) con.prepareStatement(consulta + "sucursal");
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                tabla.addRow(new Object[]{rs.getInt(1), rs.getString(2),rs.getInt(3)});
                            }
                            JTsuc.setModel(tabla);
                            suc = tabla.getRowCount();
                            res_suc.setText("Resultados: "+suc+" de "+suc);
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
        JSfacturar.getVerticalScrollBar().setValue(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MENU = new javax.swing.JTabbedPane();
        INICIO = new javax.swing.JTabbedPane();
        JSfacturar = new javax.swing.JScrollPane();
        JPfacturar = new javax.swing.JPanel();
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
        JPpago_empleado = new javax.swing.JPanel();
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
        elim_pe = new javax.swing.JButton();
        mod_pe = new javax.swing.JButton();
        reg_pe = new javax.swing.JButton();
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
        mod_iva = new javax.swing.JButton();
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
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        id_dep = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        nombre_dep = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        descripcion_dep = new javax.swing.JTextArea();
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
        id_prov = new javax.swing.JLabel();
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
        encabezado = new javax.swing.JPanel();
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
        MENU.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        MENU.setMinimumSize(new java.awt.Dimension(980, 600));
        MENU.setPreferredSize(new java.awt.Dimension(980, 600));

        INICIO.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        INICIO.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        INICIO.setMinimumSize(new java.awt.Dimension(1075, 600));
        INICIO.setPreferredSize(new java.awt.Dimension(1075, 600));

        JSfacturar.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPfacturar.setBackground(new java.awt.Color(255, 255, 255));
        JPfacturar.setMaximumSize(new java.awt.Dimension(980, 1125));
        JPfacturar.setMinimumSize(new java.awt.Dimension(980, 1125));
        JPfacturar.setPreferredSize(new java.awt.Dimension(980, 1125));

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

        jLabel2.setText("Click para seleccionar o cambiar cliente...");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2))
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

        javax.swing.GroupLayout JPfacturarLayout = new javax.swing.GroupLayout(JPfacturar);
        JPfacturar.setLayout(JPfacturarLayout);
        JPfacturarLayout.setHorizontalGroup(
            JPfacturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfacturarLayout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(JPfacturarLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        JPfacturarLayout.setVerticalGroup(
            JPfacturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPfacturarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        JSfacturar.setViewportView(JPfacturar);

        INICIO.addTab("Facturar", JSfacturar);
        JSfacturar.getVerticalScrollBar().setUnitIncrement(16);

        JSventas.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JSventas.setPreferredSize(new java.awt.Dimension(1149, 702));

        JPventas.setBackground(new java.awt.Color(255, 255, 255));
        JPventas.setMaximumSize(new java.awt.Dimension(980, 1400));
        JPventas.setMinimumSize(new java.awt.Dimension(980, 1400));
        JPventas.setPreferredSize(new java.awt.Dimension(980, 1400));

        jsTabla_ciu2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTenc = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTenc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTenc.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JTdet = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdet.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCenc.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCenc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCenc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCencItemStateChanged(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("Buscar por");

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

        res_enc.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_enc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_enc.setText("Resultados: 0 de 0");

        jl_titulo6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo6.setText("Encabezados de factura");
        jl_titulo6.setIconTextGap(10);
        jl_titulo6.setVerifyInputWhenFocusTarget(false);

        jl_titulo8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo8.setText("Detalles de factura");
        jl_titulo8.setIconTextGap(10);
        jl_titulo8.setVerifyInputWhenFocusTarget(false);

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Buscar por");

        res_det.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_det.setText("Resultados: 0 de 0");

        JCdet.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Producto", "Cantidad", "Subtotal", "C. Factura" }));
        JCdet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdetItemStateChanged(evt);
            }
        });

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

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jl_titulo9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jl_titulo9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/resumen.png"))); // NOI18N
        jl_titulo9.setText("RESUMEN");
        jl_titulo9.setIconTextGap(10);
        jl_titulo9.setVerifyInputWhenFocusTarget(false);

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

        subir_1.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 14)); // NOI18N
        subir_1.setText("ir arriba");
        subir_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        subir_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subir_1MouseClicked(evt);
            }
        });

        jSeparator8.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        JTpag = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpag.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCpag.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Producto", "Cantidad", "Subtotal", "C. Factura" }));
        JCpag.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpagItemStateChanged(evt);
            }
        });

        jl_titulo10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo10.setText("Pagos de factura");
        jl_titulo10.setIconTextGap(10);
        jl_titulo10.setVerifyInputWhenFocusTarget(false);

        jLabel57.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Buscar por");

        res_pag.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pag.setText("Resultados: 0 de 0");

        jSeparator29.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator29.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator29.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout JPventasLayout = new javax.swing.GroupLayout(JPventas);
        JPventas.setLayout(JPventasLayout);
        JPventasLayout.setHorizontalGroup(
            JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPventasLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator8)
                            .addComponent(res_enc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jsTabla_ciu2)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addComponent(jl_titulo6)
                                .addGap(190, 190, 190)
                                .addComponent(jLabel43)
                                .addGap(6, 6, 6)
                                .addComponent(JCenc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(Benc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(3, 3, 3)
                        .addComponent(Lenc)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo8, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(res_det, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPventasLayout.createSequentialGroup()
                                        .addComponent(jLabel45)
                                        .addGap(6, 6, 6)
                                        .addComponent(JCdet, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(Bdet, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                    .addComponent(jsTabla_ciu3, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addComponent(Ldet)))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addComponent(res_pag, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jsTabla_ciu17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(jl_titulo10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(JPventasLayout.createSequentialGroup()
                                        .addComponent(jLabel57)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(JCpag, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Bpag, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(3, 3, 3)
                                .addComponent(Lpag)
                                .addContainerGap(44, Short.MAX_VALUE))))))
            .addGroup(JPventasLayout.createSequentialGroup()
                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(R1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(41, 41, 41)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(R2, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(R3, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGap(460, 460, 460)
                        .addComponent(subir_1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo9, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 877, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        JPventasLayout.setVerticalGroup(
            JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPventasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jl_titulo6)
                    .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Benc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Lenc, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPventasLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JCenc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(12, 12, 12)
                .addComponent(jsTabla_ciu2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(res_enc)
                .addGap(20, 20, 20)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPventasLayout.createSequentialGroup()
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addComponent(jl_titulo8)
                                .addGap(15, 15, 15)
                                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Bdet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Ldet, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(JPventasLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(JCdet, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(12, 12, 12)
                                .addComponent(res_det)
                                .addGap(5, 5, 5)
                                .addComponent(jsTabla_ciu3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addComponent(jl_titulo10)
                                .addGap(15, 15, 15)
                                .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Bpag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Lpag, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(JPventasLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(JCpag, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(12, 12, 12)
                                .addComponent(res_pag)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jsTabla_ciu17, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jl_titulo9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(R1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(R2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60)
                        .addGroup(JPventasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPventasLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(R3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(41, 41, 41)
                        .addComponent(subir_1))
                    .addComponent(jSeparator29, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        JSventas.setViewportView(JPventas);

        INICIO.addTab("Ventas", JSventas);
        JSventas.getVerticalScrollBar().setUnitIncrement(16);

        JPpago_empleado.setBackground(new java.awt.Color(252, 240, 219));
        JPpago_empleado.setMaximumSize(new java.awt.Dimension(980, 500));
        JPpago_empleado.setMinimumSize(new java.awt.Dimension(980, 500));

        jl_titulo29.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo29.setText("Pagos a empleados");
        jl_titulo29.setIconTextGap(10);
        jl_titulo29.setVerifyInputWhenFocusTarget(false);

        jsTabla_ciu16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpe = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpe.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        jLabel147.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("Buscar por");

        JCpe.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCpe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpeItemStateChanged(evt);
            }
        });

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

        res_pe.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pe.setText("Resultados: 0 de 0");

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
                .addGap(35, 35, 35)
                .addGroup(JPpeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel156)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_pag_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPpeLayout.createSequentialGroup()
                        .addComponent(jLabel149)
                        .addGap(3, 3, 3)
                        .addComponent(total_pe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(30, Short.MAX_VALUE))
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
                .addGap(20, 20, 20))
        );

        elim_pe.setBackground(new java.awt.Color(255, 0, 51));
        elim_pe.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        elim_pe.setForeground(new java.awt.Color(255, 255, 255));
        elim_pe.setText("x    Eliminar");
        elim_pe.setBorder(null);
        elim_pe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        elim_pe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                elim_peMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                elim_peMouseExited(evt);
            }
        });
        elim_pe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elim_peActionPerformed(evt);
            }
        });

        mod_pe.setBackground(new java.awt.Color(51, 204, 255));
        mod_pe.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_pe.setForeground(new java.awt.Color(255, 255, 255));
        mod_pe.setText("¡    Modificar");
        mod_pe.setBorder(null);
        mod_pe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_pe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_peMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_peMouseExited(evt);
            }
        });
        mod_pe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_peActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout JPpago_empleadoLayout = new javax.swing.GroupLayout(JPpago_empleado);
        JPpago_empleado.setLayout(JPpago_empleadoLayout);
        JPpago_empleadoLayout.setHorizontalGroup(
            JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpago_empleadoLayout.createSequentialGroup()
                .addGroup(JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPpago_empleadoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPpago_empleadoLayout.createSequentialGroup()
                                .addComponent(jl_titulo29)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel147)
                                .addGap(6, 6, 6)
                                .addComponent(JCpe, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(Bpe, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(Lpe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(res_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsTabla_ciu16)
                            .addGroup(JPpago_empleadoLayout.createSequentialGroup()
                                .addGap(146, 146, 146)
                                .addGroup(JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JPpe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE))
                                .addGap(23, 23, 23))))
                    .addGroup(JPpago_empleadoLayout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addComponent(reg_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(61, 61, 61))
        );
        JPpago_empleadoLayout.setVerticalGroup(
            JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpago_empleadoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_titulo29, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCpe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lpe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(res_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jsTabla_ciu16, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(JPpe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(JPpago_empleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reg_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mod_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(elim_pe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        INICIO.addTab("Pagos a empleados", JPpago_empleado);

        JPiva_fp.setBackground(new java.awt.Color(252, 240, 219));
        JPiva_fp.setMaximumSize(new java.awt.Dimension(980, 500));
        JPiva_fp.setMinimumSize(new java.awt.Dimension(980, 500));

        jsTabla_ciu20.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTiva = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTiva.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTiva.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCiva.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCiva.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCiva.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCivaItemStateChanged(evt);
            }
        });

        jLabel169.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel169.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel169.setText("Buscar por");

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

        res_iva.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_iva.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_iva.setText("Resultados: 0 de 0");

        jl_titulo33.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo33.setText("IVA-s");
        jl_titulo33.setIconTextGap(10);
        jl_titulo33.setVerifyInputWhenFocusTarget(false);

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

        jsTabla_ciu21.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTfp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTfp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTfp.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JPiva.setBackground(new java.awt.Color(255, 255, 255));

        jLabel171.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel171.setText("ID:");

        id_iva.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_iva.setText("0");

        jLabel172.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel172.setText("Impuesto:  %");

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

        mod_iva.setBackground(new java.awt.Color(51, 204, 255));
        mod_iva.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        mod_iva.setForeground(new java.awt.Color(255, 255, 255));
        mod_iva.setText("¡    Modificar");
        mod_iva.setBorder(null);
        mod_iva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mod_iva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mod_ivaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                mod_ivaMouseExited(evt);
            }
        });
        mod_iva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mod_ivaActionPerformed(evt);
            }
        });

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

        JCfp.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCfp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCfp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCfpItemStateChanged(evt);
            }
        });

        jLabel173.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel173.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel173.setText("Buscar por");

        jl_titulo34.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo34.setText("Formas de pago");
        jl_titulo34.setIconTextGap(10);
        jl_titulo34.setVerifyInputWhenFocusTarget(false);

        res_fp.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_fp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_fp.setText("Resultados: 0 de 0");

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

        jSeparator31.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator31.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator31.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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

        javax.swing.GroupLayout JPiva_fpLayout = new javax.swing.GroupLayout(JPiva_fp);
        JPiva_fp.setLayout(JPiva_fpLayout);
        JPiva_fpLayout.setHorizontalGroup(
            JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPiva_fpLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addComponent(jl_titulo33, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addComponent(jLabel169)
                        .addGap(6, 6, 6)
                        .addComponent(JCiva, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Biva, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addComponent(reg_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_iva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel39, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addComponent(JPiva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Liva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator31, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addComponent(reg_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(mod_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(elim_fp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPiva_fpLayout.createSequentialGroup()
                        .addComponent(jl_titulo34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(res_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addComponent(jLabel173)
                        .addGap(6, 6, 6)
                        .addComponent(JCfp, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bfp, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(JPfp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Lfp)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPiva_fpLayout.setVerticalGroup(
            JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPiva_fpLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator31)
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo33)
                            .addComponent(res_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JCiva, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Biva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu20, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPiva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(Liva, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jl_titulo34)
                            .addComponent(res_fp))
                        .addGap(11, 11, 11)
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPiva_fpLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(JCfp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Bfp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu21, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPfp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(JPiva_fpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_fp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPiva_fpLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(Lfp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

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

        JPcli_emp.add(jsTabla_ciu4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 378, 420, 170));

        JCcli.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCcli.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
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
        JCper.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
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

        JPcli_emp.add(jsTabla_ciu6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 67, 899, 170));

        res_per.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_per.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_per.setText("Resultados: 0 de 0");
        JPcli_emp.add(res_per, new org.netbeans.lib.awtextra.AbsoluteConstraints(707, 20, 212, 35));

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

        JPcli_emp.add(JPcli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 616, 420, -1));

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
        JPcli_emp.add(elim_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 851, 130, 40));

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
        JPcli_emp.add(reg_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 851, 130, 40));

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
        JPcli_emp.add(mod_cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 851, 136, 40));

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
        JCemp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
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

        JPcli_emp.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 617, -1, -1));

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
        JPcli_emp.add(elim_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 887, 130, 40));

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
        JPcli_emp.add(mod_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(644, 887, 136, 40));

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
        JPcli_emp.add(reg_emp, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 887, 132, 40));

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

        jsTabla_ciu18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTgen = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTgen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTgen.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCgen.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCgen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCgen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCgenItemStateChanged(evt);
            }
        });

        jLabel160.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel160.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel160.setText("Buscar por");

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

        res_gen.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_gen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_gen.setText("Resultados: 0 de 0");

        jl_titulo31.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo31.setText("Géneros");
        jl_titulo31.setIconTextGap(10);
        jl_titulo31.setVerifyInputWhenFocusTarget(false);

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

        jsTabla_ciu19.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdes.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCdes.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCdes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdesItemStateChanged(evt);
            }
        });

        jLabel164.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel164.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel164.setText("Buscar por");

        jl_titulo32.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo32.setText("Descuentos");
        jl_titulo32.setIconTextGap(10);
        jl_titulo32.setVerifyInputWhenFocusTarget(false);

        res_des.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_des.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_des.setText("Resultados: 0 de 0");

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

        jSeparator30.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator30.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator30.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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

        JPdes.setBackground(new java.awt.Color(255, 255, 255));

        jLabel166.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel166.setText("Descripción:");

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

        javax.swing.GroupLayout JPgen_desLayout = new javax.swing.GroupLayout(JPgen_des);
        JPgen_des.setLayout(JPgen_desLayout);
        JPgen_desLayout.setHorizontalGroup(
            JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgen_desLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(jl_titulo31, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(jLabel160)
                        .addGap(6, 6, 6)
                        .addComponent(JCgen, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bgen, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu18, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(reg_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JPgen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Lgen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator30, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(reg_des, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(mod_des, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(elim_des, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(JPdes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(jl_titulo32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(res_des, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addComponent(jLabel164)
                        .addGap(6, 6, 6)
                        .addComponent(JCdes, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bdes, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu19, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Ldes)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPgen_desLayout.setVerticalGroup(
            JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPgen_desLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator30)
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo31)
                            .addComponent(res_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JCgen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Bgen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu18, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPgen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_gen, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(Lgen, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jl_titulo32)
                            .addComponent(res_des))
                        .addGap(11, 11, 11)
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPgen_desLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(JCdes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Bdes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu19, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPdes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(JPgen_desLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_des, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_des, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_des, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPgen_desLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(Ldes, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        PERSONAS.addTab("Géneros y Descuentos", JPgen_des);

        MENU.addTab("Personas", PERSONAS);

        JPpue_dep.setBackground(new java.awt.Color(252, 240, 219));
        JPpue_dep.setMaximumSize(new java.awt.Dimension(980, 500));
        JPpue_dep.setMinimumSize(new java.awt.Dimension(980, 500));
        JPpue_dep.setPreferredSize(new java.awt.Dimension(980, 500));

        jsTabla_ciu7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpue = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpue.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCpue.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCpue.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCpueItemStateChanged(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel81.setText("Buscar por");

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

        res_pue.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pue.setText("Resultados: 0 de 0");

        jl_titulo18.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo18.setText("Puestos");
        jl_titulo18.setIconTextGap(10);
        jl_titulo18.setVerifyInputWhenFocusTarget(false);

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

        jsTabla_ciu8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTdep = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTdep.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTdep.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCdep.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCdep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCdep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCdepItemStateChanged(evt);
            }
        });

        jLabel109.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel109.setText("Buscar por");

        jl_titulo22.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo22.setText("Departamentos");
        jl_titulo22.setIconTextGap(10);
        jl_titulo22.setVerifyInputWhenFocusTarget(false);

        res_dep.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_dep.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_dep.setText("Resultados: 0 de 0");

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

        jSeparator25.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator25.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator25.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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

        JPdep.setBackground(new java.awt.Color(255, 255, 255));

        jLabel127.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel127.setText("Descripción:");

        jLabel128.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel128.setText("ID:");

        id_dep.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_dep.setText("0");

        jLabel129.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel129.setText("Nombre:");

        nombre_dep.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_dep.setText("0");

        jScrollPane2.setBorder(null);

        descripcion_dep.setColumns(20);
        descripcion_dep.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
        descripcion_dep.setLineWrap(true);
        descripcion_dep.setRows(5);
        descripcion_dep.setWrapStyleWord(true);
        descripcion_dep.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Comic Sans MS", 0, 14))); // NOI18N
        descripcion_dep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcion_depKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(descripcion_dep);

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
                    .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        JPdepLayout.setVerticalGroup(
            JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdepLayout.createSequentialGroup()
                .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel128)
                            .addComponent(id_dep)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPdepLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel127)))
                .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(JPdepLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel129)
                            .addComponent(nombre_dep)))
                    .addGroup(JPdepLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout JPpue_depLayout = new javax.swing.GroupLayout(JPpue_dep);
        JPpue_dep.setLayout(JPpue_depLayout);
        JPpue_depLayout.setHorizontalGroup(
            JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpue_depLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPpue_depLayout.createSequentialGroup()
                        .addComponent(jl_titulo18, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPpue_depLayout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addGap(6, 6, 6)
                        .addComponent(JCpue, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bpue, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu7, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JPpue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPpue_depLayout.createSequentialGroup()
                        .addComponent(reg_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addComponent(Lpue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator25, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPpue_depLayout.createSequentialGroup()
                        .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(JPpue_depLayout.createSequentialGroup()
                                .addComponent(jl_titulo22)
                                .addGap(1, 1, 1)
                                .addComponent(res_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JPpue_depLayout.createSequentialGroup()
                                .addComponent(jLabel109)
                                .addGap(6, 6, 6)
                                .addComponent(JCdep, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(Bdep, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsTabla_ciu8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addComponent(Ldep))
                    .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(JPpue_depLayout.createSequentialGroup()
                            .addComponent(reg_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(mod_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(13, 13, 13)
                            .addComponent(elim_dep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(JPdep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPpue_depLayout.setVerticalGroup(
            JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPpue_depLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator25)
                    .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(JPpue_depLayout.createSequentialGroup()
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jl_titulo18)
                                .addComponent(res_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(JCpue, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Bpue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addComponent(jsTabla_ciu7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(JPpue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(reg_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mod_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(elim_pue, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(JPpue_depLayout.createSequentialGroup()
                            .addGap(44, 44, 44)
                            .addComponent(Lpue, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(JPpue_depLayout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addComponent(Ldep, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(JPpue_depLayout.createSequentialGroup()
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jl_titulo22)
                                .addGroup(JPpue_depLayout.createSequentialGroup()
                                    .addGap(4, 4, 4)
                                    .addComponent(res_dep)))
                            .addGap(11, 11, 11)
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel109, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(JPpue_depLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(JCdep, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(Bdep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12, 12, 12)
                            .addComponent(jsTabla_ciu8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(JPdep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addGroup(JPpue_depLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(reg_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(mod_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(elim_dep, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(103, Short.MAX_VALUE))
        );

        MENU.addTab("Puestos y Departamentos", JPpue_dep);

        JPproductos.setBackground(new java.awt.Color(252, 240, 219));
        JPproductos.setMaximumSize(new java.awt.Dimension(980, 500));
        JPproductos.setMinimumSize(new java.awt.Dimension(980, 500));

        jl_titulo24.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo24.setText("Productos");
        jl_titulo24.setIconTextGap(10);
        jl_titulo24.setVerifyInputWhenFocusTarget(false);

        jsTabla_ciu11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTpro = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTpro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTpro.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        jLabel114.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setText("Buscar por");

        JCpro.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCpro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCpro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCproItemStateChanged(evt);
            }
        });

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

        res_pro.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_pro.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_pro.setText("Resultados: 0 de 0");

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
            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

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
        jLabel108.setText("Precio:  $");

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

        imagen_pro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout JPproLayout = new javax.swing.GroupLayout(JPpro);
        JPpro.setLayout(JPproLayout);
        JPproLayout.setHorizontalGroup(
            JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel106)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel116)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoria_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel110)
                        .addGap(6, 6, 6)
                        .addComponent(marca_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel108)
                        .addGap(3, 3, 3)
                        .addComponent(precio_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel105)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(codigo_pro)))
                .addGap(198, 198, 198)
                .addComponent(imagen_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel117)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(proveedor_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stock_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addComponent(jLabel112)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_reg_pro))
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel107, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(maximos_pro, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                            .addComponent(minimos_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        JPproLayout.setVerticalGroup(
            JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagen_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPproLayout.createSequentialGroup()
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel104)
                            .addComponent(maximos_pro)
                            .addComponent(jLabel105)
                            .addComponent(codigo_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel106)
                            .addComponent(nombre_pro)
                            .addComponent(jLabel107)
                            .addComponent(minimos_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel110)
                            .addComponent(marca_pro)
                            .addComponent(jLabel111)
                            .addComponent(stock_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel108)
                            .addComponent(precio_pro)
                            .addComponent(jLabel117)
                            .addComponent(proveedor_pro))
                        .addGap(15, 15, 15)
                        .addGroup(JPproLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel116)
                            .addComponent(categoria_pro)
                            .addComponent(jLabel112)
                            .addComponent(fecha_reg_pro))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout JPproductosLayout = new javax.swing.GroupLayout(JPproductos);
        JPproductos.setLayout(JPproductosLayout);
        JPproductosLayout.setHorizontalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addComponent(jl_titulo24, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel114)
                        .addGap(6, 6, 6)
                        .addComponent(JCpro, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bpro, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(Lpro)
                        .addGap(46, 46, 46)
                        .addComponent(res_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu11)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
                            .addComponent(JPpro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mod_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(elim_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(reg_pro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        JPproductosLayout.setVerticalGroup(
            JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPproductosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_titulo24, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JCpro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Bpro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Lpro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(res_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jsTabla_ciu11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(JPproductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPproductosLayout.createSequentialGroup()
                        .addComponent(reg_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mod_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elim_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JPpro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        MENU.addTab("Productos", JPproductos);

        JPmar_cat.setBackground(new java.awt.Color(252, 240, 219));
        JPmar_cat.setMaximumSize(new java.awt.Dimension(980, 500));
        JPmar_cat.setMinimumSize(new java.awt.Dimension(980, 500));

        jsTabla_ciu9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTmar = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTmar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTmar.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCmar.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCmar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCmar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCmarItemStateChanged(evt);
            }
        });

        jLabel113.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel113.setText("Buscar por");

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

        res_mar.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_mar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_mar.setText("Resultados: 0 de 0");

        jl_titulo21.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo21.setText("Marcas");
        jl_titulo21.setIconTextGap(10);
        jl_titulo21.setVerifyInputWhenFocusTarget(false);

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

        jsTabla_ciu10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTcat = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTcat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTcat.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCcat.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCcat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCcat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCcatItemStateChanged(evt);
            }
        });

        jLabel121.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel121.setText("Buscar por");

        jl_titulo23.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo23.setText("Categorías");
        jl_titulo23.setIconTextGap(10);
        jl_titulo23.setVerifyInputWhenFocusTarget(false);

        res_cat.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_cat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_cat.setText("Resultados: 0 de 0");

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

        jSeparator26.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator26.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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

        descripcion_cat.setColumns(20);
        descripcion_cat.setFont(new java.awt.Font("Yu Gothic UI Light", 1, 18)); // NOI18N
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
                    .addGroup(JPcatLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout JPmar_catLayout = new javax.swing.GroupLayout(JPmar_cat);
        JPmar_cat.setLayout(JPmar_catLayout);
        JPmar_catLayout.setHorizontalGroup(
            JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPmar_catLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(jl_titulo21, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(jLabel113)
                        .addGap(6, 6, 6)
                        .addComponent(JCmar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bmar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu9, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JPmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(reg_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addComponent(Lmar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator26, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(reg_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(mod_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(elim_cat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(JPcat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(jl_titulo23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(res_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addComponent(jLabel121)
                        .addGap(6, 6, 6)
                        .addComponent(JCcat, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bcat, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Lcat)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPmar_catLayout.setVerticalGroup(
            JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPmar_catLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator26)
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo21)
                            .addComponent(res_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JCmar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Bmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu9, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_mar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(Lmar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jl_titulo23)
                            .addComponent(res_cat))
                        .addGap(11, 11, 11)
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPmar_catLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(JCcat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Bcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPcat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(JPmar_catLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_cat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPmar_catLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(Lcat, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        MENU.addTab("Marcas y Categorías", JPmar_cat);

        JPprov_suc.setBackground(new java.awt.Color(252, 240, 219));
        JPprov_suc.setMaximumSize(new java.awt.Dimension(980, 500));
        JPprov_suc.setMinimumSize(new java.awt.Dimension(980, 500));

        jsTabla_ciu12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTprov = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTprov.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTprov.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCprov.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCprov.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCprov.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCprovItemStateChanged(evt);
            }
        });

        jLabel118.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel118.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel118.setText("Buscar por");

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

        res_prov.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_prov.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_prov.setText("Resultados: 0 de 0");

        jl_titulo25.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo25.setText("Proveedores");
        jl_titulo25.setIconTextGap(10);
        jl_titulo25.setVerifyInputWhenFocusTarget(false);

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
            .addComponent(jLabel130, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel130, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jsTabla_ciu13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTsuc = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTsuc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTsuc.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JPprov.setBackground(new java.awt.Color(255, 255, 255));

        jLabel131.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel131.setText("ID:");

        id_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_prov.setText("0");

        jLabel132.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel132.setText("Nombre:");

        nombre_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_prov.setText("0");

        celular_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        celular_prov.setText("0");

        jLabel138.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel138.setText("Celular:");

        fecha_reg_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        fecha_reg_prov.setText("0");

        jLabel139.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel139.setText("Email:");

        email_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        email_prov.setText("0");

        jLabel140.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel140.setText("Ciudad:");

        ciudad_prov.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        ciudad_prov.setText("0");

        jLabel141.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel141.setText("Fecha de registro:");

        javax.swing.GroupLayout JPprovLayout = new javax.swing.GroupLayout(JPprov);
        JPprov.setLayout(JPprovLayout);
        JPprovLayout.setHorizontalGroup(
            JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPprovLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel138)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(celular_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel132)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel131)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(JPprovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email_prov, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel141)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_reg_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPprovLayout.createSequentialGroup()
                        .addComponent(jLabel140)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciudad_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(161, 161, 161))
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
                            .addComponent(id_prov))
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

        JCsuc.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCsuc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCsuc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCsucItemStateChanged(evt);
            }
        });

        jLabel133.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel133.setText("Buscar por");

        jl_titulo26.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo26.setText("Sucursales");
        jl_titulo26.setIconTextGap(10);
        jl_titulo26.setVerifyInputWhenFocusTarget(false);

        res_suc.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_suc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_suc.setText("Resultados: 0 de 0");

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

        jSeparator27.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator27.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator27.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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
            .addComponent(jLabel143, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel143, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        JPsuc.setBackground(new java.awt.Color(255, 255, 255));

        jLabel144.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel144.setText("ID:");

        id_suc.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        id_suc.setText("0");

        jLabel145.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        jLabel145.setText("Nombre:");

        nombre_suc.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        nombre_suc.setText("0");

        ciudad_suc.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        ciudad_suc.setText("0");

        jLabel146.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
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
                        .addComponent(id_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        javax.swing.GroupLayout JPprov_sucLayout = new javax.swing.GroupLayout(JPprov_suc);
        JPprov_suc.setLayout(JPprov_sucLayout);
        JPprov_sucLayout.setHorizontalGroup(
            JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPprov_sucLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addComponent(jl_titulo25, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addComponent(jLabel118)
                        .addGap(6, 6, 6)
                        .addComponent(JCprov, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bprov, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addComponent(reg_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JPprov, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Lprov)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator27, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addComponent(reg_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(mod_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(elim_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(JPsuc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel34, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPprov_sucLayout.createSequentialGroup()
                                .addComponent(jl_titulo26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(res_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPprov_sucLayout.createSequentialGroup()
                                .addComponent(jLabel133)
                                .addGap(6, 6, 6)
                                .addComponent(JCsuc, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(Bsuc, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsTabla_ciu13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addComponent(Lsuc)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPprov_sucLayout.setVerticalGroup(
            JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPprov_sucLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator27)
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo25)
                            .addComponent(res_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JCprov, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Bprov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPprov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_prov, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(Lprov, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jl_titulo26)
                            .addComponent(res_suc))
                        .addGap(11, 11, 11)
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPprov_sucLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(JCsuc, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Bsuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPsuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPprov_sucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_suc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPprov_sucLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(Lsuc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        MENU.addTab("Proveedores y Sucursales", JPprov_suc);

        JPciu_provi.setBackground(new java.awt.Color(252, 240, 219));
        JPciu_provi.setMaximumSize(new java.awt.Dimension(980, 500));
        JPciu_provi.setMinimumSize(new java.awt.Dimension(980, 500));

        jsTabla_ciu14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTciu = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTciu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTciu.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCciu.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCciu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCciu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCciuItemStateChanged(evt);
            }
        });

        jLabel134.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel134.setText("Buscar por");

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

        res_ciu.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_ciu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_ciu.setText("Resultados: 0 de 0");

        jl_titulo27.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo27.setText("Ciudades");
        jl_titulo27.setIconTextGap(10);
        jl_titulo27.setVerifyInputWhenFocusTarget(false);

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
            .addComponent(jLabel135, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel135, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

        jsTabla_ciu15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTprovi = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        JTprovi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        JTprovi.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
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

        JCprovi.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        JCprovi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "C. Cliente", "F. Registro", "Total" }));
        JCprovi.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JCproviItemStateChanged(evt);
            }
        });

        jLabel150.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 16)); // NOI18N
        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("Buscar por");

        jl_titulo28.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jl_titulo28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/table_icon_128243.png"))); // NOI18N
        jl_titulo28.setText("Provincias");
        jl_titulo28.setIconTextGap(10);
        jl_titulo28.setVerifyInputWhenFocusTarget(false);

        res_provi.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        res_provi.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        res_provi.setText("Resultados: 0 de 0");

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

        jSeparator28.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator28.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator28.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

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
            .addComponent(jLabel151, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
        );

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout JPciu_proviLayout = new javax.swing.GroupLayout(JPciu_provi);
        JPciu_provi.setLayout(JPciu_proviLayout);
        JPciu_proviLayout.setHorizontalGroup(
            JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciu_proviLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addComponent(jl_titulo27, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(res_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addComponent(jLabel134)
                        .addGap(6, 6, 6)
                        .addComponent(JCciu, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(Bciu, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jsTabla_ciu14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addComponent(reg_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(mod_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(elim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(JPciu, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(Lciu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator28, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addComponent(reg_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(mod_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(elim_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(JPprovi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPciu_proviLayout.createSequentialGroup()
                                .addComponent(jl_titulo28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(res_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, JPciu_proviLayout.createSequentialGroup()
                                .addComponent(jLabel150)
                                .addGap(6, 6, 6)
                                .addComponent(JCprovi, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(Bprovi, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jsTabla_ciu15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addComponent(Lprovi)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        JPciu_proviLayout.setVerticalGroup(
            JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPciu_proviLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator28)
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jl_titulo27)
                            .addComponent(res_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JCciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Bciu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu14, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPciu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_ciu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(Lciu, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jl_titulo28)
                            .addComponent(res_provi))
                        .addGap(11, 11, 11)
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(JPciu_proviLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(JCprovi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(Bprovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jsTabla_ciu15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(JPprovi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(JPciu_proviLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reg_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mod_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(elim_provi, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JPciu_proviLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(Lprovi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        MENU.addTab("Ciudades y Provincias", JPciu_provi);

        getContentPane().add(MENU, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 980, 540));

        encabezado.setBackground(new java.awt.Color(153, 0, 51));
        encabezado.setMinimumSize(new java.awt.Dimension(1020, 40));
        encabezado.setPreferredSize(new java.awt.Dimension(1020, 40));

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

        javax.swing.GroupLayout encabezadoLayout = new javax.swing.GroupLayout(encabezado);
        encabezado.setLayout(encabezadoLayout);
        encabezadoLayout.setHorizontalGroup(
            encabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encabezadoLayout.createSequentialGroup()
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
        encabezadoLayout.setVerticalGroup(
            encabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sistema_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(encabezadoLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(encabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FECHA_HORA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(USUARIO, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(SALIR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(encabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        FONDO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/abstracto_rojo.jpeg"))); // NOI18N
        FONDO.setMaximumSize(new java.awt.Dimension(1020, 625));
        FONDO.setMinimumSize(new java.awt.Dimension(1020, 625));
        FONDO.setPreferredSize(new java.awt.Dimension(1020, 625));
        getContentPane().add(FONDO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        if (actualizado == false) {
            visualizar();
            actualizado = true;
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void enc_cedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enc_cedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enc_cedulaActionPerformed

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

    private void JCencItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCencItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCencItemStateChanged

    private void BencMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BencMouseClicked
        if (Benc.getText().equals("Buscar")) {
            Benc.select(0, 0);
        }
    }//GEN-LAST:event_BencMouseClicked

    private void BencKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BencKeyPressed
        if (Benc.getText().equals("Buscar")) {
            Benc.setText("");
            Lenc.setVisible(true);
        }
    }//GEN-LAST:event_BencKeyPressed

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

    private void BencKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BencKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BencKeyTyped

    private void LencMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LencMouseClicked
        Lenc.setVisible(false);
        Benc.setText("Buscar");
        Benc.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LencMouseClicked

    private void JCdetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdetItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdetItemStateChanged

    private void BdetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdetMouseClicked
         if (Bdet.getText().equals("Buscar")) {
            Bdet.select(0, 0);
        }
    }//GEN-LAST:event_BdetMouseClicked

    private void BdetKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdetKeyPressed
        if (Bdet.getText().equals("Buscar")) {
            Bdet.setText("");
            Ldet.setVisible(true);
        }
    }//GEN-LAST:event_BdetKeyPressed

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

    private void BdetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdetKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdetKeyTyped

    private void LdetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdetMouseClicked
        Ldet.setVisible(false);
        Bdet.setText("Buscar");
        Bdet.select(0, 0);
        visualizar();
    }//GEN-LAST:event_LdetMouseClicked

    private void subir_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subir_1MouseClicked
        JSventas.getVerticalScrollBar().setValue(0);
    }//GEN-LAST:event_subir_1MouseClicked

    private void SALIRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SALIRMouseClicked
//        this.dispose();
//        JFlogin sesión = new JFlogin();
//        sesión.setVisible(true);
    }//GEN-LAST:event_SALIRMouseClicked

    private void JCcliItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCcliItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCcliItemStateChanged

    private void BcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BcliMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BcliMouseClicked

    private void BcliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BcliKeyPressed

    private void BcliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BcliKeyReleased

    private void BcliKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BcliKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BcliKeyTyped

    private void LcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LcliMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LcliMouseClicked

    private void JCperItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCperItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCperItemStateChanged

    private void BperMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BperMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BperMouseClicked

    private void BperKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BperKeyPressed

    private void BperKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BperKeyReleased

    private void BperKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BperKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BperKeyTyped

    private void LperMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LperMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LperMouseClicked

    private void elim_cliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_cliActionPerformed
        if (JPcli.isVisible()) {
            eliminar(3);
        } else {
            getToolkit().beep();
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
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
            JOptionPane.showMessageDialog(null, "!Ningun registro seleccionado!");
        }
    }//GEN-LAST:event_mod_cliActionPerformed

    private void BempMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BempMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BempMouseClicked

    private void BempKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BempKeyPressed

    private void BempKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BempKeyReleased

    private void BempKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BempKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BempKeyTyped

    private void JCempItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCempItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCempItemStateChanged

    private void LempMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LempMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LempMouseClicked

    private void elim_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_empMouseEntered

    private void elim_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_empMouseExited

    private void elim_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_empActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_empActionPerformed

    private void mod_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_empMouseEntered

    private void mod_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_empMouseExited

    private void mod_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_empActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_empActionPerformed

    private void reg_empMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_empMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_empMouseEntered

    private void reg_empMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_empMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_empMouseExited

    private void reg_empActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_empActionPerformed
        
    }//GEN-LAST:event_reg_empActionPerformed

    private void jLabel86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel86MouseClicked
        JScli_emp.getVerticalScrollBar().setValue(0);
    }//GEN-LAST:event_jLabel86MouseClicked

    private void LdepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdepMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LdepMouseClicked

    private void JCdepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdepItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdepItemStateChanged

    private void BdepKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdepKeyTyped

    private void BdepKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BdepKeyReleased

    private void BdepKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdepKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BdepKeyPressed

    private void BdepMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdepMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BdepMouseClicked

    private void mod_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_pueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_pueActionPerformed

    private void mod_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_pueMouseExited

    private void mod_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_pueMouseEntered

    private void reg_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_pueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_pueActionPerformed

    private void reg_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_pueMouseExited

    private void reg_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_pueMouseEntered

    private void elim_pueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_pueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_pueActionPerformed

    private void elim_pueMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_pueMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_pueMouseExited

    private void elim_pueMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_pueMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_pueMouseEntered

    private void LpueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LpueMouseClicked

    private void BpueKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpueKeyTyped

    private void BpueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BpueKeyReleased

    private void BpueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BpueKeyPressed

    private void BpueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BpueMouseClicked

    private void JCpueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpueItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpueItemStateChanged

    private void JCproItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCproItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCproItemStateChanged

    private void BproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BproMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BproMouseClicked

    private void BproKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BproKeyPressed

    private void BproKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BproKeyReleased

    private void BproKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BproKeyTyped

    private void LproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LproMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LproMouseClicked

    private void elim_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proMouseEntered

    private void elim_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proMouseExited

    private void elim_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proActionPerformed

    private void mod_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proMouseEntered

    private void mod_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proMouseExited

    private void mod_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proActionPerformed

    private void reg_proMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proMouseEntered

    private void reg_proMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proMouseExited

    private void reg_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_proActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proActionPerformed

    private void JCmarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCmarItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCmarItemStateChanged

    private void BmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BmarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BmarMouseClicked

    private void BmarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BmarKeyPressed

    private void BmarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BmarKeyReleased

    private void BmarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BmarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BmarKeyTyped

    private void LmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LmarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LmarMouseClicked

    private void elim_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_marMouseEntered

    private void elim_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_marMouseExited

    private void elim_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_marActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_marActionPerformed

    private void reg_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_marMouseEntered

    private void reg_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_marMouseExited

    private void reg_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_marActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_marActionPerformed

    private void mod_marMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_marMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_marMouseEntered

    private void mod_marMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_marMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_marMouseExited

    private void mod_marActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_marActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_marActionPerformed

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

    private void JCcatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCcatItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCcatItemStateChanged

    private void LcatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LcatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LcatMouseClicked

    private void elim_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_catMouseEntered

    private void elim_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_catMouseExited

    private void elim_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_catActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_catActionPerformed

    private void mod_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_catMouseEntered

    private void mod_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_catMouseExited

    private void mod_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_catActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_catActionPerformed

    private void reg_catMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_catMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_catMouseEntered

    private void reg_catMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_catMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_catMouseExited

    private void reg_catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_catActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_catActionPerformed

    private void descripcion_catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcion_catKeyPressed
        
    }//GEN-LAST:event_descripcion_catKeyPressed

    private void descripcion_depKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcion_depKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcion_depKeyPressed

    private void reg_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_depMouseEntered

    private void reg_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_depMouseExited

    private void reg_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_depActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_depActionPerformed

    private void mod_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_depMouseEntered

    private void mod_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_depMouseExited

    private void mod_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_depActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_depActionPerformed

    private void elim_depMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_depMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_depMouseEntered

    private void elim_depMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_depMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_depMouseExited

    private void elim_depActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_depActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_depActionPerformed

    private void JCprovItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCprovItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCprovItemStateChanged

    private void BprovMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BprovMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovMouseClicked

    private void BprovKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovKeyPressed

    private void BprovKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovKeyReleased

    private void BprovKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BprovKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BprovKeyTyped

    private void LprovMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LprovMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LprovMouseClicked

    private void elim_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_provMouseEntered

    private void elim_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_provMouseExited

    private void elim_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_provActionPerformed

    private void reg_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_provMouseEntered

    private void reg_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_provMouseExited

    private void reg_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_provActionPerformed

    private void mod_provMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_provMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_provMouseEntered

    private void mod_provMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_provMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_provMouseExited

    private void mod_provActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_provActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_provActionPerformed

    private void BsucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BsucMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BsucMouseClicked

    private void BsucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BsucKeyPressed

    private void BsucKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BsucKeyReleased

    private void BsucKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BsucKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BsucKeyTyped

    private void JCsucItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCsucItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCsucItemStateChanged

    private void LsucMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LsucMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LsucMouseClicked

    private void reg_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_sucMouseEntered

    private void reg_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_sucMouseExited

    private void reg_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_sucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_sucActionPerformed

    private void mod_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_sucMouseEntered

    private void mod_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_sucMouseExited

    private void mod_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_sucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_sucActionPerformed

    private void elim_sucMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_sucMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_sucMouseEntered

    private void elim_sucMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_sucMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_sucMouseExited

    private void elim_sucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_sucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_sucActionPerformed

    private void JCciuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCciuItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCciuItemStateChanged

    private void BciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BciuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BciuMouseClicked

    private void BciuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BciuKeyPressed

    private void BciuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BciuKeyReleased

    private void BciuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BciuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BciuKeyTyped

    private void LciuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LciuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LciuMouseClicked

    private void elim_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ciuMouseEntered

    private void elim_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ciuMouseExited

    private void elim_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_ciuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ciuActionPerformed

    private void reg_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ciuMouseEntered

    private void reg_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ciuMouseExited

    private void reg_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_ciuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ciuActionPerformed

    private void mod_ciuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ciuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ciuMouseEntered

    private void mod_ciuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ciuMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ciuMouseExited

    private void mod_ciuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_ciuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ciuActionPerformed

    private void BproviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BproviMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BproviMouseClicked

    private void BproviKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BproviKeyPressed

    private void BproviKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BproviKeyReleased

    private void BproviKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BproviKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BproviKeyTyped

    private void JCproviItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCproviItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCproviItemStateChanged

    private void LproviMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LproviMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LproviMouseClicked

    private void reg_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proviMouseEntered

    private void reg_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proviMouseExited

    private void reg_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_proviActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_proviActionPerformed

    private void mod_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proviMouseEntered

    private void mod_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proviMouseExited

    private void mod_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_proviActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_proviActionPerformed

    private void elim_proviMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proviMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proviMouseEntered

    private void elim_proviMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_proviMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proviMouseExited

    private void elim_proviActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_proviActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_proviActionPerformed

    private void JCgenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCgenItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCgenItemStateChanged

    private void BgenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BgenMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BgenMouseClicked

    private void BgenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BgenKeyPressed

    private void BgenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BgenKeyReleased

    private void BgenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BgenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BgenKeyTyped

    private void LgenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LgenMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LgenMouseClicked

    private void elim_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_genMouseEntered

    private void elim_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_genMouseExited

    private void elim_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_genActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_genActionPerformed

    private void reg_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_genMouseEntered

    private void reg_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_genMouseExited

    private void reg_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_genActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_genActionPerformed

    private void mod_genMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_genMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_genMouseEntered

    private void mod_genMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_genMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_genMouseExited

    private void mod_genActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_genActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_genActionPerformed

    private void BdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BdesMouseClicked

    private void BdesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BdesKeyPressed

    private void BdesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BdesKeyReleased

    private void BdesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BdesKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BdesKeyTyped

    private void JCdesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCdesItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCdesItemStateChanged

    private void LdesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LdesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LdesMouseClicked

    private void elim_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_desMouseEntered

    private void elim_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_desMouseExited

    private void elim_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_desActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_desActionPerformed

    private void mod_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_desMouseEntered

    private void mod_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_desMouseExited

    private void mod_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_desActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_desActionPerformed

    private void reg_desMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_desMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_desMouseEntered

    private void reg_desMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_desMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_desMouseExited

    private void reg_desActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_desActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_desActionPerformed

    private void JCpeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpeItemStateChanged

    private void BpeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BpeMouseClicked

    private void BpeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BpeKeyPressed

    private void BpeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BpeKeyReleased

    private void BpeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpeKeyTyped

    private void LpeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LpeMouseClicked

    private void elim_peMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_peMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_peMouseEntered

    private void elim_peMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_peMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_peMouseExited

    private void elim_peActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_peActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_peActionPerformed

    private void mod_peMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_peMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_peMouseEntered

    private void mod_peMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_peMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_peMouseExited

    private void mod_peActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_peActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_peActionPerformed

    private void reg_peMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_peMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_peMouseEntered

    private void reg_peMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_peMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_peMouseExited

    private void reg_peActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_peActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_peActionPerformed

    private void JCivaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCivaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCivaItemStateChanged

    private void BivaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BivaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BivaMouseClicked

    private void BivaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BivaKeyPressed

    private void BivaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BivaKeyReleased

    private void BivaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BivaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BivaKeyTyped

    private void LivaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LivaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LivaMouseClicked

    private void elim_ivaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ivaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ivaMouseEntered

    private void elim_ivaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_ivaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ivaMouseExited

    private void elim_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_ivaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_ivaActionPerformed

    private void reg_ivaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ivaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ivaMouseEntered

    private void reg_ivaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_ivaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ivaMouseExited

    private void reg_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_ivaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_ivaActionPerformed

    private void mod_ivaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ivaMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ivaMouseEntered

    private void mod_ivaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_ivaMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ivaMouseExited

    private void mod_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_ivaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_ivaActionPerformed

    private void BfpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BfpMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BfpMouseClicked

    private void BfpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BfpKeyPressed

    private void BfpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BfpKeyReleased

    private void BfpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BfpKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BfpKeyTyped

    private void JCfpItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCfpItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCfpItemStateChanged

    private void LfpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LfpMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LfpMouseClicked

    private void elim_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_fpMouseEntered

    private void elim_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_elim_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_fpMouseExited

    private void elim_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elim_fpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_elim_fpActionPerformed

    private void mod_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_fpMouseEntered

    private void mod_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mod_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_fpMouseExited

    private void mod_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mod_fpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mod_fpActionPerformed

    private void reg_fpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_fpMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_fpMouseEntered

    private void reg_fpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reg_fpMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_fpMouseExited

    private void reg_fpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reg_fpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reg_fpActionPerformed

    private void LpagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LpagMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LpagMouseClicked

    private void BpagMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BpagMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_BpagMouseClicked

    private void BpagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BpagKeyPressed

    private void BpagKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_BpagKeyReleased

    private void BpagKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BpagKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_BpagKeyTyped

    private void JCpagItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JCpagItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JCpagItemStateChanged

    
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

//        JTproductos.addMouseListener(new MouseAdapter() { //productos(9)
//            @Override
//            public void mousePressed(MouseEvent Mouse_evt) {
//                if (Mouse_evt.getClickCount() == 1) {
//                    jlCodigo_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 0).toString());
//                    jlNombre_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 1).toString());
//                    jlPrecio_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 2).toString());
//                    jlExistencias_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 3).toString());
//                    jlCategoria_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 4).toString());
//                    jlProveedor_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 5).toString());
//                    jlReg_pro.setText(JTproductos.getValueAt(JTproductos.getSelectedRow(), 6).toString());
//                }
//                if (Mouse_evt.getClickCount() == 2) {
//                    if (enc_cedula.getText().equals("")) {
//                        JOptionPane.showMessageDialog(null, "¡Primero selecione un cliente antes enviar productos!");
//                    } else {
//                        int existencias = Integer.parseInt(jlExistencias_pro.getText());
//                        if (existencias > 0) {
//                            boolean repetido = false;
//                            for (int i = 0; i < detalles.size(); i++) {
//                                if (detalles.get(i).equals(jlCodigo_pro.getText())) {
//                                    repetido = true;
//                                    break;
//                                }
//                            }
//                            if (repetido) {
//                                JOptionPane.showMessageDialog(null, "¡Este producto ya fué seleccionado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
//                            } else {
//                                try {
//                                    int cantidad = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la cantidad:", 1));
//                                    if (cantidad > 0 && cantidad <= Integer.parseInt(jlExistencias_pro.getText())) {
//                                        Double xprecio = Double.valueOf(jlPrecio_pro.getText());
//                                        JTdetalle.setDefaultRenderer(Object.class, new BotonTabla());
//                                        Object detalle[] = {jlCodigo_pro.getText(), jlNombre_pro.getText(), cantidad, xprecio, descuento + "%", xprecio - ((descuento * xprecio) / 100), Math.round((cantidad * (xprecio - ((descuento * xprecio) / 100))) * 100.0) / 100.0, boton1};
//
//                                        detalles.add(jlCodigo_pro.getText());
//                                        //-------------- Agrega un detalle a la tabla
//                                        tabla_detalle.addRow(detalle);
//                                        JTdetalle.setModel(tabla_detalle);
//                                        //Confirmar_venta.setEnabled(true);
//                                        //--------------- Actualiza variables
//                                        num_det++;
//
//                                        subtotal += xprecio * cantidad;
//                                        subtotal = Math.rint(subtotal * 100.0) / 100.0; //lo deja con 2 decimales
//
//                                        double xsubtotal = cantidad * (xprecio - ((descuento * xprecio) / 100));
//                                        total += xsubtotal;
//                                        total = (Math.rint(total * 100.0) / 100.0); //lo deja con 2 decimales
//
//                                        total_descuento += (xprecio * cantidad) - (xsubtotal);
//                                        total_descuento = (Math.rint(total_descuento * 100.0) / 100.0); //lo deja con 2 decimales
//
//                                        jl_num_det.setText("Detalles: " + num_det);
//                                        jl_total.setText("Total: $" + total);
//                                        jlSubtotal.setText("$" + subtotal);
//                                        jlTotal_descuento.setText("- $" + total_descuento);
//                                        jlTotal.setText("$" + (total));
//
//                                        //---------- Redireccionar
//                                        JBcrear_factura.setEnabled(true);
//                                        MENU.setSelectedIndex(0);
//                                        INICIO.setSelectedIndex(0);
//                                    } else {
//                                        if (cantidad > Integer.parseInt(jlExistencias_pro.getText())) {
//                                            JOptionPane.showMessageDialog(null, "¡Solo existen '" + jlExistencias_pro.getText() + "' de este producto!", null, JOptionPane.WARNING_MESSAGE);
//                                        }
//                                        if (cantidad <= 0) {
//                                            JOptionPane.showMessageDialog(null, "¡El mínimo de venta es de 1!", null, JOptionPane.WARNING_MESSAGE);
//                                        }
//
//                                    }
//
//                                } catch (Exception e) {
//                                    JOptionPane.showMessageDialog(null, "¡Cantiadad inválida!", null, JOptionPane.ERROR_MESSAGE);
//                                }
//                            }
//
//                        } else {
//                            JOptionPane.showMessageDialog(null, "¡Producto agotado!, Seleccione otro!", null, JOptionPane.WARNING_MESSAGE);
//                        }
//                    }
//                }
//            }
//        });
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
    public static javax.swing.JTextField Bcat;
    public static javax.swing.JTextField Bciu;
    public static javax.swing.JTextField Bcli;
    public static javax.swing.JTextField Bdep;
    public static javax.swing.JTextField Bdes;
    public static javax.swing.JTextField Bdet;
    public static javax.swing.JTextField Bemp;
    public static javax.swing.JTextField Benc;
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
    private javax.swing.JLabel FECHA_HORA;
    private javax.swing.JLabel FONDO;
    private javax.swing.JTabbedPane INICIO;
    private javax.swing.JButton JBcrear_factura;
    private javax.swing.JButton JBcrear_factura1;
    public transient javax.swing.JButton JBseleccionar_pro;
    private javax.swing.JComboBox<String> JCcat;
    private javax.swing.JComboBox<String> JCciu;
    private javax.swing.JComboBox<String> JCcli;
    private javax.swing.JComboBox<String> JCdep;
    private javax.swing.JComboBox<String> JCdes;
    private javax.swing.JComboBox<String> JCdet;
    private javax.swing.JComboBox<String> JCemp;
    private javax.swing.JComboBox<String> JCenc;
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
    private javax.swing.JPanel JPfacturar;
    private javax.swing.JPanel JPfp;
    private javax.swing.JPanel JPgen;
    private javax.swing.JPanel JPgen_des;
    private javax.swing.JPanel JPiva;
    private javax.swing.JPanel JPiva_fp;
    private javax.swing.JPanel JPmar;
    private javax.swing.JPanel JPmar_cat;
    private javax.swing.JPanel JPpago_empleado;
    private javax.swing.JPanel JPpe;
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
    private javax.swing.JScrollPane JSventas;
    private javax.swing.JTable JTcat;
    private javax.swing.JTable JTciu;
    private javax.swing.JTable JTcli;
    private javax.swing.JTable JTdep;
    private javax.swing.JTable JTdes;
    private javax.swing.JTable JTdet;
    public transient javax.swing.JTable JTdetalle;
    private javax.swing.JTable JTemp;
    private javax.swing.JTable JTenc;
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
    private javax.swing.JLabel Lemp;
    private javax.swing.JLabel Lenc;
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
    private javax.swing.JTabbedPane PERSONAS;
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
    private javax.swing.JLabel apellido_cli;
    private javax.swing.JLabel apellido_emp;
    private javax.swing.JLabel categoria_pro;
    private javax.swing.JLabel cedula_cli;
    private javax.swing.JLabel cedula_emp;
    private javax.swing.JLabel celular_cli;
    private javax.swing.JLabel celular_emp;
    private javax.swing.JLabel celular_prov;
    private javax.swing.JLabel ciudad_cli;
    private javax.swing.JLabel ciudad_emp;
    private javax.swing.JLabel ciudad_prov;
    private javax.swing.JLabel ciudad_suc;
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
    private javax.swing.JButton elim_pe;
    private javax.swing.JButton elim_pro;
    private javax.swing.JButton elim_prov;
    private javax.swing.JButton elim_provi;
    private javax.swing.JButton elim_pue;
    private javax.swing.JButton elim_suc;
    private javax.swing.JLabel email_cli;
    private javax.swing.JLabel email_emp;
    private javax.swing.JLabel email_prov;
    private javax.swing.JTextField enc_cedula;
    private javax.swing.JTextField enc_codigo;
    private javax.swing.JTextField enc_correo;
    private javax.swing.JTextField enc_direccion;
    private javax.swing.JTextField enc_nombre_apellido;
    private javax.swing.JTextField enc_telefono;
    private javax.swing.JPanel encabezado;
    private javax.swing.JTextField fec_enc;
    private javax.swing.JLabel fecha_nac_cli;
    private javax.swing.JLabel fecha_nac_emp;
    private javax.swing.JLabel fecha_pag_pe;
    private javax.swing.JLabel fecha_reg_cli;
    private javax.swing.JLabel fecha_reg_emp;
    private javax.swing.JLabel fecha_reg_pro;
    private javax.swing.JLabel fecha_reg_prov;
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
    private javax.swing.JLabel id_prov;
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
    private javax.swing.JLabel jLabel127;
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
    private javax.swing.JLabel jLabel158;
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
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
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
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
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
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator30;
    private javax.swing.JSeparator jSeparator31;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlTotal_descuento;
    private javax.swing.JLabel jl_num_det;
    private javax.swing.JLabel jl_num_det1;
    private javax.swing.JLabel jl_num_det2;
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
    private javax.swing.JLabel jl_titulo31;
    private javax.swing.JLabel jl_titulo32;
    private javax.swing.JLabel jl_titulo33;
    private javax.swing.JLabel jl_titulo34;
    private javax.swing.JLabel jl_titulo6;
    private javax.swing.JLabel jl_titulo8;
    private javax.swing.JLabel jl_titulo9;
    private javax.swing.JLabel jl_total;
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
    private javax.swing.JScrollPane jsTabla_ciu3;
    private javax.swing.JScrollPane jsTabla_ciu4;
    private javax.swing.JScrollPane jsTabla_ciu5;
    private javax.swing.JScrollPane jsTabla_ciu6;
    private javax.swing.JScrollPane jsTabla_ciu7;
    private javax.swing.JScrollPane jsTabla_ciu8;
    private javax.swing.JScrollPane jsTabla_ciu9;
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
    private javax.swing.JButton mod_iva;
    private javax.swing.JButton mod_mar;
    private javax.swing.JButton mod_pe;
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
    private javax.swing.JLabel res_emp;
    private javax.swing.JLabel res_enc;
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
    private javax.swing.JLabel sexo_cli;
    private javax.swing.JLabel sexo_emp;
    private javax.swing.JLabel sexo_gen;
    public static javax.swing.JLabel sistema_titulo;
    private javax.swing.JLabel stock_pro;
    private javax.swing.JLabel subir_1;
    private javax.swing.JLabel sueldo_pue;
    private javax.swing.JLabel t_facturas_activas11;
    private javax.swing.JLabel t_facturas_activas12;
    private javax.swing.JLabel t_facturas_activas13;
    private javax.swing.JLabel t_facturas_activas14;
    private javax.swing.JLabel t_facturas_activas15;
    private javax.swing.JLabel total_pe;
    // End of variables declaration//GEN-END:variables
}
