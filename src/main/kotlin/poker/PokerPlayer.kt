package poker

import cards.Card

class PokerPlayer(
    val name: String,
    var chips: Double = 1000.0 // standaard startbedrag
) {
    val hand: MutableList<Card> = mutableListOf()
    var currentBet: Double = 0.0
    var folded: Boolean = false
    var hasActedAfterLastRaise: Boolean = false
    var hasRaisedThisRound: Boolean = false


    fun receiveCard(card: Card) {
        hand.add(card)
    }

    fun resetForNewRound() {
        hand.clear()
        currentBet = 0.0
        folded = false
    }

    fun fold() {
        folded = true
        println("$name heeft gepast (fold).")
    }

    fun check() {
        println("$name checkt.")
    }

    fun call(amountToCall: Double) {
        val amount = amountToCall - currentBet
        if (amount <= 0) {
            println("$name heeft al genoeg ingezet.")
            return
        }

        if (amount >= chips) {
            // Speler heeft niet genoeg om volledig te callen → all-in
            println("$name gaat ALL-IN met $chips (had $amount nodig).")
            currentBet += chips
            chips = 0.0
            return
        }

        // Normale call
        chips -= amount
        currentBet += amount
        println("$name callt $amount (resterend: $chips)")
    }

    fun bet(amount: Double) {
        // TODO: controleer aantal chips
        if (amount > chips) {
            println("$name heeft niet genoeg chips!")
            return
        }

        chips -= amount
        currentBet += amount
        println("$name zet $amount in (resterend: $chips)")
    }

    override fun toString(): String {
        return "$name (${chips} chips)"
    }

    fun revealHand() {
        println("$name's  hand: ${hand.joinToString(", ")}")
    }
}
