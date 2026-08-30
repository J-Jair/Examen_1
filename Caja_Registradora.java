import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;

public class Caja_Registradora extends JFrame {
    private Integer[] dinero = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100,50};
    private String[] Tabla = {"Cantidad","Prestación","Denominación"};
    private JComboBox<Integer> Respuesta;
    private JTextField Cantidad, CanDevolver;
    private JTable TablaResultados;
    private DefaultTableModel ResTabla;
    private int[] existenciasActualizadas = new int[dinero.length];
    private JButton BtAcExistencia, BtDevolver;
public Caja_Registradora() {
        
        setSize(400, 400);
        setTitle("Caja Registradora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        //Etiquetas
        JLabel EtDenominacion = new JLabel("Denominación");
        EtDenominacion.setBounds(90, 11, 100, 25);
        add(EtDenominacion);

        JLabel VaDevolver = new JLabel("Valor a devolver");
        VaDevolver.setBounds(10, 100, 100, 25);
        add(VaDevolver);

        // Desplegable
        Respuesta = new JComboBox<>();
        Respuesta.setBounds(210, 10, 100, 25);
        add(Respuesta);
        DefaultComboBoxModel ResDinero = new DefaultComboBoxModel(dinero);
        Respuesta.setModel(ResDinero);

        //Botones
        BtAcExistencia = new JButton("Actualizar Existencia");
        BtAcExistencia.setBounds(10, 50, 160, 25);
        add(BtAcExistencia);

        BtDevolver = new JButton("Devolver");
        BtDevolver.setBounds(250, 100, 100, 25);
        add(BtDevolver);
        //Caja de texto
        Cantidad = new JTextField();
        Cantidad.setBounds(210, 50, 100, 25);
        add(Cantidad);

        CanDevolver = new JTextField();
        CanDevolver.setBounds(120, 100, 100, 25);
        add(CanDevolver);

        //Tabla

        ResTabla = new DefaultTableModel(Tabla, 0);
        TablaResultados = new JTable(ResTabla);

        JScrollPane scrollPane = new JScrollPane(TablaResultados);
        scrollPane.setBounds(10, 150, 360,200);
        //scrollPane.setVisible(false);
        add(scrollPane);
        Actualizar_Valores ();
    }
    private void Actualizar_Valores () {

        Respuesta.addActionListener(e -> {
            int indice = Respuesta.getSelectedIndex();
            if (indice >= 0) {
                int valorGuardado = existenciasActualizadas[indice];
                Cantidad.setText(String.valueOf(valorGuardado));
            }
        });
        BtAcExistencia.addActionListener(e -> {
            int indice = Respuesta.getSelectedIndex();
            if (indice >= 0) {
                try {
                    int valorIngresado = Integer.parseInt(Cantidad.getText());
                    existenciasActualizadas[indice] = valorIngresado;
                    if (valorIngresado < 0) {
                        JOptionPane.showMessageDialog(this, 
                            "No se pueden ingresar números negativos.", 
                            "Valor inválido", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    existenciasActualizadas[indice] = valorIngresado;
                    JOptionPane.showMessageDialog(this, 
                        "Existencia actualizada correctamente.", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);

                
                } catch (NumberFormatException ex) {
                   
                    JOptionPane.showMessageDialog(this,"Debe ingresar un número entero válido (sin letras ni símbolos).", 
                        "Error de formato", 
                        JOptionPane.ERROR_MESSAGE);
                        
                        
                }
            }
        });
    }
}
