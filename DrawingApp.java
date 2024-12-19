import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingApp::new);
    }

    public DrawingApp() {
        JFrame frame = new JFrame("Рисование пером");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        DrawingPanel drawingPanel = new DrawingPanel();
        frame.add(drawingPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        JMenu colorMenu = new JMenu("Цвет");
        String[] colors = {"Чёрный", "Красный", "Синий", "Зелёный"};
        Color[] colorValues = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN};
        for (int i = 0; i < colors.length; i++) {
            Color color = colorValues[i];
            JMenuItem colorItem = new JMenuItem(colors[i]);
            colorItem.addActionListener(e -> drawingPanel.setPenColor(color));
            colorMenu.add(colorItem);
        }
        menuBar.add(colorMenu);

        JMenu thicknessMenu = new JMenu("Толщина линии");
        int[] thicknesses = {1, 3, 5, 10};
        for (int thickness : thicknesses) {
            JMenuItem thicknessItem = new JMenuItem(thickness + " px");
            thicknessItem.addActionListener(e -> drawingPanel.setPenThickness(thickness));
            thicknessMenu.add(thicknessItem);
        }
        menuBar.add(thicknessMenu);

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    static class DrawingPanel extends JPanel {
        private int lastX, lastY;
        private Color penColor = Color.BLACK;
        private int penThickness = 1;
        private Image image;
        private Graphics2D graphics;

        public DrawingPanel() {
            setDoubleBuffered(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastX = e.getX();
                    lastY = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();
                    if (graphics != null) {
                        graphics.drawLine(lastX, lastY, x, y);
                        repaint();
                        lastX = x;
                        lastY = y;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) {
                image = createImage(getWidth(), getHeight());
                graphics = (Graphics2D) image.getGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clear();
            }
            g.drawImage(image, 0, 0, null);
        }

        public void setPenColor(Color color) {
            penColor = color;
            if (graphics != null) {
                graphics.setColor(penColor);
            }
        }

        public void setPenThickness(int thickness) {
            penThickness = thickness;
            if (graphics != null) {
                graphics.setStroke(new BasicStroke(penThickness));
            }
        }

        public void clear() {
            if (graphics != null) {
                graphics.setPaint(Color.WHITE);
                graphics.fillRect(0, 0, getWidth(), getHeight());
                graphics.setPaint(penColor);
            }
            repaint();
        }
    }
}
