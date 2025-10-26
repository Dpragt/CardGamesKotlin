package blackjack

import cards.Card

class ConsoleBlackjackInterface : BlackjackPlayerInterface {

    override fun showMessage(message: String) {
        println(message)
    }

    override fun showHand(player: BlackJackPlayer) {
        println("${player.name} heeft: ${player.hand.joinToString(", ")}")
        println("Totaal punten: ${player.handValue()}")
    }

    override fun showDealerCard(card: Card) {
        println("Dealer toont: $card")
    }

    override fun askForAction(player: BlackJackPlayer): PlayerAction {
        println("\n${player.name}, wat wil je doen? [1] Hit [2] Stand")
        val input = readLine() ?: "2"
        return when (input) {
            "1" -> PlayerAction(PlayerActionType.HIT)
            else -> PlayerAction(PlayerActionType.STAND)
        }
    }
}
