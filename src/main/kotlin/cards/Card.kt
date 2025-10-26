package cards

data class Card(
    val suit: Suit,
    val rank: Rank
) {
    override fun toString(): String {
        return "${rank.displayName} van ${suit.displayName}"
    }
}

enum class Suit(val displayName: String) {
    HEARTS("Harten"),
    DIAMONDS("Ruiten"),
    CLUBS("Klaveren"),
    SPADES("Schoppen")
}

enum class Rank(val displayName: String, val value: Int) {
    ACE("Aas", 1),
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("Boer", 11),
    QUEEN("Vrouw", 12),
    KING("Heer", 13)
}
