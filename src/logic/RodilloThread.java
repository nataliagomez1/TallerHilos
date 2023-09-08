package logic;

import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RodilloThread extends Thread{
    private boolean activo = false;
    private JLabel rodilloLabel;
    private String[] imagenes = {"Imagenes/img1.jpg", "Imagenes/img2.jpg", "Imagenes/img3.jpg","Imagenes/imgBonus.jpg","Imagenes/img4.jpg","Imagenes/img5.jpg"};
    private Timer temporizador;
    private long startTime;

    private String imagenActual;



    public RodilloThread(JLabel rodilloLabel) {
        this.rodilloLabel = rodilloLabel;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            if (activo) {
                String imagen = imagenes[random.nextInt(imagenes.length)];

                SwingUtilities.invokeLater(() -> {
                    ImageIcon icono = new ImageIcon(imagen);
                    rodilloLabel.setIcon(icono);
                    imagenActual = imagen;
                });

                try {
                    long elapsed = System.currentTimeMillis() - startTime;
                    if (elapsed > (40 * 1000) && elapsed <= (60 * 1000)) { // Si han pasado más de 40 segundos pero menos de 60
                        Thread.sleep(200); // Tiempo más largo entre cambios de imagen
                    } else {
                        Thread.sleep(100); // Tiempo regular entre cambios de imagen
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Thread.yield();
            }
        }
    }

    public void toggleActivo() {
        if (!activo) {  // Si el rodillo se está activando
            startTime = System.currentTimeMillis();
            temporizador = new Timer();
            temporizador.schedule(new TimerTask() {
                @Override
                public void run() {
                    detenerTemporizador();
                    activo = false;
                }
            }, 60 * 1000);  // 1 minuto en milisegundos
        } else {
            detenerTemporizador();
        }
        activo = !activo;
    }

    public void detenerTemporizador() {
        if (temporizador != null) {
            temporizador.cancel();
            temporizador.purge();
            temporizador = null;
        }
    }

    public String getImagenActual() {
        return imagenActual;
    }



}
