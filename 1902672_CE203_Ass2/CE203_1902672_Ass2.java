package abc;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;


public class CE203_1902672_Ass2 {
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Game ex = new Game();
            ex.setVisible(true);
        });
    }
    public static class Game extends JFrame {

        public Game() {

            initUI();
        }

        private void initUI() {

            add(new Board());

            setTitle("Covid : Battle for Valhalla 1902672");
            setSize( 400, 300);

            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }


    }



    public static class Person extends Shape {

        private int dx;
        private int dy;
        private List<Sanitizer> sanitizers;

        public Person(int x, int y) {
            super(x, y);

            initPerson();
        }

        private void initPerson() {

            sanitizers = new ArrayList<>();
            loadImage("masked_man.jpg");
            getImageDimensions();
        }

        public void move() {
            x = (x>=0&&x<=250)?(x+dx):(x);
            if(x<0){
                x = 0;
            } else if(x>250){
                x=250;
            }
            y = (y>=20&&y<=225)?(y+dy):(y);
            if(y<20){
                y = 20;
            } else if(y>225){
                y=225;
            }
        }

        public List<Sanitizer> getSanitizers() {
            return sanitizers;
        }

        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                fire();
            }

            if (key == KeyEvent.VK_LEFT) {
                dx = -3;
            }

            if (key == KeyEvent.VK_RIGHT) {
                dx = 3;
            }

            if (key == KeyEvent.VK_UP) {
                dy = -5;
            }

            if (key == KeyEvent.VK_DOWN) {

                dy = 5;

            }
        }

        public void fire() {
            sanitizers.add(new Sanitizer(x + width, y + height / 2));
        }

        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                dx = 0;
            }

            if (key == KeyEvent.VK_RIGHT) {
                dx = 0;
            }

            if (key == KeyEvent.VK_UP) {
                dy = 0;
            }

            if (key == KeyEvent.VK_DOWN) {
                dy = 0;
            }
        }
    }


    public static class Covid extends Shape {


        public final int INITIAL_X = 400;

        public Covid(int x, int y) {
            super(x, y);

            initCovid();
        }

        private void initCovid() {


            loadImage("covidboss.png");
            getImageDimensions();
        }

        public void move() {

            if (x < 0) {
                x = INITIAL_X;
            }

            x -= 1;
        }
    }



    public static class Shape {

        protected int x;
        protected int y;
        protected int width;
        protected int height;
        protected boolean visible;
        protected Image image;

        public Shape(int x, int y) {

            this.x = x;
            this.y = y;
            visible = true;
        }

        protected void loadImage(String imageName) {

            ImageIcon ii = new ImageIcon(imageName);
            image = ii.getImage();
        }

        protected void getImageDimensions() {

            width = image.getWidth(null);
            height = image.getHeight(null);
        }

        public Image getImage() {
            return image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(Boolean visible) {
            this.visible = visible;
        }

        public Rectangle getBounds(){
            return new Rectangle(x,y,width,height);
        }
    }



    public static class Sanitizer extends Shape {

        public Sanitizer(int x, int y) {
            super(x, y);

            initSanitizer();
        }


        private void initSanitizer() {

            loadImage("Projectile.png");
            getImageDimensions();
        }

        public void move() {

            int sanitizer_SPEED = 5;
            x += sanitizer_SPEED;

            int BOARD_WIDTH = 390;
            if (x > BOARD_WIDTH) {
                visible = false;
            }
        }
    }


    public static class AppendToFile {
        public AppendToFile(Integer scorer) {
            try {
                FileWriter out = new FileWriter("HighScores",true);
                out.write(scorer +"\n");
                out.close();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }



        }

    }



    public static class Board extends JPanel implements ActionListener {

        private Timer timer;
        private Person persona;
        private boolean ingame;
        private List<Covid> carriers;
        List<Integer> highScores;
        private boolean gamestarted;



        public Board() {
            initBoard();
        }

        private void initBoard() {




            addKeyListener(new TAdapter());
            setBackground(Color.BLACK);
            setFocusable(true);
            ingame = true;
            gamestarted = false;

            int ICRAFT_X = 0;
            int ICRAFT_Y = 0;
            persona = new Person(ICRAFT_X, ICRAFT_Y);

            initInfected();

            int DELAY = 10;
            timer = new Timer(DELAY, this);
            timer.start();
        }

        public void initInfected() {

            carriers = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                int x = (int) (((Math.random()*990)+500));
                int y = (int) (Math.random()*225);

                carriers.add(new Covid(x,y));
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if(gamestarted == false) {
                drawGameBegin(g);
            }
            else {
                if (ingame) {
                    drawObjects(g);
                } else {
                    try {
                        drawGameOver(g);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
            Toolkit.getDefaultToolkit().sync();
        }
        private void drawGameBegin(Graphics g){
            int b_WIDTH = 400;
            int b_HEIGHT = 300;
            String msg = "\nCOVID: Battle for Valhalla ";
            String msg1 = "\nPress Spacebar to Fire Sanitizer \ntowards Oncoming Covid";
            String msg2 ="\nRemember: \nDo NOT get close to the COVID";
            String msg3  =        "\nPress Spacebar to Begin";
            Font small = new Font("Helvetica", Font.BOLD, 10);
            FontMetrics fm = getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (b_WIDTH - fm.stringWidth(msg)) / 2,
                    b_HEIGHT / 2);
            g.drawString(msg1, (b_WIDTH - fm.stringWidth(msg1)) / 2,
                    (b_HEIGHT / 2)+10);
            g.drawString(msg2, (b_WIDTH - fm.stringWidth(msg2)) / 2,
                    (b_HEIGHT / 2)+20);
            g.drawString(msg3, (b_WIDTH - fm.stringWidth(msg3)) / 2,
                    (b_HEIGHT / 2)+30);
        }
        private void drawGameOver(Graphics g) throws FileNotFoundException {

            int b_WIDTH = 400;
            int b_HEIGHT = 300;
            int scoreb = pleasee();
            highestscores(scoreb);



            if (carriers.size() > 0) {
                int score = (9 - carriers.size());

                String msg = "Game Over: You did not Stay 6 feet Apart";
                String msg2 = "Your Score :" + score;
                Font small = new Font("Helvetica", Font.BOLD, 14);
                Font big = new Font("Times", Font.BOLD, 20);
                FontMetrics fm = getFontMetrics(small);

                g.setColor(Color.white);
                g.setFont(small);
                g.drawString(msg, (b_WIDTH - fm.stringWidth(msg)) / 2,
                        b_HEIGHT / 2);
                g.setFont(big);
                g.drawString(msg2, (b_WIDTH - fm.stringWidth(msg2)) / 2,
                        (b_HEIGHT / 2) + 50);

                //highestscores(score);

            } else if (carriers.size()==0) {
                int score = ((9 - carriers.size())+ 1);
                String msg = "Congratulations You Beat Covid";
                String msg2 = "Your score :" + score;
                Font small = new Font("Helvetica", Font.BOLD, 14);
                Font big = new Font("Times", Font.BOLD, 20);
                FontMetrics fm = getFontMetrics(small);

                g.setColor(Color.GREEN);
                g.setFont(small);
                g.drawString(msg, (b_WIDTH - fm.stringWidth(msg)) / 2,
                        b_HEIGHT / 2);
                g.setFont(big);
                g.drawString(msg2, (b_WIDTH - fm.stringWidth(msg2)) / 2,
                        (b_HEIGHT / 2) + 50);

                //highestscores(score);

            }



        }

        private void drawObjects(Graphics g) {

            if (persona.isVisible()) {
                g.drawImage(persona.getImage(), persona.getX(), persona.getY(),
                        this);
            }

            List<Sanitizer> ms = persona.getSanitizers();

            for (Sanitizer sanitizer : ms) {
                if (sanitizer.isVisible()) {
                    g.drawImage(sanitizer.getImage(), sanitizer.getX(),
                            sanitizer.getY(), this);
                }
            }

            for (Covid carrier : carriers) {
                if (carrier.isVisible()) {
                    g.drawImage(carrier.getImage(), carrier.getX(), carrier.getY(), this);
                }
            }

            g.setColor(Color.WHITE);
            g.drawString("Carrriers left: " + carriers.size(), 5, 15);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ingame();
            updateSanitizers();
            updatePerson();
            updateCovid();

            checkCollision();

            repaint();
        }
        private void gamestarted(){
            if (ingame){
                gamestarted = true;
            }
        }
        private void ingame() {
            if (!ingame) {
                timer.stop();
            }
        }

        private void updateSanitizers() {

            List<Sanitizer> sanitizers = persona.getSanitizers();

            for (int i = 0; i < sanitizers.size(); i++) {

                Sanitizer sanitizer = sanitizers.get(i);

                if (sanitizer.isVisible()) {

                    sanitizer.move();
                } else {

                    sanitizers.remove(i);
                }
            }
        }

        public  int pleasee() {
            int b = 0;

            if (carriers.size() > 0) {
                b =  (9 - carriers.size());
                return b;
            }
            else if (carriers.size() == 0) {
                b =  (9 - carriers.size()) +1;
                return b;
            }
            return b;
        }

        private void highestscores(int scored) throws FileNotFoundException {

            int scor = 9 - carriers.size();
            Scanner a = new Scanner("HighScores");

        /*highScores = new ArrayList<>();

        a.nextLine();

        while ((a.hasNext())) {
            highScores.add(Integer.parseInt(a.nextLine()));
        }

        if (highScores.size() < 5) {
            highScores.add(scor);
            new AppendToFile(highScores.get(1));
        }
        else {
            for (Integer hs : highScores) {
                int sc = Integer.parseInt(String.valueOf(hs));
                if (scor > sc) {
                    highScores.add(scor);
                    highScores.remove(sc);
                    break;
                }
            }
        }
            new AppendToFile(highScores.get(1));

         */


            new AppendToFile(scored);


        }







        private void updatePerson() {
            if (persona.isVisible()) {
                persona.move();
            }
        }
        private void updateCovid() {

            if (carriers.isEmpty()) {
                ingame = false;
                return;
            }



            for (int i = 0; i < carriers.size(); i++) {

                Covid a = carriers.get(i);

                if (a.isVisible()) {
                    a.move();
                } else {
                    carriers.remove(i);
                }
            }
        }
        public void checkCollision() {

            Rectangle r3 = persona.getBounds();

            for (Covid carrier : carriers) {

                Rectangle r2 = carrier.getBounds();

                if (r3.intersects(r2)) {

                    persona.setVisible(false);
                    carrier.setVisible(false);
                    ingame = false;
                }
            }
            List<Sanitizer> ms = persona.getSanitizers();

            for (Sanitizer m : ms) {

                Rectangle r1 = m.getBounds();

                for (Covid carrier : carriers) {

                    Rectangle r2 = carrier.getBounds();

                    if (r1.intersects(r2)) {

                        m.setVisible(false);
                        carrier.setVisible(false);
                    }
                }
            }
        }

        private class TAdapter extends KeyAdapter {

            @Override
            public void keyReleased(KeyEvent e) {

                persona.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    gamestarted();
                }
                persona.keyPressed(e);
            }



        }
    }




}
