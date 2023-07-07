package olmo.wellness.android.ui.livestream.view.ar

import olmo.wellness.android.R


val getMasks = arrayListOf<String>().apply {
    add("none")
    add("aviators")
    add("bigmouth")
    add("dalmatian")
    add("flowers")
    add("koala")
    add("lion")
    add("smallface")
    add("teddycigar")
    add("background_segmentation")
    //
    add("tripleface")
    add("sleepingmask")
    add("fatify")
    add("mudmask")
    //
    add("pug")
    add("twistedface")
    add("grumpycat")
    add("Helmet_PBR_V1")
}

val getEffects = arrayListOf<String>().apply {
    add("none")
    add("fire")
    add("rain")
    add("heart")
    add("blizzard")
}

val getFilters = arrayListOf<String>().apply {
    add("none")
    add("filmcolorperfection")
    add("tv80")
    add("drawingmanga")
    add("sepia")
            //
    add("bleachbypass")
}

val getMasksFilters = arrayListOf<FilterAR>().apply {
    add(FilterAR("none","Original", R.drawable.ic_original,TypeFilter.MASK))
//    add(FilterAR("galaxy_background.deepar","Universe",R.drawable.ic_universe,TypeFilter.MASK))
    add(FilterAR("MakeupLook.deepar","Universe",R.drawable.ic_universe,TypeFilter.MASK))
    add(FilterAR("flowers","Flower",R.drawable.ic_flower,TypeFilter.MASK))
    add(FilterAR("mudmask","Mudmask",R.drawable.ic_mudmask,TypeFilter.MASK))
    add(FilterAR("sepia","Sepia",R.drawable.ic_sepia,TypeFilter.FILTER))
}


