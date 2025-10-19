package com.example.phoneinfo.ui.dashboard

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phoneinfo.R
import com.example.phoneinfo.databinding.FragmentDashboardBinding
import java.io.File
import java.text.DecimalFormat

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var batteryReceiver: BroadcastReceiver
    private val handler = Handler(Looper.getMainLooper())
    private var lastIdleTime: Long = 0
    private var lastTotalTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ramUsageTextView.text = getRamUsage()
        binding.storageUsageTextView.text = getStorageUsage()
    }

    private fun updateCpuUsage() {
        val cpuUsage = getCpuUsage()
        if (cpuUsage != "N/A") {
            binding.cpuUsageTextView.text = getString(R.string.cpu_usage, cpuUsage)
        }
        handler.postDelayed({ updateCpuUsage() }, 1000)
    }

    private fun getCpuUsage(): String {
        try {
            val reader = File("/proc/stat").bufferedReader()
            val cpuLine = reader.readLine()
            reader.close()
            val parts = cpuLine.split(" ")
            val totalTime = parts.subList(2, parts.size).sumOf { it.toLong() }
            val idleTime = parts[5].toLong()

            val totalDiff = totalTime - lastTotalTime
            val idleDiff = idleTime - lastIdleTime

            lastTotalTime = totalTime
            lastIdleTime = idleTime

            if (totalDiff == 0L) return "0"

            val usage = 100 * (totalDiff - idleDiff) / totalDiff
            return usage.toString()
        } catch (e: Exception) {
            return "N/A"
        }
    }

    private fun getRamUsage(): String {
        val activityManager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val totalRam = memoryInfo.totalMem / (1024 * 1024)
        val usedRam = totalRam - (memoryInfo.availMem / (1024 * 1024))
        return getString(R.string.ram_usage, usedRam.toString(), totalRam.toString())
    }

    private fun getStorageUsage(): String {
        val stat = StatFs(Environment.getDataDirectory().path)
        val totalBytes = stat.blockCountLong * stat.blockSizeLong
        val freeBytes = stat.availableBlocksLong * stat.blockSizeLong
        val usedBytes = totalBytes - freeBytes
        val df = DecimalFormat("#.##")
        return getString(
            R.string.storage_usage,
            df.format(usedBytes / (1024.0 * 1024.0 * 1024.0)),
            df.format(totalBytes / (1024.0 * 1024.0 * 1024.0))
        )
    }

    private fun registerBatteryReceiver() {
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                val batteryPct = level * 100 / scale.toFloat()
                binding.batteryLevelTextView.text =
                    getString(R.string.battery_level, batteryPct.toString())
            }
        }
        requireActivity().registerReceiver(
            batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    override fun onResume() {
        super.onResume()
        registerBatteryReceiver()
        handler.postDelayed({ updateCpuUsage() }, 1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        requireActivity().unregisterReceiver(batteryReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}