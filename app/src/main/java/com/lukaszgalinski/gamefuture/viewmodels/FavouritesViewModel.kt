package com.lukaszgalinski.gamefuture.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lukaszgalinski.gamefuture.models.GamesModel
import com.lukaszgalinski.gamefuture.repositories.DatabaseRepository
import io.reactivex.disposables.Disposable

class FavouritesViewModel: ViewModel() {
    private var gamesLiveData: MutableLiveData<List<GamesModel>>? = null
    private var mRepo: DatabaseRepository? = null

    fun init(context: Context){
        if (gamesLiveData != null ){
            return
        }
        mRepo = DatabaseRepository().getInstance()
        gamesLiveData = mRepo?.getFavouriteGames(context)
    }

    fun getGames(): LiveData<List<GamesModel>>? {
        return gamesLiveData
    }

    fun changeStatus(context: Context, gameId: Int, status: Boolean, position: Int): Disposable?{
        gamesLiveData?.value!![position].favourite = status
        return mRepo?.changeFavouriteStatus(context, gameId, status)
    }

    fun forceDataUpdating(context: Context): ArrayList<GamesModel>?{
        val data = mRepo?.updateDataFromHttp(context)
        gamesLiveData?.postValue(data)
        return data
    }
}