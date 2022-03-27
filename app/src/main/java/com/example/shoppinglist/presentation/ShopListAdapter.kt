package com.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
    var count = 0

    var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            /**
             * У diffResult вже є всі зміни які треба зробити recyclerView, щоб їх виконати,
             * реба викликати метод dispatchUpdatesTo і передати туди адаптер. А оскільки ми вже знаходимося
             * в адаптері. то передаємо просто this. Це треба використовуваи заміть notifyDataSetChanged.
             * Тепер на видалення буде викликано метод notifyItemRemoved, а якщо змінився, то notifyItemChanged, і т.д
             */
            diffResult.dispatchUpdatesTo(this)

            field = value

            /**
             * ЧОМУ НЕ ВАРТО ВИКОРИСТОВУВАТИ notifyDataSetChanged
             * на будь-яку зміну у нас буде перестоверно наш список, тобто наш recylcerView на кожні зміну буде
             * 10 раз викликати стоврення View Holder. Але навіщо нам перестоврювати всі 10 елементів, якщо
             * змінився тільки один
             */
//            notifyDataSetChanged()
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d("ShopListAdapter", "Recreate_ViewHolder, count: ${++count}")

        val shopItem = shopList[position]

        holder.itemShopName.text = shopItem.name
        holder.itemShopCount.text = shopItem.count.toString()

        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }

//        if (shopItem.enabled) {
//            holder.itemShopName.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.holo_red_dark))
//        } else {
//            holder.itemShopName.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
//        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) VIEW_TYPE_ENABLED else VIEW_TYPE_DISABLED
    }

    /**
     * ЯК ПРАЦЮЄ RECYCLER_VIEW?
     * Ми сторюємо айтеми для видимої частини + кілька зверху і зницу (ті що не видні, відправлюється в poll ViewHolder.
     * Потім ми перстаємо стоврбвати новій айтеми, а просто починаємо перевикористовувати старі. Але варто пам'ятати,
     * що значення вни стары все ще є, тож їх потрібно перезаписати.
     *
     * Коли наші ViewHolder перестають свторюватись і починаю перевикористуовуватись
     * для них будет викликатися цей метод і тут ми можемо встановити параметра за замовченням
     *
     * Коли в нас є умова для onBindViewHolder, ми або маємо писати else і вказувати там, як має себе вести
     * item коли умова не виконалась, або встановлювати параметри за замовченням
     *
     * Або ми можемо перевизначини getItemViewType, якщо в нас є кілька різних мкетів. Якщо одна умова то тип 0,
     * якщо інша, то 1, і т.д. Але не можна просто поверати position, інакше переваги RECYCLER_VIEW нівелюються,
     * бо viewHolder не буде перевикористовууватися, а будуть постійно створбватися нові
     */
//    override fun onViewRecycled(holder: ShopItemViewHolder) {
//        super.onViewRecycled(holder)
//        holder.itemShopName.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
//    }

    override fun getItemCount() = shopList.size

    inner class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemShopName: TextView = view.findViewById(R.id.itemShopName)
        val itemShopCount: TextView = view.findViewById(R.id.itemShopCount)
    }

    companion object {
        const val VIEW_TYPE_DISABLED = 0
        const val VIEW_TYPE_ENABLED = 1

        /**
         * Якщо ми перевизначили getItemViewType, то може статися так, що в пулі viewHolder у нас всі айтеми не того типу,
         * який потрібен, тож, один буде видалятися і додаватися новий, а отже це буде виникати постійно під час скролду
         * (зоч і не так часто). Це можна вирішити додавши свої значення для максимальної кількості пула кожного типу
         */
        const val MAX_POOL_SIZE = 30
    }
}
