package olmo.wellness.android.ui.livestream.view.ar

data class FilterAR( val name : String,val title :String ,val image : Int, val type : TypeFilter)


enum class TypeFilter{
    MASK,EFFECT,FILTER
}