package me.atyrlik.plmtalexandre.repository

import com.plumelabs.lib.bluetooth.FlowBleClient
import com.plumelabs.lib.bluetooth.Measure
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import me.atyrlik.plmtalexandre.entities.FlowMeasure
import me.atyrlik.plmtalexandre.repository.local.MeasuresDatabase

interface FlowRepository {
    /**
     * Start reading measures from the flowClient.
     * Every measure is stored in database.
     */
    suspend fun registerFlowMeasures()

    /**
     * Get the list of all measures and subscribe to changes.
     */
    fun getMeasures(): Flow<List<FlowMeasure>>
}

class FlowRepositoryImpl(
    private val db: MeasuresDatabase,
    private val flowClient: FlowBleClient
) : FlowRepository {

    override suspend fun registerFlowMeasures() {
        val feed = callbackFlow {
            flowClient.connect { client, error ->
                // I'm not sure what is the correct behaviour in case of error.
                // Let's just close the connection.
                error?.let { close() }

                client.sync(
                    onData = { flowBleClient: FlowBleClient, list: List<Measure> ->
                        // format data for db storage
                        list.map {
                            FlowMeasure(
                                uid = 0,
                                aqi = it.aqi,
                                timestamp = it.timestamp,
                                type = it.type
                            )
                        }.forEach { offer(it) }
                    }
                )
            }
            awaitClose{ flowClient.disconnect() }
        }

        feed.collect {
            db.flowDao().insert(it)
        }
    }

    override fun getMeasures(): Flow<List<FlowMeasure>> {
        return db.flowDao().getAll().distinctUntilChanged()
    }
}