package imArt;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import javax.sound.sampled.*;
import javax.swing.*;

class Surface extends JPanel{
    private Snake chaos;
    private Triangle order;
    private JButton button;
    private JLabel eyeInit;
    private Icon imgEyeInit = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("eye_start_medium.gif")));
    private JLabel eyeCont;
    private Icon imgExeCont = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("eye_cont_medium.gif")));
    private OrderRunnable orderRunnable;
    private ChaosRunnable chaosRunnable;
    private final String IDLE1S = "idle1.wav";
    private final String IDLE2S = "idle2.wav";
    private final String IDLE3S = "idle3.wav";
    private final String IDLE4S = "idle4.wav";
    private final String IDLE5S = "idle5.wav";
    private final String IDLE6S = "idle6.wav";
    private final String IDLE7S = "idle7.wav";
    private final String IDLE8S = "idle8.wav";
    private final String IDLE9S = "idle9.wav";

    Surface(int frameWidth, int frameHeight) {

        initSurface(frameWidth, frameHeight);
        orderRunnable = new OrderRunnable();
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (orderRunnable.chaosSpawned) return;
                orderRunnable.chaosSpawned = true;
                orderRunnable.clip.stop();
                chaosRunnable = new ChaosRunnable();
            }
        });
        button.addActionListener(e -> {
            if (orderRunnable.interrupted) return;
            eyeInit.setVisible(true);
            orderRunnable.interrupted = true;
            orderRunnable.clip.stop();
            chaosRunnable.interrupted = true;
            chaosRunnable.clip.stop();
        });
    }

    private void initSurface(int frameWidth, int frameHeight) {
        setLayout( new GridBagLayout() );
        Dimension btnAndGif = new Dimension(frameWidth / 8, frameHeight / 6);
        order = new Triangle(frameWidth, frameHeight);
        chaos = new Snake(frameWidth, frameHeight);
        button = new JButton();
        button.setPreferredSize(btnAndGif);
        eyeInit = new JLabel(imgEyeInit);
        eyeInit.setVisible(false);
        eyeCont = new JLabel(imgExeCont);
        eyeCont.setVisible(false);
        add(eyeInit, new GridBagConstraints());
        add(eyeCont, new GridBagConstraints());
        add(button, new GridBagConstraints());

    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setPaint(new Color(50, 50, 50));

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);
        order.draw(g);
        if (orderRunnable.chaosSpawned){
            chaos.draw(g);
        }
        g2d.dispose();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    class OrderRunnable implements Runnable {

        private Clip clip;
        private boolean chaosSpawned = false;
        private boolean interrupted = false;
        private ArrayList<String> initSounds = new ArrayList<>();
        private ArrayList<String> discussionSounds = new ArrayList<>();
        private ArrayList<String> waitLoopSounds = new ArrayList<>();

        OrderRunnable() {
            initFirstSounds();
            initLoopSounds();
            initDialogueSounds();
            initThread();
        }

        private void initFirstSounds(){
            initSounds.add(IDLE2S);
            initSounds.add("init1.wav");
            initSounds.add(IDLE2S);
            initSounds.add("init2.wav");
            initSounds.add(IDLE5S);
            initSounds.add("init3.wav");
            initSounds.add(IDLE7S);
        }

        private void initLoopSounds(){
            for (int i = 1; i <=5; i++){
                waitLoopSounds.add("repeat" + i +".wav");
            }
        }

        private void initDialogueSounds(){
            discussionSounds.add("dialogue1.wav");
            discussionSounds.add(IDLE2S);
            discussionSounds.add("dialogue3.wav");
            discussionSounds.add(IDLE4S);
            discussionSounds.add("dialogue5.wav");
            discussionSounds.add(IDLE9S);
            discussionSounds.add(IDLE1S);
            discussionSounds.add("dialogue7.wav");
            discussionSounds.add(IDLE6S);
            discussionSounds.add("dialogue9.wav");
            discussionSounds.add(IDLE7S);
            discussionSounds.add("dialogue11.wav");
            discussionSounds.add(IDLE9S);
            discussionSounds.add(IDLE8S);
            discussionSounds.add("dialogue13.wav");
            discussionSounds.add(IDLE1S);
            discussionSounds.add("dialogue15.wav");
            discussionSounds.add(IDLE8S);
            discussionSounds.add("dialogue17.wav");
            discussionSounds.add(IDLE8S);
            discussionSounds.add("dialogue19.wav");
            discussionSounds.add(IDLE8S);
            discussionSounds.add("dialogue21.wav");
            discussionSounds.add(IDLE9S);
            discussionSounds.add("end1.wav");
        }


        private void initThread() {
            Thread runner = new Thread(this);
            runner.start();
        }


        private void playSound(String path){
            boolean idle = path.contains("idle");
            try {
                if (!idle){
                    order.setColor(Color.RED);
                    repaint();
                }
                CountDownLatch syncLatch = new CountDownLatch(1);

                try (AudioInputStream stream = AudioSystem.getAudioInputStream(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(path)))) {
                    clip = AudioSystem.getClip();

                    // Listener which allow method return once sound is completed
                    clip.addLineListener(e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            syncLatch.countDown();
                            if (!idle){
                                order.setColor(Color.BLACK);
                                repaint();
                            }
                            clip.close();
                        }
                    });
                    clip.open(stream);
                    clip.start();
                }
                syncLatch.await();
            } catch (InterruptedException | LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            for(String file : initSounds) {
                if (interrupted || chaosSpawned) break;
                playSound(file);
            }
            while (!interrupted && !chaosSpawned) {
                Random r = new Random();
                if (interrupted || chaosSpawned) break;
                playSound(waitLoopSounds.get(r.nextInt(5)));
                if (interrupted || chaosSpawned) break;
                playSound(IDLE7S);
            }
            for(String file : discussionSounds) {
                if (interrupted) break;
                playSound(file);
            }

            while (!interrupted){
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            playSound( "consequence.wav");
        }
    }

    class ChaosRunnable implements Runnable {

        private Clip clip;
        private ArrayList<String> discussionSounds = new ArrayList<>();
        private boolean interrupted = false;

        ChaosRunnable() {
            initDiscussionSounds();
            initThread();
        }

        private void initDiscussionSounds(){
            discussionSounds.add(IDLE3S);
            discussionSounds.add("dialogue2.wav");
            discussionSounds.add(IDLE3S);
            discussionSounds.add("dialogue4.wav");
            discussionSounds.add(IDLE3S);
            discussionSounds.add("dialogue6.wav");
            discussionSounds.add(IDLE6S);
            discussionSounds.add("dialogue8.wav");
            discussionSounds.add(IDLE5S);
            discussionSounds.add("dialogue10.wav");
            discussionSounds.add(IDLE7S);
            discussionSounds.add(IDLE9S);
            discussionSounds.add("dialogue12.wav");
            discussionSounds.add(IDLE2S);
            discussionSounds.add("dialogue14.wav");
            discussionSounds.add(IDLE3S);
            discussionSounds.add("dialogue16.wav");
            discussionSounds.add(IDLE3S);
            discussionSounds.add("dialogue18.wav");
            discussionSounds.add(IDLE9S);
            discussionSounds.add(IDLE4S);
            discussionSounds.add("dialogue20.wav");
            discussionSounds.add(IDLE6S);
            discussionSounds.add(IDLE9S);
            discussionSounds.add("end2.wav");
        }

        private void initThread() {
            Thread runner = new Thread(this);
            runner.start();
        }

        private void playSound(String path){
            boolean idle = path.contains("idle");
            try {
                if (!idle){
                    chaos.setColor(Color.RED);
                    repaint();
                }
                CountDownLatch syncLatch = new CountDownLatch(1);

                try (AudioInputStream stream = AudioSystem.getAudioInputStream(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(path)))) {
                    clip = AudioSystem.getClip();

                    // Listener which allow method return once sound is completed
                    clip.addLineListener(e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            syncLatch.countDown();
                            if (!idle){
                                chaos.setColor(Color.BLACK);
                                repaint();
                            }
                            clip.close();
                        }
                    });
                    clip.open(stream);
                    clip.start();
                }
                syncLatch.await();
            } catch (InterruptedException | LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            for (String file : discussionSounds){
                if (interrupted) break;
                playSound(file);
            }
            while (!interrupted) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            button.setVisible(false);
            repaint();
            eyeInit.setVisible(true);
            repaint();
            AudioInputStream inputStream = null;
            try {
                inputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("endSong.wav")));
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Clip clip = null;
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            try {
                clip.open(inputStream);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eyeInit.setVisible(false);
            repaint();
            eyeCont.setVisible(true);
            repaint();
        }
    }
}

