package PJ4;

import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */


/* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */


public class VideoPoker {

    // default constant values
    private static final int startingBalance = 1000;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 9, 25, 50, 250};
    private static final String[] goodHandTypes = {
            "Royal Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
            "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;
    private HashMap<Integer, Card> holdCards;
    private Scanner userInput;
    boolean payOutTable = true;

    /**
     * default constructor, set balance = startingBalance
     */
    public VideoPoker() {
        this(startingBalance);
        playerBalance = startingBalance;
        userInput = new Scanner(System.in);
        holdCards = new HashMap<>();
    }

    /**
     * constructor, set given balance
     */
    public VideoPoker(int balance) {
        this.playerBalance = balance;
        oneDeck = new Decks(1);
    }

    /**
     * This display the payout table based on multipliers and goodHandTypes arrays
     */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table   	      Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /**
     * Check current playerHand using multipliers and goodHandTypes arrays
     * Must print yourHandType (default is "Sorry, you lost") at the end of function.
     * This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {
        // implement this method!
        if (RoyalFlush()) {
            System.out.println("Royal Flush!");
            playerBalance += multipliers[8] * playerBet;
        } else if (StraightFlush()) {
            System.out.println("Straight Flush!");
            playerBalance += multipliers[7] * playerBet;
        } else if (Straight()) {
            System.out.println("Straight!");
            playerBalance += multipliers[3] * playerBet;
        } else if (FullHouse()) {
            System.out.println("Full House!");
            playerBalance += multipliers[5] * playerBet;
        } else if (Flush()) {
            System.out.println("Flush!");
            playerBalance += multipliers[4] * playerBet;
        } else if (OfAKind(4)) {
            System.out.println("Four of a Kind!");
            playerBalance += multipliers[6] * playerBet;
        } else if (OfAKind(3)) {
            System.out.println("Three of a Kind!");
            playerBalance += multipliers[2] * playerBet;
        } else if (TwoPair()) {
            System.out.println("Two Pair!");
            playerBalance += multipliers[1] * playerBet;
        } else if (RoyalPair()) {
            System.out.println("Royal Pair!");
            playerBalance += multipliers[0] * playerBet;
        } else {
            System.out.println("Sorry, you lost !");
        }
    }

    /*************************************************
     *   add additional private methods here ....
     *
     *************************************************/




    private boolean RoyalFlush() {
        int firstcardsuit = playerHand.get(0).getSuit();
        List<Integer> royalflushranklist = Arrays.asList(1, 10, 11, 12, 13);

        for (Card card : playerHand) {
            if (card.getSuit() != firstcardsuit || !royalflushranklist.contains(card.getRank()))
                return false;
        }

        return true;
    }

    private boolean StraightFlush() {
        int firstcardsuit = playerHand.get(0).getSuit();
        List<Integer> sortedCardRanks = new ArrayList<>();
        for (Card card : playerHand) {
            sortedCardRanks.add(card.getRank());
        }
        Collections.sort(sortedCardRanks);

        for (Card card : playerHand) {
            if (card.getSuit() != firstcardsuit)
                return false;
        }

        for (int i = 0; i < 4; i++) {
            if (!(sortedCardRanks.get(i) == (sortedCardRanks.get(i + 1) - 1)))
                return false;
        }

        return true;
    }

    private boolean Straight() {
        int firstcardsuit = playerHand.get(0).getSuit();
        List<Integer> sortedCardRanks = new ArrayList<>();
        List<Integer> cardSuits = new ArrayList<>();

        for (Card card : playerHand) {
            sortedCardRanks.add(card.getRank());
            cardSuits.add(card.getSuit());
        }
        Collections.sort(sortedCardRanks);
        Set<Integer> suitSet = new HashSet<>(cardSuits);

        if (suitSet.size() > cardSuits.size())
            return false;

        for (int i = 0; i < 4; i++) {
            if (!(sortedCardRanks.get(i) == (sortedCardRanks.get(i + 1) - 1)))
                return false;
        }

        return true;
    }

    private boolean Flush() {
        int firstcardsuit = playerHand.get(0).getSuit();

        for (Card card : playerHand) {
            if (card.getSuit() != firstcardsuit)
                return false;
        }

        return true;
    }

    private boolean FullHouse() {
        HashMap<Integer, Integer> rankMap = new HashMap<>();

        for (Card card : playerHand)
            if (!rankMap.containsKey(card.getRank())) {
                rankMap.put(card.getRank(), 1);
            } else {
                int value = rankMap.get(card.getRank());
                rankMap.put(card.getRank(), value + 1);
            }

        return rankMap.containsValue(3) && rankMap.containsValue(2);
    }

    private boolean OfAKind(int kindType) {
        HashMap<Integer, Integer> rankMap = new HashMap<>();

        for (Card card : playerHand) {
            if (!rankMap.containsKey(card.getRank())) {
                rankMap.put(card.getRank(), 1);
            } else {
                int value = rankMap.get(card.getRank());
                rankMap.put(card.getRank(), value + 1);
            }
        }

        return rankMap.containsValue(kindType);
    }

    private boolean TwoPair() {
        HashMap<Integer, Integer> rankMap = new HashMap<>();
        int pairCounter = 0;

        for (Card card : playerHand) {
            if (!rankMap.containsKey(card.getRank())) {
                rankMap.put(card.getRank(), 1);
            } else {
                int value = rankMap.get(card.getRank());
                rankMap.put(card.getRank(), value + 1);
                pairCounter++;
            }
        }

        return pairCounter == 2 && rankMap.containsValue(1);
    }

    private boolean RoyalPair() {
        HashMap<Integer, Integer> rankMap = new HashMap<>();
        int pairCounter = 0;

        for (Card card : playerHand) {
            if (!rankMap.containsKey(card.getRank())) {
                rankMap.put(card.getRank(), 1);
                pairCounter = 1;
            } else {
                int value = rankMap.get(card.getRank());
                rankMap.put(card.getRank(), value + 1);
                pairCounter++;
            }
        }

        return pairCounter == 2 && rankMap.containsValue(1);
    }

    private void getPlayerBet() {
        System.out.print("Enter bet: ");
        try {
            playerBet = userInput.nextInt();

            if (playerBet > playerBalance) {
                System.out.println("\nBet is larger than your balance! Try again");
                getPlayerBet();
            }
        } catch (InputMismatchException e) {
            System.out.println("\nPlease enter only integers!");
            getPlayerBet();
        }
    }

    private void updateBalance() {
        playerBalance -= playerBet;
    }

    private void dealHand() {
        try {
            playerHand = oneDeck.deal(numberOfCards);
        } catch (PlayingCardException e) {
            System.out.println("PlayingCardException: " + e.getMessage());
        }
    }

    private void PositionRetention() {
        System.out.print("Enter positions of cards to keep: ");

        userInput = new Scanner(System.in);
        String input = userInput.nextLine();

        if (input.isEmpty()) {
            return;
        }

        String[] positionsToKeep = input.trim().split("\\s+");

        try {
            for (int i = 0; i < positionsToKeep.length; i++) {
                int position = Integer.parseInt(positionsToKeep[i]) - 1;
                Card card = playerHand.get(position);
                holdCards.put(position, card);
            }
        } catch (Exception e) {
            System.out.println("\nPlease input integers 1-5 only. Try again");
            PositionRetention();
        }
    }


    private void setAndDisplayNewPlayerHand() {
        dealHand();
        for (Map.Entry<Integer, Card> card : holdCards.entrySet()) {
            playerHand.set(card.getKey(), card.getValue());
        }

        System.out.println(playerHand.toString());
    }


    private void retry() {
        System.out.println("\nDo you want to play a new game? y/n");
        userInput = new Scanner(System.in);

        String input = userInput.nextLine();
        if (input.equals("y")) {
            DisplayCheckoutTable();
            play();
        } else if (input.equals("n")) {
            quit();
        } else {
            System.out.println("Please enter y/n");
            retry();
        }
    }

    private void DisplayCheckoutTable() {
        System.out.println("\nWant to see payout table y/n");
        String input = userInput.nextLine();

        if (input.equals("n")) {
            payOutTable = false;
        }
    }

    private void quit() {
        showBalance();
        System.out.println("\n Good Bye.");
        System.exit(0);
    }

    private void showBalance() {
        System.out.println("\nBalance: $" + playerBalance);
    }



    public void play() {
        /** The main algorithm for single player poker game
         *
         * Steps:
         * 		showPayoutTable()
         *
         *
         * 		++
         * 		show balance, get bet
         *		verify bet value, update balance
         *		reset deck, shuffle deck,
         *		deal cards and display cards
         *		ask for positions of cards to keep
         *          get positions in one input line
         *		update cards
         *		check hands, display proper messages
         *		update balance if there is a payout
         *		if balance = O:
         *			end of program
         *		else
         *			ask if the player wants to play a new game
         *			if the answer is "no" : end of program
         *			else : showPayoutTable() if user wants to see it
         *			goto ++
         */

        // implement this method!
        if (payOutTable) {
            showPayoutTable();
        }

        System.out.println("\n\n-----------------------------------");
        showBalance();
        getPlayerBet();
        updateBalance();
        oneDeck.reset();
        oneDeck.shuffle();
        dealHand();
        System.out.println(playerHand.toString());
        PositionRetention();
        setAndDisplayNewPlayerHand();
        checkHands();
        showBalance();

        if (playerBalance == 0) {
            quit();
        } else {

            retry();
        }
    }

    // Creating the Hand
//        while (playing) {
//            System.out.println("Balance: " + playerBalance);
//            placeBet();
//            oneDeck.shuffle();
//            try {
//                playerHand = new ArrayList<Card>(oneDeck.deal(5));
//            } catch (PlayingCardException exc) {
//                exc.printStackTrace();
//            }
//            System.out.println("Hand: " + playerHand);
//            playerHand = discard();
//            System.out.println("Hand: " + playerHand);
//            checkHands(); // CheckDeck
//            payUp(result);
//            playerHand.clear();
//            System.out.println("\nYour balance: $" + playerBalance);
//            // Options to play more
//            if ((playerBalance > 0) && (tryAgain())) {
//                askIfShowPayoutTable();
//            } else {
//                System.out.println("Your balance is: $" + getBalance());
//                System.out.println("You suck! Bye!");
//                playing = false;
//            }
//        }
//    }
//            String[] StringArray = temp.split(" ");
//            System.out.println("Hand:" + playerHand);
//            int[] arr = new int[StringArray.length];
//            for (int i = 0; i < StringArray.length; i++) {
//                String numString = StringArray[i];
//                arr[i] = Integer.parseInt(numString);
//            }
//            boolean position[] = new boolean[numberOfCards];
//            Arrays.fill(position, false);
//            for (int i = 0; i < arr.length; i++) {
//                int k = arr[i];
//                position[k - 1] = true;
//            }

    // Adjusting the Balance
//    private void payUp(int payUp) {
//        if (payUp == 9) {
//            playerBalance = playerBalance - playerBet; // deduct each game
//        } else if (payUp != 9) {
//            playerBalance = playerBalance + (playerBet * multipliers[payUp]); // earn your winnings if you win
//        }
//    }
//
//    private int getBalance() {
//        return this.playerBalance;
//    }
//
//    private int getBet() {
//        return playerBet;
//    }

    // Make the bet
//    private void placeBet() {
//        Scanner in = new Scanner(System.in);
//        boolean hasFunds = true;
//        do {
//            System.out.print("Enter bet: ");
//            playerBet = in.nextInt();
//            if (playerBet > playerBalance) {
//                hasFunds = false;
//                System.out.println("Not enough funds. Enter a smaller amount.");
//            } else
//                hasFunds = true;
//        } while (!hasFunds);
//    }

    //Ask if play again
    public static boolean tryAgain() {

        boolean askAgain = true;
        boolean playAgain = true;
        Scanner in = new Scanner(System.in);
        while (askAgain) {
            System.out.println("One more game (y or n)?");
            String continuePlaying = in.next();
            if (continuePlaying.equalsIgnoreCase("y")) askAgain = false;
            else if (continuePlaying.equalsIgnoreCase("n")) {
                playAgain = false;
                askAgain = false;
            } else
                System.out.println("Please answer y/n");
        }
        return playAgain;
    }

    // ask if show payout table
    private void askIfShowPayoutTable() {

        boolean askAgain = true;
        boolean playAgain = true;
        Scanner in = new Scanner(System.in);

        while (askAgain) {
            System.out.println("Show payout table? (y or n)?");
            String continuePlaying = in.next();
            if (continuePlaying.equalsIgnoreCase("y")) {
                askAgain = false;
            } else if (continuePlaying.equalsIgnoreCase("n")) {
                playAgain = false;
                askAgain = false;
            } else
                System.out.println("Please answer y/n");
        }
        if (playAgain) showPayoutTable();
    }


    /*************************************************
     *   Do not modify methods below
     /*************************************************

     /** testCheckHands() is used to test checkHands() method
     *  checkHands() should print your current hand type
     */

    public void testCheckHands() {

        try {

            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(3, 1));
            playerHand.add(new Card(3, 10));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(3, 11));
            playerHand.add(new Card(3, 13));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0, new Card(3, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight
            playerHand.set(4, new Card(1, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Flush
            playerHand.set(4, new Card(3, 5));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	",
            // "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };

            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(3, 8));
            playerHand.add(new Card(0, 8));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(1, 8));
            playerHand.add(new Card(2, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(3, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(1, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(1, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Royal Pair
            playerHand.set(0, new Card(1, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // non Royal Pair
            playerHand.set(2, new Card(3, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) {
        VideoPoker pokergame = new VideoPoker();
        pokergame.testCheckHands();
    }
}
