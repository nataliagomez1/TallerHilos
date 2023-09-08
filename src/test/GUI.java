package test;

import logic.RodilloThread;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class GUI extends JFrame {
    private JButton iniciarDetener = new JButton("Iniciar/Detener");
    public static JLabel rodillo1 = new JLabel();
    public static JLabel rodillo2 = new JLabel();
    public static JLabel rodillo3 = new JLabel();
    private RodilloThread rodilloThread1;
    private RodilloThread rodilloThread2;
    private RodilloThread rodilloThread3;
    private int deposito = 0; // Valor del depósito
    //private JMenuBar menuBar = new JMenuBar();
    //private JMenu menuDeposito = new JMenu("Depósito");
    //private JMenuItem menuItemHacerDeposito = new JMenuItem("Hacer depósito");
    //private JMenuItem menuItemVerDeposito = new JMenuItem("Ver depósito");
    private JProgressBar barraDeposito = new JProgressBar();
    private JButton btnDepositar = new JButton("Hacer Depósito");

    private JPanel panelSuperior = new JPanel(new FlowLayout());

    JButton btnDetenerRodillo1 = new JButton("Detener 1");
    JButton btnDetenerRodillo2 = new JButton("Detener 2");
    JButton btnDetenerRodillo3 = new JButton("Detener 3");
    private JButton btnHacerApuesta = new JButton("Hacer Apuesta");

    private JButton btnVerGanancia = new JButton("Ganancia");
    private int apuestaActual = 0;  // Valor actual de la apuesta





    public GUI() {
        setBackground(new Color(244,217,236));
        barraDeposito.setMinimum(0);
        barraDeposito.setMaximum(100);
        barraDeposito.setStringPainted(true);
        panelSuperior.add(barraDeposito, BorderLayout.CENTER);
        //panelSuperior.add(btnDepositar, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        btnDepositar.addActionListener(e -> realizarDeposito());

        mostrarPoliticasGanancia();
        mostrarInstrucciones();



        setTitle("Tragamonedas Simple");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelRodillos = new JPanel(new FlowLayout());


        cargarYEstablecerImagen("imagenes/img1.jpg", rodillo1);
        cargarYEstablecerImagen("imagenes/img2.jpg", rodillo2);
        cargarYEstablecerImagen("imagenes/img3.jpg", rodillo3);

        panelRodillos.add(rodillo1);
        panelRodillos.add(rodillo2);
        panelRodillos.add(rodillo3);
        add(panelRodillos, BorderLayout.CENTER);

        JPanel panelControl = new JPanel(new FlowLayout());

        panelControl.add(btnDetenerRodillo1);
        panelControl.add(btnDetenerRodillo2);
        panelControl.add(btnDetenerRodillo3);
        panelControl.add(btnDepositar);
        panelControl.add(btnHacerApuesta);
        panelControl.add(btnVerGanancia);
        configurarBotonApuesta();   // Configura el botón de apuesta
        configurarBotonGanancia();

        add(panelControl, BorderLayout.SOUTH);



        panelControl.add(iniciarDetener);
        btnDetenerRodillo1.addActionListener(e -> rodilloThread1.toggleActivo());
        btnDetenerRodillo2.addActionListener(e -> rodilloThread2.toggleActivo());
        btnDetenerRodillo3.addActionListener(e -> rodilloThread3.toggleActivo());


        iniciarDetener.addActionListener(e -> toggleRodillo());
         rodilloThread1 = new RodilloThread(rodillo1);
        rodilloThread2 = new RodilloThread(rodillo2);
        rodilloThread3 = new RodilloThread(rodillo3);
        rodilloThread1.start();
        rodilloThread2.start();
        rodilloThread3.start();
    }

    private void cargarYEstablecerImagen(String ruta, JLabel label) {
        ImageIcon icono = new ImageIcon(ruta);
        label.setIcon(icono);
    }
    private void configurarBotonApuesta() {
        // Evento del botón para hacer la apuesta
        btnHacerApuesta.addActionListener(e -> realizarApuesta());
    }

    private void configurarBotonGanancia() {
        // Evento del botón para hacer la apuesta
        btnVerGanancia.addActionListener(e -> calcularGanancias());
    }


    private void toggleRodillo() {
        rodilloThread1.toggleActivo();
        rodilloThread2.toggleActivo();
        rodilloThread3.toggleActivo();

    }
    private String leerDesdeArchivo(String rutaArchivo) {
        Path path = Paths.get(rutaArchivo);
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + rutaArchivo, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    private void mostrarPoliticasGanancia() {
        String mensaje = leerDesdeArchivo("archivostxt/politicas.txt");

        if (mensaje == null) {
            return;
        }

        Object[] opciones = {"Aceptar", "Rechazar"};
        int respuesta = JOptionPane.showOptionDialog(this, mensaje, "Políticas de ganancia", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, opciones, null);

        if (respuesta == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }
    private void mostrarInstrucciones() {
        String mensaje = leerDesdeArchivo("archivostxt/instrucciones.txt");
        if (mensaje == null) {
            return;
        }

        JOptionPane.showMessageDialog(this, mensaje, "Instrucciones del juego", JOptionPane.INFORMATION_MESSAGE);
    }

    private void realizarApuesta() {
        String valor = JOptionPane.showInputDialog(this, "Introduce el valor de tu apuesta:", "Hacer Apuesta", JOptionPane.QUESTION_MESSAGE);

        if (valor != null && !valor.trim().isEmpty()) { // Si el jugador no cancela o deja vacío el cuadro de diálogo
            try {
                int valorApuesta = Integer.parseInt(valor);
                if (valorApuesta > 0 && valorApuesta <= deposito) {
                    apuestaActual = valorApuesta;
                    JOptionPane.showMessageDialog(this, "Apuesta realizada con éxito. Valor apostado: " + apuestaActual);
                } else {
                    JOptionPane.showMessageDialog(this, "El valor de la apuesta debe ser positivo y no puede exceder el depósito actual.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido.");
            }
        }
    }


    private void realizarDeposito() {
        try {
            int cantidadDeposito = Integer.parseInt(JOptionPane.showInputDialog("Ingresa la cantidad a depositar:"));
            if (cantidadDeposito > 0&&cantidadDeposito<101) {
                deposito += cantidadDeposito;
                barraDeposito.setValue(deposito);  // Actualiza la JProgressBar

                JOptionPane.showMessageDialog(this, "Depósito exitoso. Total depositado: " + deposito);
            } else {
                JOptionPane.showMessageDialog(this, "El depósito debe ser mayor que 0 y máximo 100.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido.");
        }
    }


    public void calcularGanancias() {
        String imagen1 = rodilloThread1.getImagenActual();
        String imagen2 = rodilloThread2.getImagenActual();
        String imagen3 = rodilloThread3.getImagenActual();

        int ganancia = 0;
        if (imagen1.equals(imagen2) && imagen2.equals(imagen3)) {
            if (imagen1.equals("imagenes/imgBonus.jpg")) {
                ganancia = apuestaActual * 8;
            } else {
                ganancia = apuestaActual * 6;
            }
        } else if ((imagen1.equals(imagen2) || imagen1.equals(imagen3) || imagen2.equals(imagen3))) {
            if (imagen1.equals("imagenes/imgBonus.jpg") || imagen2.equals("imagenes/imgBonus.jpg") || imagen3.equals("imagenes/imgBonus.jpg")) {
                ganancia = apuestaActual * 4;
            } else {
                ganancia = apuestaActual * 3;
            }
        }

        if (ganancia > 0) {
            JOptionPane.showMessageDialog(this, "Has ganado: " + ganancia);
            deposito += ganancia;  // Añade la ganancia al depósito
        } else {
            JOptionPane.showMessageDialog(this, "No has ganado en esta ronda.");
            deposito -= apuestaActual;  // Resta la apuesta del depósito
        }
        actualizarBarraDeposito();
    }

    private void actualizarBarraDeposito() {
        barraDeposito.setValue(deposito);
    }










}
