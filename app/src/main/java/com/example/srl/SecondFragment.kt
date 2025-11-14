package com.example.srl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.srl.databinding.FragmentSecondBinding

/**
 * Un Fragmento simple que actúa como el segundo destino en la navegación.
 */
class SecondFragment : Fragment() {

    // Variable para gestionar el View Binding, que nos permite acceder a las vistas del diseño de forma segura.
    private var _binding: FragmentSecondBinding? = null

    // Esta propiedad nos asegura que el binding no sea nulo cuando lo usemos.
    // Solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    /**
     * Esta función se llama para crear la vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Infla (crea) la vista del fragmento usando el View Binding y la asigna a la variable _binding.
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        // Devuelve la vista raíz del diseño para que se muestre en pantalla.
        return binding.root

    }

    /**
     * Esta función se llama justo después de que la vista del fragmento ha sido creada.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura un listener para el botón 'buttonSecond'.
        binding.buttonSecond.setOnClickListener {
            // Cuando se pulsa el botón, navega de vuelta al FirstFragment.
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    /**
     * Esta función se llama cuando la vista del fragmento está a punto de ser destruida.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Limpia la referencia al binding para evitar fugas de memoria.
        _binding = null
    }
}