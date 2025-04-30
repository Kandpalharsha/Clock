import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class clock extends JPanel implements Runnable {

    private final int centerX = 250;
    private final int centerY = 250;
    private final int radius = 200;
    private final JLabel digitalClock;

    public clock(JLabel digitalClock) {
        this.digitalClock = digitalClock;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analog + Digital Clock");
        frame.setSize(520, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel digitalLabel = new JLabel("", SwingConstants.CENTER);
        digitalLabel.setFont(new Font("Arial", Font.BOLD, 36));
        digitalLabel.setForeground(Color.BLUE);
        digitalLabel.setPreferredSize(new Dimension(500, 60));

        clock clockPanel = new clock(digitalLabel);
        frame.add(digitalLabel, BorderLayout.NORTH);
        frame.add(clockPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        Thread t = new Thread(clockPanel);
        t.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAnalogClock(g);
    }

    private void drawAnalogClock(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Outer circle
        g2.setColor(Color.BLUE);
        g2.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // Inner circle
        g2.setColor(Color.RED);
        g2.fillOval(centerX - (radius - 50), centerY - (radius - 50), 2 * (radius - 50), 2 * (radius - 50));

        // Clock numbers
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i * 30) - 90);
            int x = (int) (centerX + (radius - 30) * Math.cos(angle));
            int y = (int) (centerY + (radius - 30) * Math.sin(angle));
            String num = String.valueOf(i);
            int width = g2.getFontMetrics().stringWidth(num);
            g2.drawString(num, x - width / 2, y + 5);
        }

        // Time info
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);

        // Hands
        drawHand(g2, sec * 6, radius - 40, Color.RED, 1); // seconds
        drawHand(g2, min * 6, radius - 60, Color.GREEN, 3); // minutes
        drawHand(g2, (hour * 30) + (min / 2), radius - 100, Color.WHITE, 5); // hours
    }

    private void drawHand(Graphics2D g2, int angleDegree, int length, Color color, int thickness) {
        double angle = Math.toRadians(angleDegree - 90);
        int x = (int) (centerX + length * Math.cos(angle));
        int y = (int) (centerY + length * Math.sin(angle));
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawLine(centerX, centerY, x, y);
    }

    @Override
    public void run() {
        while (true) {
            repaint();

            // Update digital clock label
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            digitalClock.setText(formatter.format(new Date()));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
