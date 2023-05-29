package com.maxxxwk.sportseventsapp.presentation.models

import com.maxxxwk.sportseventsapp.domain.models.Sport
import javax.inject.Inject

class SportEventsUIMapper @Inject constructor() {
    fun mapSportsModels(
        sportsModels: List<Sport>,
        hiddenSportsIndexes: Set<Int>
    ): List<SportUIModel> {
        return sportsModels.mapIndexed { index, item ->
            SportUIModel(
                name = item.name,
                iconURL = getIconURLBySportId(item.id),
                isExpanded = !hiddenSportsIndexes.contains(index),
                events = item.events.map {
                    SportEventUIModel(
                        id = it.id,
                        name = it.name,
                        timeToStart = it.timeToStart,
                        isFavorite = it.isFavorite
                    )
                }
            )
        }
    }

    private fun getIconURLBySportId(id: String): String = when (id) {
        "FOOT" -> "https://www.flaticon.com/download/icon/1165187?icon_id=1165187&author=379&team=379&keyword=Football&pack=packs%2Ffootball-22&style=987&format=png&color=%23000000&colored=1&size=512&selection=1&premium=0&type=standard"
        "BASK" -> "https://www.flaticon.com/download/icon/10904287?icon_id=10904287&author=4874&team=4874&keyword=Ball&pack=packs%2Fsports-414&style=1369&format=png&color=&colored=1&size=512&selection=1&premium=0&type=standard"
        "TENN" -> "https://www.flaticon.com/download/icon/10771296?icon_id=10771296&author=159&team=159&keyword=Tennis+racket&pack=packs%2Fthe-great-outdoors-3&style=28&format=png&color=&colored=2&size=512&selection=1&premium=0&type=standard"
        "TABL" -> "https://www.flaticon.com/download/icon/1099535?icon_id=1099535&author=159&team=159&keyword=Table+tennis&pack=packs%2Fsports-and-games-2&style=26&format=png&color=%23000000&colored=1&size=512&selection=1&premium=0&type=standard&search=tennis"
        "HAND" -> "https://www.flaticon.com/download/icon/4893959?icon_id=4893959&author=861&team=861&keyword=Handball&pack=packs%2Fsports-ball&style=1219&format=png&color=%23000000&colored=1&size=512&selection=1&premium=0&type=standard&token=03AL8dmw8Gr53dloG43T93NSuVxrMls2XpR5KpduoTwqqF1-iKVlfWHdlSEmLXLSHvGHr-G72Zcv"
        "ESPS" -> "https://www.flaticon.com/download/icon/5515550?icon_id=5515550&author=159&team=159&keyword=Gaming&pack=packs%2Fesports-20&style=158&format=png&color=%23000000&colored=2&size=512&selection=1&premium=0&type=standard&search=esports"
        "BCHV" -> "https://www.flaticon.com/download/icon/4993681?icon_id=4993681&author=173&team=173&keyword=Volleyball&pack=packs%2Fsummer-party-64&style=1&format=png&color=%23000000&colored=1&size=512&selection=1&premium=0&type=standard&search=beach+volleyball"
        else -> "" // stub
    }
}
