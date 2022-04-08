package com.example.shoppinglist.domain

/**
 *  Тут не додаємо анотацію Entity від Room, щоб наш клас не залежав від стороннтоі ліби, так ми не будемо
 *  порушувати правила чистої архітектури, адже domain шар - не від чого не залежить
 */
data class ShopItem(
    val name: String,
    val count: Int,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}
