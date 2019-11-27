package com.vitovalov.exchangerateslive.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vitovalov.exchangerateslive.data.local.model.UserChangesEntity
import com.vitovalov.exchangerateslive.data.local.model.UserChangesModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.util.*

@RunWith(AndroidJUnit4::class)
class UserChangesLocalDatasourceTest {

    private lateinit var userChangesDb: RoomUserChangesDb
    private lateinit var localDatasource: UserChangesLocalDatasource

    private val startBaseCurrency = Currency.getInstance("EUR")
    private val startBaseCurrencyAmount = "100.0"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        //Initialize Room databaseUser
        userChangesDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            RoomUserChangesDb::class.java
        )
            .allowMainThreadQueries()
            .build()

        //Prepopulate first record
        userChangesDb.userChangesDao()
            .insert(UserChangesEntity(1, startBaseCurrency.currencyCode, startBaseCurrencyAmount))
            .blockingAwait()

        localDatasource = UserChangesRoomDatasource(userChangesDb)
    }

    @After
    fun closeDb() {
        userChangesDb.close()
    }

    @Test
    fun whenRunningObservationWithNoWriteActionsShouldReturnUserCurrencySelectionWithPrepopulatedData() {
        //when
        val testObserver = localDatasource
            .observeUserChanges()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValue(
                UserChangesModel(
                    startBaseCurrency,
                    BigDecimal(startBaseCurrencyAmount)
                )
            )
    }

    @Test
    fun whenUpdatingCurrencyAmountShouldReturnUserCurrencySelectionWithChangedAmount() {
        //given
        val newCurrencyAmount = BigDecimal("200.0")
        localDatasource
            .updateUserAmount(newCurrencyAmount)
            .blockingAwait()

        //when
        val testObserver = localDatasource
            .observeUserChanges()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValueAt(0, UserChangesModel(startBaseCurrency, newCurrencyAmount))
    }

    @Test
    fun whenUpdatingCurrencyBaseShouldReturnUserCurrencySelectionWithChangedBase() {
        //given
        val newCurrencyBase = Currency.getInstance("USD")
        localDatasource
            .updateUserBaseCurrency(newCurrencyBase)
            .blockingAwait()

        //when
        val testObserver = localDatasource
            .observeUserChanges()
            .test()

        //then
        testObserver
            .awaitCount(1)
            .assertValueCount(1)
            .assertValueAt(
                0,
                UserChangesModel(newCurrencyBase, BigDecimal(startBaseCurrencyAmount))
            )
    }
}