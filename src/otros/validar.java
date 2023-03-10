
package otros;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.time.temporal.ChronoUnit;//opcional
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class validar {
    boolean es_delete = false;

    public static void V_numero(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c)
                            || (c == KeyEvent.VK_BACK_SPACE)
                            || (c == KeyEvent.VK_DELETE))) {
                        //getToolkit().beep();
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });
        } catch (Exception a) {
        }
    }
    public static boolean V_numeroSpi(String texto) {
        return texto.matches("^[0-9]{3}$");
    }

    public static void V_letras(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean minusculas = key >= 97 && key <= 122;
                    boolean ñ = key == 209 || key == 241; //código ISO (ñ y Ñ);
                    boolean VocMinTil = key==225 || key==233 || key==237 || key==243 || key==250 || key==252; //código ISO (á, é, í, ó, ú, ü);
                    boolean VocMayTil = key==193 || key==201 || key==205 || key==211 || key==218 || key==220; //código ISO (á, é, í, ó, ú, Ü);
                    
                    if (!(minusculas || mayusculas ||ñ || VocMinTil || VocMayTil)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });
        } catch (Exception b) {
        }
    }
     public static void V_letras_sin_tilde(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean minusculas = key >= 97 && key <= 122;
                    
                    
                    if (!(minusculas || mayusculas)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });
        } catch (Exception b) {
        }
    }
    public static void V_matri(JTextField texto) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean numeros = key >= 48 && key <=57;

                    if (!(mayusculas || numeros)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= 7) {
                        e.consume();
                    }
                }
            });
        } catch (Exception b) {
        }
    }

    public static void V_letras_espacios(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override

                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean minusculas = key >= 97 && key <= 122;
                    boolean espacio = key == 32;

                    if (!(minusculas || mayusculas || espacio)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });

        } catch (Exception d) {
        }

    }

    public static boolean V_correo(JTextField email) {
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@"
                + "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        if (email != null) {
            Matcher matcher = pattern.matcher(email.getText());
            if (matcher.matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public static boolean V_matricula(String matricula) {
        return matricula.matches("^[0-9]{4}[A-Z]{3}$");
    }

    public static void V_contraseña(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override

                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean espacio = key == 32;
                    if (espacio) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });

        } catch (Exception d) {
        }

    }

    public static boolean V_cedula(String cedula) {
        boolean cedulaCorrecta = false;
        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    // Coeficientes de validación cédula
                    // El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            cedulaCorrecta = false;
        }
        return cedulaCorrecta;
    }

    public static boolean V_telefono(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }
    public static boolean V_licencia(String s) {
        Pattern p = Pattern.compile("^\\d{10}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }

    public static boolean V_RUC(String s) {
        Pattern p = Pattern.compile("^\\d{13}$");
        Matcher m = p.matcher(s);
        return (m.matches());
    }

    public static void V_desc(JTextArea texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override

                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean otros = key >= 42 && key <=58; //numeros y algunos caracteres
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean minusculas = key >= 97 && key <= 122;
                    boolean espacio = key == 32;
                    boolean ñ = key == 209 || key == 241; //código ISO (ñ y Ñ);
                    boolean VocMinTil = key==225 || key==233 || key==237 || key==243 || key==250 || key==252; //código ISO (á, é, í, ó, ú, ü);
                    boolean VocMayTil = key==193 || key==201 || key==205 || key==211 || key==218 || key==220; //código ISO (á, é, í, ó, ú, Ü);
                    

                    if (!(otros || mayusculas || minusculas || espacio || ñ || VocMinTil ||VocMayTil )) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });

        } catch (Exception d) {
        }
    }

    public static void nombre_compuesto(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override

                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean mayusculas = key >= 65 && key <= 90;
                    boolean minusculas = key >= 97 && key <= 122;
                    boolean numeros = key >= 48 && key <= 57;
                    boolean espacio = key == 32;
                    boolean punto = key ==46;
                    boolean ñ = key == 209 || key == 241; //código ISO (ñ y Ñ);
                    boolean VocMinTil = key==225 || key==233 || key==237 || key==243 || key==250 || key==252; //código ISO (á, é, í, ó, ú, ü);
                    boolean VocMayTil = key==193 || key==201 || key==205 || key==211 || key==218 || key==220; //código ISO (á, é, í, ó, ú, Ü);

                    if (!(minusculas || mayusculas || espacio || numeros || punto || ñ || VocMinTil || VocMayTil)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });

        } catch (Exception d) {
        }
    }
    public static void dinero(JTextField texto, int longitud) {
        try {
            texto.addKeyListener(new KeyAdapter() {
                @Override

                public void keyTyped(KeyEvent e) {
                    int key = e.getKeyChar();
                    boolean numeros = key >= 48 && key <= 57;
                    boolean punto = key ==46;

                    if (!(numeros || punto)) {
                        e.consume();
                    }
                    if (texto.getText().length() >= longitud) {
                        e.consume();
                    }
                }
            });
        } catch (Exception a) {
        }
    }

    public static boolean V_edad(Date nacimiento) {
        Date hoy = new Date();
        
        LocalDate lc_hoy = hoy.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate lc_nacimiento = nacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long dif_años = ChronoUnit.YEARS.between(lc_nacimiento, lc_hoy);
        if (nacimiento.compareTo(hoy) < 0 && (dif_años >=18 && dif_años <100 )) {
            return true;
        } else {
            return false;
            
        }

    }

}
