package com.example.shoppinglist.presentation.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglist.domain.ShopItem

// Це клас, який проводить порівняння старого і нового об'єкту. Використовувався у RecyclerView Adapter
class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    /**
     * Якщо навість усі поля у нового айтема і старого різні, але id однакові - тже це один і той самий елемент, у якому
     * ми просто змінили поля. Якщо поверне false то це об'єкт треба перемалювати, інакше нічого не робити
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    /**
     * Перевіряє саме вміст елементів. В нашому випадку, коли ми змінили стан з активного на неактивний.
     * Якщо поверне false то це об'єкт треба перемалювати, інакше нічого не робити. Оскільки ми працюємо з
     * Data класами, у який перевизначено метод equals, то ми можемо викликати oldItem == newItem і відбудеться
     * первірка по всіх полях ци об'єктів. Якщо всі однакові поверне true, інакше false
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

}