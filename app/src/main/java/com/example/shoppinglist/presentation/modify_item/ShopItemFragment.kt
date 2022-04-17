package com.example.shoppinglist.presentation.modify_item

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment : Fragment() {
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var shopItemName: TextInputLayout
    private lateinit var shopItemCount: TextInputLayout
    private lateinit var editItemName: EditText
    private lateinit var editItemCount: EditText
    private lateinit var shopItemSave: Button

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    // Викликається коли фрагмент прикріпляється до Activity
    override fun onAttach(context: Context) { // context це наша Activity
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must  implement listener OnEditingFinishedListener")
        }
    }

    // Викликається коли створено Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    /**
     *  Зпочатку Fragment прикріплюється до View, потім з макету створюється View в onCreateView, момент коли View
     *  створено називається onViewCreated. Працювати з View потрібно з onViewCreated, а не з onCreate, оскільки там
     *  View ще може бути не створене
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Наповнюємо inflater макетом нашого фрагменту
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        launchRightMode()
        observeInputErrors()
        addTextChangeListeners()
        observeCloseFragment()
    }

    private fun observeCloseFragment() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("param screen mode is absent")
        }

        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("unknown param screen mode $mode")
        }

        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("param shop item id not found")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        shopItemName = view.findViewById(R.id.shop_item_name)
        shopItemCount = view.findViewById(R.id.shop_item_count)
        editItemName = view.findViewById(R.id.edit_shop_item_name)
        editItemCount = view.findViewById(R.id.edit_shop_item_count)
        shopItemSave = view.findViewById(R.id.shop_item_save)
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        shopItemSave.setOnClickListener {
            viewModel.addShopItem(editItemName.text?.toString(), editItemCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(viewLifecycleOwner) {
            editItemName.setText(it.name)
            editItemCount.setText(it.count.toString())
        }

        shopItemSave.setOnClickListener {
            viewModel.editShopItem(editItemName.text?.toString(), editItemCount.text?.toString())
        }
    }

    private fun observeInputErrors() {

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it) {
                shopItemName.error = getString(R.string.error_shop_item_edit_name)
            } else {
                shopItemName.error = null
            }
        }

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            if (it) {
                shopItemCount.error = getString(R.string.error_shop_item_edit_count)
            } else {
                shopItemCount.error = null
            }
        }

    }

    private fun addTextChangeListeners() {
        editItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        editItemCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.resetErrorInputCount()
            }

        })
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}