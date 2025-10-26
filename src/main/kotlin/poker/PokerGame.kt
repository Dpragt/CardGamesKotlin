package poker

import cards.Card
import cards.Deck
import IGame
import kotlin.collections.mutableListOf

// Bron: https://www.thesubath.com/poker/howtoplay/
class PokerGame(val players: MutableList<PokerPlayer>,
                val humanPlayer: PokerPlayer) : IGame {

    private val deck = Deck()
    private val communityCards = mutableListOf<Card>() // 5 kaarten op tafel
    private var pot: Double = 0.0
    private var stage: Int = 0 // 0 = preflop, 1 = flop, 2 = turn, 3 = river


    fun setup(){
        deck.reset()
        deck.shuffle()
        pot = 0.0
        stage = 0
        communityCards.clear()
        players.forEach { it.resetForNewRound()}
        humanPlayer.resetForNewRound()
        repeat(2){
            for(player in players){
                val card = deck.getCard()
                player.receiveCard(card!!)
            }
        }
        // vijf kaarten verborgen op tafel
        repeat(5){
            val card = deck.getCard()
            communityCards.add(card!!)
        }

        println("Kaarten zijn gedeeld.")
    }

    fun betting() {
        var highestBet = 0.0
        pot += 0.0 // just to be sure
        players.forEach {
            it.currentBet = 0.0
            it.hasRaisedThisRound = false
        }

        var bettingComplete = false
        var activePlayers = players.filter { !it.folded }

        // Start with first player
        var currentIndex = 0
        // keep going unit every bet equal
        while (!bettingComplete) {
            val player = activePlayers[currentIndex]
            if (!player.folded) {
                val toCall = highestBet - player.currentBet
                val canCheck = (highestBet == 0.0)

                // if human player: ask for actions
                val raised = if (player.name == humanPlayer.name)
                    handleHumanTurn(player, highestBet, toCall, canCheck)
                else
                    // simulate turn
                    handleBotTurn(player, highestBet, toCall, canCheck)

                if (raised) {
                    highestBet = player.currentBet
                    // Alle andere spelers moeten weer reageren
                    activePlayers.forEach { it.hasActedAfterLastRaise = false }
                    player.hasRaisedThisRound = true
                }

                player.hasActedAfterLastRaise = true
            }

            // Controleer of alle actieve spelers de hoogste inzet hebben gecalled/gecheckt
            bettingComplete = activePlayers
                .filter { !it.folded }
                .all { it.hasActedAfterLastRaise && it.currentBet == highestBet }

            // Volgende speler
            currentIndex = (currentIndex + 1) % activePlayers.size
        }

        println("Ronde klaar. Pot is nu: $pot")
    }

    private fun handleHumanTurn(player: PokerPlayer, highestBet: Double, toCall: Double, canCheck: Boolean): Boolean {
        showPlayerInfo(player, highestBet)

        val allowedActions = buildList {
            add("fold")
            if (canCheck) add("check") else add("call")
            if (!player.hasRaisedThisRound) add("raise")
        }

        println("Mogelijke acties: ${allowedActions.joinToString(" / ")}")
        print("Jouw keuze: ")

        val input = readLine()?.lowercase()?.trim()

        val action = if (input in allowedActions) input!! else {
            println("Ongeldige keuze, automatisch check/call.")
            if (canCheck) "check" else "call"
        }

        return when (action) {
            "fold" -> { player.fold(); false }
            "check" -> { player.check(); false }
            "call" -> {
                player.call(highestBet)
                pot += toCall
                false
            }
            "raise" -> {
                print("Voer raise bedrag in: ")
                val raiseAmount = readLine()?.toDoubleOrNull() ?: 0.0
                if (raiseAmount > 0) {
                    player.bet(raiseAmount + toCall)
                    pot += raiseAmount + toCall
                    player.hasRaisedThisRound = true
                    println(">> Nieuwe hoogste inzet: ${player.currentBet} door ${player.name}")
                    true
                } else {
                    println("Ongeldige raise, je callt in plaats daarvan.")
                    player.call(highestBet)
                    pot += toCall
                    false
                }
            }
            else -> false
        }
    }

    private fun handleBotTurn(player: PokerPlayer, highestBet: Double, toCall: Double, canCheck: Boolean): Boolean {
        if (player.hasRaisedThisRound) {
            // Bot mag niet nog een keer raisen
            // TODO: if (ToCall > 0) kans op fold
            val decision = if (toCall > 0) "call" else "check"
            return executeBotDecision(player, decision, highestBet, toCall)
        }

        val decision = when {
            player.folded -> "fold"
            toCall > 0 && (1..10).random() <= 2 -> "fold"
            canCheck && (1..10).random() <= 5 -> "check"
            toCall > 0 && (1..10).random() <= 8 -> "call"
            else -> "raise"
        }

        return executeBotDecision(player, decision, highestBet, toCall)
    }

    private fun executeBotDecision(player: PokerPlayer, decision: String, highestBet: Double, toCall: Double): Boolean {
        return when (decision) {
            "fold" -> { player.fold(); false }
            "check" -> { player.check(); false }
            "call" -> {
                player.call(highestBet)
                pot += toCall
                println("${player.name} callt $toCall.")
                false
            }
            "raise" -> {
                val raiseAmount = (10..100).random().toDouble()
                player.bet(raiseAmount + toCall)
                pot += raiseAmount + toCall
                println(">> ${player.name} raiset naar ${player.currentBet}")
                true
            }
            else -> false
        }
    }

    fun revealCommunityCards() {
        when (stage) {
            1 -> println("Flop: ${visibleCommunityCards()}")
            2 -> println("Turn: ${visibleCommunityCards()}")
            3 -> println("River: ${visibleCommunityCards()}")
        }
    }

    private fun visibleCommunityCards(): List<Card> {
        return when (stage) {
            0 -> communityCards.take(0)
            1 -> communityCards.take(3)
            2 -> communityCards.take(4)
            3 -> communityCards.take(5)
            else -> emptyList()
        }
    }


    private fun showPlayerInfo(player: PokerPlayer, highestBet: Double) {
        println("\n${player.name}, jouw beurt!")
        println("Je hand: ${player.hand}")
        println("Je chips: ${player.chips}")
        println("Community cards: ${visibleCommunityCards()}")
        println("Pot: $pot")
        println("Huidige hoogste inzet: $highestBet")
        println("Jouw huidige inzet: ${player.currentBet}")
    }


    override fun playRound() {
        players.add(humanPlayer)
        setup()

        // Pre-flop bets
        betting()
        stage++

        // Flop
        revealCommunityCards()
        betting()
        stage++

        // Turn
        revealCommunityCards()
        betting()
        stage++

        // River
        revealCommunityCards()
        betting()

        // TODO: hierna hand evaluatie (winnaar bepalen)
        println("\nRonde voorbij. Pot = $pot")
        players.forEach { it.revealHand()}
        println("TODO: Handsterktes vergelijken en winnaar bepalen.")
    }

}