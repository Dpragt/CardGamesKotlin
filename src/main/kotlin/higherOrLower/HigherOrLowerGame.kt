package higherOrLower

import IGame
import cards.Deck

class HigherOrLowerGame : IGame {

    val deck = Deck()
    var score: Int = 0

    override fun playRound() {
        resetTable()

        var currentCard = deck.getCard()!!
        println("De eerste kaart is: $currentCard")

        while (true) {
            // make a choice
            println("Denk je dat de volgende kaart hoger of lager is? ([1] Lager, [2] Hoger)")
            val input = readLine() ?: "2"

            // get next card
            val nextCard = deck.getCard() ?: break
            println("Volgende kaart: $nextCard")

            // validate
            val correct = when (input) {
                "1" -> nextCard.rank.value < currentCard.rank.value
                "2" -> nextCard.rank.value > currentCard.rank.value
                else -> false
            }

            // show result
            if (correct) {
                score++
                println("Goed geraden!")
                currentCard = nextCard
            } else {
                println("Fout! Spel afgelopen.")
                println("Jouw score: $score")
                // stop game
                break
            }
        }
    }

    // reset score en deck
    private fun resetTable() {
        score = 0
        deck.shuffle()
    }
}