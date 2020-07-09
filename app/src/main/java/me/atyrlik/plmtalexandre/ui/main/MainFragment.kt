package me.atyrlik.plmtalexandre.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.plumelabs.lib.bluetooth.MeasurementType
import me.atyrlik.plmtalexandre.databinding.MainFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!! // this is from the Android developer guideline

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding.chart) {
            xAxis.granularity = 600f
            xAxis.isGranularityEnabled = true
            axisLeft.axisMinimum = 0f
            axisLeft.axisMaximum = 150f // if values are over 150, a special case could be added. For now, it helps visualizing the data.
            setTouchEnabled(true)
            animateXY(2000, 0)
        }

        viewModel.measures.observe(viewLifecycleOwner, Observer { measures ->

            val datasetVOC = ScatterDataSet(
                measures.filter { it.type == MeasurementType.VOC }.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }
                , "NO2").apply {
                color = Color.GREEN
            }

            val datasetN02 = ScatterDataSet(
                measures.filter { it.type == MeasurementType.NO2 }.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }
                , "NO2").apply {
                color = Color.BLUE
            }

            val datasetPM10 = ScatterDataSet(
                measures.filter { it.type == MeasurementType.PM10 }.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }
                , "PM10").apply {
                color = Color.RED
            }

            val datasetPM25 = ScatterDataSet(
                measures.filter { it.type == MeasurementType.PM25 }.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }
                , "PM25").apply {
                color = Color.YELLOW
            }

            binding.chart.data = ScatterData(datasetVOC, datasetN02, datasetPM10, datasetPM25)

            binding.chart.notifyDataSetChanged()
            binding.chart.invalidate()

            binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
                private val mFormat: SimpleDateFormat = SimpleDateFormat("d/M HH:mm", Locale.ENGLISH)

                override fun getFormattedValue(value: Float): String {
                    return mFormat.format(Date(value.toLong() * 1000))
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.startRegisteringMeasure()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}