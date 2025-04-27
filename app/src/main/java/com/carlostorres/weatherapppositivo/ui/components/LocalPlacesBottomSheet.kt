package com.carlostorres.weatherapppositivo.ui.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import com.carlostorres.weatherapppositivo.R
import com.carlostorres.weatherapppositivo.presentation.MainViewModel
import com.carlostorres.weatherapppositivo.ui.components.compose.LocalPlacesBSScreen
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocalPlacesBottomSheet(
    val viewModel: MainViewModel,
    val onMoveCameraPosition: (LatLng) -> Unit,
    val setOfflineSearchedName : (String) -> Unit,
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                LocalPlacesBSScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    viewModel = viewModel,
                    onItemClickListener = {
                        dismiss()
                    },
                    onMoveCameraPosition = { offlineLatLng ->
                        onMoveCameraPosition(offlineLatLng)
                    },
                    setOfflineSearchedName = { offlinePlaceText ->
                        setOfflineSearchedName(offlinePlaceText)
                    }
                )
            }
        }
    }

    override fun onStart() {

        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        dialog?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // Lo expande completamente
            skipCollapsed = true // Evita que se quede en "colapsado"
        }

        view?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT // Lo fuerza a pantalla completa
    }

}