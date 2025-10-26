package blackjack

import cards.Card

class BlackJackPlayer (val name: String) {
    val hand: MutableList<Card> = mutableListOf()

    fun receiveCard(card: Card) {
        hand.add(card)
    }

    fun clearHand() {
        hand.clear()
    }

    fun handValue(): Int {
        var total = 0
        var aces = 0

        for (card in hand) {
            total += when (card.rank) {
                cards.Rank.JACK, cards.Rank.QUEEN, cards.Rank.KING -> 10
                cards.Rank.ACE -> {
                    aces++
                    11
                }
                else -> card.rank.value
            }
        }

        // aas kan 11 of 1 zijn
        while (total > 21 && aces > 0) {
            total -= 10
            aces--
        }

        return total
    }

    fun isBusted(): Boolean = handValue() > 21


}