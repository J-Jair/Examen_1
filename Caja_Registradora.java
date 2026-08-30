import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
public Caja_Registradora() {
        
        setSize(400, 400);
        setTitle("Caja Registradora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel EtDenominacion = new JLabel("Denominación");
        EtDenominacion.setBounds(90, 11, 100, 25);
        add(EtDenominacion);

        JLabel VaDevolver = new JLabel("Valor a devolver");
        VaDevolver.setBounds(10, 100, 100, 25);
        add(VaDevolver);

        
        Respuesta = new JComboBox<>();
        Respuesta.setBounds(210, 10, 100, 25);
        add(Respuesta);
        DefaultComboBoxModel ResDinero = new DefaultComboBoxModel(dinero);
        Respuesta.setModel(ResDinero);

        JButton BtAcExistencia = new JButton("Actualizar Existencia");
        BtAcExistencia.setBounds(10, 50, 160, 25);
        add(BtAcExistencia);

        JButton BtDevolver = new JButton("Devolver");
        BtDevolver.setBounds(250, 100, 100, 25);
        add(BtDevolver);

        Cantidad = new JTextField();
        Cantidad.setBounds(210, 50, 100, 25);
        add(Cantidad);

        CanDevolver = new JTextField();
        CanDevolver.setBounds(120, 100, 100, 25);
        add(CanDevolver);



        ResTabla = new DefaultTableModel(Tabla, 0);
        TablaResultados = new JTable(ResTabla);

        JScrollPane scrollPane = new JScrollPane(TablaResultados);
        scrollPane.setBounds(10, 150, 360,200);
        //scrollPane.setVisible(false);
        add(scrollPane);

    }
}