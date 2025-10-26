package blackjack

import cards.Card

interface BlackjackPlayerInterface {
    fun showMessage(message: String)
    fun askForAction(player: BlackJackPlayer): PlayerAction
    fun showHand(player: BlackJackPlayer)
    fun showDealerCard(card: Card)
}