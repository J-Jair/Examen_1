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

    // Arreglo con las denominaciones disponibles en el sistema monetario
    private final Integer[] dinero = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};
    private final String[] columnasTabla = {"Cantidad", "Presentación", "Denominación"};
    
    // Declaración de componentes gráficos de la interfaz
    private JComboBox<Integer> cbDenominaciones;
    private JTextField txtCantidad, txtCanDevolver;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton btnActualizarExistencia, btnDevolver;
    private JScrollPane scrollPane;

    // Vector paralelo para llevar el control de las existencias de cada denominación
    private final int[] existenciasActualizadas = new int[dinero.length];

    public Caja_Registradora() {
        // Configuración básica de la ventana principal
        setSize(400, 400);
        setTitle("Caja Registradora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // Etiqueta para el selector de denominación
        JLabel lblDenominacion = new JLabel("Denominación");
        lblDenominacion.setBounds(90, 11, 100, 25);
        add(lblDenominacion);

        // Etiqueta para el campo del valor a devolver
        JLabel lblVaDevolver = new JLabel("Valor a devolver");
        lblVaDevolver.setBounds(10, 100, 100, 25);
        add(lblVaDevolver);

        // ComboBox desplegable con las opciones de dinero
        cbDenominaciones = new JComboBox<>();
        cbDenominaciones.setBounds(210, 10, 100, 25);
        add(cbDenominaciones);
        DefaultComboBoxModel<Integer> modeloDinero = new DefaultComboBoxModel<>(dinero);
        cbDenominaciones.setModel(modeloDinero);

        // Botón para actualizar el inventario manual
        btnActualizarExistencia = new JButton("Actualizar Existencia");
        btnActualizarExistencia.setBounds(10, 50, 160, 25);
        add(btnActualizarExistencia);

        // Botón para ejecutar el cálculo de la devolución
        btnDevolver = new JButton("Devolver");
        btnDevolver.setBounds(250, 100, 100, 25);
        add(btnDevolver);

        // Campo de texto para la cantidad de existencias
        txtCantidad = new JTextField();
        txtCantidad.setBounds(210, 50, 100, 25);
        add(txtCantidad);

        // Campo de texto para ingresar el dinero que se va a devolver
        txtCanDevolver = new JTextField();
        txtCanDevolver.setBounds(120, 100, 100, 25);
        add(txtCanDevolver);

        // Configuración de la tabla y su contenedor con barra de desplazamiento
        modeloTabla = new DefaultTableModel(columnasTabla, 0);
        tablaResultados = new JTable(modeloTabla);

        scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBounds(10, 150, 360, 200);
        scrollPane.setVisible(false);
        add(scrollPane);

        // Llamada al método que maneja las acciones de los componentes
        inicializarEventos();
    }

    // Método encargado de agrupar los eventos de los botones y listas desplegables
    private void inicializarEventos() {
        // Evento para mostrar la cantidad actual según la denominación seleccionada
        cbDenominaciones.addActionListener(e -> {
            int indice = cbDenominaciones.getSelectedIndex();
            if (indice >= 0) {
                txtCantidad.setText(String.valueOf(existenciasActualizadas[indice]));
            }
        });

        // Evento para guardar el valor ingresado en el stock de la denominación elegida
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

        // Acción al hacer clic en el botón Devolver
        btnDevolver.addActionListener(e -> realizarDevolucion());
    }

    // Algoritmo principal para calcular el desglose del cambio
    private void realizarDevolucion() {
        try {
            int valorRestante = Integer.parseInt(txtCanDevolver.getText().trim());
            
            // Validamos que el número ingresado sea positivo
            if (valorRestante <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Ingrese un valor mayor a cero para devolver.", 
                    "Valor inválido", 
                    JOptionPane.ERROR_MESSAGE);
                scrollPane.setVisible(false);
                return;
            }

            // Validamos que alcance al menos para la denominación mínima de 50
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

            // Recorremos el arreglo de dinero de mayor a menor para optimizar el cambio
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

            // Validamos si la caja no tenía nada para desglosar
            if (!seRealizoDesglose) {
                scrollPane.setVisible(false);
                JOptionPane.showMessageDialog(this, 
                    "No hay existencias en la caja para realizar la devolucion solicitada.", 
                    "Fondos insuficientes", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mostramos la tabla si el proceso avanzó correctamente
            scrollPane.setVisible(true);

            // Si queda un sobrante que no se pudo cubrir con el stock actual
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