package blackjack

data class PlayerAction(
    val type: PlayerActionType
)

enum class PlayerActionType {
    HIT,
    STAND
}