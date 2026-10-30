package com.electrofire.playpkm.Data.Repository

import com.electrofire.playpkm.Data.Carta
import com.electrofire.playpkm.Data.Fusion
import com.electrofire.playpkm.R
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalGameRepository @Inject constructor(
    private val timeRepository: TimeRepository
) {
    private val cartasList = listOf(
        Carta("Butterfree", R.drawable.butterfree1),
        Carta("Azumarill", R.drawable.azumarill1),
        Carta("Baltoy", R.drawable.baltoy1),
        Carta("Basculin", R.drawable.basculin1),
        Carta("Breloom", R.drawable.breloom1),
        Carta("Clauncher", R.drawable.clawitzer1),
        Carta("Electrode", R.drawable.electrode1),
        Carta("Geodude", R.drawable.geodude1),
        Carta("Gligar", R.drawable.gligar1),
        Carta("Gliscor", R.drawable.gliscor1),
        Carta("Gorebyss", R.drawable.gorebyss1),
        Carta("Granbull", R.drawable.granbull1),
        Carta("Heliolisk", R.drawable.heliolisk1),
        Carta("Huntail", R.drawable.huntail1),
        Carta("Jynx", R.drawable.jynx1),
        Carta("Lucario", R.drawable.lucario1),
        Carta("Mantine", R.drawable.mantine1),
        Carta("Mawile", R.drawable.mawile1),
        Carta("Meloetta", R.drawable.meloetta1),
        Carta("Morpeko", R.drawable.morpeko1),
        Carta("Munna", R.drawable.munna1),
        Carta("Palossand", R.drawable.palossand1),
        Carta("Pampour", R.drawable.pampour1),
        Carta("Pikachu", R.drawable.pikachu1),
        Carta("Plusle", R.drawable.plusle1),
        Carta("Primeape", R.drawable.primeape1),
        Carta("Qwilfish", R.drawable.qwilfish1),
        Carta("Rillaboom", R.drawable.rillaboom1),
        Carta("Shinx", R.drawable.shinx1),
        Carta("Shroomish", R.drawable.shroomish1),
        Carta("Sigilyph", R.drawable.sigyliph1),
        Carta("Sizzlipede", R.drawable.sizzlipede1),
        Carta("Magcargo", R.drawable.slugma1),
        Carta("Staryu", R.drawable.staryu1),
        Carta("Steelix", R.drawable.stealix1),
        Carta("Swampert", R.drawable.swampert1),
        Carta("Totodile", R.drawable.totodile1),
        Carta("Tsareena", R.drawable.tsarena1),
        Carta("Victini", R.drawable.victini1),
        Carta("Zeraora", R.drawable.zeraora1)
    )

    private val fusionesList = listOf(
        Fusion(0, R.drawable.fsgengarklang, arrayListOf("Gengar", "Klang")),
        Fusion(1, R.drawable.fssnorlaxazumarill, arrayListOf("Snorlax", "Azumarill")),
        Fusion(2, R.drawable.fsgolbatraikou, arrayListOf("Golbat", "Raikou")),
        Fusion(3, R.drawable.fsfabaltoyshinx, arrayListOf("Baltoy", "Shinx")),
        Fusion(4, R.drawable.fsskrelpseaking, arrayListOf("Skrelp", "Seaking")),
        Fusion(5, R.drawable.fsampharosdugtrio, arrayListOf("Ampharos", "Dugtrio")),
        Fusion(6, R.drawable.fsampharoskyurem, arrayListOf("Ampharos", "Kyurem")),
        Fusion(7, R.drawable.fsbeautiflynoctowl, arrayListOf("Beautyfly", "Noctowl")),
        Fusion(8, R.drawable.fscastformswilrix, arrayListOf("Castform", "swirlix")),
        Fusion(9, R.drawable.fscrobatdragalge, arrayListOf("crobat", "dragalge")),
        Fusion(10, R.drawable.fsdeinotrevenant, arrayListOf("deino", "trevenant")),
        Fusion(11, R.drawable.fselektroscofagrius, arrayListOf("eelektross", "cofagrigus")),
        Fusion(12, R.drawable.fsexploudgolisopod, arrayListOf("exploud", "golisopod")),
        Fusion(13, R.drawable.fsflareonhariyama, arrayListOf("flareon", "hariyama")),
        Fusion(14, R.drawable.fsflygoncrawdaunt, arrayListOf("flygon", "crawdaunt")),
        Fusion(15, R.drawable.fshakamogarchomp, arrayListOf("hakamo-o", "garchomp")),
        Fusion(16, R.drawable.fshoothootbibarel, arrayListOf("Charizard", "Tyrunt")),
        Fusion(17, R.drawable.fshoundoomgreninja, arrayListOf("houndoom", "greninja")),
        Fusion(18, R.drawable.fskrabbydrifloon, arrayListOf("krabby", "drifloon")),
        Fusion(19, R.drawable.fslycanrocaerodactyl, arrayListOf("lycanroc", "aerodactyl")),
        Fusion(20, R.drawable.fsmoltresgroudon, arrayListOf("moltres", "groudon")),
        Fusion(21, R.drawable.fspalkiavolcarona, arrayListOf("palkia", "volcarona")),
        Fusion(22, R.drawable.fssligoosneasel, arrayListOf("sligoo", "sneasel")),
        Fusion(23, R.drawable.fssnorlaxseel, arrayListOf("snorlax", "seel")),
        Fusion(24, R.drawable.fssphealpsyduck, arrayListOf("spheal", "psyduck")),
        Fusion(25, R.drawable.fsstuffulmachamp, arrayListOf("stufful", "machamp")),
        Fusion(26, R.drawable.fssunkerngastrodon, arrayListOf("sunkern", "gastrodon")),
        Fusion(27, R.drawable.fssunkernambipom, arrayListOf("sunkern", "ambipom")),
        Fusion(28, R.drawable.fsweavilechesnaught, arrayListOf("weavile", "chesnaught")),
        Fusion(29, R.drawable.fssunkernsuicune, arrayListOf("sunkern", "suicune")),
    )

    private suspend fun getDiaDelAnio(): Int {
        val horaServidor = timeRepository.obtenerHoraServidor() ?: Date()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = horaServidor
        return calendar.get(Calendar.DAY_OF_YEAR)
    }

    suspend fun getCartaDelDia(): Carta {
        val dia = getDiaDelAnio()
        val index = dia % cartasList.size
        return cartasList[index]
    }

    suspend fun getFusionDelDia(): Fusion {
        val dia = getDiaDelAnio()
        val index = dia % fusionesList.size
        return fusionesList[index]
    }
}
