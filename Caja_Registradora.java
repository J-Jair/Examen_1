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

public class Caja_Registradora extends JFrame {
    private final Integer[] dinero = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};
    private final String[] columnasTabla = {"Cantidad", "Presentación", "Denominación"};
    private JComboBox<Integer> cbDenominaciones;
    private JTextField txtCantidad, txtCanDevolver;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private final int[] existenciasActualizadas = new int[dinero.length];
    private JButton btnActualizarExistencia, btnDevolver;
    private JScrollPane scrollPane;

    public Caja_Registradora() {
        setSize(400, 400);
        setTitle("Caja Registradora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel lblDenominacion = new JLabel("Denominación");
        lblDenominacion.setBounds(90, 11, 100, 25);
        add(lblDenominacion);

        JLabel lblVaDevolver = new JLabel("Valor a devolver");
        lblVaDevolver.setBounds(10, 100, 100, 25);
        add(lblVaDevolver);

        cbDenominaciones = new JComboBox<>();
        cbDenominaciones.setBounds(210, 10, 100, 25);
        add(cbDenominaciones);
        DefaultComboBoxModel<Integer> modeloDinero = new DefaultComboBoxModel<>(dinero);
        cbDenominaciones.setModel(modeloDinero);

        btnActualizarExistencia = new JButton("Actualizar Existencia");
        btnActualizarExistencia.setBounds(10, 50, 160, 25);
        add(btnActualizarExistencia);

        btnDevolver = new JButton("Devolver");
        btnDevolver.setBounds(250, 100, 100, 25);
        add(btnDevolver);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(210, 50, 100, 25);
        add(txtCantidad);

        txtCanDevolver = new JTextField();
        txtCanDevolver.setBounds(120, 100, 100, 25);
        add(txtCanDevolver);

        modeloTabla = new DefaultTableModel(columnasTabla, 0);
        tablaResultados = new JTable(modeloTabla);

        scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBounds(10, 150, 360, 200);
        scrollPane.setVisible(false);
        add(scrollPane);

        inicializarEventos();
    }

    private void inicializarEventos() {
        cbDenominaciones.addActionListener(e -> {
            int indice = cbDenominaciones.getSelectedIndex();
            if (indice >= 0) {
                txtCantidad.setText(String.valueOf(existenciasActualizadas[indice]));
            }
        });

        btnActualizarExistencia.addActionListener(e -> {
            int indice = cbDenominaciones.getSelectedIndex();
            if (indice >= 0) {
                try {
                    int valorIngresado = Integer.parseInt(txtCantidad.getText());
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
                    JOptionPane.showMessageDialog(this, 
                        "Debe ingresar un número entero válido (sin letras ni símbolos).", 
                        "Error de formato", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDevolver.addActionListener(e -> realizarDevolucion());
    }

    private void realizarDevolucion() {
        try {
            int valorRestante = Integer.parseInt(txtCanDevolver.getText().trim());
            
            if (valorRestante <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Ingrese un valor mayor a cero para devolver.", 
                    "Valor inválido", 
                    JOptionPane.ERROR_MESSAGE);
                scrollPane.setVisible(false);
                return;
            }

            if (valorRestante < 50) {
                JOptionPane.showMessageDialog(this, 
                    "El valor a devolver es menor a la denominación mínima (50).", 
                    "Valor demasiado bajo", 
                    JOptionPane.WARNING_MESSAGE);
                scrollPane.setVisible(false);
                return;
            }

            modeloTabla.setRowCount(0);
            boolean seRealizoDesglose = false;

            for (int i = 0; i < dinero.length; i++) {
                int denominacion = dinero[i];
                int stockDisponible = existenciasActualizadas[i];

                if (valorRestante >= denominacion && stockDisponible > 0) {
                    int cantidadNecesaria = valorRestante / denominacion;
                    int cantidadAUsar = Math.min(cantidadNecesaria, stockDisponible);

                    if (cantidadAUsar > 0) {
                        String presentacion = (denominacion >= 1000) ? "Billete" : "Moneda";
                        Object[] fila = { cantidadAUsar, presentacion, denominacion };
                        modeloTabla.addRow(fila);
                        valorRestante -= (denominacion * cantidadAUsar);
                        seRealizoDesglose = true;
                    }
                }
            }

            if (!seRealizoDesglose) {
                scrollPane.setVisible(false);
                JOptionPane.showMessageDialog(this, 
                    "No hay existencias en la caja para realizar la devolucion solicitada.", 
                    "Fondos insuficientes", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            scrollPane.setVisible(true);

            if (valorRestante > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Se entregó cambio parcial. Faltan por entregar: " + valorRestante, 
                    "Cambio incompleto", 
                    JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            scrollPane.setVisible(false);
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese un número válido en 'Valor a Devolver'.", 
                "Error de formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}