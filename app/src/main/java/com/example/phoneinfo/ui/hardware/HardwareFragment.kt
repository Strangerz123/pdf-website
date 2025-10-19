package com.example.phoneinfo.ui.hardware

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phoneinfo.databinding.FragmentHardwareBinding
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class HardwareFragment : Fragment() {

    private var _binding: FragmentHardwareBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHardwareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = HardwareInfoAdapter(getHardwareInfo())
    }

    private fun getHardwareInfo(): List<HardwareInfo> {
        return listOf(
            HardwareInfo("Model", Build.MODEL),
            HardwareInfo("Manufacturer", Build.MANUFACTURER),
            HardwareInfo("Brand", Build.BRAND),
            HardwareInfo("Device", Build.DEVICE),
            HardwareInfo("Product", Build.PRODUCT),
            HardwareInfo("Hardware", Build.HARDWARE),
            HardwareInfo("Board", Build.BOARD),
            HardwareInfo("Android Version", Build.VERSION.RELEASE),
            HardwareInfo("API Level", Build.VERSION.SDK_INT.toString()),
            HardwareInfo("Build ID", Build.ID),
            HardwareInfo("Build Time", Build.TIME.toString()),
            HardwareInfo("Fingerprint", Build.FINGERPRINT),
            HardwareInfo("Kernel Version", System.getProperty("os.version") ?: "N/A"),
            HardwareInfo("CPU ABI", Build.CPU_ABI),
            HardwareInfo("CPU ABI2", Build.CPU_ABI2),
            HardwareInfo("Total RAM", getTotalRam()),
        )
    }

    private fun getTotalRam(): String {
        return try {
            val reader = BufferedReader(InputStreamReader(FileInputStream("/proc/meminfo")), 1000)
            val line = reader.readLine()
            reader.close()
            val mem = line.split(Regex("\\s+"))[1].toLong() / 1024
            "$mem MB"
        } catch (e: Exception) {
            "N/A"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}