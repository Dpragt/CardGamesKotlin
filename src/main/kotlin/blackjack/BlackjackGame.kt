package blackjack

import IGame

class BlackjackGame(
    private val players: List<blackjack.BlackJackPlayer>,
    private val blackjackInterface: BlackjackPlayerInterface
) : IGame {

    private val deck = cards.Deck()
    private val dealer = blackjack.BlackJackPlayer("Dealer")

    init {
        deck.shuffle()
    }

    fun startRound() {
        // Reset hands
        dealer.clearHand()
        players.forEach { it.clearHand() }

        // get 2 cards for each player and the dealer
        for (i in 1..2) {
            players.forEach { it.receiveCard(deck.getCard()!!) }
            dealer.receiveCard(deck.getCard()!!)
        }
        // show hands
        // dealer only reveals 1 card
        blackjackInterface.showDealerCard(dealer.hand.first())
        players.forEach { blackjackInterface.showHand(it) }
    }

    fun playerTurn(player: BlackJackPlayer) {
        blackjackInterface.showHand(player)

        while (true) {
            val action = blackjackInterface.askForAction(player)

            if (action.type == PlayerActionType.HIT) {
                val card = deck.getCard()!!
                player.receiveCard(card)
                blackjackInterface.showMessage("${player.name} trekt $card")
                blackjackInterface.showHand(player)

                if (player.isBusted()) {
                    // player cant play anymore when busted
                    blackjackInterface.showMessage("${player.name} gaat kapot (bust)!")
                    break
                }

                // If not busted, the game goes on
            } else {
                blackjackInterface.showMessage("${player.name} past.")
                break
            }
        }
    }

    private fun playerChoice(player: BlackJackPlayer): String {
        println("${player.name} (${player.handValue()}), kies een actie: (hit = 1, pass = 2)")
        return readln()
    }

    fun dealerTurn() {
        blackjackInterface.showMessage("\nDealer speelt...")
        blackjackInterface.showHand(dealer)
        while (dealer.handValue() < 17) {
            val card = deck.getCard()!!
            dealer.receiveCard(card)
            blackjackInterface.showMessage("Dealer trekt $card")
            blackjackInterface.showHand(dealer)
        }
        if (dealer.isBusted()) {
            blackjackInterface.showMessage("Dealer gaat kapot (bust)!")
        }
    }

    fun determineWinners() {
        val dealerValue = dealer.handValue()
        println("Dealer eindwaarde: $dealerValue")

        for (player in players) {
            val playerValue = player.handValue()
            when {
                player.isBusted() -> println("${player.name} verliest (bust).")
                dealer.isBusted() -> println("${player.name} wint! Dealer bust.")
                playerValue > dealerValue -> println("${player.name} wint!")
                playerValue == dealerValue -> println("${player.name} speelt gelijk.")
                else -> println("${player.name} verliest.")
            }
        }
    }

    override fun playRound() {
        startRound()

        // Speler speelt een ronde
        players.forEach {
            playerTurn(it)
        }

        // Beurt voor dealer
        dealerTurn()

        // Resultaten
        determineWinners()
    }
}