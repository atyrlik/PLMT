package me.atyrlik.plmtalexandre.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.plumelabs.lib.bluetooth.MeasurementType
import me.atyrlik.plmtalexandre.R
import me.atyrlik.plmtalexandre.databinding.MainFragmentBinding
import me.atyrlik.plmtalexandre.entities.FlowMeasure
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

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
            xAxis.isGranularityEnabled = true
            // I'm not sure if it's better to have a hard min/max or not.
            // Let's disable it for now.
            //axisLeft.axisMinimum = 0f
            //axisLeft.axisMaximum = 150f
            setTouchEnabled(true)
        }

        binding.switchVoc.setOnCheckedChangeListener { _, _ -> viewModel.measures.value?.let{ plotMeasures(it) } }
        binding.switchNo2.setOnCheckedChangeListener { _, _ -> viewModel.measures.value?.let{ plotMeasures(it) } }
        binding.switchPm10.setOnCheckedChangeListener { _, _ -> viewModel.measures.value?.let{ plotMeasures(it) } }
        binding.switchPm25.setOnCheckedChangeListener { _, _ -> viewModel.measures.value?.let{ plotMeasures(it) } }

        viewModel.measures.observe(viewLifecycleOwner, Observer { measures ->
            plotMeasures(measures)
        })
    }

    private fun plotMeasures(measures: List<FlowMeasure>) {
        if (measures.isEmpty()) return

        // filter measures by type
        val measuresVOC = measures.filter { it.type == MeasurementType.VOC }
        val measuresNO2 = measures.filter { it.type == MeasurementType.NO2 }
        val measuresPM10 = measures.filter { it.type == MeasurementType.PM10 }
        val measuresPM25 = measures.filter { it.type == MeasurementType.PM25 }

        // update headers values
        measuresVOC.lastOrNull()?.aqi?.let { voc ->
            binding.measureVoc.text = getString(R.string.voc_measure, voc.roundToInt())
            binding.measureVoc.setBackgroundColor(if (voc > 50) Color.RED else Color.GREEN)
        }
        measuresNO2.lastOrNull()?.aqi?.let { no2 ->
            binding.measureNo2.text = getString(R.string.no2_measure, no2.roundToInt())
            binding.measureNo2.setBackgroundColor(if (no2 > 50) Color.RED else Color.GREEN)
        }
        measuresPM10.lastOrNull()?.aqi?.let { pm10 ->
            binding.measurePm10.text = getString(R.string.pm10_measure, pm10.roundToInt())
            binding.measurePm10.setBackgroundColor(if (pm10 > 50) Color.RED else Color.GREEN)
        }
        measuresPM25.lastOrNull()?.aqi?.let { pm25 ->
            binding.measurePm25.text = getString(R.string.pm25_measure, pm25.roundToInt())
            binding.measurePm25.setBackgroundColor(if (pm25 > 50) Color.RED else Color.GREEN)
        }

        // create datasets
        val datasetVOC = ScatterDataSet(measuresVOC.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }, "VOC").apply {
            color = Color.GREEN
        }
        val datasetN02 = ScatterDataSet(measuresNO2.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }, "NO2").apply {
            color = Color.BLUE
        }
        val datasetPM10 = ScatterDataSet(measuresPM10.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }, "PM10").apply {
            color = Color.RED
        }
        val datasetPM25 = ScatterDataSet(measuresPM25.map { Entry(it.timestamp!!.toFloat(), it.aqi!!) }, "PM25").apply {
            color = Color.YELLOW
        }

        // update chart
        binding.chart.data = ScatterData().apply {
            if (binding.switchVoc.isChecked) addDataSet(datasetVOC)
            if (binding.switchNo2.isChecked) addDataSet(datasetN02)
            if (binding.switchPm10.isChecked) addDataSet(datasetPM10)
            if (binding.switchPm25.isChecked) addDataSet(datasetPM25)
        }

        binding.chart.notifyDataSetChanged()
        binding.chart.invalidate()

        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            private val mFormat: SimpleDateFormat = SimpleDateFormat("d/M HH:mm", Locale.ENGLISH)

            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(value.toLong() * 1000))
            }
        }
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