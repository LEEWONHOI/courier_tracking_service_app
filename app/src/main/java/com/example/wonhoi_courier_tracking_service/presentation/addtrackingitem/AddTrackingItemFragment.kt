package com.example.wonhoi_courier_tracking_service.presentation.addtrackingitem

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.wonhoi_courier_tracking_service.data.entity.ShippingCompany
import com.example.wonhoi_courier_tracking_service.databinding.FragmentAddTrackingItemBinding
import com.example.wonhoi_courier_tracking_service.extension.toGone
import com.example.wonhoi_courier_tracking_service.extension.toVisible
import com.google.android.material.chip.Chip
import org.koin.android.scope.ScopeFragment

class AddTrackingItemFragment : ScopeFragment(), AddTrackingItemContract.View {

    override val presenter: AddTrackingItemContract.Presenter by inject()

    private var binding: FragmentAddTrackingItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddTrackingItemBinding.inflate(inflater)
        .also {
            binding = it
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
        presenter.onViewCreated()

        changeInvoiceIfAvailable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toVisible()
    }

    override fun hideShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toGone()
    }

    override fun showRecommendCompanyLoadingIndicator() {
        binding?.recommendProgressBar?.toVisible()
    }

    override fun hideRecommendCompanyLoadingIndicator() {
        binding?.recommendProgressBar?.toGone()
    }

    override fun showSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = null
            isEnabled = false
        }
        binding?.saveProgressBar?.toVisible()
    }

    override fun hideSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = "SAVE"
            isEnabled = true
        }
        binding?.saveProgressBar?.toGone()
    }

    override fun showCompanies(companies: List<ShippingCompany>) {
        companies.forEach { company ->
            binding?.chipGroup?.addView(
                Chip(context).apply {
                    text = company.name
                }
            )
        }
    }

    override fun showRecommendCompany(company: ShippingCompany) {
        binding?.chipGroup
            ?.children
            ?.filterIsInstance(Chip::class.java)    // 타입이 chip 걸로 필터링한다.
            ?.forEach { chip ->
                if (chip.text == company.name) {    // chip 이 추천한 company 와 동일한 경우 선택
                    binding?.chipGroup?.apply {
                        check(chip.id)
                    }
                    return@forEach
                }
            }
    }

    override fun enableSaveButton() {
        binding?.saveButton?.isEnabled = true
    }

    override fun disableSaveButton() {
        binding?.saveButton?.isEnabled = false
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    private fun bindView() {
        binding?.chipGroup?.setOnCheckedChangeListener { group, checkedId ->
            presenter.changeSelectedShippingCompany(group.findViewById<Chip>(checkedId).text.toString())
        }
        binding?.invoiceEditText?.addTextChangedListener { editable ->
            presenter.changeShippingInvoice(editable.toString())
        }
        binding?.saveButton?.setOnClickListener { _ ->
            presenter.saveTrackingItem()
        }
    }

    private fun changeInvoiceIfAvailable() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invoice = clipboard.plainTextClip()
        if (!invoice.isNullOrBlank()) {
            AlertDialog.Builder(requireActivity())
                .setTitle("add $invoice  on clipboard as tracking number?")
                .setPositiveButton("add it") { _, _ ->
                    binding?.invoiceEditText?.setText(invoice)
                    presenter.fetchRecommendShippingCompany()
                }
                .setNegativeButton("no") { _, _ -> }
                .create()
                .show()
        }
    }


    private fun ClipboardManager.plainTextClip(): String? =
        if (hasPrimaryClip() && (primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)) {
            primaryClip?.getItemAt(0)?.text?.toString()
        } else {
            null
        }

    private fun hideKeyboard() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

}
