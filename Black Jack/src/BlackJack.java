import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class BlackJack {
    private static final String[] INITIAL_VALUES = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final String[] INITIAL_TYPES = {"C", "D", "H", "S"};
    private static final int SPACE_BETWEEN_CARDS = 5;
    private ArrayList<Card> deck;
    private Random random = new Random();

    // dealer
    private Card hiddenCard;
    private ArrayList<Card> dealerHand;
    private int dealerSum;
    private int dealerAceCount;

    // player
    private ArrayList<Card> playerHand;
    private int playerSum;
    private int playerAceCount;

    // window
    private int boardWidth = 600;
    private int boardHeight = this.boardWidth;

    // ration 1/1.4
    private int cardWidth = 110;
    private int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // draw hidden card
            Image hiddenCardImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./cards/BACK.png"))).getImage();

            if (!stayButton.isEnabled()) {
                hiddenCardImg = new ImageIcon(Objects.requireNonNull(getClass().getResource(hiddenCard.getImagePath()))).getImage();
            }

            g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

            // draw dealer hand
            for (int i = 0; i < dealerHand.size(); i++) {
                Card card = dealerHand.get(i);
                Image cardImg = new ImageIcon(Objects.requireNonNull(getClass().getResource(card.getImagePath()))).getImage();
                g.drawImage(cardImg, cardWidth + 25 + (cardWidth + SPACE_BETWEEN_CARDS) * i, 20, cardWidth, cardHeight, null);
            }

            // draw player hand
            for (int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);
                Image cardImg = new ImageIcon(Objects.requireNonNull(getClass().getResource(card.getImagePath()))).getImage();
                g.drawImage(cardImg, 20 + (cardWidth + SPACE_BETWEEN_CARDS) * i, 320, cardWidth, cardHeight, null);

            }

            if (!stayButton.isEnabled()) {
                dealerSum = reduceDealerAce();
                playerSum = reducePlayerAce();

                System.out.println("STAY: ");
                System.out.println(dealerSum);
                System.out.println(playerSum);

                String message = "";
                if (playerSum > 21) {
                    message = "You Lose!";
                } else if (dealerSum > 21) {
                    message = "You Win!";
                } else if (playerSum == dealerSum) {
                    message = "Tie!";
                } else if (playerSum > dealerSum) {
                    message = "You Win!";
                } else {
                    message = "You Lose!";
                }

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString(message, 220, 250);
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");

    public BlackJack() {
        startGame();

        this.frame.setVisible(true);
        this.frame.setSize(boardWidth, boardHeight);
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gamePanel.setLayout(new BorderLayout());
        this.gamePanel.setBackground(new Color(53, 101, 77));

        this.frame.add(gamePanel);

        this.hitButton.setFocusable(false);
        this.buttonPanel.add(this.hitButton);

        this.stayButton.setFocusable(false);
        this.buttonPanel.add(this.stayButton);

        this.frame.add(this.buttonPanel, BorderLayout.SOUTH);

        this.hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);

                // will make the hit button unclickable
                if (reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }

                gamePanel.repaint(); // it's going to call paintComponent
            }
        });

        this.stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }

                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    public void startGame() {
        // deck
        buildDeck();
        shuffleDeck();

        // dealer
        dealer();

        // player
        player();
    }

    private void player() {
        this.playerHand = new ArrayList<Card>();
        this.playerSum = 0;
        this.playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            Card card = this.deck.remove(this.deck.size() - 1);
            this.playerSum += card.getValue();
            this.playerAceCount += card.isAce() ? 1 : 0;
            this.playerHand.add(card);
        }

        System.out.println("PLAYER:");
        System.out.println(this.playerHand);
        System.out.println(this.playerSum);
        System.out.println(this.playerAceCount);
    }

    private void buildDeck() {
        this.deck = new ArrayList<Card>();

        for (String type : INITIAL_TYPES) {
            for (String value : INITIAL_VALUES) {
                Card card = new Card(value, type);
                this.deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    private void shuffleDeck() {
        for (int i = 0; i < this.deck.size(); i++) {
            int randomPosition = random.nextInt(this.deck.size());
            Card currentCard = this.deck.get(i);
            Card ramdomCard = this.deck.get(randomPosition);

            // swap the cards
            deck.set(i, ramdomCard);
            deck.set(randomPosition, currentCard);
        }

        System.out.println("SHUFFLED CARDS");
        System.out.println(this.deck);
    }

    private void dealer() {
        this.dealerHand = new ArrayList<Card>();
        this.dealerSum = 0;
        this.dealerAceCount = 0;

        // hiddenCard
        this.hiddenCard = deck.remove(this.deck.size() - 1);
        this.dealerSum += this.hiddenCard.getValue();
        this.dealerAceCount += this.hiddenCard.isAce() ? 1 : 0;

        Card card = this.deck.remove(deck.size() - 1);
        this.dealerSum += card.getValue();
        this.dealerAceCount += card.isAce() ? 1 : 0;
        this.dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(this.hiddenCard);
        System.out.println(this.dealerHand);
        System.out.println(this.dealerSum);
        System.out.println(this.dealerAceCount);
    }

    private int reducePlayerAce() {
        while (this.playerSum > 21 && this.playerAceCount > 0) {
            this.playerSum -= 10;
            this.playerAceCount -= 1;
        }

        return this.playerSum;
    }

    private int reduceDealerAce() {
        while (this.dealerSum > 21 && this.dealerAceCount > 0) {
            this.dealerSum -= 10;
            this.dealerAceCount -= 1;
        }

        return this.dealerSum;
    }
}
