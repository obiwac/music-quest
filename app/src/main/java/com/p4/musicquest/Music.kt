package com.p4.musicquest

data class Music(val iconResId: Int, val audioResId: Int)


val pianoMusic =Music(R.drawable.piano_icon, R.raw.piano_music_quest)
val guitareMusic =Music(R.drawable.guitare_icon, R.raw.guitare_music_quest)
val fluteMusic =Music(R.drawable.flute_icon, R.raw.flute_music_quest)
val trompetteMusic =Music(R.drawable.trumpet_icon, R.raw.trompette_music_quest)