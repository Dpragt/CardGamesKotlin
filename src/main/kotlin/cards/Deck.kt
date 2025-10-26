package cards

class Deck {
    private val cards: MutableList<Card> = mutableListOf()

    init {
        reset()
    }

    // Reset naar een volledig deck van 52 kaarten
    fun reset() {
        cards.clear()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                cards.add(Card(suit, rank))
            }
        }
    }

    fun shuffle() {
        cards.shuffle()
    }

    fun getCard(): Card? {
        return if (cards.isNotEmpty()) {
            cards.removeAt(0)
        } else {
            null
        }
    }
}