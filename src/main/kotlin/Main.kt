import blackjack.BlackJackPlayer
import blackjack.BlackjackGame
import blackjack.ConsoleBlackjackInterface
import higherOrLower.HigherOrLowerGame
import poker.PokerGame
import poker.PokerPlayer

fun main() {
    val blackjack = BlackjackGame(
        mutableListOf(
            BlackJackPlayer("Daniël"),
            BlackJackPlayer("Jaron"),
            BlackJackPlayer("Paul"),
            BlackJackPlayer("Thijs"),
            BlackJackPlayer("Remco"),
        ),
        ConsoleBlackjackInterface()
    )

    val poker = PokerGame(
        mutableListOf(
            PokerPlayer("Bot 1"),
            PokerPlayer("Bot 2"),
            PokerPlayer("Bot 3"),
        ),
        PokerPlayer("Daniël")
    )

    val higherOrLower = HigherOrLowerGame()

    val games = mapOf<String, IGame>(
        "1" to blackjack,
        "2" to poker,
        "3" to higherOrLower
    )

    println(" Welkom bij Kaartspellen!")
    println("")

    while (true) {
        println("Kies een spel:")
        println("1. Blackjack")
        println("2. Poker (Onder ontwikkeling!)")
        println("3. Higher or Lower")
        println("4. Stoppen")

        println("Maak je keuze (1-4): ")
        var input = readLine()?.trim()
        when (input) {
            "1", "2", "3" -> {
                val game = games[input]
                playGame(game)
            }
            "4" -> {
                println("Tot ziens!")
                break
            }
            else -> println("Ongeldige keuze, probeer opnieuw.")
        }
    }
}

fun playGame(game: IGame?) {
    if (game == null) return
    println("\nJe speelt nu: ${game::class.simpleName}")

    while (true) {
        game.playRound()
        println("\nWat wil je nu doen?")
        println("1. Nog een ronde")
        println("2. Ander spel kiezen")
        println("3. Stoppen")

        when (readLine()?.trim()) {
            "1" -> continue
            "2" -> return
            "3" -> {
                println("👋 Tot ziens!")
                kotlin.system.exitProcess(0)
            }
            else -> println("Ongeldige keuze, probeer opnieuw.")
        }
    }
}
