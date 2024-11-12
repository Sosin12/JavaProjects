package bankapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



/**
 *
 * @author vadim
 */


public class BankApp extends JFrame {
    private double balance = 1000.00;
    private JLabel balanceLabel;
    private JTextField depositField;
    private ArrayList<Snowflake> snowflakes;
    private Timer snowflakeTimer;
    private Random random;
    
    private Image background;

    public BankApp() {
        setTitle("Новогодний банк");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        initSnowflakes();
    }

    private void initUI() {
        JPanel panel = new JPanel() {
            // Переопределяем метод для добавления фонового изображения
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Задаем праздничный фон
                    background = ImageIO.read( BankApp.class.getResourceAsStream("./bg.jpeg") );
                } catch (IOException ex) {
                    Logger.getLogger(BankApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                
                // Рисуем снежинки
                for (Snowflake snowflake : snowflakes) {
                    snowflake.draw(g);
                }
            }
        };
        
        panel.setLayout(null);

        // Надпись баланса
        balanceLabel = new JLabel("Баланс: $" + balance);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setBounds(30, 20, 200, 25);
        panel.add(balanceLabel);

        // Поле для ввода суммы пополнения
        depositField = new JTextField();
        depositField.setBounds(30, 60, 200, 25);
        panel.add(depositField);

        // Кнопка для пополнения баланса
        JButton depositButton = new JButton("Пополнить");
        depositButton.setBounds(30, 100, 200, 25);
        depositButton.addActionListener(new DepositActionListener());
        panel.add(depositButton);

        add(panel);
    }

    private void initSnowflakes() {
        snowflakes = new ArrayList<>();
        random = new Random();

        // Создаем снежинки
        for (int i = 0; i < 30; i++) {
            snowflakes.add(new Snowflake(random.nextInt(getWidth()), random.nextInt(getHeight()), random.nextInt(4) + 1));
        }

        // Таймер для обновления анимации снежинок
        snowflakeTimer = new Timer(30, e -> {
            for (Snowflake snowflake : snowflakes) {
                snowflake.update(getHeight());
            }
            repaint();
        });
        snowflakeTimer.start();
    }

    private class DepositActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double amount = Double.parseDouble(depositField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Введите положительное значение.");
                    return;
                }
                balance += amount;
                balanceLabel.setText("Баланс: $" + balance);
                depositField.setText("");
                JOptionPane.showMessageDialog(null, "Баланс успешно пополнен!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Пожалуйста, введите корректную сумму.");
            }
        }
    }

    // Класс для создания снежинок
    private class Snowflake {
        private int x, y, speed;

        public Snowflake(int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        public void update(int height) {
            y += speed;
            if (y > height) {
                y = 0;
                x = random.nextInt(getWidth());
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillOval(x, y, 5, 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankApp app = new BankApp();
            app.setVisible(true);
        });
    }
}

